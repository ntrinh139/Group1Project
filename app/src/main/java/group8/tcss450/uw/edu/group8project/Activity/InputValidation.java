package group8.tcss450.uw.edu.group8project.Activity;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import static android.util.Patterns.EMAIL_ADDRESS;

/**
 * This class check the validation of user's input
 */
public class InputValidation {

    private Context context;

    public InputValidation(Context context){

        this.context = context;
    }

    /*
     * This method check if edit text field is filled.
     * Display error message
     */
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

    /*
     * This method check if email is in valid format
     * (example@haha.hehe)
     * Display error message
     */
    public boolean isEmailValid(TextInputEditText userEditText, TextInputLayout layoutEditText, String message){
        String value = userEditText.getText().toString().trim();
        if (value.isEmpty() || !EMAIL_ADDRESS.matcher(value).matches()){
            layoutEditText.setError(message);
            hideKeyboardFrom(userEditText);
            return false;

        }else {
            layoutEditText.setErrorEnabled(false);
        }
        return true;
    }

    /*
     * This method check if password and confirm password are matched
     * Display error message
     */
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

    /*
     * This method check if password is in valid form
     * (contains at least 6 characters and inclues 1 upper, 1 lower
     * 1 speical character and 1 number)
     * Display error message
     */
    public boolean isPasswordValid(TextInputEditText userEditText, TextInputLayout layoutEditText, String message1) {
        String password = userEditText.getText().toString().trim();

        if (password.length() < 5) {
                layoutEditText.setErrorEnabled(false);
                hideKeyboardFrom(userEditText);
            }
        hideKeyboardFrom(userEditText);
        layoutEditText.setError(message1);

        return true;
    }

    /*
     * The method to hide keyboard after user done input their information
     */
    private void hideKeyboardFrom(View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
