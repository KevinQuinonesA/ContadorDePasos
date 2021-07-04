package com.example.contadordepasos.datainformation;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contadordepasos.R;
import com.example.contadordepasos.models.User;

import java.util.Calendar;
import java.util.Objects;

public class PerfilInformation extends AppCompatActivity {

    Button buttonRegister;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private EditText editTextTextPersonName;
    private EditText editTextTextPersonName4;
    private EditText editTextDate;
    private EditText editTextNumber;
    private EditText editTextWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).hide();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        editTextTextPersonName4 = findViewById(R.id.editTextTextPersonName4);
        editTextDate = findViewById(R.id.editTextDate);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextWeight = findViewById(R.id.editTextWeight);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(view -> {
            User user = new User(
                    editTextTextPersonName.getText().toString(),
                    editTextTextPersonName4.getText().toString(),
                    editTextDate.getText().toString(),
                    Integer.parseInt(editTextNumber.getText().toString()),
                    Float.parseFloat(String.valueOf(editTextWeight.getText()))
                    );
            savePreferences(user);
            Toast.makeText(this,"Informacion actualizada",Toast.LENGTH_SHORT).show();
        });
        User user = readPreferences();
        editTextTextPersonName.setText(user.getName());
        editTextTextPersonName4.setText(user.getLast_name());
        editTextDate.setText(user.getDate_party());
        editTextNumber.setText(String.valueOf(user.getAge()));
        editTextWeight.setText(String.valueOf(user.getWeight()));
    }
    private void savePreferences(User user){
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name",user.getName());
        editor.putString("last_name",user.getLast_name());
        editor.putString("date_party",user.getDate_party());
        editor.putInt("age",user.getAge());
        editor.putFloat("weight",user.getWeight());
        editor.commit();
    }
    private User readPreferences(){
        SharedPreferences preferences = getSharedPreferences(
                "user",Context.MODE_PRIVATE);
        User user = new User(
                preferences.getString("name",""),
                preferences.getString("last_name",""),
                preferences.getString("date_party",""),
                preferences.getInt("age",0),
                preferences.getFloat("weight",0)
        );
        return user;
    }
}