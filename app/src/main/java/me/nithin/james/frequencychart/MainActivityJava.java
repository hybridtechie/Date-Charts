package me.nithin.james.frequencychart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;

import me.nithin.james.freqchart.FreqChartUtils;
import me.nithin.james.freqchart.FrequencyChart;
import me.nithin.james.freqchart.FrequencyChartTimestamp;

public class MainActivityJava extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FreqChartUtils freqChartUtils = new FreqChartUtils();
        List<FrequencyChartTimestamp> frequencyChartTimestampList = freqChartUtils.getSampleTimeStampData();
        HashMap<FrequencyChartTimestamp, Integer[]> map = freqChartUtils.getWeekdayFrequency(frequencyChartTimestampList);

        FrequencyChart frequencyChart = findViewById(R.id.frequencyChart);
        frequencyChart.populateWithRandomData();
        frequencyChart.setFrequency(map);
        frequencyChart.setGridColor(getColor(R.color.red_500));
        frequencyChart.setTextColor(getColor(R.color.black));
        frequencyChart.setMarkerColor(getColor(R.color.blue_500));
    }
}
