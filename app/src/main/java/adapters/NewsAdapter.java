package adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import digitalbath.fansproject.R;
import helpers.main.AppController;
import helpers.main.AppHelper;
import helpers.other.GetMetaDataFromUrl;
import helpers.other.MetaTagsLoad;
import helpers.other.NewsItemDataService;
import listeners.OnArticleClickListener;
import listeners.OnDislikeClickListener;
import listeners.OnLikeClickListener;
import listeners.OnPostCommentListener;
import models.news.ArticleItem;
import models.news.MetaTag;
import viewholders.ArticleViewHolder;

/**
 * Created by Spaja on 26-Apr-17.
 */

public class NewsAdapter extends RecyclerView.Adapter<ArticleViewHolder>
        implements MetaTagsLoad {

    private final NewsItemDataService newsItemDataService;
    private boolean shouldAnimateListItems = true;
    private Activity mActivity;
    private ArrayList<ArticleItem> mDataSet;
    private DatabaseReference mNewsRef;
    private CommentsAdapter mCommentsAdapter;
    private RelativeLayout mCommentsCont;

    public NewsAdapter(Activity activity, final ArrayList<ArticleItem> mDataSet,
                       RelativeLayout mCommentsCont) {

        this.mDataSet = mDataSet;
        this.mActivity = activity;
        this.mCommentsCont = mCommentsCont;

        mNewsRef = AppController.getFirebaseDatabase().child("news");

        this.newsItemDataService = new NewsItemDataService(mNewsRef);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shouldAnimateListItems = false;
            }
        }, 200);
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_item, parent, false);

        return new ArticleViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder holder, final int position) {

        final ArticleItem item = mDataSet.get(position);

        final String uid = AppController.getUser().getUid();

        bindArticleItem(holder, position, item, mActivity);

        mNewsRef.child(item.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArticleItem articleItem = dataSnapshot.getValue(ArticleItem.class);

                if (articleItem != null) {

                    item.setLikes(articleItem.getLikes());
                    item.setDislikes(articleItem.getDislikes());
                    item.setCommentsMap(articleItem.getCommentsMap());

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
                holder.numberOfDislikes.setText(Integer.toString(item.getDislikes().size()) + " thumbs down");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        holder.likeCont.setOnClickListener(new OnLikeClickListener(item, newsItemDataService));
        holder.dislikeCont.setOnClickListener(new OnDislikeClickListener(item, newsItemDataService));

        holder.commentsCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppBarLayout appBarLayout = (AppBarLayout) mActivity.findViewById(R.id.appbar);
                appBarLayout.setExpanded(false, true);

                getComments(item.getId());
            }
        });

        holder.copyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("url", item.getArticleUrl());
                clipboard.setPrimaryClip(clip);
                AppHelper.showToast(mActivity, "Url copied to clipboard");

            }
        });

        if (shouldAnimateListItems)
            AppHelper.animateItemAppearance(holder.itemView, position);
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

        ArticleItem articleItem = mDataSet.get(position);

        articleItem.setImageUrl(metatag.getImageUrl());

        loadImage(articleViewHolder.image, articleItem.getImageUrl(), true);

    }

    private void bindArticleItem(final ArticleViewHolder holder, final int position,
                                 final ArticleItem articleItem, final Activity mActivity) {

        holder.title.setText(articleItem.getTitle());

        String url = articleItem.getArticleUrl();

        if (articleItem.getImageUrl() == null) {

            holder.image.setVisibility(View.VISIBLE);

            GetMetaDataFromUrl worker = new GetMetaDataFromUrl(mActivity,
                    NewsAdapter.this, holder, position);

            worker.execute(url);

            /*GooseArticleDataParser worker1 = new GooseArticleDataParser(mActivity);

            worker1.execute(url);

            BoilerpipeArticleDataParser worker2 = new BoilerpipeArticleDataParser(mActivity);

            worker2.execute(url);*/

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

        holder.itemView.setOnClickListener(new OnArticleClickListener(mActivity, articleItem));
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

        final ImageView closeComments = (ImageView) mCommentsCont.findViewById(R.id.close_comments);
        closeComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               closeCommentsCont();
            }
        });

        mCommentsCont.findViewById(R.id.post_comment).setOnClickListener
                (new OnPostCommentListener(mItemCommentsDatabase, mCommentsCont));

    }

    public boolean closeCommentsCont() {

        if (mCommentsCont.getVisibility() == View.GONE)
            return false;

        mCommentsCont.setVisibility(View.GONE);
        mCommentsCont.startAnimation(AppHelper.getAnimationDown(mActivity));

        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        return true;
    }
}
