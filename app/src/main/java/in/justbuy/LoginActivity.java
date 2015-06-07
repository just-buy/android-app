package in.justbuy;

import android.content.Intent;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.HashMap;
import java.util.Map;

import in.justbuy.http.RequestManager;
import in.justbuy.http.RequestPromise;
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

    @Inject
    private Provider<RequestManager> requestManagerProvider;

    @Override
    protected void onResume() {
        super.onResume();

        getActionBar().setTitle("Sign In");

        emailView.setText("aritra.saha@flipkart.com");
        passwordView.setText("aritra123");

        submitView.setOnClickListener(v -> {
            String email = emailView.getText().toString();
            String password = passwordView.getText().toString();

            Log.i("EMAIL", emailView.getText().toString());
            Log.i("PASSWORD", passwordView.getText().toString());

            login(email, password);
        });
    }

    private void login(String email, String password) {
        Map<String, String> dataParams = new HashMap<>();
        dataParams.put("email", email);
        dataParams.put("password", password);

        RequestManager requestManager = requestManagerProvider.get();
        requestManager.setMethod(RequestManager.RequestMethod.POST);
        requestManager.setResourcePath("/user/login");
        requestManager.showProgressDialog(getApplicationContext(), "Logging in ...");
        requestManager.setToDo(new RequestPromise() {
            @Override
            public void onSuccess(String data, int status) {
                Log.i("LOGIN", data);
                if ("true".equalsIgnoreCase(data)) {
                    openAdListing();
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect Login", Toast.LENGTH_SHORT).show();
                }
            }
        });
        requestManager.execute(null, dataParams);
    }

    private void openAdListing() {
        Intent intent = new Intent(this, ListCategoryActivity.class);
        startActivity(intent);
    }

}
