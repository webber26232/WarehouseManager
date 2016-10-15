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
		ingredientStockTitle.add("ʳ��ID");
		ingredientStockTitle.add("ʳ����");
		ingredientStockTitle.add("���");
		ingredientStockTitle.add("��λ");
		ingredientStockTitle.add("��λ�۸�");
		ingredientStockTitle.add(new Button("�޸�"));
		Vector<String> condimentStockTitle = new Vector<String>();
		condimentStockTitle.add("����ID");
		condimentStockTitle.add("������");
		condimentStockTitle.add("���");
		condimentStockTitle.add("��λ");
		condimentStockTitle.add("��λ�۸�+");
		Vector<String> containerStockTitle = new Vector<String>();
		containerStockTitle.add("����ID");
		containerStockTitle.add("������");
		containerStockTitle.add("������");
		containerStockTitle.add("��λ�۸�");
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
