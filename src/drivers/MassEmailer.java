package drivers;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import classes.*;
import classes.Configuration.EmailType;
import datastructures.*;
import datastructures.Enums.RegistrationMode;

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
public class MassEmailer {
    private static enum EmailerOptions {
        PRE_FESTIVAL {
            @Override
            public String toString() {
                return "Pre-Festival";
            }

            @Override
            EmailType getEmailType() {
                return EmailType.PREFESTIVAL;
            }
        },
        THANK_YOU {
            @Override
            public String toString() {
                return "Thank You";
            }

            @Override
            EmailType getEmailType() {
                return EmailType.THANK_YOU;
            }
        };

        abstract EmailType getEmailType();
    }

    public static void main(String args[]) {
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
        me.setUpEmail(emailBodyFile, subjectLine);

//        me.processRegistrants(RegistrationMode.EARLY_REGISTRATION);
        me.processRegistrants();

        if (!me.sendEmails()) {
            new AtcErr("Did not send the early registrants their emails");
        }
    }

    private AccountInformation ai;
    private DatabaseInformation db;
    private DBClient rds;
    private Emailer emailer;
    private ETicketMaker etm;

    private static ArrayList<Integer> sentRegisterIDs = new ArrayList<>();
    /**
     * Sets up the account information, spreadsheet, and emailing features
     *
     * @param config  Configuration object containing the meta-data
     * @param regMode registration mode to start the SheetClient in
     */
    public MassEmailer(Configuration config, RegistrationMode regMode) {
        ai = config.getAccountInformation();
        db = config.getDatabaseInfo();
        rds = new DBClient(db, regMode);
        emailer = new Emailer(ai.userName, ai.passwd);
        etm = new ETicketMaker(config.getETicketFile(), "etickets");

        try {
            rds.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the mass email to be sent, but does not send it. After
     * calling this function, the email(s) are "locked and loaded".
     *
     * @param bodyFile
     * @param subjLine
     */
    public void setUpEmail(String bodyFile, String subjLine) {
        emailer.resetEmail();
        emailer.setSeperateEmails(true);
        emailer.sendAttachments(true);
        emailer.setSubjectLine(subjLine);
        emailer.setBodyFile(bodyFile);
    }

    private void processRegistrants() {
        List<Registrant> paidRegsFormSite = rds.getPaidRegistrants();

        // add recipients
        for (Registrant reg : paidRegsFormSite) {
            System.out.println("Processing registrant " + reg.name + ".");
            emailer.addRecipient(reg.email);

            for (String ticket : generateTickets(reg)) {
                emailer.addAttachmentForAddress(reg.email, ticket);
            }

            sentRegisterIDs.add(reg.registerid);
        }
//
//        Registrant test1 = new Registrant(-1, "Reed", "Trevelyan", null, null, "reedtrevelyan@gmail.com", null,
//                Enums.StudentType.GENERAL_ADMISSION, null, null, 0, 1, Arrays.asList("Class 1", "Class 2"), false,
//                null, null, 123);
//
//        Registrant test2 = new Registrant(-1, "David", "Chorvinsky", null, null, "firekenace@gmail.com", null,
//                Enums.StudentType.GENERAL_ADMISSION, null, null, 0, 1, Arrays.asList("Class 1", "Class 2"), false,
//                null, null, 124);
//
//        emailer.addRecipient(test1.email);
//        emailer.addRecipient(test2.email);
//
//        sentRegisterIDs.add(test1.registerid);
//        sentRegisterIDs.add(test2.registerid);
//
//        emailer.addAttachmentForAddress(test1.email, generateTickets(test1)[0]);
//        emailer.addAttachmentForAddress(test1.email, generateTickets(test2)[0]);
//
//        emailer.addAttachmentForAddress(test2.email, generateTickets(test2)[0]);
//        emailer.addAttachmentForAddress(test2.email, generateTickets(test1)[0]);

    }

    public String[] generateTickets(Registrant registrant) {
        if (!registrant.hasSecondRegistrant()) {
            return new String[] {etm.createTicket(registrant.name, registrant.studentType, registrant.getAllClasses())};
        } else {
            return new String[] {
                    etm.createTicket(registrant.name, registrant.studentType, registrant.getAllClasses()),
                    etm.createTicket(registrant.partnername, registrant.studentType, registrant.getAllClasses())
            };
        }
    }

    /**
     * Sends out the emails after verifying with the user that it is really
     * what they want to do.
     *
     * @return true if emails were sent, false otherwise
     */
    public boolean sendEmails() {
        String msg = String.format("The email information that will be sent is: %s\n", emailer);
        JTextArea textArea = new JTextArea(msg);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(800, 600));

        int res = JOptionPane.showOptionDialog(null, scrollPane, "Email Contents", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (res == JOptionPane.CANCEL_OPTION || res == JOptionPane.CLOSED_OPTION) {
            new AtcErr("Not sending emails.");
        }

        queryUserIfSure("ARE YOU SURE YOU WANT TO SEND THESE EMAILS?");
        queryUserIfSure("ARE YOU __REALLY__ SURE YOU WANT TO SEND THESE EMAILS?");
        queryUserIfSure("I'M ONLY GOING TO ASK YOU ONE MORE TIME!");

        // Okay, I'm sending those emails then...........
        emailer.sendEmail();

        // save registerIDs that have been sent to
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("files/sent_registerids.txt"));

            for (int i: sentRegisterIDs) {
                bw.write("" + i + "\n");
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void queryUserIfSure(String msg) {
        int res = JOptionPane.showOptionDialog(null, msg, "Are you sure?", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (res == JOptionPane.CANCEL_OPTION) {
            new AtcErr("Not sending emails.");
        }
    }
}
