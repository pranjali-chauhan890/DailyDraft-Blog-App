package com.v2v.blogapp;
public class HomeEntry {
    private String title;
    private String author;
    private String description;
    private String imageUrl;

    public HomeEntry() {
        // Required for Firebase
    }

    public HomeEntry(String title, String author, String description, String imageUrl) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
}