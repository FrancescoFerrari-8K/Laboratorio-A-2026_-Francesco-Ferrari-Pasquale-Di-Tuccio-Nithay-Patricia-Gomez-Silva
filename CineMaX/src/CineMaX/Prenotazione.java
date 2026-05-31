package CineMaX;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

public class Prenotazione { // Questa classe crea oggetti di tipo prenotazione

	// Campi


	// private String idCliente;
	private String Nome;
	private String Cognome;
	private LocalDateTime Proiezione_Data;
	private String Proiezione_Titolo;
	private int Fila;
	private int NPosti;
	private int ID;

	// Costruttori

	public Prenotazione(String nome, String cognome, LocalDateTime Proiezione_Data, String Proiezione_Titolo, int fila,
			int NPosti) {
		this.Nome = nome;
		this.Cognome = cognome;
		this.Proiezione_Data = Proiezione_Data;
		this.Proiezione_Titolo = Proiezione_Titolo;
		this.Fila = fila;
		this.NPosti = NPosti;
		this.ID = Math.random() > 0.5 ? (int) (Math.random() * 1000) : (int) (Math.random() * 1000) * -1; // genera un ID casuale tra -1000 e 1000 (codice univoco)
}

	//metodi//
	public String getNome(){
		return this.Nome;
	}

	public void setNome (String Nome){
		 this.Nome = Nome; 
	}

	public String getCognome(){
		return this.Cognome;
	}

	public void setCognome(String Cognome){
		this.Cognome = Cognome;
	}

	public LocalDateTime getProiezione_Data(){
		return this.Proiezione_Data;
	}

	public void setProiezione_Data(LocalDateTime Proiezione_Data){
		this.Proiezione_Data = Proiezione_Data;
	}

	public String getProiezione_Titolo(){
		return this.Proiezione_Titolo;
	}

	public void setProiezione_Titolo(String Proiezione_Titolo){
		this.Proiezione_Titolo = Proiezione_Titolo;
	}

	public int getFila(){
		return this.Fila;
	}

	public void setFila(int Fila){
		this.Fila = Fila;
	}

	public int getNPosti(){
		return this.NPosti;
	}

	public void setNPosti(int NPosti){
		this.NPosti = NPosti;
	}

	public int getID(){
		return this.ID;
	}

	public void setID(int ID){
		this.ID = ID;
	}

	public String toString() {

		return "Prenotazione: " + Nome + " " + Cognome + " - Proiezione: " + Proiezione_Titolo + " - Data: "
				+ Proiezione_Data + " - Fila: " + Fila + " - NPostiPrenotati: " + NPosti;
	}											
	

	public static ArrayList<Prenotazione> TrovaPrenotazioniConNomeECognome(String nome, String cognome, ArrayList<Prenotazione> listaPrenotazione){		
		ArrayList<Prenotazione> risultato = new ArrayList<Prenotazione>();
		for(Prenotazione p: listaPrenotazione){
			if(nome.equals(p.getNome()) && cognome.equals(p.getCognome())){
				risultato.add(p);
			}
		}
		return risultato;
	}

	///questo metodo estrae dal file di prenotazione tutte le prenotazione e me li ristituisce///
	
	public static ArrayList<Prenotazione> caricaPrenotazioni() throws FileNotFoundException, IOException {

	// trova il percorso assoluto del file proiezioni.csv per rendere il metodo indipendente dalla macchina su cui è eseguito
		String percorso = new File("..\\..\\data\\Prenotazioni.csv").getAbsolutePath(); // il doppio punto è per andare nella directory padre

		// inizializzo l'array di linkedlist datastruct//
		ArrayList<Prenotazione> listaPrenotazioni = new ArrayList<Prenotazione>();

		// lettura del file e scrittura delle colonne come linkedlists nell' array datastruct//
		FileReader frd = new FileReader(percorso);
		try (BufferedReader buffread = new BufferedReader(frd)) {
			String riga; // Creo la variabile che contiene ad ogni iterazione la riga successiva del file
			String[] colonne; // inizializzo la variabile che crea l'array di stringhe che contiene i valori estratti dal file da inserire nelle LinkedList

			// Leggi la prima riga (l'intestazione) a vuoto per saltarla
			if (buffread.readLine() != null) {
				// Usiamo un 'if' per sicurezza, nel caso in cui il file fosse completamente vuoto
			}

			while ((riga = buffread.readLine()) != null) {// leggo il file riga per riga fino a quando la riga non diventa null (dopo l'ultima riga!)
				colonne = riga.split(",");// divido le colonne col separatore decimale , essendo il file di tipo csv aggiungo le stringhe nelle relative LinkedList
				Prenotazione PrenotazioneTemp = new Prenotazione(colonne[0], colonne[1], LocalDateTime.parse(colonne[2]), colonne[3],
						Integer.parseInt(colonne[4]), Integer.parseInt(colonne[5]));
				listaPrenotazioni.add(PrenotazioneTemp);
			}
			// chiusura degli stream per evitare memory leaks
			buffread.close();
			frd.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return listaPrenotazioni;
		}
		return listaPrenotazioni;
	}

}
