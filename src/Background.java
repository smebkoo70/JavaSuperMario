
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Background {
    // 该场景的背景图片
    private BufferedImage backgroundImage;

    public void setOver(boolean over) {
        isOver = over;
    }

    // 障碍物对象的集合
    private List<Obstruction> obstructions = new ArrayList<>();
    // 被销毁的障碍物对象集合
    private List<Obstruction> deadObstructions = new ArrayList<>();
    // 敌人对象的集合
    private List<Enemy> enemies = new ArrayList<>();
    // 被销毁的敌人对象集合
    private List<Enemy> deadEnemies = new ArrayList<>();
    // 场景顺序
    private int index;
    // 是否为最后一个场景的判断标志
    private boolean flag;
    // 游戏结束标记
    private boolean isOver = false;
    // 降旗是否完成的属性
    private boolean flagIsDown = false;

    public void setFlagIsDown(boolean flagIsDown) {
        this.flagIsDown = flagIsDown;
    }

    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    public List<Obstruction> getObstructions() {
        return obstructions;
    }

    public List<Obstruction> getDeadObstructions() {
        return deadObstructions;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Enemy> getDeadEnemies() {
        return deadEnemies;
    }

    public int getIndex() {
        return index;
    }

    public boolean isFlag() {
        return flag;
    }

    public boolean isOver() {
        return isOver;
    }

    public boolean isFlagIsDown() {
        return flagIsDown;
    }

    // 场景类常用的构造方法
    public Background(int index, boolean flag) {
        this.index = index;
        this.flag = flag;

        if (flag) {
            this.backgroundImage = StaticObject.firstStageEnd;
        } else {
            this.backgroundImage = StaticObject.firstStage;
        }

        if (index == 1) {
            for (int i = 1; i <= 15; i++) {
                if (i < 5 || i > 8) {
                    this.obstructions.add(new Obstruction(60 * (i - 1), 540, 9, this));
                }
            }
            this.obstructions.add(new Obstruction(120, 360, 0, this));
            this.obstructions.add(new Obstruction(180, 360, 0, this));
            this.obstructions.add(new Obstruction(580, 240, 3, this));

            this.obstructions.add(new Obstruction(240, 360, 4, this));
            this.obstructions.add(new Obstruction(300, 360, 0, this));
            this.obstructions.add(new Obstruction(360, 360, 4, this));
            this.obstructions.add(new Obstruction(420, 360, 0, this));
            this.obstructions.add(new Obstruction(480, 360, 4, this));


            this.obstructions.add(new Obstruction(120, 480, 2, this));


            this.obstructions.add(new Obstruction(660, 540, 6, this));
            this.obstructions.add(new Obstruction(720, 540, 5, this));
            this.obstructions.add(new Obstruction(660, 480, 8, this));
            this.obstructions.add(new Obstruction(720, 480, 7, this));

            this.enemies.add(new Enemy(600, 480, "left", 1, 0, 600, this));
            this.enemies.add(new Enemy(480, 300, "left", 2, 0, 900, this));
            this.enemies.add(new Enemy(680, 540, "up", 3, 540, 420, this));
        }
        if (index == 2) {
             for (int i = 1; i <= 15; i++) {
                if (i < 5 || i > 8) {
                    this.obstructions.add(new Obstruction(60 * (i - 1), 540, 9, this));
                }
            }
            this.obstructions.add(new Obstruction(120, 360, 3, this));
            this.obstructions.add(new Obstruction(180, 360, 3, this));
            this.obstructions.add(new Obstruction(240, 360, 3, this));
            this.obstructions.add(new Obstruction(300, 360, 3, this));

            this.enemies.add(new Enemy(700, -100, "left", 1, 0, 900, this));
            this.enemies.add(new Enemy(600, -200, "left", 1, 0, 900, this));
            this.enemies.add(new Enemy(800, -300, "left", 1, 0, 900, this));
            this.enemies.add(new Enemy(600, -400, "left", 1, 0, 900, this));
            this.enemies.add(new Enemy(900, -200, "left", 1, 0, 900, this));
            this.enemies.add(new Enemy(1400, -800, "left", 1, 0, 900, this));
            this.enemies.add(new Enemy(1300, -700, "left", 1, 0, 900, this));
            this.enemies.add(new Enemy(1200, -600, "left", 1, 0, 900, this));
        }
        if (index == 3) {
            for (int i = 1; i <= 15; i++) {
                this.obstructions.add(new Obstruction(60 * (i - 1), 540, 9, this));
            }
            this.obstructions.add(new Obstruction(490, 180, 11, this));

         }

    }

    // 使敌人移动的方法
    public void enemyStartMove() {
        for (Enemy enemy : enemies) {
            enemy.getThread().resume();
        }
    }

    // 使敌人停止移动
    public void enemyStopRemove() {
        for (Enemy enemy : enemies) {
            enemy.getThread().suspend();
        }
    }

    // 场景重置方法
    public void reset() {
        // TODO 左上角bug尚未解决
        for (Obstruction obstruction : this.obstructions) {
            obstruction.reset();
        }
        // 先将所有死亡的障碍物还原到当前集合
        this.getObstructions().addAll(this.deadObstructions);
        this.deadObstructions = new ArrayList<>();
        this.getEnemies().addAll(this.deadEnemies);

        this.deadEnemies = new ArrayList<>();

//        System.out.println("当前敌人集合： " + this.getEnemies().size());
//        System.out.println("死亡敌人集合：" + this.getDeadEnemies());

        for (Enemy enemy : this.enemies) {
            if (enemy != null) {
                enemy.reset();
            }
        }

    }


}
