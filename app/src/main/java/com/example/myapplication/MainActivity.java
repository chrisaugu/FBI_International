package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button buttonSubmit;
    private EditText firstNameTxt, lastNameTxt, phoneTxt, noOccupantsTxt, noDaysTxt;
    private Spinner roomTypeSpinner;
    private String firstName, lastName, phone, noDays, noOccupants, roomType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstNameTxt = findViewById(R.id.firstName);
        lastNameTxt = findViewById(R.id.lastName);
        phoneTxt = findViewById(R.id.phone);
        noDaysTxt = findViewById(R.id.noDays);
        noOccupantsTxt = findViewById(R.id.noOccupants);
        roomTypeSpinner = findViewById(R.id.roomSpinner);

        List<String> roomTypes = new ArrayList<>();
        roomTypes.add("Standard");
        roomTypes.add("Deluxe");
        roomTypes.add("Suite");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roomTypes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        roomTypeSpinner.setAdapter(dataAdapter);

        roomTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                roomType = item;

//                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonSubmit = findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstName = String.valueOf(firstNameTxt.getText());
                lastName = String.valueOf(lastNameTxt.getText());
                phone = String.valueOf(phoneTxt.getText());
                noDays = String.valueOf(noDaysTxt.getText());
                noOccupants = String.valueOf(noOccupantsTxt.getText());

                makeRequest();
            }
        });
    }

    public void makeRequest() {
        RequestQueue volleyQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://192.168.1.14/fbi_international/api/makeReservation.php";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//
                        Log.d(TAG, "makeRequest: " + response);
//
                        Snackbar.make(MainActivity.this.getCurrentFocus(), response, Snackbar.LENGTH_LONG).show();
//                            Toast.makeText(MainActivity.this, "Hello " + jsonObject.getString("firstName"), Toast.LENGTH_SHORT).show();
//
//                        } catch (JSONException e) {
//                            e.fillInStackTrace();
//                            throw new RuntimeException(e);
//                        }

                        firstNameTxt.setText("");
                        lastNameTxt.setText("");
                        phoneTxt.setText("");
                        noDaysTxt.setText("");
                        noOccupantsTxt.setText("");
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.fillInStackTrace();
                        Log.e("MainActivity", "Fail to get response = " + error);
                    }
                }
        )
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("firstName", firstName);
                params.put("lastName", lastName);
                params.put("phone", phone);
                params.put("noDays", noDays);
                params.put("noOccupants", noOccupants);
                params.put("roomType", roomType);

                return params;
            }
        };

        volleyQueue.add(request);
    }

}