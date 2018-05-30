package me.nithin.james.utils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static java.util.Calendar.DAY_OF_WEEK;
import static me.nithin.james.utils.DateUtils.getStartOfDay;
import static me.nithin.james.utils.StringUtils.defaultToStringStyle;

public final class Timestamp {

    public static final long DAY_LENGTH = 86400000;

    public static final Timestamp ZERO = new Timestamp(0);

    private final long unixTime;

    public Timestamp() {

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

    public Timestamp(GregorianCalendar cal) {
        this(cal.getTimeInMillis());
    }

    public Timestamp(long unixTime) {
        if (unixTime < 0 || unixTime % DAY_LENGTH != 0)
            throw new IllegalArgumentException(
                    "Invalid unix time: " + unixTime);

        this.unixTime = unixTime;
    }

    /**
     * Given two timestamps, returns whichever timestamp is the oldest one.
     */
    public static Timestamp oldest(Timestamp first, Timestamp second) {
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

        Timestamp timestamp = (Timestamp) o;

        return new EqualsBuilder()
                .append(unixTime, timestamp.unixTime)
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, defaultToStringStyle())
                .append("unixTime", unixTime)
                .toString();
    }

    public Timestamp minus(int days) {
        return plus(-days);
    }

    public Timestamp plus(int days) {
        return new Timestamp(unixTime + DAY_LENGTH * days);
    }

    /**
     * Returns the number of days between this timestamp and the given one. If
     * the other timestamp equals this one, returns zero. If the other timestamp
     * is older than this one, returns a negative number.
     */
    public int daysUntil(Timestamp other) {
        return (int) ((other.unixTime - this.unixTime) / DAY_LENGTH);
    }

    public boolean isNewerThan(Timestamp other) {
        return compare(other) > 0;
    }

    /**
     * Returns -1 if this timestamp is older than the given timestamp, 1 if this
     * timestamp is newer, or zero if they are equal.
     */
    public int compare(Timestamp other) {
        return Long.signum(this.unixTime - other.unixTime);
    }

    public boolean isOlderThan(Timestamp other) {
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
