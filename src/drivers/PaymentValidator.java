package drivers;

import java.util.ArrayList;
import java.util.List;

import classes.ETicketMaker;
import classes.Emailer;
import classes.RegistrationGUI;
import classes.SheetClient;
import datastructures.AccountInformation;
import datastructures.Constants;
import datastructures.Enums.SheetClientMode;
import datastructures.Registrant;

/**
 * This driver is used to validate payments by a user. Registrants
 * can have their payments validated and an e-ticket sent out
 * all from this single application. This driver essentially acts
 * as the glue between the Registration GUI class and the
 * SheetClient class.
 * 
 * There are three modes to the validator to ease validating the
 * customers by hand:
 * <ul>
 * 	<li>Unpaid registrants</li>
 * 	<li>Paid registrants</li>
 * 	<li>All registrants</li>
 * </ul>
 * 
 * This program also has other features:
 * <ol>
 * 	<li>enables the user to delete entries (i.e. rows)
 * 			in the spreadsheet for clean-up.</li>
 * 	<li>retrieve a summary of the festival information
 * 		concerning the registrants</li>
 * </ol>
 * 
 * @author benjamyn
 *
 */
public class PaymentValidator {
	private volatile AccountInformation ai;
	private volatile SheetClient sc;
	private volatile RegistrationGUI gui;
	
	private volatile Emailer emailer;
	private volatile ETicketMaker eticketMaker;
	
	public static void main (String args[]) {
		PaymentValidator pv = new PaymentValidator (Constants.FESTIVAL_USER_INFORMATION_FILE);
		RegistrationGUI gui = new RegistrationGUI (pv);
		
		pv.setGUI (gui);
		pv.showAllRegistrants ();
	}
	
	public PaymentValidator (String userInfoFile) {
		ai = new AccountInformation (userInfoFile);
		sc = new SheetClient (ai, SheetClientMode.FORM_SITE);
		sc.refresh ();
		gui = null;
		
		emailer = new Emailer (ai.userName, ai.passwd);
		eticketMaker = new ETicketMaker (Constants.ETICKET_FILE, "etickets");
	}
	
	/**
	 * Sets the GUI that the validator should interact with when
	 * calling methods to update the GUI's interface
	 * 
	 * @param gui
	 */
	public void setGUI (RegistrationGUI gui) {
		this.gui = gui;
	}
	
	/**
	 * Interface that should be implemented for any filtering done
	 * on a list of {@see Registrant} objects.
	 * 
	 * The internal string to filter on is stored in lower case.
	 * 
	 * @author benjamyn
	 *
	 */
	private abstract class StringFilter {
		protected final String filterString;
		
		public StringFilter (String filterString) {
			this.filterString = filterString.toLowerCase ();
		}
		
		public abstract boolean filterString (String s);
	}
	
	/**
	 * Filters the list of input registrants based on the input
	 * parts of the first and last name. These parts can occur
	 * anywhere in the name i.e. we are looking for matching
	 * substrings.
	 * 
	 * @param firstName part of first name to filter on
	 * @param lastName part of last name to filter on
	 * @param regList list of registrants to apply filter
	 */
	public void filterRegistrantList (String firstName, String lastName, List<Registrant> regList) {
		 StringFilter firstNameFilter = getStringFilter (firstName);
		 StringFilter lastNameFilter = getStringFilter (lastName);
		 
		 ArrayList<Registrant> filteredRegList = new ArrayList<Registrant> ();
		 
		 for (Registrant reg : regList)
			 if (firstNameFilter.filterString (reg.name.first) && lastNameFilter.filterString (reg.name.last))
				 filteredRegList.add (reg);
		 
		 gui.setShownRegistrantList (filteredRegList);
	}
	
	private StringFilter getStringFilter (String s) {
		return (s == null || s.compareTo ("") == 0) ? 
				 new StringFilter (s) { public boolean filterString (String s) {return true; } } :
				 new StringFilter (s) { public boolean filterString (String s) {return s.toLowerCase ().contains (filterString); }};
	}
	
	public void showAllRegistrants () {
		gui.setRegistrantList(sc.getAllRegistrants());
	}
	
	public void showPaidRegistrants () {
		gui.setRegistrantList(sc.getPaidRegistrants());
	}
	
	public void showUnpaidRegistrants () {
		gui.setRegistrantList(sc.getUnpaidRegistrants());
	}
	
	/**
	 * delete the given list of registrants
	 * 
	 * @param regList list of registrants to delete
	 */
	public void deleteRegistrants (List<Registrant> regList) {
		sc.deleteRegistrants (regList);
		refresh ();
	}
	
	/**
	 * Shows registrant information for each of the registrants in the
	 * given input list
	 * 
	 * @param regList list of registrants for which to show information for
	 */
	public void listRegistrantClasses (List<Registrant> regList) {
		gui.showRegistrantInformation (regList);
	}
	
	public void markAsPaid (List<Registrant> regList) {
		sc.markAsPaid (regList);
		refresh (); // refresh our local information 
	}
	
	public void markAsUnpaid (List<Registrant> regList) {
		sc.markAsUnpaid (regList);
		refresh (); // refresh our local information 
	}
	
	public void sendETickets (List<Registrant> regList) {
		// make and send etickets
		for (Registrant reg : regList) {
			// remove the "None" and empty string classes
			ArrayList<String> trueClasses = new ArrayList<String> ();
			for (String regClass : reg.getFilteredClasses ())
				trueClasses.add (regClass);
			
			// create ticket
			String eTicketFile = eticketMaker.createTicket (reg.name, reg.studentType, trueClasses);
			if (null == eTicketFile) {
				System.err.println("Could not create an e-ticket");
				return;
			}
			
			// create email
			emailer.resetEmail ();
			emailer.setSubjectLine (Constants.EMAIL_ETICKET_SUBJECT);
			emailer.setBodyFile (Constants.EMAIL_ETICKET_BODY_FILE);
			emailer.addRecipient (reg.email);
//			emailer.addRecipient ("benjamyn.ward@gmail.com");
			emailer.addAttachment (eTicketFile);
			emailer.sendEmail ();
		}
		
		// mark etickets in spreadsheet as sent
		sc.markAsETicketSent (regList);
		refresh ();
	}
	
	/**
	 * shows a summary in the GUI of some statistics for all the registrants 
	 */
	public void showSummary () {
		ArrayList<Registrant> regs = sc.getAllRegistrants ();
		
		int numPaid = 0;
		int numUnpaid = 0;
		int numPaidETicketSent = 0;
		int numPaidETicketUnsent = 0;
		
		int numPaidLeaders = 0;
		int numUnpaidLeaders = 0;
		int numPaidFollowers = 0;
		int numUnpaidFollowers = 0;
		
		int numPaidBegs = 0;
		int numUnpaidBegs = 0;
		int numPaidInts = 0;
		int numUnpaidInts = 0;
		int numPaidAdvs = 0;
		int numUnpaidAdvs = 0;
		
		int numPaidUmdStud = 0;
		int numPaidOthStud = 0;
		int numPaidGenAdm = 0;		
		int numUnpaidUmdStud = 0;
		int numUnpaidOthStud = 0;
		int numUnpaidGenAdm = 0;
		
		// populate statistics
		for (Registrant reg : regs) {
			if (reg.hasPaid ()) {
				++numPaid;
				
				// must have paid to send an e-ticket
				if (reg.hasEticketSent ())
					++numPaidETicketSent;
				else
					++numPaidETicketUnsent;
				
				switch (reg.dancerType) {
					case FOLLOWER:
						++numPaidFollowers;
						break;
					case LEADER:
						++numPaidLeaders;
						break;
				}
				
				switch (reg.expLvl) {
					case BEGINNER:
						++numPaidBegs;
						break;
					case INTERMEDIATE:
						++numPaidInts;
						break;
					case ADVANCED:
						++numPaidAdvs;
						break;
				}
				
				switch (reg.studentType) {
					case UMD_GMU_STUDENT:
						++numPaidUmdStud;
						break;
					case OTHER_STUDENT:
						++numPaidOthStud;
						break;
					case GENERAL_ADMISSION:
						++numPaidGenAdm;
						break;
				}
			}
			else {
				++numUnpaid;
				
				switch (reg.dancerType) {
					case FOLLOWER:
						++numUnpaidFollowers;
						break;
					case LEADER:
						++numUnpaidLeaders;
						break;
				}
				
				switch (reg.expLvl) {
					case BEGINNER:
						++numUnpaidBegs;
						break;
					case INTERMEDIATE:
						++numUnpaidInts;
						break;
					case ADVANCED:
						++numUnpaidAdvs;
						break;
				}
				
				switch (reg.studentType) {
					case UMD_GMU_STUDENT:
						++numUnpaidUmdStud;
						break;
					case OTHER_STUDENT:
						++numUnpaidOthStud;
						break;
					case GENERAL_ADMISSION:
						++numUnpaidGenAdm;
						break;
				}
			}
		}
		String indent = "    ";
	
		String sumString_1 = String.format ("%-35s%4d\n\n", "Total registrants: ", regs.size ());
		String sumString_2 = String.format ("%-35s%4d\n", "Unpaid registrants: ", numUnpaid);
		String sumString_3 = String.format ("%-35s%4d\n", "Paid registrants: ", numPaid);
		String sumString_4 = String.format ("%-35s%4d\n", indent + "Paid, ETicket not sent: ", numPaidETicketUnsent);
		String sumString_5 = String.format ("%-35s%4d\n\n", indent + "Paid, ETicket sent: ", numPaidETicketSent);
		
		String sumString_6 = String.format ("%-35s%4d\n", "Number leaders: ", numPaidLeaders + numUnpaidLeaders);
		String sumString_7 = String.format ("%-35s%4d\n", indent + "Paid: ", numPaidLeaders);
		String sumString_8 = String.format ("%-35s%4d\n", indent + "Unpaid: ", numUnpaidLeaders);
		
		String sumString_9 = String.format ("%-35s%4d\n", "Number followers: ", numPaidFollowers + numUnpaidFollowers);
		String sumString_10 = String.format ("%-35s%4d\n", indent + "Paid: ", numPaidFollowers);
		String sumString_11 = String.format ("%-35s%4d\n\n", indent + "Unpaid: ", numUnpaidFollowers);

		String sumString_12 = String.format ("%-35s%4d\n", "Number beginners: ", numPaidBegs + numUnpaidBegs);
		String sumString_13 = String.format ("%-35s%4d\n", indent + "Paid: ", numPaidBegs);
		String sumString_14 = String.format ("%-35s%4d\n", indent + "Unpaid: ", numUnpaidBegs);

		String sumString_15 = String.format ("%-35s%4d\n", "Number intermediates: ", numPaidInts + numUnpaidInts);
		String sumString_16 = String.format ("%-35s%4d\n", indent + "Paid: ", numPaidInts);
		String sumString_17 = String.format ("%-35s%4d\n", indent + "Unpaid: ", numUnpaidInts);

		String sumString_18 = String.format ("%-35s%4d\n", "Number advanced: ", numPaidAdvs + numUnpaidAdvs);
		String sumString_19 = String.format ("%-35s%4d\n", indent + "Paid: ", numPaidAdvs);
		String sumString_20 = String.format ("%-35s%4d\n\n", indent + "Unpaid: ", numUnpaidAdvs);

		String sumString_21 = String.format ("%-35s%4d\n", "Number UMD/GMU student: ", numPaidUmdStud + numUnpaidUmdStud);
		String sumString_22 = String.format ("%-35s%4d\n", indent + "Paid: ", numPaidUmdStud);
		String sumString_23 = String.format ("%-35s%4d\n", indent + "Unpaid: ", numUnpaidUmdStud);

		String sumString_24 = String.format ("%-35s%4d\n", "Number other student: ", numPaidOthStud + numUnpaidOthStud);
		String sumString_25 = String.format ("%-35s%4d\n", indent + "Paid: ", numPaidOthStud);
		String sumString_26 = String.format ("%-35s%4d\n", indent + "Unpaid: ", numUnpaidOthStud);

		String sumString_27 = String.format ("%-35s%4d\n", "Number general admission: ", numPaidGenAdm + numUnpaidGenAdm);
		String sumString_28 = String.format ("%-35s%4d\n", indent + "Paid: ", numPaidGenAdm);
		String sumString_29 = String.format ("%-35s%4d\n\n", indent + "Unpaid: ", numUnpaidGenAdm);

		gui.showSummary (sumString_1 + sumString_2 + sumString_3 + sumString_4 + sumString_5 + sumString_6 + sumString_7 + 
							sumString_8 + sumString_9 + sumString_10 + sumString_11 + sumString_12 + sumString_13 +
							sumString_14 + sumString_15 + sumString_16 + sumString_17 + sumString_18 + sumString_19 + sumString_20 + 
							sumString_21 + sumString_22 + sumString_23 + sumString_24 + sumString_25 + sumString_26 + sumString_27 + 
							sumString_28 + sumString_29
							);
	}
	
	/**
	 * Refreshes the local spreadsheet information and refreshes
	 * the GUI to contain this information
	 */
	public void refresh () {
		sc.refresh ();
		gui.clearPanels ();
		gui.setRegistrantList(sc.getAllRegistrants());
	}
	
	/**
	 * Changes the spreadsheet mode, refreshing the local spreadsheet
	 * information and refreshes the GUI to contain this information
	 * 
	 * @param mode new mode for the spreadsheet client
	 */
	public void changeSheetClientMode (SheetClientMode mode) {
		sc.setMode (mode);
		refresh ();
	}
}
