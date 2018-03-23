package co.edu.udea.compumovil.gr04_20171.lab4.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.atomic.AtomicInteger;

import co.edu.udea.compumovil.gr04_20171.lab4.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {
    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView aboutLink = (TextView) view.findViewById(R.id.about_link);
        TextView daemonLink = (TextView) view.findViewById(R.id.about_daemonsoft);
        TextView remLink = (TextView) view.findViewById(R.id.about_rem);
        daemonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://github.com/daemonsoft";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        remLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://github.com/rem5742";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        AtomicInteger msgId = new AtomicInteger();
                            FirebaseMessaging fm = FirebaseMessaging.getInstance();
                            fm.send(new RemoteMessage.Builder("1023607157396" + "@gcm.googleapis.com")
                                    .setMessageId(Integer.toString(msgId.incrementAndGet()))
                                    .addData("Nuevo evento", "lala")
                                    .build());
        aboutLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://sites.google.com/view/jaiberyepes/mobile-computing?authuser=0";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        // aboutLink.setText(Html.fromHtml("<a style=\"color: blue\" href=\"https://github.com/daemonsoft\">© 2017 DaemonSoft</a>"));
        // aboutLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
