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
    private RecyclerView mRecyclerView;
    private FeedAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private RelativeLayout mCommentsCont;
    private boolean loading;

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

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.feed_recycler);
        mCommentsCont = (RelativeLayout) rootView.findViewById(R.id.comments_cont);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        mAdapter = new FeedAdapter(getActivity(), mFeedRef, mCommentsCont, appBarLayout, mRecyclerView.getLayoutManager());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {

                    final int visibleItemCount = mLayoutManager.getChildCount();
                    final int totalItemCount = mLayoutManager.getItemCount();
                    final int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (!loading) {
                        loadMorePosts(visibleItemCount, totalItemCount, pastVisibleItems);
                    }
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    private void loadMorePosts(int visibleItemCount, int totalItemCount, int pastVisibleItems) {
        if ((visibleItemCount + pastVisibleItems) >= (totalItemCount - 3)) {
            loading = true;
            mFeedRef.limitToLast(mAdapter.getItemCount() + 10).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<FeedItem> feedItems = new ArrayList<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        FeedItem item = data.getValue(FeedItem.class);
                        item.setId(data.getKey());
                        feedItems.add(0, item);
                    }
                    mAdapter.addFeedItems(feedItems);
                    loading = false;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "Failed to load more posts", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}