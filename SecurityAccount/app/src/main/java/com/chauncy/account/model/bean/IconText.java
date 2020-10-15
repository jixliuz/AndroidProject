package com.chauncy.account.model.bean;

public class IconText {
    private int imageId;
    private String text;

    public IconText(String text){
        this.text=text;
        imageId=-1;
    }

    public IconText(int resId, String text) {
        this.imageId = resId;
        this.text = text;
    }

    public int getImageResource() {
        return imageId;
    }

    public void setImageResource(int imageId) {
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
