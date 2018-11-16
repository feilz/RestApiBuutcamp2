package com.feilz.controller;

import com.feilz.controller.exception.ResourceNotFoundException;
import com.feilz.dao.CommentRepository;
import com.feilz.dao.PostRepository;
import com.feilz.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/{postId}/comments")
    public List<Comment> getCommentsByQuestionId(@PathVariable Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @PostMapping("/{postId}/comments")
    public Comment addComment(@PathVariable Long postId,
                              @Valid @RequestBody Comment comment) {
        return postRepository.findById(postId)
                .map(post -> {
                    comment.setPost(post);
                    return commentRepository.save(comment);
                }).orElseThrow(() -> new ResourceNotFoundException("No post with id = " + postId));
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public Comment updateComment(@PathVariable Long postId,
                                 @PathVariable Long commentId,
                                 @Valid @RequestBody Comment comment) {
        if (!postRepository.existsById(postId)){
            throw new ResourceNotFoundException("Post with id " + postId + " not found");
        }

        return commentRepository.findById(commentId)
                .map(thisComment -> {
                    thisComment.setText(comment.getText());
                    return commentRepository.save(thisComment);
                }).orElseThrow(() -> new ResourceNotFoundException("No comment found with id " + commentId));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long postId,
                                           @PathVariable Long commentId) {
        if (!postRepository.existsById(postId)){
            throw new ResourceNotFoundException("Post with id " + postId + " not found");
        }

        return commentRepository.findById(commentId)
                .map(comment -> {
                    commentRepository.delete(comment);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("No comment found with id " + commentId));
    }
}
