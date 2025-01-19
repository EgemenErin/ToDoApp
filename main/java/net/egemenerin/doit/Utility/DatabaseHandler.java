package net.egemenerin.doit.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.egemenerin.doit.Model.ToDoAppModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles all database operations for the To-Do app.
 * Provides methods for creating, reading, updating, and deleting tasks in an SQLite database.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // Database version and name
    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";

    // Table and column names
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";

    // SQL statement for creating the table
    private static final String CREATE_TODO_TABLE =
            "CREATE TABLE " + TODO_TABLE + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TASK + " TEXT, "
                    + STATUS + " INTEGER)";

    private SQLiteDatabase db;

    /**
     * Constructor for the DatabaseHandler.
     *
     * @param context The application context.
     */
    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    /**
     * Called when the database is created for the first time.
     * Creates the tasks table.
     *
     * @param db The database instance.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    /**
     * Called when the database needs to be upgraded.
     * Drops the old table and creates a new one.
     *
     * @param db         The database instance.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    /**
     * Opens the database for writing operations.
     */
    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    /**
     * Inserts a new task into the database.
     *
     * @param task The task to insert.
     */
    public void insertTask(ToDoAppModel task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0); // Default status is 0 (incomplete)
        db.insert(TODO_TABLE, null, cv);
    }

    /**
     * Retrieves all tasks from the database.
     *
     * @return A list of all tasks.
     */
    public List<ToDoAppModel> getAllTasks() {
        List<ToDoAppModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, null);
            if (cur != null && cur.moveToFirst()) {
                do {
                    ToDoAppModel task = new ToDoAppModel();
                    task.setId(cur.getInt(cur.getColumnIndex(ID)));
                    task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                    task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                    taskList.add(task);
                } while (cur.moveToNext());
            }
        } finally {
            db.endTransaction();
            if (cur != null) {
                cur.close();
            }
        }
        return taskList;
    }

    /**
     * Updates the status of a task.
     *
     * @param id     The ID of the task to update.
     * @param status The new status (0 for incomplete, 1 for complete).
     */
    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + " = ?", new String[]{String.valueOf(id)});
    }

    /**
     * Updates the description of a task.
     *
     * @param id   The ID of the task to update.
     * @param task The new description of the task.
     */
    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + " = ?", new String[]{String.valueOf(id)});
    }

    /**
     * Deletes a task from the database.
     *
     * @param id The ID of the task to delete.
     */
    public void deleteTask(int id) {
        db.delete(TODO_TABLE, ID + " = ?", new String[]{String.valueOf(id)});
    }
}
