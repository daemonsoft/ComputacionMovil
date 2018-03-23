package co.edu.udea.compumovil.gr04_20171.labscm20171.lab1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import co.edu.udea.compumovil.gr04_20171.labscm20171.lab1.R;
import co.edu.udea.compumovil.gr04_20171.labscm20171.lab1.models.Person;

public class ContactInfo extends AppCompatActivity {

    EditText txtTelefono,txtEmail,txtPais,txtCiudad,txtDireccion;
    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPais = (EditText) findViewById(R.id.txtPais);
        txtCiudad = (EditText) findViewById(R.id.txtCiudad);
        txtDireccion = (EditText) findViewById(R.id.txtDireccion);

        person = getIntent().getParcelableExtra("PERSON");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.txtPais);
        textView.setAdapter(adapter);

        ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, CIITES);
        AutoCompleteTextView textViewCity = (AutoCompleteTextView)
                findViewById(R.id.txtCiudad);
        textViewCity.setAdapter(adapterCity);
    }

    public void onClickNext(View v) {
        if (txtTelefono.getText().toString().equals("") ||
            txtEmail.getText().toString().equals("") ||
            txtPais.getText().toString().equals("") ||
            txtCiudad.getText().toString().equals("") ||
            txtDireccion.getText().toString().equals(""))
        {
            (Toast.makeText(this, getResources().getString(R.string.all_data), Toast.LENGTH_LONG)).show();
        }
        else {
            Intent intent = new Intent(ContactInfo.this, OtherInfo.class);
            person.setPhone(txtTelefono.getText().toString());
            person.setEmail(txtEmail.getText().toString());
            person.setCountry(txtPais.getText().toString());
            person.setCity(txtCiudad.getText().toString());
            person.setAddress(txtDireccion.getText().toString());
            intent.putExtra("PERSON", person);
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("telefono", txtTelefono.getText().toString());
        savedInstanceState.putString("email", txtEmail.getText().toString());
        savedInstanceState.putString("pais", txtPais.getText().toString());
        savedInstanceState.putString("ciudad", txtCiudad.getText().toString());
        savedInstanceState.putString("direccion", txtDireccion.getText().toString());
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        txtTelefono.setText(savedInstanceState.getString("telefono"));
        txtEmail.setText(savedInstanceState.getString("email"));
        txtPais.setText(savedInstanceState.getString("pais"));
        txtCiudad.setText(savedInstanceState.getString("ciudad"));
        txtDireccion.setText(savedInstanceState.getString("direccion"));
    }

    private static final String[] COUNTRIES = new String[] {
            "Argentina",
            "Bolivia",
            "Brasil",
            "Chile",
            "Colombia",
            "Costa Rica",
            "Cuba",
            "Ecuador",
            "El Salvador",
            "Guatemala",
            "Haití",
            "Honduras",
            "México20",
            "Nicaragua",
            "Panamá",
            "Paraguay",
            "Perú",
            "Puerto Rico",
            "República Dominicana",
            "Uruguay",
            "Venezuela"
    };
    private static final String[] CIITES = new String[] {
            "Bogotá",
            "Santa",
            "Medellín",
            "Villavicencio",
            "Santiago",
            "Bello",
            "Barranquilla",
            "Valledupar",
            "Cartagena",
            "Pereira",
            "Soledad",
            "Buenaventura",
            "Cúcuta",
            "San",
            "Ibagué",
            "Manizales",
            "Soacha",
            "Montería",
            "Bucaramanga",
            "Neiva"
    };
}
