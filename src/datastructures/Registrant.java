package datastructures;

import java.util.ArrayList;
import java.util.Collection;

import datastructures.Enums.*;

/**
 * The Registrant contains all the information that any registered (might not be fully)
 * festival attendee possesses. Not all the fields may have values, however, they should
 * if their payment has been received and their e-ticket has been sent.
 * 
 * @author benjamyn
 *
 */
public class Registrant implements Comparable <Registrant> {
	/* ***************************************************/
	public final int row;
	public final Name name;
	public final String email;
	public final String phone;
	public final StudentType studentType;
	public final DancerType dancerType;
	public final ExperienceLevel expLvl;
	public final double amount;
	
	private ArrayList<String> classes;
	private boolean paid;
	private boolean eticketSent;

	public Registrant (int row, String firstName, String lastName, String email, String phone,
			StudentType studentType, DancerType dancerType, ExperienceLevel expLvl, double amount) {
		this.row = row;
		this.name = new Name (firstName, lastName);
		this.email = email;
		this.phone = phone;
		this.studentType = studentType;
		this.dancerType = dancerType;
		this.expLvl = expLvl;
		this.amount = amount;
		
		classes = new ArrayList <String> ();
		paid = eticketSent = false;
	}

	public boolean hasPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public boolean hasEticketSent() {
		return eticketSent;
	}

	public void setEticketSent(boolean eticketSent) {
		this.eticketSent = eticketSent;
	}

	public void addClass (String cl) {
		classes.add (cl);
	}
	
	public void addClasses (Collection<String> cls) {
		classes.ensureCapacity (classes.size () + cls.size ());
		
		for (String cl : cls) 
			classes.add (cl);
	}

	/**
	 * Returns a list of classes, however, some may not
	 * be actual classes if the list was pulled from the spreadsheet.
	 * To receive only true classes, edit the filter strings
	 * in {@link Constants#CLASS_FILTER_STRINGS} and use
	 * {@link #getFilteredClasses()}.
	 * 
	 * @return list of unfiltered classes
	 */
	public ArrayList<String> getAllClasses () {
		return (ArrayList<String>) classes.clone ();
	}
	
	/**
	 * Returns a list of classes which pass through the filter
	 * given by the list of keywords to filter on in 
	 * {@link datastructures.Constants#CLASS_FILTER_STRINGS}.
	 * 
	 * @return a list of classes which pass through the filter
	 */
	public ArrayList<String> getFilteredClasses () {
		ArrayList<String> filteredClasses = new ArrayList<String> ();
		
		for (String cl : classes) {
			boolean shouldFilterIt = false;
			for (String filterString : Constants.CLASS_FILTER_STRINGS)
				if (cl.toLowerCase ().contains (filterString)) {
					shouldFilterIt = true;
					break;
				}

			if (!shouldFilterIt)
				filteredClasses.add (cl);
		}
		
		return filteredClasses;
	}
	
	public String toString () {
		return "{" + name + ", " + email + ", " + phone + ", " 
				   + studentType + ", " + dancerType + ", " + expLvl + ", " + amount + "}";
	}

	@Override
	public int compareTo (Registrant arg0) {
		return this.name.last.toLowerCase ().compareTo (arg0.name.last.toLowerCase ());
	}
}
