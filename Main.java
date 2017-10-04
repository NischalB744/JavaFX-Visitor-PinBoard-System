package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import netscape.javascript.JSObject;

public class Main extends Application {
	final WebView webView = new WebView();
	final WebEngine webEngine = webView.getEngine();
	
			public void storeInfo(String zip, String cit, String st, String count, Double lat, Double longt, String visitType, String visitDesc, int visitorNum, String hotelStay, String email, String timeSt)
		      {
				
		        
		             //System.out.println("the zipcode is :" + ZipCode);
		        	 String query = "INSERT INTO visitorinformationtable (zipCode, city, state, country, latitude, longitude, visitType, visitDetails, numOfVisitors, placeOfStay, email_ID, timestamp) VALUES ('"+zip+"','"+cit+"','"+st+"','"+count+"','"+lat+"','"+longt+"', '"+visitType+"','"+visitDesc+"', '"+visitorNum+"', '"+hotelStay+"','"+email+"','"+timeSt+"')";
		        	 System.out.println(query);
		         
		            try
		            {
		               Connection c = DBConnection.getConnection();
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
			
			
			public void analyzeDB(String anCity, String anState, String anCountry ) throws ClassNotFoundException, SQLException
			{
				String cityName = anCity;
				String stateName = anState;
				String countryName = anCountry;
				int cityCount =0;
				int stateCount =0;
				int countryCount =0;
				Connection anCon = DBConnection.getConnection();
   	          	Statement anStmt = anCon.createStatement();
   	          	String anCityQuery = "SELECT * FROM visitorinformationtable WHERE city = \'" +anCity+ "\'";
   	          	String anStateQuery = "SELECT * FROM visitorinformationtable WHERE state = \'" + anState + "\'";
   	          	String anCountryQuery = "SELECT * FROM visitorinformationtable WHERE state = \'" + anCountry + "\'";
   	          	System.out.println(anCityQuery);
   	          	ResultSet anCityRs = anStmt.executeQuery(anCityQuery);
   	          	while(anCityRs.next())
   	          	{
   	          		cityCount++;
   	          	}
   	          	ResultSet anStateRs = anStmt.executeQuery(anStateQuery);
	          	while(anStateRs.next())
	          	{
	          		stateCount++;
	          	}
	          	ResultSet anCountryRs = anStmt.executeQuery(anCountryQuery);
   	          	while(anCountryRs.next())
   	          	{
   	          		countryCount++;
   	          	}
   	          	
   	          	webEngine.executeScript("analyzedInfoFromDB("+cityCount+","+stateCount+","+countryCount+","+cityName+","+stateName+","+countryName+")");
			}
			public void pinOffline(int offlineNum) throws ClassNotFoundException, SQLException
			{
				String searchLocationString ="";
				 Connection offlineCon = DBConnection.getConnection();
   	          	Statement offlineStmt = offlineCon.createStatement();
				String num = Integer.toString(offlineNum);
				String offlineQuery =  "SELECT country, state, city  FROM visitorinformationtable ORDER BY SN DESC LIMIT " + num  ;
				System.out.println(offlineQuery);
				ResultSet offlineRs =offlineStmt.executeQuery(offlineQuery);
				while(offlineRs.next())
  	          	{
					String offlineCountry = offlineRs.getString(1);
					String offlineState = offlineRs.getString(2);
					String offlineCity = offlineRs.getString(3);
					if(offlineCountry.equals("United States of America"))
					{
						if(((!offlineState.equals("")) && (!offlineState.equals("N/A"))))
						{
							if((!offlineCity.equals("")))
							{
								searchLocationString = offlineCity + "," + offlineState + "," + offlineCountry;
							}
							
							else if (offlineCity.equals(""))
							{
								searchLocationString = offlineState + "," + offlineCountry;
							}
						}
						else
						{
							searchLocationString =offlineCountry;
						}
					}
					else
					{
							searchLocationString = offlineCountry;
					}
					
					System.out.println(searchLocationString);
					webEngine.executeScript("placeOfflineMarkers("+searchLocationString+")");
  	          	}
			}
	 




	private Timeline locationUpdateTimeline;

	@Override public void start(Stage stage) throws FileNotFoundException {
		// create web engine and view
		

		webEngine.load(getClass().getResource("map.html").toString());
		//final WebEngine webEngine = new WebEngine(getClass().getResource("googlemap.html").toString());
		//final WebView webView = new WebView(webEngine);
		
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
			public void changed(ObservableValue<? extends State> ov, State t, State t1) {
				if (t1 == Worker.State.SUCCEEDED) {
					  try{
						  
		    		   	  //String mapName = (String) webEngine.executeScript("map");
		    	          List<String> latArray = new ArrayList<String>();
		    	          List<String> longArray = new ArrayList<String>();
		    	          Connection initCon = DBConnection.getConnection();
		    	          Statement initStmt = initCon.createStatement();
		    	          String initQuery = "SELECT latitude, longitude  FROM visitorinformationtable ORDER BY SN DESC LIMIT 1000";
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
		    	             webEngine.executeScript("placeInitMarker("+lat1+","+lat2+")");
		    	             //System.out.println("document.placeInitMarker("+lat1+","+lat2+")");
		    	             //System.out.println(mapName);
		    	             
		    	          }
		    	          
		    	       }

		    	       catch(Exception e)
		    	       {
		    	          e.printStackTrace();
		    	          System.out.println("NOOOOOOOOOOO INTERNET");
		    	       }
					  
		                addFunctionHandlerToDocument(webEngine);

				}
			}
		});
		
		
		
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
							webEngine.executeScript("setMapTypeRoad()");
						} else if (satellite.isSelected()) {
							webEngine.executeScript("setMapTypeSatellite()");
						} else if (hybrid.isSelected()) {
							webEngine.executeScript("setMapTypeHybrid()");
						} else if (terrain.isSelected()) {
							webEngine.executeScript("document.setMapTypeTerrain()");
						}
					}
				});
		

		// add search
		final TextField searchBox = new TextField("");
		searchBox.setStyle("-fx-font-size: 1.8em; -fx-background-color:transparent");
		searchBox.setPrefColumnCount(14);
		searchBox.setPromptText("Search");
		searchBox.textProperty().addListener(new ChangeListener<String>() {
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
								webEngine.executeScript("goToLocation(\""+
										searchBox.getText()+"\")");
							}
						})
						);
				locationUpdateTimeline.play();
			}
		});

		//Label Location
		Label locationLabel = new Label("Location:");
		locationLabel.setStyle("-fx-font-size:1.8em;");
		// add Pin Box
		final TextField pinBox = new TextField("");
		pinBox.setStyle("-fx-font-size: 1.8em; ");
		pinBox.setPrefColumnCount(12);
		pinBox.setPromptText("ZIP Code");
		//

		//add pinIt button

		Button pinIt = new Button("Take me there !");
		pinIt.setId("pinItBtn");
		//pinIt.setStyle("-fx-font-size: 1.8em;-fx-background-color: linear-gradient(#ffd65b, #e68400),linear-gradient(#ffef84, #f2ba44),linear-gradient(#ffea6a, #efaa22),linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));-fx-background-radius: 30;-fx-background-insets: 0,1,2,3,0;-fx-text-fill: #654b00;-fx-font-weight: bold;-fx-font-size: 14px;-fx-padding: 10 20 10 20; ");
		pinIt.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				webEngine.executeScript("markZip(\"" + searchBox.getText() +"\")");
			}
		});

		Button zoomIn = new Button("Zoom In");
		zoomIn.setStyle("-fx-font-size: 1.8em; ");
		zoomIn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				webEngine.executeScript("zoomIn()");
			}
		});
		Button zoomOut = new Button("Zoom Out");
		zoomOut.setStyle("-fx-font-size: 1.8em; ");
		zoomOut.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				webEngine.executeScript("zoomOut()");
			}
		});
		
		//Geographical Location Button
		Button northAmButton = new Button("North America");
		northAmButton.setId("iphone");
		northAmButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				webEngine.executeScript("displayNorthAmMap()");
			}
		});
		
		Button southAmButton = new Button("South America");
		southAmButton.setId("iphone");
		southAmButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				webEngine.executeScript("displaySouthAmMap()");
			}
		});
		
		Button europeButton = new Button("Europe");
		europeButton.setId("iphone");
		europeButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				webEngine.executeScript("displayEurMap()");
			}
		});
		
		Button africaButton = new Button("Africa");
		africaButton.setId("iphone");
		africaButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				webEngine.executeScript("displayAfricaMap()");
			}
		});
		
		Button asiaButton = new Button("Asia");
		asiaButton.setId("iphone");
		asiaButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				webEngine.executeScript("displayAsiaMap()");
			}
		});
		
		Button ausButton = new Button("Australia");
		ausButton.setId("iphone");
		ausButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				webEngine.executeScript("displayAusMap()");
			}
		});
		
		Label headingLabel = new Label("                           Monroe-West Monroe Visitors Convention Bureau                           ");
		//headingLabel.setStyle("-fx-font-size: 1.8em;-fx-background-color: linear-gradient(#ffd65b, #e68400),linear-gradient(#ffef84, #f2ba44),linear-gradient(#ffea6a, #efaa22),linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));-fx-background-radius: 30;-fx-background-insets: 0,1,2,3,0;-fx-text-fill: #654b00;-fx-font-weight: bold;-fx-font-size: 14px;-fx-padding: 10 20 10 20; ");
		headingLabel.setFont(new Font("Arial", 24));
		
		Button vbLogoButton = new Button();
		Image vbLogoImg= new Image(getClass().getResourceAsStream("images/visitorsBureau_logo_small.png"));
		vbLogoButton.setGraphic(new ImageView(vbLogoImg));
		
		Button ulmLogoButton = new Button();
		
		Image ulmLogoImg= new Image(getClass().getResourceAsStream("images/ulm_logo_small.png"));
		ulmLogoButton.setGraphic(new ImageView(ulmLogoImg));
		// create toolbar
		//ToolBar toolBar = new ToolBar();
		//toolBar.setId("iphone-toolbar");
		//toolBar.getStyleClass().add("map-toolbar");
		//toolBar.getItems().addAll(ulmLogoButton, headingLabel,vbLogoButton);
				//road, satellite, hybrid, terrain,
				//createSpacer(),
				//new Label("ZIP Code:"), pinBox, pinIt,
				//createSpacer(),
				//new Label("Location:"),searchBox,pinIt, zoomIn, zoomOut);
		ToolBar bottomToolbar = new ToolBar();
		bottomToolbar.setId("iphone-toolbar");
		bottomToolbar.getItems().addAll(northAmButton,southAmButton,europeButton,africaButton,asiaButton,ausButton, pinIt);
		// create root
		BorderPane root = new BorderPane();
		root.getStyleClass().add("map");
		root.setCenter(webView);
		//root.setTop(toolBar);
		//root.setBottom(bottomToolbar);
		// create scene
		stage.setTitle("Web Map");
		Scene scene = new Scene(root,1000,700, Color.web("#666970"));
		stage.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("javastyles.css").toExternalForm());
		stage.setMaximized(true);
		//stage.setResizable(false);
		stage.setFullScreen(true);
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
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
	
	private void addFunctionHandlerToDocument(WebEngine engine) {
        JSObject window = (JSObject) engine.executeScript("window");
        window.setMember("app", this);
    }

	public static void main(String[] args){
		Application.launch(args);
	}
	
}