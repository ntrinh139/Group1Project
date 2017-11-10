package group8.tcss450.uw.edu.group8project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GetWebServiceTaskDelegate{

    private final AppCompatActivity activity = LoginActivity.this;

    private TextInputLayout layoutEmail;
    private TextInputLayout layoutPassword;
    private TextInputEditText edittextEmail;
    private TextInputEditText edittextPassword;

    private AppCompatButton logIN;
    private AppCompatTextView signUP;

    private InputValidation inputValidation;

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
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.logIN:

                verifyRegisteredUser();

                AsyncTask<String, Void, Integer> task = new GetWebServiceTask();
                ((GetWebServiceTask) task).delegate = this;
                task.execute("http://cssgate.insttech.washington.edu/~davidmk/login.php",
                        edittextEmail.getText().toString().trim(),
                        edittextPassword.getText().toString().trim());
                break;
            case R.id.signUP:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    private void verifyRegisteredUser(){
        if (!inputValidation.isTextEditFilled(edittextEmail, layoutEmail, getString(R.string.error_empty_email))) {
            emptyInputEditText();
            return;
        }
        if (!inputValidation.isEmailValid(edittextEmail, layoutEmail, getString(R.string.error_message_email))) {
            emptyInputEditText();
            return;
        }
        if (!inputValidation.isTextEditFilled(edittextPassword, layoutPassword, getString(R.string.error_empty_password))) {
            emptyInputEditText();
            return;
        }

        /*
        * Need to call database to check if the user's  is registered yet
        * I created meethod checkIfEmailVerified already.
        * -> if yes -> check if email they input email and password correctly
        *           -> if yes -> search fragment
        *           -> if no -> stay on log in page.
        * -> if no -> display messgae the email is not registered yet.
        */

    }

//    private void checkIfEmailVerified() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (user.isEmailVerified()) {
//            // user is verified, so you can finish this activity or send user to activity which you want.
//
//            Toast.makeText(activity, "Now you can log in", Toast.LENGTH_SHORT).show();
//        }
//        else  {
//            Toast.makeText(activity, "Please verify your email before logging in", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void emptyInputEditText(){
        edittextEmail.setText(null);
        edittextPassword.setText(null);
    }

    @Override
    public void handleSuccess() {
        Intent accountsIntent = new Intent(activity, DisplayActivity.class);
        accountsIntent.putExtra("EMAIL", edittextEmail.getText().toString().trim());
        emptyInputEditText();
        startActivity(accountsIntent);
    }

    @Override
    public void handleFailure(String errorMessage) {
    return;
    }
}