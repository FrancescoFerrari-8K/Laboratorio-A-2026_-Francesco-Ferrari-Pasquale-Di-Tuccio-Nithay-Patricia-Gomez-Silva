package CineMaX;

public class Utente { // Questa classe costruisce oggetti di tipo "Utente"
	
	// Campi
	
	private String Nome;
	
	private String Cognome;
	
	private String Tipo; // G = guest ; P = proiezionista ; B = bigliettaio ; C = cliente
	
	private String Password;
	
	private int ID;
	
	// Costruttori (overloading!)
	
	public Utente(){ // questo costruttore crea un'utente di tipo guest. Non servono informazioni!
		this.Tipo="G";
		
	}
	
	public Utente(String nome, String cognome, String tipo, String password, int id ){ 
		// questo costruttore crea un'utente di tipo registrato.
		this.Nome=nome;
		this.Cognome=cognome;
		this.Tipo=tipo;
		this.Password=password;
		this.ID=id;
	}
	
	
	

}
