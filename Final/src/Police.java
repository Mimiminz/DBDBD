import java.sql.*;
import java.util.*;


public class Police {
    public static void main(String[] args) throws SQLException {
        try{
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String usr = "byeonhajin";
            String pwd = "";

            Connection db = DriverManager.getConnection(url, usr, pwd);

            System.out.println("Police");
            Statement st;
            st = db.createStatement();
            ResultSet rs = null;

            rs = st.executeQuery("select * from Police");

            while(rs.next()) {
                System.out.println(rs.getInt(1)
                        + " " + rs.getInt(2) + " " + rs.getString(3)
                        + " " + rs.getString(4));
            }


        }
        catch(SQLException ex) {
            throw ex; }
    }
}