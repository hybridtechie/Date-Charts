package me.nithin.james.frequencychart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import me.nithin.james.freqchart.FrequencyChart;
import me.nithin.james.freqchart.FrequencyChartTimestamp;

public class MainActivityJava extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<FrequencyChartTimestamp> frequencyChartTimestampList = DummyData.getTimeStampData();
        HashMap<FrequencyChartTimestamp, Integer[]> map = getWeekdayFrequency(frequencyChartTimestampList);

        FrequencyChart frequencyChart = findViewById(R.id.frequencyChart);
        frequencyChart.populateWithRandomData();
        frequencyChart.setFrequency(map);
        frequencyChart.setGridColor(getColor(R.color.red_500));
        frequencyChart.setTextColor(getColor(R.color.black));
        frequencyChart.setMarkerColor(getColor(R.color.blue_500));
    }

    public HashMap<FrequencyChartTimestamp, Integer[]> getWeekdayFrequency(List<FrequencyChartTimestamp> frequencyChartTimestampList) {
        HashMap<FrequencyChartTimestamp, Integer[]> map = new HashMap<>();
        for (FrequencyChartTimestamp r : frequencyChartTimestampList) {
            Calendar date = r.toCalendar();
            int weekday = r.getWeekday();
            date.set(Calendar.DAY_OF_MONTH, 1);

            FrequencyChartTimestamp frequencyChartTimestamp = new FrequencyChartTimestamp(date.getTimeInMillis());
            Integer[] list = map.get(frequencyChartTimestamp);

            if (list == null) {
                list = new Integer[7];
                Arrays.fill(list, 0);
                map.put(frequencyChartTimestamp, list);
            }

            list[weekday]++;
        }
        return map;
    }
}
