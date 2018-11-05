package com.cloudtravel.cloudtravelandroid.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseCallBack;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseFragment;
import com.cloudtravel.cloudtravelandroid.main.activity.AddScheduleActivity;
import com.cloudtravel.cloudtravelandroid.main.activity.RoadMapActivity;
import com.cloudtravel.cloudtravelandroid.main.activity.UpdateScheduleActivity;
import com.cloudtravel.cloudtravelandroid.main.adapter.ScheduleItemAdapter;
import com.cloudtravel.cloudtravelandroid.main.api.DeleteScheduleApi;
import com.cloudtravel.cloudtravelandroid.main.api.GetLocationInfoApi;
import com.cloudtravel.cloudtravelandroid.main.api.GetScheduleApi;
import com.cloudtravel.cloudtravelandroid.main.dto.Location;
import com.cloudtravel.cloudtravelandroid.main.dto.Schedule;
import com.cloudtravel.cloudtravelandroid.main.item.ScheduleItem;
import com.cloudtravel.cloudtravelandroid.main.request.DeleteScheduleRequest;
import com.cloudtravel.cloudtravelandroid.main.request.GetLocationInfoRequest;
import com.cloudtravel.cloudtravelandroid.main.request.GetScheduleRequest;
import com.cloudtravel.cloudtravelandroid.main.util.GsonUtil;
import com.google.gson.reflect.TypeToken;
import com.lemon.support.util.DateUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends CloudTravelBaseFragment {

    private static final String TAG = "ScheduleFragment";

    private List<ScheduleItem> scheduleItemList = new ArrayList<>();
    private FloatingActionButton addScheduleButton;
    private Button roadMapButton;
    private ScheduleItemAdapter scheduleAdapter;
    private Date selectedDate;
    private ImageView selectDateImage;
    private TextView selectedDateText;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        selectedDate = new Date();
        //getSchedule(selectedDate);
        RecyclerView scheduleRecyclerView = view.findViewById(R.id.schedule_recycler_view);
        LinearLayoutManager ScheduleLayoutManager = new LinearLayoutManager(getContext());
        scheduleRecyclerView.setLayoutManager(ScheduleLayoutManager);
        scheduleAdapter = new ScheduleItemAdapter(scheduleItemList);
        scheduleRecyclerView.setAdapter(scheduleAdapter);
        scheduleAdapter.setOnDeleteItemClickListener(new ScheduleItemAdapter.OnDeleteItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ScheduleItem item = scheduleItemList.get(position);
                int scheduleId = item.getScheduleId();
                DeleteScheduleRequest request = new DeleteScheduleRequest();
                request.setScheduleId(scheduleId);
                addRequest(getService(DeleteScheduleApi.class).doDeleteSchedule(request), new CloudTravelBaseCallBack() {
                    @Override
                    public void onSuccess200(Object o) {
                        Toast.makeText(getContext(), "Deleting Schedule Succeeded!", Toast.LENGTH_SHORT);
                    }
                });
                scheduleItemList.remove(position);
                scheduleAdapter.notifyItemRemoved(position);
                scheduleAdapter.notifyItemRangeChanged(position, scheduleItemList.size() - position);
            }
        });
        scheduleAdapter.setOnUpdateItemClickListener(new ScheduleItemAdapter.OnUpdateItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), UpdateScheduleActivity.class);
                ScheduleItem item = scheduleItemList.get(position);
                intent.putExtra("name", item.getPlaceName());
                intent.putExtra("time", item.getTime());
                intent.putExtra("memo", item.getMemo());
                intent.putExtra("id", item.getScheduleId());
                startActivity(intent);
            }
        });

        addScheduleButton = view.findViewById(R.id.fab_add);
        addScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddScheduleActivity.class);
                startActivity(intent);
            }
        });
        roadMapButton = view.findViewById(R.id.road_map_button);
        roadMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RoadMapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list", (ArrayList<ScheduleItem>) scheduleItemList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        selectDateImage = view.findViewById(R.id.select_date_image);
        selectedDateText = view.findViewById(R.id.selected_date_text_view);
        selectedDateText.setText(DateUtil.date2Str(selectedDate, "YYYY-MM-dd"));
        selectDateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                selectedDate = DateUtil.str2Date(DateUtil.date2Str(calendar));
                                //Log.e(TAG,DateUtil.date2Str(selectedDate));
                                selectedDateText.setText(DateUtil.date2Str(calendar, "YYYY-MM-dd"));
                                //getSchedule(selectedDate);
                            }
                        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show(getFragmentManager(), null);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //Get schedules from the database
    private void getSchedule(Date date) {
        GetScheduleRequest request = new GetScheduleRequest();
        request.setDate(DateUtil.date2Str(date));
        addRequest(getService(GetScheduleApi.class).doGetSchedule(request), new CloudTravelBaseCallBack() {
            @Override
            public void onSuccess200(Object o) {
                List<Schedule> dataList = GsonUtil.getEntity(o, new TypeToken<List<Schedule>>() {
                }.getType());
                scheduleItemList.clear();
                for (Schedule schedule : dataList) {
                    final ScheduleItem item = new ScheduleItem();
                    item.setPlaceName(schedule.getLocationName());
                    item.setTime(DateUtil.date2Str(schedule.getDate()));
                    item.setTypeIconId(R.drawable.place_type_icon_restaurant);
                    item.setMemo(schedule.getRemark());
                    item.setScheduleId(schedule.getScheduleId());
                    GetLocationInfoRequest getLocationInfoRequest = new GetLocationInfoRequest();
                    int locationId = schedule.getLocationId();
                    getLocationInfoRequest.setLocationId(locationId);
                    addRequest(getService(GetLocationInfoApi.class).doGetLocationInfo(getLocationInfoRequest), new CloudTravelBaseCallBack() {
                        @Override
                        public void onSuccess200(Object o) {
                            Location location = GsonUtil.getEntity(o, new TypeToken<Location>() {
                            }.getType());
                            item.setLongitude(location.getLongitude());
                            item.setLatitude(location.getLatitude());
                            item.setAddress(location.getAddress());
                        }
                    });
                    scheduleItemList.add(item);
                }
                scheduleAdapter.notifyDataSetChanged();
            }
        });
    }
}
