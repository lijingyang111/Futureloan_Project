package pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class ExcelData {

    @Excel(name = "序号(caseId)")
    private String caseId;
    @Excel(name = "接口模块(module)")
    private String moudle;
    @Excel(name = "用例标题(title)")
    private String title;
    @Excel(name = "请求头(requestHeader)")
    private String resHead;
    @Excel(name = "请求方式(method)")
    private String method;
    @Excel(name = "接口地址(url)")
    private String url;
    @Excel(name = "参数输入(inputParams)")
    private String inputParams;
    @Excel(name = "期望返回结果(expected)")
    private String expected;
    @Excel(name = "提取路径（extract）")
    private String extract;
    @Excel(name = "数据库返回结果(dbresult)")
    private String dbresult;

    @Override
    public String toString() {
        return "ExcelData{" +
                "caseId='" + caseId + '\'' +
                ", moudle='" + moudle + '\'' +
                ", title='" + title + '\'' +
                ", resHead='" + resHead + '\'' +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", inputParams='" + inputParams + '\'' +
                ", expected='" + expected + '\'' +
                ", extract='" + extract + '\'' +
                ", dbresult='" + dbresult + '\'' +
                '}';
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getMoudle() {
        return moudle;
    }

    public void setMoudle(String moudle) {
        this.moudle = moudle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResHead() {
        return resHead;
    }

    public void setResHead(String resHead) {
        this.resHead = resHead;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInputParams() {
        return inputParams;
    }

    public void setInputParams(String inputParams) {
        this.inputParams = inputParams;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public String getDbresult() {
        return dbresult;
    }

    public void setDbresult(String dbresult) {
        this.dbresult = dbresult;
    }


}
