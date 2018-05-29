package me.nithin.james.freqchart;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static java.util.Calendar.DAY_OF_WEEK;
import static me.nithin.james.freqchart.DateUtils.getStartOfDay;
import static me.nithin.james.freqchart.StringUtils.defaultToStringStyle;

public final class FrequencyChartTimestamp {

    public static final long DAY_LENGTH = 86400000;

    public static final FrequencyChartTimestamp ZERO = new FrequencyChartTimestamp(0);

    private final long unixTime;

    public FrequencyChartTimestamp() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long now = getStartOfDay(cal.getTimeInMillis());

        if (now < 0 || now % DAY_LENGTH != 0)
            throw new IllegalArgumentException(
                    "Invalid unix time: " + now);

        this.unixTime = now;
    }

    public FrequencyChartTimestamp(GregorianCalendar cal) {
        this(cal.getTimeInMillis());
    }

    public FrequencyChartTimestamp(long unixTime) {
        if (unixTime < 0 || unixTime % DAY_LENGTH != 0)
            throw new IllegalArgumentException(
                    "Invalid unix time: " + unixTime);

        this.unixTime = unixTime;
    }

    /**
     * Given two timestamps, returns whichever timestamp is the oldest one.
     */
    public static FrequencyChartTimestamp oldest(FrequencyChartTimestamp first, FrequencyChartTimestamp second) {
        return first.unixTime < second.unixTime ? first : second;
    }

    public long getUnixTime() {
        return unixTime;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(unixTime).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FrequencyChartTimestamp frequencyChartTimestamp = (FrequencyChartTimestamp) o;

        return new EqualsBuilder()
                .append(unixTime, frequencyChartTimestamp.unixTime)
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, defaultToStringStyle())
                .append("unixTime", unixTime)
                .toString();
    }

    public FrequencyChartTimestamp minus(int days) {
        return plus(-days);
    }

    public FrequencyChartTimestamp plus(int days) {
        return new FrequencyChartTimestamp(unixTime + DAY_LENGTH * days);
    }

    /**
     * Returns the number of days between this timestamp and the given one. If
     * the other timestamp equals this one, returns zero. If the other timestamp
     * is older than this one, returns a negative number.
     */
    public int daysUntil(FrequencyChartTimestamp other) {
        return (int) ((other.unixTime - this.unixTime) / DAY_LENGTH);
    }

    public boolean isNewerThan(FrequencyChartTimestamp other) {
        return compare(other) > 0;
    }

    /**
     * Returns -1 if this timestamp is older than the given timestamp, 1 if this
     * timestamp is newer, or zero if they are equal.
     */
    public int compare(FrequencyChartTimestamp other) {
        return Long.signum(this.unixTime - other.unixTime);
    }

    public boolean isOlderThan(FrequencyChartTimestamp other) {
        return compare(other) < 0;
    }

    public Date toJavaDate() {
        return new Date(unixTime);
    }

    public int getWeekday() {
        return toCalendar().get(DAY_OF_WEEK) % 7;
    }

    public GregorianCalendar toCalendar() {
        GregorianCalendar day =
                new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        day.setTimeInMillis(unixTime);
        return day;
    }
}
