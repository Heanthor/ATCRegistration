package datastructures;

/**
 * Immutable name class to hold first and last name.
 *
 * @author benjamyn
 */
public class Name
{
    public final String first;
    public final String last;

    public Name(String first, String last)
    {
        this.first = first;
        this.last = last;
    }

    @Override
    public String toString()
    {
        return last + ", " + first;
    }
}
