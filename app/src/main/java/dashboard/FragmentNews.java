package dashboard;


import android.content.Context;
import android.content.SharedPreferences;
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

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import adapters.NewsAdapter;
import digitalbath.fansproject.R;
import helpers.main.AppConfig;
import helpers.main.AppHelper;
import models.news.ArticleItem;
import models.news.Item;
import models.news.ResponseData;
import networking.NewsAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by unexpected_err on 29/04/2017.
 */

public class FragmentNews extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private NewsAdapter newsAdapter;
    private RecyclerView newsRecycler;
    private AVLoadingIndicatorView progressBar;
    private RelativeLayout mCommentsCont;


    public FragmentNews() {
    }

    public static FragmentNews newInstance(int sectionNumber) {

        FragmentNews fragment = new FragmentNews();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        mCommentsCont = (RelativeLayout) rootView.findViewById(R.id.comments_cont);

        initializeViews(rootView);

        expandAppBar();

        getNewsList(AppConfig.getNewsQuery());

        initializeNewsRecycler();

        return rootView;
    }

    private void expandAppBar() {
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true, true);
    }

    public void initializeViews(View rootView) {

        newsRecycler = (RecyclerView) rootView.findViewById(R.id.news_list_recycler);
        progressBar = (AVLoadingIndicatorView) rootView.findViewById(R.id.progressBarNews);

    }

    public void initializeNewsRecycler() {

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        newsRecycler.setLayoutManager(manager);

    }

    public void getNewsList(String query) {

        String edition = getNewsEditionCode();

        NewsAPI.service.getNewsData(query, query, 20, edition, edition, edition).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                ArrayList<ArticleItem> articleItems = getNewsItems(response.body().getChannel().getNewsList());
                newsAdapter = new NewsAdapter(getActivity(), articleItems, mCommentsCont);
                newsRecycler.setAdapter(newsAdapter);

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                int i = 0;
            }
        });
    }

    private ArrayList<ArticleItem> getNewsItems(List<Item> newsList) {

        ArrayList<ArticleItem> articleItems = new ArrayList<>(newsList.size());

        for (int i = 0; i < newsList.size(); i++) {
            articleItems.add(new ArticleItem(newsList.get(i)));
        }

        return articleItems;
    }

    private String getNewsEditionCode() {

        String edition;

        SharedPreferences prefs = getContext()
                .getSharedPreferences("COUNTRY_CODES", Context.MODE_PRIVATE);

        edition = prefs.getString("COUNTRY_CODE", null);

        if (edition == null) {
            edition = AppHelper.getNewsEditionCode(getContext());
        }
        return edition;
    }
}
