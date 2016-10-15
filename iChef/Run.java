package iChef;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Run {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Statement s = DBController.getStatement();		
		Windows.initializeFrame();
	}
	public static void purchasing(Statement s) throws SQLException {
		String supplier = null, date = null;
		double tax = 0, shipping = 0;
		s.executeUpdate("INSER INTO Purchasing_Order VALUES ("+supplier+","+date+","+tax+","+shipping+")");
		int newID = s.executeQuery("SELECT TOP 1 ID FROM Purchasing_Order ORDER BY ID DESC").getInt("ID");


	}
}
