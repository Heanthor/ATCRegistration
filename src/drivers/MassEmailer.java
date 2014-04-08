package drivers;

import java.awt.*;
import java.util.List;

import classes.AtcErr;
import classes.Configuration;
import classes.Configuration.EmailType;
import classes.Emailer;
import classes.SheetClient;
import datastructures.AccountInformation;
import datastructures.Enums.RegistrationMode;
import datastructures.Enums.SheetClientMode;
import datastructures.Registrant;

import javax.swing.*;

/**
 * This program sents out a mass email to each of the paid registrants in
 * the two spreadsheets: the formsite and the on-site spreadsheet.
 * <p/>
 * PLEASE BE CAREFUL WITH THIS PROGRAM. Before the email is sent out,
 * it lists information about all the emails and asks THREE times about
 * sending out the email. Sending an email to >100 people needs to
 * not be an accident.
 *
 * @author benjamyn
 */
public class MassEmailer
{
    private static enum EmailerOptions
    {
        PRE_FESTIVAL
                {
                    @Override
                    public String toString()
                    {
                        return "Pre-Festival";
                    }

                    @Override
                    EmailType getEmailType()
                    {
                        return EmailType.PREFESTIVAL;
                    }
                },
        THANK_YOU
                {
                    @Override
                    public String toString()
                    {
                        return "Thank You";
                    }

                    @Override
                    EmailType getEmailType()
                    {
                        return EmailType.THANK_YOU;
                    }
                };

        abstract EmailType getEmailType();
    }

    public static void main(String args[])
    {
        int res = JOptionPane.showOptionDialog(null, "Do you want to send the pre-festival or thank-you email?",
                                               "What would you like to do?", JOptionPane.DEFAULT_OPTION,
                                               JOptionPane.QUESTION_MESSAGE, null,
                                               EmailerOptions.values(), null);

        Configuration config = new Configuration();
        EmailType eType = EmailerOptions.values()[res].getEmailType();

        String subjectLine = config.getEmailSubject(eType);
        String emailBodyFile = config.getEmailBodyFile(eType);

        // set to early registrants
        MassEmailer me = new MassEmailer(config, RegistrationMode.EARLY_REGISTRATION);
        me.setUpMassEmail(emailBodyFile, subjectLine);
        if (!me.sendEmails(RegistrationMode.EARLY_REGISTRATION))
        {
            AtcErr.createErrorDialog("Did not send the early registrants their emails");
        }

        // set to late registrants
        me.setRegistrationMode(RegistrationMode.LATE_REGISTRATION);
        me.setUpMassEmail(emailBodyFile, subjectLine);
        me.sendEmails(RegistrationMode.LATE_REGISTRATION);
    }

    private AccountInformation ai;
    private SheetClient        sc;
    private Emailer            emailer;

    /**
     * Sets up the account information, spreadsheet, and emailing features
     *
     * @param config  Configuration object containing the meta-data
     * @param regMode registration mode to start the SheetClient in
     */
    public MassEmailer(Configuration config, RegistrationMode regMode)
    {
        ai = config.getAccountInformation();
        sc = new SheetClient(ai, SheetClientMode.FORM_SITE, regMode);
        sc.refresh();
        emailer = new Emailer(ai.userName, ai.passwd);
    }

    /**
     * Sets up the mass email to be sent, but does not send it. After
     * calling this function, the email(s) are "locked and loaded".
     *
     * @param bodyFile
     * @param subjLine
     */
    public void setUpMassEmail(String bodyFile, String subjLine)
    {
        List<Registrant> paidRegsFormSite = sc.getPaidRegistrants();
        sc.setMode(SheetClientMode.ON_SITE);
        List<Registrant> paidRegsOnsite = sc.getPaidRegistrants();

        emailer.resetEmail();
        emailer.setSeperateEmails(true);
        emailer.sendAttachments(false);
        emailer.setSubjectLine(subjLine);
        emailer.setBodyFile(bodyFile);

        // add recipients
        for (Registrant reg : paidRegsFormSite)
            emailer.addRecipient(reg.email);
        for (Registrant reg : paidRegsOnsite)
            emailer.addRecipient(reg.email);
    }

    /**
     * Sends out the emails after verifying with the user that it is really
     * what they want to do.
     *
     * @return true if emails were sent, false otherwise
     * @param registrationMode
     */
    public boolean sendEmails(RegistrationMode registrationMode)
    {
        String msg = String.format("The email information that will be sent is for mode '%s': %s\n",
                                   registrationMode, emailer);
        JTextArea textArea = new JTextArea(msg);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new Dimension(1000, 800));

        int res = JOptionPane.showOptionDialog(null, scrollPane, "Email Contents", JOptionPane.OK_CANCEL_OPTION,
                                               JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (res == JOptionPane.CANCEL_OPTION)
        {
            AtcErr.createErrorDialog("Not sending emails.");
        }

        queryUserIfSure("ARE YOU SURE YOU WANT TO SEND THESE EMAILS?");
        queryUserIfSure("ARE YOU __REALLY__ SURE YOU WANT TO SEND THESE EMAILS?");
        queryUserIfSure("I'M ONLY GOING TO ASK YOU ONE MORE TIME!");

        // Okay, I'm sending those emails then...........
        emailer.sendEmail();

        return true;
    }

    private void queryUserIfSure(String msg)
    {
        int res = JOptionPane.showOptionDialog(null, msg, "Are you sure?", JOptionPane.OK_CANCEL_OPTION,
                                               JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (res == JOptionPane.CANCEL_OPTION)
        {
            AtcErr.createErrorDialog("Not sending emails.");
        }
    }

    public void setRegistrationMode(RegistrationMode regMode)
    {
        sc.setRegistrationMode(regMode);
    }
}
