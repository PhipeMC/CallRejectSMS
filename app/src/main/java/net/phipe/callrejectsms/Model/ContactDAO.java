package net.phipe.callrejectsms.Model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDAO {
    @Query("select * from contact")
    List<Contact> getAll();

    @Query("select * from contact where celphone_number = :number")
    Contact findByNumber(String number);

    @Insert
    void insertAll(Contact... contacts);

    @Insert
    long insert(Contact contact);

    @Delete
    void delete(Contact contact);

    @Update
    int update(Contact contact);

    @Query("select count(*) from contact")
    int getLastID();
}
