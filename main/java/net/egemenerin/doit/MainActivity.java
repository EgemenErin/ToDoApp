package net.egemenerin.doit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.egemenerin.doit.Adapter.ToDoAppAdapter;
import net.egemenerin.doit.Model.ToDoAppModel;
import net.egemenerin.doit.Utility.DatabaseHandler;
import net.egemenerin.doit.R;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The main activity of the To-Do app.
 * Displays a list of tasks, allows users to add, update, and delete tasks.
 * Implements drag-and-drop and swipe-to-delete functionality using RecyclerView.
 */
public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    // Database handler for managing tasks
    private DatabaseHandler db;

    // UI components
    private RecyclerView tasksRecyclerView;
    private ToDoAppAdapter tasksAdapter;
    private FloatingActionButton fab;

    // List of tasks to display in the RecyclerView
    private List<ToDoAppModel> taskList;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the default action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Initialize database handler
        db = new DatabaseHandler(this);
        db.openDatabase();

        // Initialize RecyclerView and adapter
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAppAdapter(db, MainActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        // Add swipe-to-delete and drag-and-drop functionality
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        // Initialize FloatingActionButton
        fab = findViewById(R.id.fab);

        // Load tasks from the database
        taskList = db.getAllTasks();
        Collections.reverse(taskList); // Reverse order for most recent tasks first
        tasksAdapter.setTasks(taskList);

        // Set click listener for FloatingActionButton to open AddNewTask dialog
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTask.newInstance().show(getSupportFragmentManager(), AddTask.TAG);
            }
        });
    }

    /**
     * Handles the close event of the AddNewTask dialog.
     * Refreshes the task list and updates the RecyclerView.
     *
     * @param dialog The dialog interface.
     */
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        // Reload tasks from the database
        taskList = db.getAllTasks();
        Collections.reverse(taskList); // Reverse order for consistent display
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged(); // Notify adapter of data changes
    }
}
