package ides.link.androidtask;

import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import ides.link.androidtask.models.CountriesModel;
import ides.link.androidtask.network.ApiUtils;
import ides.link.androidtask.network.AppServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CountriesFragment extends Fragment {

    private CountriesAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private AppServices mService;
    private ArrayList<CountriesModel> list;
    private ArrayList<CountriesModel> selectedList;
    public static final String COUNTRIES_LIST = "countries_list";
    public static final String SELECTED_COUNTRIES_LIST = "sel_countries_list";

    public static final String BASE_URL = "https://restcountries.eu/";


    public CountriesFragment() {
        // Required empty public constructor
        //todo fix loading and ui
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_countries, container, false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.countries_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CountriesAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            list = savedInstanceState.getParcelableArrayList(COUNTRIES_LIST);
            selectedList = savedInstanceState.getParcelableArrayList(SELECTED_COUNTRIES_LIST);
            mAdapter.swapData(selectedList);
        } else {
            mService = ApiUtils.getAppService(BASE_URL);
            loadAnswers();
        }

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCountries(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchCountries(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    public void searchCountries(String query) {
        if (query.equals("") || query.length() < 2) {
            mAdapter.swapData(null);
        }else{
            selectedList = new ArrayList<CountriesModel>();
            Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
            for (CountriesModel curVal : list) {
                if (pattern.matcher(curVal.getCapital()).find()) {
                    selectedList.add(curVal);
                }
            }
            mAdapter.swapData(selectedList);
        }


    }

    public void loadAnswers() {
        mService.getCountries().enqueue(new Callback<List<CountriesModel>>() {
            @Override
            public void onResponse(Call<List<CountriesModel>> call, Response<List<CountriesModel>> response) {

                if (response.isSuccessful()) {
                    list = (ArrayList<CountriesModel>) response.body();

                } else {
                    int statusCode = response.code();
                    Log.d("MainActivity", "error" + statusCode);
                }
            }

            @Override
            public void onFailure(Call<List<CountriesModel>> call, Throwable t) {

                Log.d("MainActivity", "error loading from API");

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(COUNTRIES_LIST, (ArrayList<CountriesModel>) list);
        outState.putParcelableArrayList(SELECTED_COUNTRIES_LIST, (ArrayList<CountriesModel>) selectedList);


    }
}
