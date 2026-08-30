/**
* Questo package contiene le classi necessarie al funzionamento dell'applicazione CineMaX.
* L'applicazione gestisce un cinema monosala da 200 posti.
* L'applicazione permette di gestire il palinsesto e le prenotazioni (proiezionisti e bgliettai)
* oppure effettuare o modificare prenotazioni (clienti). L'utente non registrato (guest)
* può solamente visualizzare le proiezioni disponibili.
* L'applicazione consente inoltre di registrare nuovi utenti.
* @author Francesco Ferrari
 */
package CineMaX;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *Questa classe contiene il metodo main che esegue l'applicazione e i metodi necessari
 *è il punto di ingresso del flusso di esecuzione del programma.
 */
public class CineMaX {
	/**
	 * Questo campo è un oggetto di tipo File che riferisce il file Utenti.csv usato per salvare gli utenti 
	 */
	private static final File file=new File("..\\data\\Utenti.csv");
	/**
	 * Questo campo è un oggetto di tipo String che riferisce il percorso assoluto di Utenti.csv
	 */
	private static final String percorsofile=file.getAbsolutePath();//per farlo funzionare a prescindere dalla macchina
	//Metodi
	
	/**
	 * Questo metodo statico permette di aggiungere un utente al file Utenti.csv. 
	 * Viene usato solamente all'interno del metodo Registrati().
	 * Il metodo aggiunge un utente scrivendo le informazione specificate dai parametri 
	 * nella linea successiva del file Utenti.csv, in colonne separate da ,
	 * 
	 * colonna 0 ID
	 * colonna 1 nome
	 * colonna 2 cognome
	 * colonna 3 username
	 * colonna 4 password
	 * colonna 5 data di nascita
	 * colonna 6 città di residenza
	 * colonna 7 tipo di account
	 * 
	 * @param ID codice identificativo utente, assegnato dal metodo statico assegnaIDstr()
	 * @param nome il nome dell'utente, se assegnato manualmente deve avere almeno due lettere 
	 * @param cognome il cognome dell'utente, se assegnato manualmente deve avere almeno due lettere 
	 * @param username il nome utente che comparirà nel messaggio di benvenuto che segue il login
	 * @param password la password decisa dall'utente, non ci sono limiti al numero di caratteri che può avere
	 * @param datanascita la data di nascita dell'utente in formato LocalDate compatibile AAAA-MM-GG
	 * @param residenza la città di residenza dell'utente
	 * @param tipo una lettera che identifica il tipo di utente (C cliente, P proiezionista, B bigliettaio)
	 */
	private static void aggiungiUtente(String ID, String nome, String cognome, String username, String password, String datanascita, String residenza, String tipo  ) {
		try {
			FileWriter filewrt=new FileWriter(percorsofile, true); //true serve per non cancellare il contenuto attuale del file in seguito all'aggiunta di informazione
		    BufferedWriter buff=new BufferedWriter(filewrt);
		    String str=ID+","+nome+","+cognome+","+username+","+password+","+datanascita+","+residenza+","+tipo+"\n";
		    buff.write(str); 
		    buff.close();
		    filewrt.close();
		} catch (IOException e) {
			System.out.println("Errore critico, file utenti.csv non valido!");
		}
	}
	/**
	 * Questo metodo statico permette di controllare se uno username è gia presente nel file Utenti.csv
	 * viene usato solamente all'interno del metodo Registrati().
	 * Il metodo legge il file Utenti.csv riga per riga, separando le colonne utilizzando , come separatore.
	 * La quarta colonna del file contiene gli username, la stringa in ingresso viene confrontata
	 * con ogni dato presente in questa colonna del file per capire se lo username desiderato è già in uso.
	 * 
	 * @param username lo username di cui si desidera verificare la presenza all'interno del file Utenti.csv
	 * @return true se lo username è già in uso, false altrimenti
	 */
	private static boolean usernameInUse(String username) {
				try {
			FileReader filerd = new FileReader(percorsofile);
			BufferedReader buff= new BufferedReader(filerd);
			String riga;
			String[] campiriga=new String[8];
			buff.readLine();//salto la prima riga del testo
			while ((riga=buff.readLine())!=null) {
				campiriga=riga.split(",");
				if((username.trim()).equals(campiriga[3].trim())) {
					buff.close();
					filerd.close();
					return true;
				}
			}
			buff.close();
		} catch (FileNotFoundException e) {
			System.out.println("Errore critico, file utenti.csv non trovato!");
		} catch (IOException e) {
			System.out.println("Errore critico, file utenti.csv non valido!");
		}	
		return false;
	}
	/**
	 * Questo metodo statico permette di cercare la data associata a un ID utente;
	 * viene usato solamente all'interno del metodo Registrati() se la data di nascita inserita 
	 * fa risultare il nuovo utente come minorenne.
	 * Il metodo legge il file Utenti.csv riga per riga, separando le colonne utilizzando , come separatore.
	 * La prima colonna del file contiene gli ID utente, la stringa in ingresso viene confrontata
	 * con ogni dato presente in questa colonna del file, se c'è una corrispondenza (l'ID utente è univoco)
	 * il metodo ritorna il valore contenuto nella sesta colonna della riga associata all'ID fornito
	 * ossia la data di nascita dell'utente. 
	 * 
	 * @param ID l'ID dell'utente di cui si desidera conoscere la data di nascita
	 * @return  la data di nascita dell'utente come String
	 * 
	 */
	private static String dataDinascita(String ID) {
		try {
	FileReader filerd = new FileReader(percorsofile);
	BufferedReader buff= new BufferedReader(filerd);
	String riga;
	String[] campiriga=new String[8];
	buff.readLine();//salto la prima riga del testo
	while ((riga=buff.readLine())!=null) {
		campiriga=riga.split(",");
		if((ID.trim()).equals(campiriga[0].trim())) {
			buff.close();
			filerd.close();
			return campiriga[5];
		}
	}
	buff.close();
} catch (FileNotFoundException e) {
	System.out.println("Errore critico, file utenti.csv non trovato!");
} catch (IOException e) {
	System.out.println("Errore critico, file utenti.csv non valido!");
}	
return null;
}
	/**
	 * Questo metodo esegue la cifratura delle password con l'algoritmo SHA 256
	 * è usato solamente nel metodo Registrati() per consentire il salvataggio
	 * delle password sul file Utenti.csv in modalità sicura, in quanto non è possibile
	 * risalire dal digest salvato alla password effettiva scelta dall'utente.
	 * Questo metodo è usato anche per codificare la password inserita dall'utente
	 * in fase di login per effettuare il confronto con quelle salvate nel file Utenti.csv
	 * 
	 * @param testo la parola di cui si vuole ottenere la chiave
	 * @return  la chiave corrispondente alla parola inserita come String, null se qualcosa va storto
	 */
	
	private static String sha256Hash(String testo) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");//crea un istanza dell'algoritmo sha256 utilizzato per criptare (dichiara l'algoritmo che vuoi utilizzare)
			byte[] hashed=digest.digest(testo.getBytes(StandardCharsets.UTF_8));//converte in hash il testo da criptare e lo restituisce come un array di byte
			StringBuilder hexString=new StringBuilder();//converto l'array di byte in stringa esadecimale
			//StringBuilder è un modo più efficiente di lavorare con le stringhe: 
			//String deve allocare memoria per ogni modifica, mentre StringBuilder
			//modifica direttamente la stringa in memoria
			for(byte b : hashed) {
				String s=String.format("%02x", b); //converto il singolo bit in due caratteri esadecimali
				//%02x= imposto il carattere di riempimento a 0, la stringa deve avere almeno 2 caratteri, x formato esadecimale lettere minuscole
				hexString.append(s);
			}
			String encoded=hexString.toString(); //converto da StringBuilder a String
			return encoded;
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Errore critico, non è possibile istansiare l'algoritmo SHA256!");
		} 
		return null;
	}
	/**
	 * Questo metodo assegna un'ID univoco a un utente utilizzando
	 * le prime due lettere del nome, le prime due del cognome
	 *  le prime due dello username e il numero della riga del file utenti.csv
	 *  in cui l'utente sarà salvato. Questo metodo è usato solamente dal metodo
	 *  Registrati() per 
	 * @param nome
	 * @param cognome
	 * @param username
	 * @return L'ID associato all'utente come String
	 */
	private static String assegnaIDstr(String nome, String cognome, String username) {
		Path percorso=Paths.get(percorsofile);
		String ID="";
		try {
			long numerorighe=(Files.lines(percorso).count())-7;//conta le righe del file e toglie le righe iniziali per far partire da 1 il conto dei clienti
			  String numeroID=Long.toString(numerorighe);
			  return ID=""+nome.charAt(0)+nome.charAt(1)+cognome.charAt(0)+cognome.charAt(1)+username.charAt(0)+username.charAt(1)+numeroID;
		} catch (IOException e) {
			System.out.println("Errore critico, file utenti.csv non valido!");
			e.printStackTrace();
		}
		return ID;
	}
	/**Questo metodo controlla la sintassi della data passata come parametro attuale
	 * e ne esegue il parsing in formato LocalDate compatibile (AAAA-MM-GG).
	 * Il metodo riconosce i formati di data validi e rigetta le date composte da soli numeri oppure
	 * in cui compaiono più di due caratteri speciali (separatori) o caratteri speciali in posizioni
	 * riservate a numeri interi a seconda del formato.
	 * Viene usato solo nel metodo Registrati() all'interno di un ciclo while(true)
	 * per avere le date di nascita in un formato comodo per effettuare il controllo dell'età dell'utente
	 * pertanto il metodo è stato concepito ad hoc per lavorare in sinergia col ciclo
	 * 
	 * @param data la data che si desidera controllare
	 * @return null se la data è in un formato non valido, la data parsata AAAA-MM-GG altrimenti
	 */
	private static String controlloData(String data) {//inizio controllo correttezza data inserita
        //il formato della data è definito dalla posizione dei separatori 
		LinkedList<Integer> indicisep=new LinkedList<>(); //inizializzo una LinkedList di interi in cui salvare gli indici dei separatori
		int lunghezzadata=data.length();
		if(lunghezzadata>=8 && lunghezzadata<=10) { //inizio controllo per date comprese tra 8 e 10 caratteri
			//i formati di data corretti hanno 
		   //8 caratteri (4 per l'anno, 1 per il mese (o il giorno) e 1 per il giorno (o il mese), 2 separatori)
			//9 caratteri (4 per l'anno, 2 per il mese (o il giorno) e 1 per il giorno (o il mese), 2 separatori)
			//10 caratteri (4 per l'anno, 2 per il mese (o il giorno) e 2 per il giorno (o il mese), 2 separatori)
			if(lunghezzadata==10) {//inizio controllo date di lunghezza 10 caratteri
				//trovo le posizioni dei separatori
				for(int i=0; i<data.length();i++) {//inizio ricerca separatori 
					if(!(Character.isDigit(data.charAt(i)))) {
						indicisep.add(i);
					}	
				}//fine ricerca posizioni separatori 
				//controllo caratteri
				if(indicisep.size()==0 || indicisep.size()>2) {//se la data contiene solo numeri o più di due caratteri non numerici (i separatori) ritorna errore 
					System.out.println("La data inserita contiene caratteri non validi, riprova!");
					return null;
					
				}//fine controllo caratteri 
				//salvo gli indici dei separatori in una variabile
				int indice1=indicisep.get(0);
				int indice2=indicisep.get(1);
				//se il formato è GG-MM-AAAA (MM-GG-AAAA) i separatori sono in indice 2 e 5
				//nel formato di data corretto il carattere usato come separatore dev'essere lo stesso
				//controllo che i caratteri in posizioni 2 e 5 siano uguali
				if(indicisep.get(0)==2 && indicisep.get(1)==5) {
					if((data.substring(2,3)).equals(data.substring(5,6))) {
						//formato GG-MM-AAAA
						//faccio il parsing della data in formato local date dopo aver controllato la coerenza di giorni e mesi
						//data.substring(0,2) è il giorno data.substring(3,5) è il mese data.substring(6,10) l'anno
						if(Integer.parseInt(data.substring(0,2))>=12 && Integer.parseInt(data.substring(3,5))<=12) {
						if(Integer.parseInt(data.substring(0,2))>28 && Integer.parseInt(data.substring(3,5))==2){// febbraio ha 28 giorni
							System.out.println("La data inserita non è corretta, febbraio ha 28 giorni!\n");
							return null;
						}
						//se l'anno di nascita è bisestile febbraio ha 29 giorni
						//controllo se l'anno è bisestile con il metolo isLeap(Long anno) di java.time, che restituisce un booleano
						if(Year.isLeap(Long.parseLong(data.substring(6,10)))&& Integer.parseInt(data.substring(0,2))>29 && Integer.parseInt(data.substring(3,5))==2){
							System.out.println("La data inserita non è corretta, febbraio ha 29 giorni in un anno bisestile!\n");
							return null;
						}
						//settembre,novembre,aprile e giugno hanno 30 giorni
						if(Integer.parseInt(data.substring(0,2))>30 && Integer.parseInt(data.substring(5,5))==4){
							System.out.println("La data inserita non è corretta, aprile ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(0,2))>30 && Integer.parseInt(data.substring(3,5))==6){
							System.out.println("La data inserita non è corretta, giugno ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(0,2))>30 && Integer.parseInt(data.substring(3,5))==9){
							System.out.println("La data inserita non è corretta, settembre ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(0,2))>30 && Integer.parseInt(data.substring(3,5))==11){
							System.out.println("La data inserita non è corretta, novembre ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(0,2))>31){//nessun mese ha più di 30 giorni
							System.out.println("Formato di data non valido, riprova!\n");
							return null;
						}
						//se non ci sono errori restituisci la stringa corretta
						System.out.println(data.substring(6,10)+"-"+data.substring(3, 5)+"-"+data.substring(0, 2));
						return data=data.substring(6,10)+"-"+data.substring(3, 5)+"-"+data.substring(0, 2);
					}
						//formato MM-GG-AAAA
						//faccio il parsing della data in formato local date dopo aver controllato la coerenza di giorni e mesi
						//data.substring(0,2) è il giorno data.substring(3,5) è il mese data.substring(6,10) l'anno
						if(Integer.parseInt(data.substring(3,5))>=12 && Integer.parseInt(data.substring(0,2))<=12) {
						if(Integer.parseInt(data.substring(3,5))>28 && Integer.parseInt(data.substring(0,2))==2){// febbraio ha 28 giorni
							System.out.println("La data inserita non è corretta, febbraio ha 28 giorni!\n");
							return null;
						}
						//se l'anno di nascita è bisestile febbraio ha 29 giorni
						//controllo se l'anno è bisestile con il metolo isLeap(Long anno) di java.time, che restituisce un booleano
						if(Year.isLeap(Long.parseLong(data.substring(6,10)))&& Integer.parseInt(data.substring(3,5))>29 && Integer.parseInt(data.substring(0,2))==2){
							System.out.println("La data inserita non è corretta, febbraio ha 29 giorni in un anno bisestile!\n");
							return null;
						}
						//settembre,novembre,aprile e giugno hanno 30 giorni
						if(Integer.parseInt(data.substring(3,5))>30 && Integer.parseInt(data.substring(0,2))==4){
							System.out.println("La data inserita non è corretta, aprile ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(3,5))>30 && Integer.parseInt(data.substring(0,2))==6){
							System.out.println("La data inserita non è corretta, giugno ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(3,5))>30 && Integer.parseInt(data.substring(0,2))==9){
							System.out.println("La data inserita non è corretta, settembre ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(3,5))>30 && Integer.parseInt(data.substring(0,2))==11){
							System.out.println("La data inserita non è corretta, novembre ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(3,5))>31){//nessun mese ha più di 30 giorni
							System.out.println("Formato di data non valido, riprova!\n");
							return null;
							
						}
						//se non ci sono errori restituisci la stringa corretta
						System.out.println(data.substring(6,10)+"-"+data.substring(0, 2)+"-"+data.substring(3, 5));
						return data=data.substring(6,10)+"-"+data.substring(0, 2)+"-"+data.substring(3, 5);
					 }	
					}
						
					else {
						System.out.println("Formato di data non valido, riprova!\n");
						return null;
					}
				}
				//se il formato è AAAA-MM-GG (AAAA-GG-MM) i separatori sono in indice 4 e 7
				if(indicisep.get(0)==4 && indicisep.get(1)==7) {//inizio parsing AAAA-GG-MM
					//nel formato di data corretto il carattere usato come separatore dev'essere lo stesso
					//controllo che i caratteri in posizioni 4 e 7 siano uguali
					//in caso affermativo aggiorno il separatore con il carattere corrispondente
					//altrimenti restituisco errore
					if((data.substring(4,5)).equals(data.substring(7,8))) {
						//formato AAAA-GG-MM
						//faccio il parsing della data in formato AAAA-MM-GG dopo aver controllato la correttezza delle date
						//data.substring(5,6) è il mese data.substring(8,9) è il giorno data.substring(0,4) è l'anno
						if(Integer.parseInt(data.substring(8,10))>=12 && Integer.parseInt(data.substring(5,7))<=12) {
						if(Integer.parseInt(data.substring(5,7))>28 && Integer.parseInt(data.substring(8,10))==2){// febbraio ha 28 giorni
							System.out.println("La data inserita non è corretta, febbraio ha 28 giorni!\n");
							return null;
						}
						//se l'anno di nascita è bisestile febbraio ha 29 giorni
						//controllo se l'anno è bisestile con il metolo isLeap(Long anno) di java.time, che restituisce un booleano
						if(Year.isLeap(Long.parseLong(data.substring(0,4)))&& Integer.parseInt(data.substring(5,7))>29 && Integer.parseInt(data.substring(8,10))==2){
							System.out.println("La data inserita non è corretta, febbraio ha 29 giorni in un anno bisestile!\n");
							return null;
						}
						//settembre,novembre,aprile e giugno hanno 30 giorni
						if(Integer.parseInt(data.substring(5,7))>30 && Integer.parseInt(data.substring(8,10))==4){// febbraio ha 28 giorni
							System.out.println("La data inserita non è corretta, aprile ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(5,7))>30 && Integer.parseInt(data.substring(8,10))==6){// febbraio ha 28 giorni
							System.out.println("La data inserita non è corretta, giugno ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(5,7))>30 && Integer.parseInt(data.substring(8,10))==9){// febbraio ha 28 giorni
							System.out.println("La data inserita non è corretta, settembre ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(5,7))>30 && Integer.parseInt(data.substring(8,10))==11){// febbraio ha 28 giorni
							System.out.println("La data inserita non è corretta, novembre ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(5,7))>31) {//nessun mese ha più di 31 giorni
							System.out.println("La data inserita contiene caratteri non validi, riprova!");
							return null;
						}
						//se non ci sono errori restituisci la stringa corretta
						System.out.println(data=data.substring(0,4)+"-"+data.substring(5,7)+"-"+data.substring(8, 10));
						return data=data.substring(0,4)+"-"+data.substring(5,7)+"-"+data.substring(8, 10);
					} //fine parsing AAAA-MM-GG
						//formato AAAA-GG-MM
						//faccio il parsing della data in formato AAAA-MM-GG dopo aver controllato la correttezza delle date
						//data.substring(5,6) è il mese data.substring(8,9) è il giorno data.substring(0,4) è l'anno
						if(Integer.parseInt(data.substring(5,7))>=12 && Integer.parseInt(data.substring(8,10))<=12) {
						if(Integer.parseInt(data.substring(5,7))>28 && Integer.parseInt(data.substring(8,10))==2){// febbraio ha 28 giorni
							System.out.println("La data inserita non è corretta, febbraio ha 28 giorni!\n");
							return null;
						}
						//se l'anno di nascita è bisestile febbraio ha 29 giorni
						//controllo se l'anno è bisestile con il metolo isLeap(Long anno) di java.time, che restituisce un booleano
						if(Year.isLeap(Long.parseLong(data.substring(0,4)))&& Integer.parseInt(data.substring(5,7))>29 && Integer.parseInt(data.substring(8,10))==2){
							System.out.println("La data inserita non è corretta, febbraio ha 29 giorni in un anno bisestile!\n");
							return null;
						}
						//settembre,novembre,aprile e giugno hanno 30 giorni
						if(Integer.parseInt(data.substring(5,7))>30 && Integer.parseInt(data.substring(8,10))==4){// febbraio ha 28 giorni
							System.out.println("La data inserita non è corretta, aprile ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(5,7))>30 && Integer.parseInt(data.substring(8,10))==6){// febbraio ha 28 giorni
							System.out.println("La data inserita non è corretta, giugno ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(5,7))>30 && Integer.parseInt(data.substring(8,10))==9){// febbraio ha 28 giorni
							System.out.println("La data inserita non è corretta, settembre ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(5,7))>30 && Integer.parseInt(data.substring(8,10))==11){// febbraio ha 28 giorni
							System.out.println("La data inserita non è corretta, novembre ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(5,7))>31) {//nessun mese ha più di 31 giorni
							System.out.println("La data inserita contiene caratteri non validi, riprova!");
							return null;
						}
						//se non ci sono errori restituisci la stringa corretta
						System.out.println(data.substring(0,4)+"-"+data.substring(8, 10)+"-"+data.substring(5, 7));
						return data=data.substring(0,4)+"-"+data.substring(8, 10)+"-"+data.substring(5, 7);
					}	
					}
					else {
						System.out.println("La data inserita contiene caratteri non validi, riprova!");
						return null;
					}
					
				}//fine parsing AAAA-GG-MM/AAAA-MM-GG
				else return null;// controllo di sicurezza
		     }//fine controllo per date di lunghezza 10 caratteri
			if(lunghezzadata==9) {//inizio controllo date di lunghezza 9 caratteri
				//trovo le posizioni dei separatori
				for(int i=0; i<data.length();i++) {//inizio ricerca separatori 
					if(!(Character.isDigit(data.charAt(i)))) {
						indicisep.add(i);
					}	
				}//fine ricerca posizioni separatori 
				//controllo caratteri
				if(indicisep.size()==0 || indicisep.size()>2) {//se la data contiene solo numeri o più di due caratteri non numerici (i separatori) ritorna errore 
					System.out.println("La data inserita contiene caratteri non validi, riprova!");
					return null;
					
				}//fine controllo caratteri 
				//salvo gli indici dei separatori in una variabile
				int indice1=indicisep.get(0);
				int indice2=indicisep.get(1);
				
				//se il formato è G-MM-AAAA (M-GG-AAAA) i separatori sono in indice 1 e 4
				//nel formato di data corretto il carattere usato come separatore dev'essere lo stesso
				//controllo che i caratteri in posizioni 1 e 4 siano uguali
				if(indicisep.get(0)==1 && indicisep.get(1)==4) {
					if((data.substring(1,2)).equals(data.substring(4,5))) {
						//formato M-GG-AAAA
						//faccio il parsing della data in formato local date dopo aver controllato la coerenza di giorni e mesi
						//data.substring(0,2) è il giorno data.substring(3,5) è il mese data.substring(6,10) l'anno
						if(Integer.parseInt(data.substring(2,4))>12) {
						if(Integer.parseInt(data.substring(2,4))>28 && Integer.parseInt(data.substring(0,1))==2){// febbraio ha 28 giorni
							System.out.println("La data inserita non è corretta, febbraio ha 28 giorni!\n");
							return null;
						}
						//se l'anno di nascita è bisestile febbraio ha 29 giorni
						//controllo se l'anno è bisestile con il metolo isLeap(Long anno) di java.time, che restituisce un booleano
						if(Year.isLeap(Long.parseLong(data.substring(5,9)))&& Integer.parseInt(data.substring(2,4))>29 && Integer.parseInt(data.substring(0,1))==2){
							System.out.println("La data inserita non è corretta, febbraio ha 29 giorni in un anno bisestile!\n");
							return null;
						}
						//settembre,novembre,aprile e giugno hanno 30 giorni
						if(Integer.parseInt(data.substring(0,1))>30 && Integer.parseInt(data.substring(3,4))==4){
							System.out.println("La data inserita non è corretta, aprile ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(2,4))>30 && Integer.parseInt(data.substring(0,1))==6){
							System.out.println("La data inserita non è corretta, giugno ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(2,4))>30 && Integer.parseInt(data.substring(0,1))==9){
							System.out.println("La data inserita non è corretta, settembre ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(2,4))>31){//nessun mese ha più di 30 giorni
							System.out.println("Formato di data non valido, riprova!\n");
							return null;
						}
						//se non ci sono errori restituisci la stringa corretta
						System.out.println(data.substring(5,9)+"-"+"0"+data.substring(0, 1)+"-"+data.substring(2, 4));
						return data=data.substring(5,9)+"-"+"0"+data.substring(0, 1)+"-"+data.substring(2, 4);
					}
						//formato G-MM-AAAA
						//faccio il parsing della data in formato local date dopo aver controllato la coerenza di giorni e mesi
						//data.substring(0,2) è il giorno data.substring(3,5) è il mese data.substring(6,10) l'anno
						if(Integer.parseInt(data.substring(2,4))<=12) {
						//se non ci sono errori restituisci la stringa corretta
						System.out.println(data.substring(5,9)+"-"+data.substring(2, 4)+"-"+"0"+data.substring(0, 1));
						return data=data.substring(5,9)+"-"+data.substring(2, 4)+"-"+"0"+data.substring(0, 1);
					 }	
					}
						
					else {
						System.out.println("Formato di data non valido, riprova!\n");
						return null;
					}// fine parsin
				}
				
				
				//se il formato è GG-M-AAAA (MM-G-AAAA) i separatori sono in indice 2 e 4
				//nel formato di data corretto il carattere usato come separatore dev'essere lo stesso
				//controllo che i caratteri in posizioni 2 e 4 siano uguali
				if(indicisep.get(0)==2 && indicisep.get(1)==4) {
					if((data.substring(2,3)).equals(data.substring(4,5))) {
						//formato GG-M-AAAA
						//faccio il parsing della data in formato local date dopo aver controllato la coerenza di giorni e mesi
						//data.substring(0,2) è il giorno data.substring(3,5) è il mese data.substring(6,10) l'anno
						if(Integer.parseInt(data.substring(0,2))>=12 && Integer.parseInt(data.substring(3,4))<10) {
						if(Integer.parseInt(data.substring(0,2))>28 && Integer.parseInt(data.substring(3,4))==2){// febbraio ha 28 giorni
							System.out.println("La data inserita non è corretta, febbraio ha 28 giorni!\n");
							return null;
						}
						//se l'anno di nascita è bisestile febbraio ha 29 giorni
						//controllo se l'anno è bisestile con il metolo isLeap(Long anno) di java.time, che restituisce un booleano
						if(Year.isLeap(Long.parseLong(data.substring(5,9)))&& Integer.parseInt(data.substring(0,2))>29 && Integer.parseInt(data.substring(3,5))==2){
							System.out.println("La data inserita non è corretta, febbraio ha 29 giorni in un anno bisestile!\n");
							return null;
						}
						//settembre,novembre,aprile e giugno hanno 30 giorni
						if(Integer.parseInt(data.substring(0,2))>30 && Integer.parseInt(data.substring(3,4))==4){
							System.out.println("La data inserita non è corretta, aprile ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(0,2))>30 && Integer.parseInt(data.substring(3,4))==6){
							System.out.println("La data inserita non è corretta, giugno ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(0,2))>30 && Integer.parseInt(data.substring(3,4))==9){
							System.out.println("La data inserita non è corretta, settembre ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(0,2))>31){//nessun mese ha più di 30 giorni
							System.out.println("Formato di data non valido, riprova!\n");
							return null;
						}
						//se non ci sono errori restituisci la stringa corretta
						System.out.println(data.substring(5,9)+"-"+"0"+data.substring(3, 4)+"-"+data.substring(0, 2));
						return data=data.substring(5,9)+"-"+"0"+data.substring(3, 4)+"-"+data.substring(0, 2);
					}
						//formato MM-G-AAAA
						//faccio il parsing della data in formato local date dopo aver controllato la coerenza di giorni e mesi
						//data.substring(0,2) è il giorno data.substring(3,5) è il mese data.substring(6,10) l'anno
						if(Integer.parseInt(data.substring(0,2))<=12) {
						//se non ci sono errori restituisci la stringa corretta
						System.out.println(data.substring(5,9)+"-"+data.substring(0, 2)+"-"+"0"+data.substring(3, 4));
						return data=data.substring(5,9)+"-"+data.substring(0, 2)+"-"+"0"+data.substring(3, 4);
					 }	
					}
						
					else {
						System.out.println("Formato di data non valido, riprova!\n");
						return null;
					}// fine parsin
				}
				
				
				//se il formato è AAAA-M-GG (AAAA-G-MM) i separatori sono in indice 4 e 6
				if(indicisep.get(0)==4 && indicisep.get(1)==6) {//inizio parsing AAAA-M-GG
					//nel formato di data corretto il carattere usato come separatore dev'essere lo stesso
					//controllo che i caratteri in posizioni 4 e 7 siano uguali
					if((data.substring(4,5)).equals(data.substring(6,7))) {
						//formato AAAA-G-MM
						//faccio il parsing della data in formato AAAA-G-MM dopo aver controllato la correttezza delle date
						//data.substring(5,6) è il giorno data.substring(8,9) è il mese data.substring(0,4) è l'anno
						if(Integer.parseInt(data.substring(5,6))<10 && Integer.parseInt(data.substring(7,9))<=12) {
						//se non ci sono errori restituisci la stringa corretta
						System.out.println(data.substring(0,4)+"-"+data.substring(7, 9)+"-"+"0"+data.substring(5,6));
						return data=data.substring(0,4)+"-"+data.substring(7, 9)+"-"+"0"+data.substring(5,6);
					} //fine parsing AAAA-M-G
						
						
						
						//formato AAAA-M-GG
						//faccio il parsing della data in formato AAAA-MM-G dopo aver controllato la correttezza delle date
						//data.substring(5,6) è il mese data.substring(8,9) è il giorno data.substring(0,4) è l'anno
						if(Integer.parseInt(data.substring(7,9))>12 ) {
							if(Integer.parseInt(data.substring(5,6))>12) {
								System.out.println("La data inserita è in un formato non corretto!");
								return null;
							}
							if(Integer.parseInt(data.substring(7,9))>28 && Integer.parseInt(data.substring(5,6))==2){// febbraio ha 28 giorni
								System.out.println("La data inserita non è corretta, febbraio ha 28 giorni!\n");
								return null;
							}
							//se l'anno di nascita è bisestile febbraio ha 29 giorni
							//controllo se l'anno è bisestile con il metolo isLeap(Long anno) di java.time, che restituisce un booleano
							if(Year.isLeap(Long.parseLong(data.substring(0,4)))&& Integer.parseInt(data.substring(7,9))>29 && Integer.parseInt(data.substring(5,6))==2){
								System.out.println("La data inserita non è corretta, febbraio ha 29 giorni in un anno bisestile!\n");
								return null;
							}
							//settembre,novembre,aprile e giugno hanno 30 giorni
							if(Integer.parseInt(data.substring(7,9))>30 && Integer.parseInt(data.substring(5,6))==4){
								System.out.println("La data inserita non è corretta, aprile ha 30 giorni!\n");
								return null;
							}
							if(Integer.parseInt(data.substring(7,9))>30 && Integer.parseInt(data.substring(5,6))==6){
								System.out.println("La data inserita non è corretta, giugno ha 30 giorni!\n");
								return null;
							}
							if(Integer.parseInt(data.substring(7,9))>30 && Integer.parseInt(data.substring(5,6))==9){
								System.out.println("La data inserita non è corretta, settembre ha 30 giorni!\n");
								return null;
							}
							if(Integer.parseInt(data.substring(0,2))>31){//nessun mese ha più di 30 giorni
								System.out.println("Formato di data non valido, riprova!\n");
								return null;
							}
						//se non ci sono errori restituisci la stringa corretta
						System.out.println(data.substring(0,4)+"-"+"0"+data.substring(5, 6)+"-"+data.substring(7,9));
						return data=data.substring(0,4)+"-"+"0"+data.substring(5, 6)+"-"+data.substring(7,9);
					}	//Fine parsing AAAA-M-GG
						
					}
					else {
						System.out.println("La data inserita non è valida, riprova!");
						return null;
					}
					
				}//fine parsing AAAA-G-MM/AAAA-M-GG
				
				
				
				
				//se il formato è AAAA-M-GG (AAAA-GG-M) i separatori sono in indice 4 e 7
				if(indicisep.get(0)==4 && indicisep.get(1)==7) {//inizio parsing AAAA-GG-MM
					//nel formato di data corretto il carattere usato come separatore dev'essere lo stesso
					//controllo che i caratteri in posizioni 4 e 7 siano uguali
					if((data.substring(4,5)).equals(data.substring(7,8))) {
						//formato AAAA-GG-M
						//faccio il parsing della data in formato AAAA-GG-M dopo aver controllato la correttezza delle date
						//data.substring(5,7) è il giorno data.substring(8,9) è il mese data.substring(0,4) è l'anno
						if(Integer.parseInt(data.substring(5,7))>12 && Integer.parseInt(data.substring(8,9))<10) {
						if(Integer.parseInt(data.substring(5,7))>28 && Integer.parseInt(data.substring(8,9))==2){// febbraio ha 28 giorni
							System.out.println("La data inserita non è corretta, febbraio ha 28 giorni!\n");
							return null;
						}
						//se l'anno di nascita è bisestile febbraio ha 29 giorni
						//controllo se l'anno è bisestile con il metolo isLeap(Long anno) di java.time, che restituisce un booleano
						if(Year.isLeap(Long.parseLong(data.substring(0,4)))&& Integer.parseInt(data.substring(5,7))>29 && Integer.parseInt(data.substring(8,9))==2){
							System.out.println("La data inserita non è corretta, febbraio ha 29 giorni in un anno bisestile!\n");
							return null;
						}
						//settembre,novembre,aprile e giugno hanno 30 giorni
						if(Integer.parseInt(data.substring(5,7))>30 && Integer.parseInt(data.substring(8,9))==4){// febbraio ha 28 giorni
							System.out.println("La data inserita non è corretta, aprile ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(5,7))>30 && Integer.parseInt(data.substring(8,9))==6){// febbraio ha 28 giorni
							System.out.println("La data inserita non è corretta, giugno ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(5,7))>30 && Integer.parseInt(data.substring(8,9))==9){// febbraio ha 28 giorni
							System.out.println("La data inserita non è corretta, settembre ha 30 giorni!\n");
							return null;
						}
						if(Integer.parseInt(data.substring(5,7))>31) {//nessun mese ha più di 31 giorni
							System.out.println("La data inserita contiene caratteri non validi, riprova!");
							return null;
						}
						//se non ci sono errori restituisci la stringa corretta
						System.out.println(data.substring(0,4)+"-"+"0"+data.substring(8, 9)+"-"+data.substring(5,7));
						return data=data.substring(0,4)+"-"+"0"+data.substring(8, 9)+"-"+data.substring(5,7);
					} //fine parsing AAAA-GG-M
						//formato AAAA-MM-G
						//faccio il parsing della data in formato AAAA-MM-G dopo aver controllato la correttezza delle date
						//data.substring(5,7) è il mese data.substring(8,9) è il giorno data.substring(0,4) è l'anno
						if(Integer.parseInt(data.substring(8,9))<10 ) {
							if(Integer.parseInt(data.substring(5,7))>12) {
								System.out.println("La data inserita non è valida, riprova!");
								return null;
							}
						//se non ci sono errori restituisci la stringa corretta
						System.out.println(data.substring(0,4)+"-"+data.substring(5, 7)+"-"+"0"+data.substring(8,9));
						return data=data.substring(0,4)+"-"+data.substring(5, 7)+"-"+"0"+data.substring(8,9);
					}	//Fine parsing AAAA-MM-G
						//formato AAAA-M-GG
						
					}
					else {
						System.out.println("La data inserita contiene caratteri non validi, riprova!");
						return null;
					}
					
				}//fine parsing AAAA-G-MM/AAAA-M-GG
				else return null;// controllo di sicurezza
		     }//fine controllo per date di lunghezza 9 caratteri
			
			
			if(lunghezzadata==8) {//inizio controllo date di lunghezza 8 caratteri
				//trovo le posizioni dei separatori
				for(int i=0; i<data.length();i++) {//inizio ricerca separatori 
					if(!(Character.isDigit(data.charAt(i)))) {
						indicisep.add(i);
					}	
				}//fine ricerca posizioni separatori 
				//controllo caratteri
				if(indicisep.size()==0 || indicisep.size()>2) {//se la data contiene solo numeri o più di due caratteri non numerici (i separatori) ritorna errore 
					System.out.println("La data inserita contiene caratteri non validi, riprova!");
					return null;
					
				}//fine controllo caratteri 
				//salvo gli indici dei separatori in una variabile
				int indice1=indicisep.get(0);
				int indice2=indicisep.get(1);
				
				
				
				//se il formato è AAAA-M-G (AAAA-G-M) i separatori sono in indice 4 e 6
				//nel formato di data corretto il carattere usato come separatore dev'essere lo stesso
				//controllo che i caratteri in posizioni 2 e 5 siano uguali
				if(indicisep.get(0)==4 && indicisep.get(1)==6) {
					if((data.substring(4,5)).equals(data.substring(6,7))) {
						//formato AAAA-M-G -- AAAA-MM-GG
						//deve stare attento l'utente a non confondersi
						//faccio il parsing della data in formato local date dopo aver controllato la coerenza di giorni e mesi
						//data.substring(7) è il giorno data.substring(5,6) è il mese data.substring(0,4) l'anno
						if(Integer.parseInt(data.substring(5,6))<10 && Integer.parseInt(data.substring(7))<10) {
							//se non ci sono errori restituisci la stringa corretta
							System.out.println(data.substring(0,4)+"-"+"0"+data.substring(5,6)+"-"+"0"+data.substring(7));
							return data=data.substring(0,4)+"-"+"0"+data.substring(5,6)+"-"+"0"+data.substring(7);
						}
					}
				}
						
						//formato M-G-AAAA -- G-M-AAAA
						//i separatori sono in posizione 1 e 3
					    //deve stare attento l'utente a non confondersi
						//faccio il parsing della data in formato local date dopo aver controllato la coerenza di giorni e mesi
						//data.substring(0,2) è il giorno data.substring(3,5) è il mese data.substring(6,10) l'anno
						if(indicisep.get(0)==1 && indicisep.get(1)==3) {
							if((data.substring(1,2)).equals(data.substring(3,4))) {
								//formato M-G-AAAA -- G-M-AAAA
								//deve stare attento l'utente a non confondersi
								//faccio il parsing della data in formato local date dopo aver controllato la coerenza di giorni e mesi
								//data.substring(7) è il giorno data.substring(5,6) è il mese data.substring(0,4) l'anno
								if(Integer.parseInt(data.substring(0,1))<10 && Integer.parseInt(data.substring(2,3))<10) {
									//se non ci sono errori restituisci la stringa corretta
									System.out.println(data.substring(4,8)+"-"+"0"+data.substring(2,3)+"-"+"0"+data.substring(0,1));
									return data=data.substring(4,8)+"-"+"0"+data.substring(2,3)+"-"+"0"+data.substring(0,1);
								}
					 }	
					}
						else return null;
				}
					else {
						System.out.println("La data inserita non è valida, riprova!\n");
						return null;}
			}
		System.out.println("La data inserita non è valida, riprova!\n");
		return null;
		}
	
				
					
	
	/**
	 * Questo metodo registra un nuovo utente nel file Utenti.csv
	 * esegue il controllo dell'età e richiede il login di un utente
	 * maggiorenne per completare la registrazione di un minorenne
	 * inoltre cripta le password salvate e assegna l'ID agli utenti
	 */
	private static void Registrati() {
		String dataparsed;
		Scanner obj=new Scanner(System.in); //creo un oggetto della classe Scanner, che serve tra le altre cose a gestire gli input da tastiera
		LocalDate dataoggi=LocalDate.now(); //trovo la data attuale
		String nome;
		while(true) {// il nome deve avere almeno 2 lettere
			System.out.println("Nome: ");
			nome=obj.nextLine();// il metodo nextLine() restituisce la stringa corrispondente all'ultimo input su tastiera da parte dell'utente
			if(nome.matches(".*[0-9\\p{Punct}\\s].*")) {//se il nome contiene numeri o simboli speciali o spazi è un errore
				System.out.println("Il nome non può contenere numeri, caratteri speciali o spazi, riprova!");
				continue;
			}
			if(nome.length()<2) {
				System.out.println("Il nome fornito deve avere almeno due caratteri, riprova!");
				continue;
			}
			break;
		}
		String cognome;
		while(true) {
			System.out.println("Cognome: ");
			cognome=obj.nextLine();
			if(cognome.matches(".*[0-9\\p{Punct}\\s].*")) {//se il cognome contiene numeri o simboli speciali o spazi è un errore
				System.out.println("Il cognome non può contenere numeri, caratteri speciali o spazi, riprova!");
				continue;
			}
			if(cognome.length()<2) {
				System.out.println("Il cognome fornito deve avere almeno due caratteri, riprova!");
				continue;
			}
			break;
		}
		
		String data;
		while(true) {
			System.out.println("Data di nascita (AAAA-MM-GG): ");
			data=obj.nextLine();
			dataparsed= CineMaX.controlloData(data);
			if(dataparsed==null) continue;// se il controllo della data fallisce riprova
			LocalDate dataLD=LocalDate.parse(dataparsed);//conversto la stringa in formato localdate
			Period età=Period.between(dataLD, dataoggi);// trovo anni mesi e giorni passati dalla data inserita ad oggi
			//se hai più di 125 anni ripeti la registrazione
			if(età.getYears()>=125) {
				System.out.println("Attenzione, risulta che hai più di 125 anni, riprova!");
				continue;
			}
			//se hai meno di 18 anni devi loggare con l'account di un maggiornenne per conferma
			if(età.getYears()<18) {
				System.out.println("Attenzione, risulta che hai meno di 18 anni anni");
				System.out.println("per proseguire effettua il login con l'account");
				System.out.println("di un utente maggiorenne\n");
				Guest garante=CineMaX.login();
				if(garante instanceof Proiezionista || garante instanceof Bigliettaio) {
					//i dipendenti sono per forza maggiorenni
					System.out.println("Verifica dell'età completate correttamente!");
					break;
				}
				if(garante instanceof Cliente) {// se l'utente è cliente controllo l'età
					String keyage=CineMaX.dataDinascita(((Cliente) garante).getIDUtente());// ottengo la data di nascita dell'utente di verifica
					LocalDate keyageLD=LocalDate.parse(keyage);//converto l'età in formato localdate
					età=Period.between(keyageLD, dataoggi);// trovo anni mesi e giorni passati dalla data di controllo ad oggi
					//se hai più di 125 anni ripeti la registrazione
					if(età.getYears()>=18) {// se l'utente è maggiorenne sono passati 18 anni
						System.out.println("Verifica dell'età completate correttamente!");
						break;
					}
					else {
						System.out.println("Attenzione l'utente selezionato ha meno di 18 anni, riprova!");
						continue;
					    }
					}
				}
				break;
			}
		String ind;
		while(true) {
			System.out.println("Città di residenza: ");
			ind=obj.nextLine();
			if(ind.matches(".*[0-9\\p{Punct}\\s].*")) {//se il nome della città contiene numeri o simboli speciali o spazi è un errore
				System.out.println("Il nome di una città non può contenere numeri, caratteri speciali o spazi, riprova!");
				continue;
			}
			if(ind.length()<1) {// se non viene indicata almeno una lettera per la città restituisci errore
				System.out.println("Il nome di una città ha almeno un carattere, riprova!");
				continue;
			}
			break;
		}
		String username;
		while(true) {//lo username deve avere almeno due caratteri
			System.out.println("Username: ");
			username=obj.nextLine();
			if(username.length()<2) {
				System.out.println("Lo username deve avere almeno due caratteri, riprova!");
				continue;
			}
			if(CineMaX.usernameInUse(username)==true) {
				while(CineMaX.usernameInUse(username)!=false) {
					System.out.println("Username già in uso, si prega di cambiarlo! \n");
					System.out.println("Username: ");
					username=obj.nextLine();
				}
			}
			break;
		}
		String pword;
		while(true) {
			System.out.println("Password: ");
			pword=obj.nextLine();
			if(username.length()<2) {
				System.out.println("La password deve avere almeno un carattere, riprova!");
				continue;
			}
			break;
		}
		
		pword=CineMaX.sha256Hash(pword); //cripto la password
		String ID=CineMaX.assegnaIDstr(nome,cognome,username);//calcolo l'ID utente
		CineMaX.aggiungiUtente(ID,nome,cognome,username,pword,dataparsed,ind,"C");//scrivo il nuovo utente nel file utenti.csv
		System.out.println("Registrazione completata! Il tuo ID utente è: "+ID+"\n");
		System.out.println("grazie per averci dedicato due minuti!");

	}
	/**
	 * Questo metodo consente il login da parte degli utenti registrati.
	 * Quando viene eseguito, il metodo richiede di inserire username e 
	 * password, se questi sono presenti sul file Utenti.csv (letto riga per riga)
	 * il codice genera un oggetto che è istanza delle classi Bigliettaio, Proiezionista
	 * o Cliente a seconda del tipo dell account che sta effettuando il login
	 * @return un oggetto Guest che istanzia le classi Bigliettaio, Proiezionista o Cliente
	 */
	private static Guest login() {
		Cliente client;
		Proiezionista pro;
		Bigliettaio big;
		Scanner input=new Scanner(System.in);
		System.out.println("Username: ");
		String username=input.nextLine();
		System.out.println("Password: ");
		String pword=input.nextLine();
		//char[] criptpword=System.console().readPassword(input.nextLine());//nasconde la password a console (non funziona su IDE)
		//String pword=String.valueOf(criptpword);
		pword=CineMaX.sha256Hash(pword); //Cripta la password inserita per cofrontarla con quella nel file;
		try {
			FileReader filerd = new FileReader(percorsofile);
			BufferedReader buff= new BufferedReader(filerd);
			String riga;
			String[] campiriga=new String[8];
			buff.readLine();//salto la prima riga del testo
			while ((riga=buff.readLine())!=null) {
				campiriga=riga.split(",");
				if((username.trim()).equals(campiriga[3].trim())&&(pword.trim()).equals(campiriga[4].trim())) {
					switch(campiriga[7]) {
					case "P": 
						pro=new Proiezionista();
						System.out.println("Benvenuto"+campiriga[3]);
						return pro;
					case "B":
						 big=new Bigliettaio();
						 System.out.println("Benvenuto"+campiriga[3]);
						return big;
					case "C":
						client= new Cliente(campiriga[1], campiriga[2], campiriga[0]);	
						System.out.println("Benvenuto"+campiriga[3]);
						return client;
					}
					}
				}
			buff.close();
			filerd.close();
		} catch (FileNotFoundException e) {
			System.out.println("Errore critico, file utenti.csv non trovato!");
		} catch (IOException e) {
			System.out.println("Errore critico, file utenti.csv non valido!");
		}
		System.out.println("Username o password non valido");
		return null;
	}

/**
 * Questo è il metodo main
 * @param args
 */
	public static void main(String[] args) {
		boolean On=true; //questa variabile serve per effettuare l'interruzione dell'esecuzione dell'applicazione
		Guest loggeduser;//questa variabile salva l'utente correntemente loggato
		while(On==true) {
			System.out.println("*****CineMaX*******");
			//cosa vuoi fare? loggarti, registrarti o proseguire come guest?
			//rimango nel ciclo di esecuzione principale fino a quando non chiudo l'app
			Scanner Kinput=new Scanner(System.in);//refresh dello scanner ad ogni iterazione per pulire la storia delle operazioni
			System.out.println("Cosa vuoi fare oggi?\n ");
			System.out.println("Digita il titolo anche parziale di un film per proseguire come guest ed effettuare la ricerca\n");
			System.out.println("Digita 1 per effettuare il login\n ");
			System.out.println("Digita 2 per registrarti\n");
			System.out.println("Digita 3 per proseguire come guest\n ");
			System.out.println("Digita 0 per uscire dall'applicazione\n ");
			String swtch=Kinput.nextLine();//Questa variabile serve come selettore per la modalità in cui si intende usare l'app, è di tipo string perchè l'input da tastiera è acquisit come stringa
			switch(swtch) {
			case "1":// login
				loggeduser=CineMaX.login();
				if (loggeduser instanceof Cliente) {//login come cliente
					
//					String datautente=CineMaX.dataDinascita(((Cliente) loggeduser).getIDUtente());
//					LocalDate datautenteLD=LocalDate.parse(datautente);
//					Period età=Period.between(datautenteLD, dataoggi);
					
					try {
						((Cliente)loggeduser).mostraMenuCliente();
					} catch (IOException e) {
						System.out.println("Errore critico, file proiezioni.csv non rilevato!");
					}
				}//fine login come cliente
					if (loggeduser instanceof Bigliettaio) {//inizio login come bigliettaio
						while(true) {
							System.out.println("Cosa vuoi fare oggi?\n ");
							System.out.println("Digita 1 per visualizzare le prenotazioni nella data odierna \n ");
							System.out.println("Digita 2 per cercare una prenotazione \n");
							System.out.println("Digita 0 per effettuare il logout\n ");
							String toDo=Kinput.nextLine();
							switch(toDo){
							case "1":
								try {
									((Bigliettaio)loggeduser).visualizzaPrenotazioniOdierne();
								} catch (FileNotFoundException e) {
									System.out.println("Errore critico, file prenotazioni.csv non trovato!");
								}
								continue;
							case"2":
								try {
									((Bigliettaio)loggeduser).cercaPrenotazione();
								} catch (FileNotFoundException e) {
									System.out.println("Errore critico, file prenotazioni.csv non trovato!");
								}
								continue;
							case"0":
								System.out.println("Grazie per aver usato la nostra app, a presto e buon lavoro!");
								break;
							default: 
								System.out.println("Scelta non valida, riprova");
								continue;
								
								
							}	
							break;
						}
					}//fine login come bigliettaio
					if (loggeduser instanceof Proiezionista) { //login come proiezionista
						while(true) {
							ArrayList<Proiezione>proiezioni=Proiezione.caricaProiezioni();// se loggo come proiezionista prima di tutto carico il file proiezioni altrimenti i metodi del proiezionista non funzionano
							ArrayList<Prenotazione> prenotazioni=Prenotazione.caricaPrenotazioni();//carico la lista di tutte le prenotazioni come richiesto dal metodo per eliminare una proiezione
							System.out.println("Cosa vuoi fare oggi?\n ");
							System.out.println("Digita 1 per aggiungere una proiezione al palinsesto \n ");
							System.out.println("Digita 2 per rimuovere una proiezione dal palinsesto \n");
							System.out.println("Digita 3 per modificare la data di una proiezione dal palinsesto \n");
							System.out.println("Digita 0 per effettuare il logout\n ");
							String toDo=Kinput.nextLine();
							switch(toDo){
							case "1":
								int anno=0;
								int durata=0;
								int età=0;
								double prezzo=0.0;
								System.out.println("Digitare il titolo della proiezione\n ");
								String titolo=Kinput.nextLine();
								System.out.println("Digitare il genere della proiezione\n ");
								String genere=Kinput.nextLine();
								System.out.println("Digitare il regista della proiezione\n ");
								String regista=Kinput.nextLine();
								System.out.println("Digitare l'anno di pubblicazione della proiezione\n ");
								String annotmp=Kinput.nextLine();
								try{anno=Integer.parseInt(annotmp);}//trasformo la stringa in int come richiesto da un oggetto di tipo film, usato per generare una proiezione
								catch(NumberFormatException e){//Integer.parseInt() genera una number format exception se la stringa è in formato numerico non valido!
								System.out.println("Formato non valido, il valore inserito non rappresenta un numero intero!");	
								}
								System.out.println("Digitare la durata della proiezione espressa in minuti\n ");
								String duratatmp=Kinput.nextLine();
								try{durata=Integer.parseInt(duratatmp);}//trasformo la stringa in int come richiesto da un oggetto di tipo film, usato per generare una proiezione
								catch(NumberFormatException e){//Integer.parseInt() genera una number format exception se la stringa è in formato numerico non valido!
								System.out.println("Formato non valido, il valore inserito non rappresenta un numero intero!");	
								}
								System.out.println("Digitare l'età minima per assistere alla proiezione\n ");
								String etàtmp=Kinput.nextLine();
								try{età=Integer.parseInt(etàtmp);}//trasformo la stringa in int come richiesto da un oggetto di tipo film, usato per generare una proiezione
								catch(NumberFormatException e){//Integer.parseInt() genera una number format exception se la stringa è in formato numerico non valido!
								System.out.println("Formato non valido, il valore inserito non rappresenta un numero intero!");	
								}
								System.out.println("Digitare la data della proiezione (AAAA-MM-GG)\n");
								String data=Kinput.nextLine();
								System.out.println("Digitare l'ora della proiezione (HH:MM)\n");
								String ora=Kinput.nextLine();
								LocalDateTime parsedfromstring=LocalDateTime.parse(data+"T"+ora+":00");//creo l'oggetto dalla stringa, la formattazione dev'essere(AAAA-MM-GGTHH:MM:SS), i secondi sono sempre 00
								System.out.println("Digitare il prezzo del biglietto (euro.centesimi)\n");
								String prezzotmp=Kinput.nextLine();
								try{prezzo=Double.parseDouble(prezzotmp);}//trasformo la stringa in double come richiesto dal costruttore Proiezione
								catch(NumberFormatException e) {
									System.out.println("Formato non valido, il valore inserito non rappresenta un numero decimale!");
								}
								int posti=200; //serve per creare la proiezione
								Film f=new Film(titolo,genere,regista,anno,durata,età);//creo l'oggetto film per la proiezione
								Proiezione pro=new Proiezione(f,parsedfromstring,prezzo,posti);//creo la proiezione da aggiungere
								((Proiezionista)loggeduser).aggiungiProiezioneAlPalinsesto(pro, proiezioni);//aggiungo la proiezione al palinsesto
								continue;
							case"2":
								System.out.println("Digitare il titolo della proiezione da eliminare");
								String titolo1=Kinput.nextLine();
								System.out.println("Inserisci la data e ora (es: 2026-05-20 18:30:00):");
                                String dataStringa = Kinput.nextLine();

// si converte la stringa
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
LocalDateTime dataOraParsed = LocalDateTime.parse(dataStringa.trim(), formatter);

// 3. Chiama il metodo RISPETTANDO L'ORDINE DEI PARAMETRI:
// (1° titolo, 2° dataOra, 3° proiezioni, 4° prenotazioni)
((Proiezionista) loggeduser).rimuoviProiezioneDalPalinsesto(titolo1, dataOraParsed, proiezioni, prenotazioni);//rimuove la proiezione per titolo e ora 
								continue;	
								case "3":
    System.out.println("--- MODIFICA ORARIO PROIEZIONE ---");
    
    System.out.println("Digitare il titolo della proiezione da modificare:");
    String titoloMod = Kinput.nextLine();

    // Rinominiamo la variabile in formatterMod per evitare la duplicazione
    DateTimeFormatter formatterMod = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    try {
        System.out.println("Digitare la DATA e ORA ATTUALE della proiezione (es. 2026-05-20 18:30:00):");
        String testoDataVecchia = Kinput.nextLine();
        LocalDateTime dataOraAttuale = LocalDateTime.parse(testoDataVecchia.trim(), formatterMod);

        System.out.println("Digitare la NUOVA DATA e ORA desiderata (es. 2026-05-20 21:00:00):");
        String testoDataNuova = Kinput.nextLine();
        LocalDateTime nuovaDataOra = LocalDateTime.parse(testoDataNuova.trim(), formatterMod);

        ((Proiezionista) loggeduser).modificaDataOraProiezione(titoloMod, dataOraAttuale, nuovaDataOra, proiezioni);

    } catch (Exception e) {
        System.out.println("ERRORE: Formato data non valido! Assicurati di usare il formato yyyy-MM-dd HH:mm:ss");
    }
    continue;
							case"0":
								System.out.println("Grazie per aver usato la nostra app, a presto e buon lavoro!");
								break;
							default:
								System.out.println("Scelta non valida, riprova");
								continue;
								
							}
								
							break;
						}
					}//fine login come proiezionista
					continue;//fine login
			case "2"://registrazione
				CineMaX.Registrati();
				continue;// se mi registro devo comunque effettuare il login dopo se voglio usare l'app
			case "3"://accesso come guest
				loggeduser=new Guest();
				System.out.println("Benvenuto! stai procedendo come Guest\n");
				while(true) {
					System.out.println("Cosa vuoi fare oggi?\n ");
					System.out.println("Digita 1 per cercare una proiezione \n ");
					System.out.println("Digita 2 per registrarti\n");
					System.out.println("Digita il titolo anche parziale di un film per effettuare una ricerca rapida\n");
					System.out.println("Digita 0 per effettuare il logout\n ");//puoi uscire senza registrarti, metti che vuoi loggarti col profilo di un amico
					String toDo=Kinput.nextLine();
					switch(toDo){
					case "1":
						try {
							loggeduser.cercaProiezione();
							continue;
						} catch (FileNotFoundException e) {
							System.out.println("Errore critico, file proiezioni.csv non trovato!");
						}
						break;
					case"2":
						CineMaX.Registrati();
						break; //se ti registri esci dalla modalità guest
					case"0":
						System.out.println("Grazie per aver usato la nostra app, a presto!");
						break;
					default:
						try {
							loggeduser.cercaProiezionePerTitolo(toDo);
							continue;
						} catch (FileNotFoundException e) {
							System.out.println("Errore critico, file proiezioni.csv non trovato!");
							break;
						}
					}
					break;
				}
				break;//fine accesso modalità guest
			case"0": //logout
				On=false;// spengo il ciclo  di esecuzione while(On==true)
				continue;//fine logout
			default://Se non si fa nessuna scelta si può comunque cercare un film per titolo anche parziale accedendo poi automaticamente come guest
				loggeduser=new Guest();
				try {
					loggeduser.cercaProiezionePerTitolo(swtch);
				} catch (FileNotFoundException e) {
					System.out.println("Errore critico, file proiezioni.csv non trovato!");
				}
				System.out.println("Benvenuto! stai procedendo come Guest\n");
				while(true) {
					System.out.println("Cosa vuoi fare oggi?\n ");
					System.out.println("Digita 1 per cercare una proiezione \n ");
					System.out.println("Digita 2 per registrarti\n");
					System.out.println("Digita 0 per effettuare il logout\n ");//puoi uscire senza registrarti, metti che vuoi loggarti col profilo di un amico
					String toDo=Kinput.nextLine();
					switch(toDo){
					case "1":
						try {
							loggeduser.cercaProiezione();
							continue;
						} catch (FileNotFoundException e) {
							System.out.println("Errore critico, file proiezioni.csv non trovato!");
							break;
						}
						
					case "2":
						CineMaX.Registrati();
						break;
					case "0":
						System.out.println("Arrivederci! Grazie per aver usato la nostra app!");
						break;
					default:
						try {
							loggeduser.cercaProiezione();
							continue;
						}
						catch(FileNotFoundException e){
							System.out.println("Errore critico, file proiezioni.csv non trovato!");
							break;
						}
						
					}
					break;
				}
			}//fine switch principale
		}//fine ciclo esecuzione
	System.out.println("Arrivederci! Grazie per aver usato la nostra app, a presto!");	
   }//fine main
	
		
 }//fine classe

