package datastructures.festival;

/**
 * An immutable class that contains all the information for a specific
 * festival class that is going to be offered for this "event".
 * 
 * @author benjamyn
 *
 */
public class Class {
	public final String name;
	public final String teachers;
	public final String level;
	public final int startTime; // 24-hour
	public final int endTime;
	public final String room;
	
	public Class (String name, String teachers, String level, 
			int startTime, int endTime, String room) {
		this.name = name;
		this.teachers = teachers;
		this.level = level;
		this.startTime = startTime;
		this.endTime = endTime;
		this.room = room;
	}
	
	public String toString () {
		return name + ":" + teachers + ":" + level + ":" + startTime + "-" + endTime + ":" + room;
	}
}
