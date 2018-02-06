package com.banmatrip.alert.domain;

import java.util.Date;

public class Tag {
    private Integer id;

    private String name;

    private String description;

    private String img_1;

    private String img_2;

    private String img_customize;

    private Integer group_id;

    private Byte delete_flag;

    private Date update_time;

    private String show_place;

    public Tag(Integer id, String name, String description, String img_1, String img_2, String img_customize, Integer group_id, Byte delete_flag, Date update_time, String show_place) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.img_1 = img_1;
        this.img_2 = img_2;
        this.img_customize = img_customize;
        this.group_id = group_id;
        this.delete_flag = delete_flag;
        this.update_time = update_time;
        this.show_place = show_place;
    }

    public Tag() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getImg_1() {
        return img_1;
    }

    public void setImg_1(String img_1) {
        this.img_1 = img_1 == null ? null : img_1.trim();
    }

    public String getImg_2() {
        return img_2;
    }

    public void setImg_2(String img_2) {
        this.img_2 = img_2 == null ? null : img_2.trim();
    }

    public String getImg_customize() {
        return img_customize;
    }

    public void setImg_customize(String img_customize) {
        this.img_customize = img_customize == null ? null : img_customize.trim();
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public Byte getDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(Byte delete_flag) {
        this.delete_flag = delete_flag;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getShow_place() {
        return show_place;
    }

    public void setShow_place(String show_place) {
        this.show_place = show_place == null ? null : show_place.trim();
    }
}