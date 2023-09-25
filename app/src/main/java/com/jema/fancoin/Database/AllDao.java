package com.jema.fancoin.Database;

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

    @Query("SELECT * FROM user")
    User getUserInfoNormal();

    @Insert
    void insertUser(User user);

    @Insert
    void insertUsers(User... users);

    @Delete
    void delete(User user);

    @Query("UPDATE User SET username = :username, full_name = :full_name, application_status = :application_status, category = :category, email = :email, bio = :bio, image = :image, phone = :phone, pricing = :pricing, following = :myfolli, followers = :myfollo WHERE id == 1")
    void updateUser(String username, String full_name, String application_status, String category, String email, String bio, String image, String phone, String pricing, String myfollo, String myfolli);
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
