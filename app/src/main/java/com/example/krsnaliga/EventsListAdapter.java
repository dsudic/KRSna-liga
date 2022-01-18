package com.example.krsnaliga;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

public class EventsListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    String eventType;

    public EventsListAdapter(Context context, String event) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.eventType = event;
    }

    @Override
    public int getCount() {
        switch (eventType) {
            case "2021":
                return DataStorage.y2021.length;
            case "2020":
                return DataStorage.y2020.length;
            case "2019":
                return DataStorage.y2019.length;
            case "Today":
                return (DataStorage.todayEvent == null) ? 0 : 1;
            case "Upcoming":
                return DataStorage.upcomingEvents.length;
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_layout, parent, false);
        }

        ImageView eventTmb = (ImageView) convertView.findViewById(R.id.eventTmb);
        TextView eventName = (TextView) convertView.findViewById(R.id.eventName);
        TextView eventDate = (TextView) convertView.findViewById(R.id.eventDate);

        Event currentEvent;
        switch (eventType) {
            case "2021":
                currentEvent = DataStorage.y2021[position];
                break;
            case "2020":
                currentEvent = DataStorage.y2020[position];
                break;
            case "2019":
                currentEvent = DataStorage.y2019[position];
                break;
            case "Upcoming":
                currentEvent = DataStorage.upcomingEvents[position];
                break;
            default:
                currentEvent = DataStorage.todayEvent[position];
        }

        Ion.with(eventTmb).load(currentEvent.image);
        eventName.setText(currentEvent.name);
        eventDate.setText(currentEvent.date);

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        switch (eventType) {
            case "2021":
                return DataStorage.y2021[position];
            case "2020":
                return DataStorage.y2020[position];
            case "2019":
                return DataStorage.y2019[position];
            case "Today":
                return DataStorage.todayEvent[position];
            case "Upcoming":
                return DataStorage.upcomingEvents[position];
            default:
                return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
