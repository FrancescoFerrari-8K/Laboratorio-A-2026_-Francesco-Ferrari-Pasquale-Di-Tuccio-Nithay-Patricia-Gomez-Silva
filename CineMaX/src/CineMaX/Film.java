package CineMaX;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;

public class Film { // Questa classe costruisce oggetti di tipo Film
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Campi
	private String DataeOra;
    private String Titolo;
	private String Genere;
	private String Regista;
	private String Anno;
	private String Durata; // In minuti	
	private String Età; // Età minima
	private String Prezzo; //Costo biglietto proiezione
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Costruttori
	public Film(String dataeora,String titolo, String genere, String regista, String anno, String durata, String età, String prezzo) {
		this.DataeOra=dataeora;
		this.Titolo=titolo;
		this.Genere=genere;
		this.Regista=regista;
		this.Anno=anno;
		this.Durata=durata;
		this.Età=età;
		this.Prezzo=prezzo;
	}
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Metodi
	// Getter
	public String getDataEora(){
		return DataeOra;
	}
	
	public String getTitolo(){
		return Titolo;
	}
	
	public String getGenere(){
		return Genere;
	}
	
	public String getRegista(){
		return Regista;
	}
	
	public String getAnno(){
		return Anno;
	}
	
	public String getDurata(){
		return Durata;
	}
	
	public String getEtà(){
		return Età;
	}
	
	public String getPrezzo(){
		return Prezzo;
	}
	//----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//caricaFilm: questo metodo importa le colonne del file proiezioni.csv per permettere la ricerca di un film da parte dell'utente
	//il metodo restituisce un array dimensione 8 di linkedlist di oggetti tipo stringa.
	//il metodo è statico così può essere invocato senza dover necessariamente istanziare un oggetto di tipo film, ma chiamando direttamente la classe Film
	
	public static ArrayList<LinkedList<String>> caricaFilm() throws FileNotFoundException, IOException{
		//trova il percorso assoluto del file proiezioni.csv per rendere il metodo indipendente dalla macchina su cui è eseguito
		String percorso= new File("..\\data\\proiezioni.csv").getAbsolutePath(); // il doppio punto è per andare nella directory padre
		
		//inizializzo linkedlist per salvataggio dati
		LinkedList<String> dataeora= new LinkedList<String>();
		LinkedList<String> titolo= new LinkedList<String>();
		LinkedList<String> genere= new LinkedList<String>();
		LinkedList<String> regista= new LinkedList<String>();
		LinkedList<String> anno= new LinkedList<String>();
		LinkedList<String> durata= new LinkedList<String>();
		LinkedList<String> età= new LinkedList<String>();
		LinkedList<String> prezzo= new LinkedList<String>();
		
		//inizializzo l'array di linkedlist datastruct
		ArrayList<LinkedList<String>> datastruct= new ArrayList<LinkedList<String>>();
		
		//lettura del file e scrittura delle colonne come linkedlists nell' array datastruct
		FileReader frd=new FileReader(percorso);
		try (BufferedReader buffread= new BufferedReader(frd)) {
			String riga; //Creo la variabile che contiene ad ogni iterazione la riga successiva del file
			String[] colonne; //inizializzo la variabile che crea l'array di stringhe che contiene i valori estratti dal file da inserire nelle LinkedList
			
			while((riga=buffread.readLine())!=null) {//leggo il file riga per riga fino a quando la riga non diventa null (dopo l'ultima riga!)
				colonne=riga.split(",");//divido le colonne col separatore decimale , essendo il file di tipo csv
				//aggiungo le stringhe nelle relative LinkedList
				 dataeora.add(colonne[0]);
				 titolo.add(colonne[1]);
				 genere.add(colonne[2]);
				 regista.add(colonne[3]);
				 anno.add(colonne[4]);
				 durata.add(colonne[5]);
				 età.add(colonne[6]);
				 prezzo.add(colonne[7]);
			}
			//chiusura degli stream per evitare memory leaks
			buffread.close();
			frd.close();
			//aggiungo le LikndeList all'array di LinkedList datastruct
			 datastruct.add(dataeora);
			 datastruct.add(titolo);
			 datastruct.add(genere);
			 datastruct.add(regista);
			 datastruct.add(anno);
			 datastruct.add(durata);
			 datastruct.add(età);
			 datastruct.add(prezzo);
			//System.out.println((datastruct.get(0))); //si usa il metodo get(int indice) per avere accesso  all'indice riferito dal parametro formale
			//System.out.println((datastruct.get(0)).size()); //controllo per verificare che la dimensione della lista sia uguale al numero di righe del file proiezioni.csv
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		return datastruct;
	}
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
}


