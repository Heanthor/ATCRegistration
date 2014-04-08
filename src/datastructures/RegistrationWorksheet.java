package datastructures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;

/**
 * This class abstracts all the work in getting the various information
 * types from a CellFeed, which is returned from requesting information
 * from the google document. The CellFeed should contain the entire
 * worksheet, including empty cells.
 * <p/>
 * Through this class, a user is able to get whatever column of information
 * they desire. When a new CellFeed is provided to the class, all the
 * columns are updated.
 * <p/>
 * To use <u>ANY</u> methods, you need to set the CellFeed to use, which
 * provides the class with the information concerning all the registrants.
 * This can be done using the {@link #RegistrationWorksheet(CellFeed)}
 * constructor or by calling {@link #setCellFeed(CellFeed)}.
 *
 * @author benjamyn
 */
public class RegistrationWorksheet
{
    private CellFeed cf;

    // yes, there's a lot of these.
    private ArrayList<CellEntry> amountCol;
    private ArrayList<CellEntry> firstNameCol;
    private ArrayList<CellEntry> lastNameCol;
    private ArrayList<CellEntry> emailCol;
    private ArrayList<CellEntry> phoneCol;
    private ArrayList<CellEntry> studentTypeCol;
    private ArrayList<CellEntry> dancerTypeCol;
    private ArrayList<CellEntry> expLvlCol;
    private ArrayList<CellEntry> completedRegCol;
    private ArrayList<CellEntry> paymentRcvdCol;
    private ArrayList<CellEntry> eticketCol;
    private ArrayList<CellEntry> numRegistrantsCol;
    private ArrayList<CellEntry> secondRegFirstNameCol;
    private ArrayList<CellEntry> secondRegLastNameCol;

    // this one is a bit different. It is used for querying the
    // classes for a specific row
    //
    // the index in the top ArrayList is the row number, which contains
    // a list of classes
    private ArrayList<ArrayList<CellEntry>> classRows;

    public RegistrationWorksheet()
    {
        amountCol = new ArrayList<>();
        firstNameCol = new ArrayList<>();
        lastNameCol = new ArrayList<>();
        emailCol = new ArrayList<>();
        phoneCol = new ArrayList<>();
        studentTypeCol = new ArrayList<>();
        dancerTypeCol = new ArrayList<>();
        expLvlCol = new ArrayList<>();
        completedRegCol = new ArrayList<>();
        paymentRcvdCol = new ArrayList<>();
        eticketCol = new ArrayList<>();
        numRegistrantsCol= new ArrayList<>();
        secondRegFirstNameCol = new ArrayList<>();
        secondRegLastNameCol = new ArrayList<>();
    }

    /**
     * Performs initialization and parses the given CellFeed
     * to start with registration information
     *
     * @param cf CellFeed to initially parse
     */
    public RegistrationWorksheet(CellFeed cf)
    {
        this();
        setCellFeed(cf);
    }

    public void setCellFeed(CellFeed cf)
    {
        this.cf = cf;
        parseCellFeed();
    }

    /**
     * Parses the CellFeed, filling in the appropriate lists
     */
    private void parseCellFeed()
    {
        Iterator<CellEntry> cfIter = cf.getEntries().iterator();
        classRows = new ArrayList<ArrayList<CellEntry>>();

        // resize ArrayList's so we don't have to later
        int numRows = cf.getRowCount();
        amountCol.clear();
        amountCol.ensureCapacity(numRows);
        firstNameCol.clear();
        firstNameCol.ensureCapacity(numRows);
        lastNameCol.clear();
        lastNameCol.ensureCapacity(numRows);
        emailCol.clear();
        emailCol.ensureCapacity(numRows);
        phoneCol.clear();
        phoneCol.ensureCapacity(numRows);
        studentTypeCol.clear();
        studentTypeCol.ensureCapacity(numRows);
        dancerTypeCol.clear();
        dancerTypeCol.ensureCapacity(numRows);
        expLvlCol.clear();
        expLvlCol.ensureCapacity(numRows);
        completedRegCol.clear();
        completedRegCol.ensureCapacity(numRows);
        paymentRcvdCol.clear();
        paymentRcvdCol.ensureCapacity(numRows);
        eticketCol.clear();
        eticketCol.ensureCapacity(numRows);
        numRegistrantsCol.clear();
        numRegistrantsCol.ensureCapacity(numRows);
        secondRegFirstNameCol.clear();
        secondRegFirstNameCol.ensureCapacity(numRows);
        secondRegLastNameCol.clear();
        secondRegLastNameCol.ensureCapacity(numRows);
        classRows.clear();
        classRows.ensureCapacity(numRows);

        CellEntry entry = null;
        if (cfIter.hasNext())
            entry = cfIter.next();

        while (cfIter.hasNext())
        {
            // another registrant to process
            ArrayList<CellEntry> classes = new ArrayList<CellEntry>();
            int currentRow = entry.getCell().getRow();

            do
            {
                int currentCol = entry.getCell().getCol();

                switch (currentCol)
                {
                case Constants.AMOUNT_COL:
                    amountCol.add(entry);
                    break;
                case Constants.FIRST_NAME_COL:
                    firstNameCol.add(entry);
                    break;
                case Constants.LAST_NAME_COL:
                    lastNameCol.add(entry);
                    break;
                case Constants.EMAIL_COL:
                    emailCol.add(entry);
                    break;
                case Constants.PHONE_COL:
                    phoneCol.add(entry);
                    break;
                case Constants.STUDENT_TYPE_COL:
                    studentTypeCol.add(entry);
                    break;
                case Constants.DANCER_TYPE_COL:
                    dancerTypeCol.add(entry);
                    break;
                case Constants.EXP_LVL_COL:
                    expLvlCol.add(entry);
                    break;
                case Constants.COMPLETED_REGISTRANT_COL:
                    completedRegCol.add(entry);
                    break;
                case Constants.PAYMENT_RECEIVED_COL:
                    paymentRcvdCol.add(entry);
                    break;
                case Constants.ETICKET_COL:
                    eticketCol.add(entry);
                    break;
                case Constants.NUMBER_REGISTRANTS_COL:
                    numRegistrantsCol.add(entry);
                case Constants.SECOND_REGISTRANT_FIRST_NAME_COL:
                    secondRegFirstNameCol.add(entry);
                case Constants.SECOND_REGISTRANT_LAST_NAME_COL:
                    secondRegLastNameCol.add(entry);
                default:
                    // check if in class range
                    if (Constants.CLASS_MIN_COL <= currentCol && currentCol <= Constants.CLASS_MAX_COL)
                        classes.add(entry);
                    break;
                }

                if (!cfIter.hasNext())
                    break;
                else
                    entry = cfIter.next();
            }
            while (entry.getCell().getRow() == currentRow);

            // add ArrayList of classes
            classRows.add(classes);
        }
    }

    /**
     * Interface used to determine whether a payment has been or has not been received based on the
     * input String.
     *
     * @author benjamyn
     */
    private interface PaymentParser
    {
        public boolean shouldAddRegistrant(String paymentString);
    }

    /**
     * Returns a list of registrants based on the given PaymentParser which
     * determines whether a registrant should be added to the list based
     * on whether their payment has been received or not.
     *
     * @param pp PaymentParser object to filter registrants based on if their payment has been received
     * @return list of registrants that passed through the filter
     */
    private ArrayList<Registrant> getRegistrantsBasedOnPayment(PaymentParser pp)
    {
        ArrayList<Registrant> regs = new ArrayList<>(amountCol.size());

        int numRegs = amountCol.size();
        for (int i = 0; i < numRegs; ++i)
        {
            String eticketSentEntry = eticketCol.get(i).getCell().getInputValue();

            if (pp.shouldAddRegistrant(eticketSentEntry))
            {
                String amntString = amountCol.get(i).getCell().getInputValue();
                double amnt = 0;
                if (amntString != null && amntString.compareTo("") != 0)
                {
                    amnt = Double.parseDouble(amntString.replace("$", ""));
                }

                String eticketString = eticketCol.get(i).getCell().getInputValue();
                boolean isEticketSent = eticketString != null && eticketString.compareTo(Constants.ETICKET_SENT) == 0;

                List<String> classes = new LinkedList<>();
                for (CellEntry ce : classRows.get(i))
                {
                    String regClass = ce.getCell().getInputValue();

                    // could have empty cells
                    if (regClass != null && regClass.trim().compareTo("") != 0)
                        classes.add(regClass);
                }

                // payment and eticket sent has a default value of <false>
                Registrant reg = new Registrant(amountCol.get(i).getCell().getRow(),
                                                firstNameCol.get(i).getCell().getInputValue(),
                                                lastNameCol.get(i).getCell().getInputValue(),
                                                emailCol.get(i).getCell().getInputValue(),
                                                phoneCol.get(i).getCell().getInputValue(),
                                                Enums.stringToStudentType(studentTypeCol.get(i).getCell().getInputValue()),
                                                Enums.stringToDancerType(dancerTypeCol.get(i).getCell().getInputValue()),
                                                Enums.stringToExperienceLevel(expLvlCol.get(i).getCell().getInputValue()),
                                                amnt,
                                                Integer.parseInt(numRegistrantsCol.get(i).getCell().getInputValue()),
                                                classes,
                                                isEticketSent,
                                                secondRegFirstNameCol.get(i).getCell().getInputValue(),
                                                secondRegLastNameCol.get(i).getCell().getInputValue());
                regs.add(reg);
            }
        }

        return regs;
    }

    /**
     * Returns all the registrants that are found in the current
     * CellFeed
     *
     * @return all the registrants that are found in the current CellFeed
     */
    public ArrayList<Registrant> getAllRegistrants()
    {
        return getRegistrantsBasedOnPayment(new PaymentParser()
                                            {
                                                public boolean shouldAddRegistrant(String paymentString)
                                                {
                                                    return true;
                                                }
                                            }
        );
    }

    /**
     * Returns all the paid registrants that are found in the current
     * CellFeed by checking if the eticket is sent
     *
     * @return all the paid registrants that are found in the current CellFeed
     */
    public ArrayList<Registrant> getPaidRegistrants()
    {
        return getRegistrantsBasedOnPayment(new PaymentParser()
                                            {
                                                public boolean shouldAddRegistrant(String eticketCellEntry)
                                                {
                                                    return eticketCellEntry != null && eticketCellEntry.compareTo(Constants.ETICKET_SENT) == 0;
                                                }
                                            }
        );
    }

    /**
     * Returns all the unpaid registrants that are found in the current
     * CellFeed
     *
     * @return all the unpaid registrants that are found in the current CellFeed
     */
    public ArrayList<Registrant> getUnpaidRegistrants()
    {
        return getRegistrantsBasedOnPayment(new PaymentParser()
                                            {
                                                public boolean shouldAddRegistrant(String eticketCellEntry)
                                                {
                                                    return eticketCellEntry == null || eticketCellEntry.compareTo(Constants.ETICKET_SENT) != 0;
                                                }
                                            }
        );
    }
}
