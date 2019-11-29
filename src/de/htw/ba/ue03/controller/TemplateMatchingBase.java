/**
 * @author Nico Hezel
 */
package de.htw.ba.ue03.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import de.htw.ba.ue03.Ue03_Vorlage;
import de.htw.ba.ue03.matching.TemplateMatcherFactory.Methods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;



public abstract class TemplateMatchingBase {
	
	@FXML
	private ImageView leftImageView;
	
	@FXML
	private ImageView rightImageView;
	
	@FXML
	private ComboBox<Methods> methodSelection;
	
	@FXML
	private Label runtimeLabel;
	
	private int[] cleanSrc;
		
	@FXML
	public void initialize() {
		methodSelection.getItems().addAll(Methods.values());
		methodSelection.setValue(Methods.Copy);
		methodSelection.setOnAction(this::applyTemplateMatching);

		loadImage(new File("textur-tapete.png"));
	}

	@FXML
	public void onOpenFileClick() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(".")); 
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images (*.jpg, *.png, *.gif)", "*.jpeg", "*.jpg", "*.png", "*.gif"));
		loadImage(fileChooser.showOpenDialog(null));
	}
	
	public void loadImage(File file) {		
		if(file != null) {
			leftImageView.setImage(new Image(file.toURI().toString()));
			cleanSrc = imageToPixel(leftImageView.getImage());
			applyTemplateMatching(null);
		}		
	}
	
	public int[] imageToPixel(Image image) {
		int width = (int)image.getWidth();
		int height = (int)image.getHeight();
		int[] pixels = new int[width * height];
		image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), pixels, 0, width);
		return pixels;
	}
	
	public int[] imageToPixel(BufferedImage image) {
		int width = (int)image.getWidth();
		int height = (int)image.getHeight();
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		return pixels;
	}
	
	public Image pixelToImage(int[] pixels, int width, int height) {
		WritableImage wr = new WritableImage(width, height);
		PixelWriter pw = wr.getPixelWriter();
		pw.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), pixels, 0, width);
		return wr;
	}
	
	@FXML
	public void applyTemplateMatching(ActionEvent event) {

		// no images loaded
		if(leftImageView.getImage() == null)
			return;
		
	  	// get image dimensions
    	int srcWidth = (int)leftImageView.getImage().getWidth();
    	int srcHeight = (int)leftImageView.getImage().getHeight();
    	int templateWidth = Ue03_Vorlage.template.getWidth();
    	int templateHeight = Ue03_Vorlage.template.getHeight();
    	int dstWidth  = srcWidth-templateWidth;
    	int dstHeight = srcHeight-templateHeight;

    	// get pixels arrays
    	int srcPixels[] = Arrays.copyOf(cleanSrc, cleanSrc.length);
    	int dstPixels[] = new int[dstWidth * dstHeight];
    	
		long startTime = System.currentTimeMillis();

		// get method choice 
		Methods currentMethod = methodSelection.getSelectionModel().getSelectedItem();
		try {
			runMethod(currentMethod, srcPixels, srcWidth, srcHeight, dstPixels, dstWidth, dstHeight);
		} catch (Exception e) { e.printStackTrace();
			e.printStackTrace();
		}
		
		rightImageView.setImage(pixelToImage(dstPixels, dstWidth, dstHeight));
		leftImageView.setImage(pixelToImage(srcPixels, srcWidth, srcHeight));
    	runtimeLabel.setText("Methode " + currentMethod + " ausgeführt in " + (System.currentTimeMillis() - startTime) + " ms");
	}

	public abstract void runMethod(Methods currentMethod, int[] srcPixels, int srcWidth, int srcHeight, int[] dstPixels, int dstWidth, int dstHeight) throws Exception;   

}
