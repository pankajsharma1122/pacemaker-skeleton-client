package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import models.Activity;
import models.Location;
import models.User;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

interface PacemakerInterface
{
    @GET("/users")
    Call<List<User>> getUsers();
    
    @POST("/users")
    Call<User> registerUser(@Body User User);
    
    @POST("/users/{id}/status/{status}")
    Call<User> updateActivationStatus(@Path("id") String id,@Path("status") String status);
    @POST("/users/{id}/roles/{role}")
    Call<User> updateRole(@Path("id") String id,@Path("role") String role);
    
    @GET("/users/{id}/activities")
    Call<List<Activity>> getActivities(@Path("id") String id);
    
    @GET("/users/{id}/activities/{sortBy}")
    Call<List<Activity>> listActivities(@Path("id") String id,@Path("sortBy") String sortBy);
    

    @POST("/users/{id}/activities")
    Call<Activity> addActivity(@Path("id") String id, @Body Activity activity);
    
    
    @GET("/users/{email}")
    Call<User> getUserByEmail(@Path("email") String email);
    
    @GET("/users/{id}/activity/{activityId}")
    Call<Activity> getActivity(@Path("id") String userId, @Path("activityId") String activityId);
    
    @GET("/users/{id}/activities/{activityId}/locations")
    Call<List<Location>> getActivityLocations(@Path("id") String id,@Path("activityId") String activityId);
    
    @POST("/users/{id}/activities/{activityId}/locations")
    Call<Location> addLocation(@Path("id") String id,@Path("activityId") String activityId, @Body Location location);
    
    @POST("/users/{id}/friends/{friendId}/follow")
    Call<User> followAFriend(@Path("id") String id,@Path("friendId") String friendId);
    @POST("/users/{id}/friends/{friendId}/unfollow")
    Call<User> unfollowAFriend(@Path("id") String id,@Path("friendId") String friendId);
    
    @GET("/users/{id}/friends")
    Call<List<User>> listAllFriends(@Path("id") String id);
    
    @DELETE("/users")
    Call<User> deleteUsers();

    @DELETE("/users/{id}")
    Call<User> deleteUser(@Path("id") String id);

    @GET("/users/{id}")
    Call<User> getUser(@Path("id") String id);
    
    @DELETE("/users/{id}/activities")
    Call<String> deleteActivities(@Path("id") String id);
    
    @GET("/users/{id}/activities/{activityId}/locations")
    Call<List<Location>> getLocations(@Path("id") String id, @Path("activityId") String activityId);
    
    
    @POST("/users/{id}/friends/{friendEmail}/message")
    Call<User> messageAFriend(@Path("id") String userId,@Path("friendEmail") String friendEmail, @Body String message);
    @POST("/users/{id}/friends/message")
    Call<User> messageAllFriend(@Path("id") String userId, @Body String message);
    @GET("/users/{id}/messages")
  Call<List<String>> listAllMessages(@Path("id") String id);
}

public class PacemakerAPI {

  PacemakerInterface pacemakerInterface;

  public PacemakerAPI(String url) {
    Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson)).build();
    pacemakerInterface = retrofit.create(PacemakerInterface.class);
  }

  public Collection<User> getUsers() {
    Collection<User> users = null;
    try {
      Call<List<User>> call = pacemakerInterface.getUsers();
      Response<List<User>> response = call.execute();
      users = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return users;
  }

  public void deleteUsers() {
    try {
      Call<User> call = pacemakerInterface.deleteUsers();
      call.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public User createUser(String firstName, String lastName, String email, String password) {
    User returnedUser = null;
    try {
      Call<User> call = pacemakerInterface.registerUser(new User(firstName, lastName, email, password));
      Response<User> response = call.execute();
      returnedUser = response.body();    
    } catch (Exception e) {
      System.out.println("HELLO" + e.getMessage());
    }
    return returnedUser;
  }

  public Activity createActivity(String id, String type, String location, double distance) {
    Activity returnedActivity = null;
    try {
      Call<Activity> call = pacemakerInterface.addActivity(id, new Activity(type, location, distance));
      Response<Activity> response = call.execute();
      returnedActivity = response.body();    
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return returnedActivity;
  }

  public Activity getActivity(String userId, String activityId) {
    Activity activity = null;
     try {
       Call<Activity> call = pacemakerInterface.getActivity(userId, activityId);
       Response<Activity> response = call.execute();
       activity = response.body();
     } catch (Exception e) {
       e.printStackTrace();
     }
     return activity;
   }

  public Collection<Activity> getActivities(String id) {
    Collection<Activity> activities = null;
    try {
      Call<List<Activity>> call = pacemakerInterface.getActivities(id);
      Response<List<Activity>> response = call.execute();
      activities = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return activities;
  }

  public List<Activity> listActivities(String userId, String sortBy) {
    List<Activity> activities = null;
    try {
      Call<List<Activity>> call = pacemakerInterface.listActivities(userId, sortBy);
      Response<List<Activity>> response = call.execute();
      activities = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return activities;
  }

  public void addLocation(String id,String activityId, double latitude, double longitude) {
    
    try {
      Call<Location> call = pacemakerInterface.addLocation(id, activityId, new Location(latitude, longitude));
      call.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public User getUserByEmail(String email) {
    Collection<User> users = getUsers();
    User foundUser = null;
    for (User user : users) {
      if (user.email.equals(email)) {
        foundUser = user;
      }
    }
    return foundUser;
  }

  public User getUser(String id) {
    User user = null;
    try {
      Call<User> call = pacemakerInterface.getUser(id);
      Response<User> response = call.execute();
      user = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return user;
  }

  public User deleteUser(String userId) {
    User user = null;
    try {
      Call<User> call = pacemakerInterface.deleteUser(userId);
      Response<User> response = call.execute();
      user = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return user;
  }

  public List<Location> getLocations(String id, String activityId) {
    List<Location> locations = null;
    try {
      Call<List<Location>> call = pacemakerInterface.getLocations(id, activityId);
      Response<List<Location>> response = call.execute();
      locations = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return locations;
  }
  
  public void deleteActivities(String id) {
    try {
      Call<String> call = pacemakerInterface.deleteActivities(id);
      call.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void followAFriend(String userEmail, String friendEmail)
  {
    try {
      Call call = pacemakerInterface.followAFriend(userEmail,friendEmail);
      call.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    
  }
  
  public void unfollowAFriend(String userEmail, String friendEmail)
  {
    try {
      Call call = pacemakerInterface.unfollowAFriend(userEmail,friendEmail);
      call.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    
  }
  
  public void messageAFriend(String userId, String friendEmail, String message)
  {
    try {
      Call call = pacemakerInterface.messageAFriend(userId,friendEmail,message);
      call.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  public void messageAllFriend(String userId,String message)
  {
    try {
      Call call = pacemakerInterface.messageAllFriend(userId,message);
      call.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  public Collection<String> listAllMessages(String userId)
  {
    List<String> messages = null;
    try {
      Call<List<String>> call = pacemakerInterface.listAllMessages(userId);
      Response<List<String>> response = call.execute();
      messages = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return messages;
    
  }
  
  public Collection<User> listAllFriends(String userId)
  {
    List<User> friends = null;
    try {
      Call<List<User>> call = pacemakerInterface.listAllFriends(userId);
      Response<List<User>> response = call.execute();
      friends= response.body();
      //response<List<User> call.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return friends;
  }

  public Collection<Activity> friendActivityReport(String id,String friendEmail)
  {
    Collection<Activity> activities = null;
    Collection<User> friends =  listAllFriends(id);
    Collection<User> selectedFriends = 
        friends.stream().filter(
            user -> user.email.equalsIgnoreCase(friendEmail)).collect(Collectors.toList());
    Optional<User> selectedFriend = selectedFriends.stream().findFirst();
    if (selectedFriend.isPresent()) {
      User user = selectedFriend.get();
      activities = listActivities(user.getId(),"type");
    } else {
      System.out.println("Provided Email is not in friend List");
    }
    return activities;
    
  }

  public List<Activity> distanceLeaderBoard(String id)
  {
    Collection<User> friends =  listAllFriends(id);
    List<Activity> allFriendsActivities = new ArrayList<Activity>();
    friends.forEach(user -> allFriendsActivities.addAll(getActivities(user.getId())));
    allFriendsActivities.sort((a1, a2) -> Double.compare(a2.distance,a1.distance));
    return allFriendsActivities;
  }

  public Collection<Activity> distanceLeaderBoardByType(String id, String type)
  {
    List<Activity> allFriendsActivities = distanceLeaderBoard(id);
    List<Activity> allFriendsActivitiesByType = allFriendsActivities.stream().filter(activity -> 
        activity.getType().equalsIgnoreCase(type)).collect(Collectors.toList());
    return allFriendsActivitiesByType;
  }

  public Collection<Activity> locationLeaderBoard(String id, String location)
  {
    List<Activity> allFriendsActivities = distanceLeaderBoard(id);
    List<Activity> allFriendsActivitiesByLocation = allFriendsActivities.stream().filter(activity -> 
    activity.getLocation().equalsIgnoreCase(location)).collect(Collectors.toList());
    return allFriendsActivitiesByLocation;
  }

  public User setActivationStatus(String id, String status)
  {
    User user = null;
    try {
      Call<User> call = pacemakerInterface.updateActivationStatus(id,status);
      Response<User> response = call.execute();
      user = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return user;
  }
  
  public User setUserRole(String id, String role)
  {
    User user = null;
    try {
      Call<User> call = pacemakerInterface.updateRole(id,role);
      Response<User> response = call.execute();
      user = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return user;
  }

  
}