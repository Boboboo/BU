package com.kcc.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import com.kcc.db.DBConnection;
import com.kcc.entity.OutputReport;

public class ProcessManager {

	/**
	 * Define the global mOutputList to contain all the outputReport
	 */
	ArrayList<OutputReport> mOutputList = new ArrayList<>();

	
	/**
	 * The make report progress for each OutputReport in the mOutputList
	 */
	public void progress(String outputPath) {
		getAllFloridas("Florida, USA");
		writeReport(outputPath);
	}

	
	/**
	 * Get all required Florida (landfall hurricanes since 1900)
	 * @param givenState 
	 */
	private void getAllFloridas(String givenState) {
		//make DB connection
		DBConnection DBconn = new DBConnection();
		Connection conn = DBconn.getDBConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		String sql = "";
		String day = null;
		String time = null;
		int max_speed = 0;
		String name;

		try {
			statement = conn.createStatement();
			
			//query all records from hurricane table where h_state='Florida, USA';
			sql = "SELECT * FROM hurricane WHERE h_state='"+givenState+"'";
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				day = resultSet.getString("h_day");
				time = resultSet.getString("h_time");
				max_speed = resultSet.getInt("h_max_speed");
				name = resultSet.getString("h_name");
				Date date = formatDate(day, time);
				OutputReport outputReport = new OutputReport(name, date, max_speed);

				// put all the outputReport in the mOutputList
				mOutputList.add(outputReport);
			}
			DBconn.cleanConnection(null, statement, conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Write report result on path
	 */
	private void writeReport(String outputPath) {
		//given the output path where to create output report.txt file 
		outputPath=outputPath + File.separator + "report.txt";
		System.out.println("File generate:" + outputPath);
		File file = new File(outputPath);
		
		//if the prepared output file report.txt does not exist in the path, create a report.txt
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Create File Failed");
			}
		}
		
		//if the report.txt exist, write output results in this report.txt file 
		try {
			FileWriter fileWriter = new FileWriter(outputPath);

			for (OutputReport outputReport : mOutputList) {
				String name = outputReport.getName();
				Date date = outputReport.getDate();
				int max_speed = outputReport.getMax_speed();
				String string = name + ", " + date + ", " + max_speed;

				// write into file line by line
				fileWriter.write(string + "\r\n");
			}
			fileWriter.close();
			System.out.println("Output report.txt file has been created successfully.");
		} catch (Exception var12) {
			var12.printStackTrace();
			System.out.println("Invalid Path");
		}

	}

	
	 /**
     * Format date 
     * Return each formatted date
     */
	private Date formatDate(String day, String time) {
		//get the year,month,day,hour and minute from each day+time string  EX '200408141400'
		String year=(day+time).substring(0, 4);
		String month=(day+time).substring(4, 6);
		String theDay=(day+time).substring(6, 8);
		String hour=(day+time).substring(8, 10);
		String minute=(day+time).substring(10, 12);
		
		Date date=null;
		
		try {
			//parse each day+time string
			date = new SimpleDateFormat("yyyy/MM/dd hh:mm").parse(year + "/"+ month +"/"+ theDay +" "+ hour +":" + minute);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
}
