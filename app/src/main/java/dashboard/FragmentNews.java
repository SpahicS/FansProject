package dashboard;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import java.util.Locale;

import adapters.NewsAdapter;
import digitalbath.fansproject.R;
import helpers.main.AppHelper;
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
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        newsRecycler = (RecyclerView) rootView.findViewById(R.id.news_list_recycler);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        getNewsList("Juventus");

        initializeNewsRecycler();

        return rootView;
    }

    private void initializeNewsRecycler() {

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        newsRecycler.setLayoutManager(manager);
    }

    private void getNewsList(String query) {

        String edition;

        SharedPreferences prefs = getContext()
            .getSharedPreferences("COUNTRY_CODES", Context.MODE_PRIVATE);

        edition = prefs.getString("COUNTRY_CODE", null);
        //String edition = getNewsEditionCode();

        if (edition == null) {
            edition = AppHelper.getNewsEditionCode(getContext());
        }

        NewsAPI.service.getNewsData(query, 20, "rss", edition).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                newsAdapter = new NewsAdapter(getActivity(), response.body());
                newsRecycler.setAdapter(newsAdapter);

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                int i = 0;
            }
        });
    }
}
