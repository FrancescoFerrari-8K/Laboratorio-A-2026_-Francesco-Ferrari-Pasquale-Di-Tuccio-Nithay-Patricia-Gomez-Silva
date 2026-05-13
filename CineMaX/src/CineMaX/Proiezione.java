package CineMaX;

public class Proiezione {
	
	
	
	private Film film; //Film proiettato
	private String Data; //Data della proiezione
	private String Ora; //Ora della proiezione
	private double Prezzo; // Prezzo del biglietto
	
	public Proiezione(Film film, String data, String ora, double prezzo) {
		this.film = film;
		Data = data;
		Ora = ora;
		Prezzo = prezzo;
	}
	
}

