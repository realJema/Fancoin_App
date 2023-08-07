package com.jema.fancoin.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT EXISTS(SELECT * FROM user WHERE uid = :uuid)")
    Boolean isExists(String uuid);

    @Insert
    void insertUser(User user);

    @Insert
    void insertUsers(User... users);

    @Delete
    void delete(User user);

    @Query("UPDATE User SET username = :uname, full_name = :ufullname, email = :uemail WHERE uid = :uuid")
    void updateUser(String uname, String ufullname, String uemail, String uuid);
}