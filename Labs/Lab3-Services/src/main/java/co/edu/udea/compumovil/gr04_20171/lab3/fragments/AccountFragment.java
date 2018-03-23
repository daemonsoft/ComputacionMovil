package co.edu.udea.compumovil.gr04_20171.lab3.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.edu.udea.compumovil.gr04_20171.lab3.R;
import co.edu.udea.compumovil.gr04_20171.lab3.services.Config;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    SharedPreferences pref;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        TextView name = (TextView) v.findViewById(R.id.account_name);
        TextView email = (TextView) v.findViewById(R.id.account_email);
        TextView age = (TextView) v.findViewById(R.id.account_age);
        pref = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
        name.setText(pref.getString(Config.NAME_SHARED_PREF, ""));
        email.setText(pref.getString(Config.EMAIL_SHARED_PREF, ""));
        age.setText(Integer.toString(pref.getInt(Config.AGE_SHARED_PREF, 0)));
        return v;
    }
}
