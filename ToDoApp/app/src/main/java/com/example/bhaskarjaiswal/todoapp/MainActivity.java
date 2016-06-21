package com.example.bhaskarjaiswal.todoapp;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.datasource.Tasks;
import com.example.datasource.TasksDataSource;
import com.example.dialogs.TaskAlertDialogFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CustomsTaskAdapter ctodoAdapter;
    private Tasks t;
    private ListView lvItems;
    private EditText etEditText;
    private TasksDataSource datasource;
    private Tasks task;
    private Toolbar mToolbar;
    private FloatingActionButton fab;
    private CoordinatorLayout coordinatorLayout;
    private TaskAlertDialogFragment taskFragment;
    private ArrayList<Tasks> todoItems = new ArrayList<Tasks>();
    private ArrayList<Long> itemsToBeDeleted = new ArrayList<Long>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        lvItems = (ListView) findViewById(R.id.lvItems);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        datasource = new TasksDataSource(this);
        datasource.open();

        populateArrayItems();


        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "Message is deleted", Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        view.setBackgroundColor(Color.parseColor("#009688"));

        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                if (itemsToBeDeleted.size() > 0) {
                    removeItems_fromDB(itemsToBeDeleted);
                }
            }

            @Override
            public void onShown(Snackbar snackbar) {
                super.onShown(snackbar);
            }
        });


        taskFragment = new TaskAlertDialogFragment();
        taskFragment.setOnChooseOptionsActionListener(new TaskAlertDialogFragment.ChooseOptionsActionListener() {
            @Override
            public void onOptionsChosenDialogEdit(int position, Tasks task) {
                Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
                intent.putExtra("taskId", task.getId());
                intent.putExtra("action", "edit");
                startActivity(intent);

            }

            @Override
            public void onOptionsChosenDialogDelete(int position, Tasks task) {
                showAlertDialog(position, task, snackbar);
            }
        });

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showAlertDialog(position, todoItems.get(position), snackbar);
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tasks task = (Tasks) parent.getItemAtPosition(position);
                showDialogFragment(position, task);
            }
        });
    }

    private void showAlertDialog(final int position, final Tasks task, final Snackbar snackbar){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);


        alertDialogBuilder.setTitle(task.getTaskName());

        alertDialogBuilder
                .setMessage("Would You Like To Delete This Task ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        todoItems.remove(position);
                        ctodoAdapter.notifyDataSetChanged();
                        if(itemsToBeDeleted.size() > 0){
                            removeItems_fromDB(itemsToBeDeleted);
                        }
                        itemsToBeDeleted.add(task.getId());

                        snackbar.setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Message is restored!", Snackbar.LENGTH_SHORT);
                                View view1 = snackbar1.getView();
                                view1.setBackgroundColor(Color.parseColor("#009688"));
                                todoItems.add(position, task);
                                ctodoAdapter.notifyDataSetChanged();
                                itemsToBeDeleted.clear();
                                snackbar1.show();
                            }
                        });
                        snackbar.show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();


    }

    private void showDialogFragment(int position, Tasks task){
        FragmentManager fragment = getFragmentManager();
//        TaskAlertDialogFragment taskFragment = TaskAlertDialogFragment.newInstance();
//        taskFragment = new TaskAlertDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("task", task);
        args.putInt("position",position);
        taskFragment.setArguments(args);
        taskFragment.show(fragment, "task_description_fragment");
    }

    private void populateArrayItems() {
        readItems_fromDB();

        Log.d("todoitem.size=", todoItems.size()+"");

        ctodoAdapter = new CustomsTaskAdapter(this, todoItems);
        lvItems.setAdapter(ctodoAdapter);
    }

    public void onAddItem(View view) {
        Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
        intent.putExtra("action", "add");
        startActivity(intent);
    }

    private void removeItems_fromDB(ArrayList<Long> itemsToBeDeleted) {
        StringBuffer listOfIds = new StringBuffer();
        listOfIds.append("(");
        listOfIds.append(itemsToBeDeleted.get(0));
        for(int i=1;i<itemsToBeDeleted.size();i++){
            listOfIds.append(",");
            listOfIds.append(itemsToBeDeleted.get(i));
        }
        listOfIds.append(")");
        itemsToBeDeleted.clear();
        datasource.deleteTasks(listOfIds.toString());
    }

    protected void onResume() {
        super.onResume();
        populateArrayItems();
    }

    private void readItems_fromDB() {
        todoItems.clear();
        todoItems.addAll(datasource.getAllTasks());
    }
}