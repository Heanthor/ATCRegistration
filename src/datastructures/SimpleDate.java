package datastructures;

/**
 * Simple class to hold date information. Namely, the weekday,
 * day, and month.
 *
 * @author benjamyn
 */
public class SimpleDate
{
    public final String weekDay;
    public final int    day;
    public final String month;

    public SimpleDate(String weekDay, int day, String month)
    {
        this.weekDay = weekDay;
        this.day = day;
        this.month = month;
    }

    public String toString()
    {
        return weekDay + " " + day + " " + month;
    }
}