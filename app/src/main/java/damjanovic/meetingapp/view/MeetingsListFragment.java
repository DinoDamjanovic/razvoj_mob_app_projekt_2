package damjanovic.meetingapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import damjanovic.meetingapp.databinding.FragmentItemsListBinding;
import damjanovic.meetingapp.model.Meeting;
import damjanovic.meetingapp.view.adapter.MeetingAdapter;
import damjanovic.meetingapp.viewmodel.MeetingViewModel;

public class MeetingsListFragment extends Fragment {

    private MeetingViewModel meetingViewModel;
    private MeetingAdapter adapter;
    private FragmentItemsListBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentItemsListBinding.inflate(inflater, container, false);

        initializeUI();

        return binding.getRoot();
    }

    private void initializeUI() {
        adapter = new MeetingAdapter();
        binding.listView.setAdapter(adapter);

        meetingViewModel = ((MainActivity) requireActivity()).getMeetingViewModel();

        binding.listView.setOnItemClickListener((parent, view, position, id) -> {
            Meeting selectedMeeting = adapter.getMeetings().get(position);
            meetingViewModel.setMeeting(selectedMeeting);

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).launchEditFragment(true);
            }
        });

        binding.btnCreate.setOnClickListener(v -> {
            meetingViewModel.setMeeting(new Meeting());

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).launchEditFragment(false);
            }
        });

        loadMeetings();
    }

    private void loadMeetings() {
        meetingViewModel.getAll().observe(getViewLifecycleOwner(), meetings -> {
            adapter.setMeetings(meetings);
            adapter.notifyDataSetChanged();
        });
    }
}
