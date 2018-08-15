package com.login.model;

public class LineEntityProfile {
    private String userId;
    private String displayName;
    private String pictureUrl;
    private String statusMessage;

    public LineEntityProfile() {
    }

    public LineEntityProfile(String userId, String displayName, String pictureUrl, String statusMessage) {
        this.userId = userId;
        this.displayName = displayName;
        this.pictureUrl = pictureUrl;
        this.statusMessage = statusMessage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
