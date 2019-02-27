package com.creatoo.guangdong_pos.api.domain;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.api.domain
 * @Description:
 * @date 2018/4/18
 */
public class IDcardInfo {
    private byte[] name = new byte[32];
    private byte[] sex = new byte[6];
    private byte[] birth = new byte[18];
    private byte[] nation = new byte[12];
    private byte[] address = new byte[72];
    private byte[] Department = new byte[32];
    private byte[] IDNo = new byte[38];
    private byte[] EffectDate = new byte[18];
    private byte[] ExpireDate = new byte[18];
    private byte [] BmpFile = new byte[38556];

    public byte[] getName() {
        return name;
    }

    public void setName(byte[] name) {
        this.name = name;
    }

    public byte[] getSex() {
        return sex;
    }

    public void setSex(byte[] sex) {
        this.sex = sex;
    }

    public byte[] getBirth() {
        return birth;
    }

    public void setBirth(byte[] birth) {
        this.birth = birth;
    }

    public byte[] getNation() {
        return nation;
    }

    public void setNation(byte[] nation) {
        this.nation = nation;
    }

    public byte[] getAddress() {
        return address;
    }

    public void setAddress(byte[] address) {
        this.address = address;
    }

    public byte[] getDepartment() {
        return Department;
    }

    public void setDepartment(byte[] department) {
        Department = department;
    }

    public byte[] getIDNo() {
        return IDNo;
    }

    public void setIDNo(byte[] IDNo) {
        this.IDNo = IDNo;
    }

    public byte[] getEffectDate() {
        return EffectDate;
    }

    public void setEffectDate(byte[] effectDate) {
        EffectDate = effectDate;
    }

    public byte[] getExpireDate() {
        return ExpireDate;
    }

    public void setExpireDate(byte[] expireDate) {
        ExpireDate = expireDate;
    }

    public byte[] getBmpFile() {
        return BmpFile;
    }

    public void setBmpFile(byte[] bmpFile) {
        BmpFile = bmpFile;
    }
}
