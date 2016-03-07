package com.example.merel.todo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This activity displays a list of the user's to do lists and gives the user the
 * possibility of adding and/or deleting lists. Tapping a list brings the user to
 * the corresponding list of items.
 */

public class Lists extends AppCompatActivity {

    JSONObject allLists;
    EditText addField;
    Button addButton;
    ListView lists;

    ArrayAdapter<String> listAdapter;
    ArrayList<String> listArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        addField = (EditText) findViewById(R.id.addField);
        addButton = (Button) findViewById(R.id.addButton);
        lists = (ListView) findViewById(R.id.lists);

        // get previous saved instance if possible
        if (savedInstanceState != null) {
            // get bundle from savedInstanceState
            listArray = savedInstanceState.getStringArrayList("ARRAY_LIST");
            allLists = savedInstanceState.getParcelable("ALL_LISTS");
        }else{
            // try to read previously saved file
                // TODO open file listsave.txt
                // TODO read/parse contents & get allLists & listsArray
            // else create everything anew
            JSONObject allLists = new JSONObject();
            try {
                allLists.put("listsCount", 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listArray = new ArrayList<>();
            listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listArray);
            lists.setAdapter(listAdapter);
        }

        // delete item on long click
        lists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = listAdapter.getItem(position);
                listArray.remove(item);
                listAdapter.notifyDataSetChanged();
                return false;

                // TODO remove list from JSON object
            }
        });
    }

    /**
     * Creates new list using user input and adds it to allLists and ListView
     */
    public void addNewList(String title) {
        // create new list object with empty array of items
        JSONObject newList = new JSONObject();
        try {
            newList.put("title", title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            newList.put("itemsCount", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        newList.put("itemsArray", []);

        // add list to JSON
        try {
            allLists.put(title, newList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            allLists.put("listsCount", +1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // add list to ListView
        listArray.add(title);
        listAdapter.notifyDataSetChanged();
        addField.setText("");

        // TODO take user to Items activity with empty list
    }

    /**
     * Writes all data to textfile to save for later use
     */
    public void saveLists() {
        // TODO write allLists to listsave.txt (using JSON?)
    }

    public void buttonClicked(View view) {
        String title = addField.getText().toString();
        addNewList(title);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList("ARRAY_LIST", listArray);
        // make allLists parcelable
        // TODO make parcelable from allLists
        savedInstanceState.putParcelable("ALL_LISTS", allLists);
    }
}

