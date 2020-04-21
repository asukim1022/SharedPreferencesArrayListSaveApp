package com.asukim.sharedpreferencesarraylistsave;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView textView;
    EditText editText;
    Button button;

    ArrayAdapter<String> adapter;
    ArrayList<String> array = new ArrayList<>();

    // ArrayList -> Json으로 변환
    private static final String SETTINGS_PLAYER_JSON = "settings_item_json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.textView);

        array = getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String str = array.get(position);
                textView.setText(str);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = editText.getText().toString();
                if (str.length() != 0) {
                    array.add(str + "");
                    adapter.notifyDataSetChanged();

                    editText.setText("");
                }
            }
        });


    }


    private void setStringArrayPref(Context context, String key, ArrayList<String> values) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();

        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }

        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }

        editor.apply();
    }

    private ArrayList getStringArrayPref(Context context, String key) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList urls = new ArrayList();

        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);

                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    @Override
    protected void onPause() {
        super.onPause();

        setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON, array);
        Log.d(TAG, "Put json");
    }
}