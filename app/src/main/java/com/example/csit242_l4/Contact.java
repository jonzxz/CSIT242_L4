package com.example.csit242_l4;

public class Contact {

    private int id;
    private String name;
    private String mobile;
    private String email;

    public Contact() {

    }

    public Contact(Contact c) {
        this.id = c.id;
        this.name = c.name;
        this.mobile = c.mobile;
        this.email = c.email;
    }

    public Contact(String name) {
        this.name = name;
    }
    public Contact(int id, String name, String mobile, String email) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
    }

    public Contact(String name, String mobile, String email) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
