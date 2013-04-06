package datastructures;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import classes.AtcErr;
import datastructures.Enums.RegistrationMode;

/**
 * This immutable class reads in the user information from a file 
 * called "userInfo.txt" that has the 
 * specific format (each one a separate line):
 * <ul>
 * 	<li>gmail username</li>
 * 	<li>gmail password</li>
 * 	<li>google spreadsheet key</li>
 * 	<li>google spreadsheet sheet name (formsite)</li>
 * 	<li>google spreadsheet sheet name (on-site registration)</li>
 * </ul>
 * 
 * @author benjamyn
 *
 */
public class AccountInformation {
	public final String userName;
	public final String passwd;
	
	public final String[] spreadsheets;
	public final String[] formsiteWkShtTitles;
	public final String[] onsiteWkShtTitles;
	
	public AccountInformation (String fileName) {
		if (fileName == null)
			throw new AtcErr("User information file: NULL");
		
		Scanner s = null;
		
		try {
			s = new Scanner (new BufferedReader (new FileReader (fileName)));
		} catch (FileNotFoundException e) {
			throw new AtcErr ("User information file: NOT FOUND");
		}
		
		userName = s.nextLine().trim();
		passwd = s.nextLine().trim();
		
		int numRegModes = RegistrationMode.values ().length;
		spreadsheets = new String[numRegModes];
		formsiteWkShtTitles = new String[numRegModes];
		onsiteWkShtTitles = new String[numRegModes];
		
		for (int i = 0; i < numRegModes; ++i) {
			spreadsheets[i] = s.nextLine().trim();
			formsiteWkShtTitles[i] = s.nextLine().trim();
			onsiteWkShtTitles[i] = s.nextLine().trim();
		}
		
		s.close ();
	}
}
