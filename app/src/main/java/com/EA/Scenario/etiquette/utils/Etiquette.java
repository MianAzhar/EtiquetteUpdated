package com.EA.Scenario.etiquette.utils;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Mian on 8/31/2015.
 */
public class Etiquette implements Serializable {

    public Etiquette()
    {
        uri = null;
    }

    public Etiquette(String title, String cat, int rating)
    {
        this.Scenario_Description = title;
        this.Category_Name = cat;
        this.Scenario_Level = rating;
        uri = null;
    }

    public Bitmap getUri() {
        return uri;
    }

    public void setUri(Bitmap uri) {
        this.uri = uri;
    }

    private Bitmap uri;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMeter() {
        return meter;
    }

    public void setMeter(int meter) {
        this.meter = meter;
    }

    public String getScenario_Option_1() {
        return Scenario_Option_1;
    }

    public void setScenario_Option_1(String scenario_Option_1) {
        this.Scenario_Option_1 = scenario_Option_1;
    }

    public String getScenario_Option_2() {
        return Scenario_Option_2;
    }

    public void setScenario_Option_2(String scenario_Option_2) {
        this.Scenario_Option_2 = scenario_Option_2;
    }

    public String getScenario_Option_3() {
        return Scenario_Option_3;
    }

    public void setScenario_Option_3(String opt3_text) {
        this.Scenario_Option_3 = opt3_text;
    }

    public String getScenario_Option_4() {
        return Scenario_Option_4;
    }

    public void setScenario_Option_4(String scenario_Option_4) {
        this.Scenario_Option_4 = scenario_Option_4;
    }

    private int id;
    private String title;
    private String type;
    private int meter;
    /*
    private String Scenario_Option_1;
    private String Scenario_Option_2;
    private String Scenario_Option_3;
    private String Scenario_Option_4;
*/
    public String getScenario_Option_9() {
        return Scenario_Option_9;
    }

    public void setScenario_Option_9(String scenario_Option_9) {
        Scenario_Option_9 = scenario_Option_9;
    }

    public String getScenario_Option_8() {
        return Scenario_Option_8;
    }

    public void setScenario_Option_8(String scenario_Option_8) {
        Scenario_Option_8 = scenario_Option_8;
    }

    public String getScenario_Option_7() {
        return Scenario_Option_7;
    }

    public void setScenario_Option_7(String scenario_Option_7) {
        Scenario_Option_7 = scenario_Option_7;
    }

    public String getScenario_Option_6() {
        return Scenario_Option_6;
    }

    public void setScenario_Option_6(String scenario_Option_6) {
        Scenario_Option_6 = scenario_Option_6;
    }

    public String getScenario_Option_5() {
        return Scenario_Option_5;
    }

    public void setScenario_Option_5(String scenario_Option_5) {
        Scenario_Option_5 = scenario_Option_5;
    }

    /*
    private String Scenario_Option_5;
    private String Scenario_Option_6;
    private String Scenario_Option_7;
    private String Scenario_Option_8;
    private String Scenario_Option_9;
*/
    public int getOpt1_count() {
        return opt1_count;
    }

    public void setOpt1_count(int opt1_count) {
        this.opt1_count = opt1_count;
    }

    private int opt1_count = 0;
    private int opt2_count = 0;

    public int getOpt4_count() {
        return opt4_count;
    }

    public void setOpt4_count(int opt4_count) {
        this.opt4_count = opt4_count;
    }

    public int getOpt3_count() {
        return opt3_count;
    }

    public void setOpt3_count(int opt3_count) {
        this.opt3_count = opt3_count;
    }

    public int getOpt2_count() {
        return opt2_count;
    }

    public void setOpt2_count(int opt2_count) {
        this.opt2_count = opt2_count;
    }

    private int opt3_count = 0;
    private int opt4_count = 0;

    //new data

    public String Etiquette_Id;
    public String Category_Name;
    public String User_Picture;
    public String User_Full_Name;
    public String User_Name;
    public long Scenario_Entry_Time;
    public String Scenario_Number_Of_Views;
    public String Scenario_Location;
    public String Scenario_Current_Location;
    public String Scenario_Description;
    public String Scenario_Picture;
    public int Scenario_Level;
    public String Scenario_Option_1;
    public String Scenario_Option_2;
    public String Scenario_Option_3;
    public String Scenario_Option_4;
    public String Scenario_Option_5;
    public String Scenario_Option_6;
    public String Scenario_Option_7;
    public String Scenario_Option_8;
    public String Scenario_Option_9;
    public int Scenario_Value_1;
    public int Scenario_Value_2;
    public int Scenario_Value_3;
    public int Scenario_Value_4;
    public int Scenario_Value_5;
    public int Scenario_Value_6;
    public int Scenario_Value_7;
    public int Scenario_Value_8;
    public int Scenario_Value_9;

}
