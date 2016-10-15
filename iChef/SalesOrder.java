package iChef;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;

public class SalesOrder {
	public static Vector<Vector> showSalesOrder(Statement s) throws SQLException{
		Vector<Vector> results = new Vector<Vector>();
		ResultSet rsSalesOrder = s.executeQuery("SELECT so.ID, COUNT(som.OrderID) AS MealSum, so.SalesDate "
				+ "FROM Sales_Order AS so, Sales_Order_Meals AS som "
				+ "WHERE so.ID=som.OrderID ORDER BY SalesDate DESC");
		while (rsSalesOrder.next()){
			Vector row = new Vector();
			row.addElement(rsSalesOrder.getInt("ID"));
			row.addElement(rsSalesOrder.getInt("MealSum"));
			row.add(rsSalesOrder.getDate("SalesDate"));
			results.addElement(row);
		}
		return results;
	}
	public static Vector<Vector> showSalesOrderMeals(Statement s, int SalesOrderID) throws SQLException {
		// TODO Auto-generated method stub
		Vector<Vector> results = new Vector<Vector>();
		ResultSet rsSalesOrderMeal = s.executeQuery("SELECT som.MealID, m.MealName, som.Quantity FROM Sales_Order_Meals AS som "
				+"LEFT JOIN Meal AS m ON som.MealID=m.ID WHERE som.OrderID="+SalesOrderID);
		while(rsSalesOrderMeal.next()){
			Vector row = new Vector();
			row.addElement(rsSalesOrderMeal.getInt("MealID"));
			row.addElement(rsSalesOrderMeal.getString("MealName"));
			row.addElement(rsSalesOrderMeal.getInt("Quantity"));
			results.addElement(row);
		}
		return results;
	}
//	public static Vector<Vector> showSalesOrderCondiments(Statement s, int SalesOrderID) throws SQLException {
//		Vector<Vector> results = new Vector<Vector>();
//		
//		return results;
//	}
	public static Vector<Vector> showSalesOrderIngredient(Statement s, int SalesOrderID) throws SQLException {
		Vector<Vector> results = new Vector<Vector>();
		HashMap<Integer,Vector> ingredientsSum = new HashMap<Integer,Vector>();
		ResultSet rsSalesOrderMeal = s.executeQuery("SELECT MealID, Quantity FROM Sales_Order_Meals WHERE OrderID="+SalesOrderID);
		while(rsSalesOrderMeal.next()){
			int mealID = rsSalesOrderMeal.getInt("MealID");
			int mealQuantity = rsSalesOrderMeal.getInt("Quantity");
			for(Vector row:Meal.showMealIngredients(s, mealID)){
				if(!ingredientsSum.containsKey(row.get(0))){
					Vector unit = new Vector();
					unit.addElement(row.get(1));
					unit.addElement((double)row.get(4)*mealQuantity);
					unit.addElement(row.get(5));
					ingredientsSum.put((int)row.get(0), unit);
				}else{
					ingredientsSum.get(row.get(0)).add(1,(double)ingredientsSum.get(row.get(0)).remove(1)+ ((double)row.get(4)*mealQuantity));
				}
			}
		}
		for(int ingredientID:ingredientsSum.keySet()){
			Vector row = new Vector();
			row.addElement(ingredientID);
			row.addElement(ingredientsSum.get(ingredientID).get(0));
			row.addElement(ingredientsSum.get(ingredientID).get(1));
			row.addElement(ingredientsSum.get(ingredientID).get(2));
			results.addElement(row);
		}
		return results;
	}
	public static Vector<Vector> showSalesOrderCondiment(Statement s, int SalesOrderID) throws SQLException {
		Vector<Vector> results = new Vector<Vector>();
		HashMap<Integer,Vector> condimentsSum = new HashMap<Integer,Vector>();
		ResultSet rsSalesOrderMeal = s.executeQuery("SELECT MealID, Quantity FROM Sales_Order_Meals WHERE OrderID="+SalesOrderID);
		while(rsSalesOrderMeal.next()){
			int mealQuantity = rsSalesOrderMeal.getInt("Quantity");
			for(Vector row:Meal.showMealCondiments(s, rsSalesOrderMeal.getInt("MealID"))){
				if(!condimentsSum.containsKey(row.get(0))){
					Vector unit = new Vector();
					unit.addElement(row.get(1));
					unit.addElement((double)row.get(3)*mealQuantity);
					unit.addElement(row.get(4));
					condimentsSum.put((int)row.get(0), unit);
				}else{
					condimentsSum.get(row.get(0)).add(1, (double)condimentsSum.get(row.get(0)).remove(1) + ((double)row.get(3)*mealQuantity));
				}
			}
		}
		for(int condimentID:condimentsSum.keySet()){
			Vector row = new Vector();
			row.addElement(condimentID);
			row.addElement(condimentsSum.get(condimentID).get(0));
			row.addElement(condimentsSum.get(condimentID).get(1));
			row.addElement(condimentsSum.get(condimentID).get(2));
			results.addElement(row);
		}
		return results;
	}
	public static Vector<Vector> showSalesOrderContainer(Statement s, int SalesOrderID) throws SQLException {
		Vector<Vector> results = new Vector<Vector>();
		HashMap<Integer,Vector> containersSum = new HashMap<Integer,Vector>();
		ResultSet rsSalesOrderMeal = s.executeQuery("SELECT MealID, Quantity FROM Sales_Order_Meals WHERE OrderID="+SalesOrderID);
		while(rsSalesOrderMeal.next()){
			int mealQuantity = rsSalesOrderMeal.getInt("Quantity");
			for(Vector row:Meal.showMealContainers(s, rsSalesOrderMeal.getInt("MealID"))){
				if(containersSum.containsKey(row.get(0))){
					containersSum.get(row.get(0)).add(1, (int)containersSum.get(row.get(0)).remove(1) + ((int)row.get(2)*mealQuantity));
				}else{
					Vector container = new Vector();
					container.addElement(row.get(1));
					container.addElement((int)row.get(2)*mealQuantity);
					containersSum.put((int)row.get(0),container);
				}
			}
		}
		for(int containerID:containersSum.keySet()){
			Vector row = new Vector();
			row.addElement(containerID);
			row.addElement(containersSum.get(containerID).get(0));
			row.addElement(containersSum.get(containerID).get(1));
			results.addElement(row);
		}
		return results;
	}
}
