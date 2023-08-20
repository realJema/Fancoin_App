package com.jema.fancoin.database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public interface PostCardDao {

    @Insert
    void insertPostCard(PostCard postcard);

    @Update
    void updatePostCard(PostCard postcard);

    @Delete
    void deletePostCard(PostCard postcard);

    @Query("SELECT * FROM postcard")
    List<PostCard> getAllPostCards();

    @Query("SELECT * FROM postcard WHERE id = :postId")
    PostCard getPostCardById(int postId);
}
