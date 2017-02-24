package net.intigral.code.verification.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DBHelper {

	private static DBHelper instance;
	private static String DB_DRIVER;
	
	private static String DB_CONNECTION;
	private static String DB_USER;
	private static String DB_PASSWORD;
	
	private static final String PIN_INSERT_QUERY = "INSERT INTO USER_PIN(USER_ID,PIN) VALUES(?,?)";
	private static final String PIN_VERIFY_QUERY = "SELECT 1 FROM USER_PIN WHERE USER_ID=? AND PIN=?";

	private DBHelper(){}

	public static DBHelper getInstance(){
		if (instance == null) {
			instance = new DBHelper();
			instance.loadDbConfiurations();
		}
		return instance;
	}
	
	public static void main(String[] argv) {

		try {
//			DBHelper.getInstance().insertPin("USER_1", "6tvd5l");
			DBHelper.getInstance().verifyPin("USER_1", "6tvd5l");
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	private void loadDbConfiurations() {
		DB_DRIVER = ApplicationConfiguration.getInstance().getPropertryValue("DB_DRIVER");
		DB_CONNECTION = ApplicationConfiguration.getInstance().getPropertryValue("DB_CONNECTION");
		DB_USER = ApplicationConfiguration.getInstance().getPropertryValue("DB_USER");
		DB_PASSWORD = ApplicationConfiguration.getInstance().getPropertryValue("DB_PASSWORD");
		
		System.out.println(DB_DRIVER + "\n" + DB_CONNECTION + "\n" +DB_USER + "\n" +DB_PASSWORD);
	}

	public void insertPin(String userId) throws Exception {
		System.out.println("\nExecuting statement: \n" + PIN_INSERT_QUERY + "\n\nOn parameters: \n[" + 
				"User Id:" + userId +
				 "]");
		String pin = getRandomPin();
		Connection dbConnection = null;
		PreparedStatement statement = null;
		
		try {
			dbConnection = getDBConnection("COMS");
			statement = dbConnection.prepareStatement(PIN_INSERT_QUERY);
            
			statement.setObject(1, userId);
			statement.setObject(2, pin);
			
			int count = statement.executeUpdate();

			System.out.println("\nNumber of affected rows: " + count);

		} catch (Throwable e) {
            e.printStackTrace();
            throw e;

		} finally {
			if (statement != null) {
				statement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}
	
	public boolean verifyPin(String userId, String pin) throws Exception {
		System.out.println("\nExecuting statement: \n" + PIN_VERIFY_QUERY + "\n\nOn parameters: \n[" + 
				"User Id:" + userId +
				", Request Key:" + pin +
				 "]");
		Connection dbConnection = null;
		PreparedStatement statement = null;
		
		try {
			dbConnection = getDBConnection("COMS");
			statement = dbConnection.prepareStatement(PIN_VERIFY_QUERY);
            
			statement.setObject(1, userId);
			statement.setObject(2, pin);
			
			ResultSet rs = statement.executeQuery();
			int result = 0;
			while (rs.next()) {
				result = rs.getInt(1);
				System.out.println("\nResult: " + (1 == result));
				return 1 == result;
		    }
		} catch (Throwable e) {
            e.printStackTrace();
            throw e;
		} finally {
			if (statement != null) {
				statement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		
		return false;
	}
	
	private static Connection getDBConnection(String ds) throws ClassNotFoundException, SQLException {

		Connection dbConnection = null;

		Class.forName(DB_DRIVER);
		
		dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
		
		return dbConnection;
	}
	
	private String getRandomPin() {
		SecureRandom random = new SecureRandom();
	    return new BigInteger(30, random).toString(32);
	}
}