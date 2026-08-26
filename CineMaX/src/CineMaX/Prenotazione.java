package CineMaX;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

public class Prenotazione { // Questa classe crea oggetti di tipo prenotazione

	// Campi
	public static final String percorsoFile = "..\\data\\Prenotazioni.csv";

	// private String idCliente;
	private String IDUtente;
	private String Nome;
	private String Cognome;
	private LocalDateTime Proiezione_Data;
	private String Proiezione_Titolo;
	private int NPosti;
	private int IDPrenotazione;
	private double Prezzo_Biglietto;

	// Costruttori
	public Prenotazione(String IDUtente, String nome, String cognome, LocalDateTime Proiezione_Data,
			String Proiezione_Titolo,
			int NPosti, double Prezzo_Biglietto) {

		this.Nome = nome;
		this.Cognome = cognome;
		this.Proiezione_Data = Proiezione_Data;
		this.Proiezione_Titolo = Proiezione_Titolo;
		this.NPosti = NPosti;
		this.IDPrenotazione = Prenotazione.generaNuovoID();
		this.IDUtente = IDUtente;
		this.Prezzo_Biglietto = Prezzo_Biglietto;

	}

	public Prenotazione(int IDPrenotazione, String IDUtente, String nome, String cognome, LocalDateTime Proiezione_Data,
			String Proiezione_Titolo,
			int NPosti, double Prezzo_Biglietto) {

		this.Nome = nome;
		this.Cognome = cognome;
		this.Proiezione_Data = Proiezione_Data;
		this.Proiezione_Titolo = Proiezione_Titolo;
		this.NPosti = NPosti;
		this.IDPrenotazione = IDPrenotazione;
		this.IDUtente = IDUtente;
		this.Prezzo_Biglietto = Prezzo_Biglietto;

	}

	// metodi//
	public String getNome() {
		return this.Nome;
	}

	public void setNome(String Nome) {
		this.Nome = Nome;
	}

	public String getCognome() {
		return this.Cognome;
	}

	public void setCognome(String Cognome) {
		this.Cognome = Cognome;
	}

	public LocalDateTime getProiezione_Data() {
		return this.Proiezione_Data;
	}

	public void setProiezione_Data(LocalDateTime Proiezione_Data) {
		this.Proiezione_Data = Proiezione_Data;
	}

	public String getProiezione_Titolo() {
		return this.Proiezione_Titolo;
	}

	public void setProiezione_Titolo(String Proiezione_Titolo) {
		this.Proiezione_Titolo = Proiezione_Titolo;
	}

	public int getNPosti() {
		return this.NPosti;
	}

	public void setNPosti(int NPosti) {
		this.NPosti = NPosti;
	}

	public int getIDPrenotazione() {
		return this.IDPrenotazione;
	}

	public void setIDPrenotazione(int ID) {
		this.IDPrenotazione = ID;

	}

	public String getIDUtente() {
		return this.IDUtente;
	}

	public void setIDUtente(String IDUtente) {
		this.IDUtente = IDUtente;
	}

	public double getPrezzoBiglietto() {
		return this.Prezzo_Biglietto;
	}

	public void setPrezzo_Biglietto(double Prezzo_Biglietto) {
		this.Prezzo_Biglietto = Prezzo_Biglietto;
	}

	public String toString() {

		return this.toString(false);
	}

	public String toString(boolean mostraID) {
		if (mostraID == true) {
			return "ID " + IDPrenotazione + " - " + Nome + " " + Cognome + " - Proiezione: " + Proiezione_Titolo
					+ " - Data: "
					+ Proiezione_Data.toString() + " - NPostiPrenotati: " + NPosti + "- Prezzo del Biglietto: "
					+ Prezzo_Biglietto;
		} else {
			return "Prenotazione " + Nome + " " + Cognome + " - Proiezione: " + Proiezione_Titolo + " - Data: "
					+ Proiezione_Data.toString() + " - NPostiPrenotati: " + NPosti + "- Prezzo del Biglietto: "
					+ Prezzo_Biglietto;
		}

	}

	// questo metodo serve per generare un nuovo ID:
	public static int generaNuovoID() {
		// - carico tutte le prenotazione
		ArrayList<Prenotazione> listaPrenotazioni = caricaPrenotazioni();

		if (listaPrenotazioni.isEmpty()) {
			// - controllo se la lista e vuota, se e vuota restituisco 1
			return 1;
		}
		return listaPrenotazioni.get(listaPrenotazioni.size() - 1).getIDPrenotazione() + 1;
		// - se non e vuota, controllo l'ultimo elemento della lista, prendo il suo ID,
		// aggiungo 1
	}

	// mostriamo le prenotazione future perche quelle sono le uniche modificabili
	// questo metodo serve per trovare una prenotazione con nome e cognome
	public static ArrayList<Prenotazione> TrovaPrenotazioniConNomeECognome(String nome, String cognome,
			ArrayList<Prenotazione> listaPrenotazione) {
		ArrayList<Prenotazione> risultato = new ArrayList<Prenotazione>(); // creo un nuovo arrayList di tipo
																			// prenotazione per aggiunguere la
																			// prenotazione trovata
		for (Prenotazione p : listaPrenotazione) { // uso un for each per creare un ciclo
			if (nome.equals(p.getNome()) && cognome.equals(p.getCognome()) // utilizo il if per torvare la prenotazione
																			// in base ai dati che mi arrivano e quelli
																			// che gia ho
					&& p.Proiezione_Data.isAfter(LocalDateTime.now())) {
				risultato.add(p); // una volta trovati i dati aggiungo quelli ai risultati
			}
		}
		return risultato;
	}

	// questo metodo estrae dal file di prenotazione tutte le prenotazione e me li
	// ristituisce//
	public static ArrayList<Prenotazione> caricaPrenotazioni() {

		// trova il percorso assoluto del file proiezioni.csv per rendere il metodo
		// indipendente dalla macchina su cui è eseguito
		String percorso = new File(percorsoFile).getAbsolutePath(); // il doppio punto è per andare nella directory
																	// padre

		// inizializzo l'array di linkedlist datastruct//
		ArrayList<Prenotazione> listaPrenotazioni = new ArrayList<Prenotazione>();
		try {
			// lettura del file e scrittura delle colonne come linkedlists nell' array
			// datastruct//
			FileReader frd = new FileReader(percorso);
			try (BufferedReader buffread = new BufferedReader(frd)) {
				String riga; // Creo la variabile che contiene ad ogni iterazione la riga successiva del file
				String[] colonne; // inizializzo la variabile che crea l'array di stringhe che contiene i valori
									// estratti dal file da inserire nelle LinkedList

				// Leggi la prima riga (l'intestazione) a vuoto per saltarla
				buffread.readLine();
				riga = buffread.readLine();//leggo la prima riga utile

				while (riga != null) {// leggo il file riga per riga fino a quando la riga non
																// diventa null (dopo l'ultima riga!)
					colonne = riga.split(",");// divido le colonne col separatore decimale , essendo il file di tipo csv
												// aggiungo le stringhe nelle relative LinkedList

					Prenotazione PrenotazioneTemp = new Prenotazione(Integer.parseInt(colonne[0]), colonne[1],
							colonne[2], colonne[3],
							LocalDateTime.parse(colonne[4].replace("\"", "")), colonne[5],
							Integer.parseInt(colonne[6].replace("\"", "")), Double.parseDouble(colonne[7]));
					listaPrenotazioni.add(PrenotazioneTemp);
					riga = buffread.readLine();//leggo la prossima riga utile
				}
				// chiusura degli stream per evitare memory leaks
				buffread.close();
				frd.close();

			} catch (IOException e) {
				e.printStackTrace();
				return listaPrenotazioni;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return listaPrenotazioni;
		}
		return listaPrenotazioni;
	}

	// questo metodo serve per aggiungere le prenotazioni al CSV//
	public static boolean aggiungiPrenotazioneAlCSV(Prenotazione prenotazione) {
		try (FileWriter writer = new FileWriter(percorsoFile, true)) { // Apre il file indicato in percorsoFile. Il
																		// parametro true attiva la modalità append, che
																		// permette di aggiungere testo alla fine del
																		// file senza cancellare quello che già esiste

			String nuovaRiga = prenotazione.getIDPrenotazione() + "," + // Creo la stringa con i dati della
																				// prenotazione separati da virgole
																				// (formato CSV)
					prenotazione.getIDUtente() + "," +
					prenotazione.getNome() + "," +
					prenotazione.getCognome() + "," +
					"\""
					+ prenotazione.getProiezione_Data().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
					+ "\"" + "," + prenotazione.getProiezione_Titolo() + "," +
					prenotazione.getNPosti() + "," + prenotazione.getPrezzoBiglietto();

			writer.write(nuovaRiga); // Scrive la stringa appena creata all'interno del file CSV
			//

			System.out.println("la prenotazione è stata inserita nel nostro sistema");
			System.out.println("");
			writer.close(); // Stampa i messaggi di conferma in console, esegue writer.close() per salvare
							// le modifiche sul disco e restituisce true
			return true;
		} catch (IOException e) { // Se si verifica un errore di lettura o scrittura sul file, cattura
									// l'eccezione, stampa i dettagli dell'errore e restituisce false
			System.out.println("la prenotazione non e  andata a buon fine: ");
			e.printStackTrace();
			return false;
		}
	}

	// questo metodo modifica la prenotazione//
	public static boolean modificaPrenotazioneNelCSV(int idPrenotazione, Prenotazione nuovaPrenotazione) {
		List<String> righe = new ArrayList<>(); // se Crea una lista vuota che conterrà tutte le righe del file (sia
												// l'intestazione che i dati)

		// legge il file prenotazione
		try (BufferedReader reader = new BufferedReader(new FileReader(percorsoFile))) { // Apre il file CSV in modalità
																							// lettura usando il
																							// try-with-resources,
																							// garantendo che il file
																							// venga chiuso
																							// automaticamente alla fine
			String riga = reader.readLine(); // Legge la prima riga del file, cioè l'intestazione (i nomi delle colonne
												// come IDPrenotazione, IDUtente...)
			if (riga != null) {
				righe.add(riga); // Se la riga esiste (riga != null), la aggiunge alla lista
			}
			while ((riga = reader.readLine()) != null) { // Avvia un ciclo per leggere tutte le righe successive, una ad
															// una, fino ad arrivare alla fine del file
				if (riga.trim().isEmpty()) // Controlla se la riga letta è vuota. Se lo è, il comando continue la salta
											// senza aggiungerla, evitando problemi di elaborazione
					continue;
				String[] colonne = riga.split(","); // Divide la riga letta in un array di stringhe usando la virgola
													// (,) come separatore. In questo modo se puo accedere ai singoli
													// campi (ID, nome, data, ecc.) tramite il loro indice (es.
													// colonne[0] per l'ID)

				// Controlla se l'ID corrisponde
				if (Integer.parseInt(colonne[0].trim()) == idPrenotazione) { // Prende il valore della prima colonna
																				// (colonne[0]), lo trasforma in numero
																				// intero e controlla se equivale all'ID
																				// della prenotazione che vuoi
																				// modificare
					// Sostituisci la riga con la nuova prenotazione
					String nuovaRiga = nuovaPrenotazione.getIDPrenotazione() + "," +
							nuovaPrenotazione.getIDUtente() + "," +
							nuovaPrenotazione.getNome() + "," +
							nuovaPrenotazione.getCognome() + "," +
							"\""
							+ nuovaPrenotazione.getProiezione_Data()
									.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
							+ "\"" + "," +
							"\"" + nuovaPrenotazione.getProiezione_Titolo() + "\"" + "," +
							nuovaPrenotazione.getNPosti() + "\"" + "," + nuovaPrenotazione.getPrezzoBiglietto();
					righe.add(nuovaRiga);
				} else {
					// Mantieni la riga originale
					righe.add(riga);
				}
			}
		} catch (IOException e) {
			System.out.println("Errore durante la lettura del file: " + e.getMessage());
			return false;
		}

		// Scrivi le righe aggiornate nel file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(Prenotazione.percorsoFile))) {
			// Apre il file CSV in modalità scrittura sovrascrittiva (senza il parametro
			// true). Questo significa che svuota completamente il file esistente per
			// poterci riscrivere da capo tutti i dati aggiornati. BufferedWriter rende
			// l'operazione di scrittura più efficiente
			for (String riga : righe) { // scorre una ad una tutte le stringhe contenute nella lista righe (che
										// include l'intestazione, le prenotazioni non modificate e quella appena
										// aggiornata)
				writer.write(riga); // Scrive il testo della riga corrente all'interno del file CSV
				writer.newLine(); // Aggiunge un salto di linea subito dopo la riga appena scritta, garantendo che
									// la successiva vada al rigo sotto
			}
		} catch (IOException e) {
			System.out.println("Errore durante la scrittura del file: " + e.getMessage());
			return false;
		}

		System.out.println("Prenotazione modificata con successo.");
		return true;
	}

	// questo metodo serve per eliminare una prenotazione dal csv//
	public static boolean eliminaPrenotazioneDalCSV(int idPrenotazione) {
		List<String> righe = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(percorsoFile))) {
			String riga = reader.readLine(); // Leggo la prima riga
			if (riga != null) { // se verifica che il file non sia vuoto
				righe.add(riga); // inserisce la riga letta nella lista righe, assicurando che la struttura delle
									// colonne venga conservata nel file finale
			}

			// Leggo le altre righe
			while ((riga = reader.readLine()) != null) { // Previene errori in presenza di righe vuote

				if (riga.trim().isEmpty()) // elimina gli spazi vuoti, se la riga e vuota il continue la salta e passa
											// subito alla riga successiva del file
					continue;

				String[] colonne = riga.split(",");

				// Rimuovo gli spazi prima di convertire in numero (.trim())
				if (Integer.parseInt(colonne[0].trim()) == idPrenotazione) {
					System.out.println("Prenotazione con ID " + idPrenotazione + " eliminata.");
					continue; // Se l'ID corrisponde, mostra un messaggio ed evita di aggiungere la riga alla
								// lista, ignorandola
				}

				// Mantengo le altre righe
				righe.add(riga); //Se l'ID non corrisponde, salva la riga nella lista per conservarla nel file
			}
		} catch (IOException e) {
			System.out.println("Errore durante la lettura del file: " + e.getMessage());
			return false;
		} catch (NumberFormatException e) {
			System.out.println("Errore: ID non valido nel file CSV.");
			return false;
		}

		// Scrivo le righe aggiornate nel file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(percorsoFile))) {
			for (String riga : righe) {
				writer.write(riga);
				writer.newLine();
			}
		} catch (IOException e) {
			System.out.println("Errore durante la scrittura del file: " + e.getMessage());
			return false;
		}

		System.out.println("Prenotazione eliminata con successo.");
		return true;
	}

	// questo metodo serve per sapere se il formato della data e valido oppure no//
	public static boolean FormatoDiDataCorretto(String sceltaData) {

		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/uuuu")
				.withResolverStyle(ResolverStyle.STRICT);  //Assicura che la data esista davvero nel calendario

		try {
			LocalDate dataInserita = LocalDate.parse(sceltaData, formato);
			LocalDate hoy = LocalDate.now();
			return dataInserita.isAfter(hoy);  //Restituisce true solo se la data inserita è nel futuro rispetto a oggi
		} catch (DateTimeParseException e) {
			return false; // se il formato non e valido oppure se la data non esiste
		}

	}

}