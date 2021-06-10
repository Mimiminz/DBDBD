package dbteam;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import org.json.JSONObject;



public class dbteam1 {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws SQLException, IOException
	{
		
		try {
		 int scnum,indexnum,typenum;	
		String query_lat= null;
		String query_lng= null;
		 Scanner scan = new Scanner(System.in);
		 Scanner indexscan = new Scanner(System.in);
		 Scanner typescan = new Scanner(System.in);
		 
		 
		 System.out.println("< 안전을, 걷다 > \n\n");
		 System.out.println("현재 위치를 받아오시겠습니까? (YES : 아무숫자 , NO : 0 ) ");
		 scnum = scan.nextInt();
		 
		 
		 if (scnum == 0) {
			 System.exit(0);
		 }
		 
		 System.out.println("\n 현재 위치를 기반으로한 주변의 안전 서비스 목록을 표시합니다. \n");
		 System.out.println("------------------------------------------------------\n");
		 System.out.println("\t" + "인덱스번호 타입번호" +  " 위도" + "\t\t" + "경도");
		 	 
		 URL whatismyip = new URL("http://checkip.amazonaws.com");
         BufferedReader in = new BufferedReader(new InputStreamReader(
                 whatismyip.openStream()));

         String ip = in.readLine(); //you get the IP as a String
         // System.out.println(ip);
         URL url = new URL("http://api.ipstack.com/"+ ip + "?access_key=d9352d4c38b5a09e0b35f0211126df15&format=1");

         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
         connection.setRequestMethod("GET");
         connection.getResponseCode();

         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         StringBuffer stringBuffer = new StringBuffer();
         String inputLine;
         while ((inputLine = bufferedReader.readLine()) != null)  {
             stringBuffer.append(inputLine);
             stringBuffer.append("\n");
         }
         bufferedReader.close();

         String response = stringBuffer.toString();
        // 아이피 기반 주소 및 상세정보  System.out.println(response);

         JSONObject jsonObject = new JSONObject(response);
         BigDecimal lat = jsonObject.getBigDecimal("latitude");
         BigDecimal lng = jsonObject.getBigDecimal("longitude");
          //System.out.println(lat);
         // System.out.println(lng);
	
       Connection connect = DriverManager.getConnection("jdbc:postgresql://localhost/postgres","super","psw1234");
 		Statement sm = connect.createStatement();
    
       sm.executeUpdate("insert into maps \n"
       		+ "\n"
       		+ "SELECT idx, mtype, latitude, longitude\n"
       		+ "\n"
       		+ "FROM cctv\n"
       		+ "\n"
       		+ "WHERE (6371*acos(cos(radians(" + lat + "))*cos(radians(latitude))*cos(radians(longitude)-radians(" + lng + "))+sin(radians(" + lat + "))*sin(radians(latitude)))) <= 0.5;\n");
       
       sm.executeUpdate("insert into maps \n"
         		+ "\n"
         		+ "SELECT idx, mtype, latitude, longitude\n"
         		+ "\n"
         		+ "FROM police\n"
         		+ "\n"
         		+ "WHERE (6371*acos(cos(radians(" + lat + "))*cos(radians(latitude))*cos(radians(longitude)-radians(" + lng + "))+sin(radians(" + lat + "))*sin(radians(latitude)))) <= 3;\n");
       sm.executeUpdate("insert into maps \n"
          		+ "\n"
          		+ "SELECT idx, mtype, latitude, longitude\n"
          		+ "\n"
          		+ "FROM emergency\n"
          		+ "\n"
          		+ "WHERE (6371*acos(cos(radians(" + lat + "))*cos(radians(latitude))*cos(radians(longitude)-radians(" + lng + "))+sin(radians(" + lat + "))*sin(radians(latitude)))) <= 2;\n");
       
       
      
 ////////////////////////////////////////////////////////////      
       ResultSet rs = sm.executeQuery("select (row_number() over()) as rownum, * from maps;");
    
  
  	while(rs.next()) {
   		
   		System.out.println(rs.getInt(1) + "\t" + rs.getInt(2) + "\t" + rs.getInt(3) + "\t" + rs.getDouble(4) + "\t\t" + rs.getDouble(5));
   		
   	} 
  	
  	sm.executeUpdate("delete from maps;");
////////////////////////////////////////////////////////// 여기는  maps 확인용도       

  	 System.out.println("------------------------------------------------------\n\n");
  	 
  	 System.out.println("<<<<<<<<     MENU     >>>>>>>>\n");
  	 System.out.println("1. 안전 서비스의 상세 정보 \n" + "2. 안전 서비스까지의 경로 \n" + "3. 가장 가까운 안전서비스 까지의 경로\n");
	 scnum = scan.nextInt();
	 
	 if (scnum == 1) {
		 System.out.println("1. 확인하려는 행의 인덱스 번호를 입력해주세요");
		 indexnum = indexscan.nextInt();
		 System.out.println("2. 확인하려는 행의 타입 번호를 입력해주세요  (0 : CCTV | 1 : 경찰서 | 2 : 비상벨) ");
		 typenum = typescan.nextInt();
		 
		if(typenum == 0) {
		rs = sm.executeQuery("select latitude, longitude from cctv where cctv.mtype=" + typenum + " and cctv.idx=" + indexnum + ";");
		
		while(rs.next()) {
	   		
	   		System.out.println("해당 서비스는 cctv이고 위도는 "+ rs.getDouble(1) + "이고 경도는 " + rs.getDouble(2) + " 입니다. \n");
	   		
	   	} 
		
		}
		
		
		else if(typenum == 1) {
			rs = sm.executeQuery("select pName,latitude, longitude, address from police where police .mtype=" + typenum + " and police.idx=" + indexnum + ";");
			
			while(rs.next()) {
		   		
		   		System.out.println("해당 서비스는 "+ rs.getString(1) + "이고 위도는 "+ rs.getDouble(2) + " 이고 경도는 " + rs.getDouble(3) + " 이며 주소는 " + rs.getString(4) + " 입니다. \n");
		   		
		   	} 
			
		
		}
		
		else if(typenum == 2) {
			rs = sm.executeQuery("select latitude, longitude , address , etc from emergency where emergency.mtype=" + typenum + " and emergency.idx=" + indexnum + ";");
			
			while(rs.next()) {
		   		
		   		System.out.println("해당 서비스는 안전 비상벨 이고 위도는 "+ rs.getDouble(1) + "이고 경도는 " + rs.getDouble(2) + " 이며 주소는 " + rs.getString(3) + " 이고 부가기능은  " + rs.getString(4) +"  입니다. \n");
		   		
		   	} 
			
		}
		  
		  
		else {
			System.out.println(" 올바르지 않은 인덱스번호와 타입을 입력하였습니다.\n ");
			 System.exit(0);
		}
		 
	 }
  	 
	 
	 else if (scnum == 2) {
		 
		 System.out.println("1. 경로를 확인하려는 행의 인덱스 번호를 입력해주세요");
		 indexnum = indexscan.nextInt();
		 System.out.println("2. 경로를 확인하려는 행의 타입 번호를 입력해주세요  (0 : CCTV | 1 : 경찰서 | 2 : 비상벨) ");
		 typenum = typescan.nextInt();
		 
		 
		 if(typenum == 0) {
			 rs = sm.executeQuery("select latitude, longitude from cctv where cctv.idx="+ indexnum +";"); 
			  	while(rs.next()) {
			   		query_lat = rs.getString("latitude");
			   		query_lng = rs.getString("longitude");
			   	} 
			  	
			  	
			  	
				 String urlLink = "http://map.naver.com/index.nhn?slng=" +lng+ "&slat="+ lat + "&stext=현재위치&elng="+query_lng+"&elat="+query_lat+"&etext=도착지&menu=route&pathType=1";
			    
				 
				 try{Desktop.getDesktop().browse(new URI(urlLink));
				 }
				 catch (IOException e) {
					 e.printStackTrace();
					 
				 }
				 catch (URISyntaxException e) {
					 e.printStackTrace();
				 }
		 }
		 
		 else if (typenum == 1) {
			 rs = sm.executeQuery("select latitude, longitude from police where police.idx="+ indexnum +";"); 
			  	while(rs.next()) {
			   		query_lat = rs.getString("latitude");
			   		query_lng = rs.getString("longitude");
			   	} 
			  
			  	
			  	
				 String urlLink = "http://map.naver.com/index.nhn?slng=" +lng+ "&slat="+ lat + "&stext=현재위치&elng="+query_lng+"&elat="+query_lat+"&etext=도착지&menu=route&pathType=1";
			    
				 
				 try{Desktop.getDesktop().browse(new URI(urlLink));
				 }
				 catch (IOException e) {
					 e.printStackTrace();
					 
				 }
				 catch (URISyntaxException e) {
					 e.printStackTrace();
				 }
		 }
		 
		 else if (typenum == 2) {
			 rs = sm.executeQuery("select latitude, longitude from emergency where emergency.idx="+ indexnum +";"); 
			  	while(rs.next()) {
			   		query_lat = rs.getString("latitude");
			   		query_lng = rs.getString("longitude");

			   	} 
			  	
			  	
				 String urlLink = "http://map.naver.com/index.nhn?slng=" +lng+ "&slat="+ lat + "&stext=현재위치&elng="+query_lng+"&elat="+query_lat+"&etext=도착지&menu=route&pathType=1";
			    
				 
				 try{Desktop.getDesktop().browse(new URI(urlLink));
				 }
				 catch (IOException e) {
					 e.printStackTrace();
					 
				 }
				 catch (URISyntaxException e) {
					 e.printStackTrace();
				 }
		 }
		 
		 else {
				System.out.println(" 올바르지 않은 인덱스번호와 타입을 입력하였습니다.\n ");
				 System.exit(0);
			}
		 
		 
		 
		 
		 
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 else if (scnum == 3) {
		 
		 
		 System.out.println("가장 가까운 서비스 까지의 경로를 네이버지도로 검색합니다. \n");
		 System.out.println("0.cctv \n");
		 System.out.println("1.경찰서 \n");
		 System.out.println("2.안전비상벨");
		 typenum = typescan.nextInt();

	
	   
		if(typenum == 0) {
			
			rs = sm.executeQuery("select latitude, longitude\n"
				+ "from (select *,  (6371*acos(cos(radians(" + lat + "))*cos(radians(latitude))*cos(radians(longitude)-radians(" + lng + "))+sin(radians(" + lat + "))*sin(radians(latitude)))) as distance\n"
				+ "from cctv \n"
				+ "order by distance\n"
				+ "limit 1) as dis;\n");
	    
		  
	  	while(rs.next()) {
	   		query_lat = rs.getString("latitude");	
	   		query_lng = rs.getString("longitude");
	   	} 
	  	
	  	 
	   
	  
	  	
	  	
	  	
		 String urlLink = "http://map.naver.com/index.nhn?slng=" +lng+ "&slat="+ lat + "&stext=현재위치&elng="+query_lng+"&elat="+query_lat+"&etext=도착지&menu=route&pathType=1";
	    
		 
		 try{Desktop.getDesktop().browse(new URI(urlLink));
		 }
		 catch (IOException e) {
			 e.printStackTrace();
			 
		 }
		 catch (URISyntaxException e) {
			 e.printStackTrace();
		 }
		 
		 
		 
		}
		
		
		
		else if(typenum == 1) {
			
			rs = sm.executeQuery("select latitude, longitude\n"
					+ "from (select *,  (6371*acos(cos(radians(" + lat + "))*cos(radians(latitude))*cos(radians(longitude)-radians(" + lng + "))+sin(radians(" + lat + "))*sin(radians(latitude)))) as distance\n"
					+ "from police \n"
					+ "order by distance\n"
					+ "limit 1) as dis;\n");
		    
			  
		  	while(rs.next()) {
		   		query_lat = rs.getString("latitude");
		   		query_lng = rs.getString("longitude");
		   	} 
		  	
		  	
		  
		  
			 
		  
		  
			 String urlLink = "http://map.naver.com/index.nhn?slng=" +lng+ "&slat="+ lat + "&stext=현재위치&elng="+query_lng+"&elat="+query_lat+"&etext=도착지&menu=route&pathType=1";
				
			 try{Desktop.getDesktop().browse(new URI(urlLink));
			 }
			 catch (IOException e) {
				 e.printStackTrace();
				 
			 }
			 catch (URISyntaxException e) {
				 e.printStackTrace();
			 }
			 
			
		}
		
		
		else if(typenum==2) {
			
			
			rs = sm.executeQuery("select latitude, longitude\n"
					+ "from (select *,  (6371*acos(cos(radians(" + lat + "))*cos(radians(latitude))*cos(radians(longitude)-radians(" + lng + "))+sin(radians(" + lat + "))*sin(radians(latitude)))) as distance\n"
					+ "from emergency \n"
					+ "order by distance\n"
					+ "limit 1) as dis;\n");
		    
			  
		  	while(rs.next()) {
		   		query_lat = rs.getString("latitude");
		   		query_lng = rs.getString("longitude");
		   	} 
		  

		 
			 String urlLink = "http://map.naver.com/index.nhn?slng=" +lng+ "&slat="+ lat + "&stext=현재위치&elng="+query_lng+"&elat="+query_lat+"&etext=도착지&menu=route&pathType=1";
				
			 try{Desktop.getDesktop().browse(new URI(urlLink));
			 }
			 catch (IOException e) {
				 e.printStackTrace();
				 
			 }
			 catch (URISyntaxException e) {
				 e.printStackTrace();
			 }
			 
		}
		
		else {
			 System.out.println(" 올바르지 않은 메뉴를 입력하였습니다.\n ");
			 System.exit(0);
		 }
			
		
		
		
		
		
		
		
		
		
		
		
		
		
	 }
	 
	 
	 else {
		 System.out.println(" 올바르지 않은 메뉴를 입력하였습니다.\n ");
		 System.exit(0);
	 }
  	 
  	 
  	 
  	 
  	 
  	 
  	 
  	 
  	
  	
  	 
  	
  	
  	 indexscan.close();
  	 scan.close();
  	 rs.close();
  	 typescan.close();
  	 
  	 
  	System.exit(0);
  	 
  	 
		}
		
		
		
		catch(SQLException ex)
		{
		throw ex;
		}
		
		
		}
}
