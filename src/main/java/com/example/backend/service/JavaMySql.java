package com.example.backend.service;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 
db.mysql.url="jdbc:mysql://localhost:3306/db?characterEncoding=UTF-8&useSSL=false"
*/
import com.google.gson.*;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
/**
 *
 * @author kath
 */
@Component
public class JavaMySql {

  /**
   * The name of the MySQL account to use (or empty for anonymous)
   */
  private String userName = "root";

  /**
   * The password for the MySQL account (or empty for anonymous)
   */
  private String password = "Sdsdssdd123!";

  /**
   * The name of the computer running MySQL
   */
  private final String serverName = "localhost";

  /**
   * The port of the MySQL server (default is 3306)
   */
  private final int portNumber = 3306;

  /**
   * The name of the database we are testing with (this default is installed with MySQL)
   */
  private final String dbName = "meal_plan";

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  /**
   * The name of the table we are testing with
   */
  private String tableName = "genre";
  private final boolean useSSL = false;
  private Connection conn;
  public JavaMySql(){
    try{
    Class.forName("com.mysql.jdbc.Driver");
    Properties connectionProps = new Properties();
    connectionProps.put("user", this.userName);
    connectionProps.put("password", this.password);
    this.conn = DriverManager.getConnection("jdbc:mysql://"
            + this.serverName + ":" + this.portNumber + "/" + this.dbName + "?characterEncoding=UTF-8&useSSL=false",
        connectionProps);}
    catch(Exception e){
      System.out.println("Connection fail");
    }
  }

  /**
   * Get a new database connection
   *
   * @return
   * @throws SQLException
   */
  public Connection getConnection() throws SQLException {

    Connection conn = null;
    Properties connectionProps = new Properties();
    connectionProps.put("user", this.userName);
    connectionProps.put("password", this.password);

    conn = DriverManager.getConnection("jdbc:mysql://"
            + this.serverName + ":" + this.portNumber + "/" + this.dbName + "?characterEncoding=UTF-8&useSSL=false",
        connectionProps);

    return conn;
  }

  /**
   * Run a SQL command which does not return a recordset:
   * CREATE/INSERT/UPDATE/DELETE/DROP/etc.
   *
   * @throws SQLException If something goes wrong
   */
  public boolean executeUpdate(Connection conn, String command) throws SQLException {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      stmt.executeUpdate(command); // This will throw a SQLException if it fails
      return true;
    } finally {

      // This will run whether we throw an exception or not
      if (stmt != null) {
        stmt.close();
      }
    }
  }

  /**
   * Connect to MySQL and do some stuff.
   */
  public void run() {

    // Connect to MySQL
    Connection conn = null;
    try {
      conn = this.getConnection();
      System.out.println("Connected to database");
    } catch (SQLException e) {
      System.out.println("ERROR: Could not connect to the database");
      e.printStackTrace();
      return;
    }

    // Create a table
    try {
      String createString =
          "CREATE TABLE " + this.tableName + " ( " +
              "ID INTEGER NOT NULL, " +
              "NAME varchar(40) NOT NULL, " +
              "STREET varchar(40) NOT NULL, " +
              "CITY varchar(20) NOT NULL, " +
              "STATE char(2) NOT NULL, " +
              "ZIP char(5), " +
              "PRIMARY KEY (ID))";
      this.executeUpdate(conn, createString);
      System.out.println("Created a table");
    } catch (SQLException e) {
      System.out.println("ERROR: Could not create the table");
      e.printStackTrace();
      return;
    }

    // Drop the table
    try {
      String dropString = "DROP TABLE " + this.tableName;
      this.executeUpdate(conn, dropString);
      System.out.println("Dropped the table");
    } catch (SQLException e) {
      System.out.println("ERROR: Could not drop the table");
      e.printStackTrace();
    }
  }
  public boolean queryUser(String userName,String password){
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL query_user(?,?)}");
      cs.setString(1,userName);
      cs.setString(2,password);
      ResultSet rs= cs.executeQuery();
      if(rs.next()){
        return true;
      }
    }
    catch (Exception e){
    }
    return false;
  };
  public String insertUser(String userName,String password,String email){
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL insert_user(?,?,?)}");
      cs.setString(1,userName);
      cs.setString(2,password);
      cs.setString(3,email);
      cs.executeUpdate();
      return "Sucessfully insert User";
    }
    catch (Exception e){
      return e.getMessage();
    }

  }
  public String insertNutrition(String name,String measurement){
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL insert_nutrition(?,?)}");
      cs.setString(1,name);
      cs.setString(2,measurement);
      cs.executeUpdate();
      return "Sucessfully insert nutrition";
    }
    catch (Exception e){
      return e.getMessage();
    }

  }
  public String deleteNutrition(String name){
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL delete_nutrition(?)}");
      cs.setString(1,name);
      cs.executeUpdate();
      return "Sucessfully delete nutrition";
    }
    catch (Exception e){
      return e.getMessage();
    }

  }

  /***********************************************/
  public String updateIngredient(String name,String measurement){
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL update_nutrition(?,?)}");
      cs.setString(1,name);
      cs.setString(2,measurement);
      cs.executeUpdate();
      return "Sucessfully update nutrition";
    }
    catch (Exception e){
      return e.getMessage();
    }
  }

  public String insertIngredient(String name,String measurement){
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL insert_ingredient(?,?)}");
      cs.setString(1,name);
      cs.setString(2,measurement);
      cs.executeUpdate();
      return "Sucessfully insert Ingredient";
    }
    catch (Exception e){
      return e.getMessage();
    }
  }
  public String deleteIngredient(String name){
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL delete_ingredient(?)}");
      cs.setString(1,name);
      cs.executeUpdate();
      return "Sucessfully delete Ingredient";
    }
    catch (Exception e){
      return e.getMessage();
    }
  }

  /* **********************DELIMINATER  Start*********************** */

  public JsonArray getAllPlans(String userEmail){
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL all_plans(?)}");
      cs.clearParameters();
      cs.setString(1, userEmail);
      ResultSet rs= cs.executeQuery();
      JsonArray plans=new JsonArray();
      while (rs.next()) {
        JsonObject plan=new JsonObject();
        int planId = rs.getInt("plan_id");
        String planName = rs.getString("plan_name");
        int mealsPerDay = rs.getInt("meals_per_day");
        int planInterval = rs.getInt("plan_interval");
        plan.addProperty("planId",planId);
        plan.addProperty("planName",planName);
        plan.addProperty("mealsPerDay",mealsPerDay);
        plan.addProperty("planInterval",planInterval);
        plans.add(plan);
      }
      cs.close();
      return plans;
    }
    catch (Exception e){
      System.out.println("Get plans failed");
      System.out.println(e.getMessage());
      return null;
    }

  };
  public JsonArray getAllMeals(String userEmail){
    JsonArray meals=new JsonArray();
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL all_meals(?)}");
      cs.clearParameters();
      cs.setString(1, userEmail);
      ResultSet rs= cs.executeQuery();
      while (rs.next()) {
        JsonObject meal=new JsonObject();
        int mealId = rs.getInt("meal_id");
        String mealName = rs.getString("meal_name");
        int timeNeeded = rs.getInt("time_needed");
        String instructions = rs.getString("instructions");
        String ingredients = rs.getString("ingredients");
        meal.addProperty("_id",mealId);
        meal.addProperty("name",mealName);
        meal.addProperty("instructions",instructions);
        meal.addProperty("timeNeeded",timeNeeded);
        meal.addProperty("ingredients",ingredients);
        meals.add(meal);
      }

      cs.close();

    }
    catch (Exception e){
      System.out.println("Get all meals failed");
    }
    return meals;
  }
  public JsonArray getIngredients(){
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL get_ingredient()}");
      ResultSet rs= cs.executeQuery();
      JsonArray ingredients=new JsonArray();
      while (rs.next()) {
        JsonObject ingredient=new JsonObject();
        String name = rs.getString("name");
        String measurement = rs.getString("measurement");
        ingredient.addProperty("name",name);
        ingredient.addProperty("measurement",measurement);
        ingredients.add(ingredient);
      }
      cs.close();
      return ingredients;
    }
    catch (Exception e){
      System.out.println("Get Ingredients failed");
      return null;
    }
  }
  public JsonObject getRecordOfPlan(int plan_id,String name,int mealsPerDay,int interval){
    try{
      //Gson gson = new Gson();
      JsonObject json = new JsonObject();
      json.addProperty("plan_id",plan_id);
      json.addProperty("name",name);
      json.addProperty("mealsPerDay",mealsPerDay);
      json.addProperty("interval",interval);
      //json.addProperty("user_id",user_id);
      CallableStatement cs=this.conn.prepareCall("{CALL get_records_of_plan(?)}");
      cs.clearParameters();
      cs.setInt(1, plan_id);
      ResultSet rs= cs.executeQuery();
      List<Integer> mealIds=new ArrayList<Integer>();
      List<String> mealNames=new ArrayList<String>();
      //List<Integer> dayIndexes=new ArrayList<Integer>();
      //List<Integer> mealIndexes=new ArrayList<Integer>();

      while (rs.next()) {
        int mealId = rs.getInt("meal_id");
        mealIds.add(mealId);
        String mealName = rs.getString("meal_name");
        mealNames.add(mealName);

      }
      JsonArray meals=new JsonArray();
      for(int i=1;i<=interval;i++){
        JsonObject meal=new JsonObject();
        meal.addProperty("dayIndex",i);
        JsonArray mealsOfDay=new JsonArray();
        for(int j=1;j<=mealsPerDay;j++){
          JsonObject mealInformation=new JsonObject();
          mealInformation.addProperty("mealIndex",j);
          mealInformation.addProperty("mealName",mealNames.get(mealsPerDay*(i-1)+j-1));
          mealInformation.addProperty("meal_id",mealIds.get(mealsPerDay*(i-1)+j-1));
          mealsOfDay.add(mealInformation);
        }
        meal.add("mealsOfDay",mealsOfDay);
        meals.add(meal);
      }
      json.add("meals",meals);
      cs.close();
      return json;
    }
    catch (Exception e){
      System.out.println("getRecordOfPlan error");
      System.out.println(e.getMessage());
      return null;
    }

  }

  public boolean createPlan(int planId, String planName,int mealsPerDay,int planInterval,String user){
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL create_plan(?,?,?,?,?)}");
      cs.clearParameters();
      cs.setInt(1,planId);
      cs.setString(2,planName);
      cs.setInt(3,mealsPerDay);
      cs.setInt(4,planInterval);
      cs.setString(5,user);
      cs.executeUpdate();
      cs.close();
      return true;
    }
    catch (Exception e){
      return false;
    }
  }
  public boolean createMealRecord(JsonArray mealRecord){
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL add_meal_record(?,?,?,?)}");
      for(int i=0;i<mealRecord.size();i++){
        cs.clearParameters();
        JsonObject json=mealRecord.get(i).getAsJsonObject();
        cs.setInt(1,json.get("dayIndex").getAsInt());
        cs.setInt(2,json.get("mealIndex").getAsInt());
        cs.setInt(3,json.get("planId").getAsInt());
        cs.setInt(4,json.get("mealId").getAsInt());
        cs.executeUpdate();
      }
      cs.close();
      return true;
    }
    catch (Exception e){
      return false;
    }
  }
  public String createMeal(int mealId,String mealName, String instructions, int timeNeeded, String userEmail){
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL create_meal(?,?,?,?,?)}");
      cs.clearParameters();
      cs.setString(1,mealName);
      cs.setString(2,instructions);
      cs.setInt(3,timeNeeded);
      cs.setString(4,userEmail);
      cs.setInt(5,mealId);
      cs.executeUpdate();
      cs.close();
      return "Successfully insert Meal";
    }
    catch (Exception e){
      return e.getMessage();
    }
  }
  public String addMealIngredient(int mealId, String ingredientName, double quantity){
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL add_meal_ingredient(?,?,?)}");
      cs.clearParameters();
      cs.setInt(1,mealId);
      cs.setString(2,ingredientName);
      cs.setDouble(3,quantity);
      cs.executeUpdate();
      cs.close();
      return "Successfully insert Ingredient to a Meal";
    }
    catch (Exception e){
      return e.getMessage();
    }
  }
  public String updatePlan(int planId, String planName, int mealsPerDay, int planInterval){
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL update_plan(?,?,?,?)}");
      cs.clearParameters();
      cs.setInt(1,planId);
      cs.setString(2,planName);
      cs.setInt(3,mealsPerDay);
      cs.setInt(4,planInterval);
      cs.executeUpdate();
      cs.close();
      return "Successfully update plan";
    }
    catch (Exception e){
      return e.getMessage();
    }
  }
  public String updateMeal(int mealId, String mealName, String instructions, int timeNeeded){
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL update_meal(?,?,?,?)}");
      cs.clearParameters();
      cs.setInt(1,mealId);
      cs.setString(2,mealName);
      cs.setString(3,instructions);
      cs.setInt(4,timeNeeded);
      cs.executeUpdate();
      cs.close();
      return "Successfully update meal";
    }
    catch (Exception e){
      return e.getMessage();
    }
  }
  public String deleteMeal(int mealId){
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL delete_meal(?)}");
      cs.clearParameters();
      cs.setInt(1,mealId);
      cs.executeUpdate();
      cs.close();
      return "Successfully delete meal";
    }
    catch (Exception e){
      return e.getMessage();
    }
  }
  public String deletePlan(int planId){
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL delete_plan(?)}");
      cs.clearParameters();
      cs.setInt(1,planId);
      cs.executeUpdate();
      cs.close();
      return "Successfully delete plan";
    }
    catch (Exception e){
      return e.getMessage();
    }
  }

  /* **********************DELIMINATER  End*********************** */






  /**
   * Connect to the DB and do some stuff
   *
   * @param args
   */
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    System.out.print("Please input your username：");
    String username = input.nextLine();
    System.out.print("please input your password: ");
    String password = input.nextLine();
    JavaMySql newSql = new JavaMySql();
    Connection newConn = null;
    try {
      Class.forName("com.mysql.jdbc.Driver");
      newConn = newSql.getConnection();
    } catch (Exception e) {
      System.out.println("Username/password is invalid!");
      e.printStackTrace();
    }
    String genre = "";
    boolean isValidGenre = false;
    Statement statement = null;
    ResultSet resultSet = null;
    ArrayList<String> list = new ArrayList<String>();
    try {
      statement = newConn.createStatement();
      resultSet = statement.executeQuery("SELECT name FROM genre");
      System.out.println("List of available book genres:");
      while (resultSet.next()) {
        String str = resultSet.getString("name");
        System.out.println(str);
        list.add(str);
      }
    } catch (Exception e) {
      System.out.print("Connecting failed.");
    }
    while (!isValidGenre) {
      try {
        System.out.print("Enter a book genre: ");
        genre = input.nextLine();
        //resultSet.beforeFirst();
        int i = 0;
        while (i < list.size()) {
          if (list.get(i).equals(genre)) {
            isValidGenre = true;
            break;
          }
          i++;
        }
        if (!isValidGenre) {
          throw new Exception("Invalid book genre.");
        }
      } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
      }
    }
    try {
      CallableStatement cstmt = newConn.prepareCall("{CALL book_has_genre(?)}");
      cstmt.setString(1, genre);
      ResultSet resultSet_2 = cstmt.executeQuery();
      System.out.println("Result set for book_has_genre(" + genre + "):");
      while (resultSet_2.next()) {
        System.out.println("isbn: "+resultSet_2.getString(1)+ "; "+"author: " +resultSet_2.getString(2)+ "; " +"page_count: "+resultSet_2.getString(3)+ "; "+ "publisher_name: " +resultSet_2.getString(4) );}
    } catch (SQLException e) {
      System.out.println("Error calling book_has_genre procedure: " + e.getMessage());
    }
    try {
      newConn.close();
      System.out.println("Database connection closed.");
    } catch (SQLException e) {
      System.out.println("Error closing database connection: " + e.getMessage());
    }
  }
}
 