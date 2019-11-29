package de.htw.ba.ue03.matching;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TemplateMatcherCorrCoef extends TemplateMatcherBase {
    public TemplateMatcherCorrCoef(int[] templatePixel, int templateWidth, int templateHeight) {
        super(templatePixel, templateWidth, templateHeight);
    }

    @Override
    public double[][] getDistanceMap(int[] srcPixels, int srcWidth, int srcHeight) {
        double sumR2 = 0;
        double sumR = 0;
        for (int j = 0; j < templateHeight; j++) {
            for (int i = 0; i < templateWidth; i++) {
                int pos = j * templateWidth + i;
                int value = templatePixel[pos] >> 16 & 0xff;
                sumR2 += value * value;
                sumR += value;
            }
        }
        double nR = templatePixel.length;
        double meanR = sumR / nR;
        double sigmaR = (float) Math.sqrt(sumR2 - nR * meanR * meanR);

        int width = srcWidth - templateWidth;
        int height = srcHeight - templateHeight;
        double[][] dstMap = new double[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                double sumI = 0;
                double sumI2 = 0;
                double sumIR = 0;
                for (int i = 0; i < templateWidth; i++) {
                    for (int j = 0; j < templateHeight; j++) {
                        int pos = (y + j) * srcWidth + (x + i);
                        int value = srcPixels[pos] >> 16 & 0xff;

                        int templPos = j * templateWidth + i;
                        int templValue = templatePixel[templPos] >> 16 & 0xff;

                        sumI += value;
                        sumI2 += value * value;
                        sumIR += value * templValue;
                    }
                }

                double meanI = sumI / nR;

                double result = (sumIR - nR * meanI * meanR) / (Math.sqrt(sumI2 - nR * meanI * meanI) * sigmaR);
                dstMap[x][y] = result;
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
                int value = (int) ((distanceMap[x][y] - min) / max * 255);
                value = capValue(value);

                dstPixels[dstPos] = 0xFF000000 | (value << 16) | (value << 8) | value;
            }
        }
    }

    @Override
    public List<Point> findMaximas(double[][] distanceMap) {
        ArrayList<Point> result = new ArrayList<>();
        int height = distanceMap.length;
        int width = distanceMap[0].length;
        int delta = -11 / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = distanceMap[y][x];

                boolean maxFound = false;

                if (value > 0.9) {
                    outerKernel:
                    for (int kernelY = y + delta; kernelY <= y - delta; kernelY++) {
                        for (int kernelX = x + delta; kernelX <= x - delta; kernelX++) {
                            if(kernelY < 0 || kernelX < 0 || kernelY >= height || kernelX >= width) {
                                continue;
                            }
                            double kernelValue = distanceMap[kernelY][kernelX];
                            if(kernelValue > value) {
                                maxFound = true;
                            }
                        }
                    }
                    if(!maxFound) result.add(new Point(y, x));
                }
            }
        }
        System.out.println(result.size());
        return result;
    }

}
