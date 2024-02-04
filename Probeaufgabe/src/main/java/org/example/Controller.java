package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    TableView<Person> table;
    @FXML
    private  TableColumn<Person, String> table_Id;
    @FXML
    private TableColumn<Person, String> table_Name;
    @FXML
    private TableColumn<Person, String> table_Job;
    @FXML
    private TableColumn<Person, String> table_Created;
    @FXML
    private TableColumn<Person, String> table_Edited;
    @FXML
    private TableColumn<Person, String> table_Nr;
    @FXML
    private TableColumn<Person, String> table_Email;

    private final String storagePath = "src/main/resources/storage.json";
    private ArrayList<Person> temp = readJSON(storagePath);
    private ObservableList<Person> data = FXCollections.observableArrayList(temp);



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        table_Id.setCellValueFactory(new PropertyValueFactory<>("id"));
        table_Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        table_Job.setCellValueFactory(new PropertyValueFactory<>("job"));
        table_Created.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        table_Edited.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
        table_Nr.setCellValueFactory(new PropertyValueFactory<>("phonenumber"));
        table_Email.setCellValueFactory(new PropertyValueFactory<>("email"));
        table.setItems(data);
        table.getSortOrder().add(table_Id);

        table.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2 && table.getSelectionModel().getSelectedItem() != null) {
                showUser();
            }
        });
    }

    public ArrayList<Person> readCSV(String path)
    {
        List<String[]> data;
        ArrayList<Person> user = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(path)))
        {
            data = reader.readAll();

            for (int i = 1; i < data.size(); i++)
            {
                String temp = data.get(i)[0];
                String[] temp2 = temp.split(";");
                Person person = new Person(temp2[0], temp2[1], temp2[2], temp2[3], temp2[4], temp2[5], temp2[6]);
                user.add(person);
            }

        } catch (Exception e)
        {
                System.out.println(e);
        }

        return user;
    }
    public ArrayList<Person> readJSON(String path)
    {
        List<String[]> data;
        ArrayList<Person> persons = new ArrayList<>();
        JSONParser parser =new JSONParser();

        try
        {
            Object obj = parser.parse(new FileReader(path));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jsonArray = (JSONArray) jsonObject.get("persons");

            for(int i=0; i<jsonArray.size(); i++)
            {
                JSONObject temp_person = (JSONObject) jsonArray.get(i);
                String id = (String) temp_person.get("id");
                String name = (String) temp_person.get("name");
                String job = (String) temp_person.get("job");
                String createdAt = (String) temp_person.get("createdAt");
                String updatedAt = (String) temp_person.get("updatedAt");
                String phone ;
                String email ;

                if(temp_person.get("contactdetails") == null)
                {
                    phone = (String) temp_person.get("phonenumber");
                    email = (String) temp_person.get("email");

                }
                else
                {
                    JSONObject temp_contact = (JSONObject) temp_person.get("contactdetails");
                    phone = (String) temp_contact.get("phone");
                    email = (String) temp_contact.get("email");
                }

                persons.add(new Person(id, name, job, createdAt, updatedAt, phone, email));
            }
        }
        catch(Exception e)
        {System.out.println(e);}
        return persons;
    }
    public ArrayList<Person> readXLSX(String path)
    {
        ArrayList<Person> persons = new ArrayList<Person>();
        try
        {
            FileInputStream file = new FileInputStream(path);
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++)
            {
                Row row = sheet.getRow(i);
                String id = dataFormatter.formatCellValue(row.getCell(0));
                String name = dataFormatter.formatCellValue(row.getCell(1));
                String job = dataFormatter.formatCellValue(row.getCell(2));
                String createdAt = dataFormatter.formatCellValue(row.getCell(3));
                String updatedAt = dataFormatter.formatCellValue(row.getCell(4));
                String phone = dataFormatter.formatCellValue(row.getCell(5));
                String email = dataFormatter.formatCellValue(row.getCell(6));
                persons.add(new Person(id, name, job, createdAt, updatedAt, phone, email));
            }
        }catch (Exception e){System.out.println(e);}
        return persons;
    }

    private static void writePersonToJson(ObservableList<Person> persons, String filePath)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            PersonsContainer personsContainer = new PersonsContainer(persons);
            objectMapper.writeValue(new File(filePath), personsContainer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buttonCreateUser()
    {
        Dialog dialog = new Dialog();
        dialog.setTitle("Neue Person erstellen");
        dialog.setHeaderText("Bitte Daten eingeben:");

        TextField id = new TextField();
        id.setPromptText("ID");
        TextField name = new TextField();
        name.setPromptText("Name");
        TextField job = new TextField();
        job.setPromptText("Job");
        TextField nr = new TextField();
        nr.setPromptText("Telefonnummer");
        TextField email = new TextField();
        email.setPromptText("Email");

        GridPane grid = new GridPane();
        grid.add(new Label("ID:"), 1, 1);
        grid.add(id, 2, 1);
        grid.add(new Label("Name:"), 1, 2);
        grid.add(name, 2, 2);
        grid.add(new Label("Job:"), 1, 3);
        grid.add(job, 2, 3);
        grid.add(new Label("Telefonnummer:"), 1, 4);
        grid.add(nr, 2, 4);
        grid.add(new Label("Email:"), 1, 5);
        grid.add(email, 2, 5);

        dialog.getDialogPane().setContent(grid);
        ButtonType buttonTypeOk = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.showAndWait();

        if (dialog.getResult() == buttonTypeOk)
        {

            if (name.getText().isEmpty() || job.getText().isEmpty()||nr.getText().isEmpty()|| email.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Please fill out all fields");
                alert.showAndWait();
                return;
            }

            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter date_formater = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            String formated_date = date.format(date_formater);


            Person person = new Person(id.getText(), name.getText(), job.getText(), formated_date, formated_date, nr.getText(), email.getText());
            data.add(person);
            table.setItems(data);
            table.sort();
            writePersonToJson(data, storagePath);
        }
        if(dialog.getResult()==ButtonType.CANCEL)
        {
            dialog.close();
        }

    }
    public void buttonEditUser()
    {
        Person update = table.getSelectionModel().getSelectedItem();

        if(update==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehlermeldung");
            alert.setHeaderText("Fehler");
            alert.setContentText("Bitte wählen Sie vorher eine Person aus!");
            alert.showAndWait();
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setTitle("Update/Delete");
        dialog.setHeaderText("Nutzer bearbeiten oder löschen?");
        dialog.setContentText("Möchten Sie die Person bearbeiten oder löschen?");
        ButtonType buttonTypeUpdate = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeDelete = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeUpdate, buttonTypeDelete, buttonTypeCancel);
        dialog.showAndWait();

        if(dialog.getResult()==buttonTypeUpdate)
        {
            dialog.close();
            editUser(update);
            }

        if(dialog.getResult()==buttonTypeDelete)
        {
            dialog.close();
            deleteUser(update);
            }
    }
    public void buttonReadFile()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(null);
        String file_path = file.getPath();

        if(file_path==null)
        {
            return;
        }

        ArrayList<Person> temp = new ArrayList<>();

        switch (file_path.substring(file_path.lastIndexOf(".")+1)){
            case "csv":
                temp = readCSV(file_path);
                break;
            case "json":
                 temp =readJSON(file_path);
                break;
            case "xlsx":
                temp = readXLSX(file_path);
                break;
            default:
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Please select a valid file");
                alert.showAndWait();
                return;

        }

        for (Person person: temp)
        {
            person.getCreatedAt();
            data.add(person);
            writePersonToJson(data, "src/main/resources/storage.json");
        }
        table.setItems(data);
        table.sort();
    }

    public void showUser()
    {
      Person person = table.getSelectionModel().getSelectedItem();

        if(person==null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please select a Person");
            alert.showAndWait();
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setTitle("Personal Information");
        dialog.setHeaderText("Personal Information");

        TextField id = new TextField();
        id.setText(person.getId());
        id.setEditable(false);

        TextField name = new TextField();
        name.setText(person.getName());
        name.setEditable(false);

        TextField job = new TextField();
        job.setText(person.getJob());
        job.setEditable(false);

        TextField nr = new TextField();
        nr.setText(person.getPhonenumber());
        nr.setEditable(false);

        TextField email = new TextField();
        email.setText(person.getEmail());
        email.setEditable(false);

        TextField createdAt = new TextField();
        createdAt.setText(person.getCreatedAt());
        createdAt.setDisable(false);


        TextField updatedAt = new TextField();
        updatedAt.setText(person.getUpdatedAt());
        updatedAt.setDisable(false);

        GridPane grid = new GridPane();
        grid.add(new Label("ID:"), 1, 1);
        grid.add(id, 2, 1);
        grid.add(new Label("Name:"), 1, 2);
        grid.add(name, 2, 2);
        grid.add(new Label("Job:"), 1, 3);
        grid.add(job, 2, 3);
        grid.add(new Label("Telefonnummer:"), 1, 4);
        grid.add(nr, 2, 4);
        grid.add(new Label("Email:"), 1, 5);
        grid.add(email, 2, 5);
        grid.add(new Label("Created At:"), 1, 6);
        grid.add(createdAt, 2, 6);
        grid.add(new Label("Updated At:"), 1, 7);
        grid.add(updatedAt, 2, 7);

        dialog.getDialogPane().setContent(grid);
        ButtonType butonTypeBearbeiten = new ButtonType("Bearbeiten", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeLoeschen = new ButtonType("Loeschen", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Zurueck", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(butonTypeBearbeiten, buttonTypeLoeschen, buttonTypeCancel);
        dialog.showAndWait();

        if(dialog.getResult()==butonTypeBearbeiten)
        {editUser(person);}

        if(dialog.getResult()==buttonTypeLoeschen)
        {deleteUser(person);}

        if(dialog.getResult()==ButtonType.CLOSE)
        {dialog.close();}
    }
    public void editUser(Person user)
    {
        Dialog dialog2 = new Dialog();
        dialog2.setTitle("Bearbeiten");
        dialog2.setHeaderText("Nutzer bearbeiten");

        TextField id = new TextField();
        id.setText(user.getId());
        id.setDisable(true);

        TextField name = new TextField();
        name.setText(user.getName());

        TextField job = new TextField();
        job.setText(user.getJob());

        TextField nr = new TextField();
        nr.setText(user.getPhonenumber());

        TextField email = new TextField();
        email.setText(user.getEmail());

        TextField createdAt = new TextField();
        createdAt.setText(user.getCreatedAt());
        createdAt.setDisable(true);

        TextField updatedAt = new TextField();
        updatedAt.setText(user.getUpdatedAt());
        updatedAt.setDisable(true);


        GridPane grid = new GridPane();
        grid.add(new Label("ID:"), 1, 1);
        grid.add(id, 2, 1);
        grid.add(new Label("Name:"), 1, 2);
        grid.add(name, 2, 2);
        grid.add(new Label("Job:"), 1, 3);
        grid.add(job, 2, 3);
        grid.add(new Label("Telefonnummer:"), 1, 4);
        grid.add(nr, 2, 4);
        grid.add(new Label("Email:"), 1, 5);
        grid.add(email, 2, 5);
        grid.add(new Label("Erstellt am:"), 1, 6);
        grid.add(createdAt, 2, 6);
        grid.add(new Label("Bearbeitet am:"), 1, 7);
        grid.add(updatedAt, 2, 7);


        dialog2.getDialogPane().setContent(grid);
        ButtonType buttonTypeEdit = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog2.getDialogPane().getButtonTypes().add(buttonTypeEdit);
        dialog2.showAndWait();

        Person updated_person = new Person(id.getText(), name.getText(), job.getText(), user.getCreatedAt(), getTimeRightNow(), nr.getText(), email.getText());
        if(updated_person.getName().equals(user.getName()) && updated_person.getJob().equals(user.getJob()) && updated_person.getPhonenumber().equals(user.getPhonenumber()) && updated_person.getEmail().equals(user.getEmail()))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Fehlermeldung:");
            alert.setContentText("Keine Änderungen vorgenommen!");
            alert.showAndWait();
            return;
        }
        data.remove(user);
        data.add(updated_person);
        table.setItems(data);
        writePersonToJson(data, storagePath);
        table.sort();
    }
    public void deleteUser(Person user)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Löschen");
        alert.setHeaderText("Löschen");
        alert.setContentText("Sind Sie sicher, dass Sie den Nutzer unwiderruflich löschen möchten?");
        alert.showAndWait();

        if(alert.getResult()==ButtonType.OK)
        {
            data.remove(user);
            table.setItems(data);
            writePersonToJson(data, storagePath);
            table.sort();
        }
    }
    public String getTimeRightNow()
    {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter date_formater = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formated_date = date.format(date_formater);
        return formated_date;
    }

};