package adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import digitalbath.fansproject.R;
import helpers.main.AppController;
import helpers.main.AppHelper;
import helpers.other.GetMetaDataFromUrl;
import helpers.other.MetaTagsLoad;
import listeners.OnArticleClickListener;
import listeners.OnDislikeClickListener;
import listeners.OnLikeClickListener;
import listeners.OnPostCommentListener;
import models.news.ArticleItem;
import models.news.FeedItem;
import models.news.MetaTag;
import models.news.Post;
import viewholders.FeedItemViewHolder;
import viewholders.NewMessageViewHolder;

/**
 * Created by unexpected_err on 20/05/2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements MetaTagsLoad {

    private final int FEED_ITEM_TYPE = 0;
    private final int NEW_MESSAGE_TYPE = 1;

    private boolean isPreviewCreated;
    private boolean shouldAnimateListItems = true;

    private ArrayList<FeedItem> mDataSet;
    private Activity mActivity;
    private DatabaseReference mFeedDatabase;
    private RelativeLayout mCommentsCont;
    private CommentsAdapter mCommentsAdapter;
    private AppBarLayout appBarLayout;
    private LinearLayoutManager mLayoutManager;

    public FeedAdapter(Activity activity, DatabaseReference database,
                       RelativeLayout commentsCont, AppBarLayout appBarLayout, RecyclerView.LayoutManager layoutManager) {

        mDataSet = new ArrayList<>();

        mActivity = activity;
        mFeedDatabase = database;
        mCommentsCont = commentsCont;
        this.appBarLayout = appBarLayout;
        this.mLayoutManager = (LinearLayoutManager) layoutManager;

        mFeedDatabase.limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDataSet.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    FeedItem feedItem = postSnapshot.getValue(FeedItem.class);
                    feedItem.setId(postSnapshot.getKey());
                    mDataSet.add(0, feedItem);
                }

                mDataSet.add(0, new FeedItem());
                notifyDataSetChanged();

                if (mActivity.findViewById(R.id.progressBarFeed) != null)
                    mActivity.findViewById(R.id.progressBarFeed).setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        shouldAnimateListItems = false;
                    }
                }, 200);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //        this.mFeedDatabase.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                mDataSet.clear();
//
//                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                    FeedItem feedItem = postSnapshot.getValue(FeedItem.class);
//                    feedItem.setId(postSnapshot.getKey());
//                    mDataSet.add(0, feedItem);
//                }
//
//                mDataSet.add(0, new FeedItem());
//
//                notifyDataSetChanged();
//
//                if (mActivity.findViewById(R.id.progressBarFeed) != null)
//                    mActivity.findViewById(R.id.progressBarFeed).setVisibility(View.GONE);
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override public void run() {
//                        shouldAnimateListItems = false;
//                    }
//                }, 200);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError firebaseError) {
//
//            }
//        });
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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

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

//        if (shouldAnimateListItems)
//            AppHelper.animateItemAppearance(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return NEW_MESSAGE_TYPE;
        else
            return FEED_ITEM_TYPE;
    }

    @Override
    public void onMetaTagsLoaded(RecyclerView.ViewHolder holder, int position, MetaTag metaTag) {

        final NewMessageViewHolder feedHolder = ((NewMessageViewHolder) holder);
        feedHolder.progressBar.setVisibility(View.GONE);

        if (metaTag == null)
            return;

        feedHolder.articlePreview.setVisibility(View.VISIBLE);
        feedHolder.articleTitle.setText(metaTag.getTitle());

        if (metaTag.getImageUrl() != null) {

            Glide.with(mActivity)
                    .load(metaTag.getImageUrl())
                    .into(feedHolder.articleImage);

        }

        if (metaTag.getArticleUrl() != null) {

            String domain = AppHelper.getDomainName(metaTag.getArticleUrl());

            feedHolder.articlePublisher.setText(domain);

            SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {

                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {

                    feedHolder.articlePublisherIcon.setImageBitmap(bitmap);

                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);

                    feedHolder.articlePublisherIcon
                            .setImageResource(R.drawable.publisher_icon);
                }
            };

            Glide.with(mActivity)
                    .load("http://www." + domain + "/favicon.ico")
                    .asBitmap()
                    .into(target);

        }

        ArticleItem articleItem = new ArticleItem();
        articleItem.setTitle(metaTag.getTitle());
        articleItem.setImageUrl(metaTag.getImageUrl());
        articleItem.setArticleUrl(metaTag.getArticleUrl());

        mDataSet.get(position).setArticle(articleItem);

        isPreviewCreated = true;
    }

    //region feed item
    private void bindFeedItem(final FeedItemViewHolder holder, final int position) {

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

        if (item.getArticle() != null) {

            bindArticleCont(holder, item);

        } else {

            holder.articleCont.setVisibility(View.GONE);

        }

        holder.numberOfLikes.setText(Integer.toString(item.getLikes().size()) + " thumbs up");
        holder.numberOfComments.setText(Integer.toString(item.getCommentsCount()) + " comments");
        holder.numberOfDislikes.setText(Integer.toString(item.getDislikes().size()) + " thumbs down");

        if (item.getLikes().get(AppController.getUser().getUid()) != null
                && item.getLikes().get(AppController.getUser().getUid())) {

            holder.setLikeButtonOn(mActivity);

        } else {
            holder.setLikeButtonOff(mActivity);
        }

        if (item.getDislikes().get(AppController.getUser().getUid()) != null
                && item.getDislikes().get(AppController.getUser().getUid())) {

            holder.setDislikeButtonOn(mActivity);

        } else {
            holder.setDislikeButtonOff(mActivity);
        }

        holder.likeCont.setOnClickListener(new OnLikeClickListener(item, mFeedDatabase));

        holder.dislikeCont.setOnClickListener(new OnDislikeClickListener(item, mFeedDatabase));

        holder.commentCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppBarLayout appBarLayout = (AppBarLayout) mActivity.findViewById(R.id.appbar);
                appBarLayout.setExpanded(false, true);

                getComments(item.getId());
            }
        });
    }

    private void bindArticleCont(final FeedItemViewHolder holder, FeedItem item) {

        holder.articleCont.setVisibility(View.VISIBLE);

        holder.itemView.setOnClickListener(new OnArticleClickListener(mActivity, item.getArticle()));

        holder.articleTitle.setText(item.getArticle().getTitle());

        if (item.getArticle().getImageUrl() != null) {

            Glide.with(mActivity)
                    .load(item.getArticle().getImageUrl())
                    .into(holder.articleImage);

        }

        if (item.getArticle().getArticleUrl() != null) {

            String domain = AppHelper.getDomainName(item.getArticle().getArticleUrl());

            holder.articlePublisher.setText(domain);

            SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {

                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {

                    holder.articlePublisherIcon.setImageBitmap(bitmap);

                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);

                    holder.articlePublisherIcon.setImageResource(R.drawable.publisher_icon);
                }
            };

            Glide.with(mActivity)
                    .load("http://www." + domain + "/favicon.ico")
                    .asBitmap()
                    .into(target);

        }
    }
    //endregion

    //region new message
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

                expandNewMessageItem(holder);

            }
        });

        holder.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postNewFeedItem(holder, item);

            }
        });

        holder.discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                discardNewMessage(holder);
                notifyItemChanged(0);
                mLayoutManager.scrollToPositionWithOffset(0, 0);

            }
        });

        holder.message.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

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

    private void postNewFeedItem(NewMessageViewHolder holder, FeedItem item) {

        FeedItem feedItemToPost = new FeedItem();
        feedItemToPost.setUsername(AppController.getUser().getDisplayName());
        feedItemToPost.setUserId(AppController.getUser().getUid());
        feedItemToPost.setMessage(holder.message.getText().toString());
        feedItemToPost.setDate(new Date().toString());
        feedItemToPost.setAvatar(AppController.getUser().getPhotoUrl().toString());

        feedItemToPost.setArticle(item.getArticle());

                /*feedItemToPost.setArticleImageUrl(item.getArticleImageUrl());
                feedItemToPost.setArticlePublisher(item.getArticlePublisher());
                feedItemToPost.setIsArticleItem(item.isArticleItem());
                feedItemToPost.setArticleUrl(item.getArticleUrl());*/

        if (!TextUtils.isEmpty(item.getImageUrl()))
            feedItemToPost.setImageUrl(item.getImageUrl());

        String postId = mFeedDatabase.push().getKey();

        Post post = new Post();
        post.setUserName(AppController.getUser().getDisplayName());
        post.setPostId(postId);

        DatabaseReference mPostsRef = AppController
                .getFirebaseDatabase().child("posts");

        mPostsRef.child(AppController.getUser().getUid()).child(postId).setValue(post);

        mFeedDatabase.child(postId).setValue(feedItemToPost);

        holder.message.setText("");

        AppHelper.hide(holder.post, mActivity);

        AppHelper.hide(holder.message, mActivity);

        holder.title.setText("Write a new post :)");
        holder.title.startAnimation(AppHelper.getAnimationUp(mActivity));

        holder.avatar.animate().scaleY(1f).start();
        holder.avatar.animate().scaleX(1f).start();

        isPreviewCreated = false;
    }

    private void expandNewMessageItem(NewMessageViewHolder holder) {

        holder.title.setText(AppController.getUser().getDisplayName());

        holder.avatar.animate().scaleY(0.8f).start();
        holder.avatar.animate().scaleX(0.8f).start();

        holder.message.setTypeface(AppHelper.getRobotoLight(mActivity));

        holder.post.setVisibility(View.VISIBLE);

        holder.actions.setVisibility(View.VISIBLE);
        holder.message.setVisibility(View.VISIBLE);

    }

    private void discardNewMessage(NewMessageViewHolder holder) {

        holder.title.setText("Write a new post :)");
        holder.message.setText("");

        holder.avatar.animate().scaleY(1f).start();
        holder.avatar.animate().scaleX(1f).start();

        holder.message.setVisibility(View.GONE);
        holder.actions.setVisibility(View.GONE);
        holder.image.setVisibility(View.GONE);
        holder.articlePreview.setVisibility(View.GONE);
        holder.progressBar.setVisibility(View.GONE);
        isPreviewCreated = false;
        appBarLayout.setExpanded(true, true);
    }

    private void loadPreviewImage(final String url, final FeedItem item, final NewMessageViewHolder holder) {

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {

                holder.image.setVisibility(View.VISIBLE);

                item.setImageUrl(url);

                TransitionDrawable td = new TransitionDrawable(new Drawable[]{
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

                GetMetaDataFromUrl worker = new GetMetaDataFromUrl(mActivity, FeedAdapter.this, holder, 0);

                worker.execute(url);

            }
        };

        Glide.with(mActivity)
                .load(url)
                .asBitmap()
                .into(target);
    }
    //endregion

    public void getComments(String itemId) {

        DatabaseReference mItemCommentsDatabase = mFeedDatabase.child(itemId).child("comments");

        final LinearLayout mEmptyListCont = (LinearLayout)
                mCommentsCont.findViewById(R.id.empty_list_favorites);
        mEmptyListCont.setVisibility(View.GONE);

        final RecyclerView mCommentsRecycler = (RecyclerView)
                mCommentsCont.findViewById(R.id.comments_recycler);
        mCommentsRecycler.setVisibility(View.GONE);

        mCommentsCont.setVisibility(View.VISIBLE);

        final CardView commentsContInner = (CardView)
                mCommentsCont.findViewById(R.id.comments_cont_inner);
        commentsContInner.startAnimation(AppHelper.getAnimationUp(mActivity));

        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(mActivity));

        mCommentsAdapter = new CommentsAdapter(mActivity, mCommentsRecycler, mEmptyListCont,
                mItemCommentsDatabase, AppController.getUser().getUid());

        mCommentsRecycler.setAdapter(mCommentsAdapter);

        ImageView closeComments = (ImageView) mCommentsCont.findViewById(R.id.close_comments);
        closeComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCommentsCont.setVisibility(View.GONE);
            }
        });

        mCommentsCont.findViewById(R.id.post_comment).setOnClickListener
                (new OnPostCommentListener(mItemCommentsDatabase, mCommentsCont));

    }

    public void addFeedItems(ArrayList<FeedItem> feedItems) {

        if (feedItems.size() > mDataSet.size() - 1) {
            Toast.makeText(mActivity, "Loaded More", Toast.LENGTH_SHORT).show();
            mDataSet.clear();
            mDataSet.addAll(feedItems);
            mDataSet.add(0, new FeedItem());
            notifyDataSetChanged();
        }

        if (mActivity.findViewById(R.id.progressBarFeed) != null)
            mActivity.findViewById(R.id.progressBarFeed).setVisibility(View.GONE);
    }
}


