package com.example.backend.DAO;/*
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
      System.out.println(e.getMessage());
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
      System.out.println(e.getMessage());
      return false;
    }
  }
  public boolean createMeal(int mealId,String mealName, String instructions, int timeNeeded, String userEmail){
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
      return true;
    }
    catch (Exception e){
      return false;
    }
  }
  public boolean addMealIngredient(int mealId, String ingredientName, double quantity){
    try{
      CallableStatement cs=this.conn.prepareCall("{CALL add_meal_ingredient(?,?,?)}");
      cs.clearParameters();
      cs.setInt(1,mealId);
      cs.setString(2,ingredientName);
      cs.setDouble(3,quantity);
      cs.executeUpdate();
      cs.close();
      return true;
    }
    catch (Exception e){
      return false;
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

}
 