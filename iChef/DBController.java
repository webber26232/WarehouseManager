package iChef;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBController {
	private static Statement s;
	public static Statement getStatement(){
		return s;
	}
	public static void ConnectAccessDataSource()throws Exception {
		if (s==null){
	        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");  
	        /** 
	         * 采用ODBC连接方式 如何建立ODBC连接？ 
	         * 答：在windows下，【开始】->【控制面板】->【性能和维护】->【管理工具】->【数据源】，在数据源这里添加一个指向a1.mdb文件的数据源。 
	         * 比如创建名字为dataS1 
	         */  
	        String dbur1 = "jdbc:odbc:iChef";// 此为ODBC连接方式  
	        Connection conn = DriverManager.getConnection(dbur1);  
	        s = conn.createStatement(); 
		}
    }  
}
