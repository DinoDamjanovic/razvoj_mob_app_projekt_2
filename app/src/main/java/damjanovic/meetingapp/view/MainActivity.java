package damjanovic.meetingapp.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import damjanovic.meetingapp.R;
import damjanovic.meetingapp.viewmodel.MeetingViewModel;

public class MainActivity extends AppCompatActivity {

    private MeetingViewModel meetingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meetingViewModel = new ViewModelProvider(this).get(MeetingViewModel.class);
        openMeetingsListFragment();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void openMeetingsListFragment() {
        setFragment(new MeetingsListFragment());
    }

    public void launchEditFragment(boolean updateMode) {
        MeetingEditorFragment fragment = new MeetingEditorFragment();

        Bundle bundle = new Bundle();
        bundle.putBoolean("updateMode", updateMode);
        fragment.setArguments(bundle);

        setFragment(fragment);
    }

    public MeetingViewModel getMeetingViewModel() {
        return meetingViewModel;
    }

}
