package CineMaX;

public class Film { // Questa classe costruisce oggetti di tipo Film
	
	// Campi
	
    private String Titolo;
	
	private String Genere;
	
	private String Regista;
	
	private int Anno;
	
	private int Durata; // In minuti	

	private int Età; // Età minima
	
	// Costruttori
	
	public Film(String titolo, String genere, String regista, int anno, int durata, int età) {
		
		this.Titolo=titolo;
		this.Genere=genere;
		this.Regista=regista;
		this.Anno=anno;
		this.Durata=durata;
		this.Età=età;
	}

}
