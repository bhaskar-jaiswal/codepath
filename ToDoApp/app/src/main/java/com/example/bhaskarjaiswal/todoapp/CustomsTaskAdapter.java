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

    private static class ViewHolder{
        TextView tvTaskName;
        TextView tvPriority;
    }

    public CustomsTaskAdapter(Context context, ArrayList<Tasks> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tasks task = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_list, parent, false);
            viewHolder.tvTaskName = (TextView) convertView.findViewById(R.id.tvTaskName);
            viewHolder.tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTaskName.setText(task.getTaskName());
        viewHolder.tvPriority.setText(task.getPriority().toString());
        return convertView;
    }
}
