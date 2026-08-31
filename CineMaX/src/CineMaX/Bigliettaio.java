package CineMaX;

import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Questa classe implementa i metodi che gestiscono le richieste degli utenti loggati come Bigliettaio.
 * @author Luca Gabriel Chindris
 */
public class Bigliettaio extends Guest {
		//--------------------------------------------------------------------------------------------------------------------------------------------------------------
		// Campi (non presenti perchè non necessari).
		//--------------------------------------------------------------------------------------------------------------------------------------------------------------
		// Costruttori (non presenti perchè non necessari).
		//--------------------------------------------------------------------------------------------------------------------------------------------------------------
		// Metodi
	
//Inizio metodo cercaPrenotazione().
	/**
	 * Questo metodo permette la ricerca di prenotazioni secondo diversi criteri dal file Prenotazioni.csv ed eventualmente
	 * la visualizzazione dettagliata di una di quelle trovate.
	 * Il metodo effettua stampe a video e se serve restituisce il vettore di oggetti Prenotazione corrispondenti alle prenotazioni trovate con la ricerca,
	 * null nel caso in cui la ricerca avesse 0 risultati.
	 * @return Array di oggetti Prenotazione contenente gli oggetti Prenotazione corrispondenti alle prenotazioni trovate con la ricerca,
	 * null nel caso in cui la ricerca avesse 0 risultati.
	 * @throws FileNotFoundException Eccezione legata all'uso del file Prenotazioni.csv.
	 */
	public Prenotazione[] cercaPrenotazione() throws FileNotFoundException {
		
		int limiteRic = 6000; //Limite numero risultati della ricerca.
		int numRisRicerca; //Contatore numero risultati.
		String scelta = "2"; //Variabile per opzioni scelta, impostata default a "2" cioè ricerca per nome e cognome del cliente.
		boolean sceltaOk; //Variabile che indica se scelta inserita è valida o no (serve per ciclo do while in cui è contenuta tutta la ricerca, inizializ nel ciclo).
		boolean prenotazOk; //Variabile boolean per dire se la prenotazione che si sta considerando attualmente rispetta criterio ricerca; dichiarata qui perchè...
		//...serve in qualsiasi ricerca si scelga.
		
		String inputStringint; //Variabile di appoggio che serve per prendere in input un int. Si prende l'input con la .nextLine() mettendolo in inputString e poi...
		//...si fa Integer.parseInt(inputString). Bisogna gestire il fatto che l'input potrebbe non essere stato un intero, e in quel caso la .parseInt() lancia...
		//...NumberFormatException, e quindi serve blocco try-catch(NumberFormatException e) con try che include la .parseInt().
		//L'alternativa è prendere direttamente l'input con .nextInt() che però è più complicato.
		//Prima cosa la .nextInt() è rognosa perchè, ammettendo che l'utente mette effettivamente un intero, l'utente scrive l'intero e poi schiaccia invio e allora...
		//...l'input è intero+\n e la .nextInt() "mangia" solo l'intero lasciando il \n "in sospeso" come input inserito e per questo dopo ogni .nextInt() serve...
		//...praticamente sempre una .nextLine() per "mangiare" quel \n (che altrimenti rimane "in sospeso" come input inserito e rischia di sballare tutti gli...
		//inserimenti successivi).
		//Seconda cosa l'input potrebbe non essere un intero, e in quel caso la .nextInt() lancia InputMismatchException che va gestita con blocco try-catch ma...
		//...soprattutto come per ogni .next*() fallita non viene "mangiato" l'input sbagliato e quindi rimane "in sospeso" come input inserito e rischia di...
		//...sballare tutti gli inserimenti successivi, perciò nel blocco catch bisogna fare .nextLine() per "mangiare" l'input sbagliato.
		boolean inputStringintOk; //Leggere prima il senso di inputStringint. inputStringintOk serve per dire se l'input inserito è effettivamente un int o no.
		/*In sostanza la struttura dell'inserimento di un int che deve andare in una variabile int variabileint è
		do {
			inputStringintOk=true;
			try {
				inputStringint = .nextLine();
				variabileint = Integer.parseInt(inputStringint);
			
			} catch (NumberFormatException e) {
				inputStringintOk = false;
				System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
			}
		} while (inputStringintOk == false);
		//codice che riguarda variabileint
		*/
		
		DateTimeFormatter formatterDataITA = DateTimeFormatter.ofPattern("dd/MM/yyyy"); //Variabile per formato data italiano.
		
		Scanner scFile = new Scanner(new File("../data/Prenotazioni.csv")); //scFile è lettore file prenotazioni.
		scFile.useDelimiter("\n"); //Il separatore per distinguere una "cosa" letta dal file dalla successiva è l'a-capo, quindi ogni .next legge una riga del file.
		scFile.next(); //Salto la prima riga del file prenotazioni che è l'intestazione.
		int numvirgoleintestaz = 7; //Numero virgole intestazione file prenotazioni Prenotazioni.csv.
		
		Prenotazione[] risRicerca; //Vettore che rappresenta il risultato della ricerca cioè contiene le prenotazioni che rispettano il...
		//...criterio scelto.
		
		System.out.println("Ricerca di una prenotazione");
		
		Scanner sc = new Scanner(System.in);
		
		do { //Inizio ciclo do while in cui è contenuto lo switch che effettua tutta la ricerca. Il while è while(sceltaOk == true). 
			numRisRicerca = 0; ////Reset contatore numero risultati.
			scFile = new Scanner(new File("../data/prenotazioni.csv")); //Reset lettore file prenotazioni.
			scFile.useDelimiter("\n"); //Il separatore per distinguere una "cosa" letta dal file dalla successiva è l'a-capo, quindi ogni .next() legge una riga del file.
			scFile.next(); //Salto la prima riga del file prenotazioni che è l'intestazione.
			risRicerca = new Prenotazione[limiteRic]; //Reset vettore che rappresenta il risultato della ricerca cioè contiene le prenotazioni che rispettano il...
			//...criterio scelto.
			
			System.out.println("Selezionare un criterio per la ricerca:");
			System.out.println("1=per codice prenotazione");
			System.out.println("2=per nome e cognome del cliente");
			System.out.println("3=per titolo");
			System.out.println("4=per data");
			System.out.println("0=per annullare la ricerca");
			System.out.println("Inserire criterio scelto:");
			
			sceltaOk = true; //All'inizio, e ogni volta che si ripete il do while in cui è contenuta tutta la ricerca (e questo ripetere succede quando l'utente...
//...inserisce in input un'opzione non valida) è settata a true. Se l'utente inserisce un'opzione valida allora entra nei vari case delle possibili opzioni di ricerca...
//...e in quei case sceltaOk non viene modificata, quindi rimane true, e quindi quando si esce dai case e si va al while si esce dal do while in cui è contenuta la...
//...ricerca. Viene modificata a false solo se viene inserita in input dall'utente un'opzione non valida in modo che arrivando al while si torni all'inizio del ciclo.
			
			scelta = sc.nextLine(); //Inserimento scelta utente del tipo di ricerca da effettuare.
			System.out.println("Scelta inserita: " + scelta);
			
			switch (scelta) {
			
				case "1": //Caso ricerca per codice prenotazione.
					
					int codicePrenotazRic = 0; //Variabile che conterrà il codice prenotazione inserito in input dall'utente, impostata default a 0.
					System.out.println("Ricerca per codice prenotazione");
					System.out.println("Inserire il codice prenotazione:");
					
					do {
						inputStringintOk=true;
						try {
							inputStringint = sc.nextLine();
							codicePrenotazRic = Integer.parseInt(inputStringint);
						
						} catch (NumberFormatException e) {
							inputStringintOk = false;
							System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
						}
					} while (inputStringintOk == false);
					
					System.out.println("Il codice prenotazione inserito è:" + codicePrenotazRic);
					
					while(scFile.hasNext()) { //Ciclo per leggere una prenotazione dal file delle prenotazioni e verificare se rispetta requisiti.
						
						prenotazOk = false;
						
						Prenotazione prenotaz = estraiPrenotazione(scFile,numvirgoleintestaz); //Estraggo una prenotazione dal file delle prenotazioni e la metto in...
						//prenotaz.
						
						//Inizio blocco per confronto tra criterio inserito e stesso criterio nella riga/prenotazione letta da file.
						
						//Confronto codici.
						if (codicePrenotazRic == prenotaz.getIDPrenotazione())
							prenotazOk = true;
						//Fine blocco confronto titoli.
						
						if(prenotazOk == true) { //Se la prenotazione è corretta...
							if (numRisRicerca >= limiteRic) {  //...controllo se ho raggiunto (o, per qualche strano motivo, superato) numero massimo di...
							//...risultati della ricerca...
								System.out.println("Numero massimo di risultati per la ricerca (" + limiteRic + ") raggiunto, non è possibile continuare la "
										+ "ricerca");
								System.out.println("Per effettuare una ricerca completa inserire criteri più restrittivi");
								sceltaOk = false;
								break;
							} 
							else { //Altrimenti se la prenotazione è corretta e non ho raggiunto numero massimo di risultati...
								risRicerca[numRisRicerca] = prenotaz; //...salvo prenotazione correntemente letta da file in vettore dei risultati della ricerca,...
								System.out.println();
								System.out.println(numRisRicerca+1); //...stampo numero del risultato perchè servirà per eventuale visualizzaPrenotazione...
								//...(stampa dettagli di 1 prenotazione),...
								System.out.println(prenotaz.toString(true)); //...e stampo la prenotazione.
								numRisRicerca++;
							}
						} //Fine blocco if fatto se la prenotazione è corretta.
						
					} //Fine while che legge il file delle prenotazioni e controlla le prenotazioni.
					if (sceltaOk == true)
						System.out.println("La ricerca ha dato " + numRisRicerca + " risultati");
					break;
				
				case "2": //Caso ricerca per nome e cognome del cliente.
					
					String nomeRic = "", cognomeRic = ""; //Variabili che conterranno nome e cognome inseriti in input dall'utente, impostate default a stringa vuota...
					//...come ha senso che sia.
					
					System.out.println("Ricerca per nome e cognome del cliente");
					System.out.println("Inserire il nome del cliente:");
					nomeRic = sc.nextLine(); //Leggo nome cliente da ricercare e lo metto in nomeRic.
					System.out.println("Il nome cliente inserito è:" + nomeRic);
					
					System.out.println("Inserire il cognome del cliente:");
					cognomeRic = sc.nextLine(); //Leggo cognome cliente da ricercare e lo metto in cognomeRic.
					System.out.println("Il cognome cliente inserito è:" + cognomeRic);
					
					while(scFile.hasNext()) { //Ciclo per leggere una prenotazione dal file delle prenotazioni e verificare se rispetta requisiti.
						
						prenotazOk = false;
						
						Prenotazione prenotaz = estraiPrenotazione(scFile,numvirgoleintestaz); //Estraggo una prenotazione dal file delle prenotazioni e la metto in...
						//prenotaz.
						
						//Inizio blocco per confronto tra criterio inserito e stesso criterio nella riga/prenotazione letta da file.
						
						//Confronto nome e cognome cliente.
						if (nomeRic.compareTo(prenotaz.getNome() ) == 0 && cognomeRic.compareTo(prenotaz.getCognome() ) == 0)
							prenotazOk = true;
						//Fine blocco confronto nome e cognome cliente.
						
						if(prenotazOk == true) { //Se la prenotazione è corretta...
							if (numRisRicerca >= limiteRic) {  //...controllo se ho raggiunto (o, per qualche strano motivo, superato) numero massimo di...
							//...risultati della ricerca...
								System.out.println("Numero massimo di risultati per la ricerca (" + limiteRic + ") raggiunto, non è possibile continuare la "
										+ "ricerca");
								System.out.println("Per effettuare una ricerca completa inserire criteri più restrittivi");
								sceltaOk = false;
								break;
							} 
							else { //Altrimenti se la prenotazione è corretta e non ho raggiunto numero massimo di risultati...
								risRicerca[numRisRicerca] = prenotaz; //...salvo prenotazione correntemente letta da file in vettore dei risultati della ricerca,...
								System.out.println();
								System.out.println(numRisRicerca+1); //...stampo numero del risultato perchè servirà per eventuale visualizzaPrenotazione...
								//...(stampa dettagli di 1 prenotazione),...
								System.out.println(prenotaz.toString(true)); //...e stampo la prenotazione.
								numRisRicerca++;
							}
						} //Fine blocco if fatto se la prenotazione è corretta.
						
					} //Fine while che legge il file delle prenotazioni e controlla le prenotazioni.
					if (sceltaOk == true)
						System.out.println("La ricerca ha dato " + numRisRicerca + " risultati");
					break;
					
				case "3": //Caso ricerca per titolo.
					
					String titoloRic = ""; //Variabile che conterrà il titolo inserito in input dall'utente.
					String titoloTempLowercase, titoloRicLowercase; //Variabili che conterranno i titoli cercato e estratto dalla prenotaz corrente messi a minuscolo.
					String titoloParz; //Uso spiegato in confronto titoli.
					
					System.out.println("Ricerca per titolo");
					System.out.println("Inserire il titolo (anche parziale):");
					titoloRic = sc.nextLine(); //Leggo titolo da ricercare e lo metto in titoloRic.
					System.out.println("Il titolo inserito è:" + titoloRic);
					
					while(scFile.hasNext()) { //Ciclo per leggere una prenotazione dal file delle prenotazioni e verificare se rispetta requisiti.
						
						prenotazOk = false;
						
						Prenotazione prenotaz = estraiPrenotazione(scFile,numvirgoleintestaz); //Estraggo una prenotazione dal file delle prenotazioni e la metto...
						//...in prenotaz.
						
						//Inizio blocco per confronto tra criterio inserito e stesso criterio nella riga/prenotazione letta da file.
						
						//Metto entrambi i titoli (quello ricercato e quello della prenotazione considerata attualmente) a minuscolo per non far contare le maiuscole.
						titoloTempLowercase = prenotaz.getProiezione_Titolo().toLowerCase(); 
						titoloRicLowercase = titoloRic.toLowerCase(); 
						
						//Confronto titoli.
						if(titoloRicLowercase.length() == titoloTempLowercase.length()) //Caso titolo cercato ha lungh uguale al titolo prenotazione corrente.
							if(titoloTempLowercase.compareTo(titoloRicLowercase) == 0)
								prenotazOk = true;
						if(titoloRicLowercase.length() < titoloTempLowercase.length()) { //Caso titolo cercato ha lungh minore del titolo prenotazione corrente...
							//...quindi devo vedere se il titolo della prenotaz corrente contiene il titolo cercato; per farlo uso titoloParz che...
							//...contiene man mano pezzi del titolo della prenotaz corrente lunghi quanto il titolo cercato.
							//Es se cerco "evo", che è lungo 3, titoloParz conterrà man mano tutti i pezzi di lungh 3 del titolo della prenotaz corrente per vedere se...
							//...uno di questi pezzi è "evo".
							for(int i=0; i+titoloRicLowercase.length() <= titoloTempLowercase.length(); i++) { //For per vedere se il titolo della prenotaz corrente...
							//contiene il titolo cercato.
								titoloParz = titoloTempLowercase.substring(i, i+titoloRicLowercase.length());
								if( titoloParz.compareTo(titoloRicLowercase) == 0)
									prenotazOk = true;
							}
						}
						//Fine blocco confronto titoli.
						
						if(prenotazOk == true) { //Se la prenotazione è corretta...
							if (numRisRicerca >= limiteRic) {  //...controllo se ho raggiunto (o, per qualche strano motivo, superato) numero massimo di...
							//...risultati della ricerca...
								System.out.println("Numero massimo di risultati per la ricerca (" + limiteRic + ") raggiunto, non è possibile continuare la "
										+ "ricerca");
								System.out.println("Per effettuare una ricerca completa inserire criteri più restrittivi");
								sceltaOk = false;
								break;
							} 
							else { //Altrimenti se la prenotazione è corretta e non ho raggiunto numero massimo di risultati...
								risRicerca[numRisRicerca] = prenotaz; //...salvo prenotazione correntemente letta da file in vettore dei risultati della ricerca,...
								System.out.println();
								System.out.println(numRisRicerca+1); //...stampo numero del risultato perchè servirà per eventuale visualizzaPrenotazione...
								//...(stampa dettagli di 1 prenotazione),...
								System.out.println(prenotaz.toString(true)); //...e stampo la prenotazione.
								numRisRicerca++;
							}
						} //Fine blocco if fatto se la prenotazione è corretta.
						
					} //Fine while che legge il file delle prenotazioni e controlla le prenotazione.
					if (sceltaOk == true)
						System.out.println("La ricerca ha dato " + numRisRicerca + " risultati");
					break;	
					
				case "4": //Caso ricerca per data.
					
					LocalDate dataMinRic=LocalDate.MIN, dataMaxRic=LocalDate.MAX; //Variabili che conterranno le date inserite in input dall'utente.
					//Variabili di appoggio per le date (NB il formatter per formato data italiano è a inizio metodo cercaPrenotazione() ).
					int giornoMinRic=0, giornoMaxRic=0, meseMinRic=0, meseMaxRic=0, annoMinRic=0, annoMaxRic=0;
					boolean dataRicOk = false; //Variabile per controllo validità date. Impostata default a false per case "3".
					boolean dataRic2Ok = false; //Variabile per controllo validità seconda data quando sono richieste 2 date. Impostata default a false per case "3".
					String sceltaRicData = "3"; //Variabile per opzioni ricerca per data. Impostata default a opzione ricerca prenotazioni comprese tra due date.
					boolean sceltaRicDataOk; //Variabile che indica se scelta inserita è valida o no.
					
					System.out.println("Ricerca per data");
					System.out.println("Selezionare una delle seguenti opzioni:");
					System.out.println("1=prenotazioni prima di una certa data");
					System.out.println("2=prenotazioni dopo una certa data");
					System.out.println("3=prenotazioni comprese tra due date");
					System.out.println("Inserire l'opzione di ricerca per desiderata:");
					
					do { //Inizio ciclo do while in cui è contenuto lo switch che effettua la ricerca per data.
						sceltaRicDataOk = true;
						sceltaRicData = sc.nextLine();
						
						switch (sceltaRicData){
						
						case "1": //Caso ricerca prima di una certa data.
							System.out.println("Ricerca prenotazioni prima di una certa data");
							do {
								try {
									System.out.println("Inserire il giorno:");
									
									do {
										inputStringintOk=true;
										try {
											inputStringint = sc.nextLine();
											giornoMaxRic = Integer.parseInt(inputStringint);
										
										} catch (NumberFormatException e) {
											inputStringintOk = false;
											System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
										}
									} while (inputStringintOk == false);
									
									System.out.println("Inserire il mese:");
									
									do {
										inputStringintOk=true;
										try {
											inputStringint = sc.nextLine();
											meseMaxRic = Integer.parseInt(inputStringint);
										
										} catch (NumberFormatException e) {
											inputStringintOk = false;
											System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
										}
									} while (inputStringintOk == false);
									
									
									System.out.println("Inserire l'anno:");
									
									do {
										inputStringintOk=true;
										try {
											inputStringint = sc.nextLine();
											annoMaxRic = Integer.parseInt(inputStringint);
										
										} catch (NumberFormatException e) {
											inputStringintOk = false;
											System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
										}
									} while (inputStringintOk == false);
									
									
									dataMaxRic = LocalDate.of(annoMaxRic, meseMaxRic, giornoMaxRic);
									dataRicOk = true; //Se si arriva a questa riga vuol dire che non è stata sollevata eccezione con la .of e quindi la data inserita...
									//...è valida.
									System.out.println("La data inserita è:" + dataMaxRic.format(formatterDataITA) );
									System.out.println("Ricerca delle prenotazioni prima di " + dataMaxRic.format(formatterDataITA) );
								} catch (DateTimeException e) {
									dataRicOk = false;
									System.out.println("Data non valida. Inserire un'altra data");
								}
							} while (dataRicOk == false);
							
							while(scFile.hasNext()) { //Ciclo per leggere una prenotazione dal file delle prenotazioni e verificare se rispetta requisiti.
								
								prenotazOk = false;
								
								Prenotazione prenotaz = estraiPrenotazione(scFile,numvirgoleintestaz); //Estraggo una prenotazione dal file delle prenotazioni e la...
								//metto in prenotaz.
								
								//Inizio blocco per confronto tra criterio inserito e stesso criterio nella riga/prenotazione letta da file.
								
								//Confronto date.
								if (prenotaz.getProiezione_Data().toLocalDate().compareTo(dataMaxRic) < 0)
									prenotazOk = true;
								//Fine blocco confronto date.
								
								if(prenotazOk == true) { //Se la prenotazione è corretta...
									if (numRisRicerca >= limiteRic) {  //...controllo se ho raggiunto (o, per qualche strano motivo, superato) numero massimo di...
									//...risultati della ricerca...
										System.out.println("Numero massimo di risultati per la ricerca (" + limiteRic + ") raggiunto, non è possibile continuare la "
												+ "ricerca");
										System.out.println("Per effettuare una ricerca completa inserire criteri più restrittivi");
										sceltaOk = false;
										break;
									} 
									else { //Altrimenti se la prenotazione è corretta e non ho raggiunto numero massimo di risultati...
										risRicerca[numRisRicerca] = prenotaz; //...salvo prenotazione correntemente letta da file in vettore dei risultati della ricerca,...
										System.out.println();
										System.out.println(numRisRicerca+1); //...stampo numero del risultato perchè servirà per eventuale visualizzaPrenotazione...
										//...(stampa dettagli di 1 prenotazione),...
										System.out.println(prenotaz.toString(true)); //...e stampo la prenotazione.
										numRisRicerca++;
									}
								} //Fine blocco if fatto se la prenotazione è corretta.
								
							} //Fine while che legge il file delle prenotazioni e controlla le prenotazione.
							if (sceltaOk == true)
								System.out.println("La ricerca ha dato " + numRisRicerca + " risultati");
							break;
							
						case "2": //Caso ricerca dopo una certa data.
							System.out.println("Ricerca prenotazioni dopo una certa data");
							do {
								try {
									System.out.println("Inserire il giorno:");
									
									do {
										inputStringintOk=true;
										try {
											inputStringint = sc.nextLine();
											giornoMinRic = Integer.parseInt(inputStringint);
										
										} catch (NumberFormatException e) {
											inputStringintOk = false;
											System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
										}
									} while (inputStringintOk == false);
									
									
									System.out.println("Inserire il mese:");
									
									do {
										inputStringintOk=true;
										try {
											inputStringint = sc.nextLine();
											meseMinRic = Integer.parseInt(inputStringint);
										
										} catch (NumberFormatException e) {
											inputStringintOk = false;
											System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
										}
									} while (inputStringintOk == false);
									
									
									System.out.println("Inserire l'anno:");
									
									do {
										inputStringintOk=true;
										try {
											inputStringint = sc.nextLine();
											annoMinRic = Integer.parseInt(inputStringint);
										
										} catch (NumberFormatException e) {
											inputStringintOk = false;
											System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
										}
									} while (inputStringintOk == false);
									
									dataMinRic = LocalDate.of(annoMinRic, meseMinRic, giornoMinRic);
									dataRicOk = true; //Se si arriva a questa riga vuol dire che non è stata sollevata eccezione con la .of e quindi la data inserita...
									//...è valida.
									System.out.println("La data inserita è:" + dataMinRic.format(formatterDataITA) );
									System.out.println("Ricerca delle prenotazioni dopo " + dataMinRic.format(formatterDataITA) );
								} catch (DateTimeException e) {
									dataRicOk = false;
									System.out.println("Data non valida. Inserire un'altra data");
								}
							} while (dataRicOk == false);
							
							while(scFile.hasNext()) { //Ciclo per leggere una prenotazione dal file delle prenotazioni e verificare se rispetta requisiti.
								
								prenotazOk = false;
								
								Prenotazione prenotaz = estraiPrenotazione(scFile,numvirgoleintestaz); //Estraggo una prenotazione dal file delle prenotazioni e la...
								//metto in prenotaz.
								
								//Inizio blocco per confronto tra criterio inserito e stesso criterio nella riga/prenotazione letta da file.
								
								//Confronto date.
								if (prenotaz.getProiezione_Data().toLocalDate().compareTo(dataMinRic) > 0)
									prenotazOk = true;
								//Fine blocco confronto date.
								
								if(prenotazOk == true) { //Se la prenotazione è corretta...
									if (numRisRicerca >= limiteRic) {  //...controllo se ho raggiunto (o, per qualche strano motivo, superato) numero massimo di...
									//...risultati della ricerca...
										System.out.println("Numero massimo di risultati per la ricerca (" + limiteRic + ") raggiunto, non è possibile continuare la "
												+ "ricerca");
										System.out.println("Per effettuare una ricerca completa inserire criteri più restrittivi");
										sceltaOk = false;
										break;
									} 
									else { //Altrimenti se la prenotazione è corretta e non ho raggiunto numero massimo di risultati...
										risRicerca[numRisRicerca] = prenotaz; //...salvo prenotazione correntemente letta da file in vettore dei risultati della ricerca,...
										System.out.println();
										System.out.println(numRisRicerca+1); //...stampo numero del risultato perchè servirà per eventuale visualizzaPrenotazione...
										//...(stampa dettagli di 1 prenotazione),...
										System.out.println(prenotaz.toString(true)); //...e stampo la prenotazione.
										numRisRicerca++;
									}
								} //Fine blocco if fatto se la prenotazione è corretta.
								
							} //Fine while che legge il file delle prenotazioni e controlla le prenotazioni.
							if (sceltaOk == true)
								System.out.println("La ricerca ha dato " + numRisRicerca + " risultati");
							break;
						
						case "3": //Caso ricerca tra due date.
							System.out.println("Ricerca prenotazioni tra due date");
							do {
								try {
									if (dataRicOk == false) { //Questo if serve a non far reinserire la 1a data se si è sbagliata solo la 2a. dataOk di default...
										//parte false quindi la prima volta si entra di sicuro nell'if (come giusto che sia).
										System.out.println("Inserire il giorno della data minore (quella più indietro, nel passato):");
										
										do {
											inputStringintOk=true;
											try {
												inputStringint = sc.nextLine();
												giornoMinRic = Integer.parseInt(inputStringint);
											
											} catch (NumberFormatException e) {
												inputStringintOk = false;
												System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
											}
										} while (inputStringintOk == false);
										
										
										System.out.println("Inserire il mese della data minore (quella più indietro, nel passato):");
										
										do {
											inputStringintOk=true;
											try {
												inputStringint = sc.nextLine();
												meseMinRic = Integer.parseInt(inputStringint);
											
											} catch (NumberFormatException e) {
												inputStringintOk = false;
												System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
											}
										} while (inputStringintOk == false);
										
										
										System.out.println("Inserire l'anno della data minore (quella più indietro, nel passato):");
										
										do {
											inputStringintOk=true;
											try {
												inputStringint = sc.nextLine();
												annoMinRic = Integer.parseInt(inputStringint);
											
											} catch (NumberFormatException e) {
												inputStringintOk = false;
												System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
											}
										} while (inputStringintOk == false);
										
										
										dataMinRic = LocalDate.of(annoMinRic, meseMinRic, giornoMinRic);
										dataRicOk = true; //Se si arriva a questa riga vuol dire che non è stata sollevata eccezione con la .of e quindi la 1a data...
										//...inserita è valida.
										System.out.println("La data minore inserita è: " + dataMinRic.format(formatterDataITA) );
									}
									
									System.out.println("Inserire il giorno della data maggiore (quella più avanti, nel futuro):");
									
									do {
										inputStringintOk=true;
										try {
											inputStringint = sc.nextLine();
											giornoMaxRic = Integer.parseInt(inputStringint);
										
										} catch (NumberFormatException e) {
											inputStringintOk = false;
											System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
										}
									} while (inputStringintOk == false);
									
									
									System.out.println("Inserire il mese della data maggiore (quella più avanti, nel futuro):");
									
									do {
										inputStringintOk=true;
										try {
											inputStringint = sc.nextLine();
											meseMaxRic = Integer.parseInt(inputStringint);
										
										} catch (NumberFormatException e) {
											inputStringintOk = false;
											System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
										}
									} while (inputStringintOk == false);
									
									
									System.out.println("Inserire l'anno della data maggiore (quella più avanti, nel futuro):");
									
									do {
										inputStringintOk=true;
										try {
											inputStringint = sc.nextLine();
											annoMaxRic = Integer.parseInt(inputStringint);
										
										} catch (NumberFormatException e) {
											inputStringintOk = false;
											System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
										}
									} while (inputStringintOk == false);
									
									
									dataMaxRic = LocalDate.of(annoMaxRic, meseMaxRic, giornoMaxRic);
									
									if (dataMaxRic.compareTo(dataMinRic) < 0) { //Se la 2a data inserita è minore/più indietro della 1a, la 2a data non va bene.
										System.out.println("La seconda data inserita è minore/più indietro della prima data. Inserire un'altra data");
									}
									else { //Altrimenti la 2a data va bene.
										dataRic2Ok = true; //Se si arriva a questa riga vuol dire che non è stata sollevata eccezione con la .of e quindi la 2a data...
										//...inserita è valida.
										System.out.println("La data maggiore inserita è:" + dataMaxRic.format(formatterDataITA) );
										
										//Quindi arrivati qui entrambe le data sono a posto.
										System.out.println("Ricerca delle prenotazioni comprese tra " + dataMinRic.format(formatterDataITA) + " e " 
												+ dataMaxRic.format(formatterDataITA));
									}
									
								} catch (DateTimeException e) {
									if (dataRicOk == false) //Se dataOK è false l'eccezione è scattata con la .of legata alla 1a data...
										System.out.println("La prima data non è valida. Inserire un'altra data");
									else //altrimenti è scattata con la .of legata alla 2a data.
										System.out.println("La seconda data non è valida. Inserire un'altra data");
								}
							} while (dataRicOk == false || dataRic2Ok == false);
							
							while(scFile.hasNext()) { //Ciclo per leggere una prenotazione dal file delle prenotazioni e verificare se rispetta requisiti.
								
								prenotazOk = false;
								
								Prenotazione prenotaz = estraiPrenotazione(scFile,numvirgoleintestaz); //Estraggo una prenotazione dal file delle prenotazioni e la...
								//metto in prenotaz.
								
								//Inizio blocco per confronto tra criterio inserito e stesso criterio nella riga/prenotazione letta da file.
								
								//Confronto date.
								if (prenotaz.getProiezione_Data().toLocalDate().compareTo(dataMinRic) > 0 && prenotaz.getProiezione_Data().toLocalDate().compareTo(dataMaxRic) < 0)
									prenotazOk = true;
								//Fine blocco confronto date.
								
								if(prenotazOk == true) { //Se la prenotazione è corretta...
									if (numRisRicerca >= limiteRic) {  //...controllo se ho raggiunto (o, per qualche strano motivo, superato) numero massimo di...
										//...risultati della ricerca...
										System.out.println("Numero massimo di risultati per la ricerca (" + limiteRic + ") raggiunto, non è possibile continuare la "
												+ "ricerca");
										System.out.println("Per effettuare una ricerca completa inserire criteri più restrittivi");
										sceltaOk = false;
										break;
									} 
									else { //Altrimenti se la prenotazione è corretta e non ho raggiunto numero massimo di risultati...
										risRicerca[numRisRicerca] = prenotaz; //...salvo prenotazione correntemente letta da file in vettore dei risultati della ricerca,...
										System.out.println();
										System.out.println(numRisRicerca+1); //...stampo numero del risultato perchè servirà per eventuale visualizzaPrenotazione...
										//...(stampa dettagli di 1 prenotazione),...
										System.out.println(prenotaz.toString(true)); //...e stampo la prenotazione.
										numRisRicerca++;
									}
								} //Fine blocco if fatto se la prenotazione è corretta.
								
							} //Fine while che legge il file delle prenotazioni e controlla le prenotazioni.
							if (sceltaOk == true)
								System.out.println("La ricerca ha dato " + numRisRicerca + " risultati");
							break;
							
						default:
							sceltaRicDataOk = false;
							System.out.println("L'opzione scelta non è valida. Inserire un'altra opzione:");
						} //Fine switch in cui è contenuta la ricerca per data.
					} while (sceltaRicDataOk == false); //Fine ciclo do while in cui è contenuto lo switch che effettua la ricerca per data.
					
					break;
				
				case "0": //Caso ricerca annullata.
					return null;
					
				default:
					sceltaOk = false;
					System.out.println("L'opzione scelta non è valida");
			}
		} while (sceltaOk == false); //Fine ciclo do while in cui è contenuto lo switch che effettua tutta la ricerca.
		
		
		
		if (numRisRicerca > 0) { //Inizio blocco che svolge funzionalità visualizzare in dettaglio una delle prenotazioni cercate .
			
			String sceltaVisualizDettagl = "0"; //Variabile per scelta se visualizzare in dettaglio una delle prenotazioni cercate, inizializ default a "0" cioè no.
			int sceltaNumPrenotazVisualiz = 1; //Variabile che conterrà il numero della prenotaz scelta da visualizzare in dettaglio, inizializ default a 1 cioè...
											//...la prima, che sicuramente c'è xk se siamo entrati nel blocco if in cui qsto codice si trova allora c'è almeno 1 ris.
			double costoTot=0;
			System.out.println("Si desidera visualizzare i dettagli di una delle prenotazioni cercate?");
			System.out.println("Inserire 1 se sì, 0 altrimenti:");
			do { //Inizio ciclo per chiedere il numero della prenotazione da visualizzare in dettaglio. Il while è while(true) (xk deve andare avanti finchè...
			//...l'utente inserisce una scelta valida).
				sceltaVisualizDettagl = sc.nextLine();
				
				switch(sceltaVisualizDettagl) {
				
				case "0": //Caso visualiz dettagliata di una delle prenotazioni cercate rifiutata.
					return risRicerca;
					
				case "1": //Caso visualiz dettagliata di una delle prenotazioni cercate richiesta.
					System.out.println("Inserire il numero della prenotazione di cui si desidera visualizzare i dettagli:");
					do {
						do {
							inputStringintOk=true;
							try {
								inputStringint = sc.nextLine();
								sceltaNumPrenotazVisualiz = Integer.parseInt(inputStringint);
							
							} catch (NumberFormatException e) {
								inputStringintOk = false;
								System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
							}
						} while (inputStringintOk == false);
						
						if (sceltaNumPrenotazVisualiz <= 0 || sceltaNumPrenotazVisualiz > numRisRicerca) {
							System.out.println("Il numero inserito non è valido. Inserire un numero valido di una delle prenotazioni cercate:");
						} else {
							System.out.println(risRicerca[sceltaNumPrenotazVisualiz-1].toString(true)); //C'è il -1 perchè all'utente le prenotaz sono visualiz...
							//...numerate da 1 (e quindi anche la sua scelta), mentre nel vettore sono numerate da 0.
							costoTot = risRicerca[sceltaNumPrenotazVisualiz-1].getPrezzoBiglietto() * risRicerca[sceltaNumPrenotazVisualiz-1].getNPosti(); 
							System.out.println("Costo totale:" + costoTot);
							return risRicerca;
						}
					} while (true);
					
				default:
					System.out.println("L'opzione scelta non è valida. Inserire un'opzione valida:");
				}
			} while (true); //Fine ciclo per chiedere il numero della prenotazione da visualizzare in dettaglio. Il while è while(true) xk deve andare avanti finchè...
			//...l'utente inserisce una scelta valida.
		}  else {
			System.out.println("La ricerca non ha risultati quindi non è possibile visualizzare i dettagli di una delle prenotazioni cercate");
			return null;
		} //Fine blocco che svolge funzionalità visualizzare in dettaglio una delle prenotazioni cercate.
		
	}
//Fine metodo cercaPrenotazione().
	

//Inizio metodo visualizzaPrenotazioniOdierne().
	/**
	 * Questo metodo permette di visualizzare le prenotazioni odierne accedendo al file Prenotazioni.csv.
	 * @throws FileNotFoundException Eccezione legata all'uso del file Prenotazioni.csv.
	 */
	public void visualizzaPrenotazioniOdierne() throws FileNotFoundException {
//Questo metodo permette la visualizzazione delle prenotazioni odierne.
		
		int numRisRicerca = 0; //Contatore numero risultati.
		
		double costoTot=0;
		
		Scanner scFile = new Scanner(new File("../data/Prenotazioni.csv")); //scFile è lettore file prenotazioni.
		scFile.useDelimiter("\n"); //Il separatore per distinguere una "cosa" letta dal file dalla successiva è l'a-capo, quindi ogni .next legge una riga del file.
		scFile.next(); //Salto la prima riga del file prenotazioni che è l'intestazione.
		int numvirgoleintestaz = 7; //Numero virgole intestazione file prenotazioni Prenotazioni.csv.
		
		LocalDate dataOdierna = LocalDateTime.now().toLocalDate();
		
		System.out.println("Visualizzazione prenotazioni odierne");
		
		while(scFile.hasNext()) { //Ciclo per leggere una prenotazione dal file delle prenotazioni, verificare se rispetta requisiti ed eventuale stampa.
			
			Prenotazione prenotaz = estraiPrenotazione(scFile,numvirgoleintestaz); //Estraggo una prenotazione dal file delle prenotazioni e la metto in prenotaz.
			
			//Confronto date e stampa.
			if (prenotaz.getProiezione_Data().toLocalDate().isEqual(dataOdierna) == true) {
				numRisRicerca++;
				System.out.println(numRisRicerca);
				System.out.println(prenotaz.toString(true));
				costoTot = prenotaz.getPrezzoBiglietto() * prenotaz.getNPosti(); 
				System.out.println("Costo totale:" + costoTot);
			}
			//Fine blocco confronto date e stampa.
			
		} //Fine while che legge il file delle prenotazioni, controlla le prenotazioni ed eventualmente stampa.
		System.out.println("La ricerca ha dato " + numRisRicerca + " risultati");
		
	}
//Fine metodo visualizzaPrenotazioniOdierne().


//Inizio metodo estraiPrenotazione().
	/**
	 * Metodo di supporto ai metodi che richiedono di estrarre le prenotazioni contenute nel file Prenotazioni.csv.
	 * Il metodo assume che lo scanner dato in input è già predisposto per leggere una riga del file valida (e quindi se si è
	 * all'inizio si è già saltata la riga dell'intestazione, se si è alla fine del file non si chiama questo metodo, etc...).
	 * Il secondo parametro è il numero di virgole dell'intestazione che serve perchè se la riga letta dal file ha più virgole di 
	 * quel numero (non può averne di meno perchè almeno tante virgole quante ce ne sono nell'intestazione le deve avere) allora il titolo
	 * contiene delle virgole e quindi il codice agisce di conseguenza).
	 * @param scanner Scanner dato in input per leggere il file Prenotazioni.csv, predisposto per leggere una riga del file valida.
	 * @param numvirgoleintestaz Numero di virgole dell'intestazione del file Prenotazioni.csv.
	 * @return Oggetto Prenotazione corrispondente a una prenotazione letta/estratta dal file Prenotazioni.csv.
	 */
	private Prenotazione estraiPrenotazione(Scanner scanner, int numvirgoleintestaz) {
	
	int contaVirgole = 0; //Contatore delle virgole della riga che sto leggendo in questo momento, utile per capire se ci sono virgole in più rispetto...
	//...all'intestazione xk anche il titolo ha delle virgole al suo interno.
	int differenzaVirgole = 0; //Contatore della differenza delle virgole tra numero di virgole della riga/prenotazione che sto leggendo e numero standard di virgole.
	boolean virgolaDxTrovata = false; //True quando trovo la virgola che delimita a destra il titolo della riga/prenotazione che sto leggendo.
	int indiceVirgolaDxTitolo = 0; //Indice della posizione della virgola che delimita a destra il tiolo della riga/prenotazione che sto leggendo.
	
	int virgolaSx=0, virgolaDx=0; //Indici per tenere traccia di qual è la posizione delle virgole che contengono il campo attuale (servono perchè nelle...
	//...righe/prenotazioni del file più campi non hanno lunghezza fissata.
	
	String riga; //Variabile per contenere una riga/prenotazione estratta dal file delle prenotazioni.
	
	//Variabili Temp per contenere i dati estratti da una riga/prenotazione del file delle prenotazioni.
	String IDPrenotazioneStringTemp, IDUtenteTemp, NomeTemp, CognomeTemp; 
	int IDPrenotazioneIntTemp;
	String annoTemp, meseTemp, giornoTemp, oraTemp, minutoTemp, secondoTemp, titoloTemp;
	int NPostiTemp;
	double prezzo_bigliettoTemp;
	LocalDate dataTemp;
	LocalTime orarioTemp;
	LocalDateTime dataOrarioTemp;
	Prenotazione prenotazTemp;
	
	riga = scanner.next();
	
	//Questo blocco estrae l'ID prenotazione dalla riga/prenotazione letta dal file e lo mette in IDPrenotazioneTemp.
	while(virgolaDx < riga.length() && riga.charAt(virgolaDx) != ',') { //Aumentiamo virgolaDx fino a quando non arriviamo alla prima virgola, cioè la virgola a...
	//...dx dell'IDPrenotazione.
		virgolaDx++;
	}
	IDPrenotazioneStringTemp = riga.substring(virgolaSx, virgolaDx); //NB La substring fa -1 al valore dx, quindi è giusto così perchè bisogna prendere fino al carattere...
	//...a sx della virgola dx. Notare che a differenza delle volte successive la substring qua parte da virgolaSx, questo perchè siamo all'inizio della riga e...
	//non c'è una virgola che delimita a sx il 1o campo, cioè IDPrenotazione, e quindi in questo caso virgolaSx in realtà non è sulla virgola a sx del campo ma...
	//sul 1o carattere del campo IDPrenotazione.
	IDPrenotazioneIntTemp = Integer.parseInt(IDPrenotazioneStringTemp); //Converto l'IDPrenotazione in int come serve.
	
	//Questo blocco estrae l'ID utente del cliente presente nella prenotazione dalla riga/prenotazione letta dal file e lo mette in IDUtenteTemp.
	virgolaSx = virgolaDx; //La virgola a sx dell'IDUtente è la virgola a dx dell'IDPrenotazione.
	virgolaDx++; //Spostiamo virgolaDx sulla posizione del carattere dopo la virgola su cui stava, e quindi cioè sul primo carattere del IDUtente.
	while(virgolaDx < riga.length() && riga.charAt(virgolaDx) != ',') { //Aumentiamo virgolaDx fino a quando non arriviamo alla prima virgola, cioè la virgola a...
		//...dx del IDUtente.
			virgolaDx++;
	}
	IDUtenteTemp = riga.substring(virgolaSx+1, virgolaDx); //NB La substring fa -1 al valore dx, quindi è giusto così perchè bisogna prendere fino al carattere...
	//...a sx della virgola dx. 
	
	
	//Questo blocco estrae il nome del cliente presente nella prenotazione dalla riga/prenotazione letta dal file e lo mette in NomeTemp.
	virgolaSx = virgolaDx; //La virgola a sx del Nome è la virgola a dx del IDUtente.
	virgolaDx++; //Spostiamo virgolaDx sulla posizione del carattere dopo la virgola su cui stava, e quindi cioè sul primo carattere del Nome.
	while(virgolaDx < riga.length() && riga.charAt(virgolaDx) != ',') { //Aumentiamo virgolaDx fino a quando non arriviamo alla prima virgola, cioè la virgola a...
		//...dx del Nome.
			virgolaDx++;
	}
	NomeTemp = riga.substring(virgolaSx+1, virgolaDx); //NB La substring fa -1 al valore dx, quindi è giusto così perchè bisogna prendere fino al carattere...
	//...a sx della virgola dx.
	
	
	//Questo blocco estrae il cognome del cliente presente nella prenotazione dalla riga/prenotazione letta dal file e lo mette in CognomeTemp.
	virgolaSx = virgolaDx; //La virgola a sx del Cognome è la virgola a dx del Nome.
	virgolaDx++; //Spostiamo virgolaDx sulla posizione del carattere dopo la virgola su cui stava, e quindi cioè sul primo carattere del Cognome.
	while(virgolaDx < riga.length() && riga.charAt(virgolaDx) != ',') { //Aumentiamo virgolaDx fino a quando non arriviamo alla prima virgola, cioè la virgola a...
		//...dx del Cognome.
			virgolaDx++;
	}
	CognomeTemp = riga.substring(virgolaSx+1, virgolaDx); //NB La substring fa -1 al valore dx, quindi è giusto così perchè bisogna prendere fino al carattere...
	//...a sx della virgola dx.
	
	//Questo blocco estrae la data e l'orario della prenotazione dalla riga/prenotazione letta dal file e li mette in d\ataTemp e orarioTemp.
	virgolaSx = virgolaDx; //La virgola a sx di data+orario è la virgola a dx del Nome.
	virgolaDx++; //Spostiamo virgolaDx sulla posizione del carattere dopo la virgola su cui stava, e quindi cioè sul primo carattere di data+orario che è il...
	//...carattere " che delimita data+orario a sx
	while(virgolaDx < riga.length() && riga.charAt(virgolaDx) != ',') { //Aumentiamo virgolaDx fino a quando non arriviamo alla prima virgola, cioè la virgola a...
		//...dx di data+orario.
			virgolaDx++;
	}
	annoTemp = riga.substring(virgolaSx+2, virgolaSx+6);
	meseTemp = riga.substring(virgolaSx+7, virgolaSx+9);
	giornoTemp = riga.substring(virgolaSx+10, virgolaSx+12);
	dataTemp = LocalDate.of(Integer.parseInt(annoTemp), Integer.parseInt(meseTemp), Integer.parseInt(giornoTemp));
	oraTemp = riga.substring(virgolaSx+13, virgolaSx+15);
	minutoTemp = riga.substring(virgolaSx+16, virgolaSx+18);
	secondoTemp = riga.substring(virgolaSx+19, virgolaSx+21);
	orarioTemp = LocalTime.of(Integer.parseInt(oraTemp), Integer.parseInt(minutoTemp), Integer.parseInt(secondoTemp));
	
	//Crezione data+orario in dataOrario
	dataOrarioTemp = dataTemp.atTime(orarioTemp);	
	
	
	//Inizio blocco che lavora con virgole e gli altri campi.
	
	virgolaSx = virgolaDx; //La virgola a sx del titolo è la virgola a dx di data+orario.
	
	for(int i=0; i<riga.length(); i++) { //Conto le virgole nella riga/prenotazione letta dal file.
		if (riga.charAt(i)==',')
				contaVirgole++;
	}
	
	differenzaVirgole = contaVirgole-numvirgoleintestaz; //Se vale 0 non ci sono virgole nel titolo, altrimenti sì.
	int virgoleDaPassare = differenzaVirgole; //Le virgole da incontrare nel titolo partono da differenzaVirgole (serve seconda var xk virgoleDaPassare...
											 //...viene modificata)
	
	
	for(int i=virgolaSx+1; i<riga.length() && virgolaDxTrovata==false; i++) { //i è l'indice di dove guardo nella riga/prenotazione, parte dal 1o carattere del titolo...
																	//...quindi salta la prima virgola, quella tra data+orario e titolo.
		if(riga.charAt(i)==',' && virgoleDaPassare==0) { //Se ho trovato una virgola e ho già incontrato virgole pari a quante ce ne sono nel titolo...
			indiceVirgolaDxTitolo=i; //...vuol dire che ho trovato la virgola a dx del titolo, quindi ne salvo l'indice...
			virgolaDxTrovata = true;  //...e imposto per uscire dal ciclo.
		}
		else if (riga.charAt(i)==',' && virgoleDaPassare!=0) //Se ho trovato una virgola e non ho incontrato virgole pari a quante ce ne sono nel titolo...
			virgoleDaPassare--;//...allora quella che ho trovato è una virgola nel titolo, quindi diminuisco le virgole da trovare.
	}
	virgolaDx = indiceVirgolaDxTitolo;
	
	//Estraggo il titolo del film della proiezione presente nella prenotazione dalla riga/prenotazione letta dal file e lo metto in titoloTemp.
	titoloTemp = riga.substring(virgolaSx+1, virgolaDx); //NB La substring fa -1 al valore dx, quindi è giusto così perchè bisogna prendere fino al carattere...
	//...a sx della virgola dx.
	
	
	
	//Questo blocco estrae il numero dei posti prenotati dalla riga/prenotazione letta dal file e lo mette in NPostiTemp.
	virgolaSx = virgolaDx; //La virgola a sx del NPosti è la virgola a dx del Titolo.
	virgolaDx++; //Spostiamo virgolaDx sulla posizione del carattere dopo la virgola su cui stava, e quindi cioè sul primo carattere del NPosti.
	while(virgolaDx < riga.length() && riga.charAt(virgolaDx) != ',') { //Aumentiamo virgolaDx fino a quando non arriviamo alla prima virgola, cioè la virgola a...
		//...dx del NPosti.
			virgolaDx++;
	}
	NPostiTemp = Integer.parseInt(riga.substring(virgolaSx+1, virgolaDx)); //NB La substring fa -1 al valore dx, quindi è giusto così perchè bisogna prendere fino al carattere...
	//...a sx della virgola dx.
	
	
	//Questo blocco estrae il prezzo del biglietto dalla riga/prenotazione letta dal file e lo mette in prezzo_bigliettoTemp.
	prezzo_bigliettoTemp = Double.parseDouble(riga.substring(virgolaDx+1, riga.length() ) ); //prezzo_biglietto va dal carattere dopo la virgola che sta a dx...
	//...del NPosti fino alla fine della riga/prenotazione letta dal file
	
	prenotazTemp = new Prenotazione(IDPrenotazioneIntTemp, IDUtenteTemp, NomeTemp, CognomeTemp, dataOrarioTemp, titoloTemp, NPostiTemp, prezzo_bigliettoTemp);
	return prenotazTemp;
	
	}
//Fine metodo estraiPrenotazione().

	
} // Fine classe Bigliettaio
