package dbproject2;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;

public class dbproject4 {
	public static void main(String[] args) throws SQLException, IOException
	{
		Scanner scan = new Scanner(System.in);
		Connection connect = DriverManager.getConnection("jdbc:postgresql://localhost/postgres","psw","psw1234");
		
		String fileUrl = "dbproject3.html";
		Document doc = Jsoup.connect(fileUrl).get();
		Elements spans = doc.getElementsByTag("span");
		if(spans.size() > 0) { 
			String id = spans.get(0).attr("id");
		}


	}

}
