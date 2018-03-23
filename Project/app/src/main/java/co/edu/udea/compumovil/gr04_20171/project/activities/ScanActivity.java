package co.edu.udea.compumovil.gr04_20171.project.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScanActivity extends AppCompatActivity {

    private static final String TAG = "ScanActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new IntentIntegrator((Activity) this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Intent intent = new Intent(this, MenuActivity.class);

                Map<String, String> params = new LinkedHashMap<>();
                try {
                    params = splitQuery(result.getContents().replace("https://a-la-orden-e8295.firebaseapp.com/?", ""));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(params.get("uid"))) {
                    intent.putExtra("MESA_ID", params.get("mesa"));
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Código no válido " + params.get("uid") + " " + params.get("mesa") + FirebaseAuth.getInstance().getCurrentUser().getUid(), Toast.LENGTH_LONG).show();
                }
                finish();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }
}
