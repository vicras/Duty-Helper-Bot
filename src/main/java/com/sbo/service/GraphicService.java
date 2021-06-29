package com.sbo.service;

import com.sbo.entity.Duty;

import java.awt.image.BufferedImage;

/**
 * @author Dmitars
 */
public interface GraphicService {
    BufferedImage getGraphicPerDuty(Duty duty);
}
