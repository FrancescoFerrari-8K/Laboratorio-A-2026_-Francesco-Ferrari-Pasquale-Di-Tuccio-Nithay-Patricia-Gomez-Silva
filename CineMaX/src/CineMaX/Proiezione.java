package CineMaX;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

// Classe che rappresenta una singola proiezione del cinema (monosala da 200 posti)
public class Proiezione implements Comparable<Proiezione> {

    private Film film;    
    private LocalDateTime dataOra;            
    private double prezzoBiglietto; 
    
    // Capienza massima del cinema monosala
    public static final int CAPIENZA_MASSIMA = 200; 

    // Costruttore
    public Proiezione(Film film, LocalDateTime dataOra, double prezzoBiglietto) {
        this.film = film;
        this.dataOra = dataOra;
        this.prezzoBiglietto = prezzoBiglietto;
    }

    // Calcola quanti posti sono stati prenotati nel CSV per questa specifica proiezione
    public int calcolaPostiOccupati() {
        ArrayList<Prenotazione> tutteLePrenotazioni = Prenotazione.caricaPrenotazioni();
        int postiOccupati = 0;
        
        for (Prenotazione pr : tutteLePrenotazioni) {
            if (pr.getProiezione_Titolo().equalsIgnoreCase(this.film.getTitolo()) &&
                pr.getProiezione_Data().equals(this.dataOra)) {
                postiOccupati += pr.getNPosti();
            }
        }
        return postiOccupati;
    }

    // Calcola i posti ancora disponibili
    public int calcolaPostiLiberi() {
        return Math.max(CAPIENZA_MASSIMA - calcolaPostiOccupati(), 0);
    }

    // Controlla se ci sono abbastanza posti disponibili per accogliere la richiesta
    public boolean haPostiDisponibili(int postiRichiesti) {
        return calcolaPostiLiberi() >= postiRichiesti;
    }

    // Verifica se la proiezione è futura
    public boolean isDisponibile() {
        return calcolaPostiLiberi() > 0 && dataOra.isAfter(LocalDateTime.now());
    }

    // Stampa a schermo i dettagli della proiezione
    public void visualizzaProiezione() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        System.out.println("--- DETTAGLIO PROIEZIONE ---");
        System.out.println("Film: " + film.getTitolo() + " (" + film.getGenere() + ", " + film.getDurata() + " min)");
        System.out.println("Data e Ora: " + dataOra.format(formatter));
        System.out.println("Costo Biglietto: €" + String.format("%.2f", prezzoBiglietto));
        System.out.println("Posti Liberi: " + calcolaPostiLiberi() + " / " + CAPIENZA_MASSIMA);
        System.out.println("----------------------------");
    }

    // Permette di ordinare le proiezioni cronologicamente
    @Override
    public int compareTo(Proiezione altra) {
        return this.dataOra.compareTo(altra.getDataOra());
    }

    // Metodi Getter e Setter
    public Film getFilm() { return film; }
    public void setFilm(Film film) { this.film = film; }

    public LocalDateTime getDataOra() { return dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }

    public double getPrezzoBiglietto() { return prezzoBiglietto; }
    public void setPrezzoBiglietto(double prezzoBiglietto) { this.prezzoBiglietto = prezzoBiglietto; }
}
