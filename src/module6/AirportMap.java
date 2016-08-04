package module6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMap extends PApplet {
	
	private static final long serialVersionUID = 1L;
	private UnfoldingMap map;
	List<Marker> airportList;
	List<Marker> routeList;

	
	List<Marker> seeableRouteList = new ArrayList<Marker>();
	List<Marker> seeableAirPortList;
		
	private Marker lastClicked;
	private Marker lastSelected;



	public void setup() {
		// setting up PAppler
		size(800,600, OPENGL);
		
		// setting up map and default events
		map = new UnfoldingMap(this, 50, 50, 750, 550, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// get features from airport data
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		
		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
		HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
		
		// create markers from features
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
	
			//m.setRadius(10);
			airportList.add(m);
			
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
			
		
		}
		
		
		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));//Type ShapeFeatuer and HashMap
				route.addLocation(airports.get(dest));
			}
			
			//SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
			routeMarker sl = new routeMarker(route.getLocations(), route.getProperties());
			//System.out.println(sl);
			//routeMarker rm = (routeMarker) sl;
		
			//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
			//sl.setHidden(true);
			routeList.add(sl);
		}
		
		
		
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		map.addMarkers(routeList);
		
		
		
		map.addMarkers(airportList);
		//sortAndPrintAir(20000);
		//sortAndPrintRoute(2);
		
				
	}
	
	public void draw() {
		background(0);
		map.draw();
		
	}
	
	private void sortAndPrintAir(int numToPrint) {
		Marker listToArray[] = new Marker[airportList.size()];
		listToArray = airportList.toArray(listToArray);
		Arrays.sort(listToArray);
		for (Marker temp : listToArray) {
			AirportMarker temp1 = (AirportMarker) temp;
			System.out.println( temp1.getProperties() );
			numToPrint = numToPrint - 1;
			if (numToPrint <= 0) {
				break;
			}
		}
		
	}
	
	private void sortAndPrintRoute(int numToPrint) {
		Marker listToArray[] = new Marker[routeList.size()];
		listToArray = routeList.toArray(listToArray);
		Arrays.sort(listToArray);
		for (Marker temp : listToArray) {
			routeMarker temp1 = (routeMarker) temp;
			System.out.println( temp1.getProperties() );
			numToPrint = numToPrint - 1;
			if (numToPrint <= 0) {
				break;
			}
		}
		
	}
	/*public void mouseMoved() {
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		//selectMarkerIfHover(airportList);
		
	}
	*/
	/*private void selectMarkerIfHover(List<Marker> markers) {
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker)m;
			//System.out.println(marker.isInside(map,  mouseX, mouseY) );
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
		
	}
	*/
	@Override
	public void mouseClicked() {
		if (lastClicked != null) {
			unhideRoutlist();
			unhideAirportList();
			lastClicked = null;
			//seeableRouteList = new ArrayList<Marker>();
		}
		else if (lastClicked == null) 
		{
			checkClickOnMarker ();
		}
		
	}
	
	private void unhideRoutlist() {
		for(Marker marker : routeList) {
			marker.setHidden(false);
		}
	}
	
	private void unhideAirportList() {
		for(Marker marker : airportList) {
			marker.setHidden(false);
			((AirportMarker) marker).setStaticColor("BLACK");
		}
	}
	
	private void checkClickOnMarker () {
		if (lastClicked != null) return;
		for (Marker m : airportList) {
			if (!m.isHidden() && m.isInside(map, mouseX, mouseY)) {
				lastClicked = m;
				//m.setHidden(false);
				drawLocalRoutes(m);
				setMarkerHidden("hideRouteList", null);
				setMarkerHidden("hideAirportList", m);
				seeableRouteList = new ArrayList<Marker>();
				/*
				int result1[] = countOfTrueFalse(routeList);
				System.out.println("\nroutelist " +result1[0] + " " + result1[1]);
				int result[] = countOfTrueFalse(airportList);
				System.out.println("airports " +result[0] + " " + result[1] + "\n##########################");
				*/
							
				return;
			}
			
		}
	}
	
	
	private void drawLocalRoutes (Marker airMarker) {


		for (Marker codeRoute :  routeList) {
			if ( (codeRoute.getProperty("sourceCode").toString()).equals( ((AirportMarker)airMarker).getCode())   ) {
						seeableRouteList.add(codeRoute);
			}
		}
		//System.out.println("seeableSize " +seeableRouteList.size());
		
	}
	
	public void setMarkerHidden (String keyToSelect, Marker clickedMarker) {
		if (seeableRouteList != null) {

		if (keyToSelect == "hideRouteList") {
			int countFalse = 0;
			int countTrue = 0;
			hideRouterList ();
			for (Marker seealbeMarkers : seeableRouteList) {
				long lineNumber = ((routeMarker) seealbeMarkers).getLineNumber();
				int i = 0;
				if (routeList != null) {
					int low = 0, high = routeList.size(), mid;
					while (low <= high) {
						mid = (low + high) / 2;
						i ++;
						if ( lineNumber == ((routeMarker) routeList.get(mid)).getLineNumber() ) {
							((routeMarker) routeList.get(mid)).setHidden(false);
							break;
						} else {
							if ( lineNumber < ((routeMarker) routeList.get(mid)).getLineNumber() ) {
								high = mid;
							} else {
								low = mid + 1;
							}
						}
					}
					System.out.println("itteration = " + i);
					//System.out.println("routerlistSize = " + routeList.size());
				}
				//return i;
			}
			/*for (Marker routeMarker :  routeList) {
				for (Marker seealbeMarkers : seeableRouteList) {
					//if (seealbeMarkers.equals(routeMarker) && routeMarker.isHidden()) {
					if ( ( ((routeMarker) seealbeMarkers).getLineNumber() == ((routeMarker) routeMarker).getLineNumber() ) && routeMarker.isHidden() ) {
						routeMarker.setHidden(false);
						countFalse ++;
					}
					else 
					{
						countTrue ++;
					}
					
					
				}
			}
			//System.out.println("routeList in setMarkerHidden false " + countFalse+ "; True " + countTrue);
			
			int result1[] = countOfTrueFalse(routeList);
			//System.out.println("routelist " +result1[0] + " " + result1[1]);
		}
		*/
		}
		else if (keyToSelect == "hideAirportList") {
			int countFalse = 0;
			int countTrue = 0;
			hideAirportList ();
			for (Marker airMarker : airportList) {
				for (Marker seeableMarkers : seeableRouteList) {
					if ( (seeableMarkers.getProperty("destinationCode")).toString().equals( ((AirportMarker)airMarker).getCode() )  ) {
						((AirportMarker) airMarker).setHidden(false);
						((AirportMarker) airMarker).setStaticColor("RED");
						//((AirportMarker) airMarker).getStaticColor();
					
						countFalse ++;

					}
					else 
					{
						//((AirportMarker) airMarker).getStaticColor();
						countTrue ++;
					}
					
				}	
			}
		clickedMarker.setHidden(false);
		//((AirportMarker) clickedMarker).getStaticColor();
		//((AirportMarker) clickedMarker).setStaticColor("BLACK");
		
		//System.out.println("airportList in setMarkerHidden false " + countFalse+ "; True " + countTrue);
		
		int result[] = countOfTrueFalse(airportList);
		//System.out.println("airports " +result[0] + " " + result[1]);
		}
		}
		
	}
	
	private int[] countOfTrueFalse(List<Marker> markerList) {
		int falseCount = 0;
		int trueCount = 0;
		for (Marker temp : markerList) {
			if (temp.isHidden()) {
				trueCount ++;
			}
			else {
				falseCount ++;
			}
			
		}
		return new int[] {falseCount, trueCount} ;
	}
	
	private void hideRouterList () {
		for (Marker temp : routeList) {
			temp.setHidden(true);
		}
		
	}
	
	private void hideAirportList () {
		for (Marker temp : airportList) {
			temp.setHidden(true);
		}
	}
	
	
}
