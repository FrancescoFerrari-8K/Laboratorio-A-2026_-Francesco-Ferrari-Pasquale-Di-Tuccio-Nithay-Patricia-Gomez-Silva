package CineMaX;

public class Film { // Questa classe costruisce oggetti di tipo Film
	
	// Campi
	
    private String Titolo;
	
	private String Genere;
	
	private String Regista;
	
	private String Data;
	
	private String Ora;
	
	private int Anno;
	
	private int Durata; // In minuti
	
	private int Età; // Età minima per assistere alla proiezione
	
	private double Prezzo; // Prezzo del biglietto
	
	// Costruttori
	
	public Film(String titolo, String genere, String regista, String data, String ora, int anno, int durata, int età, double prezzo  ) {
		
		this.Titolo=titolo;
		this.Genere=genere;
		this.Regista=regista;
		this.Data=data;
		this.Ora=ora;
		this.Anno=anno;
		this.Durata=durata;
		this.Età=età;
		this.Prezzo=prezzo;
		
	}

}
