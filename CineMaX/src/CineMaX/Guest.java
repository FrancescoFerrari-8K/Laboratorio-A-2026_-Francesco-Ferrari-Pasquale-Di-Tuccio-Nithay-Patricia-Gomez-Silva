package CineMaX;
import java.io.*;
import java.util.*;
import java.time.*;

// Questa classe implementa l'interfaccia GuestUser, dunque implementa i metodi che gestiscono le richieste
// degli utenti non registrati

public class Guest {
	
	// Campi
	
		private String Nome;
		
		private String Cognome;
		
		private String Tipo; // G = guest ; P = proiezionista ; B = bigliettaio ; C = cliente
		
		private String Password;
		
		private int ID;
 
		// Costruttori (overloading!)
		
		public Guest(){ // questo costruttore crea un'utente di tipo guest. Non servono informazioni!
			this.Tipo="G";
			
		}
		
		public Guest(String nome, String cognome, String tipo, String password, int id ){ 
			// questo costruttore crea un'utente di tipo registrato.
			this.Nome=nome;
			this.Cognome=cognome;
			this.Tipo=tipo;
			this.Password=password;
			this.ID=id;
		}


		public String getNome(){
			return this.Nome;
		}

		public void setNome(String nome){
			this.Nome = nome;
		}

		public String getCognome(){
			return this.Cognome;
		}

		public void setCognome(String Cognome){
			this.Cognome = Cognome;
		
		}
		
	public void cercaProiezione() throws FileNotFoundException {  // Questo metodo permette la ricerca e visualizzazione di proiezioni
		// Questo metodo permette la ricerca e visualizzazione di proiezione
		int limiteRic = 50; //Limite numero risultati della ricerca.
		int contaRic = 0; //Contatore numero risultati.
		int scelta=0; //Impostazione scelta default (che nel menù della ricerca annulla la ricerca).
		boolean proiezOk=false; //Variabile boolean per dire se proiezione che si sta considerando attualmente rispetta criterio ricerca, impostata default a false.
		
		//Variabili Ric che conterranno i criteri di ricerca. Dichiarati inizialmente xk nel caso di ricerca per combinazione di criteri ne serve più di una.
		String titoloRic, genereRic;
		float costominRic, costomaxRic;
		LocalDate dataminRic, datamaxRic;
		
		Scanner scFile = new Scanner(new File("../data/proiezioni.csv")); //scFile è lettore file.
		scFile.useDelimiter("\n"); //Il separatore per distinguere una "cosa" letta dal file dalla successiva è l'a-capo, quindi ogni .next legge una riga del file.
		scFile.next(); //Salto la prima riga del file proiezioni che ha l'intestazione.
		
		Proiezione[] risRicerca = new Proiezione[limiteRic]; //Vettore che rappresenta il risultato della ricerca cioè contiene le proiezioni che rispettano il criterio scelto.
		
		
		System.out.println("Ricerca di una proiezione");
		System.out.println("Selezionare un criterio per la ricerca:");
		System.out.println("1=per titolo");
		System.out.println("2=per tipologia/genere di film");
		System.out.println("3=per intervallo di date");
		System.out.println("4=per costo del biglietto");
		System.out.println("5=per una combinazione dei predenti criteri di ricerca");
		System.out.println("0=per annullare la ricerca");
		System.out.println("Criterio scelto: ");
		Scanner sc = new Scanner(System.in);
		scelta = sc.nextInt();
		sc.nextLine();
		switch (scelta) {
			case 1:
				System.out.println("Ricerca per titolo. Inserire il titolo (anche parziale) :");
				titoloRic = sc.nextLine(); //Leggo titolo da ricercare e lo metto in titoloRic.
				
				while(scFile.hasNext()) { //Ciclo per leggere una proiezione dal file delle proiezioni e verificare se rispetta requisiti.
					proiezOk=false;
					
					Proiezione proiez = estraiProiezione(scFile); //Estraggo una proiezione dal file delle proiezioni e la metto in proiez.
					
					//Inizio blocco per confronto titoli tra titolo cercato e titolo riga/proiez letta da file.
					
					//Metto entrambi i titoli (quello ricercato e quello della proiezione considerata attualmente) a minuscolo per non far contare le maiuscole.
					String titoloTempLowercase = proiez.getFilm().getTitolo().toLowerCase(); 
					String titoloRicLowercase = titoloRic.toLowerCase(); 
					
					//Confronto titoli.
					if(titoloRicLowercase.length() == titoloTempLowercase.length()) //Caso titolo cercato ha lungh uguale del titolo proiezione corrente.
						if(titoloTempLowercase.compareTo(titoloRicLowercase) == 0)
							proiezOk = true;
					if(titoloRicLowercase.length() < titoloTempLowercase.length()) { //Caso titolo cercato ha lungh minore del titolo proiezione corrente...
						String titoloParz; //...quindi devo vedere se il titolo della proiez corrente contiene il titolo cercato; per farlo uso titoloParz che contiene...
										  //...man mano pezzi del titolo della proiez corrente lunghi quanto il titolo cercato.
						//Es se cerco "evo", che è lungo 3, titoloParz conterrà man mano tutti i pezzi di lungh 3 del titolo della proiez corrente per vedere se uno di questi...
						//...è "evo".
						for(int i=0; i+titoloRicLowercase.length() <= titoloTempLowercase.length(); i++) {
							titoloParz = titoloTempLowercase.substring(i, i+titoloRicLowercase.length());
							if( titoloParz.compareTo(titoloRicLowercase) == 0)
								proiezOk = true;
						}
					}
					if(proiezOk == true) { //Se titolo è corretto...
						if (contaRic == limiteRic) {  //...controllo se ho raggiunto numero massimo di risultati della ricerca...
							System.out.println("Numero massimo di risultati per la ricerca (" + limiteRic + ") raggiunto, non è possibile continuare la ricerca\n");
							System.out.println("Per effettuare una ricerca completa inserire criteri più restrittivi");
							return;
						} //...e se non ho raggiunto numero massimo di risultati...
						else {
							risRicerca[contaRic] = proiez; //...salvo proiezione correntemente letta da file e che ha titolo ricercato in vettore dei risultati della ricerca...
							System.out.println(contaRic+1); //...stampo numero del risultato perchè servirà per eventuale visualizzaProiezione (stampa dettagli di 1 proiez)...
							proiez.visualizzaProiez(); //...e stampo la proiez.
							contaRic++;
						}
					}
					
					
				}
				System.out.println("La ricerca ha dato " + contaRic + " risultati");
				break;
				
			case 2:
				
				break;
			case 3:
				
				break;
			case 4:
				
				break;
			case 5:
				
				break;
			case 0:
				return;
			default:
				System.out.println("L'opzione scelta non è valida");
		}
		return;
	}
	
	private Proiezione estraiProiezione(Scanner scanner) { //Metodo che estrae e restituisce una proiezione dal file delle proiezioni letto tramite scanner fornito in input.
		
		int numvirgoleintestaz=7; //Numero "standard" di virgole in una riga/proiezione del file, dato dall'intestazione (nel progetto sono 7 essendoci 8 campi nell'instestaz).
		int contavirgole=0; //Contatore delle virgole della riga che sto leggendo in questo momento, utile per capire se ci sono virgole in più xk il titolo ne ha all'interno.
		int differenzavirgole=0; //Contatore della differenza delle virgole tra numero di virgole della riga/proiezione che sto leggendo e numero standard di virgole.
		boolean virgoladxtrovata=false; //True quando trovo la virgola che delimita a destra il titolo della riga/proiezione che sto leggendo.
		int indicevirgoladxtitolo=0; //Indice della posizione della virgola che delimita a destra il tiolo della riga/proiezione che sto leggendo.
		
		String riga; //Variabile per contenere una riga/proiezione estratta dal file delle proiezioni.
		
		//Variabili Temp per contenere i dati estratti da una riga/proiezione del file delle proiezioni.
		String annoTemp, meseTemp, giornoTemp, oraTemp, minutoTemp, titoloTemp, genereTemp, registaTemp;
		int etàminTemp, durataTemp, annofilmTemp;
		Film filmTemp;
		double prezzoTemp;
		LocalDate dataTemp;
		LocalTime orarioTemp;
		Proiezione proiezTemp = new Proiezione();
		
		riga = scFile.next();
		
		//Questo blocco estrae la data dalla riga/proiezione letta dal file e la mette in dataTemp, dataTemp impostata come data di proiezTemp.
		annoTemp = riga.substring(1, 5);
		meseTemp = riga.substring(6, 8);
		giornoTemp = riga.substring(9, 11);
		dataTemp = LocalDate.of(Integer.parseInt(annoTemp), Integer.parseInt(meseTemp), Integer.parseInt(giornoTemp));
		proiezTemp.setData(dataTemp);
		
		//Questo blocco estrae l'orario dalla riga/proiezione letta dal file e lo mette in orarioTemp, orarioTemp impostato come orario di proiezTemp.
		oraTemp = riga.substring(12, 14);
		minutoTemp = riga.substring(15, 17);
		orarioTemp = LocalTime.of(Integer.parseInt(oraTemp), Integer.parseInt(minutoTemp));
		proiezTemp.setOrario(orarioTemp);
		
		for(int i=0; i<riga.length(); i++) { //Conto le virgole nella riga/proiezione letta dal file.
			if (riga.charAt(i)==',')
					contavirgole++;
		}
		
		differenzavirgole = contavirgole-numvirgoleintestaz; //Se vale 0 non ci sono virgole nel titolo, altrimenti sì.
		int virgoledapassare = differenzavirgole; //Le virgole da incontrare nel titolo partono da differenzavirgole (serve seconda var xk virgoledapassare...
												 //...viene modificata)
		
		for(int i=22; i<riga.length() && virgoladxtrovata==false; i++) { //i è l'indice di dove guardo nella riga/proiezione, parte dal 1o carattere del titolo...
																		//...quindi salta la prima virgola, quella tra dataorario e titolo.
			if(riga.charAt(i)==',' && virgoledapassare==0) { //Se ho trovato una virgola e ho già incontrato virgole pari a quante ce ne sono nel titolo...
				indicevirgoladxtitolo=i; //...vuol dire che ho trovato la virgola a dx del titolo, quindi ne salvo l'indice...
				virgoladxtrovata=true;  //...e imposto per uscire dal ciclo
			}
			else if (riga.charAt(i)==',' && virgoledapassare!=0) //Se ho trovato una virgola e non ho incontrato virgole pari a quante ce ne sono nel titolo...
				virgoledapassare--;//...allora quella che ho trovato è una virgola nel titolo, quindi diminuisco le virgole da trovare.
		}
		
		//Estraggo il titolo dalla riga/proiezione letta dal file e lo metto in titoloTemp.
		titoloTemp = riga.substring(22, indicevirgoladxtitolo); //NB la substring prende come indice dx il 2o parametro-1 quindi è giusto mettere indicevirgoladx
		
		int virgolasx=indicevirgoladxtitolo, virgoladx=indicevirgoladxtitolo+1;
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
		
		//Creazione film filmTemp con i vari campi presenti nelle variabili "Temp"...
		filmTemp = new Film(titoloTemp, genereTemp, registaTemp, annofilmTemp, durataTemp, etàminTemp);
		proiezTemp.setFilm(filmTemp); //...e inserimento filmTemp in proiezTemp.
		
		while(virgoladx < riga.length() && riga.charAt(virgoladx) != ',') //Estrazione prezzo proiezione messo in prezzoTemp, prezzoTemp messo in proiezTemp.
			virgoladx++;
		prezzoTemp = Double.parseDouble(riga.substring(virgolasx+1, virgoladx));
		proiezTemp.setPrezzo(prezzoTemp);
		
		return proiezTemp;
		
		
		
	}
	
	public Film dettagliProiezione() { // Questo metodo permette la visualizzazione dei dettagli di un film
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public Guest registrati() { // Questo metodo permette di registrarsi come nuovo utente
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public Guest login() { // Questo metodo permette di accedere come utente registrato
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public void logout() { // Questo metodo permette di effettuare il logout;
		// TODO Auto-generated method stub
		
	}
	
}
