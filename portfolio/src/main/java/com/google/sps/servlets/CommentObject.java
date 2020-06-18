package com.google.sps.servlets;

public class CommentObject {
  private String text;
  private String imgUrl;
  public CommentObject(String textInput, String imgInput) {
    text = textInput;
    imgUrl = imgInput;
  }
  public String getText() {
    return text;
  }
  public String getUrl() {
    return imgUrl;
  }
}