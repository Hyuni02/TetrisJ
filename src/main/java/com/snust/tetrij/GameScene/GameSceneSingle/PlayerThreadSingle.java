package com.snust.tetrij.GameScene.GameSceneSingle;
import javafx.application.Platform;

import static com.snust.tetrij.Controller.GameOverController.switchToGameOver;
import static com.snust.tetrij.GameScene.GameSceneMulti.MultiTetrisController.controller;
import static com.snust.tetrij.GameScene.GameSceneSingle.SingleTetrisController.controller_s;
import static com.snust.tetrij.GameScene.GameSceneSingle.SingleTetrisView.view_s;

public class PlayerThreadSingle extends Thread {
    String thread_name;

    public PlayerThreadSingle(String thread_name) {
        super(thread_name);
        this.thread_name = thread_name;
    }


    @Override
    public void run() {
        SingleBoardController.generateTetromino();
        SingleBoardController.generateTetromino();
        SingleBoardController.bag.get(0).update_mesh(-1);

        int speedLevel = 0;
        while (!controller_s.isGameOver) {
            System.out.println(SingleBoardController.bag.get(1).pos[0]); //없으면 isPaused가 변경된걸 모름

            if (controller_s.isPaused)
                continue;

            int freq = 300;
            int finalFreq;
            int boost = 30;
            switch (controller_s.currentDifficulty) {
                case EASY -> finalFreq = freq - speedLevel * (int) (boost * 0.8f);
                case HARD -> finalFreq = freq - speedLevel * (int) (boost * 1.2f);
                default -> finalFreq = freq - speedLevel * boost; //normal or item
            }
            view_s.color_mesh();
            try {
                this.sleep(finalFreq);
            } catch (InterruptedException e) {
                this.interrupt();
            }


            if (speedLevel == 0)
                controller_s.score++;
            else if (speedLevel == 1)
                controller_s.score += 2;
            else if (speedLevel == 2)
                controller_s.score += 3;
            view_s.scoretext.setText("Score: " + Integer.toString(controller_s.score));
            view_s.level.setText("Lines: " + Integer.toString(controller_s.linesNo));

            SingleBoardController.softDrop(SingleBoardController.bag.get(0)); //한칸 드랍
            //게임오바
            if (controller_s.top == 19)
                controller_s.isGameOver = true;
        }
        Platform.runLater(() -> {
            switchToGameOver(controller.score, controller.currentDifficulty);
        });
        this.interrupt();
    }
}
