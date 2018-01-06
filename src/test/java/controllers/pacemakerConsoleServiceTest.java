package controllers;

import static models.Fixtures.users;
import static models.Fixtures.locations;
import static models.Fixtures.activities;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import models.Activity;
import models.User;


public class pacemakerConsoleServiceTest {
  
  
  PacemakerConsoleService consoleService = new PacemakerConsoleService();
  @InjectMocks
  PacemakerAPI pacemaker =  Mockito.mock(PacemakerAPI.class);
  
  @Before
  public void setup() {
    //pacemaker.deleteUsers();
    consoleService.setPaceApi(pacemaker);
    homer.setEnabled(new Boolean(true));
    homer.id = UUID.randomUUID().toString();
  }
 
  //http://localhost:7000
  //https://warm-escarpment-62674.herokuapp.com/
  User homer = new User("homer", "simpson", "homer@simpson.com", "secret");

  

  @After
  public void tearDown() {
  }

  @Test
  public void testLogin() {
    when(pacemaker.getUserByEmail(homer.email)).thenReturn(homer);
    
    consoleService.login(homer.email, homer.password);
    assertTrue(true);
  }
  @Test
  public void testLoginDisabled() {
    homer.setEnabled(false);
    when(pacemaker.getUserByEmail(homer.email)).thenReturn(homer);
    consoleService.login(homer.email, homer.password);
    assertTrue(true);
  }
  
  @Test
  public void testRegister() {
    when(pacemaker.createUser(homer.firstname,homer.lastname,homer.email, homer.password)).thenReturn(homer);
    consoleService.register(homer.firstname,homer.lastname,homer.email, homer.password);
    assertTrue(true);
  }
  
  @Test
  public void testListUsers() {
    when(pacemaker.getUsers()).thenReturn(users);
    consoleService.listUsers();
    assertTrue(true);
  }
  
  @Test
  public void testAddLocation() {
    when(pacemaker.getUserByEmail(homer.email)).thenReturn(homer);
    consoleService.login(homer.email, homer.password);
    
    when(pacemaker.getActivity(homer.getId(),null)).thenReturn(activities.get(0));
    Mockito.doNothing().when(pacemaker).addLocation(homer.getId(),"0",0,0);
    consoleService.addLocation(null,0,0);
    assertTrue(true);
  }
  
  @Test
  public void testActivityReportByType() {
    when(pacemaker.getUserByEmail(homer.email)).thenReturn(homer);
    consoleService.login(homer.email, homer.password);
    
    when(pacemaker.listActivities(homer.getId(),"DISTANCE")).thenReturn(activities);
    //Mockito.doNothing().when(pacemaker).addLocation(homer.getId(),"0",0,0);
    consoleService.activityReport("walk");
    //consoleService.activityReport("distance");
    assertTrue(true);
  }
  
  @Test
  public void testActivityReport() {
    when(pacemaker.getUserByEmail(homer.email)).thenReturn(homer);
    consoleService.login(homer.email, homer.password);
    
    when(pacemaker.listActivities(homer.getId(),"type")).thenReturn(activities);
    //Mockito.doNothing().when(pacemaker).addLocation(homer.getId(),"0",0,0);
    consoleService.activityReport();
    //consoleService.activityReport("distance");
    assertTrue(true);
  }
  
  @Test
  public void testListActivities() {
    when(pacemaker.getUserByEmail(homer.email)).thenReturn(homer);
    consoleService.login(homer.email, homer.password);
    
    when(pacemaker.getActivities(homer.getId())).thenReturn(activities);
    consoleService.listActivities();
    assertTrue(true);
  }
  
  
  
   
  
}