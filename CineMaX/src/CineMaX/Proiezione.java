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
    private static final String FILE_PROIEZIONI = "..\\data\\proiezioni.csv";

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

    // Stampa a schermo i dettagli completi della proiezione
    public void visualizzaProiezioneDettagliata() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        System.out.println("--- DETTAGLIO PROIEZIONE ---");
        film.visualizzaFilm(); // Stampa completa del film
        System.out.println("Data e Ora: " + dataOra.format(formatter));
        System.out.println("Costo Biglietto: €" + String.format("%.2f", prezzoBiglietto));
        System.out.println("Posti Liberi: " + calcolaPostiLiberi() + " / " + CAPIENZA_MASSIMA);
        System.out.println("----------------------------");
    }
    
    // METODO PER CERCARE I POSTI LIBERI TRAMITE TITOLO DEL FILM E DATA (LocalDate)
    public static int getPostiLiberiPerFilmEData(String titoloFilm, LocalDate dataCercata, ArrayList<Proiezione> palinsesto) {
        if (titoloFilm == null || dataCercata == null || palinsesto == null) {
            return -1;
        }

        for (Proiezione p : palinsesto) {
            // Confronta il titolo e controlla se il giorno coincide (.toLocalDate())
            if (p.getFilm().getTitolo().equalsIgnoreCase(titoloFilm) && 
                p.getDataOra().toLocalDate().equals(dataCercata)) {
                
                return p.calcolaPostiLiberi();
            }
        }

        System.out.println("Nessuna proiezione trovata per il film \"" + titoloFilm + "\" in data: " + dataCercata);
        return -1; 
    }

    // SALVA IL PALINSESTO SU FILE CSV (Sincronizzato a 8 colonne per rispecchiare i dettagli del Film)
    public static void salvaProiezioni(ArrayList<Proiezione> palinsesto) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PROIEZIONI))) {
            bw.write("DataOra;Titolo;Genere;Regista;Anno;Durata;Eta;Prezzo");
            bw.newLine();

            for (Proiezione p : palinsesto) {
                String riga = p.getDataOra().toString() + ";" +
                             p.getFilm().getTitolo() + ";" +
                             p.getFilm().getGenere() + ";" +
                             p.getFilm().getRegista() + ";" +
                             p.getFilm().getAnno() + ";" +
                             p.getFilm().getDurata() + ";" +
                             p.getFilm().getEtà() + ";" +
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
                // Verifica che ci siano esattamente le 8 colonne salvate
                if (dati.length == 8) {
                    LocalDateTime dataeora = LocalDateTime.parse(dati[0]);
                    String titolo = dati[1];
                    String genere = dati[2];
                    String regista = dati[3];
                    int anno = Integer.parseInt(dati[4]);
                    int durata = Integer.parseInt(dati[5]);
                    int eta = Integer.parseInt(dati[6]);
                    double prezzo = Double.parseDouble(dati[7]);

                    Film film = new Film(titolo, genere, regista, anno, durata, eta);
                    Proiezione p = new Proiezione(film, dataeora, prezzo);
                    
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
