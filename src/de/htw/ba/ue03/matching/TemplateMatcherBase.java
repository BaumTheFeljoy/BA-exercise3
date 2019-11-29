package de.htw.ba.ue03.matching;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Basis Klasse mit einigen getter/setter Funktionen
 * 
 * @author Nico
 *
 */
public abstract class TemplateMatcherBase implements TemplateMatcher {

	protected int[] templatePixel; 
	protected int templateWidth;
	protected int templateHeight;
	
	public TemplateMatcherBase(int[] templatePixel, int templateWidth, int templateHeight) {
		this.templatePixel = templatePixel;
		this.templateWidth = templateWidth;
		this.templateHeight = templateHeight;
	}
	
	@Override
	public int getTemplateWidth() {
		return templateWidth;
	}

	@Override
	public int getTemplateHeight() {
		return templateHeight;
	}

	@Override
	public List<Point> findMaximas(double[][] distanceMap) {
		return new ArrayList<>();
	}

	int capValue(int value) {
		value = Math.max(value, 0);
		value = Math.min(value, 255);
		return value;
	}
}
