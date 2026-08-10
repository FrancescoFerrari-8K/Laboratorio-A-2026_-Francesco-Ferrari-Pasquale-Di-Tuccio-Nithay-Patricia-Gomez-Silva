package CineMaX;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class CineMaX {

	// All'avvio l'app mostra menù iniziale in cui è possibile fare 3 cose: loggarsi, registrarsi o proseguire come utente non registrato (guest).
	// ecc ecc

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("*****CineMaX*******");
		// bisogna fare una grafichina carina!!

		Guest User = new Guest(); // creo un utente guest per garantire l'utilizzo minimo dell'app

		try {
			ArrayList<LinkedList<String>> a = Film.caricaFilm();
			System.out.println(a.size());

			Cliente c1 = new Cliente ("Nithay", "Gomez", "123", 000);
			c1.mostraMenuCliente(false);


			// ArrayList<Prenotazione> arrayTest = Prenotazione.caricaPrenotazioni();
			 //for(Prenotazione prenot : arrayTest){
			 	//System.out.println(prenot.toString());
			// }

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

}
