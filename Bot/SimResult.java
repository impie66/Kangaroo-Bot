package Bot;

public class SimResult {
	boolean canWin;
	double simScore;
	int fs;
	
	SimResult(Boolean won,  double sc, int fogSimmed){
		this.canWin = won;
		this.simScore = sc;
		this.fs = fogSimmed;
	}
	
	
	String statsToString(){
		return new String("Score: " + this.simScore + " Can we win?: " + this.canWin);
		// https://www.youtube.com/watch?v=Qskg6yvn8vc
		// https://www.youtube.com/watch?v=XQEBzauVIlA
	}
	
	
}
