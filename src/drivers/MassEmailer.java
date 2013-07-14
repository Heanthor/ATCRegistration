package drivers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import classes.Configuration;
import classes.Configuration.EmailType;
import classes.Emailer;
import classes.SheetClient;
import datastructures.AccountInformation;
import datastructures.Enums.RegistrationMode;
import datastructures.Enums.SheetClientMode;
import datastructures.Registrant;

/**
 * This program sents out a mass email to each of the paid registrants in 
 * the two spreadsheets: the formsite and the on-site spreadsheet.
 * 
 * PLEASE BE CAREFUL WITH THIS PROGRAM. Before the email is sent out,
 * it lists information about all the emails and asks THREE times about
 * sending out the email. Sending an email to >100 people needs to
 * not be an accident.
 * 
 * @author benjamyn
 *
 */
public class MassEmailer {
	
	public static void main (String args[]) {
		if (args.length != 1) {
			help();
		}
		
		int emailInt = Integer.parseInt(args[0]);
		
		if (! (emailInt == 1 || emailInt == 2)) {
			help();
		}
		else {
			Configuration config = new Configuration();
			EmailType eType = emailInt == 1 ? EmailType.PREFESTIVAL : EmailType.THANK_YOU;
			
			String subjectLine = config.getEmailSubject(eType);
			String emailBodyFile = config.getEmailBodyFile(eType);
			
			// set to early registrants
			MassEmailer me = new MassEmailer (config, RegistrationMode.EARLY_REGISTRATION);
			me.setUpMassEmail (emailBodyFile, subjectLine);
			me.sendEmails ();

			// set to late registrants
			me.setRegistrationMode(RegistrationMode.LATE_REGISTRATION);
			me.setUpMassEmail (emailBodyFile, subjectLine);
			me.sendEmails ();
		}
	}
	
	private static void help() {
		System.out.println("Usage: java -jar <mass emailer jar> [email type number] [Registration type number]\n\n" +
						   "Email type number: \n" +
						   "   1: Prefestival Email\n" +
						   "   2: Thank You Email\n" +
						   "Registration type number: \n" +
						   "   1: Early Registration\n" +
						   "   2: Late Registration\n");
		System.exit(-1);
	}
	
	private AccountInformation ai;
	private SheetClient sc;
	private Emailer emailer;
	
	/**
	 * Sets up the account information, spreadsheet, and emailing features
	 * 
	 * @param userInfoFile
	 */
	public MassEmailer (Configuration config, RegistrationMode regMode) {
		ai = config.getAccountInformation();
		sc = new SheetClient (ai, SheetClientMode.FORM_SITE, regMode);
		sc.refresh ();
		emailer = new Emailer (ai.userName, ai.passwd);
	}
	
	/**
	 * Sets up the mass email to be sent, but does not send it. After
	 * calling this function, the email(s) are "locked and loaded".
	 * 
	 * @param bodyFile
	 * @param subjLine
	 */
	public void setUpMassEmail (String bodyFile, String subjLine) {
		List<Registrant> paidRegsFormSite = sc.getPaidRegistrants ();
		sc.setMode (SheetClientMode.ON_SITE);
		List<Registrant> paidRegsOnsite = sc.getPaidRegistrants ();
		
		emailer.resetEmail ();
		emailer.setSeperateEmails (true);
		emailer.sendAttachments (false);
		emailer.setSubjectLine (subjLine);
		emailer.setBodyFile (bodyFile);
		
		// add recipients
		for (Registrant reg : paidRegsFormSite) 
			emailer.addRecipient (reg.email);
		for (Registrant reg : paidRegsOnsite)
			emailer.addRecipient (reg.email);
	}
	
	/**
	 * Sends out the emails after verifying with the user that it is really
	 * what they want to do.
	 */
	public void sendEmails () {
		BufferedReader br = new BufferedReader (new InputStreamReader (System.in));
		// print the email information
		System.out.println (emailer + "\n");
		
		try {
			System.out.println ("*** ARE YOU SURE YOU WANT TO SEND THESE EMAILS (Y/N)?");
			String input = br.readLine ();
			
			if (input.compareTo ("Y") != 0) {
				System.out.println ("NOT SENDING EMAIL");
				return;
			}

			System.out.println ("*** ARE YOU __REALLY__ SURE YOU WANT TO SEND THESE EMAILS (Y/N)?");
			input = br.readLine ();
			
			if (input.compareTo ("Y") != 0) {
				System.out.println ("NOT SENDING EMAIL");
				return;
			}

			System.out.println ("*** I'M ONLY GOING TO ASK YOU ONE MORE TIME (Y/N)?");
			input = br.readLine ();
			
			if (input.compareTo ("Y") != 0) {
				System.out.println ("NOT SENDING EMAIL");
				return;
			}

			System.out.println ("Okay, I'm sending those emails then...........");
			emailer.sendEmail ();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setRegistrationMode(RegistrationMode regMode) {
		sc.setRegistrationMode(regMode);
	}
}
