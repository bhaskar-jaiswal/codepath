package com.example.bhaskarjaiswal.todoapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.constants.Priority;
import com.example.constants.Status;
import com.example.datasource.Tasks;
import com.example.datasource.TasksDataSource;
import com.example.dialogs.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TasksDataSource datasource;

    EditText etTaskName;
    TextView datepicker;
    EditText etTaskNotes;
    Spinner spinnerPriority;
    Spinner spinnerStatus;
    Tasks task;
    private DatePickerFragment datePickerFragment;

    String action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

        datasource = new TasksDataSource(this);
        datasource.open();

        etTaskName = (EditText) findViewById(R.id.etTaskName);
        datepicker = (TextView) findViewById(R.id.tvDatePickerFragment);
        etTaskNotes = (EditText) findViewById(R.id.etTaskNotes);
        spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);
        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);

        datePickerFragment = new DatePickerFragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mi_edit_save:
                        onSaveItem();
                        return true;
                    case R.id.mi_edit_cancel:
                        finish();
                        return true;
                }
                return true;
            }
        });

        toolbar.inflateMenu(R.menu.task_menu_edit);

        ArrayList<String> plist = new ArrayList<String>();
        for(Priority p : Priority.values()){
            plist.add(p.toString());
        }

        ArrayList<String> slist = new ArrayList<String>();
        for(Status s : Status.values()){
            slist.add(s.toString());
        }

//        List<String> priorities = Arrays.asList(Priority.values().toString());
//        List<Priority> priorities = new ArrayList<Priority>(EnumSet.allOf(Priority.class));

//        List<String> status = Arrays.asList(Status.values().toString());
//        List<Status> status = new ArrayList<Status>(EnumSet.allOf(Status.class));

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, plist);
//        priorityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, slist);

        spinnerPriority.setAdapter(priorityAdapter);
        spinnerStatus.setAdapter(statusAdapter);

        action = getIntent().getStringExtra("action");

        switch (action){
            case "edit":

                Long id = getIntent().getLongExtra("taskId",0);
                task = datasource.getTask(id.toString());

                if(task != null){
                    etTaskName.setText(task.getTaskName());
                    etTaskNotes.setText(task.getNotes());

                    datepicker.setText(task.getDueDate());

                    spinnerPriority.setSelection(priorityAdapter.getPosition(task.getPriority().toString()));
                    spinnerStatus.setSelection(statusAdapter.getPosition(task.getStatus().toString()));
                }
                break;
            case "add":
                    datepicker.setText(sdf.format(today));
                break;
            default:Toast.makeText(this,"Shouldn't have reached here",Toast.LENGTH_SHORT).show();
        }
    }

    public void onSaveItem() {
        if(task == null){
            task=new Tasks();
        }

        if(etTaskName.getText().toString().trim().length()==0){
            Toast.makeText(this,"Task Name Cannot Be Empty",Toast.LENGTH_SHORT).show();
            return;
        }

        task.setTaskName(etTaskName.getText().toString());

        task.setNotes(etTaskNotes.getText().toString());

        task.setDueDate(datepicker.getText().toString());
        task.setPriority(Priority.valueOf(spinnerPriority.getSelectedItem().toString()));
        task.setStatus(Status.valueOf(spinnerStatus.getSelectedItem().toString()));
        writeItems_toDB(task);
        this.finish();
    }

    private void writeItems_toDB(Tasks task) {

        switch (action) {
            case "edit":
                datasource.updateTask(task);
                break;
            case "add":
                datasource.createTask(task);
                break;
            default:Toast.makeText(this,"Shouldn't have reached here",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        datepicker.setText((monthOfYear+1) + "-" + dayOfMonth + "-" + year);

    }

    public void onSetDateAction(View view){
        Bundle args = new Bundle();
        args.putString("date",datepicker.getText().toString());
        datePickerFragment.setArguments(args);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }
}
