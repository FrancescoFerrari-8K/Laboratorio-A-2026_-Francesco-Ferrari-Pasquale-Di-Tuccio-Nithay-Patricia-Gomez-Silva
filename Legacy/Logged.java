package CineMaX;

//Questa classe implementa l'interfaccia LoggedUser, dunque implementa i metodi che gestiscono le richieste
//degli utenti registrati

public class Logged implements LoggedUser {
	
	// -------------------------------------Per tutti----------------------------------------
	
	@Override
	public Utente Registrati() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utente Login() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Logout() {
		// TODO Auto-generated method stub
		
	}
	
	//-------------------------------------- Clienti ----------------------------------------------------------

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
	public Prenotazione PrenotaFilm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Prenotazione ModificaPrenotazione() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void EliminaPrenotazione() {
		// TODO Auto-generated method stub
		
	}
	
	//----------------------------------------------Proiezionisti----------------------------------------------------------

	@Override
	public Film InserisciFilm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Film ModificaFilm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void EliminaFilm() {
		// TODO Auto-generated method stub
		
	}
	
	//----------------------------------------------- Bigliettai--------------------------------------------------------------

	@Override
	public Prenotazione PrenotazioniOggi() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Prenotazione PrenotazioniGiorno() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Prenotazione CercaPrenotazione() {
		// TODO Auto-generated method stub
		return null;
	}

}
