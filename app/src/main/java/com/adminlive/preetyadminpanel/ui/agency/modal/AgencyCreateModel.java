package com.adminlive.preetyadminpanel.ui.agency.modal;

public class AgencyCreateModel {
    private String userId;
    private long uid;
    private String role;
    private String username;
    private String agencyCode;
    private String email;
    private String phone;
    private String countryCode;
    private String countryName;
    private String image;
    private String regId;
    private String deviceId;
    private String coins;
    private long agencyEarning;
    private long totalHost;
    private String docId;
    private long joinDate;
    private long loginDate;

    public AgencyCreateModel() {
    }

    public AgencyCreateModel(String userId, long uid,String role, String username, String agencyCode, String email, String phone, String countryCode, String countryName, String image, String regId, String deviceId, String coins, long agencyEarning, long totalHost, String docId, long joinDate, long loginDate) {
        this.userId = userId;
        this.uid = uid;
        this.role = role;
        this.username = username;
        this.agencyCode = agencyCode;
        this.email = email;
        this.phone = phone;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.image = image;
        this.regId = regId;
        this.deviceId = deviceId;
        this.coins = coins;
        this.agencyEarning = agencyEarning;
        this.totalHost = totalHost;
        this.docId = docId;
        this.joinDate = joinDate;
        this.loginDate = loginDate;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public long getAgencyEarning() {
        return agencyEarning;
    }

    public void setAgencyEarning(long agencyEarning) {
        this.agencyEarning = agencyEarning;
    }

    public long getTotalHost() {
        return totalHost;
    }

    public void setTotalHost(long totalHost) {
        this.totalHost = totalHost;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public long getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(long joinDate) {
        this.joinDate = joinDate;
    }

    public long getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(long loginDate) {
        this.loginDate = loginDate;
    }
}
