package com.snust.tetrij.GameScene.tetromino;

import com.snust.tetrij.GameScene.TetrisBoardController;
import com.snust.tetrij.GameSceneMulti.MultiTetrisModel;
import com.snust.tetrij.Tetris;
import javafx.scene.paint.Color;

import static com.snust.tetrij.GameSceneMulti.MultiTetrisModel.*;


public class TetrominoBase {
    public int mesh[][];
    public Color color;
    public char name;
    public int[] pos; //(y,x)

    public boolean can_move;

    public TetrominoBase(boolean gen_item) {
        this.color = new Color(0,0,0,0);
        this.can_move = true;
    }

    /**
     * 아이템모드에서 블럭에 'L'을 추가해주는 함수
     */
    protected void genItem() {
        int block_count = (int)(Math.random()*4);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (mesh[y][x] == 1) {
                    block_count--;
                    if (block_count == 0)
                        mesh[y][x] = 2;
                }
            }
        }
    }

    public void update_mesh(int player) {
        switch (player) {
            case -1:
                for (int y = 0; y < 4; y++) {
                    for (int x = 0; x < 4; x++) {
                        if (pos[0]+y < 0)
                            continue;

                        if (this.mesh[y][x] == 1)
                            Tetris.MESH[y+pos[0]][x+pos[1]] = this.name;
                        else if (this.mesh[y][x] == 2)
                            Tetris.MESH[y+pos[0]][x+pos[1]] = 'L'; // item mode - Line clear
                    }
                }
                break;
            case 0:
                for (int y = 0; y < 4; y++) {
                    for (int x = 0; x < 4; x++) {
                        if (pos[0]+y < 0)
                            continue;

                        if (this.mesh[y][x] == 1)
                            MESH1[y+pos[0]][x+pos[1]] = this.name;
                        else if (this.mesh[y][x] == 2)
                            MESH1[y+pos[0]][x+pos[1]] = 'L'; // item mode - Line clear
                    }
                }
                break;
            case 1:
                for (int y = 0; y < 4; y++) {
                    for (int x = 0; x < 4; x++) {
                        if (pos[0]+y < 0)
                            continue;

                        if (this.mesh[y][x] == 1)
                            MESH2[y+pos[0]][x+pos[1]] = this.name;
                        else if (this.mesh[y][x] == 2)
                            MESH2[y+pos[0]][x+pos[1]] = 'L'; // item mode - Line clear
                    }
                }
                break;

        }

    }

    public static Color getColor(char name) {
        if (!Tetris.color_weakness) {
            //기본모드
            switch (name) {
                case 'i' -> {
                    return Color.SKYBLUE;
                }
                case 'j' -> {
                    return Color.BLUE;
                }
                case 'l' -> {
                    return Color.ORANGE;
                }
                case 'o' -> {
                    return Color.YELLOW;
                }
                case 's' -> {
                    return Color.LIGHTGREEN;
                }
                case 't' -> {
                    return Color.PURPLE;
                }
                case 'z' -> {
                    return Color.RED;
                }
                case 'w' -> {
                    return Color.BLACK;
                }
                case 'L' -> {
                    return getColor(TetrisBoardController.bag.get(0).name);
                }
            };
        }
        else {
            //색약모드
            switch (name) {
                case 'i' -> {
                    return Color.SKYBLUE;
                }
                case 'j' -> {
                    // turquise, 청록
                    return new Color(217.0/256.0, 56.0/256.0, 30.0/256.0, 1);
                }
                case 'l' -> {
                    return Color.ORANGE;
                }
                case 'o' -> {
                    return Color.YELLOW;
                }
                case 's' -> {
                    return Color.GREEN;
                }
                case 't' -> {
                    return Color.BLUE;
                }
                case 'z' -> {
                    //red purple
                    return new Color(149.0/256.0, 53.0/256.0, 83.0/256.0, 1);
                }
                case 'w' -> {
                    return Color.BLACK;
                }
                case 'L' -> {
                    return getColor(TetrisBoardController.bag.get(0).name);
                }
            };
        }
        return Color.WHITE;
    }
}
