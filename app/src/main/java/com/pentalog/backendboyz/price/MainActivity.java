package com.pentalog.backendboyz.price;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginButton;
    private TextView mSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmailField = (EditText) findViewById(R.id.emailEditText);
        mPasswordField = (EditText) findViewById(R.id.passwordEditText);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mSignUp = (TextView) findViewById(R.id.signupTextView);


        mEmailField.addTextChangedListener(mEmailFieldWatcher);
        mPasswordField.addTextChangedListener(mPasswordFieldWatcher);
/*
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEmailField.getError() == null && mPasswordField.getError() == null){
                    if (mEmailField.getText().toString().equals("ghita@pentalog.ro" )&& mPasswordField.getText().toString().equals("123456789"))
                    {
                        Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_LONG).show();
                        //Intent intent = new Intent(MainActivity.this,App.class);
                        //startActivity(intent);

                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "FAIL", Toast.LENGTH_LONG).show();
                    }
                    Log.d("TEST",mEmailField.getText().toString());
                    Log.d("TEST",mPasswordField.getText().toString());
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Fix your errors mate!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
*/
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);
            }
        });

    }


    private TextWatcher mEmailFieldWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!isValidEmail(mEmailField.getText().toString()) || mEmailField.getText().toString().equals("")){
                mEmailField.setError("Enter a valid email address ,bro");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private TextWatcher mPasswordFieldWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(mPasswordField.getText().length() < 8 || mPasswordField.getText().toString().equals("")){
                mPasswordField.setError("Too short");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    public final static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            //android Regex to check the email address Validation
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void OnLogin(View view){
        if(mEmailField.getError() == null && mPasswordField.getError() == null
                && !mEmailField.getText().toString().equals("") && !mPasswordField.getText().toString().equals("")) {
            //if (mEmailField.getText().toString().equals("ghita@pentalog.ro") && mPasswordField.getText().toString().equals("123456789")) {
                //Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_LONG).show();

                String email = mEmailField.getText().toString();
                String password = mPasswordField.getText().toString();
                String type = "login";
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.execute(type, email, password);

            //}
        }
    }
}
