package com.example.camtel.ideaapp.dataBase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "diary")
public class Diary {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private int priority;
    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    @Ignore
    public Diary(String title,String description, int priority, Date updatedAt){
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.updatedAt = updatedAt;

    }
    public Diary(int id,String title,String description,int priority, Date updatedAt){
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.updatedAt = updatedAt;
    }


    public Date getUpdatedAt() {
        return updatedAt;
    }



    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }



    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
