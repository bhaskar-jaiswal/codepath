package com.example.bjaiswal.todoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.constants.Priority;
import com.example.constants.Status;
import com.example.datasource.Tasks;
import com.example.datasource.TasksDataSource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditTaskActivity extends AppCompatActivity {

    private TasksDataSource datasource;

    EditText etTaskName;
    DatePicker datepicker;
    EditText etTaskNotes;
    Spinner spinnerPriority;
    Spinner spinnerStatus;
    Button save;
    Tasks task;

    String action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        datasource = new TasksDataSource(this);
        datasource.open();

        etTaskName = (EditText) findViewById(R.id.etTaskName);
        datepicker = (DatePicker) findViewById(R.id.datePicker);
        etTaskNotes = (EditText) findViewById(R.id.etTaskNotes);
        spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);
        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
        save = (Button) findViewById(R.id.save);

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

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, slist);

        spinnerPriority.setAdapter(priorityAdapter);
        spinnerStatus.setAdapter(statusAdapter);

        action = getIntent().getStringExtra("action");

        switch (action){
            case "edit":
//                Toast.makeText(this,"Edit",Toast.LENGTH_SHORT).show();

                Long id = getIntent().getLongExtra("taskId",0);
                task = datasource.getTask(id.toString());

                if(task != null){
                    etTaskName.setText(task.getTaskName());
                    etTaskNotes.setText(task.getNotes());
//                    Toast.makeText(this,task.getPriority().getValue()+":"+priorityAdapter.getPosition(task.getPriority().toString()),Toast.LENGTH_SHORT).show();

                    String date = task.getDueDate();
                    int month = Integer.parseInt(date.substring(0,date.indexOf("-")));
                    int day = Integer.parseInt(date.substring(date.indexOf("-")+1,date.lastIndexOf("-")));
                    int year = Integer.parseInt(date.substring(date.lastIndexOf("-")+1));


                    datepicker.updateDate(year, month, day);

                    spinnerPriority.setSelection(priorityAdapter.getPosition(task.getPriority().toString()));
                    spinnerStatus.setSelection(statusAdapter.getPosition(task.getStatus().toString()));
                }

                break;
            case "add":
//                Toast.makeText(this,"Add",Toast.LENGTH_SHORT).show();



//                spinnerPriority.setSelection(2);
//                spinnerStatus.setSelection(1);

                break;
            default:Toast.makeText(this,"Shouldn't have reached here",Toast.LENGTH_SHORT).show();
        }

//        etTaskName.setText();

//        Toast.makeText(this, "Id of item: "+id.toString(), Toast.LENGTH_LONG).show();
    }

    public void onSaveItem(View view) {
        if(task == null){
            task=new Tasks();
        }

        if(etTaskName.getText().toString().trim().length()==0){
            Toast.makeText(this,"Task Name Cannot Be Empty",Toast.LENGTH_SHORT).show();
            return;
        }

        task.setTaskName(etTaskName.getText().toString());

        task.setNotes(etTaskNotes.getText().toString());

        int day = datepicker.getDayOfMonth();
        int month = datepicker.getMonth()-1;
        int year = datepicker.getYear()-1900;

        /*Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);*/

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = dateFormat.format(new Date(year,month,day));

        Toast.makeText(this, "formattedDate: "+formattedDate, Toast.LENGTH_LONG).show();

        task.setDueDate(formattedDate);
        task.setPriority(Priority.valueOf(spinnerPriority.getSelectedItem().toString()));
        task.setStatus(Status.valueOf(spinnerStatus.getSelectedItem().toString()));
        writeItems_toDB(task);
        this.finish();
    }

    private void writeItems_toDB(Tasks task) {
//        datasource.createTask(task.getTaskName(), task.getDueDate(), task.getNotes(),task.getPriority().getValue(),task.getStatus().getValue());


        switch (action) {
            case "edit":
                Toast.makeText(this,task.getPriority().toString()+" "+task.getStatus().toString(),Toast.LENGTH_SHORT).show();
                datasource.updateTask(task);
                break;
            case "add":
                datasource.createTask(task);
                break;
            default:Toast.makeText(this,"Shouldn't have reached here",Toast.LENGTH_SHORT).show();
        }

    }
}
