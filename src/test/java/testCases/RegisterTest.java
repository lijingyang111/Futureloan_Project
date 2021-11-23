package testCases;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import commons.BaseTest;
import data.Environments;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pojo.ExcelData;
import util.RandomUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class RegisterTest extends BaseTest {

    @BeforeClass
    public void beforeClass(){
        String phone1 = RandomUtil.phoneUtil();
        String phone2 = RandomUtil.phoneUtil();
        String phone3 = RandomUtil.phoneUtil();
        String phone4 = RandomUtil.phoneUtil();
        Environments.EnvData.put("phone1",phone1);
        Environments.EnvData.put("phone2",phone2);
        Environments.EnvData.put("phone3",phone3);
        Environments.EnvData.put("phone4",phone4);

    }

    @Test(dataProvider = "getRegisterDta")
    public void register(ExcelData excelData){
        parameterReplace(excelData);
        Response res = request(excelData);
        assertWithExpected(excelData,res);
        assertWithDb(excelData);
    }

    @DataProvider
    public Object[] getRegisterDta(){
        List<ExcelData> list  =  readAllExcel(0);
        return list.toArray();

    }





}
