package br.convidas.front.event.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import br.convidas.classes.Evento;
import br.convidas.front.event.TelaModalEvent;
import br.convidas.front.event.handlers.mouse.ButtonDeleteMouseClickedEvent;
import br.convidas.front.event.handlers.mouse.ButtonEditMouseClickedEvent;
import br.convidas.manager.ManagerEvento;
import br.convidas.tools.log.LogTools;
import fx.tools.button.ButtonEventUtils;
import fx.tools.mouse.MouseEnventControler;
import fx.tools.table.FXTable;
import fx.tools.utils.FXUtilsControl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ControllerEvent implements Initializable{
	
	private static final Double HEIGT_PANE = 485D;
	private static String IMAGE_PENCIL	= "/front/images/manager/pencil.png";
	private static String ENGRENAGEM	= "/front/images/manager/engrenagem.png";
	private static String IMAGE_LIXEIRA	= "/front/images/manager/lixeira.png";
	private static String BORDER = "-fx-border-color: #ddd;";
	private static String GRAY_BACKGROUND = "-fx-background-color: #f5f5f5;";
	private static String BUTTON_DELETE_STYLE = "-fx-background-color: #d9534f; "
											  + "-fx-border-color: #d43f3a; "
											  + "-fx-background-radius: 3; "
											  + "-fx-border-radius:3;";
	private static String BUTTON_EDIT_STYLE = "-fx-background-color: #fff; "
											+ "-fx-border-color: #8c8c8c; "
											+ "-fx-background-radius: 3; "
											+ "-fx-border-radius:3;";
	
	@FXML private Pane paneList;
	@FXML private Pane paneListOut;
	@FXML private TextField textName;
	@FXML private DatePicker textDate;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private Stage stage;
	private List<Evento> listEvents;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		updateTableEvents(null);
	}
	
	public void clickButtonNewEvent(){
		try{
			TelaModalEvent tme = new TelaModalEvent();
			tme.setNewEvent(true);
//			tme.setControllerEvent(this);
			tme.start(new Stage());
		}catch (Exception e) {
			LogTools.logError(e);
		}
	}
	
	public void filterByName(){
		String name = textName.getText().toLowerCase();
		List<Evento> listEventoSelect = new ArrayList<>();
		for (Evento evento : listEvents) {
			String nameLower = evento.getName().toLowerCase();
			if(nameLower.contains(name)){
				listEventoSelect.add(evento);
			}
		}
		updateTableEvents(listEventoSelect);
	}
	
	public void filterByDate(){
		Date data = getDate(textDate);
		if(data != null){
			String dataSelect = sdf.format(data);
			List<Evento> listEventoSelect = new ArrayList<>();
			for (Evento evento : listEvents) {
				if(evento.getInitialDate() != null){
					String initialDate = sdf.format(evento.getInitialDate());
					if(dataSelect.equals(initialDate)){
						listEventoSelect.add(evento);
					}
				}
			}
			updateTableEvents(listEventoSelect);
		}else{
			updateTableEvents(null);
		}
	}
	
	private Date getDate(DatePicker datePicker){
		LocalDate ld = datePicker.getValue();
		if(ld == null){
			return null;
		}
		Calendar c =  Calendar.getInstance();
		c.set(ld.getYear(), ld.getMonthValue()-1, ld.getDayOfMonth());
		Date date = c.getTime();
		return date;
	}
	
	public void messageSucessEvent(String message){
		Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
		dialogoInfo.setTitle("Sucesso");
		dialogoInfo.setHeaderText(message);
		dialogoInfo.showAndWait();
		updateTableEvents(null);
	}
	
	public void updateTableEvents(List<Evento> listEvents){
		if(listEvents == null){
			listEvents = ManagerEvento.getEventos();
			this.listEvents = listEvents;
		}
		paneList.getChildren().clear();
		if(listEvents.isEmpty()){
			return;
		}
		FXTable table = new FXTable();
		table.setPaneTable(paneList);
		table.setSizeRowns(36.0);
		table.setLayoutXInit(22.0);
		table.setLayoutYInit(24.0);
		table.setStyle(BORDER);
		table.setSizeFont(14.0);
		table.setFont("SansSerif Bold");
		Image image = new Image(ENGRENAGEM);
		ImageView img = new ImageView();
		img.setImage(image);
		Double[] sizeColumns = {200.0, 400.0, 115.0, 115.0, 210.0, 70.0};
		Object[] header = 	{"Nome", "Local", "Data Inicial", "Data Final", "Cidade",  img};
		table.setSizeColumns(sizeColumns);
		table.addRown(header, null);
		table.setFont("SansSerif");
		table.setStyle(BORDER+GRAY_BACKGROUND);
		for (Evento evento : listEvents) {
			Object[] line = populateRownEvent(evento);
			table.addRown(line, null);
		}
		paneListOut.setPrefHeight(HEIGT_PANE);
		if(paneListOut.getPrefHeight() < paneList.getPrefHeight()+100){
			paneListOut.setPrefHeight(paneList.getPrefHeight()+100);
		}
	}
	
	public Object[] populateRownEvent(Evento evento){
		if(evento == null){
			return null;
		}
		Object[] array = new Object[6];
		array[0] = evento.getName();
		array[1] = evento.getLocale();
	
		String initialDate =  "";
		if(evento.getInitialDate() != null){
			initialDate = sdf.format(evento.getInitialDate());
		}
		String finalDate =  "";
		if(evento.getFinalDate() != null){
			finalDate = sdf.format(evento.getFinalDate());
		}
		
		array[2] = initialDate;
		array[3] = finalDate;
		
		
		String cidade = "";
		if(evento.getCity() != null){
			cidade = evento.getCity().getName();
		}
		array[4] = cidade;
		
		
		Image image = new Image(IMAGE_PENCIL);
		ImageView img = new ImageView();
		img.setImage(image);
		Button edit= new Button(null, img);
		edit.setLayoutX(10.0);
		edit.setLayoutY(6.0);
		edit.setPrefHeight(23.0);
		edit.setMinHeight(23.0);
		edit.setPrefWidth(23.0);
		edit.setMinWidth(23.0);
		edit.setStyle(BUTTON_EDIT_STYLE);
		ButtonEditMouseClickedEvent bec = new ButtonEditMouseClickedEvent();
		bec.setEvent(evento);
		bec.setControllerEvent(this);
		edit.setOnMouseClicked(MouseEnventControler.getMouseCliked(bec));
		ButtonEventUtils.setEvents(edit);
		
		image = new Image(IMAGE_LIXEIRA);
		img = new ImageView();
		img.setImage(image);
		Button delete= new Button(null, img);
		delete.setLayoutX(40.0);
		delete.setLayoutY(6.0);
		delete.setMinHeight(22.0);
		delete.setPrefHeight(22.0);
		delete.setMinWidth(23.0);
		delete.setPrefWidth(23.0);
		delete.setStyle(BUTTON_DELETE_STYLE);
		ButtonDeleteMouseClickedEvent bdc = new ButtonDeleteMouseClickedEvent();
		bdc.setEvent(evento);
		bdc.setControllerEvent(this);
		delete.setOnMouseClicked(MouseEnventControler.getMouseCliked(bdc));
		ButtonEventUtils.setEvents(delete);
		
		Button[] buttons = {delete, edit};
		array[5] = buttons;
		return array;
	}
	
	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
		FXUtilsControl.setScene(stage.getScene());
	}
	
}
