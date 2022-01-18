package com.example.krsnaliga;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.koushikdutta.ion.Ion;

public class ResultsFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    private PopupMenu yearPopupMenu;
    private PopupMenu eventsPopupMenu;
    private String selectedYearId;
    private int currentTab;
    private ImageView resultsImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Results");

        TabLayout tabLayoutResults = (TabLayout) getView().findViewById(R.id.tabLayoutResults);
        resultsImage = (ImageView) getView().findViewById(R.id.resultsImage);

        tabLayoutResults.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        if (yearPopupMenu == null) {
                            yearPopupMenu = new PopupMenu(getContext(), tab.view);
                            yearPopupMenu.setOnMenuItemClickListener(ResultsFragment.this);
                            yearPopupMenu.inflate(R.menu.event_year_results_menu);
                        }
                        yearPopupMenu.show();
                        currentTab = tab.getPosition();
                        break;
                    case 1:
                        if (selectedYearId != null) {
                            eventsPopupMenu = new PopupMenu(getContext(), tab.view);
                            eventsPopupMenu.setOnMenuItemClickListener(ResultsFragment.this);
                            FillEventsInMenu();
                            eventsPopupMenu.getMenu().setGroupCheckable(0, true, true);
                            eventsPopupMenu.show();
                            currentTab = tab.getPosition();
                        } else {
                            Toast.makeText(getContext(), "Select the year first", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    if (yearPopupMenu == null) {
                        yearPopupMenu = new PopupMenu(getContext(), tab.view);
                        yearPopupMenu.setOnMenuItemClickListener(ResultsFragment.this);
                        yearPopupMenu.inflate(R.menu.event_year_results_menu);
                    }
                    yearPopupMenu.show();
                } else if (tab.getPosition() == 1 && selectedYearId != null) {
                    eventsPopupMenu.show();
                }
            }
        });
    }

    public void FillEventsInMenu() {
        int i = 0;
        switch (selectedYearId) {
            case "2021":
                if(DataStorage.y2021 != null) {
                    for (Event event : DataStorage.y2021) {
                        eventsPopupMenu.getMenu().add(0, 0, i++, event.name);
                    }
                }
                break;
            case "2020":
                if(DataStorage.y2020 != null) {
                    for (Event event : DataStorage.y2020) {
                        eventsPopupMenu.getMenu().add(0, 0, i++, event.name);
                    }
                }
                break;
            case "2019":
                if(DataStorage.y2019 != null) {
                    for (Event event : DataStorage.y2019) {
                        eventsPopupMenu.getMenu().add(0, 0, i++, event.name);
                    }
                }
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        item.setChecked(!item.isChecked());

        switch (currentTab) {
            case 0:
                selectedYearId = (String) item.getTitle();
                DataHandler.fillData(selectedYearId);
                break;
            case 1:
                Ion.with(resultsImage).load(GetResults(item.getOrder()));
                break;
        }
        return true;
    }

    public String GetResults(int position) {
        switch (selectedYearId) {
            case "2021":
                return DataStorage.y2021[position].results;
            case "2020":
                return DataStorage.y2020[position].results;
            case "2019":
                return DataStorage.y2019[position].results;
            default:
                return "";
        }
    }
}
