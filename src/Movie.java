
public class Movie implements Comparable<Movie>{

	private int index;
	private double predicted_rating;
	private String name;
	
	Movie(int index, double total_ratings){
		this.index = index;
		this.predicted_rating = total_ratings;
	}
	
	public double getRatings(){
		return predicted_rating;
	}
	
	public int getIndex(){
		return index;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

	@Override
	public int compareTo(Movie o) {
		// TODO Auto-generated method stub
		return Double.compare(o.getRatings(), this.predicted_rating);
	}
	
	
	
}
