package datastructures;

import classes.AtcErr;

/**
 * This class contains a list of enums that are used to describe
 * registrants, festival properties, spreadsheet properties,
 * button values, etc. There are also a few methods used
 * to help in parsing strings into the appropriate enum type.
 * 
 * @author benjamyn
 *
 */
public class Enums {
	/*
	 * A bunch of enum declarations to sort out various registrant types
	 */
	public enum TicketStatus {
		CONFIRMED, UNCONFIRMED
	}
	public enum StudentType {
		UMD_GMU_STUDENT, OTHER_STUDENT, GENERAL_ADMISSION
	}
	public enum DancerType {
		FOLLOWER, LEADER
	}
	public enum ExperienceLevel {
		BEGINNER, INTERMEDIATE, ADVANCED
	}
	
	/* used for mapping day # to week day */
	public enum FestivalDay {
		FRIDAY, SATURDAY, SUNDAY;
	}
	
	/* special pass mappings */
	public enum SpecialPassType {
		FULL_PASS, FRIDAY_PASS, SATURDAY_PASS, SUNDAY_PASS, MILONGA_PASS
	}
	
	public enum SheetClientMode {
		FORM_SITE, ON_SITE;
	}
	
	
	/* Strings used with the XML document to parse from string to enum types */
	
	/* works in conjuction with the StudentType enum */
	public static final String StudentTypeXMLTags[] = { "UmdGmuStudent", "OtherStudent", "GeneralAdmission" };

	/* works in conjuction with the SpecialPassType enum */
	public static final String SpecialPassTypeXMLTags[] = { "FullPass", "FridayPass", "SaturdayPass", "SundayPass", "MilongaPass" };
	
	/* works in conjuction with the FestivalDay enum */
	public static final String MilongaDayXMLTags[] = { "Friday", "Saturday", "Sunday" };

	
	/* Strings used for the ETicket, namely the "Ticket Type" field */ 
	
	/* works in conjuction with the StudentType enum */
	public static final String ETicketStudentType[] = { "UMD/GMU Student", "Other Student", "General Admission" };
	
	
	/* Yes/No enum for working with the On-Site registration buttons */
	public enum YesNo {
		NO, YES
	}
	

	/**
	 * A function used to convert a string to an enum value in regards to the
	 * student type. This function works by searching the input string for the
	 * word (case insensitive):
	 * <ol>
	 * 	<li>UMD_GMU_STUDENT: look for word "UMD"</li>
	 * 	<li>OTHER_STUDENT: look for word "other"</li>
	 * 	<li>GENERAL_ADMISSION: look for word "general"</li>
	 * </ol>
	 * This approach was taken in case the string changed and all the configurations
	 * would then have to be reset (e.g. "UMD or GMU Student" to "UMD/GMU Student").
	 * If none of these strings are found, an exception is thrown, in which case
	 * this method, configuration, or something needs to be modified.
	 * 
	 * @param sType string for parsing
	 * @return The appropriate student type. Returns null if the input string is
	 * null or the empty string.
	 */
	public static StudentType stringToStudentType (String sType) {
		if (sType == null || sType.compareTo("") == 0)
			return null;
		else if (sType.toLowerCase().contains("umd"))
			return StudentType.UMD_GMU_STUDENT;
		else if (sType.toLowerCase().contains("other"))
			return StudentType.OTHER_STUDENT;
		else if (sType.toLowerCase().contains("general"))
			return StudentType.GENERAL_ADMISSION;
		else 
			throw new AtcErr ("Converting string to StudentType: could not parse: " + sType);
	}
	
	/**
	 * A function used to convert a string to an enum value in regards to the
	 * dancer type. This function works by comparing the string representation
	 * of the types found in {@link DancerType}
	 * 
	 * @param dType string for parsing
	 * @return The appropriate dancer type. Returns null if the input string is
	 * null or the empty string.
	 */
	public static DancerType stringToDancerType (String dType) {
		if (dType == null || dType.compareTo("") == 0)
			return null;
		else if (dType.toLowerCase().contains(DancerType.FOLLOWER.toString().toLowerCase()))
			return DancerType.FOLLOWER;
		else if (dType.toLowerCase().contains(DancerType.LEADER.toString().toLowerCase()))
			return DancerType.LEADER;
		else
			throw new AtcErr ("Converting string to DancerType: could not parse: " + dType);
	}
	
	/**
	 * A function used to convert a string to an enum value in regards to the
	 * experience level. This function works by comparing the string representation
	 * of the types found in {@link ExperienceLevel}.
	 * 
	 * @param eLvl string for parsing
	 * @return The appropriate experience level. Returns null if the input string is
	 * null or the empty string.
	 */
	public static ExperienceLevel stringToExperienceLevel (String eLvl) {
		if (eLvl == null || eLvl.compareTo("") == 0)
			return null;
		else if (eLvl.toLowerCase().contains(ExperienceLevel.BEGINNER.toString().toLowerCase()))
			return ExperienceLevel.BEGINNER;
		else if (eLvl.toLowerCase().contains(ExperienceLevel.INTERMEDIATE.toString().toLowerCase()))
			return ExperienceLevel.INTERMEDIATE;
		else if (eLvl.toLowerCase().contains(ExperienceLevel.ADVANCED.toString().toLowerCase()))
			return ExperienceLevel.ADVANCED;
		else
			throw new AtcErr ("Converting string to ExperienceLevel: could not parse: " + eLvl);
	}
}
