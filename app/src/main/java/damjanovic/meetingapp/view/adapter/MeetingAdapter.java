package damjanovic.meetingapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import damjanovic.meetingapp.R;
import damjanovic.meetingapp.model.Meeting;

public class MeetingAdapter extends BaseAdapter {

    private List<Meeting> meetings = new ArrayList<>();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    @Override
    public int getCount() {
        return meetings.size();
    }

    @Override
    public Object getItem(int index) {
        return meetings.get(index);
    }

    @Override
    public long getItemId(int index) {
        return meetings.get(index).getId();
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = convertView.findViewById(R.id.tvTitle);
            viewHolder.tvLocation = convertView.findViewById(R.id.tvLocation);
            viewHolder.tvDate = convertView.findViewById(R.id.tvDate);
            viewHolder.tvStartTime = convertView.findViewById(R.id.tvStartTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Meeting meeting = meetings.get(index);

        viewHolder.tvTitle.setText(getNonNullString(meeting.getTitle()));
        viewHolder.tvLocation.setText(getNonNullString(meeting.getLocationDescription()));
        viewHolder.tvDate.setText(getNonNullString(meeting.getFormattedMeetingDate(dateFormatter)));
        viewHolder.tvStartTime.setText(getNonNullString(meeting.getFormattedStartTime(timeFormatter)));

        return convertView;
    }

    private String getNonNullString(String value) {
        return value != null ? value : "";
    }

    static class ViewHolder {
        TextView tvTitle;
        TextView tvLocation;
        TextView tvDate;
        TextView tvStartTime;
    }


}
