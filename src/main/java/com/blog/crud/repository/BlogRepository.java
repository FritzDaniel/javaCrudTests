package com.blog.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.crud.model.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    
    List<Blog> findByAuthor(String author);
    List<Blog> findByTitleContaining(String title);
}
