package kev575.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLManager {

	private Connection con;
	private String host, database, user, password;
	private int port;
	
	public SQLManager(String host, String database, String user, String password, int port) {
		this.host = host;
		this.database = database;
		this.user = user;
		this.password = password;
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}

	public Connection getCon() {
		return con;
	}

	public String getHost() {
		return host;
	}

	public String getDatabase() {
		return database;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}
	
	public void connect() throws SQLException {
		if (con != null && !con.isClosed()) {
			con = DriverManager.getConnection("jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase() + "?autoReconnect=true", getUser(), password);
			prepareStatement("CREATE TABLE IF NOT EXISTS `kevspermissions` ( `groups` MEDIUMTEXT NOT NULL , `players` MEDIUMTEXT NOT NULL ) ENGINE = InnoDB;").executeUpdate();
		}
	}
	
	public PreparedStatement prepareStatement(String qry) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement(qry);
		} catch (SQLException e) {
			return null;
		}
		return st;
	}
	
	public void disconnect() {
		if (con == null)
			return;
		try {
			con.close();
		} catch (SQLException e) {}
		con = null;
	}
	
}
