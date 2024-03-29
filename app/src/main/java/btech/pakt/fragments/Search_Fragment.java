package btech.pakt.fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import btech.pakt.ListAdapter_ItemSearch;
import btech.pakt.Item_Description_Class;
import btech.pakt.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Search_Fragment extends Fragment {

    //Custom listview
    RecyclerView recView;
    List<Item_Description_Class> itemList = new ArrayList<>();
    ListAdapter_ItemSearch listAdapter;

    //Layout Widgets
    SearchView search;

    //Toolbar
    Toolbar toolbar;

    public Search_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();
        toolbar = (Toolbar) getActivity().findViewById(R.id.my_awesome_toolbar);
        recView = (RecyclerView) v.findViewById(R.id.listView);
        search = (SearchView) v.findViewById(R.id.searchView);

        initialize();

        return v;
    }

    public void initialize(){

        recView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recView.setLayoutManager(llm);

        ArrayList testItems = new ArrayList();


        listAdapter = new ListAdapter_ItemSearch(itemList, getActivity());

        recView.setAdapter(listAdapter);

        toolbar.setTitle("Search");
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getSupportFragmentManager().popBackStack();     //.replace(R.id.fragmentContainer, new Profile_Fragment())

            }
        });


    }










}
