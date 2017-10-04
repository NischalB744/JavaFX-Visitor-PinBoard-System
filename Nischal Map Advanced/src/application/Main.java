package application;


import java.sql.Connection;
import java.sql.Statement;

import org.w3c.dom.Document;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import netscape.javascript.JSObject;

public class Main extends Application {

	
	public class Bridge{

		public void storeInfo(String st, String lat, String longt, String zip )
		{
			if(st.equals("dummy") && lat.equals("dummy") && longt.equals("dummy") && zip.equals("dummy"))
			{
				System.out.println("Starting DB");
				String State = st; 
				String Latitude = lat;
				String Longitude = longt;
				String ZipCode = zip;
				String query = "INSERT INTO entrytest VALUES ('"+State+"','"+Latitude+"','"+Longitude+"' ,'"+ZipCode+"')";
				String query2 = "DELETE FROM entrytest WHERE State = 'dummy'";
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
				String State = st; 
				String Latitude = lat;
				String Longitude = longt;
				String ZipCode = zip;
				String query = "INSERT INTO entrytest VALUES ('"+State+"','"+Latitude+"','"+Longitude+"' ,'"+ZipCode+"')";
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
	//======================================================================================
	private Pane splashLayout;
	  private ProgressBar loadProgress;
	  private Label progressText;
	  private WebView webView;
	  private Stage mainStage = new Stage();
	  private static final int SPLASH_WIDTH = (int) Screen.getPrimary().getVisualBounds().getWidth();
	  private static final int SPLASH_HEIGHT = (int) Screen.getPrimary().getVisualBounds().getHeight();
	  private Timeline locationUpdateTimeline;
	  public static void main(String[] args) throws Exception { launch(args); }

	  @Override public void init() {
		  
	    ImageView splash = new ImageView(new Image("file:img/earthLoader.gif"));
	    splash.setFitWidth(Screen.getPrimary().getBounds().getWidth());
	    splash.setFitHeight(Screen.getPrimary().getBounds().getHeight());
	    loadProgress = new ProgressBar();
	    loadProgress.setPrefWidth(SPLASH_WIDTH - 20);
	    progressText = new Label("Loading hobbits with pie . . .");
	    splashLayout = new VBox();
	    splashLayout.getChildren().addAll(splash, loadProgress, progressText);
	    progressText.setAlignment(Pos.CENTER);
	    splashLayout.setStyle("-fx-padding: 5; -fx-background-color: cornsilk; -fx-border-width:5; -fx-border-color: linear-gradient(to bottom, chocolate, derive(chocolate, 50%));");
	    splashLayout.setEffect(new DropShadow());
	  }
	  
	  @Override public void start(final Stage initStage) throws Exception {
	    showSplash(initStage);
	    showMainStage(mainStage);
	   

	    webView.getEngine().documentProperty().addListener(new ChangeListener<Document>() {
	      @Override public void changed(ObservableValue<? extends Document> observableValue, Document document, Document document1) {
	        if (initStage.isShowing()) {
	          loadProgress.progressProperty().unbind();
	          loadProgress.setProgress(1);
	          progressText.setText("All hobbits are full.");
	          //mainStage.setIconified(false);
	          initStage.toFront();
	          FadeTransition fadeSplash = new FadeTransition(Duration.seconds(4.0), splashLayout);
	          fadeSplash.setFromValue(1.0);
	          fadeSplash.setToValue(0.0);
	          fadeSplash.setOnFinished(new EventHandler<ActionEvent>() {
	            @Override public void handle(ActionEvent actionEvent) {
	              initStage.hide();
	            }
	          });
	          fadeSplash.play();
	        }
	      }
	    });
	  }

	  private void showMainStage(final Stage stage ) {
		  webView = new WebView();
			final WebEngine webEngine = webView.getEngine();

			webEngine.load(getClass().getResource("map.html").toString());
			//final WebEngine webEngine = new WebEngine(getClass().getResource("googlemap.html").toString());
			//final WebView webView = new WebView(webEngine);

			webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
				@Override
				public void changed(ObservableValue<? extends State> ov, State t, State t1) {
					if (t1 == Worker.State.SUCCEEDED) {
						JSObject window = (JSObject) webEngine.executeScript("window");
						window.setMember("app", new Bridge());
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


			// add search
			final TextField searchBox = new TextField("");
			searchBox.setPrefColumnCount(12);
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
									webEngine.executeScript("document.goToLocation(\""+
											searchBox.getText()+"\")");
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
			pinIt.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent actionEvent) {
					webEngine.executeScript("document.markZip(\"" + searchBox.getText() +"\")");
				}
			});

			Button zoomIn = new Button("Zoom In");
			zoomIn.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent actionEvent) {
					webEngine.executeScript("document.zoomIn()");
				}
			});
			Button zoomOut = new Button("Zoom Out");
			zoomOut.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent actionEvent) {
					webEngine.executeScript("document.zoomOut()");
				}
			});
			// create toolbar
			ToolBar toolBar = new ToolBar();
			toolBar.getStyleClass().add("map-toolbar");
			toolBar.getItems().addAll(
					road, satellite, hybrid, terrain,
					createSpacer(),
					google,yahoo,bing,
					createSpacer(),
					//new Label("ZIP Code:"), pinBox, pinIt,
					//createSpacer(),
					new Label("Location:"),searchBox,pinIt, zoomIn, zoomOut);
			// create root
			BorderPane root = new BorderPane();
			root.getStyleClass().add("map");
			root.setCenter(webView);
			root.setTop(toolBar);
			// create scene
			//stage.setIconified(true);
			stage.setTitle("Web Map");
			Scene scene = new Scene(root,1000,700, Color.web("#666970"));
			stage.setScene(scene);
			//scene.getStylesheets().add("/application.css");
			stage.setMaximized(true);
			stage.show();
		}

	  private void showSplash(Stage initStage) {
		splashLayout.setStyle("fx-backgroundColor: transparent");
	    Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);
	    initStage.initStyle(StageStyle.TRANSPARENT);
	    final Rectangle2D bounds = Screen.getPrimary().getBounds();
	    initStage.setScene(splashScene);
	    initStage.setX(SPLASH_WIDTH);
	    initStage.setY(SPLASH_HEIGHT);
	    initStage.setMaximized(true);
	    initStage.show();
	  }
	  
	  private Node createSpacer() {
			Region spacer = new Region();
			HBox.setHgrow(spacer, Priority.ALWAYS);
			return spacer;
		}

		static { // use system proxy settings when standalone application
			System.setProperty("java.net.useSystemProxies", "true");
		}

	}
	
	
	//======================================================================================
	