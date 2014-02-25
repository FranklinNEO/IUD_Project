package com.knight.excel;

/**
 * Created by NEO on 13-12-13.
 */
public class ConstructInfo {
    private String orgCode;
    private String orgName;
    private String pid;
    private String areaid;
    private String userAccount;

    public ConstructInfo(String orgCode, String orgName, String pid, String areaid, String userAccount) {
        this.orgCode = orgCode;
        this.orgName = orgName;
        this.pid = pid;
        this.areaid = areaid;
        this.userAccount = userAccount;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }
}
