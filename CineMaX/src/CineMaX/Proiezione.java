package CineMaX;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
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
    private static final String FILE_PROIEZIONI = "proiezioni.csv";

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

    // Getter compatibile per la ricerca prenotazioni
    public int getPostiPrenotati() {
        return calcolaPostiOccupati();
    }

    // Controlla se ci sono abbastanza posti disponibili
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

    // METODO PER CERCARE I POSTI LIBERI TRAMITE TITOLO DEL FILM E DATA (LocalDate)
    // Restituisce i posti liberi della proiezione trovata, oppure -1 se non esiste alcuna proiezione per quel giorno/film
    public static int getPostiLiberiPerFilmEData(String titoloFilm, LocalDate dataCercata, ArrayList<Proiezione> palinsesto) {
        if (titoloFilm == null || dataCercata == null || palinsesto == null) {
            return -1;
        }

        for (Proiezione p : palinsesto) {
            // Confronta il titolo (ignorando maiuscole/minuscole) e controlla se il giorno coincide (.toLocalDate())
            if (p.getFilm().getTitolo().equalsIgnoreCase(titoloFilm) && 
                p.getDataOra().toLocalDate().equals(dataCercata)) {
                
                return p.calcolaPostiLiberi();
            }
        }

        System.out.println("Nessuna proiezione trovata per il film \"" + titoloFilm + "\" in data: " + dataCercata);
        return -1; // Proiezione non trovata
    }

    // SALVA IL PALINSESTO SU FILE CSV
    public static void salvaProiezioni(ArrayList<Proiezione> palinsesto) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PROIEZIONI))) {
            bw.write("Titolo;Genere;Durata;DataOra;Prezzo");
            bw.newLine();

            for (Proiezione p : palinsesto) {
                String riga = p.getFilm().getTitolo() + ";" +
                             p.getFilm().getGenere() + ";" +
                             p.getFilm().getDurata() + ";" +
                             p.getDataOra().toString() + ";" +
                             p.getPrezzoBiglietto();
                bw.write(riga);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio delle proiezioni: " + e.getMessage());
        }
    }

    // CARICA IL PALINSESTO DA FILE CSV ALL'AVVIO
    public static ArrayList<Proiezione> caricaProiezioni() {
        ArrayList<Proiezione> palinsesto = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PROIEZIONI))) {
            String riga;
            boolean primaRiga = true;

            while ((riga = br.readLine()) != null) {
                if (primaRiga) {
                    primaRiga = false;
                    continue;
                }
                
                if (riga.trim().isEmpty()) continue;

                String[] dati = riga.split(";");
                if (dati.length == 5) {
                    String titolo = dati[0];
                    String genere = dati[1];
                    int durata = Integer.parseInt(dati[2]);
                    LocalDateTime dataOra = LocalDateTime.parse(dati[3]);
                    double prezzo = Double.parseDouble(dati[4]);

                    Film film = new Film(titolo, genere, durata);
                    Proiezione p = new Proiezione(film, dataOra, prezzo);
                    
                    palinsesto.add(p);
                }
            }
        } catch (IOException e) {
            System.out.println("Nessun file proiezioni esistente o errore di lettura. Inizio con palinsesto vuoto.");
        }

        return palinsesto;
    }

    // Ordina le proiezioni cronologicamente
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

