/**
 * OSM2GpsMid 
 *  
 *
 */
package osmToGpsMid;

import org.openstreetmap.gui.jmapviewer.Coordinate;

import osmToGpsMid.model.Bounds;

/**
 * 
 *
 */
public interface SelectionListener {
	
	public void regionMarked(Bounds bound);
	
	public void pointDoubleClicked(float lat, float lon);

	/**
	 * @param clickPoint
	 */
	public void addRouteDestination(Coordinate clickPoint);

}
