package CineMaX;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Classe che rappresenta una singola proiezione del cinema
// Implementa Comparable per permettere l'ordinamento in base alla data
public class Proiezione implements Comparable<Proiezione> {
    // Colleghiamo la proiezione all'oggetto Film scritto nella classe film
    private Film film;     
    private LocalDateTime dataOra;            
    private double prezzoBiglietto; 
    private static final int CAPIENZA_MASSIMA = 200; 

    // Costruttore: riceve il film, la data e il prezzo (la sala non serve essendo monosala)
    public Proiezione(Film film, LocalDateTime dataOra, double prezzoBiglietto) {
        this.film = film;
        this.dataOra = dataOra;
        this.prezzoBiglietto = prezzoBiglietto;
    }

    // Calcola i posti liberi sottraendo quelli prenotati ai 200 totali
    public int calcolaPostiLiberi(int postiPrenotati) {
        return Math.max(CAPIENZA_MASSIMA - postiPrenotati, 0);
    }

    // Confronta la data di questa proiezione con un'altra per l'ordinamento
    public int compareTo(Proiezione altra) {
        return this.dataOra.compareTo(altra.getDataOra());
    }

    // Stampa a schermo tutti i dettagli della proiezione
    public void visualizzaProiezione(int postiPrenotati) {
        // Formato italiano per la data e l'orario
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        System.out.println("--- DETTAGLIO PROIEZIONE ---");
        System.out.println("Film: " + film.getTitolo() + " (" + film.getGenere() + ", " + film.getDurata() + " min)");
        System.out.println("Data e Ora: " + dataOra.format(formatter));
        System.out.println("Costo Biglietto: €" + prezzoBiglietto);
        System.out.println("Posti Liberi: " + calcolaPostiLiberi(postiPrenotati) + " / " + CAPIENZA_MASSIMA);
        System.out.println("----------------------------");
    }

    // Metodi Getter e Setter
    public Film getFilm() { return film; }
    public void setFilm(Film film) { this.film = film; }

    public LocalDateTime getDataOra() { return dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }

    public double getPrezzoBiglietto() { return prezzoBiglietto; }
    public void setPrezzoBiglietto(double prezzoBiglietto) { this.prezzoBiglietto = prezzoBiglietto; }
}
