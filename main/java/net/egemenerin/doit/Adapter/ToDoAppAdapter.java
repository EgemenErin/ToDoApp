package net.egemenerin.doit.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.egemenerin.doit.AddTask;
import net.egemenerin.doit.MainActivity;
import net.egemenerin.doit.Model.ToDoAppModel;
import net.egemenerin.doit.R;
import net.egemenerin.doit.Utility.DatabaseHandler;

import java.util.List;

/**
 * Adapter class for managing and displaying a list of tasks in a RecyclerView.
 * This class interacts with the database to update and delete tasks dynamically
 * and provides functionality for editing tasks.
 */
public class ToDoAppAdapter extends RecyclerView.Adapter<ToDoAppAdapter.ViewHolder> {

    // List to hold ToDoModel objects representing tasks
    private List<ToDoAppModel> todoList;

    // Database handler for managing SQLite database operations
    private DatabaseHandler db;

    // Reference to the parent activity for context and fragment transactions
    private MainActivity activity;

    /**
     * Constructor for the ToDoAdapter.
     *
     * @param db       The database handler for database operations.
     * @param activity The activity where this adapter is used.
     */
    public ToDoAppAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    /**
     * Inflates the layout for each RecyclerView item.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder instance.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * Binds data to the ViewHolder for the specified position.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // Open the database connection
        db.openDatabase();

        // Get the task at the specified position
        final ToDoAppModel item = todoList.get(position);

        // Set task text and status
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));

        // Set a listener for changes in the task's checked status
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(item.getId(), 1); // Mark task as completed
                } else {
                    db.updateStatus(item.getId(), 0); // Mark task as incomplete
                }
            }
        });
    }

    /**
     * Converts an integer status to a boolean value.
     *
     * @param n The integer status (0 or 1).
     * @return True if the status is not 0, otherwise false.
     */
    private boolean toBoolean(int n) {
        return n != 0;
    }

    /**
     * Returns the total number of tasks in the list.
     *
     * @return The size of the task list.
     */
    @Override
    public int getItemCount() {
        return todoList.size();
    }

    /**
     * Gets the context of the parent activity.
     *
     * @return The context of the activity.
     */
    public Context getContext() {
        return activity;
    }

    /**
     * Sets the list of tasks and notifies the adapter of data changes.
     *
     * @param todoList The list of tasks to display.
     */
    public void setTasks(List<ToDoAppModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    /**
     * Deletes a task from the list and database.
     *
     * @param position The position of the task to delete.
     */
    public void deleteItem(int position) {
        ToDoAppModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Opens the AddNewTask fragment to edit an existing task.
     *
     * @param position The position of the task to edit.
     */
    public void editItem(int position) {
        ToDoAppModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());

        AddTask fragment = new AddTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddTask.TAG);
    }

    /**
     * ViewHolder class for RecyclerView items.
     * Manages the task CheckBox in the layout.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        /**
         * Constructor for the ViewHolder.
         *
         * @param view The item view containing the CheckBox.
         */
        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
