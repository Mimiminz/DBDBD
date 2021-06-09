import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;
import java.sql.*;

public class Emergencybell {
    public static void main(String[] args) throws SQLException {

        Scanner scan = new Scanner(System.in);

        System.out.println("Connecting PostgreSQL database");

        String url = "jdbc:postgresql://localhost:5432/DB";;
        String user = "postgres";
        String password = "";

        Connection db = DriverManager.getConnection(url, user, password);
        Statement st = db.createStatement();

        ResultSet rsbell = st.executeQuery("select * from Emergencybell;");

        System.out.println("idx\tmtype\tlatitude\tlongitude");
        System.out.println("---------------------------------------------------");
        while (rsbell.next()) {
            System.out.printf(rsbell.getInt(1) + "\t" + rsbell.getInt(2) + "\t"
                    + rsbell.getFloat(3) + "\t" + rsbell.getFloat(4) + "\n");
        }


    }

}
