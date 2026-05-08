package CineMaX;

// Questa interfaccia contiene le firme dei metodi che gestiscono le richieste degli utenti non registrati

public interface GuestUser {
	
	Film CercaFilm();  // Questo metodo permette la ricerca di una proiezione
	
	Film DettagliFilm(); // Questo metodo permette la visualizzazione dei dettagli di un film
	
	Utente Registrati();// Questo metodo permette di registrarsi come nuovo utente
	
	Utente Login();// Questo metodo permette di accedere come utente registrato
	
	void Logout();// Questo metodo permette di effettuare il logout;


}
