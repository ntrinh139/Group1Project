package group8.tcss450.uw.edu.group8project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GetWebServiceTaskDelegate{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }
    private void initViews(){
        layoutEmail = (TextInputLayout) findViewById(R.id.layoutEmail);
        layoutPassword = (TextInputLayout) findViewById(R.id.layoutPassword);

        edittextEmail = (TextInputEditText) findViewById(R.id.edittextEmail);
        edittextPassword = (TextInputEditText) findViewById(R.id.edittextPassword);

        logIN = (AppCompatButton) findViewById(R.id.logIN);
        signUP = (AppCompatTextView) findViewById(R.id.signUP);
    }

    private void initListeners(){
        logIN.setOnClickListener(this);
        signUP.setOnClickListener(this);
    }

    private void initObjects(){
        inputValidation = new InputValidation(activity);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.logIN:
                if (!areInputsValid()) {
                    break;
                }
                email = edittextEmail.getText().toString().trim();
                password = edittextPassword.getText().toString();
                GetWebServiceTask task = new GetWebServiceTask();
                task.execute("http://cssgate.insttech.washington.edu/~davidmk/login.php", email, password);
                task.delegate = this;
                break;

            case R.id.signUP:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    private boolean areInputsValid(){
        if (!inputValidation.isTextEditFilled(edittextEmail, layoutEmail, getString(R.string.error_empty_email))) {
            return false;
        }
        if (!inputValidation.isEmailValid(edittextEmail, layoutEmail, getString(R.string.error_message_email))) {
            return false;
        }
        if (!inputValidation.isTextEditFilled(edittextPassword, layoutPassword, getString(R.string.error_empty_password))) {
            return false;
        }
        return true;
    }


    private void emptyInputEditText(){
        edittextEmail.setText(null);
        edittextPassword.setText(null);
    }

    @Override
    public void handleSuccess() {
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
                                emptyInputEditText();
                                startActivity(accountsIntent);
                            }
                        }
                        // [END_EXCLUDE]
                    }
                });



    }

    @Override
    public void handleFailure(String errorMessage) {
        Toast.makeText(activity, "Sign In failed, please try again.",
                Toast.LENGTH_SHORT).show();
        return;
    }
}