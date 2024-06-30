package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapplication.adapters.NotesListAdapter;
import com.example.myapplication.database.NotesDB;
import com.example.myapplication.models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<Notes> notes = new ArrayList<>();
    List<Notes> filteredNotes = new ArrayList<>();
    NotesDB database;
    FloatingActionButton fab_add;
    SearchView searchView_home;
    Notes selectedNote;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycle_home);
        fab_add = findViewById(R.id.fab_add);
        searchView_home = findViewById(R.id.searchView_home);
        swipeRefreshLayout = findViewById(R.id.swipe_layout);

        database = NotesDB.getInstance(this);
        notes = database.noteDao().getAll();
        filteredNotes.addAll(notes);

        updateRecycler(filteredNotes);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        searchView_home.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    searchView_home.setIconified(true);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void filter(String newText) {
        filteredNotes.clear();
        for (Notes singleNote : notes) {
            if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
                    || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())) {
                filteredNotes.add(singleNote);
            }
        }
        notesListAdapter.filterList(filteredNotes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            Notes new_notes = (Notes) data.getSerializableExtra("note");
            database.noteDao().insert(new_notes);
            notes.clear();
            notes.addAll(database.noteDao().getAll());
            filter(searchView_home.getQuery().toString()); 
            notesListAdapter.notifyDataSetChanged();
        } else if (requestCode == 102 && resultCode == Activity.RESULT_OK) {
            Notes new_notes = (Notes) data.getSerializableExtra("note");
            database.noteDao().update(new_notes.getID(), new_notes.getTitle(), new_notes.getNotes());
            notes.clear();
            notes.addAll(database.noteDao().getAll());
            filter(searchView_home.getQuery().toString());
            notesListAdapter.notifyDataSetChanged();
        }
    }

    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter = new NotesListAdapter(MainActivity.this, notes, notesClickListener);
        recyclerView.setAdapter(notesListAdapter);
    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectedNote = notes;
            showPopUp(cardView);
        }

        private void showPopUp(CardView cardView) {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, cardView);
            popupMenu.setOnMenuItemClickListener(MainActivity.this);
            popupMenu.inflate(R.menu.menu);
            popupMenu.show();
        }
    };

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.pin) {
            if (selectedNote.isPinned()) {
                database.noteDao().pin(selectedNote.getID(), false);
                Toast.makeText(MainActivity.this, "Unpinned!", Toast.LENGTH_SHORT).show();
            } else {
                database.noteDao().pin(selectedNote.getID(), true);
                Toast.makeText(MainActivity.this, "Pinned!", Toast.LENGTH_SHORT).show();
            }
            notes.clear();
            notes.addAll(database.noteDao().getAll());
            filter(searchView_home.getQuery().toString()); 
            notesListAdapter.notifyDataSetChanged();
        } else if (id == R.id.delete) {
            database.noteDao().delete(selectedNote);
            notes.remove(selectedNote);
            notes.clear();
            notes.addAll(database.noteDao().getAll());
            filter(searchView_home.getQuery().toString()); 
            notesListAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Note Deleted!", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.set_timer) {
            showDateTimePicker();
        } else {
            return false;
        }
        return true;
    }

    private void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        long reminderTime = date.getTimeInMillis();
                        database.noteDao().updateReminderTime(selectedNote.getID(), reminderTime);
                        Toast.makeText(MainActivity.this, "Reminder set!", Toast.LENGTH_SHORT).show();
                        notes.clear();
                        notes.addAll(database.noteDao().getAll());
                        filter(searchView_home.getQuery().toString());
                        notesListAdapter.notifyDataSetChanged();
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ReminderChannel";
            String description = "Channel for Reminder Notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("notifyReminder", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onRefresh() {
        notes.clear();
        notes.addAll(database.noteDao().getAll());
        filter(searchView_home.getQuery().toString()); 
        notesListAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}
