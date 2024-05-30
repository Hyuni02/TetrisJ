package com.snust.tetrij.GameScene.GameSceneSingle;

import com.snust.tetrij.MultiTetris;
import com.snust.tetrij.tetromino.TetrominoBase;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.snust.tetrij.GameScene.GameSceneMulti.MultiBoardController.boardController;
import static com.snust.tetrij.GameScene.GameSceneMulti.MultiTetrisController.controller;
import static com.snust.tetrij.GameScene.GameSceneMulti.MultiTetrisModel.model;
import static com.snust.tetrij.GameScene.GameSceneMulti.MultiTetrisView.view;
import static com.snust.tetrij.GameScene.GameSceneSingle.SingleBoardController.boardController_s;
import static com.snust.tetrij.GameScene.GameSceneSingle.SingleTetrisController.controller_s;
import static com.snust.tetrij.GameScene.GameSceneSingle.SingleTetrisModel.model_s;
import static com.snust.tetrij.GameScene.GameSceneSingle.SingleTetrisView.view_s;

public class SingleKeyController {

    public final static SingleKeyController keyController_s = new SingleKeyController();
    public static String rightKey;
    public static String leftKey;
    public static String rotateKey;
    public static String downKey;
    public static String dropKey;
    public static KeyCode rightKeyCode;
    public static KeyCode leftKeyCode;
    public static KeyCode rotateKeyCode;
    public static KeyCode downKeyCode;
    public static KeyCode dropKeyCode;

    private SingleKeyController(){
    }

    public static void addListenerPause(Scene scene) {

    }

    public void reloadKeySetting(){
        rightKey = loadKeySetting("right");
        leftKey = loadKeySetting("left");
        rotateKey = loadKeySetting("rotate");
        downKey = loadKeySetting("down");
        dropKey = loadKeySetting("drop");
        rightKeyCode = getKeyCodeFromString(rightKey);
        leftKeyCode = getKeyCodeFromString(leftKey);
        rotateKeyCode = getKeyCodeFromString(rotateKey);
        downKeyCode = getKeyCodeFromString(downKey);
        dropKeyCode = getKeyCodeFromString(dropKey);
    }

    /**
     * key event 처리
     * @param scene
     */
    public static void addListenerGameControl(Scene scene) {
        scene.setOnKeyPressed(e->{
            javafx.scene.input.KeyCode code = e.getCode();
            if (model_s.bag.isEmpty())
                code = KeyCode.NONCONVERT;

            if(code == KeyCode.NONCONVERT);
            else if(code == KeyCode.ESCAPE){
                System.out.println("esc");
                controller_s.isPaused = !controller_s.isPaused;
                Platform.exit();
                System.exit(0);
            }
            else if(code == leftKeyCode){
                boardController_s.moveLeftOnKeyPress(model_s.bag.get(0));
                view_s.color_mesh();
            }
            else if(code == rightKeyCode){
                boardController_s.moveRightOnKeyPress(model_s.bag.get(0));
                view_s.color_mesh();
            }
            else if(code == rotateKeyCode){
                boardController_s.rotateClockWise(model_s.bag.get(0));
                view_s.color_mesh();
            }
            else if(code == downKeyCode){
                boardController_s.softDrop(model_s.bag.get(0));
                view_s.color_mesh();
            }
            else if(code == dropKeyCode){
                boardController_s.hardDrop(model_s.bag.get(0));
                view_s.color_mesh();
            }
            else if (code == KeyCode.P) {
                controller_s.togglePause();
            }
        });
    }

    public static KeyCode getKeyCodeFromString(String keyName) {    //json -> KeyCode로 변경
        for (KeyCode kc : KeyCode.values()) {
            if (kc.getName().equalsIgnoreCase(keyName)) {
                return kc;
            }
        }
        return null;
    }

    private static String loadKeySetting(String key) {
        try {
            String content = new String(Files.readAllBytes(Paths.get("src/main/resources/com/snust/tetrij/keysetting.json")), "UTF-8");
            JSONObject settings = new JSONObject(content);
            return settings.getString(key);
        } catch (Exception e) {
//            e.printStackTrace();
            return loadKeySetting_build(key);
        }
    }

    private static String loadKeySetting_build(String key) {
        try {
            // 클래스 로더를 사용하여 리소스 파일 읽기
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/snust/tetrij/keysetting.json");
            if (inputStream == null) {
                System.err.println("설정 파일을 찾을 수 없습니다.");
                return null;
            }

            // 입력 스트림을 문자열로 변환
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }

            // JSON 객체 생성
            JSONObject settings = new JSONObject(stringBuilder.toString());
            return settings.getString(key);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void gameProc(Scene scene) {
        scene.setOnKeyPressed(e->{
            javafx.scene.input.KeyCode code = e.getCode();
            if (model_s.bag.isEmpty())
                code = KeyCode.NONCONVERT;

            if(code == KeyCode.NONCONVERT);
            else if(code == KeyCode.ESCAPE){
                System.out.println("esc");
                controller_s.isPaused = true;
                Platform.exit();
                System.exit(0);
            }
            else if (code == KeyCode.P){
                controller_s.isPaused = !controller_s.isPaused;
                controller_s.togglePause();
            }
            else if(code == leftKeyCode){
                boardController_s.moveLeftOnKeyPress((TetrominoBase)model_s.bag.get(0));
                view_s.color_mesh();
            }
            else if(code == rightKeyCode){
                boardController_s.moveRightOnKeyPress((TetrominoBase)model_s.bag.get(0));
                view_s.color_mesh();
            }
            else if(code == rotateKeyCode){
                boardController_s.rotateClockWise((TetrominoBase)model_s.bag.get(0));
                view_s.color_mesh();
            }
            else if(code == downKeyCode){
                boardController_s.softDrop((TetrominoBase)model_s.bag.get(0));
                view_s.color_mesh();
            }
            else if(code == dropKeyCode){
                boardController_s.hardDrop((TetrominoBase)model_s.bag.get(0));
                view_s.color_mesh();
            }
        });
    }
}
