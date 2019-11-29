package de.htw.ba.ue03.matching;


/**
 * Erzeugt TemplateMatcher je nachdem welche Methode gefordert ist
 * 
 * @author Nico
 *
 */
public class TemplateMatcherFactory {

	public static enum Methods {
		Copy,
		Absolute, 
		Maximum, 
		Square, 
		CorreCoef
	};
	
	protected int[] templatePixel; 
	protected int templateWidth;
	protected int templateHeight;
	
	public TemplateMatcherFactory(int[] templatePixel, int templateWidth, int templateHeight) {
		this.templatePixel = templatePixel;
		this.templateWidth = templateWidth;
		this.templateHeight = templateHeight;
	}
	
	public int getTemplateWidth() {
		return templateWidth;
	}

	public int getTemplateHeight() {
		return templateHeight;
	}

	/**
	 * Gibt den passenden Template Matcher zurück.
	 * TODO: füge die eigenen Matcher hinzu
	 * 
	 * @param method
	 * @return
	 */
	public TemplateMatcher getTemplateMatcher(Methods method) {
		switch (method) {
			case Absolute:
				return new TemplateMatcherAbsolute(templatePixel, templateWidth, templateHeight);
			case CorreCoef:
				return new TemplateMatcherCorrCoef(templatePixel, templateWidth, templateHeight);
			case Maximum:
				// return xxx;
			case Square:
				return new TemplateMatcherSquare(templatePixel, templateWidth, templateHeight);
			case Copy:
			default:
				return new TemplateMatcherCopy(templatePixel, templateWidth, templateHeight);
		}
	}
}
