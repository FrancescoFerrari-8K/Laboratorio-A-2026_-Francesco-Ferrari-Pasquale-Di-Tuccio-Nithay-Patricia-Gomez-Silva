package CineMaX;

public class Prenotazione { // Questa classe crea oggetti di tipo prenotazione
	
	// Campi
	
	private String Nome;
	private String Cognome;
	private Film Film;
	private int Fila;
	private int Posto;
	private int ID;
	
	// Costruttori
	
	public Prenotazione(String nome, String cognome, Film film, int fila, int posto, int id) {
		this.Nome=nome;
		this.Cognome=cognome;
		this.Film=film;
		this.Fila=fila;
		this.Posto=posto;
		this.ID=id;
	}

}
