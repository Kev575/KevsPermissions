package kev575.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KevSQL {

	private String hostip, database, user, passwd;
	private int port;
	private Connection con;
	
	public KevSQL(String hostip, String database, String user, String passwd, int port) {
		this.hostip = hostip;
		this.database = database;
		this.user = user;
		this.passwd = passwd;
		this.port = port;
	}
	
	public void defaultTables() {
		update("CREATE TABLE IF NOT EXISTS `%s`.`kevspermissions_players` ( `uuid` VARCHAR(36) NOT NULL , `gson` TEXT NOT NULL)".replace("%s", database));
		update("CREATE TABLE IF NOT EXISTS `%s`.`kevspermissions_groups` ( `groupname` VARCHAR(100) NOT NULL , `gson` TEXT NOT NULL)".replace("%s", database));
	}

	public void connect() {
		try {
			if (con == null || con.isClosed()) {
				con = DriverManager.getConnection("jdbc:mysql://" + hostip + ":" + port + "/" + database + "?autoReconnect=true", user, passwd);
			}
		} catch (SQLException e) { System.out.println("Can not connect to sql server " + hostip + ":" + port + "/" + database + " using username " + user + " identified by *****"); e.printStackTrace(); }
	}
	
	public void disconnect() {
		try {
			if (con != null) {
				con.close();
				con = null;
			}
		} catch (SQLException e) {}
	}
	
	public void update(String qry) {
		PreparedStatement s;
		try {
			s = con.prepareStatement(qry);
			s.execute();
		} catch (SQLException e) {
			System.out.println("Can not execute qry '" + qry + "'!");
			e.printStackTrace();
		}
	}
	
	public ResultSet query(String sql) {
		PreparedStatement s;
		try {
			s = con.prepareStatement(sql);
			return s.executeQuery();
		} catch (SQLException e) {
			System.out.println("Can not execute qry '" + sql + "'!");
			e.printStackTrace();
			return null;
		}
	}
	
}
