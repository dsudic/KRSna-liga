package com.example.krsnaliga;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class DataHandler {

    public static void fillData(String id) {
        switch (id) {
            case "2021":
                if (DataStorage.y2021 == null) {
                    getData("http://pzi1.fesb.hr/~dsudic19/androidApp/finished/2021.json", id);
                }
                break;
            case "2020":
                if (DataStorage.y2020 == null) {
                    getData("http://pzi1.fesb.hr/~dsudic19/androidApp/finished/2020.json", id);
                }
                break;
            case "2019":
                if (DataStorage.y2019 == null) {
                    getData("http://pzi1.fesb.hr/~dsudic19/androidApp/finished/2019.json", id);
                }
                break;
            case "Today":
                if (DataStorage.todayEvent == null) {
                    getData("http://pzi1.fesb.hr/~dsudic19/androidApp/today.json", id);
                }
                break;
            case "Upcoming":
                if (DataStorage.upcomingEvents == null) {
                    getData("http://pzi1.fesb.hr/~dsudic19/androidApp/upcoming.json", id);
                }
                break;
        }
    }


    public static void getData(String url, String id) {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        Gson myGson = new Gson();

        httpClient.get(url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                switch (id) {
                    case "2021":
                        DataStorage.y2021 = myGson.fromJson(responseString, Event[].class);
                        break;
                    case "2020":
                        DataStorage.y2020 = myGson.fromJson(responseString, Event[].class);
                        break;
                    case "2019":
                        DataStorage.y2019 = myGson.fromJson(responseString, Event[].class);
                        break;
                    case "Today":
                        DataStorage.todayEvent = myGson.fromJson(responseString, Event[].class);
                        break;
                    case "Upcoming":
                        DataStorage.upcomingEvents = myGson.fromJson(responseString, Event[].class);
                        break;
                }
            }
        });
    }
}
