package damjanovic.meetingapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity(tableName = "meeting")
public class Meeting {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;

    @NonNull
    private String title;

    private String description;

    private Location location;

    private LocalDate meetingDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private String meetingPhotoURI;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalDate getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(LocalDate meetingDate) {
        this.meetingDate = meetingDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getMeetingPhotoURI() {
        return meetingPhotoURI;
    }

    public void setMeetingPhotoURI(String meetingPhotoURI) {
        this.meetingPhotoURI = meetingPhotoURI;
    }

    public String getFormattedMeetingDate(DateTimeFormatter formatter) {
        return meetingDate != null ? meetingDate.format(formatter) : null;
    }

    public String getFormattedStartTime(DateTimeFormatter formatter) {
        return startTime != null ? startTime.format(formatter) : null;
    }

    public String getLocationDescription() {
        return location != null ? location.getDescription() : null;
    }

}

