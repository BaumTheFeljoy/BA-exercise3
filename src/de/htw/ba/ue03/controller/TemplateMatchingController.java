package de.htw.ba.ue03.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

import de.htw.ba.ue03.matching.TemplateMatcher;
import de.htw.ba.ue03.matching.TemplateMatcherFactory;
import de.htw.ba.ue03.matching.TemplateMatcherFactory.Methods;

public class TemplateMatchingController extends TemplateMatchingBase {

	// wird von außen Injected
	public static TemplateMatcherFactory factory;

	@Override
	public void runMethod(Methods method, int[] srcPixels, int srcWidth, int srcHeight, 
										  int[] dstPixels, int dstWidth, int dstHeight) throws Exception {
		
		TemplateMatcher matcher = factory.getTemplateMatcher(method);
		
		// berechne die DistanceMap
		double[][] distanceMap = matcher.getDistanceMap(srcPixels, srcWidth, srcHeight);
		
		// zeichne die DistanceMap als RGB Bild
		matcher.distanceMapToIntARGB(distanceMap, dstPixels, dstWidth, dstHeight);
		
		// finde die Maximas in der DistanceMap
		List<Point> maximas = matcher.findMaximas(distanceMap);
		
		// zeichne die Maximas ein
		int templateWidth = factory.getTemplateWidth();
		int templateHeight = factory.getTemplateHeight();
		
		BufferedImage bufferedImage = new BufferedImage(srcWidth, srcHeight, BufferedImage.TYPE_INT_ARGB);
    	bufferedImage.setRGB(0, 0, srcWidth, srcHeight, srcPixels, 0, srcWidth);
    	Graphics2D g2d = bufferedImage.createGraphics();
    	g2d.setColor(Color.RED);
    	
    	for (Point point : maximas)
    	{
    		int xMin = (int) point.getX();
    		int yMin = (int) point.getY();
    		
    		int xMax = xMin + templateWidth;
			int yMax = yMin + templateHeight;
			
	    	g2d.drawLine(xMin, yMin, xMax, yMin);
	    	g2d.drawLine(xMax, yMin, xMax, yMax);
	    	g2d.drawLine(xMax, yMax, xMin, yMax);
	    	g2d.drawLine(xMin, yMax, xMin, yMin);
		}
    	
    	g2d.dispose();
		srcPixels = bufferedImage.getRGB(0, 0, srcWidth, srcHeight, srcPixels, 0, srcWidth);
	}

}
