package de.htw.ba.ue03.matching;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TemplateMatcherAbsolute extends TemplateMatcherBase {

    public TemplateMatcherAbsolute(int[] templatePixel, int templateWidth, int templateHeight) {
        super(templatePixel, templateWidth, templateHeight);
    }

    @Override
    public double[][] getDistanceMap(int[] srcPixels, int srcWidth, int srcHeight) {
        int width = srcWidth - templateWidth;
        int height = srcHeight - templateHeight;
        double[][] dstMap = new double[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                double d = 0;

                for (int i = 0; i < templateWidth; i++) {
                    for (int j = 0; j < templateHeight; j++) {
                        int pos = (y + j) * srcWidth + (x + i);
                        int value = srcPixels[pos] >> 16 & 0xff;

                        int templPos = j * templateWidth + i;
                        int templValue = templatePixel[templPos] >> 16 & 0xff;
                        d += Math.abs(value - templValue);
                    }
                }
                d = d / (templateWidth * templateHeight);
                dstMap[x][y] = d;
            }
        }
        return dstMap;
    }

    @Override
    public void distanceMapToIntARGB(double[][] distanceMap, int[] dstPixels, int dstWidth, int dstHeight) {
        double max = 0;
        double min = Double.MAX_VALUE;
        for (int x = 0; x < dstWidth; x++) {
            for (int y = 0; y < dstHeight; y++) {
                if (distanceMap[x][y] < min) min = distanceMap[x][y];
                if (distanceMap[x][y] > max) max = distanceMap[x][y];
            }
        }

        for (int y = 0; y < dstHeight; y++) {
            for (int x = 0; x < dstWidth; x++) {
                int dstPos = y * dstWidth + x;
                int value = 255 - (int) ((distanceMap[x][y] - min) / max * 255);
                value = capValue(value);

                dstPixels[dstPos] = 0xFF000000 | (value << 16) | (value << 8) | value;
            }
        }
    }
}
