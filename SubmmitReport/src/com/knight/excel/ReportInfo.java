package com.knight.excel;

/**
 * Created by NEO on 13-12-13.
 */
public class ReportInfo {
    private String parea;
    private String carea;
    private String xarea;
    private String tarea;
    private String areaId;
    private String stationName;
    private String chargePerson;
    private String chargePhone;
    private String contactPerson;
    private String contactPhone;
    private String accountId;

    public ReportInfo(String parea, String carea, String xarea, String tarea, String areaId, String stationName, String chargePerson, String chargePhone, String contactPerson, String contactPhone, String accountId) {
        this.parea = parea;
        this.carea = carea;
        this.xarea = xarea;
        this.areaId = areaId;
        this.tarea = tarea;
        this.stationName = stationName;
        this.chargePerson = chargePerson;
        this.chargePhone = chargePhone;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.accountId = accountId;
    }

    public String getParea() {
        return parea;
    }

    public String getCarea() {
        return carea;
    }

    public String getXarea() {
        return xarea;
    }

    public String getAreaId() {
        return areaId;
    }

    public String getTarea() {
        return tarea;
    }

    public String getStationName() {
        return stationName;
    }

    public String getChargePerson() {
        return chargePerson;
    }

    public String getChargePhone() {
        return chargePhone;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setParea(String parea) {
        this.parea = parea;
    }

    public void setCarea(String carea) {
        this.carea = carea;
    }

    public void setXarea(String xarea) {
        this.xarea = xarea;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public void setChargePerson(String chargePerson) {
        this.chargePerson = chargePerson;
    }

    public void setChargePhone(String chargePhone) {
        this.chargePhone = chargePhone;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
