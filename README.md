Frequency Chart
===============

[![Build Status](https://travis-ci.com/hybridtechie/Date-Charts.svg?branch=master)](https://travis-ci.com/hybridtechie/Date-Charts)
[![](https://jitpack.io/v/hybridtechie/FreqChart.svg)](https://jitpack.io/#hybridtechie/FreqChart)


The code is extracted from https://github.com/iSoron/uhabits

All credits goes to https://github.com/iSoron/uhabits


Demo in Kotlin : https://github.com/hybridtechie/DateChartsDemo

## Screenshots

[![One][screen1]][screen1]
[![Two][screen2]][screen2]
[![One][screen3]][screen3]
[![Two][screen4]][screen4]




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
	     implementation 'com.github.hybridtechie:Date-Charts:1.0.6'
	}
```

```xml
 <me.nithin.james.FrequencyDayChart
        android:id="@+id/frequencyChart"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        tools:layout_editor_absoluteX="0dp"
        tools:layout_editor_absoluteY="0dp"/>
        
        
 <me.nithin.james.DateRangeChart
        android:id="@+id/streakChart"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        tools:layout_editor_absoluteX="0dp"
        tools:layout_editor_absoluteY="230dp"/>
        
 <me.nithin.james.HistoryChart
         android:id="@+id/historyCard"
         android:layout_width="match_parent"
         android:layout_height="160dp"
         android:gravity="center"
         android:paddingBottom="0dp"/>

```

Sample
===============

```aidl    
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
```


[screen1]: screenshots/screenshot1.PNG
[screen2]: screenshots/screenshot2.PNG
[screen3]: screenshots/screenshot3.PNG
[screen4]: screenshots/screenshot4.PNG


