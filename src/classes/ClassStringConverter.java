package classes;

import java.util.HashMap;

/**
 * Bridge between awful db format to readable format.
 * @author reedt
 */
public class ClassStringConverter {
    private ClassStringConverter() {}

    private static HashMap<Integer, String> classMapping = new HashMap<>();

    static {
        classMapping.put(2, "Finding Sacadas Everywhere");
        classMapping.put(3, "Float Like a Butterfly, Sting Like a Bee: Suspension in the Dance");
        classMapping.put(5, "Break Out of Your Shell Milonga");
        classMapping.put(8, "Impossibly Small Turns: Create Space Through the Embrace");
        classMapping.put(9, "The Dance Between the Beats: Embellishments in Common Steps");
        classMapping.put(11, "Rhythmic Alteraciones for Vals");
        classMapping.put(12, "The Axis Unhinged: Comfy Volcadas");
        classMapping.put(14, "Yoga for Tango (Saturday)");
        classMapping.put(16, "Action and Reaction");
        classMapping.put(17, "Uncovering the Magic: Exploring Musicality");
        classMapping.put(19, "Tersichore's Grand Milonga");
        classMapping.put(22, "The Silky Embrace: Effortless and Sensitive Connection");
        classMapping.put(23, "Wax On Wax Off: Mastering Rotation Within Your Own Space");
        classMapping.put(25, "Making Colgadas Effortless: All Secrets of Colgada Technique");
        classMapping.put(26, "Rapid Fire Footwork for Milonga");
        classMapping.put(28, "Yoga for Tango (Sunday)");
        classMapping.put(30, "Ganchos and Wraps");
        classMapping.put(31, "Finding your Power: Solo and Partner Techniques");
        classMapping.put(33, "Shellibration Milonga");
    }

    /**
     * Get the name of the class that has this index in the classes table.
     * @param classIndex Class number
     * @return The class string
     */
    public static String getClass(int classIndex) {
        return classMapping.get(classIndex);
    }
}
