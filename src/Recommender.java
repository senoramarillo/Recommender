import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * users_movies
 * Zuordnung zwischen Usern und Filmen und den jeweiligen Wertungen
 * int array[userID][movieID]
 *  
 * 				   movie 1	   movie 2	    movie 3		movie 4
 * ------------|------------|-----------|------------|------------|
 * user 1		     2			 0           4             5
 * user 2		     4			 0			 2			   1
 * 
 * ---------------------------------------------------------------
 * 
 * users_users
 * Berechnung mit der Pearson Gleichung der Ähnlichkeit der User
 * double array[userID][userID]
 * 
 * 				   user 1	   user 2	    user 3		user 4
 * ------------|------------|-----------|------------|------------|
 * user 1		     1			 0.99         -99         0.52
 * user 2		     0.99		 1			  0.5		  0.86
 *
 *
 * moviename
 * Zuordnung von Movie ID zu Filmtitel
 * String array[movieID]
 * 
 * Movie ID				Film Titel
 * ------------|---------------------------------
 *      1		   	Toy Story
 *      2			Terminator	
 *
 *
 * Grundprinzip: 	- Nehme die Votings sowie den Durchschnittsvoting-Wert eines Users und
 * 					baue die users_users Matrix auf.
 * 					- Suche zu einem gegebenen User in der Zeile die größten Werte heraus,
 * 					packe Sie in eine Liste, sortiere diese Liste und gebe die gewünschte
 * 					Anzahl an Usern zurück (Neighbourhood). 
 * 					- Ermittel alle Filme der erhaltenen Nachbarn, die der User noch nicht gesehen hat,
 * 					addiere die jeweiligen Wertungen dieser auf. 
 * 					- Sortiere diese Wertungsliste ebenfalls und gebe die gewünschte Anzahl an
 * 					Empfehlungen zurück. 
 *
 */


public class Recommender {
	private int ARRAYSIZE = 2000;
	private int MOVIE_ARRAYSIZE = 2000;
	// Speichert die Zuordnung von Votes je Film pro User. Der 
	// erste Index gibt den user an, der zweite Index den Film.
	private int users_movies[][] = new int[ARRAYSIZE][ARRAYSIZE];
	
	// Speichert die Sim zwischen verschiedenen Usern. Daten werden
	// redundant gespeichert. Hier könnte eine Optimierung durch-
	// geführt werden, in dem nur die halbe Matrix befüllt und 
	// gelesen wird
	private double users_users[][] = new double[ARRAYSIZE][ARRAYSIZE];
	
	// Hält die Zuordnung zwischen Movie-ID und Name
	private String moviename[] = new String[MOVIE_ARRAYSIZE];
	
	public Recommender(String path1, String path2){
		importData(path1, path2);
		generateSimTable();
		
	}
	
	// Liest zeilenweise die angegebene Datei und speichert die einzelnen
	// Votings in die users_movies Matrix.
	// Path1 ist dabei die Voting-Datei, Path2 die Zuordnung von Filmen und Namen
	private void importData(String path1, String path2){
		try {
			// Liest die Voting-Datei ein und schreibt die Ergebnisse in eine 2D-Matrix, wobei
			// der erste Index für die User ID steht und der zweite Index für die Film ID.
			BufferedReader br = new BufferedReader(new FileReader(path1));
			String line;
			String split_line[] = null;
	        while((line = br.readLine()) != null) {
	        	split_line = line.split("\t");
	        	users_movies[Integer.parseInt(split_line[0])][Integer.parseInt(split_line[1])] = Integer.parseInt(split_line[2]);
	        }
	        
	        // Liest die Filmnamen ein, wobei der Index für die jeweilige Film-ID steht.
	        br = new BufferedReader(new FileReader(path2));
	        while((line = br.readLine()) != null) {
	        	split_line = line.split("\\|");
	        	moviename[Integer.parseInt(split_line[0])]= split_line[1];
	        }
	        
	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// Berechnet das arithmetische Mittel aller abgegebenen Votings eines
	// gegebenen Benutzers.
	public double getMeanOfUser(int user){
		int numberOfVotes = 0;
		int absolut = 0;
		for (int i=0;i<ARRAYSIZE;i++){
			if (users_movies[user][i] > 0){
				absolut = absolut + users_movies[user][i];
				numberOfVotes++;
			}
		}
		// Falls User nicht existiert und somit keine Votes existieren
		if (numberOfVotes == 0){
			return 0;
		}else{
			return ((double)absolut/(double)numberOfVotes);
		}
	}
	
	
	// Generiert die Sim Matrix.
	private void generateSimTable(){
		// Doppelt verschachtelte for-Schleife prüft jeden
		// User mit jedem User (folglich doppelt)
		for (int userA=1;userA<ARRAYSIZE;userA++){
			for (int userB=1;userB<ARRAYSIZE;userB++){
				// intersection enthält die Schnittmenge der Filme, für die
				// beide Benutzer ein Voting abgegeben haben.
				ArrayList<Integer> intersection = getIntersectionOf(userA, userB);
				double meanUserA = getMeanOfUser(userA);
				double meanUserB = getMeanOfUser(userB);
				double zaehler = 0;
				double nenner1 = 0;
				double nenner2 = 0;
				// Da mit "0" nicht abgestimmt werden kann, wird überprüft, ob 
				// eine der beiden Personen als Mittel der Votings 0 hat, was gleich-
				// bedeutend damit ist, dass es den User nicht gibt bzw. er keine
				// Votings abgegeben hat. In diesem Fall wird der Sim-Wert auf -99
				// gesetzt.
				if ((meanUserA != 0) && (meanUserB != 0) && (intersection.size() > 0)){
					int voteUserA = 0;
					int voteUserB = 0;
					// Für jeden Film, den beide User bewertet haben, wird der nach-
					// folgende Code ausgeführt und die Zähler und Nenner berechnet.
					for (int item=0;item<intersection.size();item++){
						voteUserA = users_movies[userA][intersection.get(item)];
						voteUserB = users_movies[userB][intersection.get(item)];
						
						zaehler = zaehler + ((voteUserA - meanUserA)*(voteUserB - meanUserB));
						nenner1 = nenner1 + Math.pow((voteUserA - meanUserA),2);
						nenner2 = nenner2 + Math.pow((voteUserB - meanUserB),2);
					}
					// Damit der gesamte Nenner 0 werden kann, muss ein Nutzer existieren, dessen
					// gesamten Votings genau seinem Durchschnitt entsprechen. Problem: Nutzer
					// die genau dieselben Wertungen abgeben haben sim=0
					// TODO: Wie wird mit solchen Usern umgegangen? 
					if ((zaehler == 0) || (nenner1 == 0) || (nenner2 == 0)){
						users_users[userA][userB] = 0;
					}else{
						users_users[userA][userB] = (zaehler/((Math.sqrt(nenner1)*Math.sqrt(nenner2))));
					}
					
				}else{
					users_users[userA][userB] = -99;
				}
				
			}
			
		}
	}
	
	
	// Berechnet die Schnittmenge an Filmen, für die beide Personen abgestimmt haben
	private ArrayList<Integer> getIntersectionOf(int userA, int userB){
		ArrayList<Integer> intersection = new ArrayList<Integer>();
		for (int i=0;i<ARRAYSIZE;i++){
			// Überprüft für jedes Voting eines Films i von UserA und UserB. Wenn
			// beide dort ein Voting abgegeben haben, kommt der Film in die Schnittmenge
			if ((users_movies[userA][i] != 0) && (users_movies[userB][i] != 0)){
				intersection.add(i);
			}
		}
		return intersection;
	}
	
	
	public double getSimBetween(int userA, int userB){
		return users_users[userA][userB];
	}
	
	
	// Diese Funktion Erzeugt eine Liste aller User, die einen Ähnlichkeitswert zwischeneinander
	// haben und sortiert sie nach einem definierten
	// Algorithmus (siehe Neighbour-Klasse), z.B. nach Sim bezüglich eines angegebenen Users.
	// Schließlich wird eine Liste angegebener Größe zurückgegeben.
	public List<Neighbour> getOrderedNeighbours(int user, int neighbourhood){
		ArrayList<Neighbour> neighbours = new ArrayList<Neighbour>();
		for (int i=0;i<ARRAYSIZE;i++){
			// Überspringt den Eintrag, bei dem der User mit sich selbst
			// vergleichen wurde.
			if (i != user){
				if (users_users[user][i] != -99){
				neighbours.add(new Neighbour(i, users_users[user][i], getIntersectionOf(user, i).size()));
				}
			}
		}
		Collections.sort(neighbours);
		// Falls weniger Nachbarn gefunden werden konnten, als gewünscht
		if (neighbours.size() < neighbourhood){
			neighbourhood = neighbours.size();
		}
		// Info Sublist: FromIndex is inklusiv, ToIndex ist exclusiv.
		return neighbours.subList(0, neighbourhood);
	}
	
	
	// Addiert die Bewertungen je Film aus einer Nachbarschaft auf, erzeugt eine Film-
	// Liste und sortiert diese nach den addierten Bewertungen und gibt dann die Anzahl
	// angegebener Empfehlungen zurück.
	public List<Movie> getRecommendations (int user, int neighbourhood, int amount, double threshold){
		List<Neighbour> neighbours = getOrderedNeighbours(user, neighbourhood);
		
		// Falls die Liste in  getOrderedNeighbours ggf. kleiner durch einen zu großen 
		// neighbourhood Parameter geworden ist. Andernfalls ist neighbourhood = neighbours.size(),
		// da getOrderedNeighbours bereits dafür sorgt.
		neighbourhood = neighbours.size();
		
		// Berechne zu jedem Film die voraussichtliche Bewertung des Benutzers. Nehme diese Liste, um eine
		// Empfehlung auszusprechen. Eine Gewichtung findet an dieser Stelle nicht statt!
		ArrayList<Movie> movies = new ArrayList<Movie>();
		for (int movie=0;movie<ARRAYSIZE;movie++){
			if (users_movies[user][movie] == 0){
				movies.add(new Movie(movie, getPrediction(user, movie, neighbours, threshold)));
			}
		}
		
		// Das Movie Array muss entsprechend sortiert werden, ohne die Zuordung auf den Index zu verlieren
		Collections.sort(movies);
		
		// Eine Teilliste mit der geforderten Anzahl wird zurückgegeben, wobei die
		// Namen der Movie IDs nun zugeordnet werden
		return matchMovieNames(movies.subList(0, amount));
	}
	
	
	// Gibt eine Vorhersage über das User-Rating eines bestimmten Filmes unter Angabe der Nachbar-
	// schaft und eines SIM-Grenzwertes. Es gehen nur Nachbarn in die Berechnung ein, die bereits
	// eine Bewertung zu dem Film abgegeben haben.
	// Der Nachbar, der in die Berechnung mit eingeht, muss den SIM-Grenzwert erfüllen und
	// und für den konkreten Film abgestimmt haben.
	public double getPrediction(int user, int movie, List<Neighbour> neighbours, double threshold){
		double zaehler = 0;
		double nenner = 0;
		int neighbour_index;
		for (int i=0; i<neighbours.size();i++){
			neighbour_index = neighbours.get(i).getIndex();
			if ((users_users[user][neighbour_index] >= threshold) && (users_movies[neighbour_index][movie] > 0)){
				zaehler = zaehler + (users_movies[neighbour_index][movie] * users_users[user][neighbour_index]);
				nenner = nenner + (users_users[user][neighbour_index]);
			}
		}
		
		// In der Nachbarschaft wurde der Film nicht bewertet.
		if ((zaehler == 0) || (nenner == 0)){
			return 0;
		}else{
			// Die vorhergesagte Bewertung wird gerundet. Messungen nach liefert dies eine bessere
			// Genauigkeit (nicht zuletzt, da der Nutzer auch nur ganzzahlig abstimmen kann).
			return Math.round(zaehler/nenner);
		}
	}
	
	// Diese Funktion weißt dem Movie Objekt einen Namen zu.
	private List<Movie> matchMovieNames(List<Movie> movies){
		for (int i=0;i<movies.size();i++){
			movies.get(i).setName(moviename[movies.get(i).getIndex()]);
		}
		return movies;
		
	}
	
	
	//DEBUG FUNKTIONEN
	private double rundeAufZweiStellen(double zahl) 
	{   
	      return Math.round( zahl * 100 ) / 100.0;
	}
	
	public void printUsersTable(){
		for (int i=0;i<ARRAYSIZE;i++){
			for (int m=0;m<ARRAYSIZE;m++){
				System.out.print(rundeAufZweiStellen(users_users[i][m])+"\t");
			}
			System.out.println("");
		}
	}
	
	public void printMovieTable(){
		for (int i=0;i<ARRAYSIZE;i++){
			for (int m=0;m<ARRAYSIZE;m++){
				System.out.print(users_movies[i][m]+"\t");
			}
			System.out.println("");
		}
	}
	
	public void printIntersection(int userA, int userB){
		ArrayList<Integer> intersection = getIntersectionOf(userA, userB);
		System.out.print("Intersection: ");
		for (int i=0;i<intersection.size();i++){
			System.out.print(intersection.get(i)+" ");
		}
		System.out.println("");
	}
	
}
