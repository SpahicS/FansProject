package dashboard;

import adapters.FeedAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.google.firebase.database.DatabaseReference;
import digitalbath.fansproject.R;
import helpers.main.AppController;
import java.util.Date;
import models.FeedItem;

/**
 * Created by unexpected_err on 29/04/2017.
 */

public class FragmentFeed extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private RecyclerView mRecyclerView;
    private FeedAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference mDatabase;

    public static FragmentFeed newInstance(int sectionNumber) {

        FragmentFeed fragment = new FragmentFeed();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = AppController.getFirebaseDatabase();

    }

    @Override public View onCreateView(LayoutInflater inflater,
        ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.feed_recycler);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new FeedAdapter(getActivity(), mDatabase
            .child("feed"), mRecyclerView);

        mRecyclerView.setAdapter(mAdapter);

        final EditText commentInput = (EditText) rootView.findViewById(R.id.message);

        rootView.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference quotes = mDatabase.child("feed");

                FeedItem feedItem = new FeedItem();

                feedItem.setUsername(AppController.getUser().getDisplayName());
                feedItem.setUserId(AppController.getUser().getUid());
                feedItem.setMessage(commentInput.getText().toString());
                feedItem.setDate(new Date().toString());
                feedItem.setAvatar(AppController.getUser().getPhotoUrl().toString());

                quotes.push().setValue(feedItem);

                commentInput.setText("");
            }
        });

        return rootView;
    }
}