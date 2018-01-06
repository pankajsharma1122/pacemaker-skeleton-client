package controllers;

import static org.mockito.Mockito.doThrow;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import models.User;


public class MockUserTest {

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

  
  
}