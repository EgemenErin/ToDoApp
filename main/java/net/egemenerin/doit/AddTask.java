package net.egemenerin.doit;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import net.egemenerin.doit.Model.ToDoAppModel;
import net.egemenerin.doit.Utility.DatabaseHandler;
import net.egemenerin.doit.R;

import java.util.Objects;

/**
 * A BottomSheetDialogFragment for adding or updating a task in the to-do list.
 * Provides a user interface to input task details and save them to the database.
 */
public class AddTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";

    // UI elements
    private EditText newTaskText;
    private Button newTaskSaveButton;

    // Database handler for managing tasks
    private DatabaseHandler db;

    /**
     * Creates a new instance of AddNewTask.
     *
     * @return A new AddNewTask fragment.
     */
    public static AddTask newInstance() {
        return new AddTask();
    }

    /**
     * Sets the style of the dialog on creation.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    /**
     * Inflates the layout for the BottomSheetDialogFragment.
     *
     * @param inflater  The LayoutInflater.
     * @param container The parent ViewGroup.
     * @param savedInstanceState The saved instance state.
     * @return The inflated view for the dialog.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_task, container, false);

        // Adjust the dialog to resize when the keyboard appears
        if (getDialog() != null) {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        return view;
    }

    /**
     * Initializes the UI elements and sets up event listeners for user interactions.
     *
     * @param view The root view of the dialog.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI elements
        newTaskText = Objects.requireNonNull(getView()).findViewById(R.id.newTaskText);
        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);

        boolean isUpdate = false;
        final Bundle bundle = getArguments();

        // Check if the dialog is for updating an existing task
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskText.setText(task);
            if (task != null && task.length() > 0) {
                newTaskSaveButton.setTextColor(
                        ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimaryDark)
                );
            }
        }

        // Initialize database handler
        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        // Add a text change listener to enable/disable the save button based on input
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                } else {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(
                            ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimaryDark)
                    );
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Save button click listener
        final boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString();
                if (finalIsUpdate) {
                    // Update existing task
                    db.updateTask(bundle.getInt("id"), text);
                } else {
                    // Insert new task
                    ToDoAppModel task = new ToDoAppModel();
                    task.setTask(text);
                    task.setStatus(0); // Default status: incomplete
                    db.insertTask(task);
                }
                dismiss(); // Close the dialog
            }
        });
    }

    /**
     * Handles the dismissal of the dialog and notifies the parent activity.
     *
     * @param dialog The dialog interface.
     */
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener) activity).handleDialogClose(dialog);
        }
    }
}
