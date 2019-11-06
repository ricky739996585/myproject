package ricky.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


/**
 * @Description:
 * @Author: ricky
 * @Date: 2019/7/8 14:37
 */
public class DataBaseUtils {

  private static Connection connectionA;
  private static Connection connectionB;
  public static final Logger logger = LoggerFactory.getLogger(DataBaseUtils.class);

  static {
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private static Connection getConnection(String url, String username, String password) {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(url, username, password);
      conn.setAutoCommit(true);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return conn;
  }

  private static void initialization(String urlA, String usernameA, String passwordA, String urlB,
      String usernameB, String passwordB) {
    connectionA = getConnection(urlA, usernameA, passwordA);
    connectionB = getConnection(urlB, usernameB, passwordB);
  }


  private static void close(Object o) {
    if (o == null) {
      return;
    }
    if (o instanceof ResultSet) {
      try {
        ((ResultSet) o).close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else if (o instanceof Statement) {
      try {
        ((Statement) o).close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else if (o instanceof Connection) {
      Connection c = (Connection) o;
      try {
        if (!c.isClosed()) {
          c.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 获取course 课程表数据
   */
  private static List<HashMap<String,Object>> getCourseList(Connection conn) {
    String sql = "select * from courses";
    ResultSet rs = null;
    List<HashMap<String,Object>> result = new ArrayList<>();
    try {
      PreparedStatement ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();

      while (rs.next()) {
        /**
         * 1.id   bigint
         * 2.course_no   varchar
         * 3.course_name  varchar
         * 4.course_type  tinyint
         * 5.create_id  bigint
         * 6.create_time  datetime
         * 7.update_id  bigint
         * 8.update_time  datetime
         * 9.dr  tinyint
         * 10.valid  int
         */
        HashMap<String,Object> data = new HashMap<>(32);
        data.put("id",rs.getLong("course_id"));
        data.put("course_no",rs.getString("course_no"));
        data.put("course_name",rs.getString("course_name"));
        data.put("course_type",0);
        data.put("create_id",rs.getLong("creator"));
        data.put("create_time",rs.getDate("creation_time")==null ? parseDate(new Date()):parseDate(rs.getDate("creation_time")));
        data.put("update_id",rs.getLong("modifier"));
        data.put("update_time",rs.getDate("modified_time")==null ? parseDate(new Date()):parseDate(rs.getDate("modified_time")));
        data.put("dr",rs.getInt("dr"));
        data.put("valid",rs.getInt("valid"));
        result.add(data);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close(rs);
    }
    return result;
  }

  /**
   * 获取course_record_detail 课程表数据
   */
  private static HashMap<String,Object> getCourseDetailList(Connection conn) {
    String sql = "select * from course_record_detail";
    List<HashMap<String,Object>> dataAList = new ArrayList<>();
    List<HashMap<String,Object>> dataBList = new ArrayList<>();
    ResultSet rs = null;
    HashMap<String,Object> result = new HashMap<>();
    try {
      PreparedStatement ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();

      while (rs.next()) {
        /**
         * 1.id   bigint
         * 2.course_id  bigint
         * 3.parent_id   bigint
         * 4.name  varchar
         * 5.type  int
         * 6.order_num  bigint
         * 7.create_id  bigint
         * 8.create_time  datetime
         * 9.update_id  bigint
         * 10.update_time  datetime
         * 11.is_listen  tinyint
         *
         * 12.course_id bigint
         * 13.chapter_id bigint
         * 14.vid varchar
         * 15.video_name varchar
         * 16.duration varchar
         * 17.ptime datetime
         * 18.first_image varchar
         * 19.duration_ms bigint
         */
        HashMap<String,Object> dataA = new HashMap<>(16);
        dataA.put("id",rs.getLong("record_id"));
        dataA.put("course_id",rs.getLong("course_id"));
        dataA.put("parent_id",rs.getLong("parent_id"));
        dataA.put("name",rs.getString("name"));
        dataA.put("type",rs.getInt("type"));
        dataA.put("order_num",rs.getLong("order_num"));
        dataA.put("create_id",rs.getLong("create_person"));
        dataA.put("create_time",rs.getDate("creation_time")==null ? parseDate(new Date()):parseDate(rs.getDate("creation_time")));
        dataA.put("update_id",rs.getLong("modify_person"));
        dataA.put("update_time",rs.getDate("modified_time")==null ? parseDate(new Date()):parseDate(rs.getDate("modified_time")));
        dataA.put("is_listen",rs.getInt("is_listen"));
        dataAList.add(dataA);
        if(rs.getInt("type") == 1){
          HashMap<String,Object> dataB = new HashMap<>(16);
          dataB.put("id",rs.getLong("record_id"));
          dataB.put("course_id",rs.getLong("course_id"));
          dataB.put("name",rs.getString("name"));
          dataB.put("chapter_id",rs.getLong("record_id"));
          dataB.put("vid",rs.getString("vid"));
          dataB.put("video_name",rs.getString("polyv_name"));
          dataB.put("duration",rs.getString("duration"));
          dataB.put("ptime",rs.getDate("ptime")==null ? parseDate(new Date()):parseDate(rs.getDate("ptime")));
          dataB.put("first_image",rs.getString("first_image"));
          dataB.put("create_id",rs.getLong("create_person"));
          dataB.put("create_time",rs.getDate("creation_time")==null ? parseDate(new Date()):parseDate(rs.getDate("creation_time")));
          dataB.put("update_id",rs.getLong("modify_person"));
          dataB.put("update_time",rs.getDate("modified_time")==null ? parseDate(new Date()):parseDate(rs.getDate("modified_time")));
          dataB.put("duration_ms",rs.getLong("duration_s"));
          dataBList.add(dataB);
        }
      }
      result.put("chapterList",dataAList);
      result.put("recordList",dataBList);

    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close(rs);
    }
    return result;
  }

  /**
   * 插入数据操作
   */
  private static void insertData(Connection conn,List<HashMap<String,Object>> dataList,String tableName) {
    try {
      for(HashMap<String,Object> data:dataList){
        String sql = getInsertSql(data,tableName);
        System.out.println(sql);
        conn.prepareStatement(sql).executeUpdate();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  private static String parseDate(Date date){
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return sdf.format(date);
  }


  /**
   * 获取insert语句
   */
  private static String getInsertSql(HashMap<String,Object> data,String tableName){
      StringBuilder sql = new StringBuilder("insert into ");
      StringBuilder columns = new StringBuilder();
      StringBuilder values = new StringBuilder();
      sql.append(tableName + " (");
    for(Map.Entry<String, Object> entry : data.entrySet()){
      columns.append(""+entry.getKey()+",");
      values.append("\""+entry.getValue()+"\",");
    }
    //去除尾部逗号
    columns.deleteCharAt(columns.length()-1);
    values.deleteCharAt(values.length()-1);
    sql.append(columns);
    sql.append(") values (");
    sql.append(values);
    sql.append(")");
    return sql.toString();
  }

  public static void main(String[] args) {
//    HashMap<String,Object> data = new HashMap<>();
//    data.put("age,",18);
//    data.put("name","ricky8'44");
//    System.out.println(getInsertSql(data,"user"));
    String urlA = "jdbc:mysql://10.0.99.37:3306/hq_school?useSSL=true";
    String usernameA = "root";
    String passwordA = "root";
    String urlB = "jdbc:mysql://10.0.99.37:3306/expert-online-school?useSSL=true";
    String usernameB = "root";
    String passwordB = "root";
    initialization(urlA,usernameA,passwordA,urlB,usernameB,passwordB);

    //获取数据
    List<HashMap<String, Object>> courseList = getCourseList(connectionA);
    HashMap<String, Object> courseDetailList = getCourseDetailList(connectionA);
    List<HashMap<String, Object>> chapterList = (List<HashMap<String, Object>>) courseDetailList.get("chapterList");
    List<HashMap<String, Object>> recordList = (List<HashMap<String, Object>>) courseDetailList.get("recordList");

    //插入数据到courses
    insertData(connectionB,courseList,"courses");
    insertData(connectionB,chapterList,"course_chapter");
    insertData(connectionB,recordList,"course_record");


  }
}
