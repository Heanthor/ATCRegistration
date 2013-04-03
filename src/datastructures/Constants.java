package datastructures;

/**
 * A class that provides a single location for all the configuration
 * options for the entire registration framework.
 * 
 * @author benjamyn
 *
 */
public class Constants {
	public static final int DAYS_IN_FESTIVAL = 3;
	public static final int NUM_STUDENT_TYPES = 3;
	
	/* SPREADSHEET COLUMN NUMBERS */
	public static final int AMOUNT_COL = 3;
	public static final int FIRST_NAME_COL = 6;
	public static final int LAST_NAME_COL = 7;
	public static final int EMAIL_COL = 8;
	public static final int PHONE_COL = 9;
	public static final int STUDENT_TYPE_COL = 10;
	public static final int DANCER_TYPE_COL = 11;
	public static final int EXP_LVL_COL = 12;
	public static final int NUMBER_REGISTRANTS_COL = 13;
	public static final int SECOND_REGISTRANT_FIRST_NAME_COL = 15;
	public static final int SECOND_REGISTRANT_LAST_NAME_COL = 16;
	public static final int SECOND_REGISTRANT_DANCER_TYPE_COL = 17;
	public static final int CLASS_MIN_COL = 25;
	public static final int CLASS_MAX_COL = 71;
	public static final int COMPLETED_REGISTRANT_COL = 74; // agreement col
	public static final int PAYMENT_RECEIVED_COL = 87;
	public static final int ETICKET_COL = 88;
	public static final String PAYMENT_RECEIVED = "x";
	public static final String ETICKET_SENT = "x";

	/* CELL FEED PARAMETERS NUMBERS */
	public static final int CELL_FEED_FIRST_COL = AMOUNT_COL;
	public static final int CELL_FEED_LAST_COL = ETICKET_COL;

	/* E-TICKET CONSTANTS */
	public static final String ETICKET_FILE = "files/tango-eticket-2013.png";
	public static final int ETICKET_FIRST_NAME_X = 105;
	public static final int ETICKET_FIRST_NAME_Y = 112;;
	public static final int ETICKET_LAST_NAME_X = 531;
	public static final int ETICKET_LAST_NAME_Y = 112;
	public static final int ETICKET_STUDENT_TYPE_X = 308;
	public static final int ETICKET_STUDENT_TYPE_Y = 157;
	public static final int ETICKET_CLASSES_X = 39;
	public static final int ETICKET_FIRST_CLASS_Y = 210;
	public static final int ETICKET_CLASS_INCREMENT_Y = 21;
	public static final float ETICKET_LARGE_FONT_SIZE = 30f;
	public static final float ETICKET_SMALL_FONT_SIZE = 20f;
	
	/* EMAIL CONSTANTS */
	public static final String EMAIL_ETICKET_SUBJECT = "Terrapin Tango Festival E-Ticket";
	public static final String EMAIL_ETICKET_BODY_FILE = "files/ticketEmail.html";
	public static final String EMAIL_PRE_FESTIVAL_BODY_FILE = "files/preFestival.html";
	public static final String EMAIL_THANK_YOU_BODY_FILE = "files/thankYouEmail.html";
	
	/* ARRAY OF STRINGS TO FILTER CLASSES WITH: SHOULD ALL BE LOWER CASE */
	// the filtering is used when pulling classes from the spreadsheet online
	public static final String CLASS_FILTER_STRINGS[] = {"none", "carte"};
	
	/* FESTIVAL INFORMATION FILE */
	public static final String FESTIVAL_XML_FILE = "files/festivalClasses.xml";
	public static final String FESTIVAL_USER_INFORMATION_FILE = "files/userInfo.txt";
	public static final double TAX = 0.06;
}
