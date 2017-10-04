package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import netscape.javascript.JSObject;

public class Main extends Application{

	
	
	
   public class Bridge{
   
      public void storeInfo(String zip, String cit, String st, String count, Double lat, Double longt, String visitType, String visitDesc, int visitorNum, String hotelStay, String email, String timeSt)
      {
         if(timeSt.equals("dummy"))
         {
            System.out.println("Starting DB");

            String ZipCode = zip;
            String City = cit;
            String State = st;
            String Country = count;
            Double Latitude = lat;
            Double Longitude = longt;
            String VisitType = visitType;
            String VisitDescription = visitDesc;	
            int VisitorNum = visitorNum;
            String HotelStay = hotelStay;
            String Email = email;
            String TimeStamp = timeSt;
            String query = "INSERT INTO visitorinformationtable (zipcode, city, state, country, latitude, longitude, visittype, visitdescription, visitornum, hotelstay, email, timestamp) VALUES ('"+ZipCode+"','"+City+"','"+State+"','"+Country+"','"+Latitude+"','"+Longitude+"', '"+VisitType+"','"+VisitDescription+"', '"+VisitorNum+"', '"+HotelStay+"','"+Email+"','"+TimeStamp+"')";
            String query2 = "DELETE FROM visitorinformationtable WHERE TimeStamp = 'dummy'";
            System.out.println(query);
            System.out.println(query2);
         
            try
            {
               System.out.println("Test has begun!");
               Connection c = DBConnection.getConnection();
               System.out.println("Test");
               Statement stmt = c.createStatement();
               stmt.executeUpdate(query);
               System.out.println("Saved to the database!");
               stmt.executeUpdate(query2);
               System.out.println("Deleted from the database!");
            	//JOptionPane.showMessageDialog(this, "Saved");
            }
            catch(Exception e)
            {
               e.printStackTrace();
            }
         }
         //DBConnection testOne = new DBConnection();
         else
         {
        	 String ZipCode = zip;
             String City = cit;
             String State = st;
             String Country = count;
             Double Latitude = lat;
             Double Longitude = longt;
             String VisitType = visitType;
             String VisitDescription = visitDesc;
             int VisitorNum = visitorNum;
             String HotelStay = hotelStay;
             String Email = email;
             String TimeStamp = timeSt;
             String query = "INSERT INTO visitorinformationtable (zipcode, city, state, country, latitude, longitude, visittype, visitdescription, visitornum, hotelstay, email, timestamp) VALUES ('"+ZipCode+"','"+City+"','"+State+"','"+Country+"','"+Latitude+"','"+Longitude+"', '"+VisitType+"','"+VisitDescription+"', '"+VisitorNum+"', '"+HotelStay+"','"+Email+"','"+TimeStamp+"')";
             System.out.println(query);
         
            try
            {
               System.out.println("Test has begun!");
               Connection c = DBConnection.getConnection();
               System.out.println("Test");
               Statement stmt = c.createStatement();
               stmt.executeUpdate(query);
               System.out.println("Saved to the database!");
               //JOptionPane.showMessageDialog(this, "Saved");
            }
            catch(Exception e)
            {
               e.printStackTrace();
            }
         }
      
      }
      
   }




   private Timeline locationUpdateTimeline;

   @Override public void start(Stage stage) throws IOException {
   	// create web engine and view
      final WebView webView = new WebView();
      final WebEngine webEngine = webView.getEngine();
      webEngine.load(getClass().getResource("map.html").toString());
   	//final WebEngine webEngine = new WebEngine(getClass().getResource("googlemap.html").toString());
   	//final WebView webView = new WebView(webEngine);
      webEngine.getLoadWorker().stateProperty().addListener(
         new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends State> ov, State t, State t1) {
               if (t1 == Worker.State.SUCCEEDED) {
            	   
            	   
            	   try{
            		   	  //String mapName = (String) webEngine.executeScript("map");
            	          List<String> latArray = new ArrayList<String>();
            	          List<String> longArray = new ArrayList<String>();
            	          Connection initCon = DBConnection.getConnection();
            	          Statement initStmt = initCon.createStatement();
            	          String initQuery = "SELECT latitude, longitude  FROM visitorinformationtable";
            	          ResultSet initRs =initStmt.executeQuery(initQuery);
            	          while(initRs.next())
            	          {
            	             latArray.add(initRs.getString(1));
            	             double lat1 = Double.parseDouble(initRs.getString(1));
            	             //System.out.println(initRs.getString(1));
            	             double lat2 = Double.parseDouble(initRs.getString(2));
            	             longArray.add(initRs.getString(2));
            	             //System.out.println(initRs.getString(2));
            	             //webEngine.executeScript("document.placeInitMarker(\""+lat1+"\",\""+lat2+"\")");
            	             webEngine.executeScript("document.placeInitMarker("+lat1+","+lat2+")");
            	             //System.out.println("document.placeInitMarker("+lat1+","+lat2+")");
            	             //System.out.println(mapName);
            	          }
            	       }
            	       catch(Exception e)
            	       {
            	          e.printStackTrace();
            	       }
            	   	
                  JSObject window = (JSObject) webEngine.executeScript("window");
                  window.setMember("app", new Bridge());
               }
            }
         });
      
      
      
      
      /*
     	 *	Place the markers on the pin 
     	*/
     	/*
      try{
          List<String> latArray = new ArrayList<String>();
          List<String> longArray = new ArrayList<String>();
          Connection initCon = DBConnection.getConnection();
          Statement initStmt = initCon.createStatement();
          String initQuery = "SELECT latitude, longitude  FROM visitorinformationtable";
          ResultSet initRs =initStmt.executeQuery(initQuery);
          while(initRs.next())
          {
             latArray.add(initRs.getString(1));
             String lat1 = initRs.getString(1);
             //System.out.println(initRs.getString(1));
             String lat2 = initRs.getString(2);
             longArray.add(initRs.getString(2));
             //System.out.println(initRs.getString(2));
             //webEngine.executeScript("document.placeInitMarker(\""+lat1+"\",\""+lat2+"\")");
             webEngine.executeScript("document.placeInitMarker(\""+lat1+"\",\""+lat2+"\")");
             //System.out.println("placeInitMarker("+lat1+","+lat2+");");
          }
       	
       }
       catch(Exception e)
       {
          e.printStackTrace();
       }
      
      */
      
     
   	/*
   	// create map type buttons
   	final ToggleGroup mapTypeGroup = new ToggleGroup();
   	final ToggleButton road = new ToggleButton("Road");
   	road.setSelected(true);
   	road.setToggleGroup(mapTypeGroup);
   	final ToggleButton satellite = new ToggleButton("Satellite");
   	satellite.setToggleGroup(mapTypeGroup);
   	final ToggleButton hybrid = new ToggleButton("Hybrid");
   	hybrid.setToggleGroup(mapTypeGroup);
   	final ToggleButton terrain = new ToggleButton("Terrain");
   	terrain.setToggleGroup(mapTypeGroup);
   	mapTypeGroup.selectedToggleProperty().addListener(
   			new ChangeListener<Toggle>() {
   				public void changed(
   						ObservableValue<? extends Toggle> observableValue,
   						Toggle toggle, Toggle toggle1) {
   					if (road.isSelected()) {
   						webEngine.executeScript("document.setMapTypeRoad()");
   					} else if (satellite.isSelected()) {
   						webEngine.executeScript("document.setMapTypeSatellite()");
   					} else if (hybrid.isSelected()) {
   						webEngine.executeScript("document.setMapTypeHybrid()");
   					} else if (terrain.isSelected()) {
   						webEngine.executeScript("document.setMapTypeTerrain()");
   					}
   				}
   			});
   			
   	// add map source toggles
   	ToggleGroup mapSourceGroup = new ToggleGroup();
   	final ToggleButton google = new ToggleButton("Google");
   	google.setSelected(true);
   	google.setToggleGroup(mapSourceGroup);
   	final ToggleButton yahoo = new ToggleButton("Yahoo");
   	yahoo.setToggleGroup(mapSourceGroup);
   	final ToggleButton bing = new ToggleButton("Bing");
   	bing.setToggleGroup(mapSourceGroup);
   	// listen to selected source
   	mapSourceGroup.selectedToggleProperty().addListener(
   			new ChangeListener<Toggle>() {
   				public void changed(
   						ObservableValue<? extends Toggle> observableValue,
   						Toggle toggle, Toggle toggle1) {
   					terrain.setDisable(true);
   					if (google.isSelected()) {
   						terrain.setDisable(false);
   						webEngine.load(
   								getClass().getResource("googlemap.html").toString());
   					} 
   					else if (yahoo.isSelected()) {
   
   						webEngine.load(
   								getClass().getResource("bingmap.html").toString());
   					} else if (bing.isSelected()) {
   						webEngine.load(
   								getClass().getResource("yahoomap.html").toString());
   					}
   					mapTypeGroup.selectToggle(road);
   				}
   			});
   
   	 	*/
   	// add search
      final TextField searchBox = new TextField("");
      searchBox.setPrefColumnCount(12);
      searchBox.setPromptText("Search");
      searchBox.textProperty().addListener(
         new ChangeListener<String>() {
            public void changed(
            	ObservableValue<? extends String> observableValue,
            	String s, String s1) {
            // delay location updates to we don't go too fast file typing
               if (locationUpdateTimeline!=null) locationUpdateTimeline.stop();
               locationUpdateTimeline = new Timeline();
               locationUpdateTimeline.getKeyFrames().add(
                  new KeyFrame(new Duration(400),
                        new EventHandler<ActionEvent>() {
                           public void handle(ActionEvent actionEvent) {
                              webEngine.executeScript("document.goToLocation(\""+searchBox.getText()+"\")");
                           }
                        })
                  );
               locationUpdateTimeline.play();
            }
         });
   
   	// add Pin Box
      final TextField pinBox = new TextField("");
      pinBox.setPrefColumnCount(12);
      pinBox.setPromptText("ZIP Code");
   	//
   
   	//add pinIt button
   
      Button pinIt = new Button("Pin it!");
      pinIt.setOnAction(
         new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
               webEngine.executeScript("document.markZip(\"" + searchBox.getText() +"\")");
            }
         });
   
      Button zoomIn = new Button("Zoom In");
      zoomIn.setOnAction(
         new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
               webEngine.executeScript("document.zoomIn()");
            }
         });
      Button zoomOut = new Button("Zoom Out");
      zoomOut.setOnAction(
         new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
               webEngine.executeScript("document.zoomOut()");
            }
         });
   	// create toolbar
      ToolBar toolBar = new ToolBar();
      toolBar.getStyleClass().add("map-toolbar");
      toolBar.getItems().addAll(new Label("Location:"),searchBox,pinIt, zoomIn, zoomOut);
   	// create root
      BorderPane root = new BorderPane();
   	//Parent root = FXMLLoader.load(getClass().getResource("/application/webView.fxml"));
      //root.getStyleClass().add("map");
      root.setCenter(webView);
   	  root.setTop(toolBar); 
   	  //create scene
      stage.setTitle("Web Map");
      Scene scene = new Scene(root,1000,700, Color.web("#666970"));
      stage.setScene(scene);
      scene.getStylesheets().add("/application.css");
      stage.setMaximized(true);
      //stage.setResizable(false);
      stage.show();
   }

   private Node createSpacer() {
      Region spacer = new Region();
      HBox.setHgrow(spacer, Priority.ALWAYS);
      return spacer;
   }

   static { // use system proxy settings when standalone application
      System.setProperty("java.net.useSystemProxies", "true");
   }

   public static void main(String[] args){
      Application.launch(args);
   }
}