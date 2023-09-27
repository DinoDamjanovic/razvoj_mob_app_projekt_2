package damjanovic.meetingapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import damjanovic.meetingapp.model.Meeting;

@Dao
public interface MeetingDAO {

    @Query("SELECT * from meeting")
    LiveData<List<Meeting>> getAll();

    @Insert
    void insert(Meeting meeting);

    @Update
    void update(Meeting meeting);

    @Delete
    void delete(Meeting meeting);

}
