import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class TestRecommender {
	
	private int ARRAYSIZE = 2000;
	private int users_movies[][] = new int[ARRAYSIZE][ARRAYSIZE];
	
	private void importData(String path){
		try {
			// Liest die Voting-Datei ein und schreibt die Ergebnisse in eine 2D-Matrix, wobei
			// der erste Index für die User ID steht und der zweite Index für die Film ID.
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			String split_line[] = null;
	        while((line = br.readLine()) != null) {
	        	split_line = line.split("\t");
	        	users_movies[Integer.parseInt(split_line[0])][Integer.parseInt(split_line[1])] = Integer.parseInt(split_line[2]);
	        }
	        br.close();
	        
	        
	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public TestRecommender(String path){
		importData(path);
	}
	
	public double testPrediction(Recommender rec, int neighbourhood_size, double theshold){
		List<Neighbour> neighbours;
		double nenner = 0;
		double zaehler = 0;
		double prediction = 0;
		for (int user=0;user<ARRAYSIZE;user++){
			neighbours = rec.getOrderedNeighbours(user, neighbourhood_size);
			for (int movie=0;movie<ARRAYSIZE;movie++){
				if (users_movies[user][movie] != 0){
					prediction = rec.getPrediction(user, movie, neighbours, theshold);
					if (prediction > 0){
						nenner++;
						zaehler = zaehler + Math.abs(users_movies[user][movie] - rec.getPrediction(user, movie, neighbours, theshold));		
					}
								
				}
				
			}
		}
		return (zaehler/nenner);
		
	}
	

}
