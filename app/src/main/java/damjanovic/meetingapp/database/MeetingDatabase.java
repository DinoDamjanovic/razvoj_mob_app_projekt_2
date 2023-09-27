package damjanovic.meetingapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.Objects;

import damjanovic.meetingapp.model.Meeting;

@Database(entities = {Meeting.class}, version = 1, exportSchema = false)
@TypeConverters({LocalDateConverter.class, LocalTimeConverter.class})
public abstract class MeetingDatabase extends RoomDatabase {

    private static MeetingDatabase instance;

    public abstract MeetingDAO meetingDAO();

    public static MeetingDatabase getInstance(Context context) {
        if (Objects.isNull(instance)) {
            instance = Room.databaseBuilder(context.getApplicationContext(), MeetingDatabase.class, "meeting_database")
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }
}
