package dashboard;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.txusballesteros.widgets.FitChart;

import java.util.ArrayList;

import adapters.LeagueTableAdapter;
import digitalbath.fansproject.R;
import helpers.main.AppConfig;
import models.team_data.Fixture;
import models.team_data.Fixtures;
import models.team_data.LeagueTable;
import models.team_data.TeamInfo;
import models.team_data.TeamStanding;
import networking.TeamAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by unexpected_err on 29/04/2017.
 */

public class FragmentTeam extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String API_KEY = "1968a7f095be48269a20d2bd89f6e930";

    private Call<LeagueTable> leagueTableCall;
    private Call<TeamInfo> teamInfoCall;
    private Call<Fixtures> teamFixturesCall;
    private RecyclerView leagueTableRecycler;
    private TextView awayTeamName;
    private TextView homeTeamPastMatch;
    private TextView awayTeamPastMatch;
    private TextView homeTeamPastMatchGoals;
    private TextView awayTeamPastMatchGoals;
    private TextView homeTeam2PastMatch;
    private TextView awayTeam2PastMatch;
    private TextView homeTeam2PastMatchGoals;
    private TextView awayTeam2PastMatchGoals;
    private TextView matchDate;
    private TextView leagueName;
    private View rootView;

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

        rootView = inflater.inflate(R.layout.fragment_team, container, false);

        initializeViews(rootView);

        expandAppBar();

        initializeLeagueTableRecyclerView(rootView);

        getLeagueTable();

        getTeamFixtures();

        return rootView;
    }

    private void expandAppBar() {
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true, true);
    }

    private void initializeViews(View rootView) {

        homeTeamPastMatch = (TextView) rootView.findViewById(R.id.home_team_past_matches);
        awayTeamPastMatch = (TextView) rootView.findViewById(R.id.away_team_past_matches);
        homeTeamPastMatchGoals = (TextView) rootView.findViewById(R.id.home_team_past_matches_points);
        awayTeamPastMatchGoals = (TextView) rootView.findViewById(R.id.away_team_past_matches_points);
        homeTeam2PastMatch = (TextView) rootView.findViewById(R.id.home_team2_past_matches);
        awayTeam2PastMatch = (TextView) rootView.findViewById(R.id.away_team2_past_matches);
        homeTeam2PastMatchGoals = (TextView) rootView.findViewById(R.id.home_team2_past_matches_points);
        awayTeam2PastMatchGoals = (TextView) rootView.findViewById(R.id.away_team2_past_matches_points);
        awayTeamName = (TextView) rootView.findViewById(R.id.away_team);
        matchDate = (TextView) rootView.findViewById(R.id.match_date_time);
        leagueName = (TextView) rootView.findViewById(R.id.league_name);
    }

    private void bindOverViewData(View rootView, TeamStanding teamStanding) {

        setChartValue((FitChart) rootView.findViewById(R.id.chart_matches),
                (TextView) rootView.findViewById(R.id.number_of_matches), teamStanding.getPlayedGames());

        setChartValue((FitChart) rootView.findViewById(R.id.chart_win),
                (TextView) rootView.findViewById(R.id.number_win), teamStanding.getWins());

        setChartValue((FitChart) rootView.findViewById(R.id.chart_lost),
                (TextView) rootView.findViewById(R.id.number_lost), teamStanding.getLosses());

        setChartValue((FitChart) rootView.findViewById(R.id.chart_draw),
                (TextView) rootView.findViewById(R.id.number_draw), teamStanding.getDraws());
    }

    private void setChartValue(FitChart chart, TextView number, float value) {

        float min = 0f;
        float max = 38;

        chart.setMinValue(min);
        chart.setMaxValue(max);
        chart.setValue(value);

        number.setText(Integer.toString((int) value));

    }

    private void initializeLeagueTableRecyclerView(View rootView) {

        leagueTableRecycler = (RecyclerView) rootView.findViewById(R.id.league_table_recycler);
        leagueTableRecycler.setNestedScrollingEnabled(false);
        leagueTableRecycler.setFocusable(false);
        leagueTableRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void getLeagueTable() {

        leagueTableCall = TeamAPI.service.getLeagueTable(AppConfig.getTeamLeagueId(), API_KEY);

        leagueTableCall.enqueue(new Callback<LeagueTable>() {
            @Override
            public void onResponse(Call<LeagueTable> call, Response<LeagueTable> response) {

                initializeLeagueTableAdapter(response);

                leagueName.setText(response.body().getLeagueCaption());

                for (int i = 0; i < response.body().getStanding().size(); i++) {
                    if (response.body().getStanding().get(i).getTeamName()
                            .contains(AppConfig.getTeamName())) {
                        bindOverViewData(rootView, response.body().getStanding().get(i));
                        break;
                    }
                }

                /*TextView matchDay = (TextView) getActivity().findViewById(R.id.match_day);
                matchDay.setText(" (Match day " + String.valueOf(response.body().getMatchDay()) + ")");*/
            }

            @Override
            public void onFailure(Call<LeagueTable> call, Throwable t) {
            }
        });
    }

    private void initializeLeagueTableAdapter(Response<LeagueTable> response) {

        if (response.body() == null)
            return;

        LeagueTableAdapter leagueTableAdapter = new LeagueTableAdapter(getContext(), response.body());
        leagueTableRecycler.setAdapter(leagueTableAdapter);
    }

    private void getTeamFixtures() {

        teamFixturesCall = TeamAPI.service.getTeamFixtures(AppConfig.getTeamId(), API_KEY);

        teamFixturesCall.enqueue(new Callback<Fixtures>() {
            @Override
            public void onResponse(Call<Fixtures> call, Response<Fixtures> response) {

                findNextMatch(response);

            }

            @Override
            public void onFailure(Call<Fixtures> call, Throwable t) {
                int i = 9;
            }
        });
    }

    private void findNextMatch(Response<Fixtures> response) {

        if (response.body() == null)
            return;

        ArrayList<Fixture> fixtures = response.body().getFixtures();

        if (fixtures.get(0).getStatus() == null) {

            matchDate.setText(fixtures.get(0).getDate());

            if (!fixtures.get(0).getAwayTeamName()
                    .contains(AppConfig.getTeamName())) {
                awayTeamName.setText(fixtures.get(0).getAwayTeamName());
            } else {
                awayTeamName.setText(fixtures.get(0).getHomeTeamName());
            }

            setFirstPastMatchUnknown();

            setSecondPastMatchUnknown();

        } else {

            for (int i = 0; i < response.body().getFixtures().size(); i++) {

                if (fixtures.get(i).getStatus().equals("TIMED")
                        || fixtures.get(i).getStatus().equals("SCHEDULED")) {

                    matchDate.setText(fixtures.get(i).getDate());

                    if (!fixtures.get(i).getAwayTeamName()
                            .contains(AppConfig.getTeamName())) {
                        awayTeamName.setText(fixtures.get(i).getAwayTeamName());
                    } else {
                        awayTeamName.setText(fixtures.get(i).getHomeTeamName());
                    }

                    boolean isFirstMatch = i == 0;
                    boolean isSecondMatch = i == 1;

                    if (isFirstMatch) {

                        setFirstPastMatchUnknown();

                        setSecondPastMatchUnknown();

                        break;
                    }

                    if (isSecondMatch) {

                        getFirstPastMatch(fixtures, i);

                        setSecondPastMatchUnknown();

                        break;
                    }

                    if (i > 1) {

                        getFirstPastMatch(fixtures, i);

                        getSecondPastMatch(fixtures, i);

                        break;
                    }
                }
            }
        }
    }

    private void getFirstPastMatch(ArrayList<Fixture> fixtures, int i) {
        homeTeamPastMatch.setText(fixtures.get(i - 1).getHomeTeamName());
        awayTeamPastMatch.setText(fixtures.get(i - 1).getAwayTeamName());
        homeTeamPastMatchGoals.setText(fixtures.get(i - 1).getResult().getGoalsHomeTeam());
        awayTeamPastMatchGoals.setText(fixtures.get(i - 1).getResult().getGoalsAwayTeam());
    }

    private void getSecondPastMatch(ArrayList<Fixture> fixtures, int i) {
        homeTeam2PastMatch.setText(fixtures.get(i - 2).getHomeTeamName());
        awayTeam2PastMatch.setText(fixtures.get(i - 2).getAwayTeamName());
        homeTeam2PastMatchGoals.setText(fixtures.get(i - 2).getResult().getGoalsHomeTeam());
        awayTeam2PastMatchGoals.setText(fixtures.get(i - 2).getResult().getGoalsAwayTeam());
    }

    private void setFirstPastMatchUnknown() {
        homeTeamPastMatch.setText("N/A");
        awayTeamPastMatch.setText("N/A");
        homeTeamPastMatchGoals.setText("N/A");
        awayTeamPastMatchGoals.setText("N/A");
    }

    private void setSecondPastMatchUnknown() {
        homeTeam2PastMatch.setText("N/A");
        awayTeam2PastMatch.setText("N/A");
        homeTeam2PastMatchGoals.setText("N/A");
        awayTeam2PastMatchGoals.setText("N/A");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (leagueTableCall != null) {
            leagueTableCall.cancel();
        }
        if (teamInfoCall != null) {
            teamInfoCall.cancel();
        }
        if (teamFixturesCall != null) {
            teamFixturesCall.cancel();
        }
    }
}