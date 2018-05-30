package me.nithin.james;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;

import me.nithin.james.utils.Timestamp;
import me.nithin.james.freqchart.R;
import me.nithin.james.utils.AndroidDateFormats;
import me.nithin.james.utils.DateUtils;
import me.nithin.james.utils.ScrollableChart;

public class FrequencyChart extends ScrollableChart {
    private Paint pGrid;

    private float em;

    private SimpleDateFormat dfMonth;

    private SimpleDateFormat dfYear;

    private Paint pText, pGraph;

    private RectF rect, prevRect;

    private int baseSize;

    private int paddingTop;

    private float columnWidth;

    private int columnHeight;

    private int nColumns;

    private int textColor;

    private int gridColor;

    private int markerColor;

    private boolean isBackgroundTransparent;

    @NonNull
    private HashMap<Timestamp, Integer[]> frequency;
    private int maxFreq;

    public FrequencyChart(Context context) {
        super(context);
        init();
    }

    private void init() {
        initPaints();
        initColors();
        initDateFormats();
        initRects();
    }

    protected void initPaints() {
        pText = new Paint();
        pText.setAntiAlias(true);

        pGraph = new Paint();
        pGraph.setTextAlign(Paint.Align.CENTER);
        pGraph.setAntiAlias(true);

        pGrid = new Paint();
        pGrid.setAntiAlias(true);
    }

    private void initColors() {

        if (textColor == 0) textColor = ContextCompat.getColor(getContext(), R.color.green_500);
        if (gridColor == 0) gridColor = ContextCompat.getColor(getContext(), R.color.blue_500);
        if (markerColor == 0)
            markerColor = ContextCompat.getColor(getContext(), R.color.deep_orange_A700);
    }

    private void initDateFormats() {
        dfMonth = AndroidDateFormats.fromSkeleton("MMM");
        dfYear = AndroidDateFormats.fromSkeleton("yyyy");
    }

    private void initRects() {
        rect = new RectF();
        prevRect = new RectF();
    }

    public FrequencyChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.frequency = new HashMap<>();
        init();
    }

    public void setTextColor(int color) {
        this.textColor = color;
        postInvalidate();
    }

    public void setMarkerColor(int color) {
        this.markerColor = color;
        postInvalidate();
    }

    public void setGridColor(int color) {
        this.gridColor = color;
        postInvalidate();
    }

    public void setFrequency(HashMap<Timestamp, Integer[]> frequency) {
        this.frequency = frequency;
        maxFreq = getMaxFreq(frequency);
        postInvalidate();
    }

    private int getMaxFreq(HashMap<Timestamp, Integer[]> frequency) {
        int maxValue = 1;

        for (Integer[] values : frequency.values()) {
            for (Integer value : values) {
                maxValue = Math.max(value, maxValue);
            }
        }

        return maxValue;
    }

    public void setIsBackgroundTransparent(boolean isBackgroundTransparent) {
        this.isBackgroundTransparent = isBackgroundTransparent;
        initColors();
    }

    @Override
    protected void onSizeChanged(int width,
                                 int height,
                                 int oldWidth,
                                 int oldHeight) {
        if (height < 9) height = 200;

        baseSize = height / 8;
        setScrollerBucketSize(baseSize);

        pText.setTextSize(baseSize * 0.4f);
        pGraph.setTextSize(baseSize * 0.4f);
        pGraph.setStrokeWidth(baseSize * 0.1f);
        pGrid.setStrokeWidth(baseSize * 0.05f);
        em = pText.getFontSpacing();

        columnWidth = baseSize;
        columnWidth = Math.max(columnWidth, getMaxMonthWidth() * 1.2f);

        columnHeight = 8 * baseSize;
        nColumns = (int) (width / columnWidth);
        paddingTop = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rect.set(0, 0, nColumns * columnWidth, columnHeight);
        rect.offset(0, paddingTop);

        drawGrid(canvas, rect);

        pText.setTextAlign(Paint.Align.CENTER);
        pText.setColor(textColor);
        pGraph.setColor(markerColor);
        prevRect.setEmpty();

        GregorianCalendar currentDate = DateUtils.getStartOfTodayCalendar();

        currentDate.set(Calendar.DAY_OF_MONTH, 1);
        currentDate.add(Calendar.MONTH, -nColumns + 2 - getDataOffset());

        for (int i = 0; i < nColumns - 1; i++) {
            rect.set(0, 0, columnWidth, columnHeight);
            rect.offset(i * columnWidth, 0);

            drawColumn(canvas, rect, currentDate);
            currentDate.add(Calendar.MONTH, 1);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private void drawGrid(Canvas canvas, RectF rGrid) {
        int nRows = 7;
        float rowHeight = rGrid.height() / (nRows + 1);

        pText.setTextAlign(Paint.Align.LEFT);
        pText.setColor(textColor);
        pGrid.setColor(gridColor);

        for (String day : DateUtils.getLocaleDayNames(Calendar.SHORT)) {
            canvas.drawText(day, rGrid.right - columnWidth,
                    rGrid.top + rowHeight / 2 + 0.25f * em, pText);

            pGrid.setStrokeWidth(1f);
            canvas.drawLine(rGrid.left, rGrid.top, rGrid.right, rGrid.top,
                    pGrid);

            rGrid.offset(0, rowHeight);
        }

        canvas.drawLine(rGrid.left, rGrid.top, rGrid.right, rGrid.top, pGrid);
    }

    private void drawColumn(Canvas canvas, RectF rect, GregorianCalendar date) {
        Integer values[] = frequency.get(new Timestamp(date));
        float rowHeight = rect.height() / 8.0f;
        prevRect.set(rect);

        Integer[] localeWeekdayList = DateUtils.getLocaleWeekdayList();
        for (int j = 0; j < localeWeekdayList.length; j++) {
            rect.set(0, 0, baseSize, baseSize);
            rect.offset(prevRect.left, prevRect.top + baseSize * j);

            int i = localeWeekdayList[j] % 7;
            if (values != null) drawMarker(canvas, rect, values[i]);

            rect.offset(0, rowHeight);
        }

        drawFooter(canvas, rect, date);
    }

    private void drawMarker(Canvas canvas, RectF rect, Integer value) {
        float padding = rect.height() * 0.2f;
        // maximal allowed mark radius
        float maxRadius = (rect.height() - 2 * padding) / 2.0f;
        // the real mark radius is scaled down by a factor depending on the maximal frequency
        float scale = 1.0f / maxFreq * value;
        float radius = maxRadius * scale;

        pGraph.setColor(markerColor);
        canvas.drawCircle(rect.centerX(), rect.centerY(), radius, pGraph);
    }

    private void drawFooter(Canvas canvas, RectF rect, GregorianCalendar date) {
        Date time = date.getTime();

        canvas.drawText(dfMonth.format(time), rect.centerX(),
                rect.centerY() - 0.1f * em, pText);

        if (date.get(Calendar.MONTH) == 1)
            canvas.drawText(dfYear.format(time), rect.centerX(),
                    rect.centerY() + 0.9f * em, pText);
    }

    private float getMaxMonthWidth() {
        float maxMonthWidth = 0;
        GregorianCalendar day = DateUtils.getStartOfTodayCalendar();

        for (int i = 0; i < 12; i++) {
            day.set(Calendar.MONTH, i);
            float monthWidth = pText.measureText(dfMonth.format(day.getTime()));
            maxMonthWidth = Math.max(maxMonthWidth, monthWidth);
        }

        return maxMonthWidth;
    }

    public void populateWithRandomData() {
        GregorianCalendar date = DateUtils.getStartOfTodayCalendar();
        date.set(Calendar.DAY_OF_MONTH, 1);
        Random rand = new Random();
        frequency.clear();

        for (int i = 0; i < 40; i++) {
            Integer values[] = new Integer[7];
            for (int j = 0; j < 7; j++) {
                values[j] = rand.nextInt(5);
            }

            frequency.put(new Timestamp(date), values);
            date.add(Calendar.MONTH, -1);
        }
    }
}
