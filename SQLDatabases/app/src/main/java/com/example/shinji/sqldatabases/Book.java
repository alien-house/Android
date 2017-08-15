package com.example.shinji.sqldatabases;

/**
 * Created by shinji on 2017/08/14.
 */

public class Book {
    private int id;
    private String title, author;

    public Book(){}
    public Book(String title, String author){
        this.title = title;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString(){
        return "BOOK [ id = " + id + " ,title = " + title + " ,Author ="+ author + " ]";
    }

}
