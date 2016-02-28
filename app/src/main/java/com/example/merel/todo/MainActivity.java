package com.example.merel.todo;

import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Author: Merel van den Hurk
 * App name: To Do
 * App description: app keeps track of to do's as entered by user. Retains list
 * on orientation change and closing. Simple click strikes through an item,
 * long click deletes it from the list.
 *
 * NOTE: resuming after closing works on emulator but does not seem to work on own phone.
 * I tried to fix this but after three hours of puzzling I still couldn't find a way that
 * wasn't just blind copy-paste from Google (which is bad form), so I left it this way.
 */

public class MainActivity extends AppCompatActivity {

    // initialize variables
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> listArray;

    EditText addField;
    Button addButton;
    ListView toDoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addField = (EditText) findViewById(R.id.addField);
        addButton = (Button) findViewById(R.id.addButton);
        toDoList = (ListView) findViewById(R.id.toDoList);

        // restore saved state if present
        if (savedInstanceState != null) {
            listArray = savedInstanceState.getStringArrayList("ARRAY_LIST");
        } else {
            listArray = new ArrayList<>();
        }

        // initialize adapter
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listArray);
        toDoList.setAdapter(arrayAdapter);

        // delete item on long click
        toDoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = arrayAdapter.getItem(position);
                listArray.remove(item);
                arrayAdapter.notifyDataSetChanged();
                return false;
            }
        });

        /**
         * This doesn't work properly but I still leave it in. The goal is to simply strike through
         * any items upon a normal click, but I couldn't find an easy way to undo this and it also
         * strikes through items below items that get removed (because of the lingering longClick).
         * Might have found a solution with some more Googling, perhaps adding checked/unchecked
         * booleans to individual items somehow and using if-else statements to fix it.
         */
        toDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView item = (TextView) toDoList.getChildAt(position);
                item.setPaintFlags(item.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }

    // add user input to list
    public void buttonClicked(View view) {
        String input = addField.getText().toString();
        listArray.add(input);
        arrayAdapter.notifyDataSetChanged();
        addField.setText("");
    }

    // save to do list for later
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList("ARRAY_LIST", listArray);
    }

    // restore saved state
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        listArray = savedInstanceState.getStringArrayList("ARRAY_LIST");
    }
}
