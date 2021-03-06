package cnuphys.ced.cedview.dcxy;

import java.awt.Graphics;
import java.awt.geom.Point2D;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.magneticfield.swim.ASwimTrajectoryDrawer;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.fastmc.FastMCManager;
import cnuphys.lund.LundId;
import cnuphys.swim.SwimTrajectory;
import cnuphys.swim.SwimTrajectory2D;

public class SwimTrajectoryDrawer extends ASwimTrajectoryDrawer {

	private DCXYView _view;

	public SwimTrajectoryDrawer(DCXYView view) {
		_view = view;
		_markSectChanges = true;
	}

	/**
	 * Actual drawing method
	 * 
	 * @param g
	 *            the graphics context
	 * @param container
	 *            the base container
	 */
	@Override
	public void draw(Graphics g, IContainer container) {
		if (!ClasIoEventManager.getInstance().isAccumulating() && !FastMCManager.getInstance().isStreaming() && _view.isSingleEventMode()) {
			super.draw(g, container);
		}
	}

	/**
	 * Here we have a chance to veto a trajectory. For example, we may decide
	 * that the trajectory won't appear on this view (assuming a view owns this
	 * drawer) and so don't bother to compute it. The default implementation
	 * vetoes nothing.
	 * 
	 * @param trajectory
	 *            the trajectory to test.
	 * @return <code>true</code> if this trajectory is vetoed.
	 */
	@Override
	protected boolean veto(SwimTrajectory trajectory) {
		return false;
	}

	/**
	 * Just us the xy coordinates directly. Ignore z.
	 * 
	 * @param v3d
	 *            the 3D vector (meters)
	 * @param wp
	 *            the projected world point.
	 */
	@Override
	public void project(double[] v3d, Point2D.Double wp) {
		// convert to cm
		wp.setLocation(v3d[0] * 100, v3d[1] * 100);
	}

	@Override
	public void prepareForRemoval() {
	}

	@Override
	public void setDirty(boolean dirty) {
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public void setVisible(boolean visible) {
	}

	@Override
	public boolean acceptSimpleTrack(SwimTrajectory2D trajectory) {
		//this is a fugly hack. Check to see if it is hit based ot time based 
		//then check the display flags
		LundId lid = trajectory.getTrajectory3D().getLundId();
		int id = lid.getId();
		
		//FUGLY hack
		if ((id == -99) || (id == -100) || (id == -101)) { //time based
			return _view.showTB();
		}
		else if ((id == -199) || (id == -200) || (id == -201)) { //hitbased based
			return _view.showHB();
		}

		
		return true;
	}

}