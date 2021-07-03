package com.sbo.common.utils;

import com.sbo.entity.PeopleOnDuty;
import com.sbo.retrofit.GraphicResult;
import com.sbo.retrofit.PostModel;
import com.sbo.retrofit.WebService;

import java.io.IOException;
import java.util.Collection;

public class WebUtils {

    static public WebService getWebService() {
        return WebService.create();
    }

    static public GraphicResult loadGraphic(int height, int width, Collection<PeopleOnDuty> list) {
        try {
            return getWebService().getFullGraphic(new PostModel(width, height, list)).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
