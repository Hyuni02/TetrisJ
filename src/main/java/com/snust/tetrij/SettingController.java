package com.snust.tetrij;
import com.snust.tetrij.GameManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SettingController extends GameManager {
    private Stage stage;
    private Scene scene;
    @FXML
    private ComboBox<String> resolutionComboBox;        //이건 어디에 쓰는거조??
    @FXML
    private ComboBox<String> sizeComboBox;
    @FXML
    private CheckBox colorBlindModeCheckBox;
    private String screenSize;
    private boolean isColorBlind;

    public void initialize() {
        loadSettings();  //json파일 읽어옴
        colorBlindModeCheckBox.setSelected(isColorBlind);
        sizeComboBox.getSelectionModel().select(screenSize);
    }

    // 해상도 변경 메서드
    public static void setResolution(int width, int height) {
        resolutionX = width;
        resolutionY = height;
    }
    @FXML
    private void handleSize() {
        screenSize = sizeComboBox.getSelectionModel().getSelectedItem();
    }

    public void colorBlindMode(){
        isColorBlind = colorBlindModeCheckBox.isSelected();
    }

    private void saveSettingsToFile() {   //json 파일로 저장
        File file = new File("setting.json");

        try {
            String content = new String(Files.readAllBytes(Paths.get(file.toURI())), "UTF-8");
            JSONObject currentSettings = new JSONObject(content);
            currentSettings.put("screenSize", screenSize);
            currentSettings.put("isColorBlind", isColorBlind);

            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(currentSettings.toString());
                fileWriter.flush();
                loadSettings();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchToStartMenu(ActionEvent event) throws IOException {
        saveSettingsToFile();
        Parent root = returnSceneRoot("start_menu.fxml");
        stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleKeySetting(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("keysetting.fxml"));
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSettings() {    //셋팅 파일 읽어옴
        try {
            File file = new File("setting.json");
            FileReader fileReader = new FileReader(file);
            StringBuilder stringBuilder = new StringBuilder();
            int i;
            while ((i = fileReader.read()) != -1) {
                stringBuilder.append((char) i);
            }
            fileReader.close();

            JSONObject setting = new JSONObject(stringBuilder.toString());
            screenSize = setting.getString("screenSize");
            isColorBlind = setting.getBoolean("isColorBlind");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}