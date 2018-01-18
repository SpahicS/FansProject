package dashboard;

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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import adapters.FeedAdapter;
import digitalbath.fansproject.R;
import helpers.main.AppController;
import models.news.FeedItem;

/**
 * Created by unexpected_err on 29/04/2017.
 */

public class FragmentFeed extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private DatabaseReference mFeedRef;
    FeedAdapter mAdapter;

    public static FragmentFeed newInstance(int sectionNumber) {

        FragmentFeed fragment = new FragmentFeed();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFeedRef = AppController.getFirebaseDatabase().child("feed");

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.feed_recycler);
        RelativeLayout mCommentsCont = (RelativeLayout) rootView.findViewById(R.id.comments_cont);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        AVLoadingIndicatorView bottomProgressBar = (AVLoadingIndicatorView) rootView.findViewById(R.id.bottom_progress_bar);
        mAdapter = new FeedAdapter(getActivity(), mFeedRef, mCommentsCont, appBarLayout, mRecyclerView, bottomProgressBar);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    public boolean closeCommentsCont() {
        return mAdapter.closeCommentsCont();
    }
}