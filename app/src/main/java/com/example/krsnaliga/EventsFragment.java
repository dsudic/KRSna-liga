package com.example.krsnaliga;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.koushikdutta.ion.builder.Builders;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class EventsFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    private PopupMenu popup;
    private ListView eventsListView;
    private AsyncHttpClient myClient;
    private Gson myGson;
    private EventsListAdapter events2021adapter;
    private EventsListAdapter events2020adapter;
    private EventsListAdapter events2019adapter;
    private EventsListAdapter todayEventAdapter;
    private EventsListAdapter upcomingEventsAdapter;
    private String eventType;
    private TextView eventsMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Events");

        myClient = new AsyncHttpClient();
        myGson = new Gson();

        eventsListView = (ListView) getView().findViewById(R.id.eventsListView);
        events2021adapter = new EventsListAdapter(getContext(), "2021");
        events2020adapter = new EventsListAdapter(getContext(), "2020");
        events2019adapter = new EventsListAdapter(getContext(), "2019");
        todayEventAdapter = new EventsListAdapter(getContext(), "Today");
        upcomingEventsAdapter = new EventsListAdapter(getContext(), "Upcoming");
        eventsMessage = (TextView) getView().findViewById(R.id.eventsMessage);

        TabLayout tabLayout = (TabLayout) getView().findViewById(R.id.tabLayout);

        eventsListView.setEmptyView(eventsMessage);

        tabLayout.getTabAt(1).select();
        eventType = "Today";

        if (DataStorage.todayEvent == null) {
            getData("http://pzi1.fesb.hr/~dsudic19/androidApp/today.json", eventType);
        }
        eventsListView.setAdapter(todayEventAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        popup = new PopupMenu(getContext(), tab.view);
                        popup.setOnMenuItemClickListener(EventsFragment.this);
                        popup.inflate(R.menu.event_year_menu);
                        popup.show();
                        break;
                    case 1:
                        eventType = (String) tab.getText();
                        eventsListView.setAdapter(todayEventAdapter);
                        break;
                    case 2:
                        eventType = (String) tab.getText();
                        if (DataStorage.upcomingEvents == null) {
                            getData("http://pzi1.fesb.hr/~dsudic19/androidApp/upcoming.json", eventType);
                        } else {
                            eventsListView.setAdapter(upcomingEventsAdapter);
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
                    popup.show();
                }
            }
        });


        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent eventDetailsIntent = new Intent(getActivity(), EventDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("eventType", eventType);
                extras.putInt("eventPosition", position);
                eventDetailsIntent.putExtras(extras);
                startActivity(eventDetailsIntent);
            }
        });

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        item.setChecked(!item.isChecked());
        eventType = (String) item.getTitle();

        switch (eventType) {
            case "2021":
                if (DataStorage.y2021 == null) {
                    getData("http://pzi1.fesb.hr/~dsudic19/androidApp/finished/2021.json", eventType);
                } else {
                    eventsListView.setAdapter(events2021adapter);
                }
                return true;
            case "2020":
                if (DataStorage.y2020 == null) {
                    getData("http://pzi1.fesb.hr/~dsudic19/androidApp/finished/2020.json", eventType);
                } else {
                    eventsListView.setAdapter(events2020adapter);
                }
                return true;
            case "2019":
                if (DataStorage.y2019 == null) {
                    getData("http://pzi1.fesb.hr/~dsudic19/androidApp/finished/2019.json", eventType);
                } else {
                    eventsListView.setAdapter(events2019adapter);
                }
                return true;
            default:
                return false;
        }
    }

    public void getData(String url, String id) {
        myClient.get(url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                switch (id) {
                    case "2021":
                        DataStorage.y2021 = myGson.fromJson(responseString, Event[].class);
                        eventsListView.setAdapter(events2021adapter);
                        break;
                    case "2020":
                        DataStorage.y2020 = myGson.fromJson(responseString, Event[].class);
                        eventsListView.setAdapter(events2020adapter);
                        break;
                    case "2019":
                        DataStorage.y2019 = myGson.fromJson(responseString, Event[].class);
                        eventsListView.setAdapter(events2019adapter);
                        break;
                    case "Today":
                        DataStorage.todayEvent = myGson.fromJson(responseString, Event[].class);
                        eventsListView.setAdapter(todayEventAdapter);
                        break;
                    case "Upcoming":
                        DataStorage.upcomingEvents = myGson.fromJson(responseString, Event[].class);
                        eventsListView.setAdapter(upcomingEventsAdapter);
                        break;
                }
            }
        });
    }
}
