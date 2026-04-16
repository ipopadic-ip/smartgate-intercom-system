package com.example.demo.dto;

public class IntercomEventDto {

    private Integer stan;
    private String image_url;
    private String timestamp;

    public Integer getStan() {
        return stan;
    }

    public void setStan(Integer stan) {
        this.stan = stan;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}