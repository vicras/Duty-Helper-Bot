package com.sbo.retrofit;


import com.sbo.common.utils.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class GraphicResult implements Serializable {
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result implements Serializable {
        public String personId;
        public String fileString;

        public String getPersonId() {
            return personId;
        }

        public String getFileString() {
            return fileString;
        }

        public BufferedImage getImage() {
            return ImageUtil.readImageFrom(fileString);
        }
    }

}
