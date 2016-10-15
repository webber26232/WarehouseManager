package iChef;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Condiment {
	public static Vector<Vector> showCondiments(Statement s) throws SQLException {
		// TODO Auto-generated method stub
		Vector<Vector> results = new Vector<Vector>();
		ResultSet rsCondiment = s.executeQuery("SELECT * FROM Condiment AS c LEFT JOIN Condiment_Stock AS cs ON c.ID=cs.CondimentID");
		while (rsCondiment.next()){
			Vector row = new Vector();
			row.addElement(rsCondiment.getString("ID"));
			row.addElement(rsCondiment.getString("CondimentName"));
			Vector measurement = Meal.selectMeasurement(rsCondiment.getDouble("Volume"),rsCondiment.getDouble("Weight"),rsCondiment.getInt("Quantity"));
			row.addElement(measurement.get(0));
			row.addElement(measurement.get(1));
			row.addElement(rsCondiment.getDouble("UnitPrice"));
			results.addElement(row);
		}
		return results;
	}
	public static void addCondiment(Statement s, String condimentName) throws SQLException{
		s.executeUpdate("INSERT INTO Condiment VALUES ("+condimentName+", GETDATE())");
	}
	public static void editCondiment(Statement s,int condimentID, String newCondimentName) throws SQLException{
		s.executeUpdate("UPDATE Condiment SET CondimentName =\""+newCondimentName+"\" WHERE ID="+condimentID);
	}
	public static void deleteCondiment(Statement s, int condimentID) throws SQLException{
		s.executeUpdate("DELETE FROM Condiment WHERE ID="+condimentID);
	}
}
