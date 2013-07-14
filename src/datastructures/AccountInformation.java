package datastructures;

/**
 * This immutable class contains the user information.
 *
 * @author benjamyn
 */
public class AccountInformation {
	public final String userName;
	public final String passwd;
	
	public final String[] spreadsheets;
	public final String[] formsiteWkShtTitles;
	public final String[] onsiteWkShtTitles;
	
	public AccountInformation (String userName, String passwd, final String[] spreadsheets, 
		final String[] formsiteWkShts, final String[] onsiteWkShts) {
		this.userName = userName;
		this.passwd = passwd;
		this.spreadsheets = spreadsheets;
		this.formsiteWkShtTitles = formsiteWkShts;
		this.onsiteWkShtTitles = onsiteWkShts;
	}
}
