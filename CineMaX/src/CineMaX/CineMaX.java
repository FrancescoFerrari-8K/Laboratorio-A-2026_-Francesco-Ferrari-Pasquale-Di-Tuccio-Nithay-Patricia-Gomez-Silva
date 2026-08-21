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
						return pro;
					case "B":
						 big=new Bigliettaio();
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Username o password non valido");
		return null;
		
	}

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
			System.out.println("Digita 1 per effettuare il login\n ");
			System.out.println("Digita 2 per registrarti\n");
			System.out.println("Digita 3 per proseguire come guest\n ");
			System.out.println("Digita 0 per uscire dall'applicazione\n ");
			String swtch=Kinput.nextLine();//Questa variabile serve come selettore per la modalità in cui si intende usare l'app, è di tipo string perchè l'input da tastiera è acquisit come stringa
			switch(swtch) {
			case "1":
				loggeduser=CineMaX.login();
				if (loggeduser instanceof Cliente) {
					try {
						((Cliente)loggeduser).mostraMenuCliente(true);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (loggeduser instanceof Bigliettaio) {
						while(true) {
							System.out.println("Cosa vuoi fare oggi?\n ");
							System.out.println("Digita 1 per visualizzare le prenotazioni nella data odierna \n ");
							System.out.println("Digita 2 per cercare una prenotazione \n");
							System.out.println("Digita 0 per effettuare il logout\n ");
							String toDo=Kinput.nextLine();
							switch(toDo){
							case "1":
								break;
							case"2":
								try {
									((Bigliettaio)loggeduser).cercaPrenotazione();
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								}
								break;
							case"0":
								System.out.println("Grazie per aver usato la nostra app, a presto e buon lavoro!");
								break;
							}	
							break;
						}
					}
					if (loggeduser instanceof Proiezionista) {
						while(true) {
							System.out.println("Cosa vuoi fare oggi?\n ");
							System.out.println("Digita 1 per aggiungere una proiezione al palinsesto \n ");
							System.out.println("Digita 2 per rimuovere una proiezione dal palinsesto \n");
							System.out.println("Digita 3 per modificare la data di una proiezione dal palinsesto \n");
							System.out.println("Digita 0 per effettuare il logout\n ");
							String toDo=Kinput.nextLine();
							switch(toDo){
							case "1":
								//String titolo=Kinput.nextLine();
								((Proiezionista)loggeduser).aggiungiProiezioneAlPalinsesto(null, null); 
								break;
							case"2":
								((Proiezionista)loggeduser).rimuoviProiezioneDalPalinsesto(null,null, null);
								break;	
							case"0":
								System.out.println("Grazie per aver usato la nostra app, a presto e buon lavoro!");
								break;
								
							}
								
							break;
						}
					}
					continue;

					}
			case "2":
				CineMaX.Registrati();
				continue;// se mi registro devo comunque effettuare il login dopo se voglio usare l'app
			case "3":
				loggeduser=new Guest();
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
							e.printStackTrace();
						}
						break;
					case"2":
						CineMaX.Registrati();
						break; //se ti registri esci dalla modalità guest
					case"0":
						System.out.println("Grazie per aver usato la nostra app, a presto!");
						break;
					default:
						System.out.println("hai digitato un carattere non valido, riprova!\n");
						continue;
					}
					break;//esci dalla modalità guest
				}
				break;
			case"0":
				On=false;
				continue;
			default:
				System.out.println("hai digitato un carattere non valido, riprova!\n");
				continue;
			}//fine switch principale
		}//fine ciclo esecuzione
	System.out.println("Arrivederci! Grazie per aver usato la nostra app, a presto!");	
   }//fine main
		
		
 }//fine classe

