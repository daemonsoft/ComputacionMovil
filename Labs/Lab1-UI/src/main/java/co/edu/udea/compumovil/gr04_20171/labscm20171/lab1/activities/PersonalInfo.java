package co.edu.udea.compumovil.gr04_20171.labscm20171.lab1.activities;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import co.edu.udea.compumovil.gr04_20171.labscm20171.lab1.R;
import co.edu.udea.compumovil.gr04_20171.labscm20171.lab1.models.Person;

public class PersonalInfo extends AppCompatActivity {
    EditText name, lastname, birthdate;
    Spinner education;
    int gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.personal_info);
        setContentView(R.layout.activity_personal_info);
        education = (Spinner) findViewById(R.id.education);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.education_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        education.setAdapter(adapter);
        birthdate = (EditText) findViewById(R.id.birthdatetext);
        name = (EditText) findViewById(R.id.nametext);
        lastname = (EditText) findViewById(R.id.lastnametext);
    }

    public void onClickGender(View v) {
        if (v.getId() == R.id.male) {
            gender = 0;
        } else {
            gender = 1;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (((RadioButton)findViewById(R.id.male)).isActivated()) {
            gender = 0;
        } else {
            gender = 1;
        }
        savedInstanceState.putString("name", name.getText().toString());
        savedInstanceState.putString("lastname", lastname.getText().toString());
        savedInstanceState.putString("birthdate", birthdate.getText().toString());
        savedInstanceState.putInt("education", education.getSelectedItemPosition());
        savedInstanceState.putInt("gender", gender);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        name.setText(savedInstanceState.getString("name"));
        lastname.setText(savedInstanceState.getString("lastname"));
        birthdate.setText(savedInstanceState.getString("birthdate"));
        education.setSelection(savedInstanceState.getInt("education"));
        if (savedInstanceState.getInt("gender") == 1)
            ((RadioButton)findViewById(R.id.female)).setActivated(true);
        else
            ((RadioButton)findViewById(R.id.male)).setActivated(true);
    }

    public void onClickNext(View v) {

        if (name.getText().toString().equals("") ||
                lastname.getText().toString().equals("") ||
                birthdate.getText().toString().equals(""))
        {
            (Toast.makeText(this, getResources().getString(R.string.all_data), Toast.LENGTH_LONG)).show();
        }
        else {
            Intent intent = new Intent(PersonalInfo.this, ContactInfo.class);
            String sname = name.getText().toString();
            String slastname = lastname.getText().toString();
            Person person = new Person(sname, slastname,
                    new Date(birthdate.getText().toString()), education.getSelectedItemPosition(), gender);
            intent.putExtra("PERSON", person);
            startActivity(intent);
        }
    }

    public void showDatePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int dyear, int dmonth, int dday) {

                birthdate.setText(dday + "/" + (dmonth + 1) + "/" + dyear);
            }
        }, day, month, year);
        datePickerDialog.show();
    }
}
