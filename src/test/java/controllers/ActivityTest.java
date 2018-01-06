package controllers;

import static models.Fixtures.locations;
import static models.Fixtures.maggie;
import static models.Fixtures.marge;
import static models.Fixtures.activities;
import static models.Fixtures.activities_2;
import static models.Fixtures.activities_3;
import static models.Fixtures.bart;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Activity;
import models.Location;
import models.User;

public class ActivityTest {

  PacemakerAPI pacemaker = new PacemakerAPI("https://warm-escarpment-62674.herokuapp.com/");
//http://localhost:7000/
  //https://warm-escarpment-62674.herokuapp.com/
//https://whispering-scrubland-77173.herokuapp.com/ --KOTLIN
  User homer = new User("homer", "simpson", "homer@simpson.com", "secret");

  @Before
  public void setup() {
    pacemaker.deleteUsers();
    homer = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testCreateActivity() {
    Activity activity = new Activity("walk", "shop", 2.5);

    Activity returnedActivity = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);
    assertEquals(activity.type, returnedActivity.type);
    assertEquals(activity.location, returnedActivity.location);
    assertEquals(activity.distance, returnedActivity.distance, 0.001);
    assertNotNull(returnedActivity.id);
  }
  
  @Test
  public void testGetActivity() {
    Activity activity = new Activity("run", "fridge", 0.5);
    Activity returnedActivity1 = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);
    Activity returnedActivity2 = pacemaker.getActivity(homer.id, returnedActivity1.id);
    assertEquals(returnedActivity1, returnedActivity2);
  }
  
  @Test
  public void testGetActivities() {
    activities.forEach(activity -> pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance));
    Collection<Activity> userActivities = pacemaker.getActivities(homer.id);
    assertEquals(userActivities.size(), activities.size());
  }
  
  @Test
  public void testlistActivitiesByType() {
    activities.forEach(activity -> pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance));
    List<Activity> userActivities = pacemaker.listActivities(homer.id, "type");
    assertEquals(userActivities.get(0).getType(), "cycle");
    assertEquals(userActivities.get(4).getType(), "walk");
  }
  
  @Test
  public void testlistActivitiesByLocation() {
    activities.forEach(activity -> pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance));
    List<Activity> userActivities = pacemaker.listActivities(homer.id, "location");
    assertEquals(userActivities.get(0).getLocation(), "aaa");
  }
  
  @Test
  public void testlistActivitiesByDistance() {
    activities.forEach(activity -> pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance));
    List<Activity> userActivities = pacemaker.listActivities(homer.id, "distance");
    assertEquals(userActivities.get(0).getDistance(), "0.5");
    //assertEquals(userActivities.get(4).getType(), "walk");
  }

  @Test
  public void testDeleteActivity() {
    Activity activity = new Activity("sprint", "pub", 4.5);
    Activity returnedActivity = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);
    assertNotNull (returnedActivity);
    pacemaker.deleteActivities(homer.id);
    returnedActivity = pacemaker.getActivity(homer.id, returnedActivity.id);
    assertNull (returnedActivity);
  }
  
  @Test
  public void testFriendActivityReport() {
    User user_marge = pacemaker.createUser(marge.firstname, marge.lastname, marge.email, marge.password);
    pacemaker.followAFriend(homer.email, user_marge.email);
    activities.forEach(activity -> pacemaker.createActivity(user_marge.getId(), activity.type, activity.location, activity.distance));
    
    List<Activity> margeActivities = (List<Activity>) pacemaker.friendActivityReport(homer.getId(), user_marge.getEmail());
    assertEquals(activities.size(),margeActivities.size());
    assertEquals(( margeActivities).get(0).getType(),"cycle");
  }
  
  @Test
  public void testDistanceLeaderBoard() {
    User user_marge = pacemaker.createUser(marge.firstname, marge.lastname, marge.email, marge.password);
    User user_maggie = pacemaker.createUser(maggie.firstname, maggie.lastname, maggie.email, maggie.password);
    User user_bart = pacemaker.createUser(bart.firstname, bart.lastname, bart.email, bart.password);
    pacemaker.followAFriend(homer.email, user_marge.email);
    pacemaker.followAFriend(homer.email, user_maggie.email);
    pacemaker.followAFriend(homer.email, user_bart.email);
    activities.forEach(activity -> pacemaker.createActivity(user_marge.getId(), activity.type, activity.location, activity.distance));
    activities_2.forEach(activity -> pacemaker.createActivity(user_maggie.getId(), activity.type, activity.location, activity.distance));
    activities_3.forEach(activity -> pacemaker.createActivity(user_bart.getId(), activity.type, activity.location, activity.distance));
    
    List<Activity> distanceLeaderBoard = pacemaker.distanceLeaderBoard(homer.getId());
    assertEquals(distanceLeaderBoard.size(),15);
    assertEquals(distanceLeaderBoard.get(0).getDistance(),"10.0");
    
    
  }
  
  @Test
  public void testDistanceLeaderBoardByType() {
    User user_marge = pacemaker.createUser(marge.firstname, marge.lastname, marge.email, marge.password);
    User user_maggie = pacemaker.createUser(maggie.firstname, maggie.lastname, maggie.email, maggie.password);
    User user_bart = pacemaker.createUser(bart.firstname, bart.lastname, bart.email, bart.password);
    pacemaker.followAFriend(homer.email, user_marge.email);
    pacemaker.followAFriend(homer.email, user_maggie.email);
    pacemaker.followAFriend(homer.email, user_bart.email);
    activities.forEach(activity -> pacemaker.createActivity(user_marge.getId(), activity.type, activity.location, activity.distance));
    activities_2.forEach(activity -> pacemaker.createActivity(user_maggie.getId(), activity.type, activity.location, activity.distance));
    activities_3.forEach(activity -> pacemaker.createActivity(user_bart.getId(), activity.type, activity.location, activity.distance));
    
    Collection<Activity> distanceLeaderBoardByType = pacemaker.distanceLeaderBoardByType(homer.getId(),"walk");
    assertEquals(distanceLeaderBoardByType.size(),9);
    //assertEquals(((List<Activity>) distanceLeaderBoardByType).get(0).getDistance(),"10.0");
  }
  
  @Test
  public void testLocationLeaderBoard() {
    User user_marge = pacemaker.createUser(marge.firstname, marge.lastname, marge.email, marge.password);
    User user_maggie = pacemaker.createUser(maggie.firstname, maggie.lastname, maggie.email, maggie.password);
    User user_bart = pacemaker.createUser(bart.firstname, bart.lastname, bart.email, bart.password);
    pacemaker.followAFriend(homer.email, user_marge.email);
    pacemaker.followAFriend(homer.email, user_maggie.email);
    pacemaker.followAFriend(homer.email, user_bart.email);
    activities.forEach(activity -> pacemaker.createActivity(user_marge.getId(), activity.type, activity.location, activity.distance));
    activities_2.forEach(activity -> pacemaker.createActivity(user_maggie.getId(), activity.type, activity.location, activity.distance));
    activities_3.forEach(activity -> pacemaker.createActivity(user_bart.getId(), activity.type, activity.location, activity.distance));
    
    Collection<Activity> distanceLocationLeaderBoard = pacemaker.locationLeaderBoard(homer.getId(),"walk");
    assertEquals(distanceLocationLeaderBoard.size(),0);
    
    distanceLocationLeaderBoard = pacemaker.locationLeaderBoard(homer.getId(),"pool");
    assertEquals(distanceLocationLeaderBoard.size(),3);
  }
  
  @Test
  public void testCreateActivityWithSingleLocation() {
    pacemaker.deleteActivities(homer.id);
    Activity activity = new Activity("walk", "shop", 2.5);
    Location location = new Location(12.0, 33.0);

    Activity returnedActivity = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);
    pacemaker.addLocation(homer.id, returnedActivity.id, location.latitude, location.longitude);

    List<Location> locations = pacemaker.getLocations(homer.id, returnedActivity.id);
    assertEquals (locations.size(), 1);
    assertEquals (locations.get(0), location);
  }
  
  @Test
  public void testCreateActivityWithMultipleLocation() {
    pacemaker.deleteActivities(homer.id);
    Activity activity = new Activity("walk", "shop", 2.5);
    Activity returnedActivity = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);

    locations.forEach (location ->  pacemaker.addLocation(homer.id, returnedActivity.id, location.latitude, location.longitude));
    List<Location> returnedLocations = pacemaker.getLocations(homer.id, returnedActivity.id);
    assertEquals (locations.size(), returnedLocations.size());
    assertEquals(locations, returnedLocations);
  }
}