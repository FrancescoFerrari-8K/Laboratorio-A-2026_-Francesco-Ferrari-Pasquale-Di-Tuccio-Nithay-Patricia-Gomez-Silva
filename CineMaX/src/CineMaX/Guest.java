package CineMaX;

import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

//Questa classe implementa i metodi che gestiscono le richieste degli utenti non registrati.

public class Guest {
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Campi (non presenti xk non necessari).
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Costruttori (non presenti xk non necessari).
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Metodi
	
//Inizio metodo cercaProiezionePerTitolo().
	public Proiezione[] cercaProiezionePerTitolo(String titoloCercato) throws FileNotFoundException {
//Questo metodo permette la ricerca di proiezioni per titolo (e visualizzazione dettagliata di una di quelle trovate). Il metodo stampa a video tutto quanto ma...
//se serve restituisce il vettore contenente le proiezioni trovate con la ricerca, null nel caso in cui la ricerca avesse 0 risultati.
			
		int limiteRic = 10000; //Limite numero risultati della ricerca.
		int numRisRicerca = 0; //Contatore numero risultati.
		boolean proiezOk; //Variabile boolean per dire se la proiezione che si sta considerando attualmente rispetta criterio ricerca.
		
		Scanner sc = new Scanner(System.in);
		String inputStringint;
		boolean inputStringintOk;
		
		Scanner scFile = new Scanner(new File("../data/proiezioni.csv")); //scFile è lettore file proiezioni.
		scFile.useDelimiter("\n"); //Il separatore per distinguere una "cosa" letta dal file dalla successiva è l'a-capo, quindi ogni .next() legge una riga del file.
		
		
		int numvirgoleintestaz = 7; // TODO DA SISTEMARE IN MODO CHE SIA GIUSTO SE AGGIUNGIAMO NUM POSTI AL FILE PROIEZIONI
		scFile.next(); //Salto la prima riga del file proiezioni che è l'intestazione.
		
		Proiezione[] risRicerca = new Proiezione[limiteRic]; //Vettore che rappresenta il risultato della ricerca cioè contiene le proiezioni che rispettano il...
		//...criterio scelto.
		
		
		System.out.println("Ricerca proiezione con titolo film :" + titoloCercato);
		
		String titoloTempLowercase, titoloRicLowercase; //Variabili che conterranno i titoli cercato e estratto dalla proiez corrente messi a minuscolo.
		String titoloParz; //Uso spiegato in confronto titoli.
		
		while(scFile.hasNext()) { //Ciclo per leggere una proiezione dal file delle proiezioni e verificare se rispetta requisiti.
			
			proiezOk = false;
			
			Proiezione proiez = estraiProiezione(scFile,numvirgoleintestaz); //Estraggo una proiezione dal file delle proiezioni e la metto in proiez.
			
			//Inizio blocco per confronto tra criterio inserito e stesso criterio nella riga/proiez letta da file.
			
			//Metto entrambi i titoli (quello ricercato e quello della proiezione considerata attualmente) a minuscolo per non far contare le maiuscole.
			titoloTempLowercase = proiez.getFilm().getTitolo().toLowerCase(); 
			titoloRicLowercase = titoloCercato.toLowerCase(); 
			
			//Confronto titoli.
			if(titoloRicLowercase.length() == titoloTempLowercase.length()) //Caso titolo cercato ha lungh uguale al titolo proiezione corrente.
				if(titoloTempLowercase.compareTo(titoloRicLowercase) == 0)
					proiezOk = true;
			if(titoloRicLowercase.length() < titoloTempLowercase.length()) { //Caso titolo cercato ha lungh minore del titolo proiezione corrente...
				//...quindi devo vedere se il titolo della proiez corrente contiene il titolo cercato; per farlo uso titoloParz che...
				//...contiene man mano pezzi del titolo della proiez corrente lunghi quanto il titolo cercato.
				//Es se cerco "evo", che è lungo 3, titoloParz conterrà man mano tutti i pezzi di lungh 3 del titolo della proiez corrente per vedere se...
				//...uno di questi pezzi è "evo".
				for(int i=0; i+titoloRicLowercase.length() <= titoloTempLowercase.length(); i++) { //For per vedere se il titolo della proiez corrente...
				//contiene il titolo cercato.
					titoloParz = titoloTempLowercase.substring(i, i+titoloRicLowercase.length());
					if( titoloParz.compareTo(titoloRicLowercase) == 0)
						proiezOk = true;
				}
			}
			//Fine blocco confronto titoli.
			
			if(proiezOk == true) { //Se la proiezione è corretta...
				if (numRisRicerca >= limiteRic) {  //...controllo se ho raggiunto (o, per qualche strano motivo, superato) numero massimo di...
				//...risultati della ricerca...
					System.out.println("Numero massimo di risultati per la ricerca (" + limiteRic + ") raggiunto, non è possibile continuare la "
							+ "ricerca\n");
					System.out.println("Per effettuare una ricerca completa inserire criteri più restrittivi");
				} 
				else { //Altrimenti se la proiezione è corretta e non ho raggiunto numero massimo di risultati...
					risRicerca[numRisRicerca] = proiez; //...salvo proiezione correntemente letta da file in vettore dei risultati della ricerca,...
					System.out.println();
					System.out.println(numRisRicerca+1); //...stampo numero del risultato perchè servirà per eventuale visualizzaProiezione...
					//...(stampa dettagli di 1 proiez),...
					proiez.visualizzaProiezione(); //...e stampo la proiez.
					numRisRicerca++;
				}
			} //Fine blocco if fatto se la proiezione è corretta.
			
		} //Fine while che legge il file delle proiezioni e controlla le proiezioni.
		System.out.println("La ricerca ha dato " + numRisRicerca + " risultati");
					
		
		if (numRisRicerca > 0) { //Inizio blocco che svolge funzionalità visualizzare in dettaglio una delle proiezioni cercate.
			
			String sceltaVisualizDettagl = "0"; //Variabile per scelta se visualizzare in dettaglio una delle proiez cercate, inizializ default a "0" cioè no.
			int sceltaNumProiezVisualiz = 1; //Variabile che conterrà il numero della proiez scelta da visualizzare in dettaglio, inizializ default a 1 cioè...
											//...la prima, che sicuramente c'è xk se siamo entrati nel blocco if in cui qsto codice si trova allora c'è almeno 1 ris.
			
			System.out.println("Si desidera visualizzare i dettagli di una delle proiezioni cercate?");
			System.out.println("Inserire 1 se sì, 0 altrimenti:");
			do { //Inizio ciclo do while per chiedere il numero della proiezione da visualizzare in dettaglio. Il while è while(true) (xk deve andare avanti finchè...
			//...l'utente inserisce una scelta valida).
				sceltaVisualizDettagl = sc.nextLine();
				
				switch(sceltaVisualizDettagl) {
				
				case "0": //Caso visualiz dettagliata di una delle proiez cercate rifiutata.
					return risRicerca;
					
				case "1": //Caso visualiz dettagliata di una delle proiez cercate richiesta.
					System.out.println("Inserire il numero della proiezione di cui si desidera visualizzare i dettagli (1 - " + numRisRicerca + "):");
					do {
						
						do {
							inputStringintOk=true;
							try {
								inputStringint = sc.nextLine();
								sceltaNumProiezVisualiz = Integer.parseInt(inputStringint);
							
							} catch (NumberFormatException e) {
								inputStringintOk = false;
								System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
							}
						} while (inputStringintOk == false);
						
						if (sceltaNumProiezVisualiz <= 0 || sceltaNumProiezVisualiz > numRisRicerca)
							System.out.println("Il numero inserito non è valido. Inserire un numero valido di una delle proiezioni "
									+ "cercate (1 - " + numRisRicerca + "):");
						else {
							risRicerca[sceltaNumProiezVisualiz-1].visualizzaProiezioneDettagliata(); //C'è il -1 perchè all'utente le proiez sono visualiz...
							//...numerate da 1 (e quindi anche la sua scelta), mentre nel vettore sono numerate da 0.
							return risRicerca;
						}
					} while (true);
					
				default:
					System.out.println("L'opzione scelta non è valida. Inserire un'opzione valida:");
					
				}
			} while (true); //Fine ciclo per chiedere il numero della proiezione da visualizzare in dettaglio. Il while è while(true) xk deve andare avanti finchè...
			//...l'utente inserisce una scelta valida.
		}  else {
			System.out.println("La ricerca non ha risultati quindi non è possibile visualizzare i dettagli di una delle proiezioni cercate");
			return null;
		} //Fine blocco che svolge funzionalità visualizzare in dettaglio una delle proiezioni cercate.
		
	}
//Fine metodo cercaProiezionePerTitolo().
	
//Inizio metodo cercaProiezione().
	public Proiezione[] cercaProiezione() throws FileNotFoundException {
//Questo metodo permette la ricerca di proiezioni (e visualizzazione dettagliata di una di quelle trovate). Il metodo stampa a video tutto quanto ma se serve...
//...restituisce il vettore contenente le proiezioni trovate con la ricerca, null nel caso in cui la ricerca avesse 0 risultati.
		
		int limiteRic = 10000; //Limite numero risultati della ricerca.
		int numRisRicerca = 0; //Contatore numero risultati.
		String scelta = "1"; //Variabile per opzioni scelta, impostata default a "1" cioè ricerca per titolo.
		boolean sceltaOk; //Variabile che indica se scelta inserita è valida o no (serve per ciclo do while in cui è contenuta tutta la ricerca, inizializ nel ciclo).
		boolean proiezOk; //Variabile boolean per dire se la proiezione che si sta considerando attualmente rispetta criterio ricerca; dichiarata qui perchè...
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
		String inputStringdouble;
		boolean inputStringdoubleOk;
		
		Scanner scFile = new Scanner(new File("../data/proiezioni.csv")); //scFile è lettore file proiezioni.
		scFile.useDelimiter("\n"); //Il separatore per distinguere una "cosa" letta dal file dalla successiva è l'a-capo, quindi ogni .next() legge una riga del file.
		
		DateTimeFormatter formatterDataITA = DateTimeFormatter.ofPattern("dd/MM/yyyy"); //Variabile per formato data italiano.
		
		int numvirgoleintestaz = 7; // TODO DA SISTEMARE IN MODO CHE SIA GIUSTO SE AGGIUNGIAMO NUM POSTI AL FILE PROIEZIONI
		scFile.next(); //Salto la prima riga del file proiezioni che è l'intestazione.
		
		Proiezione[] risRicerca = new Proiezione[limiteRic]; //Vettore che rappresenta il risultato della ricerca cioè contiene le proiezioni che rispettano il...
		//...criterio scelto.
		
		
		System.out.println("Ricerca di una proiezione");
		
		Scanner sc = new Scanner(System.in);

		do { //Inizio ciclo do while in cui è contenuto lo switch che effettua tutta la ricerca. Il while è while(sceltaOk == true). 
			
			System.out.println("Selezionare un criterio per la ricerca:");
			System.out.println("1=per titolo");
			System.out.println("2=per genere di film");
			System.out.println("3=per data");
			System.out.println("4=per prezzo del biglietto");
			System.out.println("5=per una combinazione dei predenti criteri di ricerca");
			System.out.println("0=per annullare la ricerca");
			System.out.println("Inserire criterio scelto:");
			
			sceltaOk = true; //All'inizio, e ogni volta che si ripete il do while in cui è contenuta tutta la ricerca (e questo ripetere succede quando l'utente...
//...inserisce in input un'opzione non valida) è settata a true. Se l'utente inserisce un'opzione valida allora entra nei vari case delle possibili opzioni di...
//...ricerca e in quei case sceltaOk non viene modificata, quindi rimane true, e quindi quando si esce dai case e si va al while si esce dal do while in cui è...
//...contenuta la ricerca. Viene modificata a false solo se viene inserita in input dall'utente un'opzione non valida in modo che arrivando al while si torni...
//...all'inizio del ciclo.
			
			scelta = sc.nextLine(); //Inserimento scelta utente del tipo di ricerca da effettuare.
			System.out.println("Scelta inserita: " + scelta);
			
			switch (scelta) {
			
				case "1": //Caso ricerca per titolo.
					
					String titoloRic = ""; //Variabile che conterrà il titolo inserito in input dall'utente, impostato default a stringa vuota come ha senso che sia.
					String titoloTempLowercase, titoloRicLowercase; //Variabili che conterranno i titoli cercato e estratto dalla proiez corrente messi a minuscolo.
					String titoloParz; //Uso spiegato in confronto titoli.
					
					System.out.println("Ricerca per titolo");
					System.out.println("Inserire il titolo (anche parziale):");
					titoloRic = sc.nextLine(); //Leggo titolo da ricercare e lo metto in titoloRic.
					System.out.println("Il titolo inserito è:" + titoloRic);
					
					while(scFile.hasNext()) { //Ciclo per leggere una proiezione dal file delle proiezioni e verificare se rispetta requisiti.
						
						proiezOk = false;
						
						Proiezione proiez = estraiProiezione(scFile,numvirgoleintestaz); //Estraggo una proiezione dal file delle proiezioni e la metto in proiez.
						
						//Inizio blocco per confronto tra criterio inserito e stesso criterio nella riga/proiez letta da file.
						
						//Metto entrambi i titoli (quello ricercato e quello della proiezione considerata attualmente) a minuscolo per non far contare le maiuscole.
						titoloTempLowercase = proiez.getFilm().getTitolo().toLowerCase(); 
						titoloRicLowercase = titoloRic.toLowerCase(); 
						
						//Confronto titoli.
						if(titoloRicLowercase.length() == titoloTempLowercase.length()) //Caso titolo cercato ha lungh uguale al titolo proiezione corrente.
							if(titoloTempLowercase.compareTo(titoloRicLowercase) == 0)
								proiezOk = true;
						if(titoloRicLowercase.length() < titoloTempLowercase.length()) { //Caso titolo cercato ha lungh minore del titolo proiezione corrente...
							//...quindi devo vedere se il titolo della proiez corrente contiene il titolo cercato; per farlo uso titoloParz che...
							//...contiene man mano pezzi del titolo della proiez corrente lunghi quanto il titolo cercato.
							//Es se cerco "evo", che è lungo 3, titoloParz conterrà man mano tutti i pezzi di lungh 3 del titolo della proiez corrente per vedere se...
							//...uno di questi pezzi è "evo".
							for(int i=0; i+titoloRicLowercase.length() <= titoloTempLowercase.length(); i++) { //For per vedere se il titolo della proiez corrente...
							//contiene il titolo cercato.
								titoloParz = titoloTempLowercase.substring(i, i+titoloRicLowercase.length());
								if( titoloParz.compareTo(titoloRicLowercase) == 0)
									proiezOk = true;
							}
						}
						//Fine blocco confronto titoli.
						
						if(proiezOk == true) { //Se la proiezione è corretta...
							if (numRisRicerca >= limiteRic) {  //...controllo se ho raggiunto (o, per qualche strano motivo, superato) numero massimo di...
							//...risultati della ricerca...
								System.out.println("Numero massimo di risultati per la ricerca (" + limiteRic + ") raggiunto, non è possibile continuare la "
										+ "ricerca\n");
								System.out.println("Per effettuare una ricerca completa inserire criteri più restrittivi");
								sceltaOk = false;
							} 
							else { //Altrimenti se la proiezione è corretta e non ho raggiunto numero massimo di risultati...
								risRicerca[numRisRicerca] = proiez; //...salvo proiezione correntemente letta da file in vettore dei risultati della ricerca,...
								System.out.println();
								System.out.println(numRisRicerca+1); //...stampo numero del risultato perchè servirà per eventuale visualizzaProiezione...
								//...(stampa dettagli di 1 proiez),...
								proiez.visualizzaProiezione(); //...e stampo la proiez.
								numRisRicerca++;
							}
						} //Fine blocco if fatto se la proiezione è corretta.
						
					} //Fine while che legge il file delle proiezioni e controlla le proiezioni.
					System.out.println("La ricerca ha dato " + numRisRicerca + " risultati");
					break;
					
				case "2": //Caso ricerca per genere.
					
					String genereRic = ""; //Variabile che conterrà il genere inserito in input dall'utente, impostata default a stringa vuota come ha senso che sia.
					String genereTempLowercase, genereRicLowercase; //Variabili che conterranno i generi cercato e estratto dalla proiez corrente messi a minuscolo.
					String genereParz; //Uso spiegato in confronto generi.
					
					System.out.println("Ricerca per genere");
					System.out.println("Inserire il genere (anche parziale):");
					genereRic = sc.nextLine(); //Leggo genere da ricercare e lo metto in genereRic.
					System.out.println("Il genere inserito è:" + genereRic);
					
					while(scFile.hasNext()) { //Ciclo per leggere una proiezione dal file delle proiezioni e verificare se rispetta requisiti.
						
						proiezOk = false;
						
						Proiezione proiez = estraiProiezione(scFile,numvirgoleintestaz); //Estraggo una proiezione dal file delle proiezioni e la metto in proiez.
						
						
						//Inizio blocco per confronto tra criterio inserito e stesso criterio nella riga/proiez letta da file.
						
						//Metto entrambi i generi (quello ricercato e quello della proiezione considerata attualmente) a minuscolo per non far contare le maiuscole.
						genereTempLowercase = proiez.getFilm().getGenere().toLowerCase(); 
						genereRicLowercase = genereRic.toLowerCase(); 
						
						//Confronto generi.
						if(genereRicLowercase.length() == genereTempLowercase.length()) //Caso genere cercato ha lungh uguale al genere proiezione corrente.
							if(genereTempLowercase.compareTo(genereRicLowercase) == 0)
								proiezOk = true;
						if(genereRicLowercase.length() < genereTempLowercase.length()) { //Caso genere cercato ha lungh minore del genere proiezione corrente...
							//...quindi devo vedere se il genere della proiez corrente contiene il genere cercato; per farlo uso genereParz che...
							//...contiene man mano pezzi del genere della proiez corrente lunghi quanto il genere cercato.
							//Es se cerco "horr", che è lungo 4, genereParz conterrà man mano tutti i pezzi di lungh 4 del genere della proiez corrente per vedere...
							//...se uno di questi è "horr".
							for(int i=0; i+genereRicLowercase.length() <= genereTempLowercase.length(); i++) { //For per vedere se il genere della proiez corrente...
								//contiene il genere cercato.
								genereParz = genereTempLowercase.substring(i, i+genereRicLowercase.length());
								if( genereParz.compareTo(genereRicLowercase) == 0)
									proiezOk = true;
							}
						}
						//Fine blocco confronto generi.
						
						if(proiezOk == true) { //Se la proiezione è corretta...
							if (numRisRicerca >= limiteRic) {  //...controllo se ho raggiunto (o, per qualche strano motivo, superato) numero massimo di...
							//...risultati della ricerca...
								System.out.println("Numero massimo di risultati per la ricerca (" + limiteRic + ") raggiunto, non è possibile continuare la "
										+ "ricerca\n");
								System.out.println("Per effettuare una ricerca completa inserire criteri più restrittivi");
								sceltaOk = false;
							} 
							else { //Altrimenti se la proiezione è corretta e non ho raggiunto numero massimo di risultati...
								risRicerca[numRisRicerca] = proiez; //...salvo proiezione correntemente letta da file in vettore dei risultati della ricerca,...
								System.out.println();
								System.out.println(numRisRicerca+1); //...stampo numero del risultato perchè servirà per eventuale visualizzaProiezione...
								//...(stampa dettagli di 1 proiez),...
								proiez.visualizzaProiezione(); //...e stampo la proiez.
								numRisRicerca++;
							}
						} //Fine blocco if fatto se la proiezione è corretta.
						
					} //Fine while che legge il file delle proiezioni e controlla le proiezioni.
					System.out.println("La ricerca ha dato " + numRisRicerca + " risultati");

					break;
					
				case "3": //Caso ricerca per data.
					
					LocalDate dataMinRic=LocalDate.MIN, dataMaxRic=LocalDate.MAX; //Variabili che conterranno le date inserite in input dall'utente.
					//Variabili di appoggio per le date (NB Il formatter per formato data italiano è a inizio metodo cercaProiezione() ).
					int giornoMinRic=0, giornoMaxRic=0, meseMinRic=0, meseMaxRic=0, annoMinRic=0, annoMaxRic=0;
					boolean dataRicOk = false; //Variabile per controllo validità date. Impostata default a false per case "3".
					boolean dataRic2Ok = false; //Variabile per controllo validità seconda data quando sono richieste 2 date. Impostata default a false per case "3".
					String sceltaRicData = "3"; //Variabile per opzioni ricerca per data. Impostata default a opzione ricerca proiezioni comprese tra due date.
					boolean sceltaRicDataOk; //Variabile che indica se scelta inserita è valida o no.
					
					System.out.println("Ricerca per data");
					System.out.println("Selezionare una delle seguenti opzioni:");
					System.out.println("1=proiezioni prima di una certa data");
					System.out.println("2=proiezioni dopo una certa data");
					System.out.println("3=proiezioni comprese tra due date");
					System.out.println("Inserire l'opzione di ricerca per desiderata:");
					
					do { //Inizio ciclo do while in cui è contenuto lo switch che effettua la ricerca per data.
						sceltaRicDataOk = true;
						sceltaRicData = sc.nextLine();
						
						switch (sceltaRicData){
						
						case "1": //Caso ricerca prima di una certa data.
							System.out.println("Ricerca proiezioni prima di una certa data");
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
									System.out.println("Ricerca delle proiezioni prima di " + dataMaxRic.format(formatterDataITA) );
								} catch (DateTimeException e) {
									dataRicOk = false;
									System.out.println("Data non valida. Inserire un'altra data");
								}
							} while (dataRicOk == false);
							
							while(scFile.hasNext()) { //Ciclo per leggere una proiezione dal file delle proiezioni e verificare se rispetta requisiti.
								
								proiezOk = false;
								
								Proiezione proiez = estraiProiezione(scFile,numvirgoleintestaz); //Estraggo una proiezione dal file delle proiezioni e la metto in...
								//...proiez.
								
								//Inizio blocco per confronto tra criterio inserito e stesso criterio nella riga/proiez letta da file.
								
								//Confronto date.
								if (proiez.getDataOra().toLocalDate().compareTo(dataMaxRic) < 0)
									proiezOk = true;
								//Fine blocco confronto date.
								
								if(proiezOk == true) { //Se la proiezione è corretta...
									if (numRisRicerca >= limiteRic) {  //...controllo se ho raggiunto (o, per qualche strano motivo, superato) numero massimo di...
									//...risultati della ricerca...
										System.out.println("Numero massimo di risultati per la ricerca (" + limiteRic + ") raggiunto, non è possibile continuare la "
												+ "ricerca\n");
										System.out.println("Per effettuare una ricerca completa inserire criteri più restrittivi");
										sceltaOk = false;
									} 
									else { //Altrimenti se la proiezione è corretta e non ho raggiunto numero massimo di risultati...
										risRicerca[numRisRicerca] = proiez; //...salvo proiezione correntemente letta da file in vettore dei risultati della ricerca,...
										System.out.println();
										System.out.println(numRisRicerca+1); //...stampo numero del risultato perchè servirà per eventuale visualizzaProiezione...
										//...(stampa dettagli di 1 proiez),...
										proiez.visualizzaProiezione(); //...e stampo la proiez.
										numRisRicerca++;
									}
								} //Fine blocco if fatto se la proiezione è corretta.
								
							} //Fine while che legge il file delle proiezioni e controlla le proiezioni.
							System.out.println("La ricerca ha dato " + numRisRicerca + " risultati");
							
							break;
							
						case "2": //Caso ricerca dopo una certa data.
							System.out.println("Ricerca proiezioni dopo una certa data");
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
									System.out.println("Ricerca delle proiezioni dopo " + dataMinRic.format(formatterDataITA) );
								} catch (DateTimeException e) {
									dataRicOk = false;
									System.out.println("Data non valida. Inserire un'altra data");
								}
							} while (dataRicOk == false);
							
							while(scFile.hasNext()) { //Ciclo per leggere una proiezione dal file delle proiezioni e verificare se rispetta requisiti.
								
								proiezOk = false;
								
								Proiezione proiez = estraiProiezione(scFile,numvirgoleintestaz); //Estraggo una proiezione dal file delle proiezioni e la metto in...
								//...proiez.
								
								//Inizio blocco per confronto tra criterio inserito e stesso criterio nella riga/proiez letta da file.
								
								//Confronto date.
								if (proiez.getDataOra().toLocalDate().compareTo(dataMinRic) > 0)
									proiezOk = true;
								//Fine blocco confronto date.
								
								if(proiezOk == true) { //Se la proiezione è corretta...
									if (numRisRicerca >= limiteRic) {  //...controllo se ho raggiunto (o, per qualche strano motivo, superato) numero massimo di...
									//...risultati della ricerca...
										System.out.println("Numero massimo di risultati per la ricerca (" + limiteRic + ") raggiunto, non è possibile continuare la "
												+ "ricerca\n");
										System.out.println("Per effettuare una ricerca completa inserire criteri più restrittivi");
										sceltaOk = false;
									} 
									else { //Altrimenti se la proiezione è corretta e non ho raggiunto numero massimo di risultati...
										risRicerca[numRisRicerca] = proiez; //...salvo proiezione correntemente letta da file in vettore dei risultati della ricerca,...
										System.out.println();
										System.out.println(numRisRicerca+1); //...stampo numero del risultato perchè servirà per eventuale visualizzaProiezione...
										//...(stampa dettagli di 1 proiez),...
										proiez.visualizzaProiezione(); //...e stampo la proiez.
										numRisRicerca++;
									}
								} //Fine blocco if fatto se la proiezione è corretta.
								
							} //Fine while che legge il file delle proiezioni e controlla le proiezioni.
							System.out.println("La ricerca ha dato " + numRisRicerca + " risultati");
							break;
						
						case "3": //Caso ricerca tra due date.
							System.out.println("Ricerca proiezioni tra due date");
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
										System.out.println("Ricerca delle proiezioni comprese tra " + dataMinRic.format(formatterDataITA) + " e " 
												+ dataMaxRic.format(formatterDataITA));
									}
									
								} catch (DateTimeException e) {
									if (dataRicOk == false) //Se dataOK è false l'eccezione è scattata con la .of legata alla 1a data...
										System.out.println("La prima data non è valida. Inserire un'altra data");
									else //altrimenti è scattata con la .of legata alla 2a data.
										System.out.println("La seconda data non è valida. Inserire un'altra data");
								}
							} while (dataRicOk == false || dataRic2Ok == false);
							
							while(scFile.hasNext()) { //Ciclo per leggere una proiezione dal file delle proiezioni e verificare se rispetta requisiti.
								
								proiezOk = false;
								
								Proiezione proiez = estraiProiezione(scFile,numvirgoleintestaz); //Estraggo una proiezione dal file delle proiezioni e la metto in...
								//...proiez.
								
								//Inizio blocco per confronto tra criterio inserito e stesso criterio nella riga/proiez letta da file.
								
								//Confronto date.
								if (proiez.getDataOra().toLocalDate().compareTo(dataMinRic) > 0 && proiez.getDataOra().toLocalDate().compareTo(dataMaxRic) < 0)
									proiezOk = true;
								//Fine blocco confronto date.
								
								if(proiezOk == true) { //Se la proiezione è corretta...
									if (numRisRicerca >= limiteRic) {  //...controllo se ho raggiunto (o, per qualche strano motivo, superato) numero massimo di...
									//...risultati della ricerca...
										System.out.println("Numero massimo di risultati per la ricerca (" + limiteRic + ") raggiunto, non è possibile continuare la "
												+ "ricerca\n");
										System.out.println("Per effettuare una ricerca completa inserire criteri più restrittivi");
										sceltaOk = false;
									} 
									else { //Altrimenti se la proiezione è corretta e non ho raggiunto numero massimo di risultati...
										risRicerca[numRisRicerca] = proiez; //...salvo proiezione correntemente letta da file in vettore dei risultati della ricerca,...
										System.out.println();
										System.out.println(numRisRicerca+1); //...stampo numero del risultato perchè servirà per eventuale visualizzaProiezione...
										//...(stampa dettagli di 1 proiez),...
										proiez.visualizzaProiezione(); //...e stampo la proiez.
										numRisRicerca++;
									}
								} //Fine blocco if fatto se la proiezione è corretta.
								
							} //Fine while che legge il file delle proiezioni e controlla le proiezioni.
							System.out.println("La ricerca ha dato " + numRisRicerca + " risultati");
							break;
							
						default:
							sceltaRicDataOk = false;
							System.out.println("L'opzione scelta non è valida. Inserire un'opzione valida:");
						} //Fine switch in cui è contenuta la ricerca per data.
					} while (sceltaRicDataOk == false); //Fine ciclo do while in cui è contenuto lo switch che effettua la ricerca per data.
					
					break;
					
				case "4": //Caso ricerca per prezzo del biglietto.
					
					double prezzoMinRic = 0, prezzoMaxRic = 0; //Variabili che conterranno i prezzi inseriti in input dall'utente, inizializ default a 0.
					String sceltaRicPrezzo = "3"; //Variabile per opzioni ricerca per prezzo. Impostata default a opzione ricerca proiezioni comprese tra due prezzi.
					boolean sceltaRicPrezzoOk; //Variabile che indica se scelta inserita è valida o no.
					
					System.out.println("Ricerca per intervallo di prezzi. Selezionare una delle seguenti opzioni:");
					System.out.println("1=proiezioni con prezzo minore di un certo valore");
					System.out.println("2=proiezioni con prezzo maggiore di un certo valore");
					System.out.println("3=proiezioni con prezzo compreso tra due valori");
					System.out.println("Inserire l'opzione di ricerca per desiderata:");
					
										
					do { //Inizio ciclo do while in cui è contenuto lo switch che effettua la ricerca per prezzo.
						sceltaRicPrezzoOk = true;
						sceltaRicPrezzo = sc.nextLine();
						
						switch (sceltaRicPrezzo){
						
						case "1": //Caso ricerca prezzo minore di un certo valore.
							System.out.println("Ricerca proiezioni con prezzo minore di un certo valore");
							do {
								System.out.println("Inserire il prezzo massimo:");
								
								do {
									inputStringdoubleOk=true;
									try {
										inputStringdouble = sc.nextLine();
										prezzoMaxRic = Double.parseDouble(inputStringdouble);
									
									} catch (NumberFormatException e) {
										inputStringdoubleOk = false;
										System.out.println("Non è stato inserito un numero valido. Inserire un numero valido: ");
									}
								} while (inputStringdoubleOk == false);
								
								if (prezzoMaxRic < 0)
									System.out.println("Prezzo non valido (prezzo negativo). Inserire un'altro prezzo");
							} while (prezzoMaxRic < 0);
							System.out.println("Ricerca delle proiezioni con prezzo minore di " + prezzoMaxRic);
							
							while(scFile.hasNext()) { //Ciclo per leggere una proiezione dal file delle proiezioni e verificare se rispetta requisiti.
								
								proiezOk = false;
								
								Proiezione proiez = estraiProiezione(scFile,numvirgoleintestaz); //Estraggo una proiezione dal file delle proiezioni e la metto in...
								//...proiez.
								
								//Inizio blocco per confronto tra criterio inserito e stesso criterio nella riga/proiez letta da file.
								
								//Confronto prezzi.
								if (proiez.getPrezzoBiglietto() < prezzoMaxRic)
									proiezOk = true;
								//Fine blocco confronto prezzi.
								
								if(proiezOk == true) { //Se la proiezione è corretta...
									if (numRisRicerca >= limiteRic) {  //...controllo se ho raggiunto (o, per qualche strano motivo, superato) numero massimo di...
									//...risultati della ricerca...
										System.out.println("Numero massimo di risultati per la ricerca (" + limiteRic + ") raggiunto, non è possibile continuare la "
												+ "ricerca\n");
										System.out.println("Per effettuare una ricerca completa inserire criteri più restrittivi");
										sceltaOk = false;
									} 
									else { //Altrimenti se la proiezione è corretta e non ho raggiunto numero massimo di risultati...
										risRicerca[numRisRicerca] = proiez; //...salvo proiezione correntemente letta da file in vettore dei risultati della ricerca,...
										System.out.println();
										System.out.println(numRisRicerca+1); //...stampo numero del risultato perchè servirà per eventuale visualizzaProiezione...
										//...(stampa dettagli di 1 proiez),...
										proiez.visualizzaProiezione(); //...e stampo la proiez.
										numRisRicerca++;
									}
								} //Fine blocco if fatto se la proiezione è corretta.
								
							} //Fine while che legge il file delle proiezioni e controlla le proiezioni.
							System.out.println("La ricerca ha dato " + numRisRicerca + " risultati");
							break;
							
						case "2": //Caso ricerca prezzo maggiore di un certo valore.
							
							System.out.println("Ricerca proiezioni con prezzo maggiore di un certo valore");
							do {
								System.out.println("Inserire il prezzo minimo:");
								
								do {
									inputStringdoubleOk=true;
									try {
										inputStringdouble = sc.nextLine();
										prezzoMinRic = Double.parseDouble(inputStringdouble);
									
									} catch (NumberFormatException e) {
										inputStringdoubleOk = false;
										System.out.println("Non è stato inserito un numero valido. Inserire un numero valido: ");
									}
								} while (inputStringdoubleOk == false);
								
								if (prezzoMinRic < 0)
									System.out.println("Prezzo non valido (prezzo negativo). Inserire un'altro prezzo");
							} while (prezzoMinRic < 0);
							System.out.println("Ricerca delle proiezioni con prezzo maggiore di " + prezzoMinRic);
							
							while(scFile.hasNext()) { //Ciclo per leggere una proiezione dal file delle proiezioni e verificare se rispetta requisiti.
								
								proiezOk = false;
								
								Proiezione proiez = estraiProiezione(scFile,numvirgoleintestaz); //Estraggo una proiezione dal file delle proiezioni e la metto in...
								//...proiez.
								
								//Inizio blocco per confronto tra criterio inserito e stesso criterio nella riga/proiez letta da file.
								
								//Confronto prezzi.
								if (proiez.getPrezzoBiglietto() > prezzoMinRic)
									proiezOk = true;
								//Fine blocco confronto prezzi.
								
								if(proiezOk == true) { //Se la proiezione è corretta...
									if (numRisRicerca >= limiteRic) {  //...controllo se ho raggiunto (o, per qualche strano motivo, superato) numero massimo di...
									//...risultati della ricerca...
										System.out.println("Numero massimo di risultati per la ricerca (" + limiteRic + ") raggiunto, non è possibile continuare la "
												+ "ricerca\n");
										System.out.println("Per effettuare una ricerca completa inserire criteri più restrittivi");
										sceltaOk = false;
									} 
									else { //Altrimenti se la proiezione è corretta e non ho raggiunto numero massimo di risultati...
										risRicerca[numRisRicerca] = proiez; //...salvo proiezione correntemente letta da file in vettore dei risultati della ricerca,...
										System.out.println();
										System.out.println(numRisRicerca+1); //...stampo numero del risultato perchè servirà per eventuale visualizzaProiezione...
										//...(stampa dettagli di 1 proiez),...
										proiez.visualizzaProiezione(); //...e stampo la proiez.
										numRisRicerca++;
									}
								} //Fine blocco if fatto se la proiezione è corretta.
								
							} //Fine while che legge il file delle proiezioni e controlla le proiezioni.
							System.out.println("La ricerca ha dato " + numRisRicerca + " risultati");
							break;
						
						case "3": //Caso ricerca prezzo compreso tra due valori.
							
							System.out.println("Ricerca proiezioni con prezzo compreso tra due valori");
							
							//Inserimento prezzo minimo.
							do {
								System.out.println("Inserire il prezzo minimo:");
								
								do {
									inputStringdoubleOk=true;
									try {
										inputStringdouble = sc.nextLine();
										prezzoMinRic = Double.parseDouble(inputStringdouble);
									
									} catch (NumberFormatException e) {
										inputStringdoubleOk = false;
										System.out.println("Non è stato inserito un numero valido. Inserire un numero valido: ");
									}
								} while (inputStringdoubleOk == false);
								
								if (prezzoMinRic < 0)
									System.out.println("Prezzo non valido (prezzo negativo). Inserire un'altro prezzo");
							} while (prezzoMinRic < 0);
							System.out.println("Il prezzo minimo inserito è " + prezzoMinRic);
							
							//Inserimento prezzo massimo.
							do {
								System.out.println("Inserire il prezzo massimo:");
								
								do {
									inputStringdoubleOk=true;
									try {
										inputStringdouble = sc.nextLine();
										prezzoMaxRic = Double.parseDouble(inputStringdouble);
									
									} catch (NumberFormatException e) {
										inputStringdoubleOk = false;
										System.out.println("Non è stato inserito un numero valido. Inserire un numero valido: ");
									}
								} while (inputStringdoubleOk == false);
								
								if (prezzoMaxRic < 0)
									System.out.println("Prezzo non valido (prezzo negativo). Inserire un'altro prezzo");
								if (prezzoMaxRic < prezzoMinRic)
									System.out.println("Prezzo non valido (prezzo massimo è minore del prezzo minimo precedentemente inserito). "
											+ "Inserire un'altro prezzo");
							} while (prezzoMaxRic < 0 || prezzoMaxRic < prezzoMinRic);
							System.out.println("Il prezzo massimo inserito è " + prezzoMaxRic);
							
							
							while(scFile.hasNext()) { //Ciclo per leggere una proiezione dal file delle proiezioni e verificare se rispetta requisiti.
								
								proiezOk = false;
								
								Proiezione proiez = estraiProiezione(scFile,numvirgoleintestaz); //Estraggo una proiezione dal file delle proiezioni e la metto in...
								//...proiez.
								
								//Inizio blocco per confronto tra criterio inserito e stesso criterio nella riga/proiez letta da file.
								
								//Confronto prezzi.
								if (proiez.getPrezzoBiglietto() > prezzoMinRic && proiez.getPrezzoBiglietto() < prezzoMaxRic)
									proiezOk = true;
								//Fine blocco confronto prezzi.
								
								if(proiezOk == true) { //Se la proiezione è corretta...
									if (numRisRicerca >= limiteRic) {  //...controllo se ho raggiunto (o, per qualche strano motivo, superato) numero massimo di...
									//...risultati della ricerca...
										System.out.println("Numero massimo di risultati per la ricerca (" + limiteRic + ") raggiunto, non è possibile continuare la "
												+ "ricerca\n");
										System.out.println("Per effettuare una ricerca completa inserire criteri più restrittivi");
										sceltaOk = false;
									} 
									else { //Altrimenti se la proiezione è corretta e non ho raggiunto numero massimo di risultati...
										risRicerca[numRisRicerca] = proiez; //...salvo proiezione correntemente letta da file in vettore dei risultati della ricerca,...
										System.out.println();
										System.out.println(numRisRicerca+1); //...stampo numero del risultato perchè servirà per eventuale visualizzaProiezione...
										//...(stampa dettagli di 1 proiez),...
										proiez.visualizzaProiezione(); //...e stampo la proiez.
										numRisRicerca++;
									}
								} //Fine blocco if fatto se la proiezione è corretta.
								
							} //Fine while che legge il file delle proiezioni e controlla le proiezioni.
							System.out.println("La ricerca ha dato " + numRisRicerca + " risultati");
							break;
							
						default:
							sceltaRicPrezzoOk = false;
							System.out.println("L'opzione scelta non è valida. Inserire un'opzione valida:");
						} //Fine switch in cui è contenuta la ricerca per prezzo.
					} while (sceltaRicPrezzoOk == false); //Fine ciclo do while in cui è contenuto lo switch che effettua la ricerca per prezzo.
					
					break;
					
				case "5": //Caso ricerca per combinazione di criteri.
					
					//Inizio blocco dichiarazioni variabili necessarie per contenere i criteri inseriti in input dall'utente.
					
					//Sono le stesse variabili dei case per singolo criterio ma nominate Ricerca e non Ric xk altrimenti c'è errore variabile duplicata (per il fatto...
					//che se non si mette esplicitamente un case tra {} non è considerato un blocco a parte con il suo scope).
					String titoloRicerca = ""; //Variabile che conterrà il titolo inserito in input dall'utente.
					String titoloTempLowercaseRicerca, titoloRicLowercaseRicerca; //Variabili che conterranno i titoli cercato e estratto dalla proiez corrente...
					//...messi a minuscolo.
					String titoloParzRicerca;
					String genereRicerca = ""; //Variabile che conterrà il genere inserito in input dall'utente.
					String genereTempLowercaseRicerca, genereRicLowercaseRicerca; //Variabili che conterranno i generi cercato e estratto dalla proiez corrente...
					//...messi a minuscolo.
					String genereParzRicerca;
					
					LocalDate dataMinRicerca=LocalDate.MIN, dataMaxRicerca=LocalDate.MAX; //Variabili che conterranno le date inserite in input dall'utente.
					//Variabili di appoggio per le date (NB il formatter per formato data italiano è a inizio metodo cercaProiezione() ).
					int giornoMinRicerca=0, giornoMaxRicerca=0, meseMinRicerca=0, meseMaxRicerca=0, annoMinRicerca=0, annoMaxRicerca=0;
					boolean dataRicercaOk = false; //Variabile per controllo validità date. Impostata default a false perchè serve così al case "3".
					boolean dataRicerca2Ok = false; //Variabile per controllo validità seconda data quando sono richieste 2 date. Impostata default a false...
					//...perchè serve così case "3".
					String sceltaRicercaData = "3"; //Variabile per opzioni ricerca per data. Impostata default a opzione ricerca proiezioni comprese tra due date.
					boolean sceltaRicercaDataOk; //Variabile che indica se scelta inserita è valida o no.
					
					double prezzoMinRicerca = 0, prezzoMaxRicerca = 0; //Variabili che conterranno i prezzi inseriti in input dall'utente, inizializ default a 0.
					String sceltaRicercaPrezzo = "3"; //Variabile per opzioni ricerca per prezzo. Impostata default a opzione ricerca proiezioni comprese tra due...
					//...prezzi.
					boolean sceltaRicercaPrezzoOk; //Variabile che indica se scelta inserita è valida o no.
					
					//Fine blocco dichiarazioni variabili necessarie per contenere i criteri inseriti in input dall'utente.
					
					String[] scelteCriteri = new String[4]; //Vettore per contenere le scelte relative ai criteri (cioè se usare i criteri o no).
					boolean noCriteriInseriti = true; //Variabile per capire se l'utente ha scelto almeno un criterio oppure no, inizializ a true xk all'inizio...
					//...non si è ancora scelto nex criterio.
					
					System.out.println("Ricerca per combinazione di criteri");
					System.out.println("Selezione dei criteri per la ricerca");
					
					do { //Inizio ciclo per scelta criteri.
						
						System.out.println("Ricercare per titolo? Inserire 1 se sì, 0 altrimenti:");
						do {
							scelteCriteri[0] = sc.nextLine();
							if (scelteCriteri[0].compareTo("0") != 0 && scelteCriteri[0].compareTo("1") != 0)
								System.out.println("L'opzione scelta non è valida. Inserire un'opzione valida:");
						} while (scelteCriteri[0].compareTo("0") != 0 && scelteCriteri[0].compareTo("1") != 0);
						
						System.out.println("Ricercare per genere? Inserire 1 se sì, 0 altrimenti:");
						do {
							scelteCriteri[1] = sc.nextLine();
							if (scelteCriteri[1].compareTo("0") != 0 && scelteCriteri[1].compareTo("1") != 0)
								System.out.println("L'opzione scelta non è valida. Inserire un'opzione valida:");
						} while (scelteCriteri[1].compareTo("0") != 0 && scelteCriteri[1].compareTo("1") != 0);
						
						System.out.println("Ricercare per data? Inserire 1 se sì, 0 altrimenti:");
						do {
							scelteCriteri[2] = sc.nextLine();
							if (scelteCriteri[2].compareTo("0") != 0 && scelteCriteri[2].compareTo("1") != 0)
								System.out.println("L'opzione scelta non è valida. Inserire un'opzione valida:");
						} while (scelteCriteri[2].compareTo("0") != 0 && scelteCriteri[2].compareTo("1") != 0);
						
						System.out.println("Ricercare per prezzo del biglietto? Inserire 1 se sì, 0 altrimenti:");
						do {
							scelteCriteri[3] = sc.nextLine();
							if (scelteCriteri[3].compareTo("0") != 0 && scelteCriteri[3].compareTo("1") != 0)
								System.out.println("L'opzione scelta non è valida. Inserire un'opzione valida:");
						} while (scelteCriteri[3].compareTo("0") != 0 && scelteCriteri[3].compareTo("1") != 0);
						
						for (String s : scelteCriteri) //Ciclo per controllare se si è scelto almeno un criterio.
						{
							if (s.compareTo("1") == 0)
								noCriteriInseriti = false;
						}
						if (noCriteriInseriti == true)
							System.out.println("Non è stato selezionato alcun criterio. Scegliere almeno un criterio per la ricerca");
						
					} while (noCriteriInseriti == true); //Fine ciclo per scelta criteri.
					
					System.out.print("Richiesta ricerca per:");
					boolean virgolaNecessaria=false;
					
					if(scelteCriteri[0].compareTo("1") == 0) { //titolo.
						System.out.print("titolo");
						virgolaNecessaria = true;
					} //titolo.
					
					
					if(scelteCriteri[1].compareTo("1") == 0) { //genere.
						if (virgolaNecessaria == true)
							System.out.print(",");
						System.out.print("genere");
						virgolaNecessaria = true;
					} //genere.
					
					if(scelteCriteri[2].compareTo("1") == 0) { //data.
						if (virgolaNecessaria == true)
							System.out.print(",");
						System.out.print("data");
						virgolaNecessaria = true;
					} //data.
					
					if(scelteCriteri[3].compareTo("1") == 0) { //prezzo.
						if (virgolaNecessaria == true)
							System.out.print(",");
						System.out.print("prezzo");
						virgolaNecessaria = true;
					} //prezzo.
					System.out.println();
					
					if(scelteCriteri[0].compareTo("1") == 0) { //Inserimento titolo.
						System.out.println("Inserire il titolo (anche parziale):");
						titoloRicerca = sc.nextLine(); //Leggo titolo da ricercare e lo metto in titoloRicerca.
					} //Fine inserimento titolo.
					
					if(scelteCriteri[1].compareTo("1") == 0) { //Inserimento genere.
						System.out.println("Inserire il genere (anche parziale):");
						genereRicerca = sc.nextLine(); //Leggo genere da ricercare e lo metto in genereRicerca.
					} //Fine inserimento genere.
					
					if(scelteCriteri[2].compareTo("1") == 0) { //Inserimento data.
						
						System.out.println("Selezionare una delle seguenti opzioni per la data:");
						System.out.println("1=proiezioni prima di una certa data");
						System.out.println("2=proiezioni dopo una certa data");
						System.out.println("3=proiezioni comprese tra due date");
						System.out.println("Inserire l'opzione di ricerca per desiderata:");
						
						do { //Inizio ciclo do while in cui è contenuto lo switch che effettua l'inserimento della data.
							sceltaRicercaDataOk = true;
							sceltaRicercaData = sc.nextLine();
							
							switch (sceltaRicercaData){
							
							case "1": //Caso ricerca prima di una certa data.
								System.out.println("Ricerca proiezioni prima di una certa data");
								do {
									try {
										System.out.println("Inserire il giorno:");
										
										do {
											inputStringintOk=true;
											try {
												inputStringint = sc.nextLine();
												giornoMaxRicerca = Integer.parseInt(inputStringint);
											
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
												meseMaxRicerca = Integer.parseInt(inputStringint);
											
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
												annoMaxRicerca = Integer.parseInt(inputStringint);
											
											} catch (NumberFormatException e) {
												inputStringintOk = false;
												System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
											}
										} while (inputStringintOk == false);
										
										
										dataMaxRicerca = LocalDate.of(annoMaxRicerca, meseMaxRicerca, giornoMaxRicerca);
										dataRicercaOk = true; //Se si arriva a questa riga vuol dire che non è stata sollevata eccezione con la .of e quindi la data...
										//...inserita è valida.
										System.out.println("La data inserita è:" + dataMaxRicerca.format(formatterDataITA) );
										System.out.println("Ricerca delle proiezioni prima di " + dataMaxRicerca.format(formatterDataITA) );
									} catch (DateTimeException e) {
										dataRicercaOk = false;
										System.out.println("Data non valida. Inserire un'altra data");
									}
								} while (dataRicercaOk == false);
								
								
								break;
								
							case "2": //Caso ricerca dopo una certa data.
								System.out.println("Ricerca proiezioni dopo una certa data");
								do {
									try {
										System.out.println("Inserire il giorno:");
										
										do {
											inputStringintOk=true;
											try {
												inputStringint = sc.nextLine();
												giornoMinRicerca = Integer.parseInt(inputStringint);
											
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
												meseMinRicerca = Integer.parseInt(inputStringint);
											
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
												annoMinRicerca = Integer.parseInt(inputStringint);
											
											} catch (NumberFormatException e) {
												inputStringintOk = false;
												System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
											}
										} while (inputStringintOk == false);
										
										
										dataMinRicerca = LocalDate.of(annoMinRicerca, meseMinRicerca, giornoMinRicerca);
										dataRicercaOk = true; //Se si arriva a questa riga vuol dire che non è stata sollevata eccezione con la .of e quindi la data...
										//...inserita è valida.
										System.out.println("La data inserita è:" + dataMinRicerca.format(formatterDataITA) );
										System.out.println("Ricerca delle proiezioni dopo " + dataMinRicerca.format(formatterDataITA) );
									} catch (DateTimeException e) {
										dataRicercaOk = false;
										System.out.println("Data non valida. Inserire un'altra data");
									}
								} while (dataRicercaOk == false);
								
								break;
							
							case "3": //Caso ricerca tra due date.
								System.out.println("Ricerca proiezioni tra due date");
								do {
									try {
										if (dataRicercaOk == false) { //Questo if serve a non far reinserire la 1a data se si è sbagliata solo la 2a. dataOk di...
											//default parte false quindi la prima volta si entra di sicuro nell'if (come giusto che sia).
											System.out.println("Inserire il giorno della data minore (quella più indietro, nel passato):");
											
											do {
												inputStringintOk=true;
												try {
													inputStringint = sc.nextLine();
													giornoMinRicerca = Integer.parseInt(inputStringint);
												
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
													meseMinRicerca = Integer.parseInt(inputStringint);
												
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
													annoMinRicerca = Integer.parseInt(inputStringint);
												
												} catch (NumberFormatException e) {
													inputStringintOk = false;
													System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
												}
											} while (inputStringintOk == false);
											
											
											dataMinRicerca = LocalDate.of(annoMinRicerca, meseMinRicerca, giornoMinRicerca);
											dataRicercaOk = true; //Se si arriva a questa riga vuol dire che non è stata sollevata eccezione con la .of e quindi la...
											//...1a data inserita è valida.
											System.out.println("La data minore inserita è: " + dataMinRicerca.format(formatterDataITA) );
										}
										
										System.out.println("Inserire il giorno della data maggiore (quella più avanti, nel futuro):");
										
										do {
											inputStringintOk=true;
											try {
												inputStringint = sc.nextLine();
												giornoMaxRicerca = Integer.parseInt(inputStringint);
											
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
												meseMaxRicerca = Integer.parseInt(inputStringint);
											
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
												annoMaxRicerca = Integer.parseInt(inputStringint);
											
											} catch (NumberFormatException e) {
												inputStringintOk = false;
												System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
											}
										} while (inputStringintOk == false);
										
										
										dataMaxRicerca = LocalDate.of(annoMaxRicerca, meseMaxRicerca, giornoMaxRicerca);
										
										if (dataMaxRicerca.compareTo(dataMinRicerca) < 0) { //Se la 2a data inserita è minore/più indietro della 1a, la 2a data...
										//...non va bene.
											System.out.println("La seconda data inserita è minore/più indietro della prima data. Inserire un'altra data");
										}
										else { //Altrimenti la 2a data va bene.
											dataRicerca2Ok = true; //Se si arriva a questa riga vuol dire che non è stata sollevata eccezione con la .of e quindi la...
											//...2a data inserita è valida.
											System.out.println("La data maggiore inserita è:" + dataMaxRicerca.format(formatterDataITA) );
											
											//Quindi arrivati qui entrambe le data sono a posto.
											System.out.println("Ricerca delle proiezioni comprese tra " + dataMinRicerca.format(formatterDataITA) + " e " 
													+ dataMaxRicerca.format(formatterDataITA));
										}
										
									} catch (DateTimeException e) {
										if (dataRicercaOk == false) //Se dataOK è false l'eccezione è scattata con la .of legata alla 1a data...
											System.out.println("La prima data non è valida. Inserire un'altra data");
										else //altrimenti è scattata con la .of legata alla 2a data.
											System.out.println("La seconda data non è valida. Inserire un'altra data");
									}
								} while (dataRicercaOk == false || dataRicerca2Ok == false);
								
								break;
								
							default:
								sceltaRicercaDataOk = false;
								System.out.println("L'opzione scelta non è valida. Inserire un'opzione valida:");
							} //Fine switch in cui è contenuta la ricerca per data.
							
						} while (sceltaRicercaDataOk == false); //Fine ciclo do while che effettua l'inserimento della data.
						
					} //Fine inserimento data.
					
					if(scelteCriteri[3].compareTo("1") == 0) { //Inserimento prezzo.
						
						System.out.println("Selezionare una delle seguenti opzioni per il prezzo:");
						System.out.println("1=proiezioni con prezzo minore di un certo valore");
						System.out.println("2=proiezioni con prezzo maggiore di un certo valore");
						System.out.println("3=proiezioni con prezzo compreso tra due valori");
						System.out.println("Inserire l'opzione di ricerca per desiderata:");
						
						do { //Inizio ciclo do while che effettua l'inserimento del prezzo.
							sceltaRicercaPrezzoOk = true;
							sceltaRicercaPrezzo = sc.nextLine();
							
							switch (sceltaRicercaPrezzo){
							
							case "1": //Caso ricerca prezzo minore di un certo valore.
								System.out.println("Ricerca proiezioni con prezzo minore di un certo valore");
								do {
									System.out.println("Inserire il prezzo massimo:");
									
									do {
										inputStringdoubleOk=true;
										try {
											inputStringdouble = sc.nextLine();
											prezzoMaxRicerca = Double.parseDouble(inputStringdouble);
										
										} catch (NumberFormatException e) {
											inputStringdoubleOk = false;
											System.out.println("Non è stato inserito un numero valido. Inserire un numero valido: ");
										}
									} while (inputStringdoubleOk == false);
									
									
									if (prezzoMaxRicerca < 0)
										System.out.println("Prezzo non valido (prezzo negativo). Inserire un'altro prezzo");
								} while (prezzoMaxRicerca < 0);
								System.out.println("Ricerca delle proiezioni con prezzo minore di " + prezzoMaxRicerca);
								
								break;
								
							case "2": //Caso ricerca prezzo maggiore di un certo valore.
								
								System.out.println("Ricerca proiezioni con prezzo maggiore di un certo valore");
								do {
									System.out.println("Inserire il prezzo minimo:");
									
									do {
										inputStringdoubleOk=true;
										try {
											inputStringdouble = sc.nextLine();
											prezzoMinRicerca = Double.parseDouble(inputStringdouble);
										
										} catch (NumberFormatException e) {
											inputStringdoubleOk = false;
											System.out.println("Non è stato inserito un numero valido. Inserire un numero valido: ");
										}
									} while (inputStringdoubleOk == false);
									
									
									if (prezzoMinRicerca < 0)
										System.out.println("Prezzo non valido (prezzo negativo). Inserire un'altro prezzo");
								} while (prezzoMinRicerca < 0);
								System.out.println("Ricerca delle proiezioni con prezzo maggiore di " + prezzoMinRicerca);
								
								break;
							
							case "3": //Caso ricerca prezzo compreso tra due valori.
								
								System.out.println("Ricerca proiezioni con prezzo compreso tra due valori");
								do {
									System.out.println("Inserire il prezzo minimo:");
									
									do {
										inputStringdoubleOk=true;
										try {
											inputStringdouble = sc.nextLine();
											prezzoMinRicerca = Double.parseDouble(inputStringdouble);
										
										} catch (NumberFormatException e) {
											inputStringdoubleOk = false;
											System.out.println("Non è stato inserito un numero valido. Inserire un numero valido: ");
										}
									} while (inputStringdoubleOk == false);
									
									
									if (prezzoMinRicerca < 0)
										System.out.println("Prezzo non valido (prezzo negativo). Inserire un'altro prezzo");
								} while (prezzoMinRicerca < 0);
								System.out.println("Il prezzo minimo inserito è " + prezzoMinRicerca);
								
								do {
									System.out.println("Inserire il prezzo massimo:");
									
									do {
										inputStringdoubleOk=true;
										try {
											inputStringdouble = sc.nextLine();
											prezzoMaxRicerca = Double.parseDouble(inputStringdouble);
										
										} catch (NumberFormatException e) {
											inputStringdoubleOk = false;
											System.out.println("Non è stato inserito un numero valido. Inserire un numero valido: ");
										}
									} while (inputStringdoubleOk == false);
									
									
									if (prezzoMaxRicerca < 0)
										System.out.println("Prezzo non valido (prezzo negativo). Inserire un'altro prezzo");
									if (prezzoMaxRicerca < prezzoMinRicerca)
										System.out.println("Prezzo non valido (prezzo massimo è minore del prezzo minimo precedentemente inserito). "
												+ "Inserire un'altro prezzo");
								} while (prezzoMaxRicerca < 0 || prezzoMaxRicerca < prezzoMinRicerca);
								System.out.println("Il prezzo massimo inserito è " + prezzoMaxRicerca);
								
								break;
								
							default:
								sceltaRicercaPrezzoOk = false;
								System.out.println("L'opzione scelta non è valida. Inserire un'opzione valida:");
							} //Fine switch in cui è contenuta la ricerca per prezzo.
						} while (sceltaRicercaPrezzoOk == false); //Fine ciclo do while che effettua l'inserimento del prezzo.
						
					} //Fine inserimento prezzo.
					
					
					while(scFile.hasNext()) { //Ciclo per leggere una proiezione dal file delle proiezioni e verificare se rispetta requisiti.
						
						proiezOk = true; //Inizializ a true, non appena uno dei criteri scelti non è rispettato diventa false. Se alla fine dei controlli è rimasta...
						//...true la proiezione considerata correntemente rispetta requisiti. (Essendo default true ci pox essere problemi se i controlli non funz...
						//...come dovrebbero xk in quel caso potrebbero essere considerate valide proiez che non lo sono).
						
						
						Proiezione proiez = estraiProiezione(scFile,numvirgoleintestaz); //Estraggo una proiezione dal file delle proiezioni e la metto in proiez.
						
						//Inizio blocco per confronto tra criteri scelti e stessi criterii nella riga/proiez letta da file.
						
						if(scelteCriteri[0].compareTo("1") == 0) { //Verifica titolo.
							
							//Metto entrambi i titoli (quello ricercato e quello della proiezione considerata attualmente) a minuscolo per non far contare le maiuscole.
							titoloTempLowercaseRicerca = proiez.getFilm().getTitolo().toLowerCase(); 
							titoloRicLowercaseRicerca = titoloRicerca.toLowerCase(); 
							
							//Confronto titoli.
							if (titoloRicLowercaseRicerca.length() > titoloTempLowercaseRicerca.length()) { //Caso titolo cercato ha lungh maggiore del titolo...
							//...proiez corrente.
								proiezOk = false;
							} else if (titoloRicLowercaseRicerca.length() == titoloTempLowercaseRicerca.length()) { //Caso titolo cercato ha lungh uguale al...
							//...titolo proiezione corrente.
								if(titoloTempLowercaseRicerca.compareTo(titoloRicLowercaseRicerca) != 0)
									proiezOk = false;
							} else { //Caso titolo cercato ha lungh minore del titolo proiezione corrente...
								//...quindi devo vedere se il titolo della proiez corrente contiene il titolo cercato; per farlo uso titoloParzRicerca
								///...che contiene man mano pezzi del titolo della proiez corrente lunghi quanto il titolo cercato.
								//Es se cerco "evo", che è lungo 3, titoloParzRicerca conterrà man mano tutti i pezzi di lungh 3 del titolo della proiez corrente per...
								//...vedere se uno di questi pezzi è "evo".
								boolean trovato = false;
								for (int i=0; i+titoloRicLowercaseRicerca.length() <= titoloTempLowercaseRicerca.length(); i++) { //For per vedere se il titolo della...
								//...proiez corrente contiene il titolo cercato.
									titoloParzRicerca = titoloTempLowercaseRicerca.substring(i, i+titoloRicLowercaseRicerca.length());
									if (titoloParzRicerca.compareTo(titoloRicLowercaseRicerca) == 0) //Se trovo un pezzo del titolo della proiez corrente che...
									//...è uguale al titolo cercato...
										trovato = true; //...trovato diventa true.
								}
								if (trovato == false) //Se alla fine trovato è rimasto a false vuol dire che il titolo della proiez corrente non contiene il titolo
								//...cercato.
									proiezOk = false;
							}
							
						} //Fine verifica titolo.
						
						if (scelteCriteri[1].compareTo("1") == 0 && proiezOk == true) { //Verifica genere.
							//Metto entrambi i generi (quello ricercato e quello della proiezione considerata attualmente) a minuscolo per non far contare le maiuscole.
							genereTempLowercaseRicerca = proiez.getFilm().getGenere().toLowerCase(); 
							genereRicLowercaseRicerca = genereRicerca.toLowerCase(); 
							
							//Confronto generi.
							if (genereRicLowercaseRicerca.length() > genereTempLowercaseRicerca.length()) { //Caso genere cercato ha lungh maggiore del genere...
								//...proiez corrente.
								proiezOk = false;
							} else if (genereRicLowercaseRicerca.length() == genereTempLowercaseRicerca.length()) { //Caso genere cercato ha lungh uguale al genere...
								//...proiezione corrente.
								if (genereTempLowercaseRicerca.compareTo(genereRicLowercaseRicerca) != 0)
									proiezOk = false;
							} else { //Caso genere cercato ha lungh minore del genere proiezione corrente...
								//...quindi devo vedere se il genere della proiez corrente contiene il genere cercato; per farlo uso genereParzRicerca...
								//...che contiene man mano pezzi del genere della proiez corrente lunghi quanto il genere cercato.
								//Es se cerco "horr", che è lungo 4, genereParzRicerca conterrà man mano tutti i pezzi di lungh 4 del genere della proiez corrente...
								//...per vedere se uno di questi è "horr".
								boolean trovato = false;
								for (int i=0; i+genereRicLowercaseRicerca.length() <= genereTempLowercaseRicerca.length(); i++) { //For per vedere se il genere...
									//...della proiez corrente contiene il genere cercato.
									genereParzRicerca = genereTempLowercaseRicerca.substring(i, i+genereRicLowercaseRicerca.length());
									if (genereParzRicerca.compareTo(genereRicLowercaseRicerca) == 0) //Se trovo un pezzo del genere della proiez corrente che...
									//...è uguale al genere cercato...
										trovato = true; //...trovato diventa true.
								}
								if (trovato == false) //Se alla fine trovato è rimasto a false vuol dire che il genere della proiez corrente non contiene il genere
									//...cercato.
										proiezOk = false;
							}
							//Fine blocco confronto generi.
						} //Fine verifica genere.
						
						if (scelteCriteri[2].compareTo("1") == 0 && proiezOk == true) { //Verifica data.
							
						switch (sceltaRicercaData){ //Inizio switch in cui è effettuata la verifica data.
							
							case "1": //Caso ricerca prima di una certa data.
								
								//Confronto date.
								if (proiez.getDataOra().toLocalDate().compareTo(dataMaxRicerca) >= 0)
									proiezOk = false;
								//Fine blocco confronto date.
								
								break;
								
							case "2": //Caso ricerca dopo una certa data.
								
								//Confronto date.
								if (proiez.getDataOra().toLocalDate().compareTo(dataMinRicerca) <= 0)
									proiezOk = false;
								//Fine blocco confronto date.
								
								break;
							
							case "3": //Caso ricerca tra due date.
								
								//Confronto date.
								if (proiez.getDataOra().toLocalDate().compareTo(dataMinRicerca) <= 0 
										|| proiez.getDataOra().toLocalDate().compareTo(dataMaxRicerca) >= 0)
									proiezOk = false;
								//Fine blocco confronto date.
									
								break;
								
							} //Fine switch in cui è effettuata la verifica data.
						} //Fine verifica data.
						
						if(scelteCriteri[3].compareTo("1") == 0 && proiezOk == true) { //Verifica prezzo.
							
							switch (sceltaRicercaPrezzo){ //Inizio switch in cui è effettuata la verifica prezzo.
								
							case "1": //Caso ricerca prezzo minore di un certo valore.
								
								//Confronto prezzi.
								if (proiez.getPrezzoBiglietto() >= prezzoMaxRicerca)
									proiezOk = false;
								//Fine blocco confronto prezzi.
								
								break;
								
							case "2": //Caso ricerca prezzo maggiore di un certo valore.
								
								//Confronto prezzi.
								if (proiez.getPrezzoBiglietto() <= prezzoMinRicerca)
									proiezOk = false;
								//Fine blocco confronto prezzi.
								
								break;
							
							case "3": //Caso ricerca prezzo compreso tra due valori.
								
								//Confronto prezzi.
								if (proiez.getPrezzoBiglietto() <= prezzoMinRicerca && proiez.getPrezzoBiglietto() >= prezzoMaxRicerca)
									proiezOk = false;
								//Fine blocco confronto prezzi.
								
								break;
								
							} //Fine switch in cui è effettuata la verifica prezzo.
						} //Fine verifica prezzo.
						
						//Fine blocco per confronto tra criteri scelti e stessi criterii nella riga/proiez letta da file.
						
						
						
						if(proiezOk == true) { //Se la proiezione è corretta...
							if (numRisRicerca >= limiteRic) {  //...controllo se ho raggiunto (o, per qualche strano motivo, superato) numero massimo di...
							//...risultati della ricerca...
								System.out.println("Numero massimo di risultati per la ricerca (" + limiteRic + ") raggiunto, non è possibile continuare la "
										+ "ricerca\n");
								System.out.println("Per effettuare una ricerca completa inserire criteri più restrittivi");
								sceltaOk = false;
							} 
							else { //Altrimenti se la proiezione è corretta e non ho raggiunto numero massimo di risultati...
								risRicerca[numRisRicerca] = proiez; //...salvo proiezione correntemente letta da file in vettore dei risultati della ricerca,...
								System.out.println();
								System.out.println(numRisRicerca+1); //...stampo numero del risultato perchè servirà per eventuale visualizzaProiezione...
								//...(stampa dettagli di 1 proiez),...
								proiez.visualizzaProiezione(); //...e stampo la proiez.
								numRisRicerca++;
							}
						} //Fine blocco if fatto se la proiezione è corretta.
						
					} //Fine while che legge il file delle proiezioni e controlla le proiezioni.
						
					break;
				
				case "0": //Caso ricerca annullata.
					return null;
					
				default:
					sceltaOk = false;
					System.out.println("L'opzione scelta non è valida. Inserire un'opzione valida:");
			}
		} while (sceltaOk == false); //Fine ciclo do while in cui è contenuto lo switch che effettua tutta la ricerca.
		
		
		
		if (numRisRicerca > 0) { //Inizio blocco che svolge funzionalità visualizzare in dettaglio una delle proiezioni cercate.
			
			String sceltaVisualizDettagl = "0"; //Variabile per scelta se visualizzare in dettaglio una delle proiez cercate, inizializ default a "0" cioè no.
			int sceltaNumProiezVisualiz = 1; //Variabile che conterrà il numero della proiez scelta da visualizzare in dettaglio, inizializ default a 1 cioè...
											//...la prima, che sicuramente c'è xk se siamo entrati nel blocco if in cui qsto codice si trova allora c'è almeno 1 ris.
			
			System.out.println("Si desidera visualizzare i dettagli di una delle proiezioni cercate?");
			System.out.println("Inserire 1 se sì, 0 altrimenti:");
			do { //Inizio ciclo do while per chiedere il numero della proiezione da visualizzare in dettaglio. Il while è while(true) (xk deve andare avanti finchè...
			//...l'utente inserisce una scelta valida).
				sceltaVisualizDettagl = sc.nextLine();
				
				switch(sceltaVisualizDettagl) {
				
				case "0": //Caso visualiz dettagliata di una delle proiez cercate rifiutata.
					return risRicerca;
					
				case "1": //Caso visualiz dettagliata di una delle proiez cercate richiesta.
					System.out.println("Inserire il numero della proiezione di cui si desidera visualizzare i dettagli (1 - " + numRisRicerca + "):");
					do {
						
						do {
							inputStringintOk=true;
							try {
								inputStringint = sc.nextLine();
								sceltaNumProiezVisualiz = Integer.parseInt(inputStringint);
							
							} catch (NumberFormatException e) {
								inputStringintOk = false;
								System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
							}
						} while (inputStringintOk == false);
						
						if (sceltaNumProiezVisualiz <= 0 || sceltaNumProiezVisualiz > numRisRicerca)
							System.out.println("Il numero inserito non è valido. Inserire un numero valido di una delle proiezioni"
									+ " cercate (1 - " + numRisRicerca + "):");
						else {
							risRicerca[sceltaNumProiezVisualiz-1].visualizzaProiezioneDettagliata(); //C'è il -1 perchè all'utente le proiez sono visualiz...
							//...numerate da 1 (e quindi anche la sua scelta), mentre nel vettore sono numerate da 0.
							return risRicerca;
						}
					} while (true);
					
				default:
					System.out.println("L'opzione scelta non è valida. Inserire un'opzione valida:");
					
				}
			} while (true); //Fine ciclo per chiedere il numero della proiezione da visualizzare in dettaglio. Il while è while(true) xk deve andare avanti finchè...
			//...l'utente inserisce una scelta valida.
		}  else {
			System.out.println("La ricerca non ha risultati quindi non è possibile visualizzare i dettagli di una delle proiezioni cercate");
			return null;
		} //Fine blocco che svolge funzionalità visualizzare in dettaglio una delle proiezioni cercate.
		
	}
//Fine metodo cercaProiezione().

	public static Proiezione selezionaProiezDaRicerca(Proiezione[] risRicerca) {
		
		Scanner sc = new Scanner(System.in);
		String sceltaSelezione;
		int sceltaProiezione=1;
		
		//Variabili di appoggio per prendere in input un int. Si veda inizio classe il senso di utilizzo.
		String inputStringint;
		boolean inputStringintOk;
		
		if(risRicerca == null) {
			System.out.println("Non è possibile selezionare una proiezione");
			return null;
		}
		
		System.out.println("Si desidera selezionare una delle proiezioni trovate?");
		System.out.println("Inserire 1 se sì, 0 altrimenti:");
		
		do {
			sceltaSelezione = sc.nextLine();
			
			switch(sceltaSelezione) {
			
			case "0":
				return null;
				
			case "1":
				System.out.println("Inserire il numero della proiezione che si desidera selezionare (1 - " + risRicerca.length + "):");
				do {
					do {
						inputStringintOk=true;
						try {
							inputStringint = sc.nextLine();
							sceltaProiezione = Integer.parseInt(inputStringint);
						
						} catch (NumberFormatException e) {
							inputStringintOk = false;
							System.out.println("Non è stato inserito un numero intero. Inserire un numero intero: ");
						}
					} while (inputStringintOk == false);
					
					if (sceltaProiezione <= 0 || sceltaProiezione > risRicerca.length) {
						System.out.println("Il numero inserito non è valido. Inserire un numero valido di una delle proiezioni cercate (1 - "
							+ risRicerca.length + "):");
					} else {
							return risRicerca[sceltaProiezione-1];
					}
				} while (true);
			default:
				System.out.println("L'opzione scelta non è valida. Inserire un'opzione valida:");
			}
		} while (true);
		
	}
		
//Inizio metodo estraiProiezione().
	private Proiezione estraiProiezione(Scanner scanner, int numvirgoleintestaz) {
//Metodo che estrae e restituisce una proiezione dal file delle proiezioni, file letto con scanner fornito in input.
//Il metodo assume che lo scanner dato in input è già predisposto per leggere una riga del file valida (e quindi se si è all'inizio si è già saltata la riga...
//...dell'intestazione, se si è alla fine del file non si chiama qsto metodo, etc...).
//Il secondo parametro è il numero di virgole dell'intestazione che serve perchè se la riga letta dal file ha più virgole di quel numero (non può averne di meno...
//...xk almeno tante virgole quante ce ne sono nell'intestazione le deve avere) allora il titolo contiene delle virgole e quindi il codice agisce di conseguenza).

		
		int contaVirgole = 0; //Contatore delle virgole della riga che sto leggendo in questo momento, utile per capire se ci sono virgole in più rispetto...
		//...all'intestazione xk anche il titolo ha delle virgole al suo interno.
		int differenzaVirgole = 0; //Contatore della differenza delle virgole tra numero di virgole della riga/proiezione che sto leggendo e numero standard di virgole.
		boolean virgolaDxTrovata = false; //True quando trovo la virgola che delimita a destra il titolo della riga/proiezione che sto leggendo.
		int indiceVirgolaDxTitolo = 0; //Indice della posizione della virgola che delimita a destra il tiolo della riga/proiezione che sto leggendo.
		
		String riga; //Variabile per contenere una riga/proiezione estratta dal file delle proiezioni.
		
		//Variabili Temp per contenere i dati estratti da una riga/proiezione del file delle proiezioni.
		String annoTemp, meseTemp, giornoTemp, oraTemp, minutoTemp, secondoTemp, titoloTemp, genereTemp, registaTemp;
		int etàminTemp, durataTemp, annofilmTemp;
		Film filmTemp;
		double prezzoTemp;
		LocalDate dataTemp;
		LocalTime orarioTemp;
		LocalDateTime dataOrarioTemp;
		Proiezione proiezTemp;
		
		riga = scanner.next();
		
		//Questo blocco estrae la data dalla riga/proiezione letta dal file e la mette in dataTemp.
		annoTemp = riga.substring(1, 5);
		meseTemp = riga.substring(6, 8);
		giornoTemp = riga.substring(9, 11);
		dataTemp = LocalDate.of(Integer.parseInt(annoTemp), Integer.parseInt(meseTemp), Integer.parseInt(giornoTemp));
		
		//Questo blocco estrae l'orario dalla riga/proiezione letta dal file e lo mette in orarioTemp.
		oraTemp = riga.substring(12, 14);
		minutoTemp = riga.substring(15, 17);
		secondoTemp = riga.substring(18, 20);
		orarioTemp = LocalTime.of(Integer.parseInt(oraTemp), Integer.parseInt(minutoTemp), Integer.parseInt(secondoTemp));
		
		//Creazione data+orario della riga/proiezione e messo in dataOrarioTemp.
		dataOrarioTemp = dataTemp.atTime(orarioTemp);
		
		//Inizio blocco che lavora con virgole e gli altri campi.
		
		for(int i=0; i<riga.length(); i++) { //Conto le virgole nella riga/proiezione letta dal file.
			if (riga.charAt(i)==',')
					contaVirgole++;
		}
		
		differenzaVirgole = contaVirgole-numvirgoleintestaz; //Se vale 0 non ci sono virgole nel titolo, altrimenti sì.
		int virgoleDaPassare = differenzaVirgole; //Le virgole da incontrare nel titolo partono da differenzaVirgole (serve seconda var xk virgoleDPpassare...
												 //...viene modificata)
		
		for(int i=22; i<riga.length() && virgolaDxTrovata==false; i++) { //i è l'indice di dove guardo nella riga/proiezione, parte dal 1o carattere del titolo...
																		//...quindi salta la prima virgola, quella tra data+orario e titolo.
			if(riga.charAt(i)==',' && virgoleDaPassare==0) { //Se ho trovato una virgola e ho già incontrato virgole pari a quante ce ne sono nel titolo...
				indiceVirgolaDxTitolo=i; //...vuol dire che ho trovato la virgola a dx del titolo, quindi ne salvo l'indice...
				virgolaDxTrovata = true;  //...e imposto per uscire dal ciclo.
			}
			else if (riga.charAt(i)==',' && virgoleDaPassare!=0) //Se ho trovato una virgola e non ho incontrato virgole pari a quante ce ne sono nel titolo...
				virgoleDaPassare--;//...allora quella che ho trovato è una virgola nel titolo, quindi diminuisco le virgole da trovare.
		}
		
		//Estraggo il titolo dalla riga/proiezione letta dal file e lo metto in titoloTemp.
		titoloTemp = riga.substring(22, indiceVirgolaDxTitolo); //NB la substring prende come indice dx il 2o parametro-1 quindi è giusto mettere indiceVirgolaDxTitolo
		
		int virgolasx=indiceVirgolaDxTitolo, virgoladx=indiceVirgolaDxTitolo+1;
		//Questi due saranno due indici (che traslano con il while che si vedrà usato + volte) per delimitare la virgola sx e dx dei campi dopo il titolo.
		//Siccome adesso il campo successivo è genere che è quello subito dopo il titolo l'indice sx parte sulla virgoladx del titolo e quello dx sul carattere...
		//...immediatamente dopo, cioè il 1o carattere del genere.
		
		while(riga.charAt(virgoladx) != ',' && virgoladx < riga.length()) //Estrazione genere messo in genereTemp.
			virgoladx++;
		genereTemp = riga.substring(virgolasx+1, virgoladx);
		virgolasx = virgoladx; //virgolasx si sposta dalla virgola a sx del genere alla virgola a dx del genere che è anche la virgola a sx del prossimo...
								//...campo cioè regista.
		virgoladx++; //virgoladx si sposta avanti di 1 per stare sul 1o carattere del prossimo campo cioè regista.
		
		while(riga.charAt(virgoladx) != ',' && virgoladx < riga.length()) //Estrazione regista messo in registaTemp.
			virgoladx++;
		registaTemp = riga.substring(virgolasx+1, virgoladx);
		virgolasx = virgoladx;
		virgoladx++;
		
		while(riga.charAt(virgoladx) != ',' && virgoladx < riga.length())  //Estrazione anno film messo in annofilmTemp.
			virgoladx++;
		annofilmTemp = Integer.parseInt(riga.substring(virgolasx+1, virgoladx));
		virgolasx = virgoladx;
		virgoladx++;
		
		while(riga.charAt(virgoladx) != ',' && virgoladx < riga.length())  //Estrazione durata film messa in durataTemp.
			virgoladx++;
		durataTemp = Integer.parseInt(riga.substring(virgolasx+1, virgoladx));
		virgolasx = virgoladx;
		virgoladx++;
		
		while(riga.charAt(virgoladx) != ',' && virgoladx < riga.length())  //Estrazione età minima film messa in etàminTemp.
			virgoladx++;
		etàminTemp = Integer.parseInt(riga.substring(virgolasx+1, virgoladx));
		virgolasx = virgoladx;
		virgoladx++;
		
		//Creazione film filmTemp con i vari campi presenti nelle variabili "Temp".
		filmTemp = new Film(titoloTemp, genereTemp, registaTemp, annofilmTemp, durataTemp, etàminTemp);
		
		
		//Estrazione prezzo proiezione messo in prezzoTemp.
		prezzoTemp = Double.parseDouble(riga.substring(virgolasx+1, riga.length() ) );
		
		proiezTemp = new Proiezione(filmTemp, dataOrarioTemp, prezzoTemp);

		
		return proiezTemp;
	}
//Fine metodo estraiProiezione().
	
//Inizio metodo dettagliProiezione().
	/* 
	public void dettagliProiezione() { // Questo metodo permette la visualizzazione dei dettagli di una delle proiezioni cercate.
		// TODO Da decidere se effettivamente metodo a sè o se solo parte del codice di cercaProiezione() come è per ora
		return null;
	}
	*/
//Fine metodo dettagliProiezione().

//Inizio metodo cercaProiezPerCambioPrenotaz().
	public LinkedList<Proiezione> cercaProiezPerCambioPrenotaz(String titoloPrenotaz) throws FileNotFoundException {
//Metodo che permette la ricerca di proiezioni per effettuare cambio data prenotazione. Il metodo restituisce la linkedlist contenente le proiezioni trovate...
//che hanno stesso titolo film della prenotazione da modificare e data successiva alla data odierna.
		
		Scanner scFile = new Scanner(new File("../data/proiezioni.csv")); //scFile è lettore file proiezioni.
		scFile.useDelimiter("\n"); //Il separatore per distinguere una "cosa" letta dal file dalla successiva è l'a-capo, quindi ogni .next() legge una riga...
		//...del file.
		
		int numvirgoleintestaz = 7; // TODO DA SISTEMARE IN MODO CHE SIA GIUSTO SE AGGIUNGIAMO NUM POSTI AL FILE PROIEZIONI
		scFile.next(); //Salto la prima riga del file proiezioni che è l'intestazione.
		
		LinkedList<Proiezione> risRicerca = new LinkedList<Proiezione>(); //Linkedlist che rappresenta il risultato della ricerca cioè contiene le proiezioni...
		//...che rispettano il criterio scelto.
		
		
		while(scFile.hasNext()) { //Ciclo per leggere una proiezione dal file delle proiezioni e verificare se rispetta requisiti.
			
			Proiezione proiez = estraiProiezione(scFile,numvirgoleintestaz); //Estraggo una proiezione dal file delle proiezioni e la metto in proiez.
			
			if (titoloPrenotaz.compareTo(proiez.getFilm().getTitolo()) == 0 && proiez.getDataOra().toLocalDate().isAfter(LocalDate.now()) )
				risRicerca.add(proiez);
			
		} //Fine while che legge il file delle proiezioni e controlla le proiezioni.
					
		return risRicerca;
	}
//Fine metodo cercaProiezionePerTitolo().
		
} // Fine classe Guest.
