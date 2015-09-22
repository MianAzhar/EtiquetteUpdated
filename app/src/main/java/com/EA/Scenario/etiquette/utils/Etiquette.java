package com.EA.Scenario.etiquette.utils;

import android.net.Uri;

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
        this.title = title;
        this.type = cat;
        this.meter = rating;
        uri = null;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    private Uri uri;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMinor_description() {
        return minor_description;
    }

    public void setMinor_description(String minor_description) {
        this.minor_description = minor_description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMeter() {
        return meter;
    }

    public void setMeter(int meter) {
        this.meter = meter;
    }

    public String getOpt1_text() {
        return opt1_text;
    }

    public void setOpt1_text(String opt1_text) {
        this.opt1_text = opt1_text;
    }

    public String getOpt2_text() {
        return opt2_text;
    }

    public void setOpt2_text(String opt2_text) {
        this.opt2_text = opt2_text;
    }

    public String getOpt3_text() {
        return opt3_text;
    }

    public void setOpt3_text(String opt3_text) {
        this.opt3_text = opt3_text;
    }

    public String getOpt4_text() {
        return opt4_text;
    }

    public void setOpt4_text(String opt4_text) {
        this.opt4_text = opt4_text;
    }

    private int id;
    private String title;
    private String type;
    private String description;
    private String minor_description;
    private String url;
    private int meter;
    private String opt1_text;
    private String opt2_text;
    private String opt3_text;
    private String opt4_text;

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

}
