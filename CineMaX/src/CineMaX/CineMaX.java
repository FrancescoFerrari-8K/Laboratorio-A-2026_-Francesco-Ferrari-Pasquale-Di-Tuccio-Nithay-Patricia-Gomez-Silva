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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class CineMaX {
	//i campisono il percorso del file utenti che mi serve per aggiungere gli e il suo percorso assoluto 
	private static final File file=new File("C:\\Users\\franc\\Documents\\GitHub\\Laboratorio-A-2026_-Francesco-Ferrari-Pasquale-Di-Tuccio-Nithay-Patricia-Gomez-Silva\\data\\Utenti.csv");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			filerd.close();
			return false;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return "";
	}
	//assegnare un ID a un utente
	//nella versione più semplice è il numero di registrazione, cioè il numero della riga in cui 
	//è salvato l'utente
	private static int assegnaID() {
		Path percorso=Paths.get(percorsofile);// il conteggio delle righe ha bisogno di un oggeto della classe paths come percorso file
		int ID=0;
		try {
			
			long numerorighe=Files.lines(percorso).count()-7;//conta le righe del file
			  ID=(int) numerorighe;
			  return ID;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ID;
	}
	//mia idea: prime 2 lettere nome,ultime2 cognome,prime 2 nome utente e numero di riga.
	//adesso il programma usa questo metodo
	private static String assegnaIDstr(String nome, String cognome, String username) {
		Path percorso=Paths.get(percorsofile);
		String ID="";
		try {
			long numerorighe=(Files.lines(percorso).count())-7;//conta le righe del file e toglie le righe iniziali per far partire da 1 il conto dei clienti
			  String numeroID=Long.toString(numerorighe);
			  return ID=""+nome.charAt(0)+nome.charAt(1)+cognome.charAt(0)+cognome.charAt(1)+username.charAt(0)+username.charAt(1)+numeroID;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ID;
		
	}
	//registrare un nuovo utente
	private static void Registrati() {
		Scanner obj=new Scanner(System.in); //creo un oggetto della classe Scanner, che serve tra le altre cose a gestire gli input da tastiera
		
		System.out.println("Nome: ");
		String nome=obj.nextLine();// il metodo nextLine() restituisce la stringa corrispondente all'ultimo input su tastiera da parte dell'utente
		System.out.println("Cognome: ");
		String cognome=obj.nextLine();
		System.out.println("Data di nascita (AAAA-MM-GG): ");
		String data=obj.nextLine();
		LocalDate dataLD=LocalDate.parse(data);//conversto la stringa in formato localdate
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
		obj.close();//chiudo lo stream da tastiera per evitare memory leaks
		String ID=CineMaX.assegnaIDstr(nome,cognome,username);//calcolo l'ID utente
		CineMaX.aggiungiUtente(ID,nome,cognome,username,pword,data,ind,"C");//scrivo il nuovo utente nel file utenti.csv
		System.out.println("Registrazione completata! Il tuo ID utente è: "+ID+"\n");
		System.out.println("grazie per averci dedicato due minuti!");
	}
	//login
	//se nome utente e password sono presenti nel file utenti restituisce true, altrimenti false
	private static Guest login() {
		Guest Utente;
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
						Utente=new Proiezionista();
						return Utente;
					case "B":
						 Utente=new Bigliettaio();
						return Utente;
					case "C":
						Utente= new Cliente(campiriga[1], campiriga[2], campiriga[0]);	
					}
					}
				}
			buff.close();
			filerd.close();
			System.out.println("Username o password non valido");
			return Utente;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Utente;
	}

	public static void main(String[] args) {
		// All'avvio l'app mostra menù iniziale in cui è possibile fare 3 cose: loggarsi, registrarsi o proseguire come utente non registrato (guest).
		// ecc ecc

		System.out.println("*****CineMaX*******");
		// bisogna fare una grafichina carina!!
		
		//cosa vuoi fare? loggarti, registrarti o proseguire come guest?

		Guest User = new Guest(); // creo un utente guest per garantire l'utilizzo minimo dell'app
		CineMaX.Registrati();


			

			

	

 }
}
