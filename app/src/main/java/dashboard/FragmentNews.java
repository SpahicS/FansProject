package dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import adapters.NewsAdapter;
import digitalbath.fansproject.R;
import models.Item;
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

        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        newsRecycler = (RecyclerView) rootView.findViewById(R.id.news_list_recycler);

        getNewsList("Juventus");

        initializeNewsRecycler();

        return rootView;
    }

    private void initializeNewsRecycler() {

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        newsRecycler.setLayoutManager(manager);
    }

    private void getNewsList(String query) {

        String newsEdition = Locale.getDefault().getCountry();
        NewsAPI.service.getNewsData(query, "rss", newsEdition.toLowerCase()).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                newsAdapter = new NewsAdapter(getActivity(), response.body());
                newsRecycler.setAdapter(newsAdapter);

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                int i = 0;
            }
        });
    }
}
