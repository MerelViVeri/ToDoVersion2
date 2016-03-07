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
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Author: Merel van den Hurk
 * App name: To Do
 * App description: app keeps track of to do's as entered by user based on chosen list.
 * Writes items to JSON upon closing and uses this to restore.
 */

// TODO find out which activity opens after closing
// TODO implement JSON: keep track of lists and their titles and corresponding items and counts
    // TODO each list as "sub-objects" within one big object called allLists

public class Items extends AppCompatActivity {

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
            // if file exists
            Scanner scan = null;
            try {
                // TODO adapt reading for multiple lists handling
                // TODO parse JSON if needed?
                scan = new Scanner(openFileInput("listsave.txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            assert scan != null;
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                listArray.add(line);
            }

            // if file doesn't exist
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

    @Override
    protected void onPause() {
        super.onPause();
        writeFile(listArray);
    }

    protected void writeFile(ArrayList array) {
        PrintStream out = null;
        // TODO change: adapt for JSON object and write _that_ to listsave.txt
        try {
            out = new PrintStream(openFileOutput("listsave.txt", MODE_PRIVATE));
            // TODO write for each loop for writing
                out.println();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
