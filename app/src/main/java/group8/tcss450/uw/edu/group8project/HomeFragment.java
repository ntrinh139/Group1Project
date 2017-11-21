package group8.tcss450.uw.edu.group8project;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    TextView textViewName;
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_home, container, false);

        return v;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

        @Override
        public void onStart() {
            super.onStart();
            if (getArguments() != null) {
                textViewName = (TextView) getActivity().findViewById(R.id.welcome);
                String nameFromIntent = getArguments().getString("EMAIL");
                textViewName.setText("Welcome " + nameFromIntent);
            }
        }

}
