package me.nithin.james.frequencychart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;

import me.nithin.james.DateRangeChart;
import me.nithin.james.FrequencyDayChart;
import me.nithin.james.HistoryChart;
import me.nithin.james.models.Timestamp;
import me.nithin.james.utils.SampleDataUtils;

public class MainActivityJava extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SampleDataUtils sampleDataUtils = new SampleDataUtils();
        List<Timestamp> timestampList = sampleDataUtils.getSampleTimeStampData();
        HashMap<Timestamp, Integer[]> map = sampleDataUtils.getWeekdayFrequency(timestampList);

        FrequencyDayChart frequencyDayChart = findViewById(R.id.frequencyChart);
        frequencyDayChart.populateWithRandomData();
        frequencyDayChart.setFrequency(map);
        frequencyDayChart.setGridColor(getColor(R.color.red_500));
        frequencyDayChart.setTextColor(getColor(R.color.black));
        frequencyDayChart.setMarkerColor(getColor(R.color.blue_500));

        DateRangeChart dateRangeChart = findViewById(R.id.streakChart);
        dateRangeChart.refreshDrawableState();
        dateRangeChart.setColor(getColor(R.color.red_500));
        dateRangeChart.setLabelColor(getColor(R.color.blue_300));
        dateRangeChart.setValueTextColor(getColor(R.color.black));
        timestampList = sampleDataUtils.getSampleTimeStampDataForStreakChart();
        dateRangeChart.populateWithTimeStampData(timestampList);

        HistoryChart historyChart = findViewById(R.id.historyCard);
        historyChart.setColor(getColor(R.color.blue_500));
        historyChart.setTextColor(getColor(R.color.green_500));
        historyChart.setValueTextColor(getColor(R.color.red_500));
        historyChart.populateWithRandomData();
    }
}
