package adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
import helpers.other.MetaTagsLoad;
import listeners.OnPostCommentListener;
import models.Comment;
import models.MetaTag;
import models.NewsItem;
import persistance.NewsItemDataService;

/**
 * Created by Spaja on 26-Apr-17.
 */

public class NewsAdapter extends RecyclerView.Adapter<ArticleViewHolder>
        implements MetaTagsLoad {

    private final NewsItemDataService newsItemDataService;
    private Activity mActivity;
    private ArrayList<NewsItem> mDataSet;
    private DatabaseReference mNewsRef;
    private CommentsAdapter mCommentsAdapter;
    private RelativeLayout mCommentsCont;

    public NewsAdapter(Activity activity, final ArrayList<NewsItem> mDataSet, RelativeLayout mCommentsCont) {

        this.mDataSet = mDataSet;
        this.mActivity = activity;
        this.mCommentsCont = mCommentsCont;
        this.newsItemDataService = new NewsItemDataService();
        mNewsRef = AppController.getFirebaseDatabase().child("news");
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_item, parent, false);
        return new ArticleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder holder, final int position) {

        final NewsItem item = mDataSet.get(position);
        final String uid = AppController.getUser().getUid();

        holder.render(NewsAdapter.this, position, item, mActivity, newsItemDataService);

        mNewsRef.child(item.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                NewsItem newsItem = dataSnapshot.getValue(NewsItem.class);


                if (newsItem != null) {

                    item.setLikes(newsItem.getLikes());
                    item.setDislikes(newsItem.getDislikes());
                    item.setComments(newsItem.getComments());

                    if (mCommentsCont.getVisibility() == View.VISIBLE) {

                        mCommentsAdapter.setDataSet(item.getComments());
                        mCommentsAdapter.notifyDataSetChanged();

                    }

                    mActivity.findViewById(R.id.progressBar).setVisibility(View.GONE);

                    if (item.getLikes().containsKey(uid)) {
                        holder.setLikeButtonOn(mActivity);
                    } else {
                        holder.setLikeButtonOff(mActivity);
                    }

                    if (item.getDislikes().containsKey(uid)) {
                        holder.setDislikeButtonOn(mActivity);
                    } else {
                        holder.setDislikeButtonOff(mActivity);
                    }

                } else {

                    item.getLikes().clear();
                    item.getDislikes().clear();

                    holder.setLikeButtonOff(mActivity);
                    holder.setDislikeButtonOff(mActivity);
                }

                holder.numberOfLikes.setText(item.getLikes().size() + " thumbs up");
                holder.numberOfUnlikes.setText(item.getDislikes().size() + " thumbs down");
                holder.numberOfComments.setText(item.getComments().size() + " comments");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.likeCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getLikes().containsKey(uid)) {
                    newsItemDataService.removeLike(item.getId(), uid);
                } else {
                    newsItemDataService.saveLike(item.getId(), uid);
                }
            }
        });

        holder.unlikeCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getDislikes().containsKey(uid)) {
                    newsItemDataService.removeDislike(item.getId(), uid);
                } else {
                    newsItemDataService.saveDislike(item.getId(), uid);
                }
            }
        });

        holder.commentsCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppBarLayout appBarLayout = (AppBarLayout) mActivity.findViewById(R.id.appbar);
                appBarLayout.setExpanded(false, true);

                getComments(item.getComments(), item.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onMetaTagsLoaded(RecyclerView.ViewHolder holder, int position, MetaTag metatag) {

        final ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;

        NewsItem articleItem = mDataSet.get(position);

        articleItem.setImageUrl(metatag.getImageUrl());

        loadImage(articleViewHolder.image, articleItem.getImageUrl(), true);

    }

    void loadImage(final ImageView image, String imageUrl, final boolean animate) {

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {

                if (animate) {

                    TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                            new ColorDrawable(Color.TRANSPARENT),
                            new BitmapDrawable(mActivity.getResources(), bitmap)
                    });

                    image.setImageDrawable(td);

                    td.startTransition(300);

                } else {

                    image.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);

                image.setVisibility(View.GONE);

            }
        };

        Glide.with(mActivity)
                .load(imageUrl)
                .asBitmap()
                .into(target);

    }

    public void getComments(ArrayList<Comment> comments, String itemId) {

        DatabaseReference mItemCommentsDatabase = mNewsRef.child(itemId);

        LinearLayout mEmptyListCont = (LinearLayout)
                mCommentsCont.findViewById(R.id.empty_list_favorites);
        mEmptyListCont.setVisibility(View.GONE);

        final RecyclerView mCommentsRecycler = (RecyclerView)
                mCommentsCont.findViewById(R.id.comments_recycler);
        mCommentsRecycler.setVisibility(View.GONE);

        mCommentsCont.setVisibility(View.VISIBLE);

        if (comments.size() > 0) {
            mCommentsRecycler.setVisibility(View.VISIBLE);
            mEmptyListCont.setVisibility(View.GONE);
        } else {
            mEmptyListCont.setVisibility(View.VISIBLE);
            mCommentsRecycler.setVisibility(View.GONE);
        }

        final CardView commentsContInner = (CardView)
                mCommentsCont.findViewById(R.id.comments_cont_inner);
        commentsContInner.startAnimation(AppHelper.getAnimationUp(mActivity));

        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(mActivity));

        mCommentsAdapter = new CommentsAdapter(mActivity, comments,
                mItemCommentsDatabase, AppController.getUser().getUid());

        mCommentsRecycler.setAdapter(mCommentsAdapter);

        ImageView closeComments = (ImageView) mCommentsCont.findViewById(R.id.close_comments);
        closeComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCommentsCont.setVisibility(View.GONE);
                //commentsContInner.startAnimation(animationDown);
            }
        });

        mCommentsCont.findViewById(R.id.post_comment).setOnClickListener
                (new OnPostCommentListener(mItemCommentsDatabase, mCommentsCont));

    }
}
