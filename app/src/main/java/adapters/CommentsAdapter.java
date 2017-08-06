package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import de.hdodenhof.circleimageview.CircleImageView;
import digitalbath.fansproject.R;
import helpers.main.AppHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import models.news.Comment;

/**
 * Created by unexpected_err on 19/02/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private ArrayList<Comment> mComments = new ArrayList<>();
    private Context mContext;
    private String mUserId;
    private RecyclerView mCommentsRecycler;
    private LinearLayout mEmptyListCont;
    private DatabaseReference mDataBaseRef;

    public CommentsAdapter(Context context, RecyclerView commentsRecycler,
        LinearLayout emptyListCont, DatabaseReference databaseReference, String userId) {

        this.mContext = context;
        this.mUserId = userId;
        this.mCommentsRecycler = commentsRecycler;
        this.mEmptyListCont = emptyListCont;
        this.mDataBaseRef = databaseReference;

        this.mDataBaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                mComments.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Comment commentItem = postSnapshot.getValue(Comment.class);
                    commentItem.setId(postSnapshot.getKey());
                    mComments.add(commentItem);
                }

                notifyDataSetChanged();

                if (mComments.size() > 0) {
                    mCommentsRecycler.setVisibility(View.VISIBLE);
                    mEmptyListCont.setVisibility(View.GONE);
                } else {
                    mEmptyListCont.setVisibility(View.VISIBLE);
                    mCommentsRecycler.setVisibility(View.GONE);
                }

                mCommentsRecycler.smoothScrollToPosition(mComments.size());
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Comment comment = mComments.get(position);

        holder.text.setText(comment.getText());
        holder.username.setText(comment.getUsername());
        holder.time.setText(AppHelper.getTimeDifference(comment.getDate()));

        Glide.with(mContext)
                .load(comment.getAvatar())
                .dontAnimate()
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(holder.avatar);

        holder.numberOfLikes.setText(Integer.toString(comment.getLikes().size()));

        if (mUserId.equals(""))
            holder.like.setVisibility(View.INVISIBLE);
        else
            holder.like.setVisibility(View.VISIBLE);

        if (comment.getLikes().get(mUserId) != null &&
                comment.getLikes().get(mUserId) == true) {

            holder.like.setText("Unlike");
            holder.like.setTextColor(mContext.getResources().getColor(R.color.colorAccent));

        } else {

            holder.like.setText("Like");
            holder.like.setTextColor(mContext.getResources().getColor(R.color.light_gray));

        }

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (comment.getLikes().get(mUserId) == null) {

                    Map<String, Object> taskMap = new HashMap<>();
                    taskMap.put(mUserId, true);

                    mDataBaseRef.child(comment.getId())
                            .child("likes").updateChildren(taskMap);

                } else {

                    mDataBaseRef.child(comment.getId())
                            .child("likes").child(mUserId).removeValue();
                }
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public void setDataSet(ArrayList<Comment> comments) {
        mComments = comments;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public TextView username;
        public TextView time;
        public TextView numberOfLikes;
        public CircleImageView avatar;
        public TextView like;
        //public LinearLayout share;

        ViewHolder(View itemView) {
            super(itemView);

            text = (TextView) itemView.findViewById(R.id.text);
            username = (TextView) itemView.findViewById(R.id.username);
            time = (TextView) itemView.findViewById(R.id.time_published);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            numberOfLikes = (TextView) itemView.findViewById(R.id.likes);
            like = (TextView) itemView.findViewById(R.id.like);
            //share = (LinearLayout) itemView.findViewById(R.id.share);
        }
    }


}