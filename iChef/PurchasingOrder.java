package iChef;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;

public class PurchasingOrder {
	public static Vector<Vector> showPurchasingOrders(Statement s) throws SQLException{
		Vector<Vector> results = new Vector<Vector>();
		ResultSet rsPurchasingOrder = s.executeQuery("SELECT * FROM Purchasing_Order ORDER BY PurchasingDate DESC");		
		while (rsPurchasingOrder.next()){
			int purchasingOrderID = rsPurchasingOrder.getInt("ID");
			Vector row = new Vector();
			row.addElement(purchasingOrderID);
			row.addElement(calculateCosts(s,purchasingOrderID).get("total")+rsPurchasingOrder.getDouble("Tax")+rsPurchasingOrder.getDouble("Shipping"));
			row.addElement(rsPurchasingOrder.getString("Supplier"));
			row.addElement(rsPurchasingOrder.getInt("PurchasingDate"));
			results.addElement(row);
		}
		return results;
	}
	public static HashMap<String,Double> calculateCosts(Statement s, int purchasingOrderID) throws SQLException{
		HashMap<String,Double> costs = new HashMap<String,Double>();
		costs.put("containerSum", 0.0);
		costs.put("ingredientSum", 0.0);
		costs.put("condimentSum", 0.0);
		ResultSet rsPurchasingOrderCondiment = s.executeQuery("SELECT SUM(Price)FROM Purchasing_Order_Condiment WHERE OrderID ="+purchasingOrderID);
		ResultSet rsPurchasingOrderIngredient = s.executeQuery("SELECT SUM(Price)FROM Purchasing_Order_Ingredient WHERE OrderID ="+purchasingOrderID);
		ResultSet rsPurchasingOrderContainer = s.executeQuery("SELECT SUM(Price)FROM Purchasing_Order_Container WHERE OrderID ="+purchasingOrderID);
		if (rsPurchasingOrderCondiment.next()){
			costs.put("condimentSum", rsPurchasingOrderCondiment.getDouble(1));
		}
		if(rsPurchasingOrderIngredient.next()){
			costs.put("ingredientSum",rsPurchasingOrderIngredient.getDouble(1));
		}
		if(rsPurchasingOrderContainer.next()){
			costs.put("containerSum",rsPurchasingOrderContainer.getDouble(1));
		}
		costs.put("total", costs.get("containerSum")+costs.get("ingredientSum")+costs.get("condimentSum"));
		return costs;
	}
	public static Vector<Vector> showPurchasingOrderCondiments(Statement s, int purchasingOrderID) throws SQLException{
		Vector<Vector> results = new Vector<Vector>();
		ResultSet rsPurchasingOrderCondiment = s.executeQuery("SELECT poc.CondimentID, c.CondimentName, poc.Volume, poc.Weight, poc.Quantity, poc.Price"
				+ "FROM Purchasing_Order_Condiment AS poc LEFT JOIN Condiment AS c ON poc.CondimentID=c.ID WHERE poc.OrderID ="+purchasingOrderID);
		while(rsPurchasingOrderCondiment.next()){
			Vector row = new Vector();
			row.addElement(rsPurchasingOrderCondiment.getString("ID"));
			row.addElement(rsPurchasingOrderCondiment.getString("CondimentName"));
			Vector measurement = Meal.selectMeasurement(rsPurchasingOrderCondiment.getDouble("Volume"),rsPurchasingOrderCondiment.getDouble("Weight"),rsPurchasingOrderCondiment.getInt("Quantity"));
			row.addElement(measurement.get(0));
			row.addElement(measurement.get(1));
			row.addElement(rsPurchasingOrderCondiment.getDouble("Price"));
			results.addElement(row);
		}
		return results;
	}
	public static Vector<Vector> showPurchasingOrderIngredients(Statement s, int purchasingOrderID) throws SQLException{
		Vector<Vector> results = new Vector<Vector>();
		ResultSet rsPurchasingOrderIngredient = s.executeQuery("SELECT poi.IngredientID, i.IngredientName, poi.Volume, poi.Weight, poi.Quantity, poi.Price"
				+ "FROM Purchasing_Order_Ingredient AS poi LEFT JOIN Ingredient AS i ON poi.IngredientID=i.ID WHERE poi.OrderID ="+purchasingOrderID);
		while(rsPurchasingOrderIngredient.next()){
			Vector row = new Vector();
			row.addElement(rsPurchasingOrderIngredient.getString("ID"));
			row.addElement(rsPurchasingOrderIngredient.getString("IngredientName"));
			Vector measurement = Meal.selectMeasurement(rsPurchasingOrderIngredient.getDouble("Volume"),rsPurchasingOrderIngredient.getDouble("Weight"),rsPurchasingOrderIngredient.getInt("Quantity"));
			row.addElement(measurement.get(0));
			row.addElement(measurement.get(1));
			row.addElement(rsPurchasingOrderIngredient.getDouble("Price"));
			results.addElement(row);
		}
		return results;
	}
	public static Vector<Vector> showPurchasingOrderContainers(Statement s, int purchasingOrderID) throws SQLException{
		Vector<Vector> results = new Vector<Vector>();
		ResultSet rsPurchasingOrderContainers = s.executeQuery("SELECT poc.ContainerID, c.ContainerName, poc.Quantity, poc.Price "
				+ "FROM Purchasing_Order_Container AS poc LEFT JOIN Container AS c ON poc.ContainerID=c.ID WHERE poc.OrderID="+purchasingOrderID);
		while (rsPurchasingOrderContainers.next()){
			Vector row = new Vector();
			row.addElement(rsPurchasingOrderContainers.getInt("ID"));
			row.addElement(rsPurchasingOrderContainers.getString("ContainerName"));
			row.addElement(rsPurchasingOrderContainers.getInt("Quantity"));
			row.addElement(rsPurchasingOrderContainers.getInt("Price"));
			results.addElement(row);
		}
		return results;		
	}
	public static void addPurchasingOrder(Statement s, String supplier, double tax, double shipping) throws SQLException{
		s.executeUpdate("INSER INTO Purchasing_Order VALUES("+supplier+","+tax+","+shipping+")");
	}
	public static void addPurchasingContainer(Statement s, int orderID, int containerID, int quantity, double price) throws SQLException {
		// TODO Auto-generated method stub
		//From here
		Container.showContainers(s);
		s.executeUpdate("INSER INTO Purchasing_Order_Container VALUES ("+orderID+","+containerID+","+quantity+","+price+")");
		ResultSet rsContainerStock = s.executeQuery("SELECT Quantity, UnitPrice From Container_Stock WHERE ContainerID="+containerID);
		int formerQuantity = rsContainerStock.getInt("Quantity");
		int newQuantity = formerQuantity + quantity;
		double formerUnitPrice = rsContainerStock.getDouble("UnitPrice");
		s.executeUpdate("UPDATE Container_Stock SET Quantity="+newQuantity+", "
				+ "UnitPrice="+((formerQuantity*formerUnitPrice+price)/newQuantity)+" WHERE ContainerID="+containerID);
	}
	public static void addPurchsingIngredient(Statement s, int orderID, int ingredientID, double volume, double weight, int quantity, double price) throws SQLException {
		// TODO Auto-generated method stub
		Ingredient.showIngredients(s);
		s.executeUpdate("INSER INTO Purchasing_Order_Ingredient VALUES ("+orderID+","+ingredientID+","+volume+","+weight+","+quantity+","+price+")");
		ResultSet rsContainerStock = s.executeQuery("SELECT Volume, Weight, Quantity, UnitPrice From Ingredient_Stock WHERE IngredientID="+ingredientID);
		int formerQuantity = rsContainerStock.getInt("Quantity");
		int newQuantity = formerQuantity + quantity;
		double formerVolume = rsContainerStock.getDouble("Volume"),
				formerWeight = rsContainerStock.getDouble("Weight"),
				formerUnitPrice = rsContainerStock.getDouble("UnitPrice");
		double newVolume = formerVolume + volume;
		double newWeight = formerWeight + weight;
		s.executeUpdate("UPDATE Ingredient_Stock SET Volume="+newVolume+", Weight="+newWeight+", Quantity="+newQuantity+", "
				+ "UnitPrice="+(((formerVolume+formerWeight+formerQuantity)*formerUnitPrice+price)/(newVolume+newWeight+newQuantity))+" "
						+ "WHERE IngredientID="+ingredientID);
	}
	public static void addPurchasingCondiment(Statement s, int orderID, int condimentID, double volume, double weight, int quantity, double price) throws SQLException {
		// TODO Auto-generated method stub
		Condiment.showCondiments(s);	
		s.executeUpdate("INSER INTO Purchasing_Order_Condiment VALUES ("+orderID+","+condimentID+","+volume+","+weight+","+quantity+","+price+")");
		ResultSet rsContainerStock = s.executeQuery("SELECT Volume, Weight, Quantity, UnitPrice From Condiment_Stock WHERE CondimentID="+condimentID);
		int formerQuantity = rsContainerStock.getInt("Quantity");
		int newQuantity = formerQuantity + quantity;
		double formerVolume = rsContainerStock.getDouble("Volume"),
				formerWeight = rsContainerStock.getDouble("Weight"),
				formerUnitPrice = rsContainerStock.getDouble("UnitPrice");
		double newVolume = formerVolume + volume;
		double newWeight = formerWeight + weight;
		s.executeUpdate("UPDATE Condiment_Stock SET Volume="+newVolume+", Weight="+newWeight+", Quantity="+newQuantity+", "
				+ "UnitPrice="+(((formerVolume+formerWeight+formerQuantity)*formerUnitPrice+price)/(newVolume+newWeight+newQuantity))+" "
						+ "WHERE CondimentID="+condimentID);
	}
}