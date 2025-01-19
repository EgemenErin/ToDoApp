package net.egemenerin.doit.Model;

/**
 * Represents a task in the to-do list.
 * This model class holds the properties of a task, including its ID, status, and description.
 */
public class ToDoAppModel {
    // Unique identifier for the task
    private int id;

    // Status of the task (e.g., 0 for incomplete, 1 for complete)
    private int status;

    // Description or name of the task
    private String task;

    /**
     * Gets the unique identifier of the task.
     *
     * @return The task ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the task.
     *
     * @param id The task ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the status of the task.
     *
     * @return The task status (0 for incomplete, 1 for complete).
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the status of the task.
     *
     * @param status The task status to set (0 for incomplete, 1 for complete).
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Gets the description or name of the task.
     *
     * @return The task description.
     */
    public String getTask() {
        return task;
    }

    /**
     * Sets the description or name of the task.
     *
     * @param task The task description to set.
     */
    public void setTask(String task) {
        this.task = task;
    }
}
