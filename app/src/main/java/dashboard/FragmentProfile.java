package dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import digitalbath.fansproject.R;

/**
 * Created by unexpected_err on 29/04/2017.
 */

public class FragmentProfile extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public FragmentProfile() {}

    public static FragmentProfile newInstance(int sectionNumber) {

        FragmentProfile fragment = new FragmentProfile();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater,
        ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        return rootView;
    }
}