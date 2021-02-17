import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Obstruction implements Runnable {
    // 障碍物的坐标
    private int x;
    private int y;
    // 初始种类
    private int startType;
    // 障碍物的种类
    private int type;

    public void setType(int type) {
        this.type = type;
    }

    // 障碍物所属的当前场景
    private Background currentBackground;
    // 障碍物对应的图片
    private BufferedImage obstructionImage; // type = 9, ob10
    // 障碍物对象的线程（暂时只用于旗子）
    private Thread thread;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getStartType() {
        return startType;
    }

    public int getType() {
        return type;
    }

    public Background getCurrentBackground() {
        return currentBackground;
    }

    public BufferedImage getObstructionImage() {
        return obstructionImage;
    }

    public Thread getThread() {
        return thread;
    }
 
    // 障碍物常用的构造方法
    public Obstruction(int x, int y, int type, Background currentBackground) {
        this.thread = new Thread(this);
        this.x = x;
        this.y = y;
        this.type = type;
        this.startType = type;
        this.currentBackground = currentBackground;
        setImage(type);

        if (this.type == 11) {
            thread.start();
        }
    }

    // 根据障碍物类型获取障碍物图片
    public void setImage(int type) {
        this.obstructionImage = StaticObject.obstructionImages.get(type);
    }

    @Override
    public void run() {
        while (true) {
            if (this.currentBackground.isOver()) {
                // 降旗
                if (this.y < 420) {
                    this.y += 5;
                } else {
                    this.currentBackground.setFlagIsDown(true);
                }
            }


            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 障碍物的重置方法
    public void reset() {
        this.type = this.startType;
        this.obstructionImage = StaticObject.obstructionImages.get(this.type);
    }


}
