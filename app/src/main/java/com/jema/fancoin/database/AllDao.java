package com.jema.fancoin.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AllDao {

    @Query("SELECT * FROM user")
    List<User> getAllUsers();
    @Query("SELECT * FROM user  WHERE id == 1")
    List<User> check4User();

    @Query("SELECT * FROM user")
    LiveData<User> getUserInfo();

    @Insert
    void insertUser(User user);

    @Insert
    void insertUsers(User... users);

    @Delete
    void delete(User user);

    @Query("UPDATE User SET username = :uname, full_name = :ufullname, email = :uemail WHERE id == 1")
    void updateUser(String uname, String ufullname, String uemail);


    @Query("UPDATE User SET image = :image WHERE id == 1")
    void updateImage(String image);

    @Query("UPDATE User SET username = :username WHERE id == 1")
    void updateName(String username);

    @Query("DELETE FROM User")
    void delete();


//    add posts data

    @Insert
    void insertPost(Post post);

    @Insert
    void insertPosts(List<Post> posts);
    @Query("SELECT * FROM post  WHERE uid == :uuid")
    List<User> check4Post(String uuid);
    @Query("DELETE FROM Post")
    void deletePost();

    @Query("SELECT * FROM post")
    LiveData<Post> getAllPosts();
}
