package util;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.activation.DataSource;

public class DataSourceUtils {
	BasicDataSource ds;
	public DataSourceUtils() throws NamingException {
		ds = (BasicDataSource) new InitialContext().lookup("java:comp/env/" + "jdbc/datasource");
	}

	public BasicDataSource getDataSource() {
		return this.ds;
	}
}
