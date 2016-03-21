package drivers;

import java.sql.*;
import classes.XmlParserHelper;
import org.w3c.dom.Element;

/**
 * Created by reedt on 3/20/2016.
 */
public class AmazonRDSTestConnection {
    private static final String accountInfo = "files/info/accountInfo.xml";

    public static void main(String[] args) {
        // Parse XML
        Element root = XmlParserHelper.getRootElement(accountInfo);
        Element dbRoot = XmlParserHelper.getSingleElement(root, "DB");

        String dbURL = XmlParserHelper.getContent(dbRoot, "URL");
        String username = XmlParserHelper.getContent(dbRoot, "UserName");
        String password = XmlParserHelper.getContent(dbRoot, "Password");
        String dbName = XmlParserHelper.getContent(dbRoot, "Database");

        // Connect to DB
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection c = null;

        try {
            c = DriverManager.getConnection("jdbc:mariadb://" + dbURL + "/" + dbName, username, password);
        } catch (SQLException e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
            System.exit(-1);
        }

        try {
            Statement s = c.createStatement();

            ResultSet rs = s.executeQuery("select * from records");

            while (rs.next()) {
                System.out.println(rs.getString("Fname"));
            }

            // Close everything
            rs.close();
            s.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
