package co.edu.udea.compumovil.gr04_20171.labscm20171.lab1.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import co.edu.udea.compumovil.gr04_20171.labscm20171.lab1.R;
import co.edu.udea.compumovil.gr04_20171.labscm20171.lab1.models.Person;

public class ShowInfo extends AppCompatActivity {
    TextView showText;
    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);
        person = getIntent().getParcelableExtra("PERSON");
        showText = (TextView) findViewById(R.id.showText);
        String education = person.getEducation() == 0 ? getResources().getString(R.string.school) :
                (person.getEducation() == 1 ? getResources().getString(R.string.college) :
                        (person.getEducation() == 2) ? getResources().getString(R.string.university) : getResources().getString(R.string.other));

        SimpleDateFormat postFormater = new SimpleDateFormat("MMMM dd, yyyy");

        String resume = "";
        resume += getResources().getString(R.string.resume_data) + " \n\n";
        resume += getResources().getString(R.string.name) + ": " + person.getName() + "\n";
        resume += getResources().getString(R.string.lastname) + ": " + person.getLastname() + "\n";
        resume += getResources().getString(R.string.gender) + ": " + (person.getGender() == 0 ? getResources().getString(R.string.male) : getResources().getString(R.string.female)) + "\n";
        resume += getResources().getString(R.string.birthdate) + ": " + postFormater.format(person.getBirthdate()) + "\n";
        resume += getResources().getString(R.string.education) + ":" + education + "\n";
        resume += getResources().getString(R.string.telefonoHint) + ": " + person.getPhone() + "\n";
        resume += getResources().getString(R.string.emailHint) + ": " + person.getEmail() + "\n";
        resume += getResources().getString(R.string.paisHint) + ": " + person.getCountry() + "\n";
        resume += getResources().getString(R.string.ciudadHint) + ": " + person.getCity() + "\n";
        resume += getResources().getString(R.string.direccionHint) + ": " + person.getAddress() + "\n";

        resume += "\n" + getResources().getString(R.string.hobbby) + "\n";

        if (person.getHread() != -1)
            resume += getResources().getString(R.string.read) + ": " + person.getHread() + " " + getResources().getString(R.string.stars) + "\n";
        if (person.getHwatchtv() != -1)
            resume += getResources().getString(R.string.watch_tv) + ": " + person.getHwatchtv() + " " + getResources().getString(R.string.stars) + "\n";
        if (person.getHdance() != -1)
            resume += getResources().getString(R.string.dance) + ": " + person.getHdance() + " " + getResources().getString(R.string.stars) + "\n";
        if (person.getHsing() != -1)
            resume += getResources().getString(R.string.sing) + ": " + person.getHsing() + " " + getResources().getString(R.string.stars) + "\n";
        if (person.getHswim() != -1)
            resume += getResources().getString(R.string.swim) + ": " + person.getHswim() + " " + getResources().getString(R.string.stars) + "\n";
        showText.setText(resume);
    }
}
