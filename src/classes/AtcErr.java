package classes;

/**
 * Class to handle ATC registration specific exceptions.
 *
 * @author benjamyn
 */
public class AtcErr extends Error
{
    public AtcErr()
    {
        super();
    }

    public AtcErr(String msg)
    {
        super(msg);
    }
}
