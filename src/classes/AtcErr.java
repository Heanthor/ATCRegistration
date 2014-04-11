package classes;

import javax.swing.*;
import java.awt.*;

/**
 * Class to handle ATC registration specific exceptions.
 *
 * @author benjamyn
 */
public class AtcErr extends Error
{
    public AtcErr(String msg)
    {
        this(msg, "");
    }

    public AtcErr(String msgFormat, Object... args)
    {
        String msg = String.format(msgFormat + "\n\n", args);
        JTextArea textArea = new JTextArea(msg + generateStackTrace());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showOptionDialog(null, scrollPane, "ERROR!!", JOptionPane.DEFAULT_OPTION,
                                               JOptionPane.ERROR_MESSAGE, null, null, null);
        System.exit(-1);
    }

    private String generateStackTrace()
    {
        StringBuilder builder = new StringBuilder();
        for (StackTraceElement e : getStackTrace())
        {
            builder.append(e.toString()).append("\n");
        }
        return builder.toString();
    }
}
