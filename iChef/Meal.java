package iChef;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;

public class Meal {
	public static Vector<Vector> showMeals(Statement s) throws SQLException {
		// TODO Auto-generated method stub
		Vector<Vector> results = new Vector<Vector>();
		ResultSet rs = s.executeQuery("SELECT m.ID, m.MealName, c.ContainerName, m.AddDate FROM Meal AS m LEFT JOIN Container AS c ON m.ContainerID = c.ID");
		while (rs.next()){
			Vector row = new Vector();
			int mealID = rs.getInt("MealID");
			row.addElement(mealID);
			row.addElement(rs.getString("MealName"));
			row.addElement(rs.getString("ContainerName"));
			row.addElement(rs.getString("addDate"));
			row.addElement(calculateMealCosts(s,mealID).get("total"));
			results.addElement(row);
		}
		return results;	
	}
	public static Vector<Vector> showMealIngredients(Statement s,int mealID) throws SQLException{
		Vector<Vector> results = new Vector<Vector>();
		ResultSet rsMealIngredient = s.executeQuery("SELECT * FROM Meal_Ingredient AS mi, Ingredient AS i, Container AS ct "
				+ "WHERE mi.ingredientID = i.ID AND mi.ContainerID = ct.ID AND MealID="+mealID);
		while(rsMealIngredient.next()){
			Vector row = new Vector();
			row.addElement(rsMealIngredient.getString("IngredientID"));
			row.addElement(rsMealIngredient.getString("IngredientName"));
			row.addElement(rsMealIngredient.getString("CuttingMethod"));
			row.addElement(rsMealIngredient.getString("ContainerName"));
			Vector measurement = selectMeasurement(rsMealIngredient.getDouble("Volume"),rsMealIngredient.getDouble("Weight"),rsMealIngredient.getInt("Quantity"));
			row.addElement(measurement.get(0));
			row.addElement(measurement.get(1));
			results.addElement(row);
		}
		return results;
	}
	public static Vector<Vector> showMealCondiments(Statement s,int mealID) throws SQLException{
		Vector<Vector> results = new Vector<Vector>();
		ResultSet rsMealCondiment = s.executeQuery("SELECT * FROM Meal_Condiment AS mc, Condiment AS cd, Container AS ct "
				+ "WHERE mc.CondimentID = cd.ID AND mc.ContainerID = ct.ID AND MealID="+mealID);
		while(rsMealCondiment.next()){
			Vector row = new Vector();
			row.addElement(rsMealCondiment.getString("CondimentID"));
			row.addElement(rsMealCondiment.getString("CondimentName"));
			row.addElement(rsMealCondiment.getString("ContainerName"));
			Vector measurement = selectMeasurement(rsMealCondiment.getDouble("Volume"),rsMealCondiment.getDouble("Weight"),rsMealCondiment.getInt("Quantity"));
			row.addElement(measurement.get(0));
			row.addElement(measurement.get(1));
			results.addElement(row);
		}
		return results;	
	}
	public static Vector<Vector> showMealMixes(Statement s,int mealID) throws SQLException {
		// TODO Auto-generated method stub
		Vector<Vector> results = new Vector<Vector>();
		ResultSet rsMealMix = s.executeQuery("SELECT * FROM Meal_Mix AS mm LEFT JOIN Container AS ct ON mm.ContainerID = ct.ID WHERE MealID="+mealID);
		while(rsMealMix.next()){
			Vector row = new Vector();
			row.addElement(rsMealMix.getString("ID"));
			row.addElement(rsMealMix.getString("MixName"));
			row.addElement(rsMealMix.getString("ConainerName"));
			results.addElement(row);
		}
		return results;	
	}
	public static Vector<Vector> showMealContainers(Statement s,int mealID) throws SQLException {
		Vector<Vector> results = new Vector<Vector>();
		HashMap<Integer,Integer> containerNo = new HashMap<Integer,Integer>();
		HashMap<Integer,String> containerName = new HashMap<Integer,String>();
		ResultSet rsMealContainer = s.executeQuery("SELECT ContainerID, ContainerName FROM Meal AS m LEFT JOIN Container AS c ON m.ContainerID = c.ID WHERE m.ID="+mealID);
		ResultSet rsMealIngredientContainer = s.executeQuery("SELECT ContainerID, ContainerName FROM Meal_Ingredient AS mi LEFT JOIN Container AS c ON mi.ContainerID = c.ID WHERE mi.MealID="+mealID);
		ResultSet rsMealCondimentContainer = s.executeQuery("SELECT ContainerID, ContainerName FROM Meal_Condiment AS mc LEFT JOIN Container AS c ON mc.ContainerID = c.ID WHERE mc.MealID="+mealID);
		ResultSet rsMealMixContainer = s.executeQuery("SELECT ContainerID, ContainerName FROM Meal_Mix AS mm LEFT JOIN Container AS c ON mm.ContainerID = c.ID WHERE mm.MealID="+mealID);
		calculateContainer(rsMealContainer,containerNo,containerName);
		calculateContainer(rsMealIngredientContainer,containerNo,containerName);
		calculateContainer(rsMealCondimentContainer,containerNo,containerName);
		calculateContainer(rsMealMixContainer,containerNo,containerName);
		for(int containerID:containerNo.keySet()){
			Vector row = new Vector();
			row.addElement(containerID);
			row.addElement(containerName.get(containerID));
			row.addElement(containerNo.get(containerID));
			results.addElement(row);
		}
		return results;
	}
	public static HashMap<String,Double> calculateMealCosts(Statement s, int mealID) throws SQLException {
		// TODO Auto-generated method stub
		HashMap<String, Double> costs = new HashMap<String, Double>();
		costs.put("containerSum", 0.0);
		costs.put("ingredientSum", 0.0);
		costs.put("condimentSum", 0.0);
		HashMap<Integer,Integer> containerNo = new HashMap<Integer,Integer>();
		ResultSet rsMealIngredient = s.executeQuery("SELECT * FROM Meal_Ingredient WHERE MealID="+mealID);
		ResultSet rsMealCondiment = s.executeQuery("SELECT * FROM Meal_Condiment WHERE MealID="+mealID);
		ResultSet rsMealMix = s.executeQuery("SELECT * FROM Meal_Mix WHERE MealID="+mealID);	
		ResultSet rsMealContainer = s.executeQuery("SELECT ContainerID FROM Meal WHERE ID="+mealID);
		while(rsMealContainer.next()){
			containerNo.put(rsMealContainer.getInt("ContainerID"), 1);
		}
		while(rsMealIngredient.next()){
			int containerID = rsMealIngredient.getInt("ContainerID");
			int ingredientID = rsMealIngredient.getInt("IngredientID");
			double volume = 0, weight = 0, quantity = 0, measurement = 0;
			volume = rsMealIngredient.getDouble("Volume");
			weight = rsMealIngredient.getInt("Weight");
			quantity = rsMealIngredient.getInt("Quantity");
			if(volume!=0){
				measurement = volume;
			}else if(weight!=0){
				measurement = weight;
			}else if(quantity!=0){
				measurement = quantity;
			}
			costs.put("ingredientSum", s.executeQuery("SELECT UnitPrice FROM Ingredient_Stock "
					+ "WHERE IngredientID="+ingredientID).getDouble("UnitPrice")*measurement+costs.get("ingredientSum"));
			if(containerNo.containsKey(containerID)){
				containerNo.put(containerID,containerNo.get(containerID)+1);
			}else{
				containerNo.put(containerID, 1);
			}
		}
		rsMealIngredient.close();
		while(rsMealCondiment.next()){
			int containerID = rsMealCondiment.getInt("ContainerID");
			int condimentID = rsMealCondiment.getInt("CondimentID");
			double volume = 0, weight = 0, quantity = 0, measurement = 0;
			volume = rsMealCondiment.getDouble("Volume");
			weight = rsMealCondiment.getInt("Weight");
			weight = rsMealCondiment.getInt("Quantity");
			if(volume!=0){
				measurement = volume;
			}else if(weight!=0){
				measurement = weight;
			}else if(quantity!=0){
				measurement = quantity;
			}
			costs.put("condimentSum", s.executeQuery("SELECT UnitPrice FROM Condiment_Stock "
					+ "WHERE CondimentID="+condimentID).getDouble("UnitPrice")*measurement+costs.get("CondimentSum"));
			if(containerNo.containsKey(containerID)){
				containerNo.put(containerID,containerNo.get(containerID)+1);
			}else{
				containerNo.put(containerID, 1);
			}
		}
		rsMealCondiment.close();
		while(rsMealMix.next()){
			int containerID = rsMealMix.getInt("ContainerID");
			int mixID = rsMealMix.getInt("MixID");
			ResultSet rsMixCondiment = s.executeQuery("SELECT CondimentID, Volume, Weight, Quantity FROM Mix_Condiment WHERE MixID="+mixID);
			while(rsMixCondiment.next()){
				int condimentID = rsMixCondiment.getInt("CondimentID");
				double volume = 0, weight = 0, quantity = 0, measurement = 0;
				volume = rsMixCondiment.getDouble("Volume");
				weight = rsMixCondiment.getInt("Weight");
				weight = rsMixCondiment.getInt("Quantity");
				if(volume!=0){
					measurement = volume;
				}else if(weight!=0){
					measurement = weight;
				}else if(quantity!=0){
					measurement = quantity;
				}
				costs.put("condimentSum", s.executeQuery("SELECT UnitPrice FROM Condiment_Stock "
						+ "WHERE CondimentID="+condimentID).getDouble("UnitPrice")*measurement+costs.get("CondimentSum"));
			}
			rsMixCondiment.close();
			if(containerNo.containsKey(containerID)){
				containerNo.put(containerID,containerNo.get(containerID)+1);
			}else{
				containerNo.put(containerID, 1);
			}
		}
		rsMealMix.close();
		ResultSet rsContainerPrice = s.executeQuery("SELECT ContainerID, UnitPrice FROM Container_Stock");
		while(rsContainerPrice.next()){
			int containerID = rsContainerPrice.getInt("ContainerID");
			if(containerNo.containsKey(containerID)){
				costs.put("containerSum", containerNo.get(containerID)*rsContainerPrice.getDouble("UnitPrice")+costs.get("containerSum"));
			}		
		}
		rsContainerPrice.close();
		costs.put("total", costs.get("containerSum")+costs.get("ingredientSum")+costs.get("condimentSum"));
		return costs;
	}
	public static void addMealCondiment(Statement s, int mealID) throws SQLException {
		int condimentID = 0;
		int containerID = 0;
		String volume = null,weight = null;
		s.executeUpdate("INSERT INTO Meal_Condiment VALUES ("+mealID+", "+condimentID+","+containerID+","+volume+","+weight+")");
	}
	public static void editMealCondiment(Statement s, int mealID) throws SQLException {
		// TODO Auto-generated method stub
		int condimentID = 0;
		int containerID = 0;
		String volume = null,weight = null;
		s.executeUpdate("UPDATE Meal_Condiment SET ContainerID="+containerID+", Volume="+volume+", Weight="+weight);
	}
	public static void deleteMealCondiment(Statement s, int mealID) throws SQLException {
		int condimentID = 0;
		s.executeUpdate("DELTE FROM Meal_Condiment WHERE MealID="+mealID+" AND CondimentID="+condimentID);
	}
	public static void addMealMix(Statement s, int mealID) throws SQLException {
		int containerID = 0;
		String mixName = null;
		s.executeUpdate("INSERT INTO Meal_Mix VALUES ("+mealID+","+mixName+","+containerID+")");
		//int newID = s.executeQuery("SELECT TOP 1 ID FROM Meal_Mix ORDER BY ID DESC").getInt("MixID");

	}
	public static void deleteMealMix(Statement s, int mealID) throws SQLException {
		// TODO Auto-generated method stub
		int mixID = 0;
		s.executeUpdate("DELTE FROM Mix_Condiment WHERE MixlID="+mixID);
		s.executeUpdate("DELTE FROM MealMix WHERE MixlID="+mixID);
	}
	
	public static void addMealIngredient(Statement s, int mealID) throws SQLException {
		// TODO Auto-generated method stub
		int ingredientID = 0;
		int containerID = 0;
		String volume = null,weight = null,quantity = null, cuttingMethod = null;
		s.executeUpdate("INSERT INTO Meal_Ingredient VALUES ("+mealID+", "+ingredientID+","+containerID+","+volume+","+weight+","+quantity+","+cuttingMethod+")");
	}

	public static void editMealIngredient(Statement s, int mealID) throws SQLException {
		// TODO Auto-generated method stub
		int ingredientID = 0;
		int containerID = 0;
		String volume = null,weight = null,quantity = null, cuttingMethod = null;
		s.executeUpdate("UPDATE Meal_Ingredient SET Volume="+volume+", Weight="+weight+", Quantity="+quantity+", "
				+ "CuttingMethod="+cuttingMethod+" WHERE MealID="+mealID+" AND IngredientID="+ingredientID);
	}
	
	public static void deleteMealIngredient(Statement s, int mealID) throws SQLException {
		// TODO Auto-generated method stub
		int ingredientID = 0;
		int containerID = 0;
		s.executeUpdate("DELTE FROM Meal_Ingredient WHERE MealID="+mealID+" AND IngredientID="+ingredientID);
	}
	
	public static void addMeal(Statement s) throws SQLException {
		// TODO Auto-generated method stub
		String mealName = "";
		Container.showContainers(s);
		int containerID = 0;
		s.executeUpdate("INSERT INTO Meal VALUES ("+mealName+" , "+containerID+" , GETDATE())");
	}
	
	public static void editMeal(Statement s, int mealID) throws SQLException {
		// TODO Auto-generated method stub
		String newMealName = "";
		s.executeUpdate("UPDATE Meal SET MealName =\""+newMealName+"\" WHERE ID="+mealID);
	}
	
	public static void deleteMeal(Statement s, int mealID) throws SQLException {
		// TODO Auto-generated method stub
		s.executeUpdate("DELETE FROM Mix_Condiment WHERE MixID = SELECT ID FROM Meal_Mix WHERE MealID="+mealID);
		s.executeUpdate("DELETE FROM Meal_Condiment, Meal_Ingredient, Meal_Mix WHERE ID="+mealID);
		s.executeUpdate("DELETE FROM Meal WHERE ID="+mealID);
	}
	public static Vector selectMeasurement(double volume,double weight,int quantity){
		Vector measurement = new Vector();
		if(volume!=0){
			measurement.addElement(volume);
			measurement.addElement("fl oz");
		}else if(weight!=0){
			measurement.addElement(weight);
			measurement.addElement("g");
		}else if(quantity!=0){
			measurement.addElement(quantity);
			measurement.addElement("units");
		}
		return measurement;
	}
	private static void calculateContainer(ResultSet rs, HashMap<Integer,Integer> containerNo, HashMap<Integer,String> containerName) throws SQLException{
		while(rs.next()){
			int containerID = rs.getInt("ContainerID");		
			if(containerNo.containsKey(containerID)){
				containerNo.put(containerID,containerNo.get(containerID)+1);
			}else{
				containerNo.put(containerID, 1);
				containerName.put(containerID, rs.getString("ContainerName"));
			}
		}
	}
}