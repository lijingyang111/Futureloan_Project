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

public class LoginTest extends BaseTest {

    @BeforeClass
    public void beforeClass(){
        String phone = RandomUtil.getPhone();
        Environments.EnvData.put("phone",phone);
        List<ExcelData> list = readSpecifyExcel(1,1,0);
        parameterReplace(list.get(0));
        request(list.get(0));

    }

    @Test(dataProvider = "getLoginCase")
    public void login(ExcelData excelData){
        parameterReplace(excelData);
        Response res = request(excelData);
        assertWithExpected(excelData,res);
        assertWithDb(excelData);

    }

    @DataProvider
    public Object[] getLoginCase(){
        List<ExcelData> list = readSpecifyExcel(1,1);
        return list.toArray();

    }


}
