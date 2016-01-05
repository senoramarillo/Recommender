import java.util.ArrayList;
import java.util.List;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Recommender rec = new Recommender("u1.base", "u.item");
		
		
		int user = 1;
		int neighbourhood_size = 525;
		int recommendations = 10;
		double threshold = 0.2;
		
		// An dieser Stelle kann die Nachbarliste nochmals generiert werden, um sie in der Main Klasse auszugeben.
		/* 
		List<Neighbour> neighbours;
		neighbours = rec.getOrderedNeighbours(user, neighbourhood_size);
		
		if (neighbours.size()<neighbourhood_size){
			neighbourhood_size = neighbours.size();
		}
		
		System.out.println("TOP "+neighbourhood_size+" Nachbarn: ");
		for (int i=0;i<neighbourhood_size;i++){
			System.out.println(neighbours.get(i).getIndex()+":\t"+neighbours.get(i).getSim()+"\tbei "+neighbours.get(i).getIntersection()+" Überschneidungen");
		}
		System.out.println("");
		*/
		
		
		System.out.println("Unsere Empfehlungen fuer Benutzer "+user+":");
		// 1. Parameter: User, der Empfehlung erhalten soll
		// 2. Parameter: Größe der Nachbarschaft
		// 3. Parameter: Anzahl der Empfehlungen
		// 4. Parameter: Grenzwert der Sim-Werte
		
		List<Movie> recomms = rec.getRecommendations(user, neighbourhood_size, recommendations, threshold);	
		for (int i=0; i<recomms.size();i++){
			System.out.println((i+1)+". Empfehnung: "+recomms.get(i).getName()+" (ID: "+recomms.get(i).getIndex()+") - Predicted Rating: "+recomms.get(i).getRatings());
		}
	

		
		
		TestRecommender mae = new TestRecommender("u1.test");	
		// Ausgabe für den besten berechneten Fall:
		System.out.println("");
		System.out.println("MAE unter Verwendung NS: 525 und Threshold 0.2: "+mae.testPrediction(rec, 525, 0.2));

		
		/* Diese Funktion misst den MAE unter Angabe vom Threshold und der Neighbour-Size
		 
		double search_best = 1;
		double current_predict = 1;
		String search_result = "";
		TestRecommender mae = new TestRecommender("C:/Users/jakob/git/recommender/Recommender/src/u1.test");	
		for (int n=25;n<=700;n=n+25){
			for (double t=0.0;t<=0.8;t=t+0.1){
				current_predict = mae.testPrediction(rec, n, t);
				System.out.println(n+";"+t+";"+current_predict);
				if (current_predict < search_best){
					search_best = current_predict;
					search_result = n+";"+t+";"+current_predict;
				}
			}
		}
		System.out.println("\n");
		System.out.println("Best Predict: "+search_result);
		*/ 

	}

}
