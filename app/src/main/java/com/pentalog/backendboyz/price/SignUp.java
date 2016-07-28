package com.pentalog.backendboyz.price;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SignUp extends AppCompatActivity {
    private EditText mEmailField;
    private EditText mPass1Field;
    private EditText mPass2Field;
    private Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmailField = (EditText) findViewById(R.id.emailEditText);
        mPass1Field = (EditText) findViewById(R.id.pass1EditText);
        mPass2Field = (EditText) findViewById(R.id.pass2EditText);
        mSignUpButton = (Button) findViewById(R.id.signUpButton);


        mEmailField.addTextChangedListener(mEmailFieldWatcher);
        mPass1Field.addTextChangedListener(mPass1FieldWatcher);
        mPass2Field.addTextChangedListener(mPass2FieldWatcher);

       /* mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmailField.getError() == null && mPass2Field.getError() == null && mPass1Field.getError() == null){
                    Toast.makeText(SignUp.this, "Account Created.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SignUp.this, "FAIL", Toast.LENGTH_SHORT).show();
                }
            }

        });
        */
    }

    private TextWatcher mEmailFieldWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!isValidEmail(mEmailField.getText().toString())){
                mEmailField.setError("Enter a valid email address ,bro");
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
    private TextWatcher mPass1FieldWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(mPass1Field.getText().length() < 8 ){
                mPass1Field.setError("Too short");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private TextWatcher mPass2FieldWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!mPass2Field.getText().toString().equals(mPass1Field.getText().toString())){
                mPass2Field.setError("Passwords don't match");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void OnRegister(View view){
        if (mEmailField.getError() == null && mPass2Field.getError() == null && mPass1Field.getError() == null
                && !mEmailField.getText().toString().equals("") && !mPass1Field.getText().toString().equals("")){
            //Toast.makeText(SignUp.this, "Account Created.", Toast.LENGTH_SHORT).show();
            String email = mEmailField.getText().toString();
            String password = mPass1Field.getText().toString();
            String type = "register";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type, email, password);

        }
        else {
            Toast.makeText(SignUp.this, "FAIL", Toast.LENGTH_SHORT).show();
        }
    }
}
