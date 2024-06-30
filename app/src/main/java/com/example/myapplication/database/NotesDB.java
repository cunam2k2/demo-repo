package com.example.myapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.models.Notes;

@Database(entities = Notes.class, version = 2, exportSchema = false)
public abstract class NotesDB extends RoomDatabase {
    private static NotesDB database;
    private static String DATABASE_NAME = "NoteApp";

    public synchronized static NotesDB getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    NotesDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract noteDao noteDao();
}
