package gmerg.beans;

import gmerg.entities.submission.Probe;
import gmerg.utils.FacesUtil;
import gmerg.assemblers.MaProbeAssembler;

public class MaProbeBean {
    private boolean debug = false;

	private Probe maProbe;
	private MaProbeAssembler assembler;
	
	public MaProbeBean() {
	if (debug)
	    System.out.println("MaProbeBean.constructor");

		String probeId = FacesUtil.getRequestParamValue("probe");
		String maprobeId = FacesUtil.getRequestParamValue("maprobe");
		String gudmapId = FacesUtil.getRequestParamValue("gudmapid");

	    System.out.println("MaProbeBean probeId="+probeId+" maprobeId="+maprobeId+" gudmapId="+gudmapId);
		
		if (maprobeId != null && 8 < maprobeId.length())
			maprobeId = maprobeId.substring(8);
		
		assembler = new MaProbeAssembler();
		maProbe = assembler.getData(probeId, maprobeId);
	}
	
	public Probe getMaProbe() {
		return maProbe;
	}
	
	public void setMaProbe(Probe probe){
		maProbe = probe;
	}
	

}
