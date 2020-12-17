import java.util.*;
import java.net.*;
import java.text.*;
import java.lang.*;
import java.io.*;
import java.sql.*;
import pgpass.*;

/*============================================================================
CLASS CreateQuest
============================================================================*/

public class CreateQuest {
    private Connection conDB;        // Connection to the database system.
    private String url;              // URL: Which database?
    private String user = "bruce123"; // Database user account

    private String  realm;     // Who are we tallying?
    private String  day;   // Name of that customer.
    private java.sql.Date day1;
    private String  theme;
    private int  amount;
    private float seed;
    // Constructor
    public CreateQuest (String[] args) {
        // Set up the DB connection.
        try {
            // Register the driver with DriverManager.
            Class.forName("org.postgresql.Driver").newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(0);
        }

        // URL: Which database?
        //url = "jdbc:postgresql://db:5432/<dbname>?currentSchema=yrb";
        url = "jdbc:postgresql://db:5432/";

        // set up acct info
        // fetch the PASSWD from <.pgpass>
        Properties props = new Properties();
        try {
            String passwd = PgPass.get("db", "*", user, user);
            props.setProperty("user",    "bruce123");
            props.setProperty("password", passwd);
            // props.setProperty("ssl","true"); // NOT SUPPORTED on DB
        } catch(PgPassException e) {
            System.out.print("\nCould not obtain PASSWD from <.pgpass>.\n");
            System.out.println(e.toString());
            System.exit(0);
        }

        // Initialize the connection.
        try {
            // Connect with a fall-thru id & password
            //conDB = DriverManager.getConnection(url,"<username>","<password>");
            conDB = DriverManager.getConnection(url, props);
        } catch(SQLException e) {
            System.out.print("\nSQL: database connection error.\n");
            System.out.println(e.toString());
            System.exit(0);
        }    

        // Let's have autocommit turned off.  No particular reason here.
        try {
            conDB.setAutoCommit(false);
        } catch(SQLException e) {
            System.out.print("\nFailed trying to turn autocommit off.\n");
            e.printStackTrace();
            System.exit(0);
        }    

        // Who are we tallying?
        if (args.length < 4) {
            // Don't know what's wanted.  Bail.
            System.out.println("\nUsage: java CreateQuest day realm theme amount");
            System.exit(0);
        } else {
            try {
                day = args[0];
                day1 =java.sql.Date.valueOf(args[0]); 
                realm = new String(args[1]);
                theme = new String(args[2]);
                amount = new Integer(args[3]);
                user = new String(args[4]);
                seed = new Float(args[5]);
            } catch (NumberFormatException e) {
                System.out.println("\nUsage: java CreateQuest day realm theme amount");
                System.out.println("Provide an String for the day.realm,theme and INT for amount");
                System.exit(0);
            }
        }
        
        // Is this custID for real?
        if (!dayCheck()) {
            System.out.print("The date you input is not valid(not in future) : ");
            System.out.println(day);
            System.out.println("Bye.");
            System.exit(0);
        }

        if(!realmCheck()){
            System.out.print("the realm you input is not valid(not in realm table) : ");
            System.out.println(realm);
            System.out.println("Bye");
            System.exit(0);
        }

        if(!amountExceeds()){
            System.out.print("the amount you input is not valid(it should be smaller than sum of sql of all treasure and bigger than the smallest treasure's sql) : ");
            System.out.println(amount);
            System.out.println("Bye");
            System.exit(0);
        }

        if(!seedCheck()){
            System.out.print("the seed you input is not valid(between -1 and 1) : ");
            System.out.println(seed);
            System.out.println("Bye");
            System.exit(0);
        }

        // insert quest for table quest.
       insertQuest();
       insertLoot();

        // Commit.  Okay, here nothing to commit really, but why not...
        try {
            conDB.commit();
        } catch(SQLException e) {
            System.out.print("\nFailed trying to commit.\n");
            e.printStackTrace();
            System.exit(0);
        }    
        // Close the connection.
        try {
            conDB.close();
        } catch(SQLException e) {
            System.out.print("\nFailed trying to close the connection.\n");
            e.printStackTrace();
            System.exit(0);
        }    

    }

    public boolean dayCheck(){
        java.sql.Date current = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
        java.sql.Date questdate = java.sql.Date.valueOf(day);
        boolean infu = false;
        if(current.compareTo(questdate) <= 0) infu = true;
        return infu;
    }

    public boolean realmCheck(){
        String queryText = "";
        PreparedStatement querySt = null;
        ResultSet answer = null;
        boolean check = false;
        queryText = "select realm from realm where realm = ?";
         try {
            querySt = conDB.prepareStatement(queryText);
        } catch(SQLException e) {
            System.out.println("realm check failed in prepare");
            System.out.println(e.toString());
            System.exit(0);
        }
        // Execute the query.
        try {
            querySt.setString(1,realm);
            answer = querySt.executeQuery();
            if(answer.next()) check = true;
        } catch(SQLException e) {
            System.out.println("realm check failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }
        try {
            answer.close();
            querySt.close();
        } catch(SQLException e) {
            System.out.print("realm check failed closing the handle.\n");
            System.out.println(e.toString());
            System.exit(0);
        }
            return check;
    }

    public boolean amountExceeds(){
        String queryText1 = "";
        String queryText2 = "";
        Statement queryst1 = null;
        Statement queryst2 = null;
        ResultSet answer1 = null;
        ResultSet answer2 = null;
        boolean check = false;
        queryText1 = "select sum(sql) as max from treasure";
        queryText2 = "select min(sql) as min from treasure";
         try {
            queryst1 = conDB.createStatement();
            queryst2 = conDB.createStatement();
        } catch(SQLException e) {
            System.out.println("max,min failed in prepare");
            System.out.println(e.toString());
            System.exit(0);
        }
        // Execute the query.
        try {
            answer1 = queryst1.executeQuery(queryText1);
            answer2 = queryst2.executeQuery(queryText2);
        } catch(SQLException e) {
            System.out.println("max,min failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }
        try {
            if(answer1.next() && answer2.next()&& amount <= answer1.getInt("max") && amount >= answer2.getInt("min")) check = true;
        } catch(SQLException e) {
            System.out.print("compare max and min failed.\n");
            System.out.println(e.toString());
            System.exit(0);
        }   
         try {
            answer1.close();
            answer2.close();
            queryst1.close();
            queryst2.close();
        } catch(SQLException e) {
            System.out.print("amountexceeds failed closing cursor.\n");
            System.out.println(e.toString());
            System.exit(0);
        }  
            return check;
    }
    
    public boolean seedCheck(){
        if(seed <= 1 && seed >= -1 ) return true;
        return false;
    }

    public void insertQuest() {
        String            updateText = "";     // The SQL text.
        PreparedStatement insertSt   = null;   // The query handle.
        updateText =
            "insert into quest(theme,realm,day) values (?,?,?)";
        // Prepare the query.
        try {
            insertSt = conDB.prepareStatement(updateText);
        } catch(SQLException e) {
            System.out.println("insertquest failed in prepare");
            System.out.println(e.toString());
            System.exit(0);
        }
        // Execute the query.
        try {
            insertSt.setString(1, new String(theme));
            insertSt.setString(2, new String(realm));
            insertSt.setDate(3, day1);
            int num = insertSt.executeUpdate();
        } catch(SQLException e) {
            System.out.println("insertquest failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }
        // We're done with the handle.
        try {
            insertSt.close();
        } catch(SQLException e) {
            System.out.print("insert quest failed closing the handle.\n");
            System.out.println(e.toString());
            System.exit(0);
        }
    }
   
    public void insertLoot() {
        String            queryText1 = "";
        String            queryText2 = "";     // The SQL text.
        PreparedStatement querySt1   = null;
        PreparedStatement querySt2   = null;
        PreparedStatement seedstatement   = null;
        PreparedStatement insertSt   = null;   // The query handle.   
        ResultSet         answers1   = null;   // A cursor.
        ResultSet         answers2  = null; 
        ResultSet         seedset   = null; 

        int value = 0;
        Integer id = new Integer(0);
        String seedText = "select setseed (" + seed + ")";
         String treasureTable = " select treasure,sql "+"  from treasure "+"  order by random()";
        String treasure = "";
         String lootid = "select max(loot_id) as id from loot where day = ? and theme = ? and realm = ?";
        try {
             seedstatement = conDB.prepareStatement(seedText);
            querySt1 = conDB.prepareStatement(treasureTable);
        } catch(SQLException e) {
            System.out.println("treasure table failed in prepare");
            System.out.println(e.toString());
            System.exit(0);
        }
        try {
           seedset= seedstatement.executeQuery(); 
           answers1 = querySt1.executeQuery();
        } catch(SQLException e) {
            System.out.println("treasure table failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }
        
         try {
            querySt2 = conDB.prepareStatement(lootid);
        } catch(SQLException e) {
            System.out.println("lootid failed in prepare");
            System.out.println(e.toString());
            System.exit(0);
        }
        try {
            querySt2.setDate(1,day1);
            querySt2.setString(2,new String(theme));
            querySt2.setString(3,new String(realm));
            answers2 = querySt2.executeQuery();
            if(answers2.next()) id = answers2.getInt("id") + 1;
        } catch(SQLException e) {
            System.out.println("Lootid failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }
        int num = 0;
        String insertLoot = "insert into loot(loot_id,treasure,theme,realm,day,login) values (?,?,?,?,?,null)";
          try {
           insertSt = conDB.prepareStatement(insertLoot);
        } catch(SQLException e) {
            System.out.println("insertLoot failed in prepare");
            System.out.println(e.toString());
            System.exit(0);
        }
        try{
         if(seedset.next()) {

        	  }
        }
        catch(SQLException e) {
            System.out.println("seedset failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }
         while(value < amount){
        try {
            if(answers1.next()){
           treasure = answers1.getString("treasure");
            value += answers1.getInt("sql");
            }
        } catch(SQLException e) {
            System.out.println("treasure table 2 failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }
        try {
            insertSt.setInt(1,id.intValue());
            insertSt.setString(2,new String(treasure));
            insertSt.setString(3,new String(theme));
            insertSt.setString(4,new String(realm));
            insertSt.setDate(5,day1);
            num = insertSt.executeUpdate();
        } catch(SQLException e) {
            System.out.println("insert loot 2 failed in execute");
            System.out.println(e.toString());
            System.exit(0);
        }
             id++;
        }
        try {
            seedset.close();
            answers1.close();
            answers2.close();
            querySt1.close();
            querySt2.close();
            insertSt.close();
        } catch(SQLException e) {
            System.out.print("insertloot failed closing cursor.\n");
            System.out.println(e.toString());
            System.exit(0);
        }

    }

    public static void main(String[] args) {
        CreateQuest ct = new CreateQuest(args);
    }
 }
