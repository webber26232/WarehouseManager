package iChef;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Ingredient {
	public static Vector<Vector> showIngredients(Statement s) throws SQLException {
		// TODO Auto-generated method stub
		Vector<Vector> results = new Vector<Vector>();
		ResultSet rsIngredient = s.executeQuery("SELECT * FROM Ingredient AS i LEFT JOIN Ingredient_Stock AS is ON i.ID=is.IngredientID");
		while (rsIngredient.next()){
			Vector row = new Vector();
			row.addElement(rsIngredient.getString("ID"));
			row.addElement(rsIngredient.getString("IngredientName"));
			Vector measurement = Meal.selectMeasurement(rsIngredient.getDouble("Volume"),rsIngredient.getDouble("Weight"),rsIngredient.getInt("Quantity"));
			row.addElement(measurement.get(0));
			row.addElement(measurement.get(1));
			row.addElement(rsIngredient.getDouble("UnitPrice"));
			results.addElement(row);
		}
		return results;
	}
	public static void addIngredient(Statement s) throws SQLException{
		String ingredientName = "";
		s.executeUpdate("INSERT INTO Ingredient VALUES ("+ingredientName+", GETDATE())");
	}
	public static void editIngredient(Statement s) throws SQLException{
		String newIngredientName = "";
		int ingredientID = 0;
		s.executeUpdate("UPDATE Ingredient SET IngredientName =\""+newIngredientName+"\" WHERE ID="+ingredientID);
	}
	public static void deleteIngredient(Statement s) throws SQLException{
		int ingredientID = 0;
		s.executeUpdate("DELETE FROM Ingredient WHERE ID="+ingredientID);
	}
}
