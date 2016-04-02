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
        if (first != null) {
            this.first = toTitleCase(first);
        } else {
            this.first = null;
        }
        if (last != null) {
            this.last = toTitleCase(last);
        } else {
            this.last = null;
        }
    }

    @Override
    public String toString()
    {
        return last + ", " + first;
    }

    private static String toTitleCase(String string) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : string.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }
}
