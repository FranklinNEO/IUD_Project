package com.knight.excel;

/**
 * Created by NEO on 13-12-13.
 */
public class AreaInfo {
    private String areaId;
    private String areaName;
    private String fullName;
    private String fareaid;
    private String parea;
    private String carea;
    private String xarea;
    private String lvl;
    private String userAccount;
    private String tarea;
    private String sarea;

    public AreaInfo(String areaId, String areaName, String fullName, String fareaid, String parea, String carea, String xarea, String lvl, String tarea, String sarea, String userAccount) {
        this.areaId = areaId;
        this.areaName = areaName;
        this.fullName = fullName;
        this.fareaid = fareaid;
        this.parea = parea;
        this.carea = carea;
        this.xarea = xarea;
        this.lvl = lvl;
        this.tarea = tarea;
        this.sarea = sarea;
        this.userAccount = userAccount;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFareaid() {
        return fareaid;
    }

    public void setFareaid(String fareaid) {
        this.fareaid = fareaid;
    }

    public String getParea() {
        return parea;
    }

    public void setParea(String parea) {
        this.parea = parea;
    }

    public String getCarea() {
        return carea;
    }

    public void setCarea(String carea) {
        this.carea = carea;
    }

    public String getXarea() {
        return xarea;
    }

    public void setXarea(String xarea) {
        this.xarea = xarea;
    }

    public String getLvl() {
        return lvl;
    }

    public void setLvl(String lvl) {
        this.lvl = lvl;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getSarea() {
        return sarea;
    }

    public void setSarea(String sarea) {
        this.sarea = sarea;
    }
}
