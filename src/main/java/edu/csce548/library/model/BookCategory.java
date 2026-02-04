package edu.csce548.library.model;

import java.time.LocalDateTime;

public class BookCategory {
    private Integer categoryId;
    private String categoryName;
    private String description;
    private LocalDateTime createdAt;
    
    public BookCategory() {}
    
    public BookCategory(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
    }
    
    public Integer getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

