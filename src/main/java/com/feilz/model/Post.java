package com.feilz.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="post")
public class Post {

    @Id
    @GeneratedValue(generator = "id_generator")
    @SequenceGenerator(
            name="id_generator",
            sequenceName = "id_sequence",
            initialValue = 100
    )
    private long id;

    @NotBlank
    @Size(min=1)
    private String title;

    @Column(name="text")
    private String post;

    @Column(name="key")
    @JsonIgnore
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
