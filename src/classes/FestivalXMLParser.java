package classes;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;
		
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(filename);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// get root element ("Festival")
		festivalEle = dom.getDocumentElement();
	}
	
	/**
	 * Returns an array of Event objects of size, numEvents
	 *  
	 * @param numEvents the number of events that should be found in the XML file
	 * @return an array of Event objects, one for each day in {@link datastructures.Enums.FestivalDay}
	 */
	public Event[] getEvents (int numEvents) {
		// get "Events" tag
		NodeList eventsList = festivalEle.getElementsByTagName("Events");
		if (eventsList.getLength() != 1)
			throw new AtcErr ("Number of \"Events\" tags not one");
		
		Event[] eventArr = new Event[numEvents];
		Element eventsEle = (Element) eventsList.item(0);
		
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
	 * @param eventEle
	 * @return
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
	 * @param eventEle
	 * @return
	 */
	private SimpleDate parseEventDate (Element eventEle) {
		NodeList dateList = eventEle.getElementsByTagName("Date");
		if (dateList.getLength() != 1)
			throw new AtcErr ("Invalid number of \"Date\" tags for event");
		
		Element dateEle = (Element) dateList.item(0);
		
		// get week day
		String weekDay = getContent (dateEle, "WeekDay");
		int day = getContentInteger (dateEle, "Day");
		String month = getContent (dateEle, "Month");
		
		return new SimpleDate (weekDay, day, month);
	}
	
	/**
	 * Parses the "Classes" tag in the Element object, putting the classes
	 * into the Event object.
	 * @param eventEle
	 * @return
	 */
	private ArrayList<Class> parseEventClasses (Element eventEle) {
		ArrayList<Class> classes = new ArrayList<Class>();
		
		NodeList classesList = eventEle.getElementsByTagName("Classes");
		if (classesList.getLength() != 1)
			throw new AtcErr ("Invalid number of \"Classes\" tags for event");
		
		Element classesEle = (Element) classesList.item(0);
		
		// get list of classes
		NodeList classList = classesEle.getElementsByTagName("Class");
		for (int i = 0; i < classList.getLength(); ++i)
			classes.add(parseEventClass ((Element) classList.item(i)));
		
		return classes;
	}
	
	private Class parseEventClass (Element classEle) {
		return new Class (getContent (classEle, "Name"), getContent (classEle, "Teachers"),
				          getContent (classEle, "Level"), getContentInteger (classEle, "StartTime"),
				          getContentInteger (classEle, "EndTime"), getContent (classEle, "Room"));
	}
	
	/**
	 * Parses the "Milonga" tag in the Element object, putting the milonga
	 * into the Event object.
	 * @param eventEle
	 * @return
	 */
	private Milonga parseEventMilonga (Element eventEle) {
		NodeList milongaList = eventEle.getElementsByTagName("Milonga");
		if (milongaList.getLength() != 1)
			throw new AtcErr ("Invalid number of \"Milonga\" tags for event");
		
		Element milongaEle = (Element) milongaList.item(0);
		
		return new Milonga (getContent (milongaEle, "Name"),
				getContentInteger (milongaEle, "StartTime"),
				getContentInteger (milongaEle, "EndTime"));
	}
	
	/**
	 * Helper method to quickly get String content
	 * 
	 * @param e
	 * @param tag
	 * @return
	 */
	private String getContent (Element e, String tag) {
		NodeList nl = e.getElementsByTagName(tag);
		if (nl.getLength() != 1)
			throw new AtcErr ("Expecting only a single tag with tag name: " + tag + ": " + nl.getLength());
		
		return nl.item(0).getTextContent();
	}

	/**
	 * Helper method to quickly get Integer content
	 * 
	 * @param e
	 * @param tag
	 * @return
	 */
	private int getContentInteger (Element e, String tag) {
		return Integer.parseInt(getContent (e, tag));
	}
	
	/**
	 * Parses the prices section of the input file.
	 * 
	 * @param studentType the student type for which the prices are for
	 * @return a Prices object containing the costs for the given student type
	 */
	public Prices getPrices (StudentType studentType) {
		// get "Prices" tag
		NodeList pricesList = festivalEle.getElementsByTagName("Prices");
		if (pricesList.getLength() != 1)
			throw new AtcErr ("Unexpected number of \"Prices\" tags, expected only 1: " +
								pricesList.getLength());
		
		Element pricesEle = (Element) pricesList.item(0);
		
		// get the desired student type
		NodeList studentTypeList = pricesEle.getElementsByTagName(Enums.StudentTypeXMLTags[studentType.ordinal()]);
		if (studentTypeList.getLength() != 1)
			throw new AtcErr ("Unexpected number of student type elements, expected only 1: " + 
								studentTypeList.getLength());
		
		Element studentTypeEle = (Element) studentTypeList.item(0);
		Prices prices = new Prices (studentType, getContentInteger (studentTypeEle, "SingleClass"));
		prices.addSpecialPasses(parseSpecialPasses(studentTypeEle));
		prices.addMilongaPrices(parseMilongaPrices(studentTypeEle));
		
		return prices;
	}
	
	private ArrayList<SpecialPass> parseSpecialPasses (Element studentTypeEle) {
		ArrayList<SpecialPass> sps = new ArrayList <SpecialPass> ();
		
		NodeList spsList = studentTypeEle.getElementsByTagName("SpecialPasses");
		if (spsList.getLength() != 1)
			throw new AtcErr ("Unexpected number of \"SpecialPasses\" tags, expect 1: " + spsList.getLength());
		
		Element spsEle = (Element) spsList.item(0);
		for (SpecialPassType spt : Enums.SpecialPassType.values())
			sps.add (new SpecialPass (spt, getContentInteger (spsEle, Enums.SpecialPassTypeXMLTags[spt.ordinal()])));
		
		return sps;
	}
	
	private ArrayList<MilongaPrice> parseMilongaPrices (Element studentTypeEle) {
		ArrayList<MilongaPrice> mps = new ArrayList <MilongaPrice> ();
		
		NodeList mpsList = studentTypeEle.getElementsByTagName("Milongas");
		if (mpsList.getLength() != 1)
			throw new AtcErr ("Unexpected number of \"Milongas\" tags, expect 1: " + mpsList.getLength());
		
		Element mpsEle = (Element) mpsList.item(0);
		for (FestivalDay day : Enums.FestivalDay.values())
			mps.add (new MilongaPrice (day, getContentInteger (mpsEle, Enums.MilongaDayXMLTags[day.ordinal()])));
		
		return mps;
	}
}
