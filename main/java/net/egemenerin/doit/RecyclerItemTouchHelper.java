package net.egemenerin.doit;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import net.egemenerin.doit.Adapter.ToDoAppAdapter;
import net.egemenerin.doit.R;

/**
 * Provides swipe functionality for RecyclerView items.
 * Handles swipe-to-delete and swipe-to-edit actions with visual feedback.
 */
public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private final ToDoAppAdapter adapter;

    /**
     * Constructor for RecyclerItemTouchHelper.
     *
     * @param adapter The adapter for the RecyclerView to which this helper is attached.
     */
    public RecyclerItemTouchHelper(ToDoAppAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    /**
     * Handles item move events (not used in this implementation).
     *
     * @return Always returns false as moving items is not supported.
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * Handles swipe events for RecyclerView items.
     * Left swipe prompts the user to delete the item.
     * Right swipe allows editing the item.
     *
     * @param viewHolder The ViewHolder being swiped.
     * @param direction  The direction of the swipe (left or right).
     */
    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();

        if (direction == ItemTouchHelper.LEFT) {
            // Prompt the user for confirmation before deleting the task
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure you want to delete this Task?");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.deleteItem(position);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            // Edit the task
            adapter.editItem(position);
        }
    }

    /**
     * Draws custom visual feedback for swipe actions.
     *
     * @param c                 The canvas on which to draw.
     * @param recyclerView      The RecyclerView containing the swiped item.
     * @param viewHolder        The ViewHolder being swiped.
     * @param dX                The horizontal displacement of the ViewHolder.
     * @param dY                The vertical displacement of the ViewHolder.
     * @param actionState       The type of interaction on the item (e.g., swipe).
     * @param isCurrentlyActive Whether the item is actively being swiped.
     */
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        // Get the view of the item being swiped
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20; // Offset for rounded corners

        if (dX > 0) { // Swiping to the right (edit action)
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_edit);
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.colorPrimaryDark));
        } else { // Swiping to the left (delete action)
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_delete);
            background = new ColorDrawable(Color.RED);
        }

        // Calculate icon position
        assert icon != null;
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + iconMargin;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) { // Right swipe
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        } else if (dX < 0) { // Left swipe
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // No swipe
            background.setBounds(0, 0, 0, 0);
        }

        // Draw the background and icon
        background.draw(c);
        icon.draw(c);
    }
}
