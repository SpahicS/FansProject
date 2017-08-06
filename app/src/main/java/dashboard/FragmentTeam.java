package dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import adapters.LeagueTableAdapter;
import digitalbath.fansproject.R;
import models.team_data.Fixtures;
import models.team_data.LeagueTable;
import models.team_data.Team;
import networking.TeamAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

/**
 * Created by unexpected_err on 29/04/2017.
 */

public class FragmentTeam extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    LeagueTableAdapter leagueTableAdapter;
    RecyclerView leagueTableRecycler;

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
        leagueTableRecycler = (RecyclerView) rootView.findViewById(R.id.league_table_recycler);

        leagueTableRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        getLeagueTable();



        return rootView;
    }

    private void getTeamData() {
        TeamAPI.service.getTeamData().enqueue(new Callback<Team>() {
            @Override
            public void onResponse(Call<Team> call, Response<Team> response) {

            }

            @Override
            public void onFailure(Call<Team> call, Throwable t) {
            }
        });
    }

    private void getLeagueTable() {
        TeamAPI.service.getLeagueTable().enqueue(new Callback<LeagueTable>() {
            @Override
            public void onResponse(Call<LeagueTable> call, Response<LeagueTable> response) {

                leagueTableAdapter = new LeagueTableAdapter(getContext(), response.body());
                leagueTableRecycler.setAdapter(leagueTableAdapter);
            }

            @Override
            public void onFailure(Call<LeagueTable> call, Throwable t) {
            }
        });
    }

    private void getTeamFixtures() {
        TeamAPI.service.getTeamFixtures().enqueue(new Callback<Fixtures>() {
            @Override
            public void onResponse(Call<Fixtures> call, Response<Fixtures> response) {
            }

            @Override
            public void onFailure(Call<Fixtures> call, Throwable t) {
            }
        });
    }
}