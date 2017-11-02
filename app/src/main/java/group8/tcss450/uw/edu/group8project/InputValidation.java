package group8.tcss450.uw.edu.group8project;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidation {

    private Context context;

    public InputValidation(Context context){

        this.context = context;
    }

    public boolean isTextEditFilled(TextInputEditText userEditText, TextInputLayout layoutEditText, String message){
        String value = userEditText.getText().toString().trim();
        if (value.isEmpty()){
            layoutEditText.setError(message);
            hideKeyboardFrom(userEditText);
            return false;
        } else {
            layoutEditText.setErrorEnabled(false);
        }
        return true;
    }

    public boolean isEmailValid(TextInputEditText userEditText, TextInputLayout layoutEditText, String message){
        String value = userEditText.getText().toString().trim();
        if (value.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            layoutEditText.setError(message);
            hideKeyboardFrom(userEditText);
            return false;

        }else {
            layoutEditText.setErrorEnabled(false);
        }
        return true;
    }

    public boolean isPasswordMatched(TextInputEditText userEditText1, TextInputEditText userEditText2, TextInputLayout layoutEditText, String message ){
        String value1 = userEditText1.getText().toString().trim();
        String value2 = userEditText2.getText().toString().trim();
        if (!value1.contentEquals(value2)){
            layoutEditText.setError(message);
            hideKeyboardFrom(userEditText2);
            return false;
        }else {
            layoutEditText.setErrorEnabled(false);
        }
        return true;
    }

    public boolean isPasswordValid(TextInputEditText userEditText, TextInputLayout layoutEditText, String message1, String message2) {
        String password = userEditText.getText().toString().trim();

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        boolean value = matcher.matches();

        if (password.length() > 5) {
            if (value == false) {
                layoutEditText.setError(message1);
                hideKeyboardFrom(userEditText);
                return false;
            } else {
                layoutEditText.setErrorEnabled(false);
            }
            return true;
        } else {
            layoutEditText.setError(message2);
        }
        return true;
    }

    private void hideKeyboardFrom(View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
