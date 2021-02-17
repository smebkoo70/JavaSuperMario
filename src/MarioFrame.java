import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MarioFrame extends JFrame implements KeyListener, Runnable {

    // 游戏开始后所有场景存放的集合
    private List<Background> backgroundList;
    // 游戏进行时的当前场景对象
    private Background currentBackground;
    // 当前游戏的mario对象
    private Mario mario;
    // 用于界面刷新的线程
    private Thread thread;
    // 游戏正式开始标记
    private boolean isStart = false;
    // 可操作标记
    public static boolean canOperate = true;

    // 光标悬停在JFrame红色文字上 然后 alt + enter
    public MarioFrame() throws IOException {
        this.backgroundList = new ArrayList<>();
        this.currentBackground = new Background();
        this.mario = new Mario();
        this.thread = new Thread(this);


        // 设置程序标题
        this.setTitle("马里奥");
        // 设置窗口大小, 两个参数，宽高，默认单位像素
        this.setSize(900, 600);
        // 获取全萤幕尺寸
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        // 设置屏幕位置
        this.setLocation((width - 900) / 2, (height - 600) / 2);
        // 关闭界面时，程序立即停止
        // 注释当前行的快捷键： ctrl + ?
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 支持渲染
        this.setVisible(true);
        // 用户无法手动更改窗体大小
        this.setResizable(false);

        StaticObject.init();


        // 定义了当前游戏的所有场景
        for (int i = 1; i <= 3; i++) {
            this.backgroundList.add(new Background(i, i == 3));
        }
        this.currentBackground = this.backgroundList.get(0);

        this.mario = new Mario(0, 480);
        this.mario.setCurrentBackground(this.currentBackground);

        // 打开界面时，就让控制马里奥图片刷新的线程启动
        this.thread.start();
        this.addKeyListener(this);
        // 实时渲染
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        // 为读取到的图片信息创建一个缓冲区
        BufferedImage image = new BufferedImage(900, 600, BufferedImage.TYPE_3BYTE_BGR);
        // 拿到该图片的画笔工具
        Graphics graphics = image.getGraphics();
        if (this.isStart) {
            // 把图片绘制到缓冲区
            graphics.drawImage(this.currentBackground.getBackgroundImage(), 0, 0, this);

            graphics.drawString("生命：" + this.mario.getLife(), 50, 50);
            graphics.drawString("分数：" + this.mario.getNum(), 150, 50);

            // 将当前场景的而敌人信息绘制到缓冲区
            for (Enemy enemy : this.currentBackground.getEnemies()) {
                graphics.drawImage(enemy.getEnemyImage(), enemy.getX(), enemy.getY(), this);
            }
            // 将当前场景的障碍物绘制到缓冲区
            for (Obstruction obstruction : this.currentBackground.getObstructions()) {
                graphics.drawImage(obstruction.getObstructionImage(), obstruction.getX(), obstruction.getY(), this);
            }
            graphics.drawImage(this.mario.getMarioImage(), this.mario.getX(), this.mario.getY(), this);
        } else {
            graphics.drawImage(StaticObject.start, 0, 0, this);
        }



        // 界面的画笔把缓冲区的内容绘制到界面上
        g.drawImage(image, 0, 0, this);
    }

    // 键盘敲击
    @Override
    public void keyTyped(KeyEvent e) {

    }

    // 键盘按下
    @Override
    public void keyPressed(KeyEvent e) {

        if (this.isStart && canOperate) {
            // 按下向右
            if (e.getKeyCode() == 39) {
                this.mario.rightMove();
            }
            // 按下向左
            if (e.getKeyCode() == 37) {
                this.mario.leftMove();
            }
            // 按下向上)
            if (e.getKeyCode() == 38) {
                this.mario.jump();
            }
        } else {
            if (e.getKeyCode() == 32) {
                this.isStart = true;
                this.currentBackground.enemyStartMove();
                this.mario.setLife(30);
                this.mario.setNum(0);
            }
        }


    }

    // 键盘抬起
    @Override
    public void keyReleased(KeyEvent e) {
        // 抬起向右
        if (e.getKeyCode() == 39) {
            this.mario.rightStanding();
        }
        // 抬起向左
        if (e.getKeyCode() == 37) {
            this.mario.leftStanding();
        }
    }

    @Override
    public void run() {
        while (true) {
            this.repaint();
            if (this.mario.getX() >= 840) {
                this.currentBackground = this.backgroundList.get(this.currentBackground.getIndex());
                this.mario.setCurrentBackground(this.currentBackground);
                this.currentBackground.enemyStartMove();
                this.mario.setX(0);
            }
            if (this.mario.isGameOver()) {
                JOptionPane.showMessageDialog(this, "生命耗尽， 游戏结束");
                System.exit(0);
            }
            if (this.mario.isDead() && this.mario.isDeadFlashOver()) {
                canOperate = false;
                this.currentBackground.enemyStopRemove();
                JOptionPane.showMessageDialog(this, "mario死亡，重新开始");
                this.currentBackground.enemyStartMove();
                this.mario.dead();
                this.mario.setDead(false);
                this.mario.setDeadFlashOver(false);
            }
            if (this.mario.isFinish()) {
                JOptionPane.showMessageDialog(this, "恭喜游戏通关");
                System.exit(0);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}