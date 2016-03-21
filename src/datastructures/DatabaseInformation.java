package datastructures;

/**
 * Immutable class holding database information (not formsite!)
 * Created by reedt on 3/20/2016.
 */
public class DatabaseInformation {
    public final String dbURL;
    public final String dbUsername;
    public final String dbPassword;
    public final String dbName;

    public DatabaseInformation(String dbURL, String dbUsername, String dbPassword, String dbName) {
        this.dbURL = dbURL;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.dbName = dbName;
    }
}
