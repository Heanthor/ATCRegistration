package drivers;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import classes.Configuration;
import classes.ETicketMaker;
import classes.Emailer;
import classes.OnsiteRegistrationGUI;
import classes.SheetClient;
import datastructures.AccountInformation;
import datastructures.Enums.FestivalDay;
import datastructures.Enums.RegistrationMode;
import datastructures.Enums.SheetClientMode;
import datastructures.Enums.SpecialPassType;
import datastructures.Enums.StudentType;
import datastructures.Enums.YesNo;
import datastructures.Registrant;
import datastructures.festival.Festival;
import datastructures.festival.Milonga;
import datastructures.festival.Prices.SpecialPass;

/**
 * This driver is used to register new registrants on-site i.e
 * at the festival table. This driver essentially acts
 * as the glue between the OnsiteRegistrationGUI class and the
 * SheetClient class.
 *
 * @author benjamyn
 */
public class OnsiteRegisterer
{
    // MAIN
    public static void main(String args[])
    {
        Configuration config = new Configuration();
        OnsiteRegisterer or = new OnsiteRegisterer(config);
        OnsiteRegistrationGUI gui = new OnsiteRegistrationGUI(or);
        or.setGUI(gui);
    }

    private volatile AccountInformation    ai;
    private volatile SheetClient           sc;
    private volatile OnsiteRegistrationGUI gui;

    private volatile Festival festival; // contains all the festival class and price information

    private volatile Emailer      emailer;
    private volatile ETicketMaker eticketMaker;

    private volatile Configuration config;

    public OnsiteRegisterer(Configuration config)
    {
        this.config = config;
        ai = config.getAccountInformation();
        sc = new SheetClient(ai, SheetClientMode.ON_SITE, RegistrationMode.LATE_REGISTRATION);
        gui = null;

        festival = new Festival(config.getFestivalClassesFile());

        emailer = new Emailer(ai.userName, ai.passwd);
        eticketMaker = new ETicketMaker(config.getETicketFile(), "etickets");
    }

    /**
     * Sets the GUI that the onsite registerer should interact with when
     * calling methods to update the GUI's interface
     *
     * @param gui
     */
    public void setGUI(OnsiteRegistrationGUI gui)
    {
        this.gui = gui;
    }

    /**
     * Clears the GUI's fields
     */
    public void newRegistrant()
    {
        gui.resetFields();
    }

    /**
     * updates the cost values found in the GUI for the checked leaves
     * in the tree and for the given student type
     *
     * @param checkedLeaves list of leaves that have been checked
     * @param st            student type for which to calculate the cost for
     */
    public void updateTotalCost(List<DefaultMutableTreeNode> checkedLeaves, StudentType st)
    {
        double totalCost = 0;

        // we need to process the special passes first to see if we should
        // not add the cost of classes/milongas that occur on those days
        boolean addFridayClasses = true;
        boolean addSaturdayClasses = true;
        boolean addSundayClasses = true;
        boolean addMilongas = true;

        for (DefaultMutableTreeNode node : checkedLeaves)
        {
            Object userObj = node.getUserObject();

            if (userObj instanceof SpecialPassCost)
            {
                SpecialPassCost sPass = (SpecialPassCost) userObj;

                switch (sPass.specialPass.passType)
                {
                case FULL_PASS:
                    addFridayClasses = addSaturdayClasses = addSundayClasses = false;
                    break;
                case FRIDAY_PASS:
                    addFridayClasses = false;
                    break;
                case SATURDAY_PASS:
                    addSaturdayClasses = false;
                    break;
                case SUNDAY_PASS:
                    addSundayClasses = false;
                    break;
                case MILONGA_PASS:
                    addMilongas = false;
                    break;
                }
            }
        }

        // now we add the costs, checking if any special actions need to be done
        for (DefaultMutableTreeNode node : checkedLeaves)
        {
            Object userObj = node.getUserObject();
            if (userObj instanceof ClassCost)
            {
                ClassCost cc = (ClassCost) userObj;

                // only add classes if don't have a special pass for that day
                switch (cc.day)
                {
                case FRIDAY:
                    if (addFridayClasses)
                        totalCost += ClassCost.cost;
                    break;
                case SATURDAY:
                    if (addSaturdayClasses)
                        totalCost += ClassCost.cost;
                    break;
                case SUNDAY:
                    if (addSundayClasses)
                        totalCost += ClassCost.cost;
                    break;
                }
            }

            // always add special passes
            if (userObj instanceof SpecialPassCost)
                totalCost += ((SpecialPassCost) userObj).specialPass.cost;

            // milonga passes more complicated: need to check day and if milonga pass
            if (userObj instanceof MilongaCost)
            {
                MilongaCost mc = (MilongaCost) userObj;

                if (addMilongas)
                {
                    switch (mc.day)
                    {
                    case FRIDAY:
                        if (addFridayClasses)
                            totalCost += mc.cost;
                        break;
                    case SATURDAY:
                        if (addSaturdayClasses)
                            totalCost += mc.cost;
                        break;
                    case SUNDAY:
                        if (addSundayClasses)
                            totalCost += mc.cost;
                        break;
                    }
                }
            }
        }

        gui.updateTotalCost(totalCost, config.getTaxPercent() / 100. * totalCost);
    }

    /**
     * creates a new registrant, adding the information to the google spreadsheet
     * and sends an e-ticket if required
     */
    public void submit()
    {
        // check if we need to send an e-ticket
        Registrant newReg = createRegistrant();

        if (newReg.hasEticketSent())
        {
            // remove the "None" and empty string classes
            ArrayList<String> trueClasses = new ArrayList<String>();
            for (String regClass : newReg.getFilteredClasses())
                trueClasses.add(regClass);

            // create ticket
            String eTicketFile = eticketMaker.createTicket(newReg.name, newReg.studentType, trueClasses);

            // create email
            emailer.resetEmail();
            emailer.setSubjectLine(config.getEmailSubject(Configuration.EmailType.ETICKET));
            emailer.setBodyFile(config.getEmailBodyFile(Configuration.EmailType.ETICKET));
            emailer.addRecipient(newReg.email);
            //			emailer.addRecipient ("benjamyn.ward@gmail.com");
            emailer.addAttachment(eTicketFile);
            emailer.sendEmail();
        }

        sc.pushNewRegistrant(newReg);
        newRegistrant();
    }

    private Registrant createRegistrant()
    {
        Registrant reg = new Registrant(sc.getNumWorksheetRows() + 1,
                                        gui.getFirstName(),
                                        gui.getLastName(),
                                        gui.getEmail(),
                                        gui.getPhoneNumber(),
                                        gui.getStudentStatus(),
                                        gui.getDancerType(),
                                        gui.getExperienceLevel(),
                                        gui.getTotal(),
                                        gui.getNumRegistrants());

        if (gui.getPaymentReceived() == YesNo.YES)
            reg.setPaid(true);

        if (gui.getSendETicket() == YesNo.YES)
            reg.setEticketSent(true);

        // get list of classes and milongas
        for (DefaultMutableTreeNode node : gui.getCheckedList())
        {
            Object userObj = node.getUserObject();

            if (userObj instanceof ClassCost)
                reg.addClass(((ClassCost) userObj).festivalClass.name);

            if (userObj instanceof MilongaCost)
                reg.addClass(((MilongaCost) userObj).milonga.name);
        }

        return reg;
    }

    /**
     * updates the tree in the GUI to reflect the new costs caused
     * by a change in the student type
     *
     * @param st new student type
     */
    public void changeTicketCost(StudentType st)
    {
        gui.setTreeModel(getTreeModel(st));
    }

    /**
     * Creates a tree model that contains a tree of the passes,
     * classes, and milongas and their costs for the input student type
     *
     * @param st student type for which to get the costs for
     * @return a tree model containing a tree of the passes, classes, and milongas and their costs
     */
    public DefaultTreeModel getTreeModel(StudentType st)
    {
        // set class cost
        ClassCost.setClassCost(festival.getClassCost(st));

        // root
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

        // special tickets
        DefaultMutableTreeNode specialTickets = new DefaultMutableTreeNode("Special Tickets");
        for (SpecialPassType passType : SpecialPassType.values())
            specialTickets.add(new DefaultMutableTreeNode(new SpecialPassCost(festival.getSpecialPass(st, passType))));
        root.add(specialTickets);

        // milongas
        DefaultMutableTreeNode milongas = new DefaultMutableTreeNode("Milongas");
        for (FestivalDay day : FestivalDay.values())
            milongas.add(new DefaultMutableTreeNode(new MilongaCost(festival.getMilonga(day), festival.getMilongaPrice(st, day).cost, day)));
        root.add(milongas);

        // a la carte
        DefaultMutableTreeNode aLaCarte = new DefaultMutableTreeNode("A La Carte");

        // friday classes
        DefaultMutableTreeNode fridayClasses = new DefaultMutableTreeNode("Friday Classes");
        for (datastructures.festival.Class friClass : festival.getClasses(FestivalDay.FRIDAY))
            fridayClasses.add(new DefaultMutableTreeNode(new ClassCost(friClass, FestivalDay.FRIDAY)));
        aLaCarte.add(fridayClasses);

        // saturday classes
        DefaultMutableTreeNode saturdayClasses = new DefaultMutableTreeNode("Saturday Classes");
        for (datastructures.festival.Class satClass : festival.getClasses(FestivalDay.SATURDAY))
            saturdayClasses.add(new DefaultMutableTreeNode(new ClassCost(satClass, FestivalDay.SATURDAY)));
        aLaCarte.add(saturdayClasses);

        // sunday classes
        DefaultMutableTreeNode sundayClasses = new DefaultMutableTreeNode("Sunday Classes");
        for (datastructures.festival.Class sunClass : festival.getClasses(FestivalDay.SUNDAY))
            sundayClasses.add(new DefaultMutableTreeNode(new ClassCost(sunClass, FestivalDay.SUNDAY)));
        aLaCarte.add(sundayClasses);

        root.add(aLaCarte);

        return new DefaultTreeModel(root);
    }

	/* ********** SOME CLASSES TO CONNECT FESTIVAL OBJECTS TO THEIR DAY AND COST ***********/
    // also used to return a tree friendly string

    private static class ClassCost
    {
        public final datastructures.festival.Class festivalClass;
        public final FestivalDay                   day;
        private static int cost = 0;

        public ClassCost(datastructures.festival.Class festivalClass, FestivalDay day)
        {
            this.festivalClass = festivalClass;
            this.day = day;
        }

        public String toString()
        {
            return "($" + cost + ") " + festivalClass;
        }

        public static void setClassCost(int cost)
        {
            ClassCost.cost = cost;
        }
    }

    private class SpecialPassCost
    {
        public final SpecialPass specialPass;

        public SpecialPassCost(SpecialPass specialPass)
        {
            this.specialPass = specialPass;
        }

        public String toString()
        {
            return "($" + specialPass.cost + ") " + specialPass;
        }
    }

    private class MilongaCost
    {
        public final Milonga     milonga;
        public final int         cost;
        public final FestivalDay day;

        public MilongaCost(Milonga milonga, int cost, FestivalDay day)
        {
            this.milonga = milonga;
            this.cost = cost;
            this.day = day;
        }

        public String toString()
        {
            return "($" + cost + ") " + milonga;
        }
    }
}
