package com.jema.fancoin.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

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
}