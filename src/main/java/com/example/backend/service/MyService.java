package com.example.backend.service;

import com.example.backend.DAO.JavaMySql;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service
public class MyService {
  JavaMySql mysql;
  @Autowired
  public MyService(){
    mysql=new JavaMySql();
  }
  public boolean query_user(String userName,String password){
    return mysql.queryUser(userName,password);
  }
  public String insertUser(String userName,String password,String email){
    return mysql.insertUser(userName,password,email);
  }
  public String queryPlans(String userEmail){
    Gson g=new Gson();
    JsonArray plans_overview=mysql.getAllPlans(userEmail);
    JsonArray outPut=new JsonArray();
    if (plans_overview != null) {
      for (int i = 0; i < plans_overview.size(); i++) {
        JsonObject plan = plans_overview.get(i).getAsJsonObject();
        JsonObject planElement = mysql.getRecordOfPlan(plan.get("planId").getAsInt(), plan.get("planName").getAsString(), plan.get("mealsPerDay").getAsInt(), plan.get("planInterval").getAsInt());
        outPut.add(planElement);
      }
    }
    String op=g.toJson(outPut);
    return op;
  }
  public boolean createPlan(int planId, String planName,int mealsPerDay,int planInterval,String user,JsonArray mealRecord){
    if(mysql.createPlan(planId, planName,mealsPerDay,planInterval,user)&& mysql.createMealRecord(mealRecord)){
      return true;
    }
    else{
      return false;
    }
  }
  public boolean createMeal(String mealName,int mealId,int timeNeeded,String instructions,String user,String[] currentIngredients,int[] numOfIngredients){

    if(!mysql.createMeal(mealId,mealName,instructions,timeNeeded,user)){
      return false;
    };
    for(int i=0;i< currentIngredients.length;i++){
      if(!mysql.addMealIngredient(mealId,currentIngredients[i],numOfIngredients[i])){
        return false;
      };
    }
    return true;
  }
  public String getIngredient(){
    JsonArray ings=mysql.getIngredients();
    Gson g=new Gson();
    String otpt=g.toJson(ings);
    return otpt;
  }
  public String insertNutrition(String name,String measurement){
    return mysql.insertNutrition(name,measurement);
  }
  public String deleteNutrition(String name){
    return mysql.deleteNutrition(name);
  }
  public String updateIngredient(String name,String measurement){
    return mysql.updateIngredient(name,measurement);
  }
  public String insertIngredient(String name,String measurement){
    return mysql.insertIngredient(name,measurement);
  }
  public String deleteIngredient(String name){
    return mysql.deleteIngredient(name);
  }
  public JsonArray getAllPlans(String userEmail){return mysql.getAllPlans(userEmail);
  }
  public String getAllMeals(String userEmail) {
    Gson g= new Gson();
    JsonArray array= mysql.getAllMeals(userEmail);
    String otpt=g.toJson(array);
    return otpt;
  }
  public boolean addMealIngredient(int mealId, String ingredientName, double quantity) {
    return mysql.addMealIngredient(  mealId,  ingredientName, quantity);
  }
  public String updatePlan(int planId, String planName, int mealsPerDay, int planInterval) {
    return mysql.updatePlan (  planId,  planName, mealsPerDay,  planInterval);
  }
  public String updateMeal(int mealId, String mealName, String instructions, int timeNeeded){
    return mysql.updateMeal( mealId,  mealName,  instructions,  timeNeeded);
  }
  public String deleteMeal(int mealId){
    return mysql.deleteMeal( mealId);
  }
  public String deletePlan(int planId) {
    return mysql.deletePlan( planId);
  }
}