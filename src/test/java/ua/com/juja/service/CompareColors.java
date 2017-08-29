package ua.com.juja.service;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CompareColors {

    public static void imagine() throws IOException {
        BufferedImage image =
                ImageIO.read(new File("C:\\tmp\\image1.png"));
        BufferedImage image2 =
                ImageIO.read(new File("C:\\tmp\\image2.png"));

        int widthImagine1 = image.getWidth();
        int heightImagine1 = image.getHeight();
        int widthImagine2 = image2.getWidth();
        int heightImagine2 = image2.getHeight();
        int[][] pixels1 = new int[widthImagine1][heightImagine1];
        int[][] pixels2 = new int[widthImagine2][heightImagine2];

        for (int y = 0; y < heightImagine1; y++) {
            for (int x = 0; x < widthImagine1; x++) {
                pixels1[x][y] = image.getRGB(x, y);
            }
        }

        for (int y = 0; y < heightImagine2; y++) {
            for (int x = 0; x < widthImagine2; x++) {
                pixels2[x][y] = image2.getRGB(x, y);
            }
        }
        imageToBufferedImage(image);
        for (int y = 0; y < heightImagine2; y++) {
            for (int x = 0; x < widthImagine2; x++) {
                if (pixels1[x][y] != pixels2[x][y]) {
                    System.out.println("Different : " + pixels1[x][y] + " " + pixels2[x][y]);
                    Graphics2D gc = image.createGraphics();
                    gc.setColor(Color.RED);
                    gc.drawRect(x, y, 1, 1);
                }
            }
        }
        ImageIO.write(image, "png", new File("C:\\tmp\\new.png"));
    }

    public static void main(String[] args) throws IOException {
        imagine();
    }

    private static BufferedImage imageToBufferedImage(Image img) {
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();
        g2.drawImage(img, null, null);
        return bi;
    }
}