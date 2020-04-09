package com.easyapps.coronatracker.date_wise;

public class DateWiseItem {
    private String mText1;
    private String mText2;
    private String mText3;
    private String mText4;
    private String mText5;
    private String mText6;
    private String mText7;

    public DateWiseItem(String text1, String text2, String text3, String text4,
                        String text5, String text6, String text7){
        mText1=text1;
        mText2=text2;
        mText3=text3;
        mText4=text4;
        mText5=text5;
        mText6=text6;
        mText7=text7;
    }

    public String getText1(){
        return mText1;
    }

    public String getText2(){
        return mText2;
    }

    public String getText3(){
        return mText3;
    }

    public String getText4(){
        return mText4;
    }

    public String getText5(){
        return mText5;
    }

    public String getText6(){
        return mText6;
    }

    public String getText7(){
        return mText7;
    }
}
