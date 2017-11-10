package group8.tcss450.uw.edu.group8project;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplaySingleRecipe extends Fragment {


    private JSONObject recipeDetails;
    public DisplaySingleRecipe() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                TextView tv = (TextView) getActivity().findViewById(R.id.displayRecipe);
                // tv.setText(getArguments().getString("recipeDetails"));
                //recipeDetails = new JSONObject(getArguments().getString("recipeDetails"));
            } catch (Exception e) {
                throw new RuntimeException(getContext()+"  Couldnt parse json");
            }
        }

    }
    @Override
    public void onStart () {
        super.onStart();
        try {
            TextView tv = (TextView) getActivity().findViewById(R.id.displayRecipe);
            tv.setText(getArguments().getString("recipeDetails"));
            recipeDetails = new JSONObject(getArguments().getString("recipeDetails"));
        } catch (Exception e) {

        }


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_single_recipe, container, false);
    }

}
