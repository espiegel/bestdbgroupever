package FreeBase;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class testFileUploader {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		Connection connect=null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String address = "jdbc:mysql://localhost:3305/DbMysql02";
			String user = "DbMysql02", pass = "DbMysql02";
			connect = DriverManager.getConnection(address, user, pass);
		} catch (Exception e) {}
		
		File media,cast;
		
		/*
		//TV upload example
		media =new File("TV\\tv_program.tsv");
		cast =new File("TV\\regular_tv_appearance.tsv");
		try {
			FileUploader fu = new FileUploader(connect, media, cast, true);
			fu.upload(10);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Upload Completed !");
		*/
		
		//Film Upload example
		media =new File("Film\\film.tsv");
		cast =new File("Film\\performance.tsv");
		try {
			FileUploader fu = new FileUploader(connect, media, cast, false);
			fu.upload(200);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Upload Completed !");
	}

}
