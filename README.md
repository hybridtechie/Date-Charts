Frequency Chart
===============

[![Build Status](https://travis-ci.com/hybridtechie/FreqChart.svg?branch=master)](https://travis-ci.com/hybridtechie/FreqChart)


The code is extracted from https://github.com/iSoron/uhabits
All credits goes to https://github.com/iSoron/uhabits

How to use
===============

```java
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
```java 
    dependencies {
		implementation 'com.github.hybridtechie:FreqChart:1.0.1'
	}
```

```xml
 <me.nithin.james.freqchart.FrequencyChart
        android:id="@+id/frequencyChart"
        android:layout_width="match_parent"
        android:layout_height="200dp"/>
```

Sample
===============

```aidl
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
        
```

```aidl
    {
        List<FrequencyChartTimestamp> frequencyChartTimestampList = DummyData.getTimeStampData();
        HashMap<FrequencyChartTimestamp, Integer[]> map = getWeekdayFrequency(frequencyChartTimestampList);

        FrequencyChart frequencyChart = findViewById(R.id.frequencyChart);
        frequencyChart.populateWithRandomData();
        frequencyChart.setFrequency(map);
        frequencyChart.setGridColor(getColor(R.color.red_500));
        frequencyChart.setTextColor(getColor(R.color.black));
        frequencyChart.setMarkerColor(getColor(R.color.blue_500));
    }
        
```




