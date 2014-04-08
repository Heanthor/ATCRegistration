package classes;

import javax.swing.*;

/**
 * Class to handle ATC registration specific exceptions.
 *
 * @author benjamyn
 */
public class AtcErr extends Error
{
    private AtcErr() {}

    public static void createErrorDialog(String msg)
    {
        createErrorDialog(msg, "");
    }

    public static void createErrorDialog(String msgFormat, Object... args)
    {
        String msg = String.format(msgFormat, args);
        JOptionPane.showOptionDialog(null, msg, "ERROR!!", JOptionPane.DEFAULT_OPTION,
                                     JOptionPane.ERROR_MESSAGE, null, null, null);
        System.exit(-1);
    }
}
