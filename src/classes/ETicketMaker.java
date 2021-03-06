package classes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import constants.Constants;
import datastructures.Name;
import datastructures.Enums.StudentType;

/**
 * Class to create ETickets for a Registrant
 *
 * @author benjamyn
 */
public class ETicketMaker
{
    private final String eticketFile;
    private final String outputDir;

    public ETicketMaker(String eticketFile, String outputDir)
    {
        this.eticketFile = eticketFile;
        this.outputDir = outputDir;
    }

    /**
     * Generates an E-Ticket given the name of the registrant,
     * their student type and a list of their classes.
     *
     * @param name        name of the registrant
     * @param studentType registrant's student status
     * @param classes     list of classes the registrant will be taking
     * @return a string of the location of the generate e-ticket image
     */
    public String createTicket(Name name, StudentType studentType, List<String> classes)
    {
        String output = outputDir + "/" + sanitize(name.last) + "_" + sanitize(name.first) + "-eticket.png";
        File f = new File(output);

        if (f.exists()) {
            System.out.format("File %s already exists.\n", output);
            return output;
        }

        try
        {
            BufferedImage image = ImageIO.read(new File(eticketFile));

            Graphics g = image.getGraphics();

            // set up font size and color
            g.setFont(g.getFont().deriveFont(Constants.ETICKET_LARGE_FONT_SIZE));
            g.setColor(Color.BLACK);

            // draw first and last name
            g.drawString(name.first, Constants.ETICKET_FIRST_NAME_X, Constants.ETICKET_FIRST_NAME_Y);
            g.drawString(name.last, Constants.ETICKET_LAST_NAME_X, Constants.ETICKET_LAST_NAME_Y);

            // draw ticket type
            g.drawString(studentType.toString(), Constants.ETICKET_STUDENT_TYPE_X, Constants.ETICKET_STUDENT_TYPE_Y);

            // draw list of classes
            g.setFont(g.getFont().deriveFont(Constants.ETICKET_SMALL_FONT_SIZE)); // decrease font size
            int yOffset = Constants.ETICKET_FIRST_CLASS_Y;
            for (String c : classes)
            {
                g.drawString(c, Constants.ETICKET_CLASSES_X, yOffset);
                yOffset += Constants.ETICKET_CLASS_INCREMENT_Y;
            }

            // clear drawing buffers
            g.dispose();

            ImageIO.write(image, "png", new File(output));

        }
        catch (IOException e)
        {
            new AtcErr("Could not create ticket '%s' using base eticket image '%s'", output, eticketFile);
        }

        return output;
    }

    private static String sanitize(String in) {
        return in.replaceAll("[^a-zA-Z0-9.-]", "_"); // disallowed characters

    }
}
