package com.example.bjaiswal.todoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.datasource.Tasks;
import com.example.datasource.TasksDataSource;

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
    private ArrayList<Tasks> todoItems = new ArrayList<Tasks>();
    private ArrayList<Long> itemsToBeDeleted = new ArrayList<Long>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        lvItems = (ListView) findViewById(R.id.lvItems);
//        etEditText = (EditText) findViewById(R.id.etEditText);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

//        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        datasource = new TasksDataSource(this);
        datasource.open();

        populateArrayItems();

        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "Message is deleted", Snackbar.LENGTH_LONG);

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int posDeleted = position;

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                alertDialogBuilder.setTitle(todoItems.get(posDeleted).getTaskName());

                alertDialogBuilder
                        .setMessage("Would You Like To Delete This Task ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                t = todoItems.remove(posDeleted);
                                ctodoAdapter.notifyDataSetChanged();
                                itemsToBeDeleted.add(t.getId());

                                snackbar.setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Message is restored!", Snackbar.LENGTH_SHORT);
                                        todoItems.add(posDeleted, t);
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

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tasks task = (Tasks) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
                intent.putExtra("taskId", task.getId());
                intent.putExtra("action", "edit");
                startActivity(intent);
            }
        });

        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                if (itemsToBeDeleted.size() > 0) {
                    removeItems_fromDB(itemsToBeDeleted.get(0));
                }
            }

            @Override
            public void onShown(Snackbar snackbar) {
                super.onShown(snackbar);
            }
        });
    }

    private void populateArrayItems() {
//        readItems();
//        atodoAdapter = new ArrayAdapter<Tasks>(this, android.R.layout.simple_list_item_1, todoItems);
        readItems_fromDB();

        System.out.println("todoitem.size=" + todoItems.size());

        ctodoAdapter = new CustomsTaskAdapter(this, todoItems);
        lvItems.setAdapter(ctodoAdapter);
    }

    public void onAddItem(View view) {
       /* atodoAdapter.add(etEditText.getText().toString());
        etEditText.setText("");
        writeItems();*/

        /*writeItems_toDB();
        etEditText.setText("");
        ctodoAdapter.add(task);*/

        Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
        intent.putExtra("action", "add");
        startActivity(intent);
    }

    private void readItems_fromDB() {
        todoItems.clear();
        todoItems.addAll(datasource.getAllTasks());
//        Log.e("item1 Name: ", todoItems.get(0).getTaskName());
//        Log.e("item1 Priority: " , todoItems.get(0).getPriority().name());
//        Log.e("item1 Name: ", todoItems.get(1).getTaskName());
//        Log.e("item1 Priority: " , todoItems.get(1).getPriority().name());
    }

    /*private void writeItems_toDB() {
        System.out.println("Text = " + etEditText.getText().toString());
        task = datasource.createTask(etEditText.getText().toString(), "", "", "", "");
    }*/

    private void removeItems_fromDB(Long id) {
        datasource.deleteComment(id);
    }

    protected void onResume() {
        super.onResume();
        populateArrayItems();
    }



    /*private void readItems(){
        File fileDirs = getFilesDir();
        File file = new File(fileDirs, "todo.txt");
        if(file.exists()){
            Toast.makeText(getApplicationContext(),"FILES EXISTS",Toast.LENGTH_SHORT).show();
            try{
                todoItems = new ArrayList<String>(FileUtils.readLines(file));
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(),"FILE DOES NOT EXISTS",Toast.LENGTH_SHORT).show();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    /*private void writeItems(){
        File fileDirs = getFilesDir();
        File file = new File(fileDirs, "todo.txt");
        try{
            FileUtils.writeLines(file,todoItems);
        }catch (IOException e){
            e.printStackTrace();
        }
    }*/
}
