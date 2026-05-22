package CineMaX;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class CineMaX {

	// All'avvio l'app crea un oggetto UtenteGuest (non registrato) per permettere
	// la ricerca di una proiezione
	// ecc ecc

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("*****CineMaX*******");
		// bisogna fare una grafichina carina!!

		Guest User = new Guest(); // creo un utente guest per garantire l'utilizzo minimo dell'app

		try {
			ArrayList<LinkedList<String>> a = Film.caricaFilm();
			System.out.println(a.size());
			
			mostraMenuCliente(false);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	}
	//mettere test true mostra l'interfaccia del menu cliente//
	public static void  mostraMenuCliente(boolean test){

	
		
		if (test == true) {
			int sceltaMenucliente = 0;
			Scanner sc = new Scanner(System.in);
			

			do {
				//interfaccia di menuCliente//
				System.out.println("---MENU CLIENTE---");
				System.out.println("");
				System.out.println("-Scegli un numero (1,2,3) per continuare-");
				System.out.println("");
				System.out.println("1- Cerca proiezione e prenota");
				System.out.println("2- Visualizzare le mie prenotazione");
				System.out.println("3- Logout");
				System.out.println("");
				System.out.print("Scelta: ");
				sceltaMenucliente = sc.nextInt();

				switch (sceltaMenucliente) {
					case 1:
						int sceltaProiezione;

						do {
							//interfaccia di cerca proiezione//
							System.out.println("---CERCA PROIEZIONE---");
							System.out.println("");
							System.out.println("-Scegli un numero per continuare-");
							System.out.println("");
							System.out.println("1- Genere");
							System.out.println("2- Titolo");
							System.out.println("3- Data");
							System.out.println("4- Costo");
							//TODO: fare proiezioni disponibili//
							System.out.println("5- Mostra proiezioni disponibili");
							System.out.println("0- INDIETRO");
							System.out.println("");
							System.out.print("Scelta: ");
							sceltaProiezione = sc.nextInt();

							switch (sceltaProiezione) {

								//interfaccia di genere//
								case 1:
									int sceltaGenere;

									System.out.println("---GENERE---");
									System.out.println("");
									System.out.println("-Scegli un numero per continuare-");
									System.out.println("");
									System.out.println("1- Adventure");
									System.out.println("2- Action");
									System.out.println("3- Animation");
									System.out.println("4- Biography");
									System.out.println("5- Comedy");
									System.out.println("6- Crime");
									System.out.println("7- Drama");
									System.out.println("8- Film-Noir");
									System.out.println("9- Horror");
									System.out.println("0- INDIETRO");
									System.out.println("");
									System.out.print("Scelta: ");
									sceltaGenere = sc.nextInt();
									break;

								case 2:
									//interfaccia di titolo//
									String sceltaTitolo;
									System.out.println("---TITOLO---");
									System.out.println("");
									System.out.println(
											"Inserisce il titolo del film che vuoi vedere oppure 0 per tornare indieto: ");
									sc.nextLine();
									sceltaTitolo = sc.nextLine();
									break;

								case 3:
									//interfaccia di data//
									String sceltaData;
									System.out.println("---DATA---");
									System.out.println("");
									//TODO: controllare se la data inserita e in un formatto corretto e se e una data futura//
									System.out.println(	"Inserisce data (GG/MM/YYYY) della proiezione che vuoi vedere oppure 0 per tornare indietro: ");
									sc.nextLine();
									sceltaData = sc.nextLine();
									System.out.println("");
									break;

								case 4:
									//interfaccia di costo//
									int sceltaCosto;
									System.out.println("---COSTO---");
									System.out.println("");
									System.out.println("-Scegli un numero per continuare-");
									System.out.println("");
									System.out.println("1- Minore di 5 Euro");
									System.out.println("2- Maggiore di 5 Euro");
									System.out.println("3- Tra 5£ e 15 Euro");
									System.out.println("0-INDIETRO");
									System.out.println("");
									System.out.println("Scelta: ");
									sceltaCosto = sc.nextInt();
									break;
										case 5:
											System.out.println("---PROIEZIONI DISPONIBILI---");
											System.out.println("");

							}

						} while (sceltaProiezione != 0);

						break;

					case 2:
						//interfaccia di prenotazione//
						int sceltaPrenotazioni;
						System.out.println("---LE MIE PRENOTAZIONI---");
						System.out.println("");
						System.out.println("-Scegli un numero per continuare-");
						System.out.println("");
						System.out.println("1- Prenota posto");
						System.out.println("2- Visualizzare prenotazione");
						System.out.println("3- Modificare prenotazione");
						System.out.println("4- Eliminare prenotazione");
						System.out.println("0- INDIETRO");
						System.out.println("");
						System.out.println("Scelta");
						sceltaPrenotazioni = sc.nextInt();

						switch(sceltaPrenotazioni){
							case 1:
								//interfaccia di prenotazione posto (manca finire)//
								System.out.println("---PRENOTA POSTO---");
								System.out.println("");
								break;

								case 2:
									System.out.println("---VISUAIZZA PRENOTAZIONE---");
									System.out.println("");
									break;

									case 3:
										System.out.println("---MODIFICA PRENOTAZIONE---");
										System.out.println("");
										break;

										case 4: 
										System.out.println("---ELIMINA PRENOTAZIONE---");
										System.out.println("");
										break;

									 
						}

						break;
					case 3:
						System.out.println("stai per efettuare il logout...");
						break;
					default:
						System.out.println("opzione non riconosciuta, riprova");
						break;

				}

			} while (sceltaMenucliente != 3);
			

		}

	}

	

}
