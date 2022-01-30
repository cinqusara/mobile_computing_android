package com.example.maledettatreest2.posts;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

public class OfficialPost {
    private final String title;
    private final LocalDateTime timestamp;
    private final String direction;

    public OfficialPost(String title, LocalDateTime timestamp, String direction) {
        this.title = title;
        this.timestamp = timestamp; //forse meglio LocalDateTime
        this.direction = direction;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDirection() {
        return direction;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDataString(){
        return String.valueOf(this.timestamp.getDayOfMonth() + "/" +  this.timestamp.getMonth().getValue() + "/" +  this.timestamp.getYear());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getOraString(){
        String ore = String.format("%02d",this.timestamp.getHour());
        String minuti = String.format("%02d",this.timestamp.getMinute());
        return (ore + ":" + minuti);
    }

    @Override
    public String toString() {
        return "OfficialPost{" +
                "title='" + title + '\'' +
                ", timestamp=" + timestamp +
                ", direction='" + direction + '\'' +
                '}';
    }
}
