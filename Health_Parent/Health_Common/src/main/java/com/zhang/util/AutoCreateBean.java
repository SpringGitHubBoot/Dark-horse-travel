package com.zhang.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据数据库表结构 自动生成javabean
 */
public class AutoCreateBean {

    //  mysql 驱动类
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    //  数据库登录用户名
    private static final String USERNAME = "root";

    //  数据库登录密码
    private static final String PASSWORD = "root";

    // 数据库连接地址
    private static final String URL = "jdbc:mysql:///health";

    private static String tableName;

    private String[] colNames; // 列名数组

    private String[] colTypes; // 列名类型数组

    private int[] colSizes; // 列名大小数组

    private boolean f_util = false; // 是否需要导入包java.util.*

    private boolean f_sql = false; // 是否需要导入包java.sql.*

    /**
     * 获取指定数据库中包含的表 TabList
     *
     * @return 返回所有表名(将表名放到一个集合中)
     * @throws Exception
     * @time 2016年3月4日下午5:54:52
     * @packageName com.util
     */
    public List<String> tabList() throws Exception {
        // 访问数据库 采用 JDBC方式
        Class.forName(DRIVER);

        Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        DatabaseMetaData md = con.getMetaData();

        List<String> list = null;

        ResultSet rs = md.getTables(null, null, null, null);
        if (rs != null) {
            list = new ArrayList<>();
        }
        int i = 1;
        while (rs.next()) {
//          获得某个数据库下的所有表，并将表加入到集合
//          System.out.println("|表" + (i++) + ":" + rs.getString("TABLE_NAME"));
            String tableName = rs.getString("TABLE_NAME");
            list.add(tableName);
        }
        rs.close();
        con.close();
        return list;
    }

    public void GenEntity(List<String> tabList, String packageName) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSetMetaData rsmd = null;

        // 访问数据库 采用 JDBC方式
        Class.forName(DRIVER);
        conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        for (int k = 0; k < tabList.size(); k++) {
            tableName = tabList.get(k);
            String sql = "select * from " + tableName;
            ps = conn.prepareStatement(sql);
            rsmd = ps.getMetaData();
            int columnCount = rsmd.getColumnCount();

            colNames = new String[columnCount];
            colTypes = new String[columnCount];
            colSizes = new int[columnCount];

            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                colNames[i - 1] = rsmd.getColumnName(i);
                colTypes[i - 1] = rsmd.getColumnTypeName(i);

                if (colTypes[i - 1].equalsIgnoreCase("date")) {
                    f_util = true;
                }
                if (colTypes[i - 1].equalsIgnoreCase("text")) {
                    f_sql = true;
                }
                colSizes[i - 1] = rsmd.getColumnDisplaySize(i);
            }
            markerBean(initcap(tableName), parse(), packageName);
        }
        ps.close();
        conn.close();
    }

    /**
     * 解析处理(生成实体类主体代码)
     */
    private String parse() {
        StringBuffer sb = new StringBuffer();
        if (f_util) {
            sb.append("import java.util.Date;\r\n");
        }
        if (f_sql) {
            sb.append("import java.sql.*;\r\n\r\n\r\n");
        }
        sb.append("public class " + initcap(tableName) + " {\r\n");
        processAllAttrs(sb);
        processAllMethod(sb);
        sb.append("}\r\n");

        return sb.toString();

    }

    /**
     * 创建java 文件 将生成的属性 get/set 方法 保存到 文件中 markerBean
     *
     * @param className   类名称
     * @param content     类内容 包括属性 get、set 方法
     * @param packageName 包名
     */
    public void markerBean(String className, String content, String packageName) {
//      folder是个文件目录？ 是的！
        String folder = System.getProperty("user.dir") + "/src/" + packageName + "/";

        File file = new File(folder);
        if (!file.exists()) {
            file.mkdirs();
        }
//      写出的Java文件的路径：(D:\IdeaProjects\本项目\src\com\javabean)
        String fileName = folder + className + ".java";
//      System.out.println(file);

        try {
            File newDao = new File(fileName);
            FileWriter fw = new FileWriter(newDao);
            fw.write("package\t" + packageName.replace("/", ".") + ";\r\n");
            fw.write(content);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成所有的方法
     *
     * @param sb
     */
    private void processAllMethod(StringBuffer sb) {
        for (int i = 0; i < colNames.length; i++) {
            sb.append("\tpublic void set" + initcap(colNames[i]) + "("
                    + sqlType2JavaType(colTypes[i]) + " " + colNames[i]
                    + "){\r\n");
            sb.append("\t\tthis." + colNames[i] + " = " + colNames[i] + ";\r\n");
            sb.append("\t}\r\n");

            sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get"
                    + initcap(colNames[i]) + "(){\r\n");
            sb.append("\t\treturn " + colNames[i] + ";\r\n");
            sb.append("\t}\r\n");
        }
    }

    /**
     * 解析输出属性
     *
     * @return
     */
    private void processAllAttrs(StringBuffer sb) {
        for (int i = 0; i < colNames.length; i++) {
            sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " " + colNames[i] + ";\r\n");

        }
    }

    /**
     * 把输入字符串的首字母改成大写
     *
     * @param str
     * @return
     */
    private String initcap(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    private String sqlType2JavaType(String sqlType) {

        if (sqlType.equalsIgnoreCase("bit")) {
            return "boolean";
        } else if (sqlType.equalsIgnoreCase("tinyint")) {
            return "byte";
        } else if (sqlType.equalsIgnoreCase("smallint")) {
            return "short";
        } else if (sqlType.equalsIgnoreCase("int")) {
            return "int";
        } else if (sqlType.equalsIgnoreCase("bigint")) {
            return "long";
        } else if (sqlType.equalsIgnoreCase("float")) {
            return "float";
        } else if (sqlType.equalsIgnoreCase("decimal")
                || sqlType.equalsIgnoreCase("numeric")
                || sqlType.equalsIgnoreCase("real")) {
            return "double";
        } else if (sqlType.equalsIgnoreCase("money")
                || sqlType.equalsIgnoreCase("smallmoney")
                || sqlType.equalsIgnoreCase("double")) {
            return "double";
        } else if (sqlType.equalsIgnoreCase("varchar")
                || sqlType.equalsIgnoreCase("char")
                || sqlType.equalsIgnoreCase("nvarchar")
                || sqlType.equalsIgnoreCase("nchar")) {
            return "String";
        } else if (sqlType.equalsIgnoreCase("datetime")
                || sqlType.equalsIgnoreCase("date")) {
            return "Date";
        } else if (sqlType.equalsIgnoreCase("image")) {
            return "Blob";
        } else if (sqlType.equalsIgnoreCase("text")) {
            return "Clob";
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        AutoCreateBean auto = new AutoCreateBean();
        List<String> list = auto.tabList();
        auto.GenEntity(list, "com/javabean");

    }
}
