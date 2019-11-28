package com.example.yamyam;

class Msmg {
    String userFrom;
    String userTo;
    String Text;


    public String getuserFrom() { return userFrom; }

    public void setuserFrom(String userFrom) { this.userFrom = userFrom; }

    public String getuserTo() { return userTo; }

    public void setuserTo(String userTo) { this.userTo = userTo; }

    public String getText() { return Text; }

    public void setText(String Text) { this.Text = Text; }



    public Msmg(String userTo, String userFrom, String Text) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.Text = Text;

    }
}