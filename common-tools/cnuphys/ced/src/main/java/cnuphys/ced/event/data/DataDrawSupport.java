package cnuphys.ced.event.data;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import cnuphys.bCNU.graphics.SymbolDraw;
import cnuphys.splot.plot.X11Colors;

public class DataDrawSupport {
	
	public static final int HB_CROSS  = 0;
	public static final int TB_CROSS  = 1;
	public static final int SVT_CROSS = 2;
	public static final int BMT_CROSS = 3;
	
	private static final Color TRANSYELLOW = new Color(255, 255, 0, 240);
	private static final Color TRANSORANGE = X11Colors.getX11Color("dark orange", 240);
	private static final Color TRANSGREEN = X11Colors.getX11Color("lawn green", 250);

	public static Color transColors[] = { TRANSYELLOW, TRANSORANGE, TRANSYELLOW, TRANSGREEN };
	public static String prefix[] = { "HB ", "TB ", "SVT ", "BMT "};
	

	public static final int CROSSHALF = 6; // pixels



	private static final Color gemc_hit_fillColor = new Color(255, 255, 0, 196);
	private static final Color gemc_hit_lineColor = X11Colors
			.getX11Color("dark red");

	private static final Color rec_hit_fillColor = new Color(0, 255, 255, 196);
	private static final Color rec_hit_lineColor = X11Colors
			.getX11Color("dark red");

	public static final String[] EC_PLANE_NAMES = { "?", "Inner", "Outer" };
	public static final String[] EC_VIEW_NAMES = { "?", "U", "V", "W" };


	/**
	 * Draw a GEMC truth hit at the given screen location
	 * 
	 * @param g
	 *            the graphics context
	 * @param pp
	 *            the screen location
	 */
	public static void drawGemcHit(Graphics g, Point pp) {
		SymbolDraw.drawDavid(g, pp.x, pp.y, 4, gemc_hit_lineColor, gemc_hit_fillColor);
//		g.setColor(gemc_hit_fillColor);
//		g.fillOval(pp.x - 3, pp.y - 3, 6, 6);
//		// now the cross
//		g.setColor(gemc_hit_lineColor);
//		g.drawOval(pp.x - 3, pp.y - 3, 6, 6);
//		g.drawLine(pp.x - 3, pp.y, pp.x + 3, pp.y);
//		g.drawLine(pp.x, pp.y - 3, pp.x, pp.y + 3);
	}

	/**
	 * Draw a reconstructed hit at the given screen location
	 * 
	 * @param g
	 *            the graphics context
	 * @param pp
	 *            the screen location
	 */
	public static void drawReconHit(Graphics g, Point pp) {
		// now the cross
		g.setColor(rec_hit_lineColor);
		g.drawLine(pp.x - 4, pp.y - 4, pp.x + 4, pp.y + 4);
		g.drawLine(pp.x - 4, pp.y + 4, pp.x + 4, pp.y - 4);
		g.setColor(rec_hit_fillColor);
		g.fillRect(pp.x - 3, pp.y - 3, 6, 6);
		g.setColor(rec_hit_lineColor);
		g.drawRect(pp.x - 3, pp.y - 3, 6, 6);
	}
	
	/**
	 * Draw a reconstructed cross
	 * @param g the graphics context
	 * @param x the x location
	 * @param y the y location
	 * @param mode the mode (HB, TB, etc)
	 */
	public static void drawCross(Graphics g, int x, int y, int mode) {
		SymbolDraw.drawOval(g, x, y, CROSSHALF, CROSSHALF, Color.black,
				transColors[mode]);
		SymbolDraw.drawCross(g, x, y, CROSSHALF, Color.black);
	}

	/**
	 * Get a string representing the id array
	 * 
	 * @param ids
	 *            variable length ids
	 * @return a string representing the id array
	 */
	public static String getIDString(int... ids) {
		if ((ids == null) || (ids.length < 1)) {
			return "???";
		}
		StringBuilder sb = new StringBuilder(50);
		sb.append("]");
		for (int i = 0; i < ids.length; i++) {
			sb.append(ids[i]);
			if (i < (ids.length - 1)) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}
	

}
