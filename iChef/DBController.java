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
	         * ����ODBC���ӷ�ʽ ��ν���ODBC���ӣ� 
	         * ����windows�£�����ʼ��->��������塿->�����ܺ�ά����->�������ߡ�->������Դ����������Դ�������һ��ָ��a1.mdb�ļ�������Դ�� 
	         * ���紴������ΪdataS1 
	         */  
	        String dbur1 = "jdbc:odbc:iChef";// ��ΪODBC���ӷ�ʽ  
	        Connection conn = DriverManager.getConnection(dbur1);  
	        s = conn.createStatement(); 
		}
    }  
}
