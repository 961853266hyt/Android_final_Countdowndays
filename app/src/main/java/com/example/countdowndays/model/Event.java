package com.example.countdowndays.model;

import com.tuacy.fuzzysearchlibrary.IFuzzySearchItem;
import com.tuacy.fuzzysearchlibrary.PinyinUtil;

import java.io.Serializable;
import java.util.List;

public class Event implements Serializable, IFuzzySearchItem {
    private int id;
    private String title;
    private  String note;
    private int color;
    private Long date;
    private int bgm;
    private Long notidate;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public int getBgm() {
        return bgm;
    }

    public void setBgm(int bgm) {
        this.bgm = bgm;
    }

    public Long getNotidate() {
        return notidate;
    }

    public void setNotidate(Long notidate) {
        this.notidate = notidate;
    }

    @Override
    public String getSourceKey() {

        return PinyinUtil.getPinYin(title);
    }

    @Override
    public List<String> getFuzzyKey() {

        return PinyinUtil.getPinYinList(title);
    }
}