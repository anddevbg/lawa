package com.anddevbg.lawa.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.model.ForecastData;

import java.util.Collections;
import java.util.List;


/**
 * Created by adri.stanchev on 22/07/2015.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastViewHolder> {


    private LayoutInflater inflater;
    List<ForecastData> forecastDataList = Collections.emptyList();

    public ForecastAdapter(Context context, List<ForecastData> data) {
        inflater = LayoutInflater.from(context);
        this.forecastDataList = data ;
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
        holder.minimalTemperature.setText(String.valueOf(forecastObject.getmMinimalTemperature()));
        holder.maximalTemperature.setText(String.valueOf(forecastObject.getmMaximalTemperature()));
        holder.weatherIcon.setImageResource(R.drawable.cloudsasdas);
    }

    @Override
    public int getItemCount() {
            return forecastDataList.size();
    }
}

class ForecastViewHolder extends RecyclerView.ViewHolder {

    TextView dayOfWeek;
    TextView minimalTemperature;
    TextView maximalTemperature;
    ImageView weatherIcon;
    TextView slashText;

    public ForecastViewHolder(View itemView) {
        super(itemView);

        dayOfWeek = (TextView) itemView.findViewById(R.id.day_text_view);
        minimalTemperature = (TextView) itemView.findViewById(R.id.min_temperature_text_view);
        maximalTemperature = (TextView) itemView.findViewById(R.id.max_temperature_text_view);
        weatherIcon = (ImageView) itemView.findViewById(R.id.weather_Icon);
        slashText = (TextView) itemView.findViewById(R.id.slash_text_view);
    }
}


