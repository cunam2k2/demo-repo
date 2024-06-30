package com.example.myapplication.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.models.Notes;

import java.util.List;

@Dao
public interface noteDao {
    @Insert
    void insert(Notes notes);
    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Notes> getAll();
    @Query("UPDATE notes SET title = :title, notes = :notes WHERE ID = :id")
    void update(int id, String title, String notes);
    @Delete
    void delete(Notes notes) ;
    @Query("UPDATE notes SET pinned = :pin WHERE ID = :id")
    void pin(int id, boolean pin);

    @Query("UPDATE notes SET reminderTime = :reminderTime WHERE id = :id")
    void updateReminderTime(int id, long reminderTime);
}
