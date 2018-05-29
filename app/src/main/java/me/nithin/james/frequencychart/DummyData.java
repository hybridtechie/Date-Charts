package me.nithin.james.frequencychart;

import java.util.ArrayList;
import java.util.List;

import me.nithin.james.freqchart.FrequencyChartTimestamp;

/**
 * Created by HyBr!dT3cH!3 on 5/28/2018..
 */
public class DummyData {

    public static List<FrequencyChartTimestamp> getTimeStampData() {

        int[] marks = {
                0, 1, 3, 5, 7, 8, 9, 10, 12, 14, 15, 17, 19, 20, 26, 27, 28, 50, 51,
                52, 53, 54, 58, 60, 63, 65, 70, 71, 72, 73, 74, 75, 80, 81, 83, 89, 90,
                91, 95, 102, 103, 108, 109, 120, 220, 230, 240, 250, 1000, 1001, 1002, 1003, 251, 252
        };
        List<FrequencyChartTimestamp> frequencyChartTimestampList = new ArrayList<>();
        for (int mark : marks) {
            frequencyChartTimestampList.add(new FrequencyChartTimestamp().minus(mark));
        }

        return frequencyChartTimestampList;
    }
}
