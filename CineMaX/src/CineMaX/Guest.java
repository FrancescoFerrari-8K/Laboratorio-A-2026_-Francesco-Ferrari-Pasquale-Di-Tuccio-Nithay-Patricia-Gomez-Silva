package CineMaX;

// Questa classe implementa l'interfaccia GuestUser, dunque implementa i metodi che gestiscono le richieste
// degli utenti non registrati

public class Guest implements GuestUser {
	
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

	@Override
	public Film CercaFilm() { 
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Film DettagliFilm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Guest Registrati() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Guest Login() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Logout() {
		// TODO Auto-generated method stub
		
	}

}
