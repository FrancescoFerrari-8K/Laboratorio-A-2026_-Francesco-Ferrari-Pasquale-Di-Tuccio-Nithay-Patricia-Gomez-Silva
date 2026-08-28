package CineMaX;
/**
 * Questa classe costruisce oggetti di tipo Film
 * che sono utilizzati per costruire oggetti di tipo 
 * Proiezione
 */
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
/**
 * Questo costruttore forma oggetti di tipo Film
 * 
 * @param titolo il titolo del film come String
 * @param genere il genere del film come String
 * @param regista il regista del film come String
 * @param anno l'anno in cui è uscito il film come int
 * @param durata la durata in minuti del film come int
 * @param età l'età minima consigliata per la visione del film come int
 */
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
	
	/**
	 * Questo metodo restituisce il titolo del film
	 * @return il titolo del film come String
	 */
	public String getTitolo(){
		return Titolo;
	}
	/**
	 * Questo metodo restituisce il genere del film
	 * @return il genere del film come String
	 */
	public String getGenere(){
		return Genere;
	}
	/**
	 * Questo metodo restituisce il regista del film
	 * @return il nome del regista del film come String
	 */
	public String getRegista(){
		return Regista;
	}
	/**
	 * Questo metodo restituisce l'anno di pubblicazione del film
	 * @return l'anno di pubblicazione del film come int
	 */
	public int getAnno(){
		return Anno;
	}
	/**
	 * Questo metodo restituisce la durata del film in minuti
	 * @return la durata del film in minuti come int
	 */
	public int getDurata(){
		return Durata;
	}
	/**
	 * Questo metodo restituisce l'età minima consigliata per la visione del film
	 * @return l'età minima consigliata per la visione del film come int
	 */
	public int getEtà(){
		return Età;
	}
	
	//Setter
	/**
	 * Questo metodo imposta il titolo del film
	 * @param titolo il titolo del film passato come String
	 */
	public void setTitolo(String titolo){
		Titolo=titolo;
	}
	/**Questo metodo imposta il genere del film
	 * @param genere il genere del film passato come String
	 */
	public void setGenere(String genere){
		Genere=genere;
	}
	/**
	 * Questo metodo imposta il regista del film
	 * @param regista il regista del film passato come String
	 */
	public void setRegista(String regista){
		Regista=regista;
	}
	/**
	 * Questo metodo imposta l'anno di pubblicazione del film
	 * @param anno l'anno di pubblicazione del film passato come int
	 */
	public void setAnno(int anno){
		Anno=anno;
	}
	/**
	 * Questo metodo imposta la durata del film
	 * @param durata la durata del film passata come int
	 */
	public void setDurata(int durata){
		Durata=durata;
	}
	/**
	 * Questo metodo imposta l'età minima consigliata per la visione del film
	 * @param età l'età minima consigliata per la visione del film passata come int
	 */
	public void setEtà(int età){
		Età=età;
	}
	/**
	 * Questo metodo stampa a schermo le caratteristiche di un Film
	 */
	public void visualizzaFilm() {
		System.out.println("Film: " + Titolo);
		System.out.println("Genere: " + Genere);
		System.out.println("Regista: " + Regista);
		System.out.println("Anno: " + Anno);
		System.out.println("Durata (min): " + Durata);
		System.out.println("Età minima: " + Età);

	}
	
} //Fine classe Film.
