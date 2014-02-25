package com.knight.excel;

/**
 * Created by NEO on 13-12-13.
 */
public class Result {
    private String reportInfodata;
    private String areaInfodata;
    private String constructInfodata;

    public Result(String reportInfodata, String areaInfodata, String constructInfodata) {
        this.reportInfodata = reportInfodata;
        this.areaInfodata = areaInfodata;
        this.constructInfodata = constructInfodata;
    }

    public String getReportInfodata() {
        return reportInfodata;
    }

    public void setReportInfodata(String reportInfodata) {
        this.reportInfodata = reportInfodata;
    }

    public String getAreaInfodata() {
        return areaInfodata;
    }

    public void setAreaInfodata(String areaInfodata) {
        this.areaInfodata = areaInfodata;
    }

    public String getConstructInfodata() {
        return constructInfodata;
    }

    public void setConstructInfodata(String constructInfodata) {
        this.constructInfodata = constructInfodata;
    }
}
