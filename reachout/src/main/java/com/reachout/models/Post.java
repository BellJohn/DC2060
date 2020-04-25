
package com.reachout.models;

/**
 * Used to store all relevant post content so that it can
 * be easily accessed for display on the relevant page.
 * 
 * @author Jordan
 * 
 */
public class Post {
    private String title;
    private String author;
    private String date;
    private String content;

    public Post(String title, String author, String date, String content) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }
    
}