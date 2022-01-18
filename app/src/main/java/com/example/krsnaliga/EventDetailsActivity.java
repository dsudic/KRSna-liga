package com.example.krsnaliga;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.koushikdutta.ion.Ion;

public class EventDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details_layout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3491DC")));


        ImageView eventCoverImg = (ImageView) findViewById(R.id.eventCoverImg);
        TextView eventName = (TextView) findViewById(R.id.eventName);
        TextView eventDate = (TextView) findViewById(R.id.eventDate);
        TextView eventLocation = (TextView) findViewById(R.id.eventLocation);
        TextView eventDescription = (TextView) findViewById(R.id.eventDescription);

        Event clickedEvent = null;

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int position = extras.getInt("eventPosition");
            switch (extras.getString("eventType")){
                case "2021":
                    clickedEvent = DataStorage.y2021[position];
                    break;
                case "2020":
                    clickedEvent =  DataStorage.y2020[position];
                    break;
                case "2019":
                    clickedEvent =  DataStorage.y2019[position];
                    break;
                case "Today":
                    clickedEvent =  DataStorage.todayEvent[position];
                    break;
                case "Upcoming":
                    clickedEvent =  DataStorage.upcomingEvents[position];
                    break;
                default:
                    clickedEvent =  null;
            };
        }

        if(clickedEvent != null){
            Ion.with(eventCoverImg).load(clickedEvent.image);
            eventName.setText(clickedEvent.name);
            eventDate.setText(clickedEvent.date);
            eventLocation.setText(clickedEvent.location);
            eventDescription.setText(clickedEvent.description);
            actionBar.setTitle(clickedEvent.name);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
