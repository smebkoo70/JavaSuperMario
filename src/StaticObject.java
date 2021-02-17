import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StaticObject {
    // 所有的马里奥图片集合
    public static List<BufferedImage> marioImages = new ArrayList<>();
    // 所有的食人花集合
    public static List<BufferedImage> flowerImages = new ArrayList<>();
    // 所有的蘑菇头集合
    public static List<BufferedImage> triangleImages = new ArrayList<>();
    // 所有的乌龟集合
    public static List<BufferedImage> turtleImages = new ArrayList<>();
    // 所有的障碍物集合
    public static List<BufferedImage> obstructionImages = new ArrayList<>();
    // 马里奥死亡时图片
    public static BufferedImage marioDead = null;
    // 初始背景图
    public static BufferedImage firstStage = null;
    // 结束背景图
    public static BufferedImage firstStageEnd = null;
    // 界面启动背景图
    public static BufferedImage start = null;
    // 封装图片路径
    public static String imagePath = System.getProperty("user.dir") + "/out/production/mario/";

    // 静态资源的重置方法
    public static void init() throws IOException {
        // 读取马里奥图片并放入集合
        for (int i = 1; i <= 10; i++) {
            marioImages.add(ImageIO.read(new File(imagePath + i + ".png")));
            // alt + enter 处理异常信息
        }
        // 读取背景图并放入容器
        marioDead = ImageIO.read(new File(imagePath + "over.png"));
        firstStage = ImageIO.read(new File(imagePath + "firstStage.jpg"));
        firstStageEnd = ImageIO.read(new File(imagePath + "firstStageEnd.jpg"));
        start = ImageIO.read(new File(imagePath + "start.jpg"));
        // 乌龟 蘑菇头 食人花
        for (int i = 1; i <= 5; i++) {
            if (i <= 2) {
                flowerImages.add(ImageIO.read(new File(imagePath + "flower" + i + ".png")));
            }
            if (i <= 3) {
                triangleImages.add(ImageIO.read(new File(imagePath + "triangle" + i + ".png")));
            }
            turtleImages.add(ImageIO.read(new File(imagePath + "turtle" + i + ".png")));
        }

        // 读取障碍物图片并放入容器
        for (int i = 1; i <= 12; i++) {
            obstructionImages.add(ImageIO.read(new File(imagePath + "ob" + i + ".png")));
        }


    }
}
