package iChef;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Windows {
	private static JFrame mainFrame = new JFrame();
	protected static void initializeFrame() throws SQLException{
		mainFrame.setVisible(true);
		mainFrame.setTitle("iChef");
		mainFrame.setSize(800,500);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		mainFrame.setLayout(new GridLayout(2,3));
		mainFrame.setResizable(false);
		initializeStockElements();
	}
	private static void initializeStockElements() throws SQLException{
		Vector ingredientStockTitle = new Vector();
		ingredientStockTitle.add("食材ID");
		ingredientStockTitle.add("食材名");
		ingredientStockTitle.add("库存");
		ingredientStockTitle.add("单位");
		ingredientStockTitle.add("单位价格");
		ingredientStockTitle.add(new Button("修改"));
		Vector<String> condimentStockTitle = new Vector<String>();
		condimentStockTitle.add("调料ID");
		condimentStockTitle.add("调料名");
		condimentStockTitle.add("库存");
		condimentStockTitle.add("单位");
		condimentStockTitle.add("单位价格+");
		Vector<String> containerStockTitle = new Vector<String>();
		containerStockTitle.add("容器ID");
		containerStockTitle.add("容器名");
		containerStockTitle.add("库存个数");
		containerStockTitle.add("单位价格");
//		JTable ingredientStock = new JTable(Ingredient.showIngredients(DBController.getStatement()),ingredientStockTitle);
//		JTable condimentStock = new JTable(Condiment.showCondiments(DBController.getStatement()),condimentStockTitle);
//		JTable containerStock = new JTable(Container.showContainers(DBController.getStatement()), containerStockTitle);
		Vector v = new Vector();
		Vector inv = new Vector();
		inv.add("asdf");
		v.addElement(inv);
		JTable ingredientStock = new JTable(v,ingredientStockTitle);
		mainFrame.add(ingredientStock);
		JTable table = new JTable(new Vector(),ingredientStockTitle);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		mainFrame.setLayout(new BorderLayout());
		mainFrame.add(table.getTableHeader(), BorderLayout.PAGE_START);
		mainFrame.add(table, BorderLayout.CENTER);
	}
	private static void initializeManageElements(){
		JTable meals = new JTable();
		
	}
}
