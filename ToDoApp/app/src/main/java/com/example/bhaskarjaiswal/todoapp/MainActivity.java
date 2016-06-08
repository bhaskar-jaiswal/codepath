package com.example.bhaskarjaiswal.todoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> todoItems;
    ArrayAdapter<String> atodoAdapter;
    ListView lvItems;
    EditText etEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(atodoAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);
        lvItems.setOnItemLongClickListener (new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                atodoAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }
    private void populateArrayItems() {
        readItems();
//        todoItems = new ArrayList<String>();
//        todoItems.add("Item 1");
        atodoAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,todoItems);
    }


    public void onAddItem(View view) {
        atodoAdapter.add(etEditText.getText().toString());
        etEditText.setText("");
        writeItems();
    }

    private void readItems(){
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

    }

    private void writeItems(){
        File fileDirs = getFilesDir();
        File file = new File(fileDirs, "todo.txt");
        try{
            FileUtils.writeLines(file,todoItems);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
