package com.cloudtravel.cloudtravelandroid.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseFragment;
import com.cloudtravel.cloudtravelandroid.main.activity.AddScheduleActivity;
import com.cloudtravel.cloudtravelandroid.main.activity.RoadMapActivity;
import com.cloudtravel.cloudtravelandroid.main.activity.UpdateScheduleActivity;
import com.cloudtravel.cloudtravelandroid.main.adapter.ScheduleItemAdapter;
import com.cloudtravel.cloudtravelandroid.main.dto.BaseResponse;
import com.cloudtravel.cloudtravelandroid.main.dto.ScheduleDTO;
import com.cloudtravel.cloudtravelandroid.main.item.ScheduleItem;
import com.cloudtravel.cloudtravelandroid.main.service.CloudTravelService;
import com.lemon.support.util.DateUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleFragment extends CloudTravelBaseFragment {

    private static final String TAG = "ScheduleFragment";

    private List<ScheduleItem> scheduleItemList = new ArrayList<>();
    private FloatingActionButton addScheduleButton;
    private Button roadMapButton;
    private ScheduleItemAdapter scheduleAdapter;
    private Date selectedTime;
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
        selectedTime = new Date();
        getSchedule();
        RecyclerView scheduleRecyclerView = view.findViewById(R.id.schedule_recycler_view);
        LinearLayoutManager ScheduleLayoutManager = new LinearLayoutManager(getContext());
        scheduleRecyclerView.setLayoutManager(ScheduleLayoutManager);
        scheduleAdapter = new ScheduleItemAdapter(scheduleItemList);
        scheduleRecyclerView.setAdapter(scheduleAdapter);
        scheduleAdapter.setOnDeleteItemClickListener(new ScheduleItemAdapter.OnDeleteItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                deleteSchedule(position);
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
        selectedDateText.setText(DateUtil.date2Str(selectedTime, "YYYY-MM-dd"));
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
                                selectedTime = DateUtil.str2Date(DateUtil.date2Str(calendar));
                                selectedDateText.setText(DateUtil.date2Str(calendar, "YYYY-MM-dd"));
                                getSchedule();
                            }
                        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show(getFragmentManager(), null);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        getSchedule();
        super.onResume();
    }

    private void getSchedule() {
        String time = DateUtil.date2Str(selectedTime, DateUtil.FORMAT_YMDHMS);
        Call<BaseResponse<List<ScheduleDTO>>> call = CloudTravelService.getInstance()
                .getSchedule(time);
        call.enqueue(new Callback<BaseResponse<List<ScheduleDTO>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ScheduleDTO>>> call, Response<BaseResponse<List<ScheduleDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        List<ScheduleDTO> scheduleDTOList = response.body().getObject();
                        scheduleItemList.clear();
                        for (ScheduleDTO scheduleDTO : scheduleDTOList) {
                            final ScheduleItem item = new ScheduleItem();
                            item.setPlaceName(scheduleDTO.getLocationName());
                            item.setTime(DateUtil.date2Str(scheduleDTO.getTime()));
                            item.setTypeIconId(R.drawable.place_type_icon_restaurant);
                            item.setMemo(scheduleDTO.getMemo());
                            item.setScheduleId(scheduleDTO.getId());
                            item.setLatitude(scheduleDTO.getLatitude());
                            item.setLongitude(scheduleDTO.getLongitude());
                            scheduleItemList.add(item);
                        }
                        scheduleAdapter.notifyDataSetChanged();
                        if (scheduleItemList.size() == 0) {
                            roadMapButton.setVisibility(View.GONE);
                        } else {
                            roadMapButton.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "未知错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<ScheduleDTO>>> call, Throwable t) {
                Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSchedule(final int position) {
        ScheduleItem item = scheduleItemList.get(position);
        int scheduleId = item.getScheduleId();
        Call<BaseResponse> call = CloudTravelService.getInstance().deleteSchedule(scheduleId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                        scheduleItemList.remove(position);
                        scheduleAdapter.notifyItemRemoved(position);
                        scheduleAdapter.notifyItemRangeChanged(position, scheduleItemList.size() - position);
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "未知错误",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
