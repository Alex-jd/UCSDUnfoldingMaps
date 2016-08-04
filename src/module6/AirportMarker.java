package module6;

import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PGraphics;

/** 
 * A class to represent AirportMarkers on a world map.
 *   
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMarker extends CommonMarker implements Comparable<AirportMarker> {
	//public static final float THRESHOLD_MODERATE = 5;
	public static List<SimpleLinesMarker> routes;
	private String COLOR = "BLACK";
	
	
	public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	
	}
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		
		pg.pushStyle();
		setFill(pg);
		//pg.fill(color);
		pg.ellipse(x, y, radius, radius);
		pg.popStyle();
		
		
	}
	
	public int compareTo(AirportMarker marker) {
		return this.getCode().compareTo(marker.getCode());
	}
	

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		 // show rectangle with title
		
		// show routes
		
		
	}
	
	private void setFill(PGraphics pg) {
		if (COLOR == "RED") {
			pg.fill(255, 0, 0);
		}
		else if (COLOR == "BLACK") {
			pg.fill(0, 0, 0);
		}
		
	}
	
	public void setStaticColor(String color) {
		COLOR = color;
	}
	
	public void getStaticColor() {
		System.out.println(COLOR);
	}
	
	public String getCode() {
		return (getProperty("code").toString()).replace("\"", "");
	}
	
}
