package dashboard;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import adapters.NewsAdapter;
import digitalbath.fansproject.R;
import helpers.main.AppHelper;
import models.Item;
import models.NewsItem;
import models.ResponseData;
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
    private ProgressBar progressBar;
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

        getNewsList("Juventus");

        initializeNewsRecycler();

        return rootView;
    }

    private void initializeViews(View rootView) {

        newsRecycler = (RecyclerView) rootView.findViewById(R.id.news_list_recycler);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarNews);

    }

    private void initializeNewsRecycler() {

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        newsRecycler.setLayoutManager(manager);

    }

    private void getNewsList(String query) {

        String edition = getNewsEditionCode();

        NewsAPI.service.getNewsData(query, 20, "rss", edition).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                ArrayList<NewsItem> newsItems = getNewsItems(response.body().getChannel().getNewsList());
                newsAdapter = new NewsAdapter(getActivity(), newsItems, mCommentsCont);
                newsRecycler.setAdapter(newsAdapter);

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                int i = 0;
            }
        });
    }

    private ArrayList<NewsItem> getNewsItems(List<Item> newsList) {

        ArrayList<NewsItem> newsItems = new ArrayList<>(newsList.size());

        for (int i = 0; i < newsList.size(); i++) {
            newsItems.add(new NewsItem(newsList.get(i)));
        }

        return newsItems;
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
