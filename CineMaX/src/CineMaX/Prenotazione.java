package CineMaX;

public class Prenotazione { // Questa classe crea oggetti di tipo prenotazione
	
	// Campi
	// private String idCliente;
	private String Nome;
	private String Cognome;
	private Proiezione proiezione;
	private int Fila;
	private int NPosti;
	private int ID;

	
	// Costruttori
	
	public Prenotazione(String nome, String cognome, Proiezione proiezione, int fila, int NPosti) {
		this.Nome=nome;
		this.Cognome=cognome;
		this.proiezione = proiezione;
		this.Fila=fila;
		this.NPosti=NPosti;
		this.ID= Math.random()>0.5 ? (int)(Math.random() * 1000) : (int)(Math.random() * 1000) * -1; // genera un ID casuale tra -1000 e 1000 (codice univoco)
	}


	
}
