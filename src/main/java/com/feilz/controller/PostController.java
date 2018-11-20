package com.feilz.controller;

import com.feilz.controller.exception.NotAllowedException;
import com.feilz.controller.exception.ResourceNotFoundException;
import com.feilz.dao.PostRepository;
import com.feilz.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostRepository postRepository;


    @GetMapping("/posts")
    public List<Post> getPosts(@RequestParam("key") String key) {
        return postRepository.findAllByKey(key);
    }

    @PostMapping("/posts")
    public Post createPost(@Valid @RequestBody Post post, @RequestParam("key") String key) {
        post.setKey(key);
        return postRepository.save(post);
    }

    @PutMapping("/posts")
    public Post putPost(@Valid @RequestBody Post post, @RequestParam("key") String key) {
        return postRepository.findById(post.getId())
                .map(newPost -> {
                    if (key.equals(newPost.getKey())){
                        newPost.setTitle(post.getTitle());
                        newPost.setPost(post.getPost());
                        return postRepository.save(newPost);
                    }
                    throw new NotAllowedException("Given userKey is not the owner of this post");
                }).orElseThrow(() -> new ResourceNotFoundException("No post with id = " + post.getId()));
    }

    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable long id, @RequestParam("key") String key) {
        return postRepository.findById(id)
             .orElseThrow(()-> new ResourceNotFoundException("No post with id = " + id));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable long id, @RequestParam("key") String key) {
        return postRepository.findById(id)
                .map(post -> {
                    if (key.equals(post.getKey())){
                        postRepository.delete(post);
                        return ResponseEntity.ok().build();
                    }
                    throw new NotAllowedException("Given userKey is not the owner of this post");
                }).orElseThrow(() -> new ResourceNotFoundException("No post with id = " + id));
    }
}
