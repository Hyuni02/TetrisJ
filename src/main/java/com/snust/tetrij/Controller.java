package com.snust.tetrij;

import com.snust.tetrij.tetromino.I;
import com.snust.tetrij.tetromino.J;
import com.snust.tetrij.tetromino.L;
import com.snust.tetrij.tetromino.O;
import com.snust.tetrij.tetromino.S;
import com.snust.tetrij.tetromino.T;
import com.snust.tetrij.tetromino.Z;
import com.snust.tetrij.tetromino.TetrominoBase;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.*;

public class Controller {
    public static List<TetrominoBase> bag = new Vector<TetrominoBase>();

    public static ArrayList<Integer> field = new ArrayList<Integer>();

    public Controller() { }

    public static void SetField(Tetris.difficulty dif){
        //모든 블록의 비중을 10으로 세팅
        for(int i=0;i<7;i++){
            for(int j=0;j<10;j++){
                field.add(i);
            }
        }

        //쉬움 난이도는 I블럭의 비중 20% 증가
        if(dif == Tetris.difficulty.EASY){
            field.add(1);
            field.add(1);
        }
        //어려움 난이도는 I블럭의 비중 20% 감소
        if(dif == Tetris.difficulty.HARD){
            field.remove(1);
            field.remove(1);
        }
    }

    public static void generateTetromino() {
        TetrominoBase t = new TetrominoBase();
        int idx = (int)(Math.random() * field.size());
        switch(field.get(idx)) {
            case 0 -> t = new Z();
            case 1 -> t = new I();
            case 2 -> t = new J();
            case 3 -> t = new L();
            case 4 -> t = new O();
            case 5 -> t = new S();
            case 6 -> t = new T();
        }

        if (!canMoveDown(t, 0)) {
            Tetris.isPaused = true;
            return;
        }
        bag.add(t);
        t.update_mesh();
    }


    public static void softDrop(TetrominoBase tb) {
        int rot = tb.rotate;
        int height = tb.getHeight();
        int width = tb.getWidth();

        eraseMesh(tb);
        tb.pos[0]++;
        if (!canMoveDown(tb, 1)) {
            updateTop(tb);
            Controller.bag.remove(0);
        }
        else {
            tb.pos[0]++;
        }
        tb.update_mesh();
        eraseLine();
    }

    public static void hardDrop(TetrominoBase tb) {
        eraseMesh(tb);
        int dropHeight = 0;
        while (canMoveDown(tb, dropHeight + 1)) {
            dropHeight++;
        }

        tb.pos[0] += dropHeight;
        tb.update_mesh();
        updateTop(tb);
        eraseLine();
        Controller.bag.remove(0);
    }

    public static void moveRightOnKeyPress(TetrominoBase tb) {
        eraseMesh(tb);
        if (canMoveSideWays(tb, 1)) {
            tb.pos[1]++;
        }
        tb.update_mesh();
    }

    public static void moveLeftOnKeyPress(TetrominoBase tb) {
        eraseMesh(tb);
        if (canMoveSideWays(tb, -1)) {
            tb.pos[1]--;
        }
        tb.update_mesh();
    }

    public static void rotateRight(TetrominoBase tb) {
        eraseMesh(tb);
        tb.rotate = tb.rotate != 3 ? ++tb.rotate : 0;
        tb.update_mesh();
    }

    public static void eraseMesh(TetrominoBase tb) {
        int rot = tb.rotate;
        int height = tb.getHeight();
        int width = tb.getWidth();

        for (int y = tb.pos[0]; y < tb.pos[0] + height; y++) {
            for (int x = tb.pos[1]; x < tb.pos[1] + width; x++) {
                Tetris.MESH[y][x] = '0';
            }
        }
    }

    public static void eraseLine() {
        //리스트에 가득 찬 라인을 저장
        List<Integer> l = new Vector<>();
        for (int y = 2; y < Tetris.HEIGHT; y++) {
            boolean is_full = true;
            for (int x = 0; x < Tetris.WIDTH; x++) {
                if (Tetris.MESH[y][x] == '0') {
                    is_full = false;
                    break;
                }
            }

            if (is_full)
                l.add(y);
        }
        Tetris.top -= l.size();

        //리스트에 저장된 라인들을 지움
        for (int i : l) {
            for (int line = i; line > 2; line--) {
                //highlightLines(i);
                Tetris.MESH[line] = Tetris.MESH[line-1];
            }
            Tetris.MESH[2] = new char[Tetris.WIDTH];
            Arrays.fill(Tetris.MESH[2], '0');

            Tetris.score += 50;
            Tetris.linesNo++;
            Tetris.changeSpeed();
        }
    }
//    public static void highlightLines(int fullLine) {
//        Platform.runLater(() -> {
//            for (int x = 0; x < Tetris.WIDTH; x++) {
//                Rectangle r = (Rectangle) Tetris.pane.getChildren().get(fullLine * Tetris.WIDTH + x);
//                r.setFill(Color.WHITE);
//            }
//        });
//
//        // 대기한 후 원래 색상으로 복원
//        PauseTransition pause = new PauseTransition(Duration.millis(500));
//        pause.setOnFinished(event -> {
//            Platform.runLater(() -> {
//                for (int x = 0; x < Tetris.WIDTH; x++) {
//                    Rectangle r = (Rectangle) Tetris.pane.getChildren().get(fullLine * Tetris.WIDTH + x);
//                    r.setFill(TetrominoBase.getColor(Tetris.MESH[fullLine][x]));
//                }
//            });
//        });
//        pause.play();
//    }


    public static boolean canMoveDown(TetrominoBase tb, int distance) {
        int rot = tb.rotate;
        int height = tb.getHeight();
        int width = tb.getWidth();

        // Tetromino가 아래쪽 경계에 닿았는지 확인
        if (tb.pos[0] + height + distance > Tetris.HEIGHT) {
            return false;
        }

        //무게추 모드
        if (tb.name == 'w') {
            return true;
        }

        // Tetromino의 각 블록이 아래로 이동할 때 다른 블록과 겹치는지 확인
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (tb.mesh[rot][y][x] != 1) {
                    continue; // 빈 공간은 확인하지 않음
                }

                // Tetromino의 블록이 아래쪽으로 이동할 때 충돌 여부 확인
                if (Tetris.MESH[tb.pos[0] + y + distance][tb.pos[1] + x] != '0') {
                    return false;
                }
            }
        }

        return true; // 아래로 이동 가능
    }

    public static boolean canMoveSideWays(TetrominoBase tb, int distance) {
        int rot = tb.rotate;
        int height = tb.getHeight();
        int width = tb.getWidth();

        // Tetromino가 아래쪽 경계에 닿았는지 확인
        if (tb.pos[1] + distance < 0 || tb.pos[1] + width + distance > Tetris.WIDTH) {
            return false;
        }

        // Tetromino의 각 블록이 아래로 이동할 때 다른 블록과 겹치는지 확인
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (tb.mesh[rot][y][x] != 1) {
                    continue; // 빈 공간은 확인하지 않음
                }

                // Tetromino의 블록이 아래쪽으로 이동할 때 충돌 여부 확인
                if (Tetris.MESH[tb.pos[1] + y][tb.pos[1] + x + distance] != '0') {
                    return false;
                }
            }
        }
        return true; // 아래로 이동 가능
    }

    private static void updateTop(TetrominoBase tb) {
        Tetris.top = Math.max(Tetris.HEIGHT - tb.pos[0], Tetris.top);
    }
}

