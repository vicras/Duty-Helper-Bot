package com.sbo.common.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Dmitars
 */
public class ImageUtil {
    public static BufferedImage readImageFrom(String fileString) {
        try {
            return javax.imageio.ImageIO.read(new File(fileString));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
