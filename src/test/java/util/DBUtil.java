package util;

import data.Constant;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {
    public static Connection getConnect(){
        String url = "jdbc:mysql://"+ Constant.DB_URL+"/"+Constant.DB_NAME+"?useUnicode=true&characterEncoding=utf-8";
        Connection connection =null;
        try {
            connection =  DriverManager.getConnection(url,Constant.DB_USER,Constant.DB_PWD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    public static void closeConnect(Connection connection){
        if(connection!=null)
        {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static void update(String sql){
        QueryRunner queryRunner = new QueryRunner();
        Connection connection = getConnect();
        try {
            queryRunner.update(connection,sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            closeConnect(connection);
        }

    }

    public static Map<String,Object> queryOneResult(String sql){
        Map<String,Object> result = new HashMap<String, Object>();
        Connection connection = getConnect();
        QueryRunner queryRunner = new QueryRunner();
        try {
            result = queryRunner.query(connection,sql,new MapHandler());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            closeConnect(connection);
        }
        return result;
    }

    public static List<Map<String,Object>> queryMultiResult(String sql){
        List<Map<String,Object>> result = null;
        Connection connection = getConnect();
        QueryRunner queryRunner = new QueryRunner();
        try {
            result = queryRunner.query(connection,sql,new MapListHandler());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            closeConnect(connection);
        }
        return result;
    }

    public static Object queryOneField(String sql){
        Object result = null;
        Connection connection = getConnect();
        QueryRunner queryRunner = new QueryRunner();
        try {
            result = queryRunner.query(connection,sql,new ScalarHandler<Object>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            closeConnect(connection);
        }
        return result;
    }

}
