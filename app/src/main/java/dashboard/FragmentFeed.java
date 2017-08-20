package dashboard;

import adapters.FeedAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.firebase.database.DatabaseReference;

import digitalbath.fansproject.R;
import helpers.main.AppController;

/**
 * Created by unexpected_err on 29/04/2017.
 */

public class FragmentFeed extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private RecyclerView mRecyclerView;
    private FeedAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private RelativeLayout mCommentsCont;

    public static FragmentFeed newInstance(int sectionNumber) {

        FragmentFeed fragment = new FragmentFeed();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override public View onCreateView(LayoutInflater inflater,
        ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        mCommentsCont = (RelativeLayout) rootView.findViewById(R.id.comments_cont);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.feed_recycler);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);

        mAdapter = new FeedAdapter(getActivity(), AppController.getFirebaseDatabase
            (getContext()).child("feed"), mCommentsCont, appBarLayout, mRecyclerView.getLayoutManager());

        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }


}