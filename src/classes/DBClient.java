package classes;

import datastructures.DatabaseInformation;
import datastructures.Enums;
import datastructures.Enums.RegistrationMode;
import datastructures.Name;
import datastructures.Registrant;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static datastructures.Enums.StudentType;

/**
 * Client used to connect to a database, and pull registration records
 * Created by reedt on 3/20/2016.
 */
public class DBClient {
    private DatabaseInformation db;
    private RegistrationMode regMode;

    private Connection connection;

    private boolean connected = false;

    public DBClient(DatabaseInformation db, RegistrationMode regMode) {
        this.db = db;
        this.regMode = regMode;
    }

    /**
     * Attempt to connect to the database. Performs an action only when this client is not connected to any database.
     * Uses MariaDB/MySQL syntax.
     *
     * @throws SQLException If connection error occurs
     */
    public void connect() throws SQLException {
        if (!connected) {
            // Connect to DB
            try {
                Class.forName("org.mariadb.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                new AtcErr(e.getMessage());
            }

            connection = DriverManager.getConnection("jdbc:mariadb://" + db.dbURL + "/" + db.dbName, db.dbUsername, db.dbPassword);
        }
    }

    public void disconnect() {
        try {
            connection.close();
            connected = false;
        } catch (SQLException e) {
            new AtcErr(e.getMessage());
        }
    }

    public void reset() {
        if (connected) {
            disconnect();
            try {
                connect();
            } catch (SQLException e) {
                new AtcErr(e.getMessage());
            }
        }
    }

    private ResultSet query(String queryString) throws SQLException {
        Statement s = connection.createStatement();

        return s.executeQuery(queryString);
    }

    public List<Registrant> getPaidRegistrants() {
        ArrayList<Registrant> toReturn = new ArrayList<>();
        String queryString = "SELECT r.registerid, r.fname, r.lname, r.phone, r.tickettype, r.partnerfname, r.partnerlname, " +
                "r.email, c.classes, c.passtype, c.price " +
                "FROM records r, classes c, confirmation f " +
                "WHERE r.registerid = c.registerid and r.registerid = f.registerid and " +
                "f.payment_status = 'Completed'";

        try {
            ResultSet rs = query(queryString);

            while (rs.next()) {
                Registrant result = parseDbResult(rs);
                if (!(emailInRegistrantList(toReturn, result.email))) {
                    toReturn.add(result);
                }
            }
        } catch (SQLException e) {
            new AtcErr(e.getMessage());
        }

        return toReturn;
    }

    /**
     * Avoid adding duplicates to the list.
     *
     * @param in
     * @param email
     * @return
     */
    private boolean emailInRegistrantList(List<Registrant> in, String email) {
        for (Registrant i : in) {
            if (i.email.equals(email)) {
                return true;
            }
        }

        return false;
    }

    private Registrant parseDbResult(ResultSet rs) throws SQLException {
        Name name = new Name(rs.getString("fname"), rs.getString("lname"));
        Name partnername = new Name(rs.getString("partnerfname"), rs.getString("partnerlname"));
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        String passtype = rs.getString("tickettype");
        String classString = rs.getString("classes");
        int registerid = Integer.parseInt(rs.getString("registerid"));
        // This all assumes valid format, which is stored in the db
        String[] classList = classString.split(",");
        int[] classListInt = new int[classList.length];

        // sort by class index
        for (int i = 0; i < classList.length; i++) {
            String c = classList[i];

            if (c.length() > 0) {
                classListInt[i] = Integer.parseInt(classList[i]);
            }
        }

        Arrays.sort(classListInt);

        int numRegistrants = partnername.first == null ? 1 : 2;

        StudentType s = Enums.stringToStudentType(passtype);
        ArrayList<String> classes = new ArrayList<>();

        for (int c : classListInt) {
            classes.add(ClassStringConverter.getClass(c));
        }

        return new Registrant(-1, name.first, name.last, partnername.first, partnername.last,
                email, phone, s, null, null, 0.0, numRegistrants, classes, false, null, null, registerid);
    }
}
