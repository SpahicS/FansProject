package activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import de.hdodenhof.circleimageview.CircleImageView;
import digitalbath.fansproject.R;
import helpers.main.AppHelper;
import helpers.view.FansTextView;
import models.ArticleData;
import models.news.ArticleItem;
import networking.ArticleAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleActivity extends AppCompatActivity {

    private int currentOffset;
    private boolean fabHidden;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        ArticleItem article = (ArticleItem) getIntent().getSerializableExtra("article");

        //loadWebView(url);

        Call<ArticleData> call = ArticleAPI.service.getArticleData(article.getLink()
                .split("url=")[1], "sx2E9aIbmK9NFgaqnAwa1OHWXjTxg6ehBIYBM4xO");
        fetchArticleMainContent(article.getArticleUrl());

        bindHeaderView(article);

        initializeToolbar();

        initializeScrollView();
    }

    private void fetchArticleMainContent(final String articleUrl) {

        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {

                Button openInBrowser = (Button) findViewById(R.id.open_in_browser);
                openInBrowser.setVisibility(View.VISIBLE);

                openInBrowser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(articleUrl));

                        startActivity(browserIntent);
                    }
                });
            }
        };

        handler.postDelayed(runnable, 3000);

        Call<ArticleData> call = ArticleAPI.service.getArticleData(articleUrl,
                "sx2E9aIbmK9NFgaqnAwa1OHWXjTxg6ehBIYBM4xO");

        call.enqueue(new Callback<ArticleData>() {
            @Override
            public void onResponse(Call<ArticleData> call, Response<ArticleData> response) {

                if (response.body() != null) {

                    initializeSimplified(response.body().getContent());

                }

                handler.removeCallbacks(runnable);
            }

            @Override
            public void onFailure(Call<ArticleData> call, Throwable t) {
            }
        });


    }

    private void bindHeaderView(ArticleItem article) {

        ImageView image = (ImageView) findViewById(R.id.image);
        TextView title = (TextView) findViewById(R.id.title);
        TextView publisherNameAndTime = (TextView) findViewById(R.id.publisher_name_and_time);

        final CircleImageView publisherIcon = (CircleImageView) findViewById(R.id.publisher_icon);

        title.setText(article.getTitle());

        String url = article.getArticleUrl();

        if (TextUtils.isEmpty(article.getImageUrl())) {

            image.setVisibility(View.GONE);

        } else {

            loadImage(image, article.getImageUrl(), false);

        }

        String domain = AppHelper.getDomainName(url);

        publisherNameAndTime.setText(domain + " · " + AppHelper
                .getTimeDifference(article.getPubDate()));

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {

                publisherIcon.setImageBitmap(bitmap);

            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);

                publisherIcon.setImageResource(R.drawable.publisher_icon);
            }
        };

        Glide.with(ArticleActivity.this)
                .load("http://www." + domain + "/favicon.ico")
                .asBitmap()
                .into(target);
    }

    private void loadImage(final ImageView image, String imageUrl, final boolean animate) {

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {

                if (animate) {

                    TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                            new ColorDrawable(Color.TRANSPARENT),
                            new BitmapDrawable(ArticleActivity.this.getResources(), bitmap)
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

        Glide.with(ArticleActivity.this)
                .load(imageUrl)
                .asBitmap()
                .into(target);

    }

    private void initializeSimplified(String content) {

        LinearLayout articleContent = (LinearLayout) findViewById(R.id.article_content_cont);

        findViewById(R.id.article_content_loader).setVisibility(View.GONE);

        Document document = Jsoup.parse(content, "", Parser.htmlParser());

        final Elements elements = document.body().select("span, p, h1, h2, h3, img, tr");

        if (elements != null && elements.size() > 1 && elements.get(0).tag().getName().equals("tr") &&
                elements.get(0).getAllElements() != null && elements.get(0).getAllElements().size() > 0 &&
                elements.get(0).getAllElements().get(0) != null && elements.get(0).getAllElements().get(0).tag().getName().equals("tr"))
            elements.remove(0);

        for (int i = 0; i < elements.size(); i++) {

            if (elements.get(i).tag().getName().equals("p") || elements.get(i).tag().getName().equals("h1")
                    || elements.get(i).tag().getName().equals("h2") || elements.get(i).tag().getName().equals("h3")
                    || elements.get(i).tag().getName().equals("tr")) {

                Elements pElements = elements.get(i).getAllElements();


                if (!(pElements.size() == 2 && (pElements.get(1).tag().getName().equals("img") || pElements.get(1).tag().getName().equals("p")))
                        && !elements.get(i).html().equals("") && !String.valueOf(Html.fromHtml(elements.get(i).html())).equals("")) {

                    FansTextView textView = new FansTextView(this, "light");

                    textView.setTextColor(Color.BLACK);
                    textView.setLineSpacing(1.4f, 1.4f);
                    textView.setTextIsSelectable(true);

                    String text = String.valueOf(elements.get(i).html());

                    if (elements.get(i).nextSibling() != null && elements.get(i).nextSibling() instanceof TextNode) {
                        String nextSibling = elements.get(i).nextSibling().toString();
                        text = text + " " + nextSibling;
                    }

                    textView.setText(Html.fromHtml(text));

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins((int) getResources().getDimension(R.dimen.simplified_paragraph_other_margin),
                            (int) getResources().getDimension(R.dimen.simplified_paragraph_top_margin),
                            (int) getResources().getDimension(R.dimen.simplified_paragraph_other_margin),
                            (int) getResources().getDimension(R.dimen.simplified_paragraph_other_margin));
                    textView.setLayoutParams(lp);

                    if (elements.get(i).tag().getName().equals("p"))
                        textView.setTag(0);
                    else if (elements.get(i).tag().getName().equals("tr"))
                        textView.setTag(0);
                    else if (elements.get(i).tag().getName().equals("h1"))
                        textView.setTag(6);
                    else if (elements.get(i).tag().getName().equals("h2"))
                        textView.setTag(4);
                    else if (elements.get(i).tag().getName().equals("h3"))
                        textView.setTag(2);

                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                    textView.setAutoLinkMask(Linkify.ALL);

                    articleContent.addView(textView);
                }
            }
        }
    }

    private void initializeToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private void initializeScrollView() {

        final ImageView image = (ImageView) findViewById(R.id.image);

        ObservableScrollView readerViewCont = (ObservableScrollView) findViewById(R.id.scroll_view);

        readerViewCont.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

                ViewHelper.setTranslationY(image, scrollY / 2);

                if (scrollY > currentOffset && scrollY > 50) {
                    if (!fabHidden) {
                        toolbar.animate().alpha(0.1f);
                        fabHidden = true;
                    }
                } else if (scrollY < currentOffset) {
                    if (fabHidden) {
                        toolbar.animate().alpha(1f);
                        fabHidden = false;
                    }
                }

                currentOffset = scrollY;
            }

            @Override
            public void onDownMotionEvent() {
                if (!fabHidden) {
                    toolbar.animate().alpha(0.1f);
                    fabHidden = true;
                }
            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {
            }
        });
    }

    private void loadWebView(String url) {

        /*WebView webView = (WebView) findViewById(R.id.article_web_view);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private class CustomWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress > 70)
                findViewById(R.id.progress_bar).setVisibility(View.GONE);

        }
    }
}
