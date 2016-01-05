
public class Neighbour implements Comparable<Neighbour>{

	private int index;
	private double sim;
	private int intersection;
	
	Neighbour(int index, double sim, int intersection){
		this.index = index;
		this.sim = sim;
		this.intersection = intersection;
	}
	
	public double getSim(){
		return sim;
	}
	
	public int getIndex(){
		return index;
	}
	
	public int getIntersection(){
		return intersection;
	}

	
	@Override
	public int compareTo(Neighbour o) {
		// Vergleich zunächst die Größe der Sim, anschließend die Anzahl der
		// Film-Schnittmengen.
		
		if (this.sim == o.getSim()){
			if (this.intersection < o.getIntersection()){
				return 1;
			}else if (this.intersection == o.getIntersection()){
				return 0;
			}else{
				return -1;
			}
		}else if (this.sim < o.getSim())  {
			return 1;
		}else{
			return -1;
		}
		
		
		// Alternatives Rating, in dem Anzahl der Film-Schnittmengen besser eingeht.
		// Bei Gleichheit zählt Sim
		/*
		if ((this.sim * this.intersection) < (o.getSim()*o.getIntersection())){
			return 1;
		}else if ((this.sim * this.intersection) == (o.getSim()*o.getIntersection())){
			if (this.sim < o.getSim()){
				return 1;
			}else if (this.sim == o.getSim()){
				return 0;
			}else{
				return -1;
			}
		}else{
			return -1;
		}
		*/
		// TODO Auto-generated method stub
		
	}
	
	
	
}
