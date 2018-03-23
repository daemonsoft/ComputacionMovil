package co.edu.udea.compumovil.gr04_20171.labscm20171.lab1.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RatingBar;

import co.edu.udea.compumovil.gr04_20171.labscm20171.lab1.R;
import co.edu.udea.compumovil.gr04_20171.labscm20171.lab1.models.Person;

public class OtherInfo extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener {

    CheckBox watchtvcb, readcb, dancecb, singcb, swimcb;
    RatingBar watchtvrb, readrb, dancerb, singrb, swimrb;
    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_info);
        person = getIntent().getParcelableExtra("PERSON");
        watchtvcb = (CheckBox) findViewById(R.id.watchtvcb);
        readcb = (CheckBox) findViewById(R.id.readcb);
        dancecb = (CheckBox) findViewById(R.id.dancecb);
        singcb = (CheckBox) findViewById(R.id.singcb);
        swimcb = (CheckBox) findViewById(R.id.swimcb);

        readrb = (RatingBar) findViewById(R.id.readrb);
        dancerb = (RatingBar) findViewById(R.id.dancerb);
        watchtvrb = (RatingBar) findViewById(R.id.watchtvrb);
        singrb = (RatingBar) findViewById(R.id.singrb);
        swimrb = (RatingBar) findViewById(R.id.swimrb);

        readrb.setStepSize(1);
        dancerb.setStepSize(1);
        watchtvrb.setStepSize(1);
        singrb.setStepSize(1);
        swimrb.setStepSize(1);


        readrb.setOnRatingBarChangeListener(this);
        dancerb.setOnRatingBarChangeListener(this);
        watchtvrb.setOnRatingBarChangeListener(this);
        singrb.setOnRatingBarChangeListener(this);
        swimrb.setOnRatingBarChangeListener(this);


    }

    public void onClickShow(View v) {

        if (readcb.isChecked()) {
            person.setHread((int) readrb.getRating());
        } else {
            person.setHread(-1);
        }
        if (singcb.isChecked()) {
            person.setHsing((int) singrb.getRating());
        } else {
            person.setHsing(-1);
        }
        if (swimcb.isChecked()) {
            person.setHswim((int) swimrb.getRating());
        } else {
            person.setHswim(-1);
        }
        if (watchtvcb.isChecked()) {
            person.setHwatchtv((int) watchtvrb.getRating());
        } else {
            person.setHwatchtv(-1);
        }
        if (dancecb.isChecked()) {
            person.setHdance((int) dancerb.getRating());
        } else {
            person.setHdance(-1);
        }

        Intent intent = new Intent(OtherInfo.this, ShowInfo.class);
        intent.putExtra("PERSON", person);
        startActivity(intent);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        switch (ratingBar.getId()) {
            case (R.id.readrb):
                if (readcb.isChecked()) {
                    person.setHread((int) rating);
                } else {
                    readrb.setRating(0);
                }

                break;
            case (R.id.watchtvrb):
                if (watchtvcb.isChecked()) {
                    person.setHwatchtv((int) rating);
                } else {
                    watchtvrb.setRating(0);
                }
                break;
            case (R.id.dancerb):
                if (dancecb.isChecked()) {
                    person.setHdance((int) rating);
                } else {
                    dancerb.setRating(0);
                }
                break;
            case (R.id.singrb):
                if (singcb.isChecked()) {
                    person.setHsing((int) rating);
                } else {
                    singrb.setRating(0);
                }
                break;
            case (R.id.swimrb):
                if (swimcb.isChecked()) {
                    person.setHswim((int) rating);
                } else {
                    swimrb.setRating(0);
                }
                break;
        }
    }
}
