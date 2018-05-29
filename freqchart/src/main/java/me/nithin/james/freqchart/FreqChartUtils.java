package me.nithin.james.freqchart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HyBr!dT3cH!3 on 5/29/2018..
 */
public class FreqChartUtils {

    public List<FrequencyChartTimestamp> getSampleTimeStampData() {

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
