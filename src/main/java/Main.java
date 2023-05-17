

import exel.GeneratingExcelFile;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./users";

    //  Database credentials
    static final String USER = "sa";
    static final String PASS = "";

    public static void main(String[] args) {

        Main main = new Main();
        main.create();

        String dir = "users/"; // задаем директорию
        File[] files = new File(dir).listFiles();
        for(File file : files) {
            System.out.println(file.toString());
            main.readFile(file.toString());
        }
        main.select("select lastname ,firstname,surname,mail, STRING_AGG(groupname,', ') AS Член_групп_СКДПУ from users group by lastname,firstname,surname,mail order by lastname;");
        main.execute("DROP TABLE users");


    }
    public String transformation(String str,String filename){
        filename = filename.replace("users\\","");
        filename = filename.replace(".txt","");

        str=str.replace("\t"," ");
        String [] a = str.split(" ");
        //System.out.println(a.length);
        if (a.length == 7){
            String lastname = a[0];
            String firstname = a[1];
            String surname = a[2];
            String surname2 = a[3];
            String status = a[4];
            String login = a[5];
            String mail = a[6];
            return "INSERT INTO USERS VALUES ('"+filename+"','"+lastname+"', '"+firstname+"', '"+surname+surname2+"', '"+status+"','"+login+"','"+mail+"')";

        } else if (a.length == 6){
            String lastname = a[0];
            String firstname = a[1];
            String surname = a[2];
            String status = a[3];
            String login = a[4];
            String mail = a[5];
            return "INSERT INTO USERS VALUES ('"+filename+"','"+lastname+"', '"+firstname+"', '"+surname+"', '"+status+"','"+login+"','"+mail+"')";

        } else {
            String lastname = a[0];
            String firstname = a[1];
            String surname = "-";
            String status = a[2];
            String login = a[3];
            String mail = a[4];
            return "INSERT INTO USERS VALUES ('"+filename+"','"+lastname+"', '"+firstname+"', '"+surname+"', '"+status+"','"+login+"','"+mail+"')";

        }



    }
    public void readFile(String filename){
        BufferedReader reader;
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();

            while (line != null) {
                if (line.length() > 5 ){
                    arrayList.add(line);
                    insert(transformation(line,filename));
                   // System.out.println(transformation(line,filename));
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void insert(String sql ){
        Connection conn = null;
        Statement stmt = null;
        try{
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            // STEP 4: Clean-up environment
            stmt.close();
            conn.close();
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if(stmt!=null) stmt.close();
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try
        } // end try

    }
    public void create(){
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            System.out.println("Creating table in given database...");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE USERS "
                    + "( groupname VARCHAR(255), "
                    + " lastname VARCHAR(255), "
                    + " firstname VARCHAR(255), "
                    + " surname VARCHAR(255), "
                    + " status VARCHAR(255), "
                    + " login VARCHAR(255), "
                    + " mail VARCHAR(255) )";
//            String sql =  "CREATE TABLE WORDS " +
//                    "( word VARCHAR(255) not NULL, " +
//                    " insertdate date, " +
//                    " progress INTEGER, " +
//                    " PRIMARY KEY ( word ))";

            stmt.executeUpdate(sql);
            System.out.println("Created table in given database...");

            // STEP 4: Clean-up environment
            stmt.close();
            conn.close();
        } catch(SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try{
                if(stmt!=null) stmt.close();
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se){
                se.printStackTrace();
            } //end finally try
        } //end try
    }
    public  String select(String sql){
        Connection conn = null;
        Statement stmt = null;
        String select = "";
        GeneratingExcelFile generatingExcelFile = new GeneratingExcelFile();

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                // Retrieve by column name
//                String groupname = rs.getString("groupname");
                String Result = rs.getString("Член_групп_СКДПУ");
                String lastname = rs.getString("lastname");
                String firstname = rs.getString("firstname");
                String surname = rs.getString("surname");
//                String status = rs.getString("status");
//                String login = rs.getString("login");
                String mail = rs.getString("mail");
//                System.out.println(groupname+" "+lastname+" "+firstname+" "+surname+" "+status+" "+login+" "+mail);
                System.out.println(lastname+" "+firstname+" "+surname+" "+mail+" "+Result);
                generatingExcelFile.gef(rs);

            }
            // STEP 5: Clean-up environment
            rs.close();
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if(stmt!=null) stmt.close();
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try
        } // end try
        return select;
    }

    public void execute(String sql){
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            stmt.execute(sql);

        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if(stmt!=null) stmt.close();
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try
        } // end try
    }

}
