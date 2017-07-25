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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import helpers.other.GetMetaDataFromUrl;
import java.util.ArrayList;

import digitalbath.fansproject.R;
import helpers.main.AppController;
import helpers.main.AppHelper;
import helpers.other.MetaTagsLoad;
import listeners.OnArticleClickListener;
import listeners.OnPostCommentListener;
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

        bindArticleItem(holder, position, item, mActivity);

        mNewsRef.child(item.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                NewsItem newsItem = dataSnapshot.getValue(NewsItem.class);

                if (newsItem != null) {

                    item.setLikes(newsItem.getLikes());
                    item.setDislikes(newsItem.getDislikes());
                    item.setCommentsMap(newsItem.getCommentsMap());

                    if (item.getLikes().containsKey(uid))
                        holder.setLikeButtonOn(mActivity);
                    else
                        holder.setLikeButtonOff(mActivity);


                    if (item.getDislikes().containsKey(uid))
                        holder.setDislikeButtonOn(mActivity);
                    else
                        holder.setDislikeButtonOff(mActivity);


                } else {

                    item.getLikes().clear();
                    item.getDislikes().clear();

                    holder.setLikeButtonOff(mActivity);
                    holder.setDislikeButtonOff(mActivity);
                }

                holder.numberOfLikes.setText(Integer.toString(item.getLikes().size()) + " thumbs up");
                holder.numberOfComments.setText(Integer.toString(item.getCommentsCount()) + " comments");
                holder.numberOfUnlikes.setText(Integer.toString(item.getDislikes().size()) + " thumbs down");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
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

                getComments(item.getId());
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

    private void bindArticleItem(final ArticleViewHolder holder, final int position,
        final NewsItem articleItem, final Activity mActivity) {

        holder.title.setText(articleItem.getTitle());

        String url = articleItem.getLink().split("url=")[1];

        if (articleItem.getImageUrl() == null) {

            holder.image.setVisibility(View.VISIBLE);
            GetMetaDataFromUrl worker = new GetMetaDataFromUrl
                (mActivity, NewsAdapter.this, holder, position, null, false);
            worker.execute(url);

        } else if (TextUtils.isEmpty(articleItem.getImageUrl())) {

            holder.image.setVisibility(View.GONE);

        } else {

            loadImage(holder.image, articleItem.getImageUrl(), false);

        }

        String domain = AppHelper.getDomainName(url);

        holder.publisherNameAndTime.setText(domain + " · " + AppHelper
            .getTimeDifference(articleItem.getPubDate()));

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {

                holder.publisherIcon.setImageBitmap(bitmap);

            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);

                holder.publisherIcon.setImageResource(R.drawable.publisher_icon);
            }
        };

        Glide.with(mActivity)
            .load("http://www." + domain + "/favicon.ico")
            .asBitmap()
            .into(target);

        holder.image.setOnClickListener(new OnArticleClickListener(mActivity, url));
    }

    private void loadImage(final ImageView image, String imageUrl, final boolean animate) {

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

    private void getComments(String itemId) {

        DatabaseReference mItemCommentsDatabase = mNewsRef.child(itemId).child("comments");

        LinearLayout mEmptyListCont = (LinearLayout)
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
}
