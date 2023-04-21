package com.example.backend.controller;

import com.example.backend.DAO.MyService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MyController {

  @Autowired
  private MyService myService;

  @PostMapping("/queryUser")
  public boolean queryUser(@RequestBody String body) {
    Gson g=new Gson();
    JsonObject input=g.fromJson(body, JsonObject.class);
    return myService.query_user(input.get("userName").getAsString(),input.get("password").getAsString() );
  }
  @PostMapping("/insertUser")
  public String insertUser(@RequestBody String body) {
    Gson g=new Gson();
    JsonObject input=g.fromJson(body, JsonObject.class);
    return myService.insertUser(input.get("userName").getAsString(),input.get("password").getAsString(),input.get("userEmail").getAsString());
  }
  @PostMapping ("/queryPlan")
  public String queryPlan(@RequestBody String body){
    Gson g=new Gson();
    JsonObject input=g.fromJson(body, JsonObject.class);
    return myService.queryPlans(input.get("userEmail").getAsString());
  }
  @PostMapping("/createPlan")
  public String createPlan(@RequestBody String body){
    Gson g=new Gson();
    JsonObject input=g.fromJson(body, JsonObject.class);
    return myService.createPlan(input.get("planId").getAsInt(), input.get("planName").getAsString(),input.get("mealsPerDay").getAsInt(),input.get("planInterval").getAsInt(),input.get("user").getAsString(),input.get("mealRecords").getAsJsonArray());
  }
  @PostMapping("/createMeal")
  public String createMeal(@RequestBody String body){
    Gson g=new Gson();
    JsonObject input=g.fromJson(body, JsonObject.class);
    JsonArray ing=input.getAsJsonArray("currentIngredients");
    JsonArray num=input.getAsJsonArray("numOfIngredients");
    String[] inga=g.fromJson(ing,String[].class);
    int[] numa=g.fromJson(num,int[].class);
    myService.createMeal(input.get("mealName").getAsString(),input.get("mealId").getAsInt(),input.get("timeNeeded").getAsInt(),input.get("instructions").getAsString(),input.get("user").getAsString(),inga,numa);
    return "Success";
  }
  @PostMapping("/getIngredient")
  public String getIngredient(@RequestBody String body){
    return myService.getIngredient();
  }
  @PostMapping("/getMeals")
  public String getMeals(@RequestBody String body){
    Gson g=new Gson();
    JsonObject input=g.fromJson(body, JsonObject.class);
    return myService.getAllMeals(input.get("userEmail").getAsString());
  }
  @PostMapping("/deletePlan")
  public String deletePlan(@RequestBody String body){
    Gson g=new Gson();
    JsonObject input=g.fromJson(body, JsonObject.class);
    return myService.deletePlan(input.get("planId").getAsInt());
  }
  @PostMapping("/updatePlan")
  public String updatePlan(@RequestBody String body){
    Gson g=new Gson();
    JsonObject input=g.fromJson(body, JsonObject.class);
    return myService.updatePlan(input.get("planId").getAsInt(),input.get("name").getAsString(),input.get("mealsPerDay").getAsInt(),input.get("interval").getAsInt());
  }
  @PostMapping("/delete")
  public String delete(@RequestBody String body){
    Gson g=new Gson();
    JsonObject input=g.fromJson(body, JsonObject.class);
    return myService.deletePlan(input.get("plan_id").getAsInt());
  }
}