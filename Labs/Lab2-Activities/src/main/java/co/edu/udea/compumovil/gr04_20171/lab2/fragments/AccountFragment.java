package co.edu.udea.compumovil.gr04_20171.lab2.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.edu.udea.compumovil.gr04_20171.lab2.R;
import co.edu.udea.compumovil.gr04_20171.lab2.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private User mUser;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = getArguments().getParcelable("USER");
        }
        //Foto, ubicación,
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        TextView name = (TextView) v.findViewById(R.id.account_name);
        TextView email = (TextView) v.findViewById(R.id.account_email);
        TextView age = (TextView) v.findViewById(R.id.account_age);

        name.setText(mUser.getName());
        email.setText(mUser.getEmail());
        age.setText((mUser.getAge() == 0) ? getString(R.string.not_entered) : mUser.getAge() + "");
        return v;
    }

}
