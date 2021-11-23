package testCases;

import commons.BaseTest;
import data.Environments;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pojo.ExcelData;
import util.RandomUtil;

import java.util.List;

public class RechageTest extends BaseTest {
    @BeforeClass
    public void beforeClass(){
        String phone = RandomUtil.getPhone();
        Environments.EnvData.put("phone",phone);

        List<ExcelData> list = readSpecifyExcel(2,2,0);
        ExcelData excelDataLogin = list.get(0);
        parameterReplace(excelDataLogin);
        Response res = request(excelDataLogin);
        assertWithDb(excelDataLogin);
        assertWithExpected(excelDataLogin,res);
        putEnvironments(excelDataLogin,res);

        ExcelData excelDataRegister = list.get(1);
        parameterReplace(excelDataRegister);
        Response res1 = request(excelDataRegister);
        assertWithDb(excelDataRegister);
        assertWithExpected(excelDataRegister,res1);
        putEnvironments(excelDataRegister,res1);




    }
    @Test(dataProvider = "getRecharge")
    public void recharge(ExcelData excelData){
        v3Token();

        parameterReplace(excelData);
        Response res = request(excelData);
        assertWithDb(excelData);
        assertWithExpected(excelData,res);

    }
    @DataProvider
    public Object[] getRecharge(){
        List<ExcelData> list = readSpecifyExcel(2,2);
        return list.toArray();

    }
}
