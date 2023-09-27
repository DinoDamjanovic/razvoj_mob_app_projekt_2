package damjanovic.meetingapp.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

import damjanovic.meetingapp.R;
import damjanovic.meetingapp.databinding.FragmentMeetingEditorBinding;
import damjanovic.meetingapp.model.Location;
import damjanovic.meetingapp.viewmodel.MeetingViewModel;

public class MeetingEditorFragment extends Fragment {

    private boolean updateMode;

    private FragmentMeetingEditorBinding binding;
    private MeetingViewModel meetingViewModel;

    private LocalDate selectedDate;
    private LocalTime selectedStartTime;
    private LocalTime selectedEndTime;

    private final ActivityResultLauncher<String> imagePickLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            this::handleImageResult);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMeetingEditorBinding.inflate(inflater, container, false);


        if (getArguments() != null) {
            updateMode = getArguments().getBoolean("updateMode");
        } else {
            updateMode = false;
        }

        initializeUI();

        return binding.getRoot();
    }

    private void initializeUI() {
        meetingViewModel = ((MainActivity) requireActivity()).getMeetingViewModel();

        if (!updateMode || Objects.isNull(meetingViewModel.getMeeting().getMeetingPhotoURI())) {
            Picasso.get().load(R.drawable.upload_icon).fit().into(binding.ivMeetingPhoto);
        } else if (updateMode && Objects.nonNull(meetingViewModel.getMeeting().getMeetingPhotoURI())) {
            Uri imageUri = Uri.parse(meetingViewModel.getMeeting().getMeetingPhotoURI());
            binding.ivMeetingPhoto.setImageURI(imageUri);
        }

        binding.btnSetPhoto.setOnClickListener(v -> {
            imagePickLauncher.launch("image/*");
        });

        ArrayAdapter<Location> locationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, Location.values());
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerLocation.setAdapter(locationAdapter);

        if (updateMode) {
            binding.etTitle.setText(meetingViewModel.getMeeting().getTitle());
            binding.etDescription.setText(meetingViewModel.getMeeting().getDescription());
            binding.spinnerLocation.setSelection(meetingViewModel.getMeeting().getLocation().ordinal());

            selectedDate = meetingViewModel.getMeeting().getMeetingDate();
            updateDateDisplay();
            selectedStartTime = meetingViewModel.getMeeting().getStartTime();
            updateStartTimeDisplay();
            selectedEndTime = meetingViewModel.getMeeting().getEndTime();
            updateEndTimeDisplay();
        }

        binding.btnDate.setOnClickListener(v -> showDatePicker());
        binding.btnStartTime.setOnClickListener(v -> showStartTimePicker());
        binding.btnEndTime.setOnClickListener(v -> showEndTimePicker());

        if (!updateMode || Objects.isNull(meetingViewModel.getMeeting().getEndTime())) {
            binding.btnEndTime.setEnabled(false);
        }

        binding.btnBack.setOnClickListener(v -> backToMeetingList());
        binding.btnDelete.setOnClickListener(v -> deleteMeeting());
        binding.btnSaveMeeting.setOnClickListener(v -> saveOrUpdateMeeting());

        if(!updateMode) {
            binding.btnDelete.setVisibility(View.GONE);
        }
    }

    private void handleImageResult(Uri uri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            float aspectRatio = (float) bitmap.getWidth() / (float) bitmap.getHeight();
            int scaledWidth = 150;
            int scaledHeight = Math.round(scaledWidth / aspectRatio);

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);

            String filename = "IMG_meeting_" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + ".jpg";

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

            Uri imageUri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (imageUri == null) {
                Toast.makeText(requireContext(), "Failed to save image.", Toast.LENGTH_SHORT).show();
                return;  // Exit the method to prevent further processing
            }

            try {
                OutputStream outputStream = requireContext().getContentResolver().openOutputStream(imageUri);
                if (outputStream == null) {
                    Toast.makeText(requireContext(), "Failed to save image.", Toast.LENGTH_SHORT).show();
                    return;
                }

                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Error processing image", Toast.LENGTH_SHORT).show();
            }

            Picasso.get().load(imageUri).into(binding.ivMeetingPhoto);
            meetingViewModel.getMeeting().setMeetingPhotoURI(imageUri.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error processing image", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePicker() {
        LocalDate initialDate = selectedDate != null ? selectedDate : LocalDate.now();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    LocalDate pickedDate = LocalDate.of(year, month + 1, dayOfMonth);
                    if (isDateInFuture(pickedDate)) {
                        selectedDate = pickedDate;
                        updateDateDisplay();
                    } else {
                        Toast.makeText(requireContext(), "Selected date should be in the future.", Toast.LENGTH_SHORT).show();
                    }
                },
                initialDate.getYear(),
                initialDate.getMonthValue() - 1,
                initialDate.getDayOfMonth()
        );
        datePickerDialog.show();
    }

    private boolean isDateInFuture(LocalDate pickedDate) {
        return pickedDate.isAfter(LocalDate.now());
    }

    private void showStartTimePicker() {
        LocalTime initialTime = selectedStartTime != null ? selectedStartTime : LocalTime.MIDNIGHT;
        showTimePicker(initialTime, selectedTime -> {
            selectedStartTime = selectedTime;
            updateStartTimeDisplay();
            binding.btnEndTime.setEnabled(true);
        });
    }

    private void showEndTimePicker() {
        LocalTime initialTime = selectedEndTime != null ? selectedEndTime : LocalTime.MIDNIGHT;
        showTimePicker(initialTime, selectedTime -> {
            if (isEndTimeValid(selectedTime)) {
                selectedEndTime = selectedTime;
                updateEndTimeDisplay();
            } else {
                Toast.makeText(requireContext(), "End time cannot be set before start time.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isEndTimeValid(LocalTime selectedEndTime) {
        return !selectedEndTime.isBefore(selectedStartTime);
    }

    private void showTimePicker(LocalTime initialTime, Consumer<LocalTime> onTimeSelected) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    LocalTime selectedTime = LocalTime.of(hourOfDay, minute);
                    onTimeSelected.accept(selectedTime);
                },
                initialTime.getHour(),
                initialTime.getMinute(),
                true
        );
        timePickerDialog.show();
    }

    private void updateDateDisplay() {
        if (selectedDate != null) {
            binding.btnDate.setText(selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }
    }

    private void updateStartTimeDisplay() {
        if (selectedStartTime != null) {
            binding.btnStartTime.setText(selectedStartTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        }
    }

    private void updateEndTimeDisplay() {
        if (selectedEndTime != null) {
            binding.btnEndTime.setText(selectedEndTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        }
    }

    private void deleteMeeting() {
        meetingViewModel.delete();
        backToMeetingList();
    }

    private void backToMeetingList() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).openMeetingsListFragment();
        }
    }

    private void saveOrUpdateMeeting() {
        if (!isFormValid()) {
            return;
        }

        meetingViewModel.getMeeting().setTitle(getTextFromEditText(binding.etTitle));
        meetingViewModel.getMeeting().setDescription(getTextFromEditText(binding.etDescription));
        meetingViewModel.getMeeting().setLocation(Location.valueOf(getSelectedFromSpinner(binding.spinnerLocation)));

        meetingViewModel.getMeeting().setMeetingDate(selectedDate);
        meetingViewModel.getMeeting().setStartTime(selectedStartTime);
        meetingViewModel.getMeeting().setEndTime(selectedEndTime);

        if (updateMode) {
            meetingViewModel.update();
        } else {
            meetingViewModel.create();
        }

        backToMeetingList();
    }

    private String getTextFromEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private String getSelectedFromSpinner(Spinner spinner) {
        return spinner.getSelectedItem().toString();
    }

    private boolean isFormValid() {
        if (getTextFromEditText(binding.etTitle).trim().isEmpty()) {
            Toast.makeText(requireContext(), "Title is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Objects.isNull(selectedDate)) {
            Toast.makeText(requireContext(), "Date is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Objects.isNull(selectedStartTime)) {
            Toast.makeText(requireContext(), "Start time is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Objects.isNull(selectedEndTime)) {
            Toast.makeText(requireContext(), "End time is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
