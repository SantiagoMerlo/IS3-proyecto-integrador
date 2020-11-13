package worker;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.sql.*;
import org.json.JSONObject;

class Worker {

  private static String DB_URL = "postgres://uzmqbqluddcolu:2a2fc77c8108ef5b4fce2e77d8742c577641a3c9952dc99b6485ccb7e6d343b4@ec2-18-233-32-61.compute-1.amazonaws.com:5432/d57p3q8jvbo598"; // System.getenv("DATABASE_URL")

  private static String POST_HOST = "ec2-18-233-32-61.compute-1.amazonaws.com"; // System.getenv("REDIS_HOST")

  private static String POST_DB = "d57p3q8jvbo598"; // System.getenv("POST_DB")

  private static String POST_PASS = "2a2fc77c8108ef5b4fce2e77d8742c577641a3c9952dc99b6485ccb7e6d343b4";

  private static String POST_PORT = "5432"; // System.getenv("POST_PORT")

  private static String POST_URI = "postgres://uzmqbqluddcolu:2a2fc77c8108ef5b4fce2e77d8742c577641a3c9952dc99b6485ccb7e6d343b4@ec2-18-233-32-61.compute-1.amazonaws.com:5432/d57p3q8jvbo598"; // System.getenv("POST_URI")

  private static String POST_USER = "uzmqbqluddcolu"; // System.getenv("POST_USER")

  private static String REDIS_HOST = "redis-17663.c16.us-east-1-3.ec2.cloud.redislabs.com"; //System.getenv("REDIS_HOST")

  private static String REDIS_PASS = "0g4WbPSX6ilxJRBkMBmYIWNS2D8I7ShZ";// System.getenv("REDIS_PASS")
  
  private static String REDIS_PORT = "17663"; // System.getenv("REDIS_PORT")
  public static void main(String[] args) {
    try {

      String URL_Redis = strConnectionRedis();

      Jedis redis = connectToRedis(new Jedis(URL_Redis));

      String ping = redisPing(redis);

      System.out.println(ping);

      Connection dbConn = connectToDB();

      System.err.println("Watching vote queue");

      while (true) {
        String voteJSON = redis.blpop(0, "votes").get(1);
        JSONObject voteData = new JSONObject(voteJSON);
        String voterID = voteData.getString("voter_id");
        String vote = voteData.getString("vote");

        System.err.printf("Processing vote for '%s' by '%s'\n", vote, voterID);
        updateVote(dbConn, voterID, vote);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  static void updateVote(Connection dbConn, String voterID, String vote) throws SQLException {
    PreparedStatement insert = dbConn.prepareStatement(
      "INSERT INTO votes (id, vote) VALUES (?, ?)");
    insert.setString(1, voterID);
    insert.setString(2, vote);

    try {
      insert.executeUpdate();
    } catch (SQLException e) {
      PreparedStatement update = dbConn.prepareStatement(
        "UPDATE votes SET vote = ? WHERE id = ?");
      update.setString(1, vote);
      update.setString(2, voterID);
      update.executeUpdate();
    }
  }

  static String redisPing( Jedis conn) {
    return "Server is runing:" + conn.ping();
  }

  public static String strConnectionRedis(){
    return "redis://default:" + REDIS_PASS + "@" + REDIS_HOST + ":" + REDIS_PORT;
  }

  static Jedis connectToRedis(Jedis conn) {
    while (true) {
      try {
        conn.keys("*");
        break;
      } catch (JedisConnectionException e) {
        System.err.println("Waiting for redis");
        sleep(1000);
      }
    }

    System.err.println("Connected to redis");
    return conn;
  }

  static Connection connectToDB() throws SQLException {

    Connection conn = null;

    try {

      Class.forName("org.postgresql.Driver");

      String url = strConnectionPostgres();

      conn = DriverManager.getConnection(url);

      PreparedStatement st = conn.prepareStatement(
        "CREATE TABLE IF NOT EXISTS votes (id VARCHAR(255) NOT NULL UNIQUE, vote VARCHAR(255) NOT NULL)");
      st.executeUpdate();

    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    System.err.println("Connected to db");
    return conn;
  }

  public static String strConnectionPostgres() {
    return "jdbc:postgresql://" + POST_HOST + ":" + POST_PORT + "/" + POST_DB + "?user=" + POST_USER + "&password=" + POST_PASS;
  }



  static void sleep(long duration) {
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      System.exit(1);
    }
  }
}
