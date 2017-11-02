package group8.tcss450.uw.edu.group8project;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

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
    private SQLiteHelper sqLiteHelper;
    private User user;

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
        sqLiteHelper = new SQLiteHelper(activity);
        user = new User();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.signUP:
                postDataToSQLite();
                break;
            case R.id.logIN:
                finish();
                break;
        }
    }

    private void postDataToSQLite(){

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
            return;
        }

        if (!inputValidation.isPasswordValid(edittextPassword, layoutPassword,
                getString(R.string.error_validPassword), getString(R.string.error_validPassword2))) {
            return;
        }

        if (!inputValidation.isPasswordMatched(edittextPassword, edittextConfirmPassword,
                layoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }

        if (!sqLiteHelper.checkUser(edittextEmail.getText().toString().trim())) {
            user.setEmail(edittextEmail.getText().toString().trim());
            user.setPassword(edittextPassword.getText().toString().trim());
            sqLiteHelper.addUser(user);
            Toast.makeText(getApplicationContext(), R.string.success_message, Toast.LENGTH_LONG).show();
            emptyInputEditText();

            Intent inent = new Intent(this, LoginActivity.class);
            startActivity(inent);

        } else {
            Toast.makeText(getApplicationContext(), R.string.error_email_exists, Toast.LENGTH_LONG).show();
            emptyInputEditText();
        }
    }

    private void emptyInputEditText(){
        edittextEmail.setText(null);
        edittextPassword.setText(null);
        edittextConfirmPassword.setText(null);
    }

}

