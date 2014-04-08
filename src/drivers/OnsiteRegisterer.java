package drivers;

import java.util.LinkedList;
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

    private volatile AccountInformation    m_accountInformation;
    private volatile SheetClient           m_sheetClient;
    private volatile OnsiteRegistrationGUI m_gui;
    private volatile Festival              m_festival; // contains all the festival class and price information
    private volatile Emailer               m_emailer;
    private volatile ETicketMaker          m_eTicketMaker;
    private volatile Configuration         m_config;

    public OnsiteRegisterer(Configuration config)
    {
        m_config = config;
        m_accountInformation = config.getAccountInformation();
        m_sheetClient = new SheetClient(m_accountInformation, SheetClientMode.ON_SITE, RegistrationMode.LATE_REGISTRATION);
        m_gui = null;
        m_festival = new Festival(config.getFestivalClassesFile());
        m_emailer = new Emailer(m_accountInformation.userName, m_accountInformation.passwd);
        m_eTicketMaker = new ETicketMaker(config.getETicketFile(), "etickets");
    }

    /**
     * Sets the GUI that the onsite registerer should interact with when
     * calling methods to update the GUI's interface
     *
     * @param gui
     */
    public void setGUI(OnsiteRegistrationGUI gui)
    {
        m_gui = gui;
    }

    /**
     * Clears the GUI's fields
     */
    public void resetRegistrationGui()
    {
        m_gui.resetFields();
    }

    /**
     * updates the cost values found in the GUI for the checked leaves
     * in the tree and for the given student type
     *  @param checkedLeaves list of leaves that have been checked
     *
     */
    public void updateTotalCost(List<DefaultMutableTreeNode> checkedLeaves)
    {
        double totalCost = 0;

        // we need to process the special passes first to see if we should
        // not add the cost of classes/milongas that occur on those days
        boolean addFridayClasses = true;
        boolean addSaturdayClasses = true;
        boolean addSundayClasses = true;
        boolean addMilongas = true;

        int numRegistrants = m_gui.getNumRegistrants();

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
                        totalCost += ClassCost.cost * numRegistrants;
                    break;
                case SATURDAY:
                    if (addSaturdayClasses)
                        totalCost += ClassCost.cost * numRegistrants;
                    break;
                case SUNDAY:
                    if (addSundayClasses)
                        totalCost += ClassCost.cost * numRegistrants;
                    break;
                }
            }

            // always add special passes
            if (userObj instanceof SpecialPassCost)
            {
                if (numRegistrants == 2)
                {
                    totalCost += ((SpecialPassCost) userObj).specialPass.partnerCost;
                }
                else
                {
                    totalCost += ((SpecialPassCost) userObj).specialPass.cost;
                }
            }

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
                            totalCost += mc.cost * numRegistrants;
                        break;
                    case SATURDAY:
                        if (addSaturdayClasses)
                            totalCost += mc.cost * numRegistrants;
                        break;
                    case SUNDAY:
                        if (addSundayClasses)
                            totalCost += mc.cost * numRegistrants;
                        break;
                    }
                }
            }
        }

        m_gui.updateTotalCost(totalCost, m_config.getTaxPercent() / 100. * totalCost);
    }

    /**
     * creates a new registrant, adding the information to the google spreadsheet
     * and sends an e-ticket if required
     */
    public void submit()
    {
        // check if we need to send an e-ticket
        processNewRegistrant(createRegistrant());
        resetRegistrationGui();
    }

    private void processNewRegistrant(Registrant reg)
    {
        if (reg.hasEticketSent())
        {
            sendEticketForRegistrant(reg);
        }

        m_sheetClient.pushNewRegistrant(reg);
    }

    private void sendEticketForRegistrant(Registrant reg)
    {
        // create email
        m_emailer.resetEmail();
        m_emailer.setSubjectLine(m_config.getEmailSubject(Configuration.EmailType.ETICKET));
        m_emailer.setBodyFile(m_config.getEmailBodyFile(Configuration.EmailType.ETICKET));
        m_emailer.addRecipient(reg.email);

        // create ticket
        List<String> filteredClasses = reg.getFilteredClasses();
        String eTicketFile = m_eTicketMaker.createTicket(reg.name, reg.studentType, filteredClasses);
        m_emailer.addAttachment(eTicketFile);

        if (reg.hasSecondRegistrant())
        {
            String secondaryETicketFile = m_eTicketMaker.createTicket(reg.secondRegName, reg.studentType, filteredClasses);
            m_emailer.addAttachment(secondaryETicketFile);
        }

        m_emailer.sendEmail();
    }

    private Registrant createRegistrant()
    {
        // get list of classes and milongas
        List<String> classes = new LinkedList<>();
        for (DefaultMutableTreeNode node : m_gui.getCheckedList())
        {
            Object userObj = node.getUserObject();

            if (userObj instanceof ClassCost)
                classes.add(((ClassCost) userObj).festivalClass.name);

            if (userObj instanceof MilongaCost)
                classes.add(((MilongaCost) userObj).milonga.name);
        }

        return new Registrant(m_sheetClient.getNumWorksheetRows() + 1,
                              m_gui.getFirstName(),
                              m_gui.getLastName(),
                              m_gui.getEmail(),
                              m_gui.getPhoneNumber(),
                              m_gui.getStudentStatus(),
                              m_gui.getDancerType(),
                              m_gui.getExperienceLevel(),
                              m_gui.getTotal(),
                              m_gui.getNumRegistrants(),
                              classes,
                              m_gui.getSendETicket().equals(YesNo.YES),
                              m_gui.getSecondRegFirstName(),
                              m_gui.getSecondRegLastName());
    }

    /**
     * updates the tree in the GUI to reflect the new costs caused
     * by a change in the student type
     *
     * @param st new student type
     */
    public void changeTicketCost(StudentType st)
    {
        m_gui.setTreeModel(getTreeModel(st));
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
        ClassCost.setClassCost(m_festival.getClassCost(st));

        // root
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

        // special tickets
        DefaultMutableTreeNode specialTickets = new DefaultMutableTreeNode("Special Tickets");
        for (SpecialPassType passType : SpecialPassType.values())
            specialTickets.add(new DefaultMutableTreeNode(new SpecialPassCost(m_festival.getSpecialPass(st, passType))));
        root.add(specialTickets);

        // milongas
        DefaultMutableTreeNode milongas = new DefaultMutableTreeNode("Milongas");
        for (FestivalDay day : FestivalDay.values())
            milongas.add(new DefaultMutableTreeNode(new MilongaCost(m_festival.getMilonga(day), m_festival.getMilongaPrice(st, day).cost, day)));
        root.add(milongas);

        // a la carte
        DefaultMutableTreeNode aLaCarte = new DefaultMutableTreeNode("A La Carte");

        // friday classes
        DefaultMutableTreeNode fridayClasses = new DefaultMutableTreeNode("Friday Classes");
        for (datastructures.festival.Class friClass : m_festival.getClasses(FestivalDay.FRIDAY))
            fridayClasses.add(new DefaultMutableTreeNode(new ClassCost(friClass, FestivalDay.FRIDAY)));
        aLaCarte.add(fridayClasses);

        // saturday classes
        DefaultMutableTreeNode saturdayClasses = new DefaultMutableTreeNode("Saturday Classes");
        for (datastructures.festival.Class satClass : m_festival.getClasses(FestivalDay.SATURDAY))
            saturdayClasses.add(new DefaultMutableTreeNode(new ClassCost(satClass, FestivalDay.SATURDAY)));
        aLaCarte.add(saturdayClasses);

        // sunday classes
        DefaultMutableTreeNode sundayClasses = new DefaultMutableTreeNode("Sunday Classes");
        for (datastructures.festival.Class sunClass : m_festival.getClasses(FestivalDay.SUNDAY))
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

        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            builder.append("($").append(specialPass.cost).append("/$").append(specialPass.partnerCost).append(") ");
            builder.append(specialPass);
            return builder.toString();
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
