/*
 * Copyright (C) 2016 Álinson Santos Xavier <isoron@gmail.com>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.nithin.james;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import java.text.DateFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import me.nithin.james.freqchart.R;
import me.nithin.james.models.DateRange;
import me.nithin.james.models.Timestamp;
import me.nithin.james.utils.DateUtils;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.getSize;
import static android.view.View.MeasureSpec.makeMeasureSpec;
import static me.nithin.james.utils.InterfaceUtils.getDimension;

public class DateRangeChart extends View {
    private Paint paint;

    private long minLength;

    private long maxLength;

    private int[] colors;

    private RectF rect;

    private int baseSize;

    private int primaryColor;

    private List<DateRange> dateRanges;

    private boolean isBackgroundTransparent;

    private DateFormat dateFormat;

    private int width;

    private float em;

    private float maxLabelWidth;

    private float textMargin;

    private boolean shouldShowLabels;

    private int textColor;

    private int valueTextColor;

    public DateRangeChart(Context context) {
        super(context);
        init();
    }

    public DateRangeChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Returns the maximum number of dateRanges this view is able to show, given
     * its current size.
     *
     * @return max number of visible dateRanges
     */
    public int getMaxStreakCount() {
        return (int) Math.floor(getMeasuredHeight() / baseSize);
    }

    public void populateWithRandomData() {
        Timestamp start = DateUtils.getToday();
        LinkedList<DateRange> dateRanges = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            int length = new Random().nextInt(100);
            Timestamp end = start.plus(length);
            dateRanges.add(new DateRange(start, end));
            start = end.plus(1);
        }

        setDateRanges(dateRanges);
    }

    public void populateWithTimeStampData(List<Timestamp> timestampList) {
        int count = 0;

        LinkedList<DateRange> dateRanges = new LinkedList<>();
        int length = timestampList.size() < 10 ? timestampList.size() : 10;

        for (int i = 0; i < length; i++) {
            Timestamp end = timestampList.get(count++);
            Timestamp start = timestampList.get(count++);
            dateRanges.add(new DateRange(start, end));
        }

        setDateRanges(dateRanges);
    }

    public void setColor(int color) {
        this.primaryColor = color;
        int red = Color.red(primaryColor);
        int green = Color.green(primaryColor);
        int blue = Color.blue(primaryColor);

        colors = new int[4];
        colors[3] = primaryColor;
        colors[2] = Color.argb(192, red, green, blue);
        colors[1] = Color.argb(96, red, green, blue);
        colors[0] = Color.argb(40, red, green, blue);
        postInvalidate();
    }

    public void setLabelColor(int color) {
        this.textColor = color;
        postInvalidate();
    }

    public void setValueTextColor(int color) {
        this.valueTextColor = color;
        postInvalidate();
    }

    public void setIsBackgroundTransparent(boolean isBackgroundTransparent) {
        this.isBackgroundTransparent = isBackgroundTransparent;
        initColors();
    }

    public void setDateRanges(List<DateRange> dateRanges) {
        this.dateRanges = dateRanges;
        initColors();
        updateMaxMinLengths();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dateRanges.size() == 0) return;

        rect.set(0, 0, width, baseSize);

        for (DateRange s : dateRanges) {
            drawRow(canvas, s, rect);
            rect.offset(0, baseSize);
        }
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        LayoutParams params = getLayoutParams();

        if (params != null && params.height == LayoutParams.WRAP_CONTENT) {
            int width = getSize(widthSpec);
            int height = dateRanges.size() * baseSize;

            heightSpec = makeMeasureSpec(height, EXACTLY);
            widthSpec = makeMeasureSpec(width, EXACTLY);
        }

        setMeasuredDimension(widthSpec, heightSpec);
    }

    @Override
    protected void onSizeChanged(int width,
                                 int height,
                                 int oldWidth,
                                 int oldHeight) {
        this.width = width;

        Context context = getContext();
        float minTextSize = getDimension(context, R.dimen.tinyTextSize);
        float maxTextSize = getDimension(context, R.dimen.regularTextSize);
        float textSize = baseSize * 0.5f;

        paint.setTextSize(
                Math.max(Math.min(textSize, maxTextSize), minTextSize));
        em = paint.getFontSpacing();
        textMargin = 0.5f * em;

        updateMaxMinLengths();
    }

    private void drawRow(Canvas canvas, DateRange dateRange, RectF rect) {
        if (maxLength == 0) return;

        float percentage = (float) dateRange.getLength() / maxLength;
        float availableWidth = width - 2 * maxLabelWidth;
        if (shouldShowLabels) availableWidth -= 2 * textMargin;

        float barWidth = percentage * availableWidth;
        float minBarWidth =
                paint.measureText(Long.toString(dateRange.getLength())) + em;
        barWidth = Math.max(barWidth, minBarWidth);

        float gap = (width - barWidth) / 2;
        float paddingTopBottom = baseSize * 0.05f;

        paint.setColor(percentageToColor(percentage));

        canvas.drawRect(rect.left + gap, rect.top + paddingTopBottom,
                rect.right - gap, rect.bottom - paddingTopBottom, paint);

        float yOffset = rect.centerY() + 0.3f * em;

        paint.setColor(valueTextColor);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(Long.toString(dateRange.getLength()), rect.centerX(),
                yOffset, paint);

        if (shouldShowLabels) {
            String startLabel = dateFormat.format(dateRange.getStart().toJavaDate());
            String endLabel = dateFormat.format(dateRange.getEnd().toJavaDate());

            paint.setColor(textColor);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(startLabel, gap - textMargin, yOffset, paint);

            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(endLabel, width - gap + textMargin, yOffset, paint);
        }
    }

    private void init() {
        initPaints();
        initColors();

        dateRanges = Collections.emptyList();

        dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        rect = new RectF();
        baseSize = getResources().getDimensionPixelSize(R.dimen.baseSize);
    }

    private void initColors() {

        if (primaryColor == 0)
            primaryColor = ContextCompat.getColor(getContext(), R.color.green_500);
        if (textColor == 0) textColor = ContextCompat.getColor(getContext(), R.color.blue_500);
        if (valueTextColor == 0)
            valueTextColor = ContextCompat.getColor(getContext(), R.color.green_500);

        int red = Color.red(primaryColor);
        int green = Color.green(primaryColor);
        int blue = Color.blue(primaryColor);

        colors = new int[4];
        colors[3] = primaryColor;
        colors[2] = Color.argb(192, red, green, blue);
        colors[1] = Color.argb(96, red, green, blue);
        colors[0] = Color.argb(40, red, green, blue);
    }

    private void initPaints() {
        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
    }

    private int percentageToColor(float percentage) {
        if (percentage >= 1.0f) return colors[3];
        if (percentage >= 0.8f) return colors[2];
        if (percentage >= 0.5f) return colors[1];
        return colors[0];
    }

    private void updateMaxMinLengths() {
        maxLength = 0;
        minLength = Long.MAX_VALUE;
        shouldShowLabels = true;

        for (DateRange s : dateRanges) {
            maxLength = Math.max(maxLength, s.getLength());
            minLength = Math.min(minLength, s.getLength());

            float lw1 =
                    paint.measureText(dateFormat.format(s.getStart().toJavaDate()));
            float lw2 =
                    paint.measureText(dateFormat.format(s.getEnd().toJavaDate()));
            maxLabelWidth = Math.max(maxLabelWidth, Math.max(lw1, lw2));
        }

        if (width - 2 * maxLabelWidth < width * 0.25f) {
            maxLabelWidth = 0;
            shouldShowLabels = false;
        }
    }
}
