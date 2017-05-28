package adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import de.hdodenhof.circleimageview.CircleImageView;
import digitalbath.fansproject.R;
import helpers.main.AppController;
import helpers.main.AppHelper;
import helpers.other.GetMetaDataFromUrl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import models.FeedItem;

/**
 * Created by unexpected_err on 20/05/2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int FEED_ITEM_TYPE = 0;
    private final int NEW_MESSAGE_TYPE = 1;

    private boolean isPreviewCreated;

    private ArrayList<FeedItem> mDataSet;
    private Activity mActivity;
    private DatabaseReference mDatabase;

    public FeedAdapter(Activity activity, DatabaseReference database) {

        mDataSet = new ArrayList<>();

        mActivity = activity;
        mDatabase = database;

        this.mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                mDataSet.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    FeedItem feedItem = postSnapshot.getValue(FeedItem.class);
                    feedItem.setId(postSnapshot.getKey());
                    mDataSet.add(0, feedItem);
                }

                mDataSet.add(0, new FeedItem());

                notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;

        if (viewType == NEW_MESSAGE_TYPE) {

            View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_message_item, parent, false);

            vh = new NewMessageViewHolder(v);

        } else {

            View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_item, parent, false);

            vh = new FeedItemViewHolder(v);
        }

        return vh;
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {

            case NEW_MESSAGE_TYPE:
                NewMessageViewHolder newMessageViewHolder = (NewMessageViewHolder) holder;
                bindNewMessageItem(newMessageViewHolder);
                break;
            case FEED_ITEM_TYPE:
                FeedItemViewHolder googleViewHolder = (FeedItemViewHolder) holder;
                bindFeedItem(googleViewHolder, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override public int getItemViewType(int position) {
        if (position == 0)
            return NEW_MESSAGE_TYPE;
        else
            return FEED_ITEM_TYPE;
    }

    private void bindFeedItem(final FeedItemViewHolder holder, int position) {

        final FeedItem item = mDataSet.get(position);

        Glide.with(mActivity)
            .load(item.getAvatar())
            .dontAnimate()
            .placeholder(R.drawable.avatar)
            .error(R.drawable.avatar)
            .into(holder.avatar);

        holder.username.setText(item.getUsername());
        holder.time.setText(AppHelper.getTimeDifference(item.getDate()));

        holder.message.setText(item.getMessage());

        if (item.getImageUrl() != null && !TextUtils.isEmpty(item.getImageUrl())) {

            holder.image.setVisibility(View.VISIBLE);

            Glide.with(mActivity)
                .load(item.getImageUrl())
                .into(holder.image);
        } else {

            holder.image.setVisibility(View.GONE);
            holder.image.setBackground(null);
        }

        holder.numberOfLikes.setText(Integer.toString(item.getLikes().size()) + " thumbs up");
        holder.numberOfUnlikes.setText(Integer.toString(item.getUnlikes().size()) + " thumbs down");

        if (item.getLikes().get(AppController.getUser().getUid()) != null
            && item.getLikes().get(AppController.getUser().getUid()) == true) {

            holder.like.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
            holder.likeIcon.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorAccent));

        } else {

            holder.like.setTextColor(mActivity.getResources().getColor(R.color.main_color_dark));
            holder.likeIcon.setColorFilter(ContextCompat.getColor(mActivity, R.color.light_gray_with_tr));
        }

        if (item.isArticleItem()) {

            holder.articleCont.setVisibility(View.VISIBLE);
            holder.articleTitle.setText(item.getArticleTitle());

            if (item.getArticleImageUrl() != null) {

                Glide.with(mActivity)
                    .load(item.getArticleImageUrl())
                    .into(holder.articleImage);

            }

            if (item.getArticleUrl() != null) {

                String domain = AppHelper.getDomainName(item.getArticleUrl());

                holder.articlePublisher.setText(domain);

                Glide.with(mActivity)
                    .load("http://www." + domain + "/favicon.ico")
                    .asBitmap()
                    .placeholder(R.drawable.ic_rss_feed)
                    .into(holder.articlePublisherIcon);

            }

        } else {

            holder.articleCont.setVisibility(View.GONE);

        }

        holder.likeCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (item.getLikes().get(AppController.getUser().getUid()) == null) {

                    Map<String, Object> taskMap = new HashMap<>();
                    taskMap.put(AppController.getUser().getUid(), true);

                    mDatabase.child(item.getId())
                        .child("likes").updateChildren(taskMap);

                } else {

                    mDatabase.child(item.getId())
                        .child("likes").child(AppController.getUser().getUid()).removeValue();
                }
            }
        });

        if (item.getUnlikes().get(AppController.getUser().getUid()) != null
            && item.getUnlikes().get(AppController.getUser().getUid()) == true) {

            holder.unlike.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
            holder.unlikeIcon.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorAccent));

        } else {

            holder.unlike.setTextColor(mActivity.getResources().getColor(R.color.main_color_dark));
            holder.unlikeIcon.setColorFilter(ContextCompat.getColor(mActivity, R.color.light_gray_with_tr));
        }

        holder.unlikeCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (item.getUnlikes().get(AppController.getUser().getUid()) == null) {

                    Map<String, Object> taskMap = new HashMap<>();
                    taskMap.put(AppController.getUser().getUid(), true);

                    mDatabase.child(item.getId())
                        .child("unlikes").updateChildren(taskMap);

                } else {

                    mDatabase.child(item.getId())
                        .child("unlikes").child(AppController.getUser().getUid()).removeValue();
                }
            }
        });
    }

    private void bindNewMessageItem(final NewMessageViewHolder holder) {

        final FeedItem item = mDataSet.get(0);

        Glide.with(mActivity)
            .load(AppController.getUser().getPhotoUrl())
            .dontAnimate()
            .placeholder(R.drawable.avatar)
            .error(R.drawable.avatar)
            .into(holder.avatar);

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.title.setText(AppController.getUser().getDisplayName());

                holder.avatar.animate().scaleY(0.8f).start();
                holder.avatar.animate().scaleX(0.8f).start();

                holder.message.setTypeface(AppHelper.getRobotoLight(mActivity));

                holder.actions.setVisibility(View.VISIBLE);
                holder.message.setVisibility(View.VISIBLE);


            }
        });

        holder.post.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                FeedItem feedItemToPost = new FeedItem();
                feedItemToPost.setUsername(AppController.getUser().getDisplayName());
                feedItemToPost.setUserId(AppController.getUser().getUid());
                feedItemToPost.setMessage(holder.message.getText().toString());
                feedItemToPost.setDate(new Date().toString());
                feedItemToPost.setAvatar(AppController.getUser().getPhotoUrl().toString());

                feedItemToPost.setArticleTitle(item.getArticleTitle());
                feedItemToPost.setArticleImageUrl(item.getArticleImageUrl());
                feedItemToPost.setArticlePublisher(item.getArticlePublisher());
                feedItemToPost.setIsArticleItem(item.isArticleItem());
                feedItemToPost.setArticleUrl(item.getArticleUrl());

                if (!TextUtils.isEmpty(item.getImageUrl()))
                    feedItemToPost.setImageUrl(item.getImageUrl());

                mDatabase.push().setValue(feedItemToPost);

                holder.message.setText("");

                AppHelper.hide(holder.post, mActivity);

                AppHelper.hide(holder.message, mActivity);

                holder.title.setText("Write a new post :)");
                holder.title.startAnimation(AppHelper.getAnimationUp(mActivity));

                holder.avatar.animate().scaleY(1f).start();
                holder.avatar.animate().scaleX(1f).start();

                isPreviewCreated = false;
            }
        });

        holder.discard.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                holder.title.setText("Write a new post :)");
                holder.message.setText("");

                holder.avatar.animate().scaleY(1f).start();
                holder.avatar.animate().scaleX(1f).start();

                holder.message.setVisibility(View.GONE);
                holder.actions.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.GONE);
            }
        });

        holder.message.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override public void afterTextChanged(Editable s) {

                if (isPreviewCreated)
                    return;

                String url = AppHelper.findUrl(s.toString());

                if (!TextUtils.isEmpty(url)) {

                    holder.progressBar.setVisibility(View.VISIBLE);
                    loadPreviewImage(url, item, holder);

                }
            }
        });
    }

    private void loadPreviewImage(final String url, final FeedItem item,
        final NewMessageViewHolder holder) {

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {

                holder.image.setVisibility(View.VISIBLE);

                item.setImageUrl(url);

                TransitionDrawable td = new TransitionDrawable(new Drawable[] {
                    new ColorDrawable(Color.TRANSPARENT),
                    new BitmapDrawable(mActivity.getResources(), bitmap)
                });

                holder.image.setImageDrawable(td);

                td.startTransition(300);

                holder.progressBar.setVisibility(View.GONE);
                isPreviewCreated = true;
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);

                GetMetaDataFromUrl worker = new GetMetaDataFromUrl(mActivity,
                    holder, item, isPreviewCreated);
                worker.execute(url);

            }
        };

        Glide.with(mActivity)
            .load(url)
            .asBitmap()
            .into(target);
    }

    public static class FeedItemViewHolder extends RecyclerView.ViewHolder {

        public TextView message;
        public TextView username;
        public TextView time;
        public TextView numberOfLikes;
        public TextView numberOfUnlikes;
        public CircleImageView avatar;
        public ImageView image;
        public LinearLayout likeCont;
        public LinearLayout unlikeCont;
        public TextView like;
        public TextView unlike;
        public ImageView likeIcon;
        public ImageView unlikeIcon;
        public RelativeLayout articleCont;
        public TextView articleTitle;
        public TextView articlePublisher;
        public ImageView articlePublisherIcon;
        public ImageView articleImage;

        FeedItemViewHolder(View itemView) {
            super(itemView);

            message = (TextView) itemView.findViewById(R.id.message);
            username = (TextView) itemView.findViewById(R.id.username);
            time = (TextView) itemView.findViewById(R.id.time_published);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            image = (ImageView) itemView.findViewById(R.id.image);

            numberOfLikes = (TextView) itemView.findViewById(R.id.likes);
            numberOfUnlikes = (TextView) itemView.findViewById(R.id.unlikes);

            likeCont = (LinearLayout) itemView.findViewById(R.id.like_cont);
            unlikeCont = (LinearLayout) itemView.findViewById(R.id.unlike_cont);

            like = (TextView) itemView.findViewById(R.id.like);
            unlike = (TextView) itemView.findViewById(R.id.unlike);

            likeIcon = (ImageView) itemView.findViewById(R.id.like_icon);
            unlikeIcon = (ImageView) itemView.findViewById(R.id.unlike_icon);

            articleCont = (RelativeLayout) itemView.findViewById(R.id.article_cont);
            articleTitle = (TextView) itemView.findViewById(R.id.article_title);
            articlePublisher = (TextView) itemView.findViewById(R.id.article_publisher_name);
            articlePublisherIcon = (ImageView) itemView.findViewById(R.id.article_publisher_icon);
            articleImage = (ImageView) itemView.findViewById(R.id.article_image);
        }
    }

    public static class NewMessageViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public EditText message;
        public CircleImageView avatar;
        public ImageView image;
        public LinearLayout post;
        public LinearLayout discard;
        public LinearLayout actions;

        public RelativeLayout articlePreview;
        public TextView articleTitle;
        public TextView articlePublisher;
        public ImageView articlePublisherIcon;
        public ImageView articleImage;
        public ProgressBar progressBar;

        NewMessageViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            message = (EditText) itemView.findViewById(R.id.message);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            image = (ImageView) itemView.findViewById(R.id.image);
            post = (LinearLayout) itemView.findViewById(R.id.post_btn);
            discard = (LinearLayout) itemView.findViewById(R.id.discard_btn);
            actions = (LinearLayout) itemView.findViewById(R.id.actions_cont);

            articlePreview = (RelativeLayout) itemView.findViewById(R.id.preview_article);
            articleTitle = (TextView) itemView.findViewById(R.id.preview_title);
            articlePublisher = (TextView) itemView.findViewById(R.id.preview_publisher_name);
            articlePublisherIcon = (ImageView) itemView.findViewById(R.id.preview_publisher_icon);
            articleImage = (ImageView) itemView.findViewById(R.id.article_image);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);

        }
    }
}

