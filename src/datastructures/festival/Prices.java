package datastructures.festival;

import java.util.ArrayList;
import java.util.Collection;

import classes.AtcErr;

import datastructures.Enums.FestivalDay;
import datastructures.Enums.SpecialPassType;
import datastructures.Enums.StudentType;


/**
 * This class encapsulates the price of the classes,
 * special passes, and milongas for a particular
 * type of admission ticket.
 *
 * @author benjamyn
 */
public class Prices
{
    /**
     * A class that represents a Special Pass ticket such
     * as a Full Festival pass.
     *
     * @author benjamyn
     */
    public static class SpecialPass implements Comparable<SpecialPass>
    {
        public final SpecialPassType passType;
        public final int             cost;
        public final int             partnerCost;

        public SpecialPass(SpecialPassType passType, int cost, int partnerCost)
        {
            this.passType = passType;
            this.cost = cost;
            this.partnerCost = partnerCost;
        }

        @Override
        public int compareTo(SpecialPass sp)
        {
            return this.passType.ordinal() - sp.passType.ordinal();
        }

        public boolean equals(SpecialPass sp)
        {
            return this.passType.ordinal() == sp.passType.ordinal();
        }

        @Override
        public String toString()
        {
            return passType + ": " + cost;
        }
    }

    /**
     * A class that represents a Milonga and its price. Milongas
     * are classified by the night that the Milonga occurs on.
     *
     * @author benjamyn
     */
    public static class MilongaPrice implements Comparable<MilongaPrice>
    {
        public final FestivalDay day;
        public final int         cost;

        public MilongaPrice(FestivalDay day, int cost)
        {
            this.day = day;
            this.cost = cost;
        }

        public int compareTo(MilongaPrice mp)
        {
            return this.day.ordinal() - mp.day.ordinal();
        }

        public boolean equals(MilongaPrice mp)
        {
            return this.day.ordinal() == mp.day.ordinal();
        }

        public String toString()
        {
            return day + ": " + cost;
        }
    }

    private final StudentType studentType;
    private ArrayList<SpecialPass> specialPasses = new ArrayList<SpecialPass>();
    private final int singleClassCost;
    private ArrayList<MilongaPrice> milongaPrices = new ArrayList<MilongaPrice>();

    public Prices(StudentType studentType, int singleClassCost)
    {
        this.studentType = studentType;
        this.singleClassCost = singleClassCost;
    }

    /**
     * Returns the StudentType associated with the Prices object
     *
     * @return the StudentType associated with the Prices object
     */
    public StudentType getStudentType()
    {
        return studentType;
    }

    /**
     * Returns the cost of a single class
     *
     * @return the cost of a single class
     */
    public int getSingleClassCost()
    {
        return singleClassCost;
    }

    /**
     * Add/overwrite a SpecialPass for this Prices object
     *
     * @param sp SpecialPass to add
     */
    public void addSpecialPass(SpecialPass sp)
    {
        int index = specialPasses.indexOf(sp);

        if (index == -1)
            specialPasses.add(sp);
        else
            specialPasses.set(index, sp);
    }

    /**
     * Add/overwrite a set of SpecialPass's for this Prices object
     *
     * @param sps Collection of SpecialPass's to add
     */
    public void addSpecialPasses(Collection<SpecialPass> sps)
    {
        for (SpecialPass sp : sps)
            addSpecialPass(sp);
    }

    /**
     * Used to get the SpecialPass associated with the student
     * type of the Prices object and the input SpecialPassType
     *
     * @param passType the type of the SpecialPass to return
     * @return the SpecialPass associated with the StudentType and SpecialPassType
     */
    public SpecialPass getSpecialPass(SpecialPassType passType)
    {
        for (SpecialPass sp : specialPasses)
            if (sp.passType == passType)
                return sp;

        // this is a problem
        new AtcErr("Could not find special pass type: '%s'", passType);
        return null;
    }

    /**
     * Add/overwrite a MilongaPrice for this Prices object
     *
     * @param mp MilongaPrice to add
     */
    public void addMilongaPrice(MilongaPrice mp)
    {
        int index = milongaPrices.indexOf(mp);

        if (index == -1)
            milongaPrices.add(mp);
        else
            milongaPrices.set(index, mp);
    }

    /**
     * Add/overwrite a set of MilongaPrice's for this Prices object
     *
     * @param mps Collection of MilongaPrice's to add
     */
    public void addMilongaPrices(Collection<MilongaPrice> mps)
    {
        for (MilongaPrice mp : mps)
            addMilongaPrice(mp);
    }

    /**
     * Used to get the price of the milonga for the specific day
     * for the student type associated with the Prices object
     *
     * @param day the day that the milonga occurs on
     * @return the price for the milonga
     */
    public MilongaPrice getMilongaPrice(FestivalDay day)
    {
        for (MilongaPrice mp : milongaPrices)
            if (mp.day == day)
                return mp;

        // this is a problem
        new AtcErr("Could not find milonga price for day: '%s'", day);
        return null;
    }

    @Override
    public String toString()
    {
        return studentType + "\n\tSingle class cost: " + singleClassCost + "\n\t"
                       + "Special passes: " + specialPasses + "\n\tMilonga prices: " + milongaPrices;
    }
}
