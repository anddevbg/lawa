package com.anddevbg.lawa.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anddevbg.lawa.LawaApplication;
import com.anddevbg.lawa.R;
import com.anddevbg.lawa.model.ForecastData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by adri.stanchev on 22/07/2015.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastViewHolder> implements InterfaceTouchHelper {

    private LayoutInflater inflater;
    List<ForecastData> forecastDataList = Collections.emptyList();
    private final OnStartDragListener mDragStartListener;

    public ForecastAdapter(Context context, OnStartDragListener listener) {
        inflater = LayoutInflater.from(context);
        mDragStartListener = listener;
        forecastDataList = new ArrayList<>();
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.forecast_list_item_layout, parent, false);
        ForecastViewHolder viewHolder = new ForecastViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        ForecastData forecastObject = forecastDataList.get(position);
        holder.dayOfWeek.setText(forecastObject.getmDay());
        holder.minimalTemperature.setText(String.valueOf(forecastObject.getMinimalTemperature() + "ºC"));
        holder.maximalTemperature.setText(String.valueOf(forecastObject.getMaximalTemperature() + "ºC"));
        holder.descriptionText.setText(String.valueOf(forecastObject.getmDescription()));
        holder.cityNameTextView.setText(forecastObject.getmCityName());
        Picasso.with(LawaApplication.getContext()).load(forecastObject.getImageUrl()).into(holder.weatherIcon);
    }

    @Override
    public int getItemCount() {
            return forecastDataList.size();
    }

    public void setData(List<ForecastData> data) {
        forecastDataList = data;
        notifyDataSetChanged();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(forecastDataList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        forecastDataList.remove(position);
        notifyItemRemoved(position);
    }
}

class ForecastViewHolder extends RecyclerView.ViewHolder {

    TextView dayOfWeek;
    TextView minimalTemperature;
    TextView maximalTemperature;
    ImageView weatherIcon;
    TextView slashText;
    TextView descriptionText;
    TextView cityNameTextView;


    public ForecastViewHolder(View itemView) {
        super(itemView);

        dayOfWeek = (TextView) itemView.findViewById(R.id.day_text_view);
        minimalTemperature = (TextView) itemView.findViewById(R.id.min_temperature_text_view);
        maximalTemperature = (TextView) itemView.findViewById(R.id.max_temperature_text_view);
        weatherIcon = (ImageView) itemView.findViewById(R.id.weather_Icon);
        slashText = (TextView) itemView.findViewById(R.id.slash_text_view);
        descriptionText = (TextView) itemView.findViewById(R.id.description_text_view);
        cityNameTextView = (TextView) itemView.findViewById(R.id.city_name_text_view);

    }
    public void onItemClear() {

    }

}


