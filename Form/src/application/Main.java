package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		GridPane info = new GridPane();
		info.setMaxSize(500, 500);
		info.setPadding(new Insets(10, 10, 10, 10));
		info.setAlignment(Pos.TOP_LEFT);
		info.setHgap(15);
		info.setVgap(15);		
		info.setGridLinesVisible(false);
		
		Scene scene = new Scene(info, 800, 500);
		
		Label fnameL = new Label("First Name");
		info.add(fnameL, 1, 1);
		TextField fname = new TextField();
		info.add(fname, 2, 1);
		
		Label lnameL = new Label("Last Name");
		info.add(lnameL, 1, 2);
		TextField lname = new TextField();
		info.add(lname, 2, 2);
		
		Label emailL = new Label("E-mail");
		info.add(emailL, 1, 3);
		TextField email = new TextField();
		info.add(email, 2, 3);
		
		Label cityL = new Label("City");
		info.add(cityL, 1, 4);
		TextField city = new TextField();
		info.add(city, 2, 4);
		
		Label stateL = new Label("State");
		info.add(stateL, 3, 4 );
		TextField state = new TextField();
		info.add(state, 4, 4);
		
		Label zipL = new Label("Zip-Code");
		info.add(zipL, 5, 4 );
		TextField zip = new TextField();
		info.add(zip, 6, 4);
		
		Label countryL = new Label("Country");
		info.add(countryL, 1, 5 );
		TextField country = new TextField();
		info.add(country, 2, 5);
		
		Label totalVisitor = new Label("Number of Visitors");
		info.add(totalVisitor, 1, 6);
		ChoiceBox<String> number = new ChoiceBox<String>();
		number.getItems().addAll("1", "2", "3", "4", "5", "more than 5");
		info.add(number, 2, 6);
		
		Label povL = new Label("Purpose of Visit");
	    info.add(povL, 1, 7);
	    ToggleGroup groupVisit = new ToggleGroup(); 
	    RadioButton businessRadio = new RadioButton("Business"); 
	    businessRadio.setToggleGroup(groupVisit); 
	    RadioButton vacationRadio = new RadioButton("Vacation"); 
	    vacationRadio.setToggleGroup(groupVisit);
	    RadioButton otherRadio = new RadioButton("Other"); 
	    otherRadio.setToggleGroup(groupVisit);
	    info.add(businessRadio, 2, 7);
	    info.add(vacationRadio, 4, 7);
	    info.add(otherRadio, 6, 7);
	    
	    Label posL = new Label("Purpose of Stay");
	    info.add(posL, 1, 8);
	    ToggleGroup groupStay = new ToggleGroup(); 
	    RadioButton hotelRadio = new RadioButton("Hotel"); 
	    hotelRadio.setToggleGroup(groupStay); 
	    RadioButton motelRadio = new RadioButton("Motel"); 
	    motelRadio.setToggleGroup(groupStay);
	    RadioButton othersRadio = new RadioButton("Other"); 
	    otherRadio.setToggleGroup(groupStay);
	    info.add(hotelRadio, 2, 8);
	    info.add(motelRadio, 4, 8);
	    info.add(othersRadio, 6, 8);
		
	    Button submit = new Button("Submit");
		info.add(submit, 3, 12);
		
		primaryStage.setTitle("Information Form");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	public static void main(String[] args){
		launch(args);
		}

}
				