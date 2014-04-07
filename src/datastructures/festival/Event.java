package datastructures.festival;

import java.util.ArrayList;
import java.util.Collection;

import datastructures.SimpleDate;

/**
 * There is an "event" for each day of the festival.
 * An event contains the date of the event as well as the
 * list of classes that occur in that day's event.
 *
 * @author benjamyn
 */
public class Event
{
    private SimpleDate date;
    private ArrayList<Class> classes = new ArrayList<Class>();
    private Milonga milonga;

    public void addClass(Class c)
    {
        classes.add(c);
    }

    public void addClasses(Collection<Class> cs)
    {
        for (Class c : cs)
            classes.add(c);
    }

    public ArrayList<Class> getClasses()
    {
        return (ArrayList<Class>) classes.clone();
    }

    public void setMilonga(Milonga m)
    {
        this.milonga = m;
    }

    public Milonga getMilonga()
    {
        return milonga;
    }

    public void setDate(SimpleDate sd)
    {
        this.date = sd;
    }

    public SimpleDate getDate()
    {
        return date;
    }

    public String toString()
    {
        return date.toString() + "\n\t" + classes.toString() + "\n\t" + milonga.toString();
    }
}
