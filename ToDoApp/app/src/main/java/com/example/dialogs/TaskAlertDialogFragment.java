package com.example.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bhaskarjaiswal.todoapp.R;
import com.example.datasource.Tasks;

/**
 * Created by bjaiswal on 6/16/2016.
 */
public class TaskAlertDialogFragment extends DialogFragment {
    private EditText mEditText;
    private ChooseOptionsActionListener chooseOptionsActionListener;

    private TextView tvTaskName;
    private TextView tvDueDate;
    private TextView tvTaskNotes;
    private TextView tvPriority;
    private TextView tvStatus;
    private Tasks task;
    private int position;

    public interface ChooseOptionsActionListener {
        void onOptionsChosenDialogEdit(int position, Tasks task);

        void onOptionsChosenDialogDelete(int position, Tasks task);
    }

    public void setOnChooseOptionsActionListener(ChooseOptionsActionListener optionChosen) {
        chooseOptionsActionListener = optionChosen;
    }

    public TaskAlertDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Papyrus");
        View view = inflater.inflate(R.layout.task_description_fragment, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        setHasOptionsMenu(true);

        tvTaskName = (TextView) view.findViewById(R.id.tvTaskNameResult);
        tvDueDate = (TextView) view.findViewById(R.id.tvDueDateResult);
        tvTaskNotes = (TextView) view.findViewById(R.id.tvNotesResult);
        tvPriority = (TextView) view.findViewById(R.id.tvPriorityResult);
        tvStatus = (TextView) view.findViewById(R.id.tvStatusResult);



        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miDelete:
                        chooseOptionsActionListener.onOptionsChosenDialogDelete(position, task);
                        getDialog().dismiss();
                        return true;
                    case R.id.miEdit:
                        chooseOptionsActionListener.onOptionsChosenDialogEdit(position, task);
                        getDialog().dismiss();
                        return true;
                    case R.id.miCancel:
                        getDialog().dismiss();
                        return true;
                }
                return true;
            }
        });

        toolbar.inflateMenu(R.menu.task_menu);

        task = (Tasks) getArguments().getSerializable("task");
        position = getArguments().getInt("position");

        tvTaskName.setText(task.getTaskName());
        tvDueDate.setText(task.getDueDate());
        tvTaskNotes.setText(task.getNotes());
        tvPriority.setText(task.getPriority().toString());
        tvStatus.setText(task.getStatus().toString());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
