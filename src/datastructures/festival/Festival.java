package datastructures.festival;

import java.util.ArrayList;

import classes.AtcErr;
import classes.FestivalXMLParser;
import datastructures.Constants;
import datastructures.Enums;
import datastructures.Enums.FestivalDay;
import datastructures.Enums.SpecialPassType;
import datastructures.Enums.StudentType;
import datastructures.SimpleDate;
import datastructures.festival.Prices.MilongaPrice;
import datastructures.festival.Prices.SpecialPass;

/**
 * This class provides an interface to the list of classes
 * and milongas available to register for. It also provides
 * access to the prices of each of the events via a query.
 * <p/>
 * The class requires an XML file containing all the class
 * information. I would recommend copying and editing the
 * file in the "test" directory.
 *
 * @author benjamyn
 */
public class Festival
{
    private Event  events[];
    private Prices prices[];

    public Festival(String festivalFile)
    {
        if (festivalFile == null)
            new AtcErr("Festival XML File: NULL");

        FestivalXMLParser fxmlp = new FestivalXMLParser(festivalFile);
        events = fxmlp.getEvents(FestivalDay.values().length);

        prices = new Prices[StudentType.values().length];
        for (StudentType st : StudentType.values())
        {
            prices[st.ordinal()] = fxmlp.getPrices(st);
        }
    }

    /**
     * Returns a list of classes based on the input day
     *
     * @param day the day for which we want all the class
     * @return a list of classes that occur on the input day
     */
    public ArrayList<Class> getClasses(FestivalDay day)
    {
        return events[day.ordinal()].getClasses();
    }

    /**
     * Returns the milonga that occurs on the input day
     *
     * @param day the day for which we want the milonga
     * @return the milonga that occurs on the input day
     */
    public Milonga getMilonga(FestivalDay day)
    {
        return events[day.ordinal()].getMilonga();
    }

    /**
     * Returns the SimpleDate object associated with the
     * event that occurs on the input day
     *
     * @param day the day for which we want the SimpleDate object
     * @return the SimpleDate representation of the input day
     */
    public SimpleDate getDate(FestivalDay day)
    {
        return events[day.ordinal()].getDate();

    }

    /**
     * Query to get the special pass with the given properties of
     * having the input student type and special pass type
     *
     * @param st  the student type that the SpecialPass will have
     * @param spt the special pass type that the SpecialPass will have
     * @return SpecialPass of the given StudentType and SpecialPassType
     */
    public SpecialPass getSpecialPass(StudentType st, SpecialPassType spt)
    {
        return prices[st.ordinal()].getSpecialPass(spt);
    }

    /**
     * Returns the class cost for the given student type
     *
     * @param st student type for the class cost
     * @return the class cost for the given student type
     */
    public int getClassCost(StudentType st)
    {
        return prices[st.ordinal()].getSingleClassCost();
    }

    /**
     * Query to get the milonga price for the given student type and
     * the given day
     *
     * @param st  student type of the milonga price
     * @param day day for which the milonga occurs
     * @return a MilongaPrice object which contains the milonga information and price
     */
    public MilongaPrice getMilongaPrice(StudentType st, FestivalDay day)
    {
        return prices[st.ordinal()].getMilongaPrice(day);
    }
}
