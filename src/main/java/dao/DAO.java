package dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
	public boolean create(T c);
	public boolean delete(Object key);
	public boolean update(T c);
	public T read(Object key) throws SQLException;
	public List<T> readAll() throws SQLException;
}
