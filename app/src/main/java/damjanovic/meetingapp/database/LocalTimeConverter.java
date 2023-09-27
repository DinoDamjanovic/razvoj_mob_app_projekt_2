package damjanovic.meetingapp.database;

import androidx.room.TypeConverter;

import java.time.LocalTime;

public class LocalTimeConverter {
    @TypeConverter
    public static LocalTime fromTimestamp(String value) {
        return value == null ? null : LocalTime.parse(value);
    }

    @TypeConverter
    public static String timeToTimestamp(LocalTime time) {
        return time == null ? null : time.toString();
    }
}