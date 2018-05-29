Frequency Chart
===============

[![Build Status](https://travis-ci.com/hybridtechie/FreqChart.svg?branch=master)](https://travis-ci.com/hybridtechie/FreqChart)
[![](https://jitpack.io/v/hybridtechie/FreqChart.svg)](https://jitpack.io/#hybridtechie/FreqChart)


The code is extracted from https://github.com/iSoron/uhabits
All credits goes to https://github.com/iSoron/uhabits

## Screenshots

[![One][screen1th]][screen1]
[![Two][screen2th]][screen2]




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
		implementation 'com.github.hybridtechie:FreqChart:1.0.2'
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
     FreqChartUtils freqChartUtils = new FreqChartUtils();
     List<FrequencyChartTimestamp> frequencyChartTimestampList = freqChartUtils.getSampleTimeStampData();
     HashMap<FrequencyChartTimestamp, Integer[]> map = freqChartUtils.getWeekdayFrequency(frequencyChartTimestampList);
     FrequencyChart frequencyChart = findViewById(R.id.frequencyChart);
     frequencyChart.populateWithRandomData();
     frequencyChart.setFrequency(map);
     frequencyChart.setGridColor(getColor(R.color.red_500));
     frequencyChart.setTextColor(getColor(R.color.black));
     frequencyChart.setMarkerColor(getColor(R.color.blue_500));
```


[screen1]: screenshots/screenshot1.PNG
[screen2]: screenshots/screenshot2.PNG


