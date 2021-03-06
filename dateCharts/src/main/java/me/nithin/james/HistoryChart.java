package me.nithin.james;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import me.nithin.james.freqchart.R;
import me.nithin.james.models.ScrollableChart;
import me.nithin.james.models.Timestamp;
import me.nithin.james.utils.AndroidDateFormats;
import me.nithin.james.utils.DateUtils;

import static me.nithin.james.utils.InterfaceUtils.dpToPixels;
import static me.nithin.james.utils.InterfaceUtils.getDimension;

public class HistoryChart extends ScrollableChart {
    private int[] checkmarks;

    private int target;

    private Paint pSquareBg, pSquareFg, pTextHeader;

    private float squareSpacing;

    private float squareTextOffset;

    private float headerTextOffset;

    private float columnWidth;

    private float columnHeight;

    private int nColumns;

    private SimpleDateFormat dfMonth;

    private SimpleDateFormat dfYear;

    private Calendar baseDate;

    private int nDays;

    /**
     * 0-based-position of today in the column
     */
    private int todayPositionInColumn;

    private int colors[];

    private RectF baseLocation;

    private int primaryColor;

    private boolean isBackgroundTransparent;

    private int textColor;

    private int valueColor;

    private int reverseTextColor;

    private boolean isEditable;

    private String previousMonth;

    private String previousYear;

    private float headerOverflow = 0;

    private boolean isNumerical = false;

    public HistoryChart(Context context) {
        super(context);
        init();
    }

    public HistoryChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void populateWithRandomData() {
        Random random = new Random();
        checkmarks = new int[100];

        for (int i = 0; i < 100; i++) {
            if (random.nextFloat() < 0.3) checkmarks[i] = 2;
        }

        for (int i = 0; i < 100 - 7; i++) {
            int count = 0;
            for (int j = 0; j < 7; j++) {
                if (checkmarks[i + j] != 0) count++;
            }

            if (count >= 3) checkmarks[i] = Math.max(checkmarks[i], 1);
        }
    }

    public void setCheckmarks(int[] checkmarks) {
        this.checkmarks = checkmarks;
        postInvalidate();
    }

    public void setColor(int color) {
        this.primaryColor = color;
        initColors();
        postInvalidate();
    }

    public void setTextColor(int color) {
        this.textColor = color;
        initColors();
        postInvalidate();
    }

    public void setValueTextColor(int color) {
        this.valueColor = color;
        initColors();
        postInvalidate();
    }

    public void setNumerical(boolean numerical) {
        isNumerical = numerical;
    }

    public void setIsBackgroundTransparent(boolean isBackgroundTransparent) {
        this.isBackgroundTransparent = isBackgroundTransparent;
        initColors();
    }

    public void setIsEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public void setTarget(int target) {
        this.target = target;
        postInvalidate();
    }

    protected void initPaints() {
        pTextHeader = new Paint();
        pTextHeader.setTextAlign(Align.LEFT);
        pTextHeader.setAntiAlias(true);

        pSquareBg = new Paint();

        pSquareFg = new Paint();
        pSquareFg.setAntiAlias(true);
        pSquareFg.setTextAlign(Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        baseLocation.set(0, 0, columnWidth - squareSpacing,
                columnWidth - squareSpacing);
        baseLocation.offset(getPaddingLeft(), getPaddingTop());

        headerOverflow = 0;
        previousMonth = "";
        previousYear = "";
        pTextHeader.setColor(textColor);

        updateDate();
        GregorianCalendar currentDate = (GregorianCalendar) baseDate.clone();

        for (int column = 0; column < nColumns - 1; column++) {
            drawColumn(canvas, baseLocation, currentDate, column);
            baseLocation.offset(columnWidth, -columnHeight);
        }

        drawAxis(canvas, baseLocation);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int width,
                                 int height,
                                 int oldWidth,
                                 int oldHeight) {
        if (height < 8) height = 200;
        float baseSize = height / 8.0f;
        setScrollerBucketSize((int) baseSize);

        squareSpacing = dpToPixels(getContext(), 1.0f);
        float maxTextSize = getDimension(getContext(), R.dimen.regularTextSize);
        float textSize = height * 0.06f;
        textSize = Math.min(textSize, maxTextSize);

        pSquareFg.setTextSize(textSize);
        pTextHeader.setTextSize(textSize);
        squareTextOffset = pSquareFg.getFontSpacing() * 0.4f;
        headerTextOffset = pTextHeader.getFontSpacing() * 0.3f;

        float rightLabelWidth = getWeekdayLabelWidth() + headerTextOffset;
        float horizontalPadding = getPaddingRight() + getPaddingLeft();

        columnWidth = baseSize;
        columnHeight = 8 * baseSize;
        nColumns =
                (int) ((width - rightLabelWidth - horizontalPadding) / baseSize) +
                        1;

        updateDate();
    }

    private void drawAxis(Canvas canvas, RectF location) {
        float verticalOffset = pTextHeader.getFontSpacing() * 0.4f;

        for (String day : DateUtils.getLocaleDayNames(Calendar.SHORT)) {
            location.offset(0, columnWidth);
            canvas.drawText(day, location.left + headerTextOffset,
                    location.centerY() + verticalOffset, pTextHeader);
        }
    }

    private void drawColumn(Canvas canvas,
                            RectF location,
                            GregorianCalendar date,
                            int column) {
        drawColumnHeader(canvas, location, date);
        location.offset(0, columnWidth);

        for (int j = 0; j < 7; j++) {
            if (!(column == nColumns - 2 && getDataOffset() == 0 &&
                    j > todayPositionInColumn)) {
                int checkmarkOffset =
                        getDataOffset() * 7 + nDays - 7 * (column + 1) +
                                todayPositionInColumn - j;
                drawSquare(canvas, location, date, checkmarkOffset);
            }

            date.add(Calendar.DAY_OF_MONTH, 1);
            location.offset(0, columnWidth);
        }
    }

    private void drawColumnHeader(Canvas canvas,
                                  RectF location,
                                  GregorianCalendar date) {
        String month = dfMonth.format(date.getTime());
        String year = dfYear.format(date.getTime());

        String text = null;
        if (!month.equals(previousMonth)) text = previousMonth = month;
        else if (!year.equals(previousYear)) text = previousYear = year;

        if (text != null) {
            canvas.drawText(text, location.left + headerOverflow,
                    location.bottom - headerTextOffset, pTextHeader);
            headerOverflow +=
                    pTextHeader.measureText(text) + columnWidth * 0.2f;
        }

        headerOverflow = Math.max(0, headerOverflow - columnWidth);
    }

    private void drawSquare(Canvas canvas,
                            RectF location,
                            GregorianCalendar date,
                            int checkmarkOffset) {
        if (checkmarkOffset >= checkmarks.length) pSquareBg.setColor(colors[0]);
        else {
            int checkmark = checkmarks[checkmarkOffset];
            if (checkmark == 0) pSquareBg.setColor(colors[0]);
            else if (checkmark < target) {
                pSquareBg.setColor(isNumerical ? textColor : colors[1]);
            } else pSquareBg.setColor(colors[2]);
        }

        pSquareFg.setColor(reverseTextColor);
        canvas.drawRect(location, pSquareBg);
        String text = Integer.toString(date.get(Calendar.DAY_OF_MONTH));
        canvas.drawText(text, location.centerX(),
                location.centerY() + squareTextOffset, pSquareFg);
    }

    private float getWeekdayLabelWidth() {
        float width = 0;

        for (String w : DateUtils.getLocaleDayNames(Calendar.SHORT)) {
            width = Math.max(width, pSquareFg.measureText(w));
        }

        return width;
    }

    private void init() {
        isEditable = false;
        checkmarks = new int[0];
        target = 2;

        initColors();
        initPaints();
        initDateFormats();
        initRects();
    }

    private void initColors() {

        if (isBackgroundTransparent)
            primaryColor = setMinValue(primaryColor, 0.75f);

        int red = Color.red(primaryColor);
        int green = Color.green(primaryColor);
        int blue = Color.blue(primaryColor);

        if (isBackgroundTransparent) {
            colors = new int[3];
            colors[0] = Color.argb(16, 255, 255, 255);
            colors[1] = Color.argb(128, red, green, blue);
            colors[2] = primaryColor;
            textColor = Color.WHITE;
            reverseTextColor = Color.WHITE;
        } else {
            colors = new int[3];
            colors[0] = ContextCompat.getColor(getContext(), R.color.grey_100);
            colors[1] = Color.argb(127, red, green, blue);
            colors[2] = primaryColor;
            textColor = textColor;
            reverseTextColor =
                    valueColor;
        }
    }

    public static int setMinValue(int color, float newValue) {
        float hsv[] = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = Math.max(hsv[2], newValue);
        return Color.HSVToColor(hsv);
    }

    private void initDateFormats() {
        dfMonth = AndroidDateFormats.fromSkeleton("MMM");
        dfYear = AndroidDateFormats.fromSkeleton("yyyy");
    }

    private void initRects() {
        baseLocation = new RectF();
    }

    @Nullable
    private Timestamp positionToTimestamp(float x, float y) {
        int col = (int) (x / columnWidth);
        int row = (int) (y / columnWidth);

        if (row == 0) return null;
        if (col == nColumns - 1) return null;

        int offset = col * 7 + (row - 1);
        Calendar date = (Calendar) baseDate.clone();
        date.add(Calendar.DAY_OF_YEAR, offset);

        if (DateUtils.getStartOfDay(date.getTimeInMillis()) >
                DateUtils.getStartOfToday()) return null;

        return new Timestamp(date.getTimeInMillis());
    }

    private void updateDate() {
        baseDate = DateUtils.getStartOfTodayCalendar();
        baseDate.add(Calendar.DAY_OF_YEAR, -(getDataOffset() - 1) * 7);

        nDays = (nColumns - 1) * 7;
        int realWeekday =
                DateUtils.getStartOfTodayCalendar().get(Calendar.DAY_OF_WEEK);
        todayPositionInColumn =
                (7 + realWeekday - baseDate.getFirstDayOfWeek()) % 7;

        baseDate.add(Calendar.DAY_OF_YEAR, -nDays);
        baseDate.add(Calendar.DAY_OF_YEAR, -todayPositionInColumn);
    }
}
