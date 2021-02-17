import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mario implements Runnable {
    // mario的死亡标记
    private boolean isDead;
    // 游戏结束标记
    private boolean gameOver;
    // 马里奥的位置
    private int x;
    private int y;
    // mario的生命数
    private int life;
    // mario的分数
    private int num;
    // 马里奥每50ms x轴位移的距离
    private int xMove;
    // 马里奥每50ms y轴的位移距离
    private int yMove;
    // mario的上升时间
    private int upTime;
    // 马里奥状态
    // 'left-standing' 'right-standing' 'left-moving' 'right-moving' 'jump'
    private String status;
    // 马里奥当前状态的图片
    private BufferedImage marioImage;
    // mario当前所在的场景
    private Background currentBackground;
    private Thread thread;
    // 记录上一次循环中 mario 的图片索引
    private int currentMarioImageIndex;
    // 更换场景标记
    private boolean changeBackground = false;
    // 游戏是否结束标记
    private boolean finish = false;
    private boolean deadFlashOver = false;

    public boolean isDead() {
        return isDead;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLife() {
        return life;
    }

    public int getNum() {
        return num;
    }

    public int getxMove() {
        return xMove;
    }

    public int getyMove() {
        return yMove;
    }

    public int getUpTime() {
        return upTime;
    }

    public String getStatus() {
        return status;
    }

    public BufferedImage getMarioImage() {
        return marioImage;
    }

    public Background getCurrentBackground() {
        return currentBackground;
    }

    public Thread getThread() {
        return thread;
    }

    public int getCurrentMarioImageIndex() {
        return currentMarioImageIndex;
    }

    public boolean isChangeBackground() {
        return changeBackground;
    }

    public boolean isFinish() {
        return finish;
    }

    public boolean isDeadFlashOver() {
        return deadFlashOver;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setxMove(int xMove) {
        this.xMove = xMove;
    }

    public void setyMove(int yMove) {
        this.yMove = yMove;
    }

    public void setUpTime(int upTime) {
        this.upTime = upTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMarioImage(BufferedImage marioImage) {
        this.marioImage = marioImage;
    }

    public void setCurrentBackground(Background currentBackground) {
        this.currentBackground = currentBackground;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public void setCurrentMarioImageIndex(int currentMarioImageIndex) {
        this.currentMarioImageIndex = currentMarioImageIndex;
    }

    public void setChangeBackground(boolean changeBackground) {
        this.changeBackground = changeBackground;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public void setDeadFlashOver(boolean deadFlashOver) {
        this.deadFlashOver = deadFlashOver;
    }

    // mario的常用构造方法
    public Mario(int x, int y) {
        this.isDead = false;
        this.gameOver = false;
        this.x = x;
        this.y = y;
        this.status = "right-standing";
        this.marioImage = StaticObject.marioImages.get(0);
        thread = new Thread(this);
        thread.start();
    }

    // mario的向左移动方法
    public void leftMove() {
        this.xMove = -20;
        if (this.status.contains("jump")) {
            this.status = "left-moving-jump";
        } else {
            this.status = "left-moving";
        }
    }

    // mario的向右移动方法
    public void rightMove() {
        this.xMove = 20;
        if (this.status.contains("jump")) {
            this.status = "right-moving-jump";
        } else {
            this.status = "right-moving";
        }
    }

    // mario的向左站立方法
    public void leftStanding(){
        this.xMove = 0;
        if (this.status.contains("jump")) {
            this.status = "left-standing-jump";
        } else {
            this.status = "left-standing";
        }
    }

    // mario的向右站立方法
    public void rightStanding() {
        this.xMove = 0;
        if (this.status.contains("jump")) {
            this.status = "right-standing-jump";
        } else {
            this.status = "right-standing";
        }
    }

    // mario的跳跃方法
    public void jump() {
        if (!this.status.contains("jump")) {
            if (this.status.contains("left")) {
                if (this.xMove != 0) {
                    this.status = "left-moving-jump";
                } else {
                    this.status = "left-standing-jump";
                }
            } else {
                if (this.xMove != 0) {
                    this.status = "right-moving-jump";
                } else {
                    this.status = "right-standing-jump";
                }
            }
            this.yMove = -10;
            // 假设mario跳跃的最大高度为 180px  每50ms 向上位移10px  upTime应该是多少
            this.upTime = 18;
        }
    }

    // mario的下落方法
    public void drop() {
        if (this.status.contains("left")) {
            if (this.xMove != 0) {
                this.status = "left-moving-jump";
            } else {
                this.status = "left-standing-jump";
            }
        } else {
            if (this.xMove != 0) {
                this.status = "right-moving-jump";
            } else {
                this.status = "right-standing-jump";
            }
        }
        this.yMove = 10;
        if (this.isDead) {
            this.yMove = 30;
        }
    }

    // mario的死亡方法
    public void dead() {
        // TODO bug3
        this.rightStanding();
        this.life --;
        if (this.life == 0) {
            this.gameOver = true;
        } else {
            this.currentBackground.reset();
            this.x = 0;
            this.y = 470;
            this.setMarioImage(StaticObject.marioImages.get(0));
            MarioFrame.canOperate = true;
        }
    }

//    private int q = 0;

    @Override
    public void run() {
        while(true) {

            if (this.currentBackground.isFlag() && this.x >= 490) {
                this.currentBackground.setOver(true);

                if (this.currentBackground.isFlagIsDown()) {
                    this.status = "right-moving";
                    this.x += 5;
                    if (this.x >= 780) {
                        this.finish = true;
                    }
                } else {
                    if (this.y < 480) {
                        this.y += 20;
                    }
                    if (this.y >= 480) {
                        this.y = 480;
                    }
                    this.status = "right-standing";
                }

            } else {
                boolean allowRight = true;
                boolean allowLeft = true;
                boolean onObstruction = false;
                boolean allowUp = true;
                Obstruction obstruction;
                Enemy enemy;

                Obstruction obstruction1 = new Obstruction();
                Obstruction obstruction2 = new Obstruction();

                for (int j = 0; j < this.currentBackground.getObstructions().size(); j++) {
                    obstruction = this.currentBackground.getObstructions().get(j);
                    if (obstruction.getType() == 9 && obstruction.getX() == 120) {
                       obstruction1 = obstruction;
                    }
                    if (obstruction.getType() == 9 && obstruction.getX() == 180) {
                        obstruction2 = obstruction;
                    }
                }

                if (!this.isDead) {
                    for (int i = 0; i < this.currentBackground.getEnemies().size(); i ++) {
                        enemy = this.currentBackground.getEnemies().get(i);
                        if (enemy != null) {
                            // 马里奥横向和敌人碰撞
                            if (enemy.getX() + 60 > this.x && enemy.getX() - 60 < this.x && (enemy.getY() < this.y + 60 && enemy.getY() > this.y - 60)) {
                                this.upTime = 5;
                                this.yMove = -20;
                                this.isDead = true;
                            }
                            // 马里奥从上方与敌人碰撞
                            if (enemy.getY() == this.y + 60 && (enemy.getX() - 62 < this.x && enemy.getX() + 90 > this.x)) {
                                if (enemy.getType() == 1) {
                                    enemy.dead();
                                    this.upTime = 10;
                                    this.yMove = -5;
                                }
                                if (enemy.getType() == 2) {
                                    enemy.turtleDead();
                                    this.upTime = 10;
                                    this.yMove = -5;
                                }
                                if (enemy.getType() == 3) {
                                    this.isDead = true;
                                }
                            }
                        }


                    }

                    for (int i = 0; i < this.currentBackground.getObstructions().size(); i ++) {
                        obstruction = this.currentBackground.getObstructions().get(i);
                        // 如果mario的坐标加上60和障碍物坐标相等，不允许向右
                        if (obstruction.getX() == this.x + 60 && (obstruction.getY() < this.y + 60 && obstruction.getY() > this.y - 60)) {
                            allowRight = false;
                            if (obstruction.getType() == 3) {
                                allowRight = true;
                            }
                        }
                        // 如果mario的坐标减上60和障碍物坐标相等，不允许向右
                        if (obstruction.getX() == this.x - 60 && (obstruction.getY() < this.y + 60 && obstruction.getY() > this.y - 60)) {
                            allowLeft = false;
                            if (obstruction.getType() == 3) {
                                allowLeft = true;
                            }
                        }
                        // 是否允许跳跃
                        if (obstruction.getY() == this.y + 60 && (obstruction.getX() - 62 < this.x && obstruction.getX() + 62 > this.x)) {
                            onObstruction = true;
                            if (obstruction.getType() == 3) {
                                onObstruction = false;
                            }
                        }
                        // 是否头顶有障碍物
                        if (obstruction.getY() == this.y - 60 && (obstruction.getX() - 60 < this.x && obstruction.getX() + 60 > this.x)) {
                            allowUp = false;
                            if (obstruction.getType() == 0) {
                                this.currentBackground.getObstructions().remove(obstruction);
                                this.currentBackground.getDeadObstructions().add(obstruction);
                            }
                            if ((obstruction.getType() == 4 || obstruction.getType() == 3) && this.upTime > 0) {
                                obstruction.setType(2);
                                obstruction.setImage(2);

                                if (this.currentBackground.getIndex() == 2) {

                                    // TODO bug2
                                    this.currentBackground.getDeadObstructions().add(obstruction1);
                                    this.currentBackground.getDeadObstructions().add(obstruction2);
                                    for (int m = 0; m < this.currentBackground.getObstructions().size(); m++) {
                                        this.currentBackground.getObstructions().remove(obstruction1);
                                        this.currentBackground.getObstructions().remove(obstruction2);

                                    }
                                }

                            }

                        }
                    }
                }



                // 是让马里奥的坐标发生偏移

                if ((allowLeft && this.xMove < 0) || (allowRight && this.xMove > 0)) {
                    this.x += this.xMove;
                    if (this.x < 0) {
                        this.x = 0;
                    }
                }

                if (this.y > 600) {
                    this.isDead = true;
                    this.deadFlashOver = true;
                }

                if (onObstruction && this.upTime == 0) {
                    // 将mario 的 status中的 jump干掉
                    if (this.status.contains("left")) {
                        if (this.xMove != 0) {
                            this.status = "left-moving";
                        } else {
                            this.status = "left-standing";
                        }
                    } else {
                        if (this.xMove != 0) {
                            this.status = "right-moving";
                        } else {
                            this.status = "right-standing";
                        }
                    }
                } else {
                    if (this.upTime > 0) {
                        this.upTime --;
                        if (!allowUp) {
                            this.upTime = 0;
                            this.drop();
                        }
                    } else {
                        this.drop();
                    }
                    this.y += this.yMove;
                }


            }

            // 用来区分马里奥面向 同时 用来获取马里奥对应的图片
            int marioImageIndex = 0;

            // 根据马里奥当前的状态，让马里奥的面向发生改变
            if (this.status.contains("left")) {
                marioImageIndex += 5;
            }

            if (this.status.contains("moving")) {
                //currentMarioImageIndex 记录上一次循环中 mario 的图片索引
                marioImageIndex += this.currentMarioImageIndex;
                this.currentMarioImageIndex ++;
                if (this.currentMarioImageIndex == 4) {
                    this.currentMarioImageIndex = 0;
                }
            }

            if (this.status.contains("jump")) {
                if (this.status.contains("left")) {
                    marioImageIndex = 9;
                }
                if (this.status.contains("right")) {
                    marioImageIndex = 4;
                }
            }

            if (this.isDead) {
                this.marioImage = StaticObject.marioDead;
            } else {
                this.marioImage = StaticObject.marioImages.get(marioImageIndex);
            }


            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
