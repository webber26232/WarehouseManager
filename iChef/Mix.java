package iChef;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Mix {

	public static Vector<Vector> showMixCondiments(Statement s, int mixID) throws SQLException {
		Vector<Vector> results = new Vector<Vector>();
		ResultSet rsMixCondiment = s.executeQuery("SELECT * FROM Mix_Condiemnt AS mc WHERE MixID="+mixID+" LEFT JOIN Condiment AS cd ON mm.CondimentID = cd.ID");
		while(rsMixCondiment.next()){
			Vector row = new Vector();
			row.addElement(rsMixCondiment.getString("CondimentID"));
			row.addElement(rsMixCondiment.getString("CondimentName"));
			Vector measurement = Meal.selectMeasurement(rsMixCondiment.getDouble("Volume"),rsMixCondiment.getDouble("Weight"),rsMixCondiment.getInt("Quantity"));
			row.addElement(measurement.get(0));
			row.addElement(measurement.get(1));
			results.addElement(row);
		}
		return results;	
	}
	public static void addMixCondiment(Statement s, int mixID) throws SQLException {
		int condimentID = 0;
		String volume = null,weight = null;
		s.executeUpdate("INSERT INTO Mix_Condiment VALUES ("+mixID+","+condimentID+","+volume+","+weight+")");
	}
	
	public static void editMixCondiment(Statement s, int mixID) throws SQLException {
		int condimentID = 0;
		String volume = null,weight = null;
		s.executeUpdate("UPDATE Mix_Condiment SET CondimentID ="+condimentID+", Volume="+volume+", Weight="+weight+" WHERE MixID="+mixID);
	}
	public static void deleteMixCondiment(Statement s, int mixID) throws SQLException {
		int condimentID = 0;
		s.executeUpdate("DELTE FROM MealMix WHERE MixlID="+mixID+" AND CondimentID="+condimentID);
	}
}
