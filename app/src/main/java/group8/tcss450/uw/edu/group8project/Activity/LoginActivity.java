package group8.tcss450.uw.edu.group8project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import group8.tcss450.uw.edu.group8project.GetWebServiceTaskDelegate;
import group8.tcss450.uw.edu.group8project.R;

/**
 * LoginActivity class display log in layout
 * where user input there email and password
 * System will check if the email and password is registered
 * and email is veried yet.
 * It failed -> staying on login layout
 *    passed -> go to DisplayActivty
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GetWebServiceTaskDelegate {

    private final AppCompatActivity activity = LoginActivity.this;

    private TextInputLayout layoutEmail;
    private TextInputLayout layoutPassword;
    private TextInputEditText edittextEmail;
    private TextInputEditText edittextPassword;

    private AppCompatButton logIN;
    private AppCompatTextView signUP;

    private InputValidation inputValidation;
    private FirebaseAuth mAuth;
    private String email;
    private String password;

    /*
     * Initialize activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        //initialize views
        layoutEmail = (TextInputLayout) findViewById(R.id.layoutEmail);
        layoutPassword = (TextInputLayout) findViewById(R.id.layoutPassword);

        edittextEmail = (TextInputEditText) findViewById(R.id.edittextEmail);
        edittextPassword = (TextInputEditText) findViewById(R.id.edittextPassword);

        logIN = (AppCompatButton) findViewById(R.id.logIN);
        signUP = (AppCompatTextView) findViewById(R.id.signUP);

        //initialize listeners
        logIN.setOnClickListener(this);
        signUP.setOnClickListener(this);

        //initialize objects
        inputValidation = new InputValidation(activity);
        mAuth = FirebaseAuth.getInstance();
    }

    /*
     * Launch activity if different button is pressed
     * If click signup -> launch RegisterActivity
     * If click log in -> check authentication and email verification
     */
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.logIN:
                if (!areInputsValid()) {
                    break;
                }
                email = edittextEmail.getText().toString().trim();
                password = edittextPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // [START_EXCLUDE]

                                if (!task.isSuccessful()) {
                                    handleFailure("");

                                } else {
                                    if (!mAuth.getCurrentUser().isEmailVerified()) {
                                        Toast.makeText(activity, "This email address has not been verified.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Intent accountsIntent = new Intent(activity, DisplayActivity.class);
                                        accountsIntent.putExtra("EMAIL", edittextEmail.getText().toString().trim());
                                        startActivity(accountsIntent);
                                    }
                                }
                            }
                        });
                break;

            case R.id.signUP:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    /*
     * This method checks if the validation of input
     */
    private boolean areInputsValid(){

        //check if email field is filled.
        if (!inputValidation.isTextEditFilled(edittextEmail, layoutEmail, getString(R.string.error_empty_email))) {
            edittextEmail.requestFocus();
            return false;
        }

        //check if email is in valid format (example@hehe.haha)
        if (!inputValidation.isEmailValid(edittextEmail, layoutEmail, getString(R.string.error_message_email))) {
            edittextEmail.requestFocus();
            return false;
        }

        //check if password field is filled.
        if (!inputValidation.isTextEditFilled(edittextPassword, layoutPassword, getString(R.string.error_empty_password))) {
            edittextPassword.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void handleSuccess() {


    }

    @Override
    public void handleFailure(String errorMessage) {
        Toast.makeText(activity, "Sign In failed, please try again.",
                Toast.LENGTH_SHORT).show();
        return;
    }
}