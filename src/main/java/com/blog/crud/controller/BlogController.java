package com.blog.crud.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//Model
import com.blog.crud.model.Blog;

//Repository
import com.blog.crud.repository.BlogRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class BlogController {
    
    @Autowired
    BlogRepository blogRepository;

	@GetMapping("/blogs")
	public ResponseEntity<List<Blog>> getAllBlogs(@RequestParam(required = false) String title) {
		try {
			List<Blog> _Blogs = new ArrayList<Blog>();

			if (title == null)
				blogRepository.findAll().forEach(_Blogs::add);
			else
				blogRepository.findByTitleContaining(title).forEach(_Blogs::add);

			if (_Blogs.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(_Blogs, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    @GetMapping("/blog/{id}")
	public ResponseEntity<Blog> getBlogById(@PathVariable("id") long id) {
		Optional<Blog> _BlogData = blogRepository.findById(id);

		if (_BlogData.isPresent()) {
			return new ResponseEntity<>(_BlogData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/blog/create")
	public ResponseEntity<Blog> createBlog(@RequestBody Blog blog) {
		try {
			Blog _Blog = blogRepository.save(new Blog(blog.getTitle(), blog.getBody(), blog.getAuthor()));
			return new ResponseEntity<>(_Blog, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/blog/{id}")
	public ResponseEntity<Blog> updateBlog(@PathVariable("id") long id, @RequestBody Blog Blog) {
		Optional<Blog> BlogData = blogRepository.findById(id);

		if (BlogData.isPresent()) {
			Blog _Blog = BlogData.get();
			_Blog.setTitle(Blog.getTitle());
			_Blog.setBody(Blog.getBody());
			_Blog.setAuthor(Blog.getAuthor());
			return new ResponseEntity<>(blogRepository.save(_Blog), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/blog/delete/{id}")
	public ResponseEntity<HttpStatus> deleteBlog(@PathVariable("id") long id) {
		try {
			blogRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
