import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enemy implements Runnable {
    // 敌人运动时的坐标
    private int x;
    private int y;
    // 敌人的初始坐标
    private int startX;
    private int startY;
    private String startMoveType;
    // 敌人的种类
    private int type; // 1代表蘑菇头 2代表乌龟 3代表食人花
    // 敌人的图片
    private BufferedImage enemyImage;
    // 移动的范围
    private int upMax;
    private int downMax;
    private int leftMax;
    private int rightMax;
    // 移动类型
    private String moveType; // "left" "right" "up" "down"
    // 定义线程
    private Thread thread;
    // 敌人上一次运动的图片索引
    private int currentImageIndex;
    // 计数器
    private int temp;
    private Background currentBackground;
    private boolean turtleFirstDead = false;
    private boolean turtleSecondDead = false;
    private boolean isDead = false;
    private int turtleTemp;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public String getStartMoveType() {
        return startMoveType;
    }

    public int getType() {
        return type;
    }

    public BufferedImage getEnemyImage() {
        return enemyImage;
    }

    public int getUpMax() {
        return upMax;
    }

    public int getDownMax() {
        return downMax;
    }

    public int getLeftMax() {
        return leftMax;
    }

    public int getRightMax() {
        return rightMax;
    }

    public String getMoveType() {
        return moveType;
    }

    public Thread getThread() {
        return thread;
    }

    public int getCurrentImageIndex() {
        return currentImageIndex;
    }

    public int getTemp() {
        return temp;
    }

    public Background getCurrentBackground() {
        return currentBackground;
    }

    public boolean isTurtleFirstDead() {
        return turtleFirstDead;
    }

    public boolean isTurtleSecondDead() {
        return turtleSecondDead;
    }

    public boolean isDead() {
        return isDead;
    }

    public int getTurtleTemp() {
        return turtleTemp;
    }

    public Enemy(int startX, int startY, String moveType, int type, int min, int max, Background currentBackground) {
        this.turtleTemp = 200;
        this.currentBackground = currentBackground;
        this.thread = new Thread(this);
        this.x = startX;
        this.y = startY;
        this.startX = startX;
        this.startY = startY;
        this.type = type;
        this.moveType = moveType;
        this.startMoveType = moveType;
        if (this.type == 1) {
            this.leftMax = min;
            this.rightMax = max;
            this.enemyImage = StaticObject.triangleImages.get(0);
        }
        if (this.type == 2) {
            this.leftMax = min;
            this.rightMax = max;
            this.enemyImage = StaticObject.turtleImages.get(0);
        }
        if (this.type == 3) {
            this.downMax = min;
            this.upMax = max;
            this.enemyImage = StaticObject.flowerImages.get(0);
        }
        this.temp = 15;
        thread.start();
        thread.suspend();
    }

    // 敌人的死亡方法
    public void dead() {
        this.currentBackground.getDeadEnemies().add(this);
        this.currentBackground.getEnemies().remove(this);
        System.out.println("当前敌人集合： " + this.currentBackground.getEnemies().size());
        System.out.println("死亡敌人集合：" + this.currentBackground.getDeadEnemies().size());
    }
    // 乌龟死亡方法
    public void turtleDead() {
        this.enemyImage = StaticObject.turtleImages.get(4);

        if (this.turtleFirstDead) {
            this.turtleSecondDead = true;
        }
        this.turtleFirstDead = true;
    }

    public void reset() {
        this.currentImageIndex = 0;
        this.x = this.startX;
        this.y = this.startY;
        this.moveType = this.startMoveType;
        if (this.type == 1) {
            this.enemyImage = StaticObject.triangleImages.get(0);
        } else if (this.type == 2) {
            this.turtleTemp = 200;
            this.turtleFirstDead = false;
            this.turtleSecondDead = false;
            this.enemyImage = StaticObject.turtleImages.get(0);
        } else {
            this.enemyImage = StaticObject.flowerImages.get(0);
        }
        this.isDead = false;
    }
    @Override
    public void run() {
        while (true) {
            int imageIndex = 0;
            Obstruction obstruction;
            boolean allowLeft = true;
            boolean allowRight = true;
            boolean onObstruction = false;
            this.temp --;

            for (int i = 0; i < this.currentBackground.getObstructions().size(); i ++) {
                obstruction = this.currentBackground.getObstructions().get(i);
                // 如果enemy的坐标加上60和障碍物坐标相等，不允许向右
                if (obstruction.getX() == this.x + 60 && (obstruction.getY() < this.y + 60 && obstruction.getY() > this.y - 60)) {
                    allowRight = false;
                    if (obstruction.getType() == 3) {
                        allowRight = true;
                    }
                }
                // 如果enemy的坐标减上60和障碍物坐标相等，不允许向左
                if (obstruction.getX() == this.x - 60 && (obstruction.getY() < this.y + 60 && obstruction.getY() > this.y - 60)) {
                    allowLeft = false;
                    if (obstruction.getType() == 3) {
                        allowLeft = true;
                    }
                }
                // 敌人是否在障碍物上
                if (obstruction.getY() == this.y + 60 && (obstruction.getX() - 62 < this.x && obstruction.getX() + 62 > this.x)) {
                    onObstruction = true;
                    if (obstruction.getType() == 3) {
                        onObstruction = false;
                    }
                }
            }


            // 因为只有乌龟在左右移动的时候会带有面向
            if (this.moveType.equals("right") && this.type == 2) {
                imageIndex += 2;
            }

            imageIndex += this.currentImageIndex;
           if (this.temp == 0) {
               this.currentImageIndex ++;
               this.temp = 15;
           }


            if (this.type == 1) {
                if (this.moveType.equals("left")) {
                    this.x += -5;
                    System.out.println("蘑菇头的横坐标" + this.x);
                    if (!allowLeft) {
                        this.moveType = "right";
                    }
                }
                if (this.moveType.equals("right")) {
                    this.x += 5;
                    if (!allowRight) {
                            this.moveType = "left";

                    }
                }
                if (!onObstruction) {
                    this.y += 5;
                }
                if (this.x >= 900 || this.x <= 0 || this.y >= 600) {
                    if (!this.isDead) {
                        this.dead();
                        this.isDead = true;
                    }
                }
                this.enemyImage = StaticObject.triangleImages.get(imageIndex);
                if (this.currentImageIndex == 2) {
                    this.currentImageIndex = 0;
                }
            }

            // 乌龟的移动
            if (this.type == 2 && !this.turtleFirstDead) {
                this.turtleTemp = 200;
                if (this.moveType.equals("left")) {
                    this.x += -5;
                    if (!allowLeft) {
                        this.moveType = "right";
                    }
                }
                if (this.moveType.equals("right")) {
                    this.x += 5;
                    if (!allowRight) {
                        this.moveType = "left";
                    }
                }
                if (!onObstruction) {
                    this.y += 5;
                }
                if (this.x >= 900 || this.x <= 0 || this.y >= 600) {
                    if (!this.isDead) {
                        this.dead();
                        this.isDead = true;
                    }
                }
                this.enemyImage = StaticObject.turtleImages.get(imageIndex);
                if (this.currentImageIndex == 2) {
                    this.currentImageIndex = 0;
                }
            } else if (this.turtleFirstDead && !this.turtleSecondDead) {
                this.x += 0;
                this.turtleTemp --;
                if (this.turtleTemp == 0) {
                    this.turtleFirstDead = false;
                    this.turtleTemp = 200;
                    this.currentImageIndex = 0;
                }
            } else if (this.turtleSecondDead) {
                this.turtleTemp = 200;
                if (this.moveType.equals("left")) {
                    this.x += -30;
                    if (!allowLeft) {
                        this.moveType = "right";
                    }
                }
                if (this.moveType.equals("right")) {
                    this.x += 30;
                    if (!allowRight) {
                        this.moveType = "left";
                    }
                }
                if (!onObstruction) {
                    this.y += 5;
                }
                if (this.x >= 900 || this.x <= 0 || this.y >= 600) {
                    if (!this.isDead) {
                        this.dead();
                        this.isDead = true;
                    }
                }
            }

            if (this.type == 3) {
                if (this.moveType.equals("up")) {
                    this.y += -5;
                    if (this.y == this.upMax) {
                        this.moveType = "down";
                    }
                }
                if (this.moveType.equals("down")) {
                    this.y += 5;
                    if (this.y == this.downMax) {
                        this.moveType = "up";
                    }
                }
                this.enemyImage = StaticObject.flowerImages.get(imageIndex);
                if (this.currentImageIndex == 2) {
                    this.currentImageIndex = 0;
                }
            }



            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
