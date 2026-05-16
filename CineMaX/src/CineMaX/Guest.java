package CineMaX;

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

	public Film CercaFilm() {  // Questo metodo permette la ricerca di una proiezione
		// TODO Auto-generated method stub
		return null;
	}

	
	public Film DettagliFilm() { // Questo metodo permette la visualizzazione dei dettagli di un film
		// TODO Auto-generated method stub
		return null;
	}

	
	public Guest Registrati() { // Questo metodo permette di registrarsi come nuovo utente
		// TODO Auto-generated method stub
		return null;
	}

	
	public Guest Login() { // Questo metodo permette di accedere come utente registrato
		// TODO Auto-generated method stub
		return null;
	}

	
	public void Logout() { // Questo metodo permette di effettuare il logout;
		// TODO Auto-generated method stub
		
	}

}
