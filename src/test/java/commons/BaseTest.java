package commons;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSONObject;
import com.lemon.encryption.EncryptUtils;
import data.Constant;
import data.Environments;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import pojo.ExcelData;
import util.DBUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.*;

public class BaseTest {

    @BeforeTest
    public void setUp(){
        baseURI=Constant.PROJECT_PATH;
    }

    public  List<ExcelData> readAllExcel(int sheetNum){
            File file = new File(Constant.EXCEL_PATH);
            ImportParams importParams = new ImportParams();
            importParams.setStartSheetIndex(sheetNum);
            return  ExcelImportUtil.importExcel(file,ExcelData.class,importParams);

    }

    public List<ExcelData> readSpecifyExcel(int sheetNum,int readRow,int startRow)
    {
        File file = new File(Constant.EXCEL_PATH);
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(sheetNum);
        importParams.setStartRows(startRow);
        importParams.setReadRows(readRow);
        return ExcelImportUtil.importExcel(file,ExcelData.class,importParams);

    }

    public List<ExcelData> readSpecifyExcel(int sheetNum,int startRow)
    {
        File file = new File(Constant.EXCEL_PATH);
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(sheetNum);
        importParams.setStartRows(startRow);
        return ExcelImportUtil.importExcel(file,ExcelData.class,importParams);

    }

    /**
     * 发送请求，接收响应。1.请求头转化为map格式，方便发送 2.根据method来发送对应请求
     * @param excelData
     * @return
     */

    public Response request(ExcelData excelData){

        String logPth = System.getProperty("user.dir")+"//log//"+excelData.getMoudle();
        File file = new File(logPth);

        if(!file.exists()){
            file.mkdirs();
        }

        logPth = logPth+"//test_"+excelData.getCaseId()+".log";

        if(!Constant.LOG_IN_CONSOLE) {

            PrintStream printStream = null;
            try {
                printStream = new PrintStream(logPth);
                RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(printStream));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }



        Map<String,Object> map = JSONObject.parseObject(excelData.getResHead());
        String method = excelData.getMethod();
        Response res = null;
        if("get".equalsIgnoreCase(method)){
            res = given().log().all().headers(map).when().get(excelData.getUrl()).then().log().all().extract().response();
        }else if("post".equalsIgnoreCase(method)){
            res = given().log().all().headers(map).body(excelData.getInputParams()).when().post(excelData.getUrl()).then().log().all().extract().response();
        }else if("patch".equalsIgnoreCase(method)){
            res = given().log().all().headers(map).body(excelData.getInputParams()).when().patch(excelData.getUrl()).then().log().all().extract().response();
        }

        if(!Constant.LOG_IN_CONSOLE){
            try {
                Allure.addAttachment("接口请求返回信息", new FileInputStream(logPth));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        return res;
    }

    /**
     * 用正则表达式(.*?)来匹配需要替换的参数，并从环境变量中取出实际值来替换
     * @param orgStr 原始字符串，例如入参、请求头等
     * @return
     */

    public  String regexDeal(String orgStr) {
        String result = orgStr;
        if(result!=null) {

            Pattern pattern = Pattern.compile("\\{\\{(.*?)}}");
            Matcher matcher = pattern.matcher(orgStr);
            while (matcher.find()) {
                //group（1）取出{{}}里的值，group（0）是包含{{}}的值
                String str1 = matcher.group(0);
                String key = matcher.group(1);

                result = result.replace(str1, Environments.EnvData.get(key) + "");
            }
        }
        return result;
    }

    /**
     * 替换excel表格case的参数{{}}为实际值，并赋值给excel
     * @param excelData
     */
    public void  parameterReplace(ExcelData excelData){
        //替换请求头
        String header = excelData.getResHead();
        header = regexDeal(header);
        excelData.setResHead(header);
        //替换入参
        String input = excelData.getInputParams();
        input = regexDeal(input);
        excelData.setInputParams(input);
        //替换期望返回
        String expected = excelData.getExpected();
        expected = regexDeal(expected);
        excelData.setExpected(expected);
        //替换数据库返回结果
        String dbResult = excelData.getDbresult();
        dbResult = regexDeal(dbResult);
        excelData.setDbresult(dbResult);

    }

    public  void assertWithExpected(ExcelData excelData,Response res) {
        Map<String, Object> expectedMap = JSONObject.parseObject(excelData.getExpected());
        for (String key : expectedMap.keySet()) {
            String expected = expectedMap.get(key)+"";
            String actual = res.jsonPath().get(key)+"";
            Assert.assertEquals(actual, expected);
        }
    }



    public  void assertWithDb(ExcelData excelData){
        Map<String,Object> expectedMap =JSONObject.parseObject(excelData.getDbresult());
        if(expectedMap!=null)
        for(String key:expectedMap.keySet())
        {
            String expected = expectedMap.get(key)+"";
            String actual = DBUtil.queryOneField(key)+"";
            Assert.assertEquals(actual,expected);
        }


    }

    public void putEnvironments(ExcelData excelData,Response res){
        Map<String,Object> map = JSONObject.parseObject(excelData.getExtract());
        if(map!=null) {
            for (String key : map.keySet()) {
                String path = map.get(key).toString();
                Object value = res.jsonPath().get(path);

                Environments.EnvData.put(key, value);
            }
        }



    }

    public  void v3Token(){
        long timestamp = System.currentTimeMillis()/1000;
        String token = Environments.EnvData.get("token").toString();
        String str = token.substring(0,50)+timestamp+"";
        String sign = EncryptUtils.rsaEncrypt(str);
        Environments.EnvData.put("timestamp",timestamp);
        Environments.EnvData.put("sign",sign);
    }







}
