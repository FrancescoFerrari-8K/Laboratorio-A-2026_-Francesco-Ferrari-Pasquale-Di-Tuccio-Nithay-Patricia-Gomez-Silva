package CineMaX;

// Questa interfaccia contiene le firme dei metodi che gestiscono le richieste degli utenti registrati
// cioé clienti, proiezionisti e bigliettai
// Questa interfaccia estende GuestUser, quindi eredita i suoi metodi! Si veda l'interfaccia GuestUser.

public interface LoggedUser extends GuestUser {
	
	// gli utenti loggati si dividono in tre categorie: clienti, proiezionisti e bigliettai
	// quest'interfaccia contiene i metodi per tutti i tipi di utente, divisi in categorie
	// i metodi per gli utenti registrati saranno implementati tutti nella stessa classe
	// ma opereranno su oggetti di tipo diverso (un tipo per categoria di utente)!
	
	// ---------------------------------- Clienti -------------------------------------------------------
	
	// Il cliente loggato fa poco più dell'utente guest: oltre a visualizzare i film ecc, può fare una 
	// prenotazione per uno spettacolo, eliminarla o modificarla
	
	Prenotazione PrenotaFilm(); // Questo metodo permette di effettuare una prenotazione
	
	Prenotazione ModificaPrenotazione(); // Questo metodo permette di effettuare una prenotazione
	
	void EliminaPrenotazione(); // Questo metodo permette di effettuare una prenotazione
	
	// ---------------------------------- Proiezionisti -------------------------------------------------
	
	// I proiezionisti dirigono le operazioni! I metodi che li riguardano consentono di gestire il palinsesto
	// (elenco proiezioni) del cinema
	
	Film InserisciFilm(); // Questo metodo permette di inserire una nuova proiezione a palinsesto
	
	Film ModificaFilm(); // Questo metodo permette di modificare i dati di una proiezione nel palinsesto
	
	void EliminaFilm(); // Questo metodo permette di eliminare una proiezione dal palinsesto
	
	// ---------------------------------- Bigliettai -----------------------------------------------------
	
	// I bigliettai gestiscono le prenotazioni del cinema e la vendita di biglietti
	
	Prenotazione PrenotazioniOggi(); // Questo metodo permette di vedere le prenotazioni odierne
	
	Prenotazione PrenotazioniGiorno(); // Questo metodo permette di vedere le prenotazioni ne giorno specificato
    
	Prenotazione CercaPrenotazione(); // Questo metodo permette di cercare una specifica prenotazione




}
