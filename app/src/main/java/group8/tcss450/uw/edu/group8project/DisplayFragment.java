package group8.tcss450.uw.edu.group8project;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DisplayFragment.OnFragmentInteractionListener2} interface
 */
public class DisplayFragment extends Fragment implements View.OnClickListener{

    JSONObject jsonObject;
    private OnFragmentInteractionListener2 mListener;
    private String arg;
    public DisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart () {
        super.onStart();

        LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.fragmentDisplay);

        try {
            for (int i = 0; i < jsonObject.getInt("number") && i < 14 ; i++) {
                TextView newTV = new TextView(getContext());
                newTV.setOnClickListener(this);
                newTV.setId(i);
                newTV.setText(jsonObject.getJSONArray("results").getJSONObject(i).getString("title")+"\n");

                linearLayout.addView(newTV);
            }
        } catch (Exception e) {

        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                jsonObject = new JSONObject(getArguments().getString("Json"));
            } catch (Exception e) {
                throw new RuntimeException(getContext()+"  Couldnt parse json");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_display, container, false);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener2) {
            mListener = (OnFragmentInteractionListener2) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener2");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        mListener.onFragmentInteraction2(jsonObject, v.getId());

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener2 {
        // TODO: Update argument type and name
        void onFragmentInteraction2(JSONObject jsonObject, int recipeIndex);
    }
}
