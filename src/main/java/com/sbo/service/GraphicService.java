package com.sbo.service;

import com.sbo.domain.postgres.entity.Duty;

import java.awt.image.BufferedImage;

/**
 * @author Dmitars
 */
public interface GraphicService {
    BufferedImage getGraphicPerDuty(Duty duty);
}
