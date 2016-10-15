package iChef;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Container {
	public static Vector<Vector> showContainers(Statement s) throws SQLException {
		// TODO Auto-generated method stub
		Vector<Vector> results = new Vector<Vector>();
		ResultSet rsContainers = s.executeQuery("SELECT * FROM Container AS c LEFT JOIN Container_Stock AS cs ON c.ID=cs.ContainerID");
		while (rsContainers.next()){
			Vector row = new Vector();
			row.addElement(rsContainers.getInt("ID"));
			row.addElement(rsContainers.getString("ContainerName"));
			row.addElement(rsContainers.getInt("Quantity"));
			row.addElement(rsContainers.getInt("UnitPrice"));
			results.addElement(row);
		}
		return results;
	}
	public static void addContainer(Statement s) throws SQLException{
		String containerName = "";
		s.executeUpdate("INSERT INTO Container VALUES ("+containerName+", GETDATE())");
		int newID = s.executeQuery("SELECT TOP 1 ID FROM Container ORDER BY ID DESC").getInt("ContainerID");
		s.executeUpdate("INSERT INTO Container_Stock VALUES ("+newID+",0,0");
	}
	public static void editContainer(Statement s) throws SQLException{
		String newContainerName = "";
		int containerID = 0;
		s.executeUpdate("UPDATE Container SET ContainerName =\""+newContainerName+"\" WHERE ID="+containerID);		
	}
	public static void deleteContainer(Statement s) throws SQLException{
		int containerID = 0;
		s.executeUpdate("DELETE FROM Container WHERE ID="+containerID);
	}
}
