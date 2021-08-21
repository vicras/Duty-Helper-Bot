package com.sbo.service.impl;

import com.sbo.common.utils.WebUtils;
import com.sbo.domain.postgres.entity.Duty;
import com.sbo.service.GraphicService;

import java.awt.image.BufferedImage;

/**
 * @author Dmitars
 */
public class GraphicServiceImpl implements GraphicService {
    @Override
    public BufferedImage getGraphicPerDuty(Duty duty) {
        return WebUtils.loadGraphic(65, 240, duty.getPeopleOnDuties()).getResult().getImage();
    }
}
