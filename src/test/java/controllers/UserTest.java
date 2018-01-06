package controllers;

import static models.Fixtures.bart;
import static models.Fixtures.maggie;
import static models.Fixtures.marge;
import static models.Fixtures.users;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;


import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import models.User;


public class UserTest {

  PacemakerAPI pacemaker = new PacemakerAPI("https://warm-escarpment-62674.herokuapp.com/");
  
  
  
 //https://whispering-scrubland-77173.herokuapp.com/ --KOTLIN
  //http://localhost:7000
  //https://warm-escarpment-62674.herokuapp.com/
  User homer = new User("homer", "simpson", "homer@simpson.com", "secret");

  @Before
  public void setup() {
    pacemaker.deleteUsers();
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testCreateUser() {
    User user = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
    assertEquals(user, homer);
    User user2 = pacemaker.getUserByEmail(homer.email);
    assertEquals(user2, homer);
    assertEquals(user2.toString(), user.toString());
  }
  
    @Test
  public void testCreateUsers() {
    users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
    Collection<User> returnedUsers = pacemaker.getUsers();
    assertEquals(users.size(), returnedUsers.size());
  }
  @Test
  public void testDeleteUser() {
    User user = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
    pacemaker.deleteUser(user.getId());
    User user2 = pacemaker.getUserByEmail(homer.email);
    assertNull(user2);
  }
  @Test
  public void testSetUserRole() {
    User user = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
    pacemaker.setUserRole(user.getId(), "ADMIN");
    User user2 = pacemaker.getUserByEmail(homer.email);
    assertEquals(user2.getRole(), "ADMIN");
  }
  
  @Test
  public void testActivationStatus() {
    User user = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
    assertTrue(user.getEnabled());
    pacemaker.setActivationStatus(user.getId(), "N");
    user = pacemaker.getUser(user.getId());
    assertTrue(!user.getEnabled());
    //User user2 = pacemaker.getUserByEmail(homer.email);
    //assertEquals(user2.getRole(), "ADMIN");
  }
  @Test
  public void testGetUserById() {
    User user = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
    User user2 = pacemaker.getUser(user.getId());
    assertEquals(user, user2);
  }
  @Test
  public void testGetUserByEmail() {
    User user = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
    User user2 = pacemaker.getUserByEmail(user.getEmail());
    assertEquals(user, user2);
  }
  @Test
  public void testFollowAFriend() {
    User user_homer = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
    User user_marge = pacemaker.createUser(marge.firstname, marge.lastname, marge.email, marge.password);
     pacemaker.followAFriend(user_homer.email, user_marge.email);
     Collection<User> friendList = pacemaker.listAllFriends(user_homer.getId());
     Collection<User> friendAsMargeList = friendList.stream().filter(
         user -> user.email.equalsIgnoreCase(user_marge.getEmail())).collect(Collectors.toList());
     assertEquals(friendAsMargeList.size(), 1);
  }
  
  @Test
  public void testUnFollowAFriend() {
    User user_homer = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
    User user_marge = pacemaker.createUser(marge.firstname, marge.lastname, marge.email, marge.password);
    pacemaker.followAFriend(user_homer.email, user_marge.email);
    Collection<User> friendList = pacemaker.listAllFriends(user_homer.getId());
    Collection<User> friendAsMargeList = friendList.stream().filter(user -> user.email.equalsIgnoreCase(user_marge.getEmail())).collect(Collectors.toList());
    assertEquals(friendAsMargeList.size(), 1);
    pacemaker.unfollowAFriend(user_homer.email, user_marge.email);
    friendList = pacemaker.listAllFriends(user_homer.getId());
    friendAsMargeList = friendList.stream().filter(
        user -> user.email.equalsIgnoreCase(user_marge.getEmail())).collect(Collectors.toList());
    assertEquals(friendAsMargeList.size(), 0);
  }
  
  @Test
  public void testmessageAFriend() {
    String test_message = "Message from homer to marge";
    User user_homer = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
    User user_marge = pacemaker.createUser(marge.firstname, marge.lastname, marge.email, marge.password);
    pacemaker.followAFriend(user_homer.email, user_marge.email);
    pacemaker.messageAFriend(user_homer.getId(), user_marge.getEmail(), test_message);
    Collection<String> messages = pacemaker.listAllMessages(user_marge.getId());
    long message_count = messages.stream()
        .filter(message -> {System.out.println(message + "::" + test_message);return message.contains(test_message);}).count();
    assertEquals(1,message_count);
  }
  
  @Test
  public void testmessageAllFriends() {
    String test_message1 = "Message from homer to All Friends";
    String test_message2 = "Message from homer to All Friends";
    String test_message3 = "Message from homer to All Friends";
    User user_homer = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
    User user_marge = pacemaker.createUser(marge.firstname, marge.lastname, marge.email, marge.password);
    User user_maggie = pacemaker.createUser(maggie.firstname, maggie.lastname, maggie.email, maggie.password);
    User user_bart = pacemaker.createUser(bart.firstname, bart.lastname, bart.email, bart.password);
    pacemaker.followAFriend(user_homer.email, user_marge.email);
    pacemaker.followAFriend(user_homer.email, user_maggie.email);
    pacemaker.followAFriend(user_homer.email, user_bart.email);
    pacemaker.messageAllFriend(user_homer.getId(), test_message1);
    pacemaker.messageAllFriend(user_homer.getId(), test_message2);
    pacemaker.messageAllFriend(user_homer.getId(), test_message3);
    Collection<String> messages_marge = pacemaker.listAllMessages(user_marge.getId());
    Collection<String> messages_maggie = pacemaker.listAllMessages(user_maggie.getId());
    Collection<String> messages_bart = pacemaker.listAllMessages(user_bart.getId());
    assertTrue(messages_marge.containsAll(messages_maggie));
    assertTrue(messages_bart.containsAll(messages_maggie));
    assertTrue(messages_bart.containsAll(messages_marge));
  }
  
 
  
}