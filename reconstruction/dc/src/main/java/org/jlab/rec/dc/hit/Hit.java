package org.jlab.rec.dc.hit;

import org.jlab.rec.dc.GeometryLoader;

/**
 * A DC hit characterized by superlayer, layer, sector, wire number, and time.
 * The TDC to time conversion has been done.
 *
 * @author ziegler
 *
 */
public class Hit implements Comparable<Hit> {
    // class implements Comparable interface to allow for sorting a collection of hits by wire number values

    // constructors 
    /**
     *
     * @param sector (1...6)
     * @param superlayer (1...6)
     * @param layer (1...6)
     * @param wire (1...112)
     * @param time (for gemc output without digitization)
     * @param timeEr the error on the time
     */
    public Hit(int sector, int superlayer, int layer, int wire, double time, double docaEr, double B, int Id) {
        this._Sector = sector;
        this._Superlayer = superlayer;
        this._Layer = layer;
        this._Wire = wire;
        this._Time = time;
        this._DocaErr = docaEr;
        this._B = B;
        this._Id = Id;

    }

    private int _Sector;      							//	   sector[1...6]
    private int _Superlayer;    	 					//	   superlayer [1,...6]
    private int _Layer;    	 							//	   layer [1,...6]
    private int _Wire;    	 							//	   wire [1...112]

    private double _Time;      							//	   Reconstructed time, for now it is the gemc time
    private double _Doca;									//     Reconstructed doca, for now it is using the linear parametrization that is in  gemc 
    private double _DocaErr;      							//	   Error on doca

    private double _B;										// 	   B-field at hit location

    private int _Id;										//		Hit Id

    public int _lr;

    /**
     *
     * @return the sector (1...6)
     */
    public int get_Sector() {
        return _Sector;
    }

    /**
     * Sets the sector
     *
     * @param _Sector
     */
    public void set_Sector(int _Sector) {
        this._Sector = _Sector;
    }

    /**
     *
     * @return the superlayer (1...6)
     */
    public int get_Superlayer() {
        return _Superlayer;
    }

    /**
     * Sets the superlayer
     *
     * @param _Superlayer
     */
    public void set_Superlayer(int _Superlayer) {
        this._Superlayer = _Superlayer;
    }

    /**
     *
     * @return the layer (1...6)
     */
    public int get_Layer() {
        return _Layer;
    }

    /**
     * Sets the layer
     *
     * @param _Layer
     */
    public void set_Layer(int _Layer) {
        this._Layer = _Layer;
    }

    /**
     *
     * @return the wire number (1...112)
     */
    public int get_Wire() {
        return _Wire;
    }

    /**
     * Sets the wire number
     *
     * @param _Wire
     */
    public void set_Wire(int _Wire) {
        this._Wire = _Wire;
    }

    /**
     *
     * @return the time in ns
     */
    public double get_Time() {
        return _Time;
    }

    /**
     * Sets the time
     *
     * @param _Time
     */
    public void set_Time(double _Time) {
        this._Time = _Time;
    }

    public double get_Doca() {
        return _Doca;
    }

    public void set_Doca(double _Doca) {
        this._Doca = _Doca;
    }

    /**
     *
     * @return error on the time in ns (4ns time window used by default in
     * reconstructing simulated data)
     */
    public double get_DocaErr() {
        return _DocaErr;
    }

    /**
     * Sets the doca uncertainty
     *
     * @param _docaErr
     */
    public void set_DocaErr(double _docaErr) {
        this._DocaErr = _docaErr;
    }

    /**
     *
     * @return the ID
     */
    public int get_Id() {
        return _Id;
    }

    /**
     * Sets the hit ID. The ID corresponds to the hit index in the EvIO column.
     *
     * @param _Id
     */
    public void set_Id(int _Id) {
        this._Id = _Id;
    }

    /**
     *
     * @return region (1...3)
     */
    public int get_Region() {
        return (int) (this._Superlayer + 1) / 2;
    }

    /**
     *
     * @return superlayer 1 or 2 in region (1...3)
     */
    public int get_RegionSlayer() {
        return (this._Superlayer + 1) % 2 + 1;
    }

    /**
     *
     * @param arg0 the other hit
     * @return an int used to sort a collection of hits by wire number. Sorting
     * by wire is used in clustering.
     */
    @Override
    public int compareTo(Hit arg) {
        int return_val = 0;
        int CompSec = this.get_Sector() < arg.get_Sector() ? -1 : this.get_Sector() == arg.get_Sector() ? 0 : 1;
        int CompPan = this.get_Layer() < arg.get_Layer() ? -1 : this.get_Layer() == arg.get_Layer() ? 0 : 1;
        int CompPad = this.get_Wire() < arg.get_Wire() ? -1 : this.get_Wire() == arg.get_Wire() ? 0 : 1;

        int return_val1 = ((CompPan == 0) ? CompPad : CompPan);
        return_val = ((CompSec == 0) ? return_val1 : CompSec);

        return return_val;

    }

    /**
     *
     * @param layer layer number from 1 to 6
     * @param wire wire number from 1 to 112 calculates the center of the cell
     * as a function of wire number in the local superlayer coordinate system.
     */
    public double calcLocY(int layer, int wire) {

        // in old mc, layer 1 is closer to the beam than layer 2, in hardware it is the opposite
        //double  brickwallPattern = GeometryLoader.dcDetector.getSector(0).getSuperlayer(0).getLayer(1).getComponent(1).getMidpoint().x()
        //		- GeometryLoader.dcDetector.getSector(0).getSuperlayer(0).getLayer(0).getComponent(1).getMidpoint().x();
        double brickwallPattern = GeometryLoader.getDcDetector().getWireMidpoint(0, 1, 1).x
                - GeometryLoader.getDcDetector().getWireMidpoint(0, 0, 1).x;

        double brickwallSign = Math.signum(brickwallPattern);

        //center of the cell asfcn wire num
        //double y= (double)wire*(1.+0.25*Math.sin(Math.PI/3.)/(1.+Math.sin(Math.PI/6.)));
        double y = (double) wire * 2 * Math.tan(Math.PI / 6.);
        if (layer % 2 == 1) {
            //y = y-brickwallSign*Math.sin(Math.PI/3.)/(1.+Math.sin(Math.PI/6.));
            y = y - brickwallSign * Math.tan(Math.PI / 6.);
        }
        return y;

    }

    /**
     * identifying outoftimehits;
     */
    private boolean _OutOfTimeFlag;

    public void set_OutOfTimeFlag(boolean b) {
        _OutOfTimeFlag = b;
    }

    public boolean get_OutOfTimeFlag() {
        return _OutOfTimeFlag;
    }

    /**
     *
     * @return the cell size in a given superlayer
     */
    public double get_CellSize() {
        // fix cell size = w_{i+1} -w_{i}
        //double layerDiffAtMPln  = GeometryLoader.dcDetector.getSector(0).getSuperlayer(this.get_Superlayer()-1).getLayer(0).getComponent(0).getMidpoint().x()
        //             - GeometryLoader.dcDetector.getSector(0).getSuperlayer(this.get_Superlayer()-1).getLayer(0).getComponent(1).getMidpoint().x();
        double layerDiffAtMPln = GeometryLoader.getDcDetector().getWireMidpoint(this.get_Superlayer() - 1, 0, 0).x
                - GeometryLoader.getDcDetector().getWireMidpoint(this.get_Superlayer() - 1, 0, 1).x;

        //double cellSize = 0.5*Math.cos(Math.toRadians(6.)*Math.abs(layerDiffAtMPln*Math.cos(Math.toRadians(6.)));
        double cellSize = 0.5 * Math.abs(layerDiffAtMPln);

        return cellSize;
    }

    public double get_B() {
        return _B;
    }

    public void set_B(double _B) {
        this._B = _B;
    }

    /**
     *
     * @return print statement with hit information
     */
    public String printInfo() {
        String s = "DC Hit: ID " + this.get_Id() + " Sector " + this.get_Sector() + " Superlayer " + this.get_Superlayer() + " Layer " + this.get_Layer() + " Wire " + this.get_Wire() + " Time " + this.get_Time();
        return s;
    }

}
