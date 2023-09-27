package damjanovic.meetingapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import damjanovic.meetingapp.database.MeetingDAO;
import damjanovic.meetingapp.database.MeetingDatabase;
import damjanovic.meetingapp.model.Meeting;

public class MeetingViewModel extends AndroidViewModel {

    private final MeetingDAO meetingDAO;
    private Meeting meeting;

    public MeetingViewModel(@NonNull Application application) {
        super(application);
        meetingDAO = MeetingDatabase.getInstance(application.getApplicationContext()).meetingDAO();
    }

    public LiveData<List<Meeting>> getAll() {
        return meetingDAO.getAll();
    }

    public void create() {
        meetingDAO.insert(meeting);
    }

    public void update() {
        meetingDAO.update(meeting);
    }

    public void delete() {
        meetingDAO.delete(meeting);
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

}
