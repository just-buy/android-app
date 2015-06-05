package in.justbuy;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_login)
public class LoginActivity extends RoboActivity {

    @InjectView(R.id.email)
    private AutoCompleteTextView emailView;

    @InjectView(R.id.password)
    private EditText passwordView;

    @InjectView(R.id.email_sign_in_button)
    private Button submitView;

    @Override
    protected void onResume() {
        super.onResume();

        submitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("EMAIL", emailView.getText().toString());
                Log.i("PASSWORD", passwordView.getText().toString());
            }
        });
    }
}
