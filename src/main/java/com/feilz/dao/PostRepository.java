package com.feilz.dao;

import com.feilz.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAllByKey(String key);
    List<Post> findAllByIdAndKey(Long id,String key);

}
