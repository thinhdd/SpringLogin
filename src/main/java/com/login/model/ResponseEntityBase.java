package com.login.model;

public class ResponseEntityBase<T> {

    private int responCode;
    private String responMessage;
    private T data;

    public ResponseEntityBase() {
    }

    public ResponseEntityBase(String responMessage, int responCode, T data) {
        this.responMessage = responMessage;
        this.responCode = responCode;
        this.data = data;
    }



    public String getResponMessage() {
        return responMessage;
    }

    public void setResponMessage(String responMessage) {
        this.responMessage = responMessage;
    }

    public int getResponCode() {
        return responCode;
    }

    public void setResponCode(int responCode) {
        this.responCode = responCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
