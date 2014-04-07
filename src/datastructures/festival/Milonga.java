package datastructures.festival;

/**
 * An immutable class that contains information for this "event"'s
 * milonga. This class does not contain pricing information. It
 * is simply used to get the name and times of the milonga.
 *
 * @author benjamyn
 */
public class Milonga
{
    public final String name;
    public final int    startTime;
    public final int    endTime;

    public Milonga(String name, int startTime, int endTime)
    {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String toString()
    {
        return name + ": " + startTime + " - " + endTime;
    }
}
