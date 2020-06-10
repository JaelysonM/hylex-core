package com.uzm.hylex.core.sql;


import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MariaDB {

  private static final LinkedHashMap<String, MariaDB> SQLS = new LinkedHashMap<>();

  private Connection connection;

  private String host, port, database, username, password;

  private ExecutorService executorService;

  private String name;

  private Properties info;

  private String[] tablesRules;


  public MariaDB(String name, String host, String port, String database, String username, String password) {
    this.name = name;
    this.host = host;
    this.port = port;
    this.database = database;
    this.username = username;
    this.password = password;

    this.info = new Properties();
    this.info.put("autoReconnect", true);
    this.info.put("user", this.username);
    this.info.put("password", this.password);
    this.info.put("useUnicode", "true");
    this.info.put("characterEncoding", "utf8");

    this.executorService = Executors.newCachedThreadPool();
    open();
  }

  public void setTablesRules(String... tablesRules) {
    this.tablesRules = tablesRules;
  }

  public void open() {
    if (!isConnected()) {
      getExecutorService().execute(() -> {
        try {
          // System.out.println("[hylex-core] New MySQL connection has been opened > Hostname: " + host + " / Name > " + name);
          Class.forName("org.mariadb.jdbc.Driver");
          connection = DriverManager.getConnection("jdbc:mariadb://" + this.host + ":" + this.port + "/" + this.database, this.info);
          createTables(getTablesRules());
        } catch (SQLException | ClassNotFoundException e) {
          System.err.println("[hylex-core] MySQL error, probably the infractions are incorrect >" + e.getMessage());

        }
      });
    }
  }

  public void close() {
    if (isConnected()) {
      try {
        getConnection().close();
      } catch (SQLException e) {
        System.err.println("[hylex-core] MySQL error, probably the connection is closed > " + e.getMessage());
      }
    }
  }

  public boolean isConnected() {
    try {
      return connection != null && !connection.isClosed();
    } catch (SQLException e) {
      System.err.println("[hylex-core] MySQL error, probably the connection is closed > " + e.getMessage());
    }
    return false;
  }


  public void execute(final String query, final Object... vars) {
    if (isConnected()) {
      getExecutorService().execute(() -> {
        try {
          PreparedStatement ps = prepareStatement(query, vars);
          if (ps !=null) {
            ps.execute();
            ps.close();
          }
        } catch (SQLException e) {
          System.err.println("[hylex-core] MySQL error, probably the connection is closed > " + e.getMessage());
        }
      });
    } else {
      open();
      execute(query, vars);
    }
  }

  private PreparedStatement prepareStatement(String query, Object... vars) {
    try {
      if (isConnected()) {
        PreparedStatement ps = getConnection().prepareStatement(query);
        int i = 0;
        if (query.contains("?") && vars.length != 0) {
          for (Object obj : vars) {
            i++;
            ps.setObject(i, obj);
          }
        }
        return ps;
      } else {
        open();
        return prepareStatement(query, vars);
      }
    } catch (SQLException e) {
      System.err.println("[hylex-core] MySQL error, probably the connection is closed > " + e.getMessage());
    }

    return null;
  }

  public CachedRowSet query(final String query, final Object... vars) {
    CachedRowSet rowSet = null;
    if (isConnected()) {
      try {

        Future<CachedRowSet> future = getExecutorService().submit(() -> {
          try {
            PreparedStatement ps = prepareStatement(query, vars);
            if (ps !=null) {
              ResultSet rs = ps.executeQuery();
              CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
              crs.populate(rs);
              rs.close();
              ps.close();

              if (crs.next())
                return crs;
            }
          } catch (SQLException e) {
            System.err.println("[hylex-core] MySQL error, probably the connection is closed > " + e.getMessage());
          }

          return null;
        });

        if (future.get() != null)
          rowSet = future.get();

      } catch (Exception e) {
        System.err.println("[hylex-core] MySQL error, probably the connection is closed > " + e.getMessage());
      }
    } else {
      open();
      query(query, vars);
    }

    return rowSet;
  }

  public void createTables(String... tables) {

    for (String t : tables) {
      execute(t);
    }
  }


  public ExecutorService getExecutorService() {
    return executorService;
  }

  public Connection getConnection() {
    return connection;
  }

  public String[] getTablesRules() {
    return tablesRules;
  }

  public String getName() {
    return name;
  }

  public void destroy() {
    this.connection = null;
    this.host = null;
    this.database = null;
    this.username = null;
    this.password = null;
    this.executorService = null;

    this.tablesRules = null;
    this.name = null;

    this.info.clear();
    this.info = null;
  }



  public static LinkedHashMap<String, MariaDB> getSqls() {
    return SQLS;
  }


  public static MariaDB create(String name, String host, String port, String database, String username, String password) {
    return SQLS.computeIfAbsent(name, map -> new MariaDB(name, host, port, database, username, password));
  }

  public static void remove(String name) {
    if (SQLS.containsKey(name)) {
      get(name).destroy();
      SQLS.remove(name);
    }
  }


  public static MariaDB get(String name) {
    return SQLS.getOrDefault(name, null);
  }

  /*
  	execute("CREATE TABLE IF NOT EXISTS `" + Config.MYSQL_PLAYERTABLE + "` ("
				+ "`Nick` varchar(16) COLLATE utf8_unicode_ci NOT NULL,"
				+ "`Skin` varchar(16) COLLATE utf8_unicode_ci NOT NULL,"
				+ "PRIMARY KEY (`Nick`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci");
		execute("CREATE TABLE IF NOT EXISTS `" + Config.MYSQL_SKINTABLE + "` ("
				+ "`Nick` varchar(16) COLLATE utf8_unicode_ci NOT NULL," + "`Value` text COLLATE utf8_unicode_ci,"
				+ "`Signature` text COLLATE utf8_unicode_ci,"
				+ "PRIMARY KEY (`Nick`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci");
				*/


}
