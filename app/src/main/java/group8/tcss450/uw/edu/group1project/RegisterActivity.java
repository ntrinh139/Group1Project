package group8.tcss450.uw.edu.group1project;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

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
            return;
        }

        if (!inputValidation.isTextEditFilled(edittextPassword, layoutPassword, getString(R.string.error_empty_password))) {
            return;
        }

        if (!inputValidation.isTextEditFilled(edittextConfirmPassword, layoutConfirmPassword, getString(R.string.error_empty_confirmPassword))) {
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

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(signupView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();


        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(signupView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText(){
        edittextEmail.setText(null);
        edittextPassword.setText(null);
        edittextConfirmPassword.setText(null);
    }

}

