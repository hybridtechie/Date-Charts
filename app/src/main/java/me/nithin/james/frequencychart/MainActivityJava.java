package me.nithin.james.frequencychart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;

import me.nithin.james.FrequencyChart;
import me.nithin.james.StreakChart;
import me.nithin.james.freqchart.SampleDataUtils;
import me.nithin.james.utils.Timestamp;

public class MainActivityJava extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SampleDataUtils sampleDataUtils = new SampleDataUtils();
        List<Timestamp> timestampList = sampleDataUtils.getSampleTimeStampData();
        HashMap<Timestamp, Integer[]> map = sampleDataUtils.getWeekdayFrequency(timestampList);

        FrequencyChart frequencyChart = findViewById(R.id.frequencyChart);
        frequencyChart.populateWithRandomData();
        frequencyChart.setFrequency(map);
        frequencyChart.setGridColor(getColor(R.color.red_500));
        frequencyChart.setTextColor(getColor(R.color.black));
        frequencyChart.setMarkerColor(getColor(R.color.blue_500));

        StreakChart streakChart = findViewById(R.id.streakChart);
        streakChart.refreshDrawableState();
        streakChart.setColor(getColor(R.color.red_500));
        timestampList = sampleDataUtils.getSampleTimeStampDataForStreakChart();
        streakChart.populateWithTimeStampData(timestampList);
    }
}
