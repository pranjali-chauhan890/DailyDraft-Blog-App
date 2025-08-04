package com.v2v.blogapp;

public class LogEntry {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private String url;
    private String author;

    // Required empty constructor for Firebase
    public LogEntry() {}

    // Constructor without ID (used while adding new entries)
    public LogEntry(String title, String description, String imageUrl, String url, String author) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.url = url;
        this.author = author;
    }

    // Constructor with ID (used for updating entries)
    public LogEntry(String id, String title, String description, String imageUrl, String url, String author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.url = url;
        this.author = author;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}