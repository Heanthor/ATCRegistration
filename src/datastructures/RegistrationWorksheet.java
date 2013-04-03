package datastructures;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;

/**
 * This class abstracts all the work in getting the various information
 * types from a CellFeed, which is returned from requesting information
 * from the google document. The CellFeed should contain the entire
 * worksheet, including empty cells.
 * 
 * Through this class, a user is able to get whatever column of information
 * they desire. When a new CellFeed is provided to the class, all the
 * columns are updated.
 * 
 * To use <u>ANY</u> methods, you need to set the CellFeed to use, which
 * provides the class with the information concerning all the registrants.
 * This can be done using the {@link #RegistrationWorksheet(CellFeed)} 
 * constructor or by calling {@link #setCellFeed(CellFeed)}.
 * 
 * @author benjamyn
 *
 */
public class RegistrationWorksheet {
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
	
	// this one is a bit different. It is used for querying the
	// classes for a specific row
	//
	// the index in the top ArrayList is the row number, which contains
	// a list of classes
	private ArrayList<ArrayList<CellEntry>> classRows;
	
	public RegistrationWorksheet () {
		amountCol = new ArrayList<CellEntry> ();
		firstNameCol = new ArrayList<CellEntry> ();
		lastNameCol = new ArrayList<CellEntry> ();
		emailCol = new ArrayList<CellEntry> ();
		phoneCol = new ArrayList<CellEntry> ();
		studentTypeCol = new ArrayList<CellEntry> ();
		dancerTypeCol = new ArrayList<CellEntry> ();
		expLvlCol = new ArrayList<CellEntry> ();
		completedRegCol = new ArrayList<CellEntry> ();
		paymentRcvdCol = new ArrayList<CellEntry> ();
		eticketCol = new ArrayList<CellEntry> ();
	}
	
	/**
	 * Performs initialization and parses the given CellFeed
	 * to start with registration information
	 * 
	 * @param cf CellFeed to initially parse
	 */
	public RegistrationWorksheet (CellFeed cf) {
		this ();
		setCellFeed (cf);
	}
	
	public void setCellFeed (CellFeed cf) {
		this.cf = cf;
		parseCellFeed ();
	}
	
	/**
	 * Parses the CellFeed, filling in the appropriate lists 
	 */
	private void parseCellFeed () {
		Iterator<CellEntry> cfIter = cf.getEntries ().iterator ();
		classRows = new ArrayList <ArrayList<CellEntry>> ();
		
		// resize ArrayList's so we don't have to later
		int numRows = cf.getRowCount ();
		amountCol.clear ();
		amountCol.ensureCapacity (numRows);
		firstNameCol.clear ();
		firstNameCol.ensureCapacity (numRows);
		lastNameCol.clear ();
		lastNameCol.ensureCapacity (numRows);
		emailCol.clear ();
		emailCol.ensureCapacity (numRows);
		phoneCol.clear ();
		phoneCol.ensureCapacity (numRows);
		studentTypeCol.clear ();
		studentTypeCol.ensureCapacity (numRows);
		dancerTypeCol.clear ();
		dancerTypeCol.ensureCapacity (numRows);
		expLvlCol.clear ();
		expLvlCol.ensureCapacity (numRows);
		completedRegCol.clear ();
		completedRegCol.ensureCapacity (numRows);
		paymentRcvdCol.clear ();
		paymentRcvdCol.ensureCapacity (numRows);
		eticketCol.clear ();
		eticketCol.ensureCapacity (numRows);
		classRows.clear ();
		classRows.ensureCapacity (numRows);
		
		CellEntry entry = null;
		if (cfIter.hasNext ())
			entry = cfIter.next ();
		
		while (cfIter.hasNext ()) {
			// another registrant to process
			ArrayList<CellEntry> classes = new ArrayList<CellEntry> ();
			int currentRow = entry.getCell ().getRow ();
			
			do {
				int currentCol = entry.getCell ().getCol ();
				
				switch (currentCol) {
					case Constants.AMOUNT_COL:
						amountCol.add (entry);
						break;
					case Constants.FIRST_NAME_COL:
						firstNameCol.add (entry);
						break;
					case Constants.LAST_NAME_COL:
						lastNameCol.add (entry);
						break;
					case Constants.EMAIL_COL:
						emailCol.add (entry);
						break;
					case Constants.PHONE_COL:
						phoneCol.add (entry);
						break;
					case Constants.STUDENT_TYPE_COL:
						studentTypeCol.add (entry);
						break;
					case Constants.DANCER_TYPE_COL:
						dancerTypeCol.add (entry);
						break;
					case Constants.EXP_LVL_COL:
						expLvlCol.add (entry);
						break;
					case Constants.COMPLETED_REGISTRANT_COL:
						completedRegCol.add (entry);
						break;
					case Constants.PAYMENT_RECEIVED_COL:
						paymentRcvdCol.add (entry);
						break;
					case Constants.ETICKET_COL:
						eticketCol.add (entry);
						break;
					default:
						// check if in class range
						if (Constants.CLASS_MIN_COL <= currentCol && currentCol <= Constants.CLASS_MAX_COL)
							classes.add (entry);
						break;
				}
				
				if (!cfIter.hasNext ())
					break;
				else
					entry = cfIter.next ();
			} while (entry.getCell ().getRow () == currentRow);
			
			// add ArrayList of classes
			classRows.add (classes);
		}
	}
	
	/**
	 * Interface used to determine whether a payment has been or has not been received based on the
	 * input String.
	 * 
	 * @author benjamyn
	 *
	 */
	private interface PaymentParser {
		public boolean shouldAddRegistrant (String paymentString);
	}
	
	/**
	 * Returns a list of registrants based on the given PaymentParser which
	 * determines whether a registrant should be added to the list based
	 * on whether their payment has been received or not.
	 * 
	 * @param pp PaymentParser object to filter registrants based on if their payment has been received
	 * @return list of registrants that passed through the filter
	 */
	private ArrayList<Registrant> getRegistrantsBasedOnPayment (PaymentParser pp) {
		ArrayList<Registrant> regs = new ArrayList<Registrant> (amountCol.size ());
		
		int numRegs = amountCol.size ();
		for (int i = 0; i < numRegs; ++i) {
			String paymentRcvdString = paymentRcvdCol.get (i).getCell ().getInputValue ();
			
			if (pp.shouldAddRegistrant (paymentRcvdString)) {
				String amntString = amountCol.get (i).getCell ().getInputValue ();
				double amnt = 0;
				if (amntString != null && amntString.compareTo ("") != 0)
					amnt = Double.parseDouble (amntString.replace ("$", ""));
				
				// payment and eticket sent has a default value of <false>
				Registrant reg = new Registrant (
						amountCol.get (i).getCell ().getRow (),
						firstNameCol.get (i).getCell ().getInputValue (),
						lastNameCol.get (i).getCell ().getInputValue (),
						emailCol.get (i).getCell ().getInputValue (),
						phoneCol.get (i).getCell ().getInputValue (),
						Enums.stringToStudentType (studentTypeCol.get (i).getCell ().getInputValue ()),
						Enums.stringToDancerType (dancerTypeCol.get (i).getCell ().getInputValue ()),
						Enums.stringToExperienceLevel (expLvlCol.get (i).getCell ().getInputValue ()),
						amnt);
				
				if (paymentRcvdString != null && paymentRcvdString.compareTo (Constants.PAYMENT_RECEIVED) == 0)
					reg.setPaid (true);
				
				String eticketString = eticketCol.get (i).getCell ().getInputValue ();
				if (eticketString != null && eticketString.compareTo (Constants.ETICKET_SENT) == 0)
					reg.setEticketSent (true);
				
				for (CellEntry ce : classRows.get (i)) {
					String regClass = ce.getCell ().getInputValue ();
					
					// could have empty cells
					if (regClass != null && regClass.trim ().compareTo ("") != 0)
						reg.addClass (regClass);
				}
				
				regs.add (reg);
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
	public ArrayList<Registrant> getAllRegistrants () {
		return getRegistrantsBasedOnPayment (
				new PaymentParser () {
					public boolean shouldAddRegistrant (String paymentString) {
						return true;
					}
				});
	}
	
	/**
	 * Returns all the paid registrants that are found in the current
	 * CellFeed
	 * 
	 * @return all the paid registrants that are found in the current CellFeed
	 */
	public ArrayList<Registrant> getPaidRegistrants () {
		return getRegistrantsBasedOnPayment (
				new PaymentParser () {
					public boolean shouldAddRegistrant (String paymentString) {
						if (paymentString != null && paymentString.compareTo (Constants.PAYMENT_RECEIVED) == 0)
							return true;
						else
							return false;
					}
				});
	}

	/**
	 * Returns all the unpaid registrants that are found in the current
	 * CellFeed
	 * 
	 * @return all the unpaid registrants that are found in the current CellFeed
	 */
	public ArrayList<Registrant> getUnpaidRegistrants () {
		return getRegistrantsBasedOnPayment (
				new PaymentParser () {
					public boolean shouldAddRegistrant (String paymentString) {
						if (paymentString == null || paymentString.compareTo (Constants.PAYMENT_RECEIVED) != 0)
							return true;
						else
							return false;
					}
				});
	}
}
