package classes;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import datastructures.AccountInformation;
import datastructures.Constants;
import datastructures.Enums.RegistrationMode;
import datastructures.Enums.SheetClientMode;
import datastructures.Registrant;
import datastructures.RegistrationWorksheet;

/**
 * This class provides the interface to accessing and performing
 * operations on the spreadsheet containing the registration
 * data. Some operations supported are:
 * <ol>
 * 	<li>Getting a list of completed registrations</li>
 * 	<li>Getting a list of completed registrations, but whose e-ticket has
 * 		not been sent</li>
 * 	<li>Adding on-site registrations</li>
 * </ol>
 * 
 * The default registration mode of the SheetClient is EARLY. This is 
 * because the late registration spreadsheet may not have been made yet.
 * 
 * @author benjamyn
 *
 */
public class SheetClient {
	private AccountInformation ai;
	private SpreadsheetService service;
	private WorksheetEntry wEntries[];
	private FeedURLFactory factory; // generates the appropriate feed URLs
	private SheetClientMode mode;
	private RegistrationWorksheet regWorksheet; // object to parse and retrieve information from cell feeds
	private RegistrationMode regisMode; // early or late registration
	
	/**
	 * While the sheet mode is set, the SheetClient must be refreshed
	 * to get the spreadsheet information. See {@link #refresh()}.
	 * 
	 * @param ai
	 * @param scMode
	 */
	public SheetClient (AccountInformation ai, SheetClientMode scMode, RegistrationMode regisMode) {
		this.ai = ai;
		this.regisMode = regisMode;
		
		service = new SpreadsheetService ("Terrapin Tango Festival");
		factory = FeedURLFactory.getDefault();
		
		login ();

		// get spreadsheet
		SpreadsheetEntry spreadsheet = getSpreadsheet ();
		if (spreadsheet == null)
			throw new AtcErr ("Could not locate spreadsheet: " + ai.spreadsheets[regisMode.ordinal ()]);
			
		// get formsite worksheet
		WorksheetEntry formsiteWorksheet = getWorksheetEntry (spreadsheet, ai.formsiteWkShtTitles[regisMode.ordinal ()]);
		if (formsiteWorksheet == null)
			throw new AtcErr ("Could not locate worksheet: " + ai.formsiteWkShtTitles[regisMode.ordinal ()]);
		
		// get onsite worksheet	
		WorksheetEntry onsiteWorksheet = getWorksheetEntry (spreadsheet, ai.onsiteWkShtTitles[regisMode.ordinal ()]);
		if (onsiteWorksheet == null)
			throw new AtcErr ("Could not locate worksheet: " + ai.onsiteWkShtTitles[regisMode.ordinal ()]);

		wEntries = new WorksheetEntry [2];
		wEntries [SheetClientMode.FORM_SITE.ordinal()] = formsiteWorksheet;
		wEntries [SheetClientMode.ON_SITE.ordinal()] = onsiteWorksheet;
		
		this.mode = scMode;
		
		regWorksheet = new RegistrationWorksheet ();
	}
	
	public SheetClientMode getMode() {
		return mode;
	}

	/**
	 * This function updates the local spreadsheet information for
	 * the given mode (even if it is the same as the current mode)
	 * 
	 * @param mode
	 */
	public void setMode(SheetClientMode mode) {
		this.mode = mode;
		refresh ();
	}
	
	/**
	 * This function updates the local spreadsheet information for
	 * the given registration mode (even if it is the same as the 
	 * current mode). The local information is refreshed and updated.
	 * 
	 * @param regMode registration mode to set the sheet client to
	 */
	public void setRegistrationMode(RegistrationMode regMode) {
		this.regisMode = regMode;
		
		// get spreadsheet
		SpreadsheetEntry spreadsheet = getSpreadsheet ();
		if (spreadsheet == null)
			throw new AtcErr ("Could not locate spreadsheet: " + ai.spreadsheets[regisMode.ordinal ()]);
			
		// get formsite worksheet
		WorksheetEntry formsiteWorksheet = getWorksheetEntry (spreadsheet, ai.formsiteWkShtTitles[regisMode.ordinal ()]);
		if (formsiteWorksheet == null)
			throw new AtcErr ("Could not locate worksheet: " + ai.formsiteWkShtTitles[regisMode.ordinal ()]);
		
		// get onsite worksheet	
		WorksheetEntry onsiteWorksheet = getWorksheetEntry (spreadsheet, ai.onsiteWkShtTitles[regisMode.ordinal ()]);
		if (onsiteWorksheet == null)
			throw new AtcErr ("Could not locate worksheet: " + ai.onsiteWkShtTitles[regisMode.ordinal ()]);

		wEntries [SheetClientMode.FORM_SITE.ordinal()] = formsiteWorksheet;
		wEntries [SheetClientMode.ON_SITE.ordinal()] = onsiteWorksheet;
		
		refresh();
	}
	
	private void login () {
		try {
			service.setUserCredentials(ai.userName, ai.passwd);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
	}

	private SpreadsheetEntry getSpreadsheet () {
		try {
			SpreadsheetFeed feed = service.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
			List<SpreadsheetEntry> spreadsheets = feed.getEntries();
			
			// try and find the spreadsheet with the given title
			for (SpreadsheetEntry sheet : spreadsheets)
				if (sheet.getTitle().getPlainText().trim().compareTo(ai.spreadsheets[regisMode.ordinal ()]) == 0)
					return sheet;
			
		} catch (IOException | ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private WorksheetEntry getWorksheetEntry (SpreadsheetEntry spreadsheet, String worksheetTitle) {
		List<WorksheetEntry> worksheets = null;
		
		try {
			worksheets = spreadsheet.getWorksheets();
		} catch (IOException | ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (WorksheetEntry we : worksheets)
			if (we.getTitle().getPlainText().trim().compareTo(worksheetTitle) == 0)
				return we;
		
		return null;
	}
	
	/* *********** END GETTING WORKSHEET / BEGIN PROCESSING WORKSHEET ************* */
	
	private CellFeed getWorksheetCellFeed (WorksheetEntry we) {
		URL url = we.getCellFeedUrl();
		CellQuery cq = new CellQuery (url);
		cq.setReturnEmpty(true); // we want empty cells
		cq.setMinimumRow (2); // first row (row 0) contains form information
		cq.setMinimumCol (Constants.CELL_FEED_FIRST_COL);
		cq.setMaximumCol (Constants.CELL_FEED_LAST_COL);
		
		CellFeed feed = null;
		
		try {
			feed = service.query (cq, CellFeed.class);
						
		} catch (IOException | ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return feed;
	}
	
	/**
	 * Refreshes the local worksheet information
	 */
	public void refresh () {
		regWorksheet.setCellFeed (getWorksheetCellFeed (wEntries [mode.ordinal()]));
	}
	
	/**
	 * Prints the contents of the input column to stdout
	 * 
	 * @param column column of the google spreadsheet to be printed
	 */
	public void printColumn (int column) {
		for (CellEntry entry : getColumn (wEntries [mode.ordinal()], column).getEntries())
			printCell (entry);
	}

	private void printCell (CellEntry cell) {
		String shortId = cell.getId().substring(cell.getId().lastIndexOf('/') + 1);
		System.out.println(" -- Cell(" + shortId + "/" + cell.getTitle().getPlainText()
				+ ") formula(" + cell.getCell().getInputValue() + ") numeric("
				+ cell.getCell().getNumericValue() + ") value("
				+ cell.getCell().getValue() + ")");
	}
	
	private void printCellSimple (CellEntry cell) {
		System.out.println (" -- Cell (R" + cell.getCell().getRow() + "C" + cell.getCell().getCol() + ")");
	}
	
	private CellFeed getColumn (WorksheetEntry we, int column) {
		URL url = we.getCellFeedUrl();
		CellQuery cq = new CellQuery (url);
		cq.setReturnEmpty(true); // we want empty cells
		cq.setMinimumRow (2); // first row (row 1) contains form information
		cq.setMinimumCol (column);
		cq.setMaximumCol (column);
		
		CellFeed feed = null;
		
		try {
			feed = service.query (cq, CellFeed.class);
						
		} catch (IOException | ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return feed;
	}
	
	/**
	 * @return list of all the registrants found in the local worksheet
	 */
	public ArrayList<Registrant> getAllRegistrants () {
		return regWorksheet.getAllRegistrants ();
	}

	/**
	 * @return list of all the paid registrants found in the local worksheet
	 */
	public ArrayList<Registrant> getPaidRegistrants () {
		return regWorksheet.getPaidRegistrants ();
	}

	/**
	 * @return list of all the unpaid registrants found in the local worksheet
	 */
	public ArrayList<Registrant> getUnpaidRegistrants () {
		return regWorksheet.getUnpaidRegistrants ();
	}
	
	/**
	 * This method is primarily for debugging purposes. It prints the
	 * contents of all the CellEntrys in the given input list
	 * 
	 * @param cel list of cell entries to be printed
	 */
	public void printCellEntryList (List<CellEntry> cel) {
		for (CellEntry ce : cel) 
			printCellSimple (ce);
	}
	
	/**
	 * Deletes the given list of registerants from the google
	 * spreadsheet. The local copy of the spreadsheet is
	 * then refereshed to reflect these changes.
	 * 
	 * @param regList list of registrants to delete
	 */
	public void deleteRegistrants (List<Registrant> regList) {
		// must delete the registrants in the reverse order
		// because deleting the lower rows will cause the higher
		// rows to be incorrect
		LinkedList<Registrant> revRegList = new LinkedList<Registrant> ();
		
		for (Registrant reg : regList)
			revRegList.push (reg);
		
		URL listFeedUrl = wEntries [mode.ordinal ()].getListFeedUrl ();
		ListFeed listFeed = null;
		// Note that ListFeed's make assumptions on how the data is 
		// laid out in the spreadsheet. Namely, that the first row of 
		// the spreadsheet is a "header row", which does not get
		// returned in the ListFeed's entries. We need to take this
		// into consideration when figuring out which index to delete.
		
		try {
			listFeed = service.getFeed (listFeedUrl, ListFeed.class);
		} catch (IOException | ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Registrant reg : revRegList)
			try {
				listFeed.getEntries ().get (reg.row - 2).delete ();
				// subtract 1 for index of a List starting at 0 while first row of spreadsheet is 1
				// subtract 1 for the lack of the first row being returned
			} catch (IOException | ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/**
	 * @param regs list of registrants to mark as paid in the google spreadsheet
	 */
	public void markAsPaid (List<Registrant> regs) {
		changeContents (regs, getColumn (wEntries[mode.ordinal ()], Constants.PAYMENT_RECEIVED_COL).getEntries (), Constants.PAYMENT_RECEIVED);
	}

	/**
	 * @param regs list of registrants to mark as unpaid in the google spreadsheet
	 */
	public void markAsUnpaid (List<Registrant> regs) {
		changeContents (regs, getColumn (wEntries[mode.ordinal ()], Constants.PAYMENT_RECEIVED_COL).getEntries (), "");
	}

	/**
	 * @param regs list of registrants whose e-ticket is to be marked as sent in the google spreadsheet
	 */
	public void markAsETicketSent (List<Registrant> regs) {
		// change contents of the eticket cells
		changeContents (regs, getColumn (wEntries[mode.ordinal ()], Constants.ETICKET_COL).getEntries (), Constants.ETICKET_SENT);
	}
	
	private void changeContents (List<Registrant> regs, List<CellEntry> ces, String newContents) {
		try {
			for (Registrant reg : regs) {
				CellEntry ce = ces.get (reg.row - 2); // list starts at 0, row starts at 1 & column query does not include row 1
				ce.changeInputValueLocal (newContents);
				ce.update ();
			}
		} catch (IOException | ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a new registrant to the current worksheet, which is determined by the
	 * SheetClient's mode ({@link datastructures.Enums.SheetClientMode}). The new registrant is
	 * inserted into a new row at the end of the worksheet.
	 * 
	 * @param reg registrant to add to the google spreadsheet
	 */
	public void pushNewRegistrant (Registrant reg) {
		// get url for submitting changes
		URL listFeedUrl = wEntries [mode.ordinal ()].getListFeedUrl ();
		ListFeed listFeed = null;
		
		try {
			listFeed = service.getFeed (listFeedUrl,  ListFeed.class);
		} catch (IOException | ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// from the listfeed, we can retrieve the header row tags
		//    while we have a Set of tags instead of a list, the order
		//    is preserved (see CustomElementCollection documentation)
		Set<String> tags = listFeed.getEntries ().get (0).getCustomElements ().getTags ();
		
		// create an actual list out of them
		ArrayList<String> tagList = new ArrayList<String> (tags);
		
		// create a row and add the appropriate data
		ListEntry row = new ListEntry ();
		row.getCustomElements ().setValueLocal (tagList.get (Constants.FIRST_NAME_COL - 1), reg.name.first);
		row.getCustomElements ().setValueLocal (tagList.get (Constants.LAST_NAME_COL - 1), reg.name.last);
		row.getCustomElements ().setValueLocal (tagList.get (Constants.EMAIL_COL - 1), reg.email);
		row.getCustomElements ().setValueLocal (tagList.get (Constants.PHONE_COL - 1), reg.phone);
		row.getCustomElements ().setValueLocal (tagList.get (Constants.STUDENT_TYPE_COL - 1), reg.studentType.toString ());
		row.getCustomElements ().setValueLocal (tagList.get (Constants.DANCER_TYPE_COL - 1), reg.dancerType.toString ());
		row.getCustomElements ().setValueLocal (tagList.get (Constants.EXP_LVL_COL - 1), reg.expLvl.toString ());
		row.getCustomElements ().setValueLocal (tagList.get (Constants.AMOUNT_COL - 1), String.format ("$%02.2f", reg.amount));
		
		// add classes
		ArrayList<String> regClasses = reg.getFilteredClasses ();
		for (int i = 0; i < regClasses.size (); ++i)
			row.getCustomElements ().setValueLocal (tagList.get (Constants.CLASS_MIN_COL - 1 + i), regClasses.get (i));
		
		// mark if paid/eticket sent
		if (reg.hasPaid ())
			row.getCustomElements ().setValueLocal (tagList.get (Constants.PAYMENT_RECEIVED_COL - 1), Constants.PAYMENT_RECEIVED);
		if (reg.hasEticketSent ())
			row.getCustomElements ().setValueLocal (tagList.get (Constants.ETICKET_COL - 1), Constants.ETICKET_SENT);

		// update the spreadsheet, adding the new row
		try {
			row = service.insert (listFeedUrl, row);
		} catch (IOException | ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return number of rows in the google spreadsheet
	 */
	public int getNumWorksheetRows () {
		return wEntries [mode.ordinal ()].getRowCount ();
	}
}
