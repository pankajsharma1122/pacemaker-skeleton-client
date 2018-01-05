package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fixtures {
  
  public static User marge = new User("marge", "simpson", "marge@simpson.com", "secret");
  public static User lisa = new User("lisa", "simpson", "lisa@simpson.com", "secret");
  public static User bart = new User("bart", "simpson", "bart@simpson.com", "secret");
  public static User maggie = new User("maggie", "simpson", "maggie@simpson.com", "secret");
  public static List<User> users =
      new ArrayList<>(Arrays.asList(marge,lisa,bart, maggie));

  public static List<Activity> activities = new ArrayList<>(
      Arrays.asList(new Activity("walk", "fridge", 3.001),
          new Activity("walk", "aaa", 1.0),
          new Activity("run", "work", 2.2),
          new Activity("walk", "shop", 0.5),
          new Activity("cycle", "pool", 4.5)));
  
  public static List<Activity> activities_2 = new ArrayList<>(
      Arrays.asList(new Activity("walk", "fridge", 2),
          new Activity("walk", "aaa", 4),
          new Activity("run", "work", 6),
          new Activity("walk", "shop", 8),
          new Activity("cycle", "pool", 10)));
  
  public static List<Activity> activities_3 = new ArrayList<>(
      Arrays.asList(new Activity("walk", "fridge",1),
          new Activity("walk", "aaa", 3),
          new Activity("run", "work", 5),
          new Activity("walk", "shop",7),
          new Activity("cycle", "pool", 9)));

  public static List<Location> locations = new ArrayList<>(Arrays.asList(new Location(23.3, 33.3),
      new Location(34.4, 45.2), new Location(25.3, 34.3), new Location(44.4, 23.3)));

  public static List<Activity> margeActivities =
      new ArrayList<>(Arrays.asList(activities.get(0), activities.get(1)));

  public static List<Activity> lisasActivities =
      new ArrayList<>(Arrays.asList(activities.get(2), activities.get(3)));

  public static List<Location> route1 =
      new ArrayList<>(Arrays.asList(locations.get(0), locations.get(1)));

  public static List<Location> route2 =
      new ArrayList<>(Arrays.asList(locations.get(2), locations.get(3)));

  public static List<Activity> activitiesSortedByType =
      new ArrayList<>(Arrays.asList(activities.get(4), activities.get(2), activities.get(1),
          activities.get(0), activities.get(3)));
}