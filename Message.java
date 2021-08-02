package com.example.email_j2ee.entity;

public class Message {
    public int id;
   public String email;
  public  String message;

    public Message(String email, String message) {
        this.email = email;
        this.message = message;
    }

    public Message(int id, String email, String message) {
        this.id = id;
        this.email = email;
        this.message = message;
    }
}

