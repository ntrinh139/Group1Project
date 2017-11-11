package group8.tcss450.uw.edu.group8project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, GetWebServiceTaskDelegate {

    private final AppCompatActivity activity = RegisterActivity.this;

    private ConstraintLayout signupView;

    private TextInputLayout layoutEmail;
    private TextInputLayout layoutPassword;
    private TextInputLayout layoutConfirmPassword;

    private TextInputEditText edittextEmail;
    private TextInputEditText edittextPassword;
    private TextInputEditText edittextConfirmPassword;

    private AppCompatButton signUP;
    private AppCompatTextView logIN;

    private InputValidation inputValidation;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    private void initViews(){
        signupView = (ConstraintLayout) findViewById(R.id.signupView);

        layoutEmail = (TextInputLayout) findViewById(R.id.layoutEmail);
        layoutPassword = (TextInputLayout) findViewById(R.id.layoutPassword);
        layoutConfirmPassword = (TextInputLayout) findViewById(R.id.layoutConfirmPassword);

        edittextEmail = (TextInputEditText) findViewById(R.id.edittextEmail);
        edittextPassword = (TextInputEditText) findViewById(R.id.edittextPassword);
        edittextConfirmPassword = (TextInputEditText) findViewById(R.id.edittextConfirmPassword);

        signUP = (AppCompatButton) findViewById(R.id.signUP);
        logIN = (AppCompatTextView) findViewById(R.id.logIN);
    }

    private void initListeners(){
        signUP.setOnClickListener(this);
        logIN.setOnClickListener(this);
    }

    private void initObjects(){
        inputValidation = new InputValidation(activity);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.signUP:
                checkValidation();
                break;
            case R.id.logIN:
                finish();
                break;
        }
    }

    private void checkValidation(){

        if (!inputValidation.isTextEditFilled(edittextEmail, layoutEmail, getString(R.string.error_empty_email))) {
            emptyInputEditText();
            return;
        }

        if (!inputValidation.isTextEditFilled(edittextPassword, layoutPassword, getString(R.string.error_empty_password))) {
            emptyInputEditText();
            return;
        }

        if (!inputValidation.isTextEditFilled(edittextConfirmPassword, layoutConfirmPassword, getString(R.string.error_empty_confirmPassword))) {
            emptyInputEditText();
            return;
        }


        if (!inputValidation.isEmailValid(edittextEmail, layoutEmail, getString(R.string.error_message_email))) {
            emptyInputEditText();
            return;
        }

        if (!inputValidation.isPasswordValid(edittextPassword, layoutPassword,
                getString(R.string.error_validPassword1), getString(R.string.error_validPassword2))) {
            emptyInputEditText();
            return;
        }

        if (!inputValidation.isPasswordMatched(edittextPassword, edittextConfirmPassword,
                layoutConfirmPassword, getString(R.string.error_password_match))) {
            emptyInputEditText();
            return;
        }

        final String email = edittextEmail.getText().toString().trim();
        final String password = edittextPassword.getText().toString();
        final GetWebServiceTask registerTask = new GetWebServiceTask();
        registerTask.delegate = this;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            handleFailure("");
                        } else {
                            registerTask.execute("http://cssgate.insttech.washington.edu/~davidmk/register.php", email, password);
                        }

                        // ...
                    }
                });

    }

    private void emailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]

                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this,
                                    "Verification email sent to " + user.getEmail()
                                    + "\nPlease verify your email before logging in",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Log.e("TAG", "sendEmailVerification", task.getException());
                            Toast.makeText(RegisterActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
    }

    private void emptyInputEditText(){
        edittextEmail.setText(null);
        edittextPassword.setText(null);
        edittextConfirmPassword.setText(null);
    }

    @Override
    public void handleSuccess() {
        Toast.makeText(activity, "Authetication success. \nNow verify your email",
                Toast.LENGTH_SHORT).show();
        emailVerification();
    }

    @Override
    public void handleFailure(String errorMessage) {
        Toast.makeText(activity, "Authetication failed",
                Toast.LENGTH_SHORT).show();
    }
}

