package dashboard;

import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.InputStream;
import java.util.ArrayList;

import SvgHelper.SvgDecoder;
import SvgHelper.SvgDrawableTranscoder;
import SvgHelper.SvgSoftwareLayerSetter;
import adapters.LeagueTableAdapter;
import digitalbath.fansproject.R;
import models.team_data.Fixture;
import models.team_data.Fixtures;
import models.team_data.LeagueTable;
import models.team_data.TeamInfo;
import networking.TeamAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by unexpected_err on 29/04/2017.
 */

public class FragmentTeam extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private RecyclerView leagueTableRecycler;
    private TextView homeTeamName;
    private TextView awayTeamName;
    private TextView homeTeamPastMatch;
    private TextView awayTeamPastMatch;
    private TextView homeTeamPastMatchGoals;
    private TextView awayTeamPastMatchGoals;
    private TextView homeTeam2PastMatch;
    private TextView awayTeam2PastMatch;
    private TextView homeTeam2PastMatchGoals;
    private TextView awayTeam2PastMatchGoals;
    private TextView stadium;
    private TextView matchTime;
    private ImageView homeTeamLogo;
    private ImageView awayTeamLogo;
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;
    private AVLoadingIndicatorView progressBar;

    public FragmentTeam() {
    }

    public static FragmentTeam newInstance(int sectionNumber) {

        FragmentTeam fragment = new FragmentTeam();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_team, container, false);

        initializeViews(rootView);

        initializeSVGHelper();

        initializeRecyclerView(rootView);

        getLeagueTable();

        getTeamFixtures();

        return rootView;
    }

    private void initializeViews(View rootView) {
        homeTeamName = (TextView) rootView.findViewById(R.id.home_team_name);
        homeTeamLogo = (ImageView) rootView.findViewById(R.id.home_team_logo);
        awayTeamName = (TextView) rootView.findViewById(R.id.away_team_name);
        awayTeamLogo = (ImageView) rootView.findViewById(R.id.away_team_logo);
        matchTime = (TextView) rootView.findViewById(R.id.match_time);
        homeTeamPastMatch = (TextView) rootView.findViewById(R.id.home_team_past_matches);
        awayTeamPastMatch = (TextView) rootView.findViewById(R.id.away_team_past_matches);
        homeTeamPastMatchGoals = (TextView) rootView.findViewById(R.id.home_team_past_matches_points);
        awayTeamPastMatchGoals = (TextView) rootView.findViewById(R.id.away_team_past_matches_points);
        homeTeam2PastMatch = (TextView) rootView.findViewById(R.id.home_team2_past_matches);
        awayTeam2PastMatch = (TextView) rootView.findViewById(R.id.away_team2_past_matches);
        homeTeam2PastMatchGoals = (TextView) rootView.findViewById(R.id.home_team2_past_matches_points);
        awayTeam2PastMatchGoals = (TextView) rootView.findViewById(R.id.away_team2_past_matches_points);
        progressBar = (AVLoadingIndicatorView) rootView.findViewById(R.id.progressBarNews);
    }

    private void initializeSVGHelper() {
        requestBuilder = Glide.with(getContext())
                .using(Glide.buildStreamModelLoader(Uri.class, getContext()), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());
    }

    private void initializeRecyclerView(View rootView) {
        leagueTableRecycler = (RecyclerView) rootView.findViewById(R.id.league_table_recycler);
        leagueTableRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getTeamData(int teamId, final boolean homeTeam) {
        TeamAPI.service.getTeamData(teamId).enqueue(new Callback<TeamInfo>() {
            @Override
            public void onResponse(Call<TeamInfo> call, Response<TeamInfo> response) {
                Uri uri = Uri.parse(response.body().getCrestUrl());

                if (homeTeam) {
                    requestBuilder
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .load(uri)
                            .into(homeTeamLogo);
                } else {
                    requestBuilder
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .load(uri)
                            .into(awayTeamLogo);
                }
            }

            @Override
            public void onFailure(Call<TeamInfo> call, Throwable t) {
            }
        });
    }

    private void getLeagueTable() {
        TeamAPI.service.getLeagueTable().enqueue(new Callback<LeagueTable>() {
            @Override
            public void onResponse(Call<LeagueTable> call, Response<LeagueTable> response) {

                initializeLeagueTableAdapter(response);

                TextView matchDay = (TextView) getActivity().findViewById(R.id.match_day);
                matchDay.setText(" (Match day " + String.valueOf(response.body().getMatchDay()) + ")");
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<LeagueTable> call, Throwable t) {
            }
        });
    }

    private void initializeLeagueTableAdapter(Response<LeagueTable> response) {
        LeagueTableAdapter leagueTableAdapter = new LeagueTableAdapter(getContext(), response.body());
        leagueTableRecycler.setAdapter(leagueTableAdapter);
    }

    private void getTeamFixtures() {
        TeamAPI.service.getTeamFixtures().enqueue(new Callback<Fixtures>() {
            @Override
            public void onResponse(Call<Fixtures> call, Response<Fixtures> response) {

                findNextMatch(response);

            }

            @Override
            public void onFailure(Call<Fixtures> call, Throwable t) {
            }
        });
    }

    private void findNextMatch(Response<Fixtures> response) {

        ArrayList<Fixture> fixtures = response.body().getFixtures();

        if (fixtures.get(0).getStatus() == null) {
            homeTeamName.setText(fixtures.get(0).getHomeTeamName());
            awayTeamName.setText(fixtures.get(0).getAwayTeamName());
            matchTime.setText(fixtures.get(0).getDate());
            String homeTeamId = fixtures.get(0).getLinks().getHomeTeam().getHref().split("teams/")[1];
            String awayTeamId = fixtures.get(0).getLinks().getAwayTeam().getHref().split("teams/")[1];

            homeTeamPastMatch.setText("N/A");
            awayTeamPastMatch.setText("N/A");
            homeTeamPastMatchGoals.setText("N/A");
            awayTeamPastMatchGoals.setText("N/A");

            homeTeam2PastMatch.setText("N/A");
            awayTeam2PastMatch.setText("N/A");
            homeTeam2PastMatchGoals.setText("N/A");
            awayTeam2PastMatchGoals.setText("N/A");

            getTeamData(Integer.parseInt(homeTeamId), true);
            getTeamData(Integer.parseInt(awayTeamId), false);
        } else {
            for (int i = 0; i < response.body().getFixtures().size(); i++) {

                if (fixtures.get(i).getStatus().equals("TIMED")) {
                    homeTeamName.setText(fixtures.get(i).getHomeTeamName());
                    awayTeamName.setText(fixtures.get(i).getAwayTeamName());
                    matchTime.setText(fixtures.get(i).getDate());
                    String homeTeamId = fixtures.get(i).getLinks().getHomeTeam().getHref().split("teams/")[1];
                    String awayTeamId = fixtures.get(i).getLinks().getAwayTeam().getHref().split("teams/")[1];

                    getTeamData(Integer.parseInt(homeTeamId), true);
                    getTeamData(Integer.parseInt(awayTeamId), false);

                    if (i == 0) {
                        homeTeamPastMatch.setText("N/A");
                        awayTeamPastMatch.setText("N/A");
                        homeTeamPastMatchGoals.setText("N/A");
                        awayTeamPastMatchGoals.setText("N/A");

                        homeTeam2PastMatch.setText("N/A");
                        awayTeam2PastMatch.setText("N/A");
                        homeTeam2PastMatchGoals.setText("N/A");
                        awayTeam2PastMatchGoals.setText("N/A");
                    }

                    if (i == 1) {
                        homeTeamPastMatch.setText(fixtures.get(0).getHomeTeamName());
                        awayTeamPastMatch.setText(fixtures.get(0).getAwayTeamName());
                        homeTeamPastMatchGoals.setText(fixtures.get(0).getResult().getGoalsHomeTeam());
                        awayTeamPastMatchGoals.setText(fixtures.get(0).getResult().getGoalsAwayTeam());

                        homeTeam2PastMatch.setText("N/A");
                        awayTeam2PastMatch.setText("N/A");
                        homeTeam2PastMatchGoals.setText("N/A");
                        awayTeam2PastMatchGoals.setText("N/A");
                    }

                    if (i > 1) {
                        homeTeamPastMatch.setText(fixtures.get(i - 1).getHomeTeamName());
                        awayTeamPastMatch.setText(fixtures.get(i - 1).getAwayTeamName());
                        homeTeamPastMatchGoals.setText(fixtures.get(i - 1).getResult().getGoalsHomeTeam());
                        awayTeamPastMatchGoals.setText(fixtures.get(i - 1).getResult().getGoalsAwayTeam());
                        homeTeam2PastMatch.setText(fixtures.get(i - 2).getHomeTeamName());
                        awayTeam2PastMatch.setText(fixtures.get(i - 2).getAwayTeamName());
                        homeTeam2PastMatchGoals.setText(fixtures.get(i - 2).getResult().getGoalsHomeTeam());
                        awayTeam2PastMatchGoals.setText(fixtures.get(i - 2).getResult().getGoalsAwayTeam());
                    }
                }
            }
        }
    }
}