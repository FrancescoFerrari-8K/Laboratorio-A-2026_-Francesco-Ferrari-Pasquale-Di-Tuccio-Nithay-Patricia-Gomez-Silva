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
import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class CineMaX {
	//i campisono il percorso del file utenti che mi serve per aggiungere gli e il suo percorso assoluto 
	private static final File file=new File("..\\data\\Utenti.csv");
	private static final String percorsofile=file.getAbsolutePath();//per farlo funzionare a prescindere dalla macchina
	//Metodi
	//aggiungo un utente al file
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
	//il nome utente è già in uso?
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
	//trova la data di nascita di un utente per la registrazione di un minorenne
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
	//Hash con sha256 per le pword
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
		return "";
	}
	//assegnare un ID a un utente
	//l'ID è assegnato utilizzando le prime 2 lettere nome,ultime2 cognome,prime 2 nome utente e numero di riga.
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
	//controllare la sintassi della data di nascita inserita in fase di registrazione
	//e la sistema per salvarla nel file utenti sempre nello stesso formato indipendentemente
	//dai separatori inseriti dall'utente, dall'aver scambiato il mese con il giorno o dall'aver scritto 1
	//al posto di 01 per gennaio o il primo giorno del mese.
	
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
						System.out.println("La data inserita contiene caratteri non validi, riprova!");
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
								System.out.println("La data inserita è in un formato non corretto!");
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
						System.out.println("Formato di data non valido, riprova!\n");
						return null;}
			}
		System.out.println("Formato di data non valido, riprova!\n");
		return null;
		}
	
				
					
	
	//registrare un nuovo utente
	private static void Registrati() {
		Scanner obj=new Scanner(System.in); //creo un oggetto della classe Scanner, che serve tra le altre cose a gestire gli input da tastiera
		
		System.out.println("Nome: ");
		String nome=obj.nextLine();// il metodo nextLine() restituisce la stringa corrispondente all'ultimo input su tastiera da parte dell'utente
		System.out.println("Cognome: ");
		String cognome=obj.nextLine();
		System.out.println("Data di nascita (AAAA-MM-GG): ");
		String data="";
		while(true) {
			data=obj.nextLine();
			if(CineMaX.controlloData(data)==null) continue;
			break;
		}
		//LocalDate dataLD=LocalDate.parse(data);//conversto la stringa in formato localdate
		System.out.println("Città di residenza: ");
		String ind=obj.nextLine();
		System.out.println("Username: ");
		String username=obj.nextLine();
		if(CineMaX.usernameInUse(username)==true) {
			while(CineMaX.usernameInUse(username)!=false) {
				System.out.println("Username già in uso, si prega di cambiarlo! \n");
				System.out.println("Username: ");
				username=obj.nextLine();
			}
		}
		System.out.println("Password: ");
		String pword=obj.nextLine();
		pword=CineMaX.sha256Hash(pword); //cripto la password
		String ID=CineMaX.assegnaIDstr(nome,cognome,username);//calcolo l'ID utente
		CineMaX.aggiungiUtente(ID,nome,cognome,username,pword,data,ind,"C");//scrivo il nuovo utente nel file utenti.csv
		System.out.println("Registrazione completata! Il tuo ID utente è: "+ID+"\n");
		System.out.println("grazie per averci dedicato due minuti!");
	}
	//login
	//se nome utente e password sono presenti nel file utenti restituisce true, altrimenti false
	private static Guest login() {
		Cliente client;
		Proiezionista pro;
		Bigliettaio big;
		Scanner input=new Scanner(System.in);
		System.out.println("Username: ");
		String username=input.nextLine();
		System.out.println("Password: ");
		String pword=CineMaX.sha256Hash(input.nextLine()); //Cripta la password inserita per cofrontarla con quella nel file;
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

//inizio metodo main
	public static void main(String[] args) {
		// All'avvio l'app mostra menù iniziale in cui è possibile fare 3 cose: loggarsi, registrarsi o proseguire come utente non registrato (guest).
		// ecc ecc
		boolean On=true; //questa variabile serve per effettuare l'interruzione dell'esecuzione dell'applicazione
		Guest loggeduser;//questa variabile salva l'utente correntemente loggato
		while(On==true) {
			System.out.println("*****CineMaX*******");
			// bisogna fare una grafichina carina!!
			//cosa vuoi fare? loggarti, registrarti o proseguire come guest?
			//rimango nel ciclo grande fino a quando non chiudo l'app
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
					try {
						((Cliente)loggeduser).mostraMenuCliente(true);
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
								break;
							case"2":
								try {
									((Bigliettaio)loggeduser).cercaPrenotazione();
								} catch (FileNotFoundException e) {
									System.out.println("Errore critico, file prenotazioni.csv non trovato!");
								}
								break;
							case"0":
								System.out.println("Grazie per aver usato la nostra app, a presto e buon lavoro!");
								break;
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
								break;
							case"2":
								System.out.println("Digitare il titolo della proiezione da eliminare");
								String titolo1=Kinput.nextLine();
								((Proiezionista)loggeduser).rimuoviProiezioneDalPalinsesto(titolo1,proiezioni, prenotazioni);// rimuove la prenotazione per titolo
								break;	
							case"0":
								System.out.println("Grazie per aver usato la nostra app, a presto e buon lavoro!");
								break;
								
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
						} catch (FileNotFoundException e) {
							System.out.println("Errore critico, file proiezioni.csv non trovato!");
						}
						continue;
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
						} catch (FileNotFoundException e) {
							System.out.println("Errore critico, file proiezioni.csv non trovato!");
						}
						break;
					case "2":
						CineMaX.Registrati();
						break;
					case "0":
						System.out.println("Grazie per aver usato la nostra app, a presto!");
						break;
						
					}
					break;
				}
			}//fine switch principale
		}//fine ciclo esecuzione
	System.out.println("Arrivederci! Grazie per aver usato la nostra app, a presto!");	
   }//fine main
	
		
 }//fine classe

