package com.example.bhaskarjaiswal.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.datasource.Tasks;

import java.util.ArrayList;

/**
 * Created by bhaskarjaiswal on 6/10/16.
 */
public class CustomsTaskAdapter extends ArrayAdapter<Tasks> {

    public CustomsTaskAdapter(Context context, ArrayList<Tasks> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tasks task = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_list, parent, false);
        }
        TextView tvTaskName = (TextView) convertView.findViewById(R.id.tvTaskName);
        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
        tvTaskName.setText(task.getTaskName());
        tvPriority.setText(task.getPriority().toString());
        return convertView;
    }
}
