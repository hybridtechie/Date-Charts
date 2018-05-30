package me.nithin.james.freqchart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import me.nithin.james.utils.Timestamp;

/**
 * Created by HyBr!dT3cH!3 on 5/29/2018..
 */
public class FreqChartUtils {

    public List<Timestamp> getSampleTimeStampData() {

        int[] marks = {
                0, 1, 3, 5, 7, 8, 9, 10, 12, 14, 15, 17, 19, 20, 26, 27, 28, 50, 51,
                52, 53, 54, 58, 60, 63, 65, 70, 71, 72, 73, 74, 75, 80, 81, 83, 89, 90,
                91, 95, 102, 103, 108, 109, 120, 220, 230, 240, 250, 1000, 1001, 1002, 1003, 251, 252
        };
        List<Timestamp> timestampList = new ArrayList<>();
        for (int mark : marks) {
            timestampList.add(new Timestamp().minus(mark));
        }

        return timestampList;
    }


    public List<Timestamp> getSampleTimeStampDataForStreakChart() {

        int[] marks = {
                0, 1, 3, 5, 7, 8, 9, 10, 12, 14, 15, 17, 19, 20, 26, 27, 28, 50, 51,
                52, 53, 54, 58, 60, 63, 65, 70, 71, 72, 73, 74, 75, 80, 81, 83, 89, 90,
                91, 95, 102, 103, 108, 109, 120, 220, 230, 240, 250, 1000, 1001, 1002, 1003
        };

        List<Timestamp> frequencyChartTimestampList = new ArrayList<>();
        for (int mark : marks) {
            frequencyChartTimestampList.add(new Timestamp().minus(mark));
        }
        Collections.reverse(Arrays.asList(marks));
        return frequencyChartTimestampList;
    }

    public HashMap<Timestamp, Integer[]> getWeekdayFrequency(List<Timestamp> timestampList) {
        HashMap<Timestamp, Integer[]> map = new HashMap<>();
        for (Timestamp r : timestampList) {
            Calendar date = r.toCalendar();
            int weekday = r.getWeekday();
            date.set(Calendar.DAY_OF_MONTH, 1);

            Timestamp timestamp = new Timestamp(date.getTimeInMillis());
            Integer[] list = map.get(timestamp);

            if (list == null) {
                list = new Integer[7];
                Arrays.fill(list, 0);
                map.put(timestamp, list);
            }

            list[weekday]++;
        }
        return map;
    }

}
