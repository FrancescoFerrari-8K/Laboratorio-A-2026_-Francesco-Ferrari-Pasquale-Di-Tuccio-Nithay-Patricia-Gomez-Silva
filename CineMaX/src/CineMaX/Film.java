package CineMaX;

public class Film { // Questa classe costruisce oggetti di tipo Film
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------	
	// Campi
    private String Titolo;
	private String Genere;
	private String Regista;
	private int Anno;
	private int Durata; // In minuti	
	private int Età; // Età minima
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------	
	// Costruttori

	public Film(String titolo, String genere, String regista, int anno, int durata, int età) {
		Titolo=titolo;
		Genere=genere;
		Regista=regista;
		Anno=anno;
		Durata=durata;
		Età=età;
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------	
	// Metodi
	
	// Getter
	
	
	public String getTitolo(){
		return Titolo;
	}
	
	public String getGenere(){
		return Genere;
	}
	
	public String getRegista(){
		return Regista;
	}
	
	public int getAnno(){
		return Anno;
	}
	
	public int getDurata(){
		return Durata;
	}
	
	public int getEtà(){
		return Età;
	}
	
	//Setter
	
	public void setTitolo(String titolo){
		Titolo=titolo;
	}
	
	public void setGenere(String genere){
		Genere=genere;
	}
	
	public void setRegista(String regista){
		Regista=regista;
	}
	
	public void setAnno(int anno){
		Anno=anno;
	}
	
	public void setDurata(int durata){
		Durata=durata;
	}
	
	public void setEtà(int età){
		Età=età;
	}
	
	public void visualizzaFilm() {
		System.out.println("Film: " + Titolo);
		System.out.println("Genere: " + Genere);
		System.out.println("Regista: " + Regista);
		System.out.println("Anno: " + Anno);
		System.out.println("Durata (min): " + Durata);
		System.out.println("Età minima: " + Età);

	}
	
} //Fine classe Film.
