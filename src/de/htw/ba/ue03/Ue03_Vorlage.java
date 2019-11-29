/**
 * @author Nico Hezel
 */
package de.htw.ba.ue03;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import de.htw.ba.ue03.controller.TemplateMatchingController;
import de.htw.ba.ue03.matching.TemplateMatcherFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Ue03_Vorlage extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent ui = new FXMLLoader(getClass().getResource("view/TemplateMatchingView.fxml")).load();
		Scene scene = new Scene(ui);
		stage.setScene(scene);
		stage.setTitle("Template Matching - Vorlage");
		stage.show();
	}
	
	public static BufferedImage template;

	public static void main(String[] args) throws IOException {
		
		// lade das Template
		template = ImageIO.read(Paths.get("template.png").toFile());
		int templateWidth = template.getWidth();
		int templateHeight = template.getHeight();
		int[] templatePixel = new int[templateWidth*templateHeight];
		template.getRGB(0, 0, templateWidth, templateHeight, templatePixel, 0, templateWidth);
		
		// Inject Factory
		TemplateMatchingController.factory = new TemplateMatcherFactory(templatePixel, templateWidth, templateHeight);
		
		launch(args);
	}
}