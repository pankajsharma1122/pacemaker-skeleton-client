package controllers;

import static org.mockito.Mockito.doThrow;

import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import models.User;
import models.Activity;
import models.Fixtures;
import models.Location;


public class MockUserExceptionTest {

  PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000/");
  
  
  @InjectMocks
  PacemakerInterface pacemakerInterface = Mockito.mock(PacemakerInterface.class);

  //http://localhost:7000
  //https://warm-escarpment-62674.herokuapp.com/
  User homer = new User("homer", "simpson", "homer@simpson.com", "secret");

  @Before
  public void setup() {
    //pacemaker.deleteUsers();
    pacemaker.pacemakerInterface = this.pacemakerInterface;
  }

  @After
  public void tearDown() {
  }

    
  @Test
  public void testCreateUserException() {
   doThrow(new RuntimeException("register user Service not working")).when(pacemakerInterface).registerUser(
       new User(homer.firstname, homer.lastname, homer.email, homer.password));
   User user = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
   assertNull(user);
  }
  
  @Test
  public void testDeleteUserException() {
   doThrow(new RuntimeException("DELETE user Service not working")).when(pacemakerInterface).deleteUser(null);
   User user = pacemaker.deleteUser(null);
   assertNull(user);
  }
  @Test
  public void testDeleteusersException() {
    //pacemakerInterface.deleteUsers()
    doThrow(new RuntimeException("delete All user Service not working")).when(pacemakerInterface).deleteUsers();
    pacemaker.deleteUsers();
    assertNull(null);
  }
  
  @Test
  public void testUnfollowFriendException() {
    //pacemakerInterface.deleteUsers()
    doThrow(new RuntimeException("Unfollow friend Service not working")).when(pacemakerInterface).unfollowAFriend(homer.email, homer.email);
    pacemaker.unfollowAFriend(homer.email, homer.email);
    assertNull(null);
  }
  
  @Test
  public void testgetUsersException() {
   doThrow(new RuntimeException("Get User  Service not working")).when(pacemakerInterface).getUsers();
   Collection<User> users = pacemaker.getUsers();
   assertNull(users);
  }
  
  @Test
  public void testCreateActivityException() {
   doThrow(new RuntimeException("CreateActivity  Service not working")).
         when(pacemakerInterface).addActivity("0",Fixtures.activities.get(0));
   Activity activity = pacemaker.createActivity("0",Fixtures.activities.get(0).type,Fixtures.activities.get(0).location,Fixtures.activities.get(0).distance);
   assertNull(activity);
  }
  
  @Test
  public void testGetActivityException() {
   doThrow(new RuntimeException("GetActivity  Service not working")).
         when(pacemakerInterface).getActivity("0","0");
   Activity activity = pacemaker.getActivity("0","0");
   assertNull(activity);
  }
  
  @Test
  public void testgetActivitiesException() {
   doThrow(new RuntimeException("getActivities  Service not working")).
         when(pacemakerInterface).getActivities("0");
   Collection<Activity> activity = pacemaker.getActivities("0");
   assertNull(activity);
  }
  
  @Test
  public void testlistActivitiesException() {
   doThrow(new RuntimeException("listActivities  Service not working")).
         when(pacemakerInterface).listActivities("0","distance");
   List<Activity> activity = pacemaker.listActivities("0","distance");
   assertNull(activity);
  }
  
  @Test
  public void testaddLocationException() {
   doThrow(new RuntimeException("addLocation  Service not working")).
         when(pacemakerInterface).addLocation("0","0",Fixtures.locations.get(0));
   pacemaker.addLocation("0","distance",
             Fixtures.locations.get(0).getLatitude(),Fixtures.locations.get(0).getLongitude());
   assertNull(null);
  }
  
  @Test
  public void testgetUserException() {
   doThrow(new RuntimeException("getUser  Service not working")).
         when(pacemakerInterface).getUser("0");
   User user = pacemaker.getUser("0");
   assertNull(user);
  }
  
  @Test
  public void testgetLocationsException() {
   doThrow(new RuntimeException("getLocations  Service not working")).
         when(pacemakerInterface).getLocations("0","0");
   List<Location> location = pacemaker.getLocations("0","0");
   assertNull(location);
  }
  
  @Test
  public void testdeleteActivitiesException() {
   doThrow(new RuntimeException("deleteActivities  Service not working")).
         when(pacemakerInterface).deleteActivities("0");
   pacemaker.deleteActivities("0");
   assertNull(null);
  }
  
  @Test
  public void testfollowAFriendException() {
   doThrow(new RuntimeException("followAFriend  Service not working")).
         when(pacemakerInterface).followAFriend(Fixtures.users.get(0).email,Fixtures.users.get(1).email);
   pacemaker.followAFriend(Fixtures.users.get(0).email,Fixtures.users.get(1).email);
   assertNull(null);
  }
  
  @Test
  public void testunfollowAFriendException() {
   doThrow(new RuntimeException("unfollowAFriend  Service not working")).
         when(pacemakerInterface).unfollowAFriend(Fixtures.users.get(0).email,Fixtures.users.get(1).email);
   pacemaker.unfollowAFriend(Fixtures.users.get(0).email,Fixtures.users.get(1).email);
   assertNull(null);
  }
  
  @Test
  public void testmessageAllFriendException() {
   doThrow(new RuntimeException("messageAllFriend  Service not working")).
         when(pacemakerInterface).messageAllFriend(Fixtures.users.get(0).email,"Test message");
   pacemaker.messageAllFriend(Fixtures.users.get(0).email,"Test message");
   assertNull(null);
  }
  
  @Test
  public void testmessageAFriendException() {
   doThrow(new RuntimeException("messageAFriend  Service not working")).
         when(pacemakerInterface).messageAFriend("0",Fixtures.users.get(0).email,"Test message");
   pacemaker.messageAFriend("0",Fixtures.users.get(0).email,"Test message");
   assertNull(null);
  }
  
  @Test
  public void testlistAllMessagesException() {
   doThrow(new RuntimeException("listAllMessage  Service not working")).
         when(pacemakerInterface).listAllMessages("0");
   Collection<String> messages = pacemaker.listAllMessages("0");
   assertNull(messages);
  }

  
  
}