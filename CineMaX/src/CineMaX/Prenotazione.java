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
import java.util.LinkedList;
import java.util.List;

public class Prenotazione { // Questa classe crea oggetti di tipo prenotazione

	// Campi
	public static final String percorsoFile = "..\\..\\data\\Prenotazioni.csv";

	// private String idCliente;
	private String IDUtente;
	private String Nome;
	private String Cognome;
	private LocalDateTime Proiezione_Data;
	private String Proiezione_Titolo;
	private int NPosti;
	private int IDPrenotazione;
	

	// Costruttori

	public Prenotazione(String IDUtente, String nome, String cognome, LocalDateTime Proiezione_Data, String Proiezione_Titolo,
			int NPosti) {

		this.Nome = nome;
		this.Cognome = cognome;
		this.Proiezione_Data = Proiezione_Data;
		this.Proiezione_Titolo = Proiezione_Titolo;
		this.NPosti = NPosti;
		this.IDPrenotazione = Prenotazione.generaNuovoID();
		this.IDUtente = IDUtente;
		
	}

	public Prenotazione(int IDPrenotazione, String IDUtente, String nome, String cognome, LocalDateTime Proiezione_Data, String Proiezione_Titolo,
			int NPosti) {

		this.Nome = nome;
		this.Cognome = cognome;
		this.Proiezione_Data = Proiezione_Data;
		this.Proiezione_Titolo = Proiezione_Titolo;
		this.NPosti = NPosti;
		this.IDPrenotazione = IDPrenotazione;
        this.IDUtente = IDUtente;
	
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
		this.IDPrenotazione = IDPrenotazione;
	}

	public String getIDUtente(){
		return this.IDUtente;
	}

	public void setIDUtente(){
		this.IDUtente =IDUtente;
	}

	public String toString() {

		return this.toString(false);
	}

	public String toString(boolean mostraID) {
		if(mostraID==true){
			return "ID "+IDPrenotazione +" - "+ Nome + " " + Cognome + " - Proiezione: " + Proiezione_Titolo + " - Data: "
			+ Proiezione_Data.toString() + " - NPostiPrenotati: " + NPosti;
		} else{
			return "Prenotazione " + Nome + " " + Cognome + " - Proiezione: " + Proiezione_Titolo + " - Data: "
			+ Proiezione_Data.toString() + " - NPostiPrenotati: " + NPosti;
		}
		
	}
	// - carico tutte le prenotazione
	// - controllo se la lista e vuota, se e vuota restituisco 1
	// - se non e vuota, controllo l'ultimo elemento della lista, prendo il suo ID,
	// aggiungo 1

	// questo metodo serve per generare un nuovo ID:
	public static int generaNuovoID() {
		ArrayList<Prenotazione> listaPrenotazioni = caricaPrenotazioni();

		if (listaPrenotazioni.isEmpty()) {
			return 1;
		}
		return listaPrenotazioni.get(listaPrenotazioni.size() - 1).getIDPrenotazione() + 1;
	}

	public static ArrayList<Prenotazione> TrovaPrenotazioniConNomeECognome(String nome, String cognome,
			ArrayList<Prenotazione> listaPrenotazione) {
		ArrayList<Prenotazione> risultato = new ArrayList<Prenotazione>();
		for (Prenotazione p : listaPrenotazione) {
			if (nome.equals(p.getNome()) && cognome.equals(p.getCognome())) {
				risultato.add(p);
			}
		}
		return risultato;
	}

	/// questo metodo estrae dal file di prenotazione tutte le prenotazione e me
	/// li ristituisce

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
				if (buffread.readLine() != null) {
					// Usiamo un 'if' per sicurezza, nel caso in cui il file fosse completamente
					// vuoto
				}

				while ((riga = buffread.readLine()) != null) {// leggo il file riga per riga fino a quando la riga non
																// diventa null (dopo l'ultima riga!)
					colonne = riga.split(",");// divido le colonne col separatore decimale , essendo il file di tipo csv
												// aggiungo le stringhe nelle relative LinkedList

					Prenotazione PrenotazioneTemp = new Prenotazione(Integer.parseInt(colonne[0]), colonne[1],
							colonne[2], colonne [3],
							LocalDateTime.parse(colonne[4].replace("\"", "")), colonne[5],
							Integer.parseInt(colonne[6]));
					listaPrenotazioni.add(PrenotazioneTemp);
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

	public static boolean aggiungiPrenotazioneAlCSV(Prenotazione prenotazione) {
		try (FileWriter writer = new FileWriter(percorsoFile, true)) {

			String nuovaRiga = "\n" + prenotazione.getIDPrenotazione() + "," +
		        	prenotazione.getIDUtente()+","+
					prenotazione.getNome() + "," +
					prenotazione.getCognome() + "," +
					"\"<"
					+ prenotazione.getProiezione_Data().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
					+ "\"" + "," + "\"" + prenotazione.getProiezione_Titolo() + "\"" + "," +
					prenotazione.getNPosti();

			writer.write(nuovaRiga);

			System.out.println("la prenotazione è stata inserita nel nostro sistema");
			writer.close();
			return true;
		} catch (IOException e) {
			System.out.println("la prenotazione non e  andata a buon fine: ");
			e.printStackTrace();
			return false;
		}
	}


	 // questo metodo modifica la prenotazione//

	 public static boolean modificaPrenotazioneNelCSV(int idPrenotazione, Prenotazione nuovaPrenotazione) {
		List<String> righe = new ArrayList<>();
	
		 // leggere file prenotazione
		try (BufferedReader reader = new BufferedReader(new FileReader(percorsoFile))) {
			String riga = reader.readLine(); // Leggi la prima riga (cabecera)
			if (riga != null) {
				righe.add(riga); // Aggiungi la cabecera alla lista
			}
	
			// Leggi le altre righe
			while ((riga = reader.readLine()) != null) {
				if (riga.trim().isEmpty()) continue;
				String[] colonne = riga.split(",");
	
				// Controlla se l'ID corrisponde
				if (Integer.parseInt(colonne[0].trim()) == idPrenotazione) {
					// Sostituisci la riga con la nuova prenotazione
					String nuovaRiga = nuovaPrenotazione.getIDPrenotazione() + "," +
					     	nuovaPrenotazione.getIDUtente()+","+
							nuovaPrenotazione.getNome() + "," +
							nuovaPrenotazione.getCognome() + "," +
							"\"" + nuovaPrenotazione.getProiezione_Data().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")) + "\"" + "," +
							"\"" + nuovaPrenotazione.getProiezione_Titolo() + "\"" + "," +
							nuovaPrenotazione.getNPosti();
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
			for (String riga : righe) {
				writer.write(riga);
				writer.newLine();
			}
		} catch (IOException e) {
			System.out.println("Errore durante la scrittura del file: " + e.getMessage());
			return false;
		}
	
		System.out.println("Prenotazione modificata con successo.");
		return true;
	}
		
		 // elimina prenotazione//
		 public static boolean eliminaPrenotazioneDalCSV(int idPrenotazione) {
			List<String> righe = new ArrayList<>();
		
			try (BufferedReader reader = new BufferedReader(new FileReader(percorsoFile))) {
				String riga = reader.readLine(); // Leggo la prima riga (cabecera)
				if (riga != null) {
					righe.add(riga); // Aggiungo la cabecera alla lista
				}
		
				// Leggo le altre righe
				while ((riga = reader.readLine()) != null) {
					// Previene errori in presenza di righe vuote
					if (riga.trim().isEmpty()) continue;
		
					String[] colonne = riga.split(",");
		
					// Rimuovo gli spazi prima di convertire in numero (.trim())
					if (Integer.parseInt(colonne[0].trim()) == idPrenotazione) {
						System.out.println("Prenotazione con ID " + idPrenotazione + " eliminata.");
						continue; // Salto questa riga (non verrà salvata)
					}
		
					// Mantengo le altre righe
					righe.add(riga);
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

		// leggere file prenotazione
		// trovare prenotazione con idPrenotazioneDaModificare
		// sostituirla con nuovaPrenotazione
		// salvare file prenotazione



		//questo metodo serve per sapere se il formato della data e valido oppure no//

		public static boolean FormatoDiDataCorretto(String sceltaData){

		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/uuuu")
        .withResolverStyle(ResolverStyle.STRICT);

		try {
        LocalDate dataInserita = LocalDate.parse(sceltaData, formato);
        LocalDate hoy = LocalDate.now();
        return dataInserita.isAfter(hoy);
    } catch (DateTimeParseException e) {
        return false; // se il formato non e valido oppure se la data non esiste
    }

		}

	
}