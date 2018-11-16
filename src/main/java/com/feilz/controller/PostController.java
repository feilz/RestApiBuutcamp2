package com.feilz.controller;

import com.feilz.controller.exception.ResourceNotFoundException;
import com.feilz.dao.PostRepository;
import com.feilz.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/")
    private String getFirst() {
        return "Hello world!";
    }

    @GetMapping("/posts")
    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @PostMapping("/posts")
    public Post createPost(@Valid @RequestBody Post post) {
        return postRepository.save(post);
    }

    @PutMapping("/posts")
    public Post putPost(@Valid @RequestBody Post post) {
        return postRepository.findById(post.getId())
                .map(newPost -> {
                    newPost.setTitle(post.getTitle());
                    newPost.setPost(post.getPost());
                    return postRepository.save(newPost);
                }).orElseThrow(() -> new ResourceNotFoundException("No post with id = " + post.getId()));
    }

    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable long id) {
        return postRepository.findById(id)
             .orElseThrow(()-> new ResourceNotFoundException("No post with id = " + id));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable long id) {
        return postRepository.findById(id)
                .map(post -> {
                    postRepository.delete(post);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("No post with id = " + id));
    }
}
