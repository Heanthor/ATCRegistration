package classes;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import datastructures.Enums;
import datastructures.Enums.FestivalDay;
import datastructures.Enums.SpecialPassType;
import datastructures.Enums.StudentType;
import datastructures.SimpleDate;
import datastructures.festival.Event;
import datastructures.festival.Milonga;
import datastructures.festival.Class;
import datastructures.festival.Prices;
import datastructures.festival.Prices.MilongaPrice;
import datastructures.festival.Prices.SpecialPass;

/**
 * A class to encapsulate the parsing of the XML file, which contains
 * class and milonga descriptions/times, special passes, class costs,
 * and other pricing details. All of this information is neatly
 * packed into a list of {@link Event} objects, one for each day
 * found in the XML file, and a {@link Prices} object
 * 
 * @author benjamyn
 *
 */
public class FestivalXMLParser {
	private Element festivalEle;
	
	public FestivalXMLParser (String filename) {
		// get root element ("Festival")
		festivalEle = XmlParserHelper.getRootElement(filename);
	}
	
	/**
	 * Returns an array of Event objects of size, numEvents
	 *  
	 * @param numEvents the number of events that should be found in the XML file
	 * @return an array of Event objects, one for each day in {@link datastructures.Enums.FestivalDay}
	 */
	public Event[] getEvents (int numEvents) {
		// get "Events" tag
		Element eventsEle = XmlParserHelper.getSingleElement(festivalEle, "Events");
		Event[] eventArr = new Event[numEvents];
		
		// get list of "Event" nodes
		NodeList eventList = eventsEle.getElementsByTagName("Event");
		if (eventList.getLength() != numEvents)
			throw new AtcErr ("Number of \"Event\" tags not equal to expected number: " + numEvents);
		
		for (int i = 0; i < numEvents; ++i)
			eventArr[i] = parseNextEvent ((Element) eventList.item(i));
		
		return eventArr;
	}
	
	/**
	 * Reads the next event from the given node and returns an Event object
	 * 
	 * @param eventEle XML element of a "Event" tag
	 * @return Event object representing a single day of festival events
	 */
	private Event parseNextEvent (Element eventEle) {
		Event event = new Event();
		
		event.setDate(parseEventDate (eventEle));
		event.addClasses(parseEventClasses (eventEle));
		event.setMilonga(parseEventMilonga (eventEle));
		
		return event;
	}
	
	/**
	 * Parses the "Date" tag in the Element object, putting the date
	 * into the Event object.
	 * 
	 * @param eventEle XML element of a "Event" tag
	 * @return date of the event
	 */
	private SimpleDate parseEventDate (Element eventEle) {
		Element dateEle = XmlParserHelper.getSingleElement(eventEle, "Date");
		
		// get week day
		String weekDay = XmlParserHelper.getContent (dateEle, "WeekDay");
		int day = XmlParserHelper.getContentInteger (dateEle, "Day");
		String month = XmlParserHelper.getContent (dateEle, "Month");
		
		return new SimpleDate (weekDay, day, month);
	}
	
	/**
	 * Parses the "Classes" tag in the Element object, putting the classes
	 * into the Event object.
	 * 
	 * @param eventEle XML element of a "Event" tag
	 * @return a list of Class objects, which contain information about them 
	 *         for that festival day
	 */
	private ArrayList<Class> parseEventClasses (Element eventEle) {
		ArrayList<Class> classes = new ArrayList<Class>();
		Element classesEle = XmlParserHelper.getSingleElement(eventEle, "Classes");
		
		// get list of classes
		NodeList classList = classesEle.getElementsByTagName("Class");
		for (int i = 0; i < classList.getLength(); ++i)
			classes.add(parseEventClass ((Element) classList.item(i)));
		
		return classes;
	}
	
	private Class parseEventClass (Element classEle) {
		return new Class (XmlParserHelper.getContent (classEle, "Name"), XmlParserHelper.getContent (classEle, "Teachers"),
				          XmlParserHelper.getContent (classEle, "Level"), XmlParserHelper.getContentInteger (classEle, "StartTime"),
				          XmlParserHelper.getContentInteger (classEle, "EndTime"), XmlParserHelper.getContent (classEle, "Room"));
	}
	
	/**
	 * Parses the "Milonga" tag in the Element object, putting the milonga
	 * into the Event object.
	 * 
	 * @param eventEle XML element of a "Event" tag
	 * @return a Milonga object containing information about the milonga for that day
	 */
	private Milonga parseEventMilonga (Element eventEle) {
		Element milongaEle = XmlParserHelper.getSingleElement(eventEle, "Milonga");
		
		return new Milonga (XmlParserHelper.getContent (milongaEle, "Name"),
				XmlParserHelper.getContentInteger (milongaEle, "StartTime"),
				XmlParserHelper.getContentInteger (milongaEle, "EndTime"));
	}
	
	/**
	 * Parses the prices section of the input file.
	 * 
	 * @param studentType the student type for which the prices are for
	 * @return a Prices object containing the costs for the given student type
	 */
	public Prices getPrices (StudentType studentType) {
		// get "Prices" tag
		Element pricesEle = XmlParserHelper.getSingleElement(festivalEle, "Prices");
		
		// get the desired student type
		Element studentTypeEle = XmlParserHelper.getSingleElement(pricesEle, Enums.StudentTypeXMLTags[studentType.ordinal()]);

		Prices prices = new Prices (studentType, XmlParserHelper.getContentInteger (studentTypeEle, "SingleClass"));
		prices.addSpecialPasses(parseSpecialPasses(studentTypeEle));
		prices.addMilongaPrices(parseMilongaPrices(studentTypeEle));
		
		return prices;
	}
	
	/**
	 * Get a list of special passes given an XML element that contains a 
	 * "SpecialPasses" XML element. See @{link Enums.SpecialPassTypeXMLTags}.
	 * 
	 * @param studentTypeEle XML Element node that contains the special pass XML tags
	 * @return List of SpecialPass objects
	 */
	private ArrayList<SpecialPass> parseSpecialPasses (Element studentTypeEle) {
		ArrayList<SpecialPass> sps = new ArrayList <SpecialPass> ();
		
		Element spsEle = XmlParserHelper.getSingleElement(studentTypeEle, "SpecialPasses");
		for (SpecialPassType spt : Enums.SpecialPassType.values())
			sps.add (new SpecialPass (spt, XmlParserHelper.getContentInteger (spsEle, Enums.SpecialPassTypeXMLTags[spt.ordinal()])));
		
		return sps;
	}
	
	/**
	 * Get a list of milonga prices given an XML element that contains a 
	 * "Milongas" XML element. See @{link Enums.SpecialPassTypeXMLTags}.
	 * 
	 * @param milongasNode
	 * @return List of MilongaPrice objects
	 */
	private ArrayList<MilongaPrice> parseMilongaPrices (Element milongasNode) {
		ArrayList<MilongaPrice> mps = new ArrayList <MilongaPrice> ();
		
		Element mpsEle = XmlParserHelper.getSingleElement(milongasNode, "Milongas");
		for (FestivalDay day : Enums.FestivalDay.values())
			mps.add (new MilongaPrice (day, XmlParserHelper.getContentInteger (mpsEle, Enums.MilongaDayXMLTags[day.ordinal()])));
		
		return mps;
	}
}
