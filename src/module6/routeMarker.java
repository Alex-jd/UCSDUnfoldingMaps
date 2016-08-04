package module6;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;

public class routeMarker extends SimpleLinesMarker implements Comparable <routeMarker>{
	
	/*public routeMarker (java.util.List<Location> locations ) {
		super(locations);
		
	}
	*/
	public routeMarker(java.util.List<Location> locations, java.util.HashMap<java.lang.String,java.lang.Object> properties) {
		super(locations, properties);
	}
	
	/*public routeMarker(Location startLocation, Location endLocation) {
		super(startLocation, endLocation);
	}

	public routeMarker(Feature route) {
		super(((ShapeFeature)route).getLocations(), route.getProperties());
	
	}
	*/
	
	/*
	public int compareTo(routeMarker marker) {
		return this.getCode().compareTo(marker.getCode());
	}
	*/
	
	public int compareTo(routeMarker marker) {
		//return this.getLineNumber().compareTo(marker.getLineNumber());
		if (this.getLineNumber() < marker.getLineNumber() ){
			return -1;
		}
		else if (this.getLineNumber() > marker.getLineNumber() ) {
			return 1;
		}
		return 0;
	}
	

	public String getCode() {
		return getProperty("sourceCode").toString();
	}
	
	public long getLineNumber() {
		return  Long.valueOf(getProperty("lineNumber").toString());
		
	}
}
