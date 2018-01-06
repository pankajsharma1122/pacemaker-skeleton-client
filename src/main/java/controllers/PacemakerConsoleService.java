package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;

import asg.cliche.Command;
import asg.cliche.Param;
import models.Activity;
import models.User;
import parsers.AsciiTableParser;
import parsers.Parser;

public class PacemakerConsoleService {

  private PacemakerAPI paceApi = new PacemakerAPI("http://localhost:7000/");
  //https://warm-escarpment-62674.herokuapp.com/
  //http://localhost:7000
  //https://whispering-scrubland-77173.herokuapp.com/
  private Parser console = new AsciiTableParser();
  private User loggedInUser = null;

  public PacemakerConsoleService() {
  }

  // Starter Commands
  public void setPaceApi(PacemakerAPI paceApi)
  {
    this.paceApi = paceApi;
  }

  @Command(description = "Register: Create an account for a new user")
  public void register(@Param(name = "first name") String firstName,
      @Param(name = "last name") String lastName,
      @Param(name = "email") String email, @Param(name = "password") String password) {
    
    console.renderUser(paceApi.createUser(firstName, lastName, email, password));
  }
  

  @Command(description = "Register: Create an account for a new user")
  public void deleteUsers() {
    if(!isUserLoggedIn()) {return;}
    if(!isUserAdmin()) {return;}
    paceApi.deleteUsers();
    logout();
  }
  
  @Command(description = "Register: Create an account for a new user")
  public void deleteUser(@Param(name = "userId") String userId) {
    if(!isUserLoggedIn()) {return;}
    if(!isUserAdmin()) {return;}
    paceApi.deleteUser(userId);
  }

  @Command(description = "List Users: List all users emails, first and last names")
  public void listUsers() {
    console.renderUsers(paceApi.getUsers());
  }

  @Command(description = "Login: Log in a registered user in to pacemaker")
  public void login(@Param(name = "email") String email,
      @Param(name = "password") String password) {
    
    Optional<User> user = Optional.fromNullable(paceApi.getUserByEmail(email));
    if (user.isPresent()) {
      if (user.get().password.equals(password) && user.get().getEnabled()) {
        loggedInUser = user.get();
        console.println(loggedInUser.toString());
        console.println("ok");
      } else {
        console.println("Error on login.");
        if(!user.get().getEnabled()) {
          console.println("USER is disabled.");
        }
        console.println(user.get().toString());
        
      }
    }
  }

  @Command(description = "Logout: Logout current user")
  public void logout() {
    if (null != loggedInUser) {
      console.println("Logging out " + loggedInUser.email);
      console.println("ok");
    }
    
    loggedInUser = null;
  }

  @Command(description = "Add activity: create and add an activity for the logged in user")
  public void addActivity(
      @Param(name = "type") String type,
      @Param(name = "location") String location,
      @Param(name = "distance") double distance) {
    if(!isUserLoggedIn()) {return;}
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) {
      console.renderActivity(paceApi.createActivity(user.get().id, type, location, distance));
    }
  }

  
  
  @Command(description = "List Activities: List all activities for logged in user")
  public void listLocation(@Param(name = "activity-id") String activityId) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) {
      console.renderLocations(paceApi.getLocations(user.get().id, activityId));
    }
  }

  // Baseline Commands

  @Command(description = "Add location: Append location to an activity")
  public void addLocation(@Param(name = "activity-id") String activityId,
      @Param(name = "longitude") double longitude,
      @Param(name = "latitude") double latitude) {
    if(!isUserLoggedIn()) {return;}
    Optional<Activity> activity = Optional.fromNullable(paceApi.getActivity(loggedInUser.getId(), activityId));
    if (activity.isPresent()) {
      paceApi.addLocation(loggedInUser.getId(),activity.get().id, latitude, longitude);
      console.println("ok");
    } else {
      console.println("Activity not found");
    }
  }

  @Command(description = "List Activities: List all activities for logged in user")
  public void listActivities() {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) {
      console.renderActivities(paceApi.getActivities(user.get().id));
    }
  }
  
  @Command(description = "ActivityReport: List all activities for logged in user, sorted alphabetically by type")
  public void activityReport() {
    if(!isUserLoggedIn()) {return;}
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) {
      System.out.println("USER :: " + user.get().id);
      console.renderActivities(paceApi.listActivities(user.get().id, "type"));
    }
  }

  @Command(
      description = "Activity Report: List all activities for logged in user by type. Sorted longest to shortest distance")
  public void activityReport(@Param(name = "byType: type") String type) {
    if(!isUserLoggedIn()) {return;}
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) {
      List<Activity> reportActivities = new ArrayList<>();
      Collection<Activity> usersActivities = paceApi.listActivities(user.get().id, "DISTANCE");
      usersActivities.forEach(a -> {
        if (a.type.equals(type))
          reportActivities.add(a);
      });
      reportActivities.sort((a1, a2) -> {
        if (a1.distance >= a2.distance)
          return -1;
        else
          return 1;
      });
      console.renderActivities(reportActivities);
    }
  }

  @Command(description="List all locations for a specific activity",name="listActivityLocations", abbrev="lal")
  public void listActivityLocations(@Param(name = "activity-id") String id) {
    if(!isUserLoggedIn()) {return;}
    Optional<Activity> activity = Optional.fromNullable(paceApi.getActivity(loggedInUser.getId(), id));
    if (activity.isPresent()) {
      console.renderLocations(paceApi.getLocations(id, activity.get().id));
    }
  }

  @Command(description = "Follow Friend: Follow a specific friend")
  public void follow(@Param(name = "email") String email) {
    if(!isUserLoggedIn()) {return;}
    paceApi.followAFriend(loggedInUser.getEmail(),email);
  }
  
  @Command(description = "Unfollow Friends: Stop following a friend")
  public void unfollowFriend(@Param(name = "email") String email) {
    if(!isUserLoggedIn()) {return;}
    paceApi.unfollowAFriend(loggedInUser.getEmail(),email);
  }

  @Command(description = "List Friends: List all of the friends of the logged in user")
  public void listFriends() {
    if(!isUserLoggedIn()) {return;}
    System.out.println("********Friends *******");
    console.renderUsers(paceApi.listAllFriends(loggedInUser.getId()));
  }

  @Command(description = "Friend Activity Report: List all activities of specific friend, sorted alphabetically by type)")
  public void friendActivityReport(@Param(name = "email") String friendEmail) {
    if(!isUserLoggedIn()) {return;}
    console.renderActivities(paceApi.friendActivityReport(loggedInUser.getId(),friendEmail));
  }

  // Good Commands

  

  @Command(description = "Message Friend: send a message to a friend")
  public void messageFriend(@Param(name = "email") String friendEmail,
      @Param(name = "message") String message) {
    if(!isUserLoggedIn()) {return;}
    if (friendEmail.equalsIgnoreCase(loggedInUser.getEmail())) {
      System.out.println("Cannot Send message to yourself");
      return;
    }
    paceApi.messageAFriend(loggedInUser.getId(), friendEmail, message);
  }

  @Command(description = "List Messages: List all messages for the logged in user")
  public void listMessages() {
    if(!isUserLoggedIn()) {return;}
    console.renderMessageList(paceApi.listAllMessages(loggedInUser.getId()));
  }

  @Command(description = "Distance Leader Board: list summary distances of all friends, sorted longest to shortest")
  public void distanceLeaderBoard() {
    if(!isUserLoggedIn()) {return;}
    console.renderActivities(paceApi.distanceLeaderBoard(loggedInUser.getId()));
  }

  // Excellent Commands

  @Command(description = "Distance Leader Board: distance leader board refined by type")
  public void distanceLeaderBoardByType(@Param(name = "byType: type") String type) {
    if(!isUserLoggedIn()) {return;}
    console.renderActivities(paceApi.distanceLeaderBoardByType(loggedInUser.getId(),type));
  }

  @Command(description = "Message All Friends: send a message to all friends")
  public void messageAllFriends(@Param(name = "message") String message) {
    if(!isUserLoggedIn()) {return;}
    paceApi.messageAllFriend(loggedInUser.getId(), message);
  }

  @Command(description = "Location Leader Board: list sorted summary distances of all friends in named location")
  public void locationLeaderBoard(@Param(name = "location") String location) {
    if(!isUserLoggedIn()) {return;}
    console.renderActivities(paceApi.locationLeaderBoard(loggedInUser.getId(),location));
  }
  @Command(description = "Change user status to enabled or disabled by a Admin user")
  public void setActivationStatus(@Param(name = "id") String userId,@Param(name = "status") String status) {
    if(!isUserLoggedIn()) {return;}
    if(!isUserAdmin()) {return;}
    if (userId.equalsIgnoreCase(loggedInUser.getId())) {
      System.out.println("Cannot change self status");
      return;
    }
    if (!status.equalsIgnoreCase("Y") && !status.equalsIgnoreCase("N")) {
      System.out.println("Please provode valid activation status (Y/N)");
      return;
    }
    paceApi.setActivationStatus(userId,status);
  }
  
  @Command(description = "Change user status to enabled or disabled by a Admin user")
  public void setUserRole(@Param(name = "id") String userId,@Param(name = "status") String role) {
    if(!isUserLoggedIn()) {return;}
    if(!isUserAdmin()) {return;}
    if(!isValidRoleType(role)) {return;}
    if (userId.equalsIgnoreCase(loggedInUser.getId())) {
      System.out.println("Cannot change self status");
      return;
    }
    paceApi.setUserRole(userId,role);
  }
  
  private boolean isValidRoleType(String role)
  {
    if ("ADMIN".equals(role) || "USER".equals(role)) {
      return true;
    } else {
      System.out.println("Please pass valid user role (ADMIN/USER)");
      return false;
      
    }
    
  }

  private boolean isUserLoggedIn()
  {
    if (null == loggedInUser) {
      System.out.println("User not loged in");
      return false;
    } else {
      System.out.println("User logged in :: >>" + loggedInUser.getEmail() + "::ROLE::" + loggedInUser.getRole());
      return true;
    }
  }
  private boolean isUserAdmin()
  {
    if (!loggedInUser.getRole().equals("ADMIN")) {
      System.out.println("User not loged in as Administrator");
      return false;
    } else {
      //System.out.println("Admin User logged in :: >>" + loggedInUser.getEmail());
      return true;
    }
  }
  // Outstanding Commands

  // Todo
}