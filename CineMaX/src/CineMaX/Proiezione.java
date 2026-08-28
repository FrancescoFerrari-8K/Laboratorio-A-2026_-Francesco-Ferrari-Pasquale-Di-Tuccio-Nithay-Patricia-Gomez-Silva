package CineMaX;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

// Classe che rappresenta una singola proiezione del cinema (monosala da 200 posti)
public class Proiezione implements Comparable<Proiezione> {

    private Film film;
    private LocalDateTime dataOra;
    private double prezzoBiglietto;
    private int numeroPosti;

    // Capienza massima del cinema monosala
    public static final int CAPIENZA_MASSIMA = 200;
    private static final String FILE_PROIEZIONI = "..\\data\\Proiezioni.csv";

    // Costruttore
    public Proiezione(Film film, LocalDateTime dataOra, double prezzoBiglietto, int numeroPosti) {
        this.film = film;
        this.dataOra = dataOra;
        this.prezzoBiglietto = prezzoBiglietto;
        this.numeroPosti = numeroPosti;
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

    // METODO PER CERCARE I POSTI LIBERI TRAMITE TITOLO DEL FILM E DATA
    public static int getPostiLiberiPerFilmEData(String titoloFilm, LocalDateTime dataCercata,
            ArrayList<Proiezione> palinsesto) {
        if (titoloFilm == null || dataCercata == null || palinsesto == null) {
            return -1;
        }

        for (Proiezione p : palinsesto) {
            if (p.getFilm().getTitolo().equalsIgnoreCase(titoloFilm) &&
                    p.getDataOra().equals(dataCercata)) {

                return p.calcolaPostiLiberi();
            }
        }

        System.out.println("Nessuna proiezione trovata per il film " + titoloFilm + " in data: " + dataCercata);
        return -1;
    }

    // SALVA IL PALINSESTO SU FILE CSV (Formatta la data con lo spazio per evitare la 'T')
    public static void salvaProiezioni(ArrayList<Proiezione> palinsesto) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PROIEZIONI))) {
            bw.write("DataOra,numeroPosti,Titolo,Genere,Regista,Anno,Durata,Eta,Prezzo");
            bw.newLine();

            // Formatter esplicito per salvare senza la 'T'
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (Proiezione p : palinsesto) {
                String riga = p.getDataOra().format(formatter) + "," +
                        p.getNumeroPosti() + "," +
                        p.getFilm().getTitolo() + "," +
                        p.getFilm().getGenere() + "," +
                        p.getFilm().getRegista() + "," +
                        p.getFilm().getAnno() + "," +
                        p.getFilm().getDurata() + "," +
                        p.getFilm().getEtà() + "," +
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

                if (riga.trim().isEmpty())
                    continue;

                String[] dati = Proiezione.splitCSV(riga);
                
                if (dati.length == 9) {

                    String testoData = dati[0].replace("\"", "").trim();
                    
                    // GESTIONE AUTOMATICA DELLA 'T': sostituisce la T con uno spazio
                    testoData = testoData.replace("T", " ");
                    
                    // Se mancano i secondi (es. 2018-01-01 10:30), aggiunge ":00"
                    if (testoData.length() == 16) {
                        testoData += ":00";
                    }

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime dataeora = LocalDateTime.parse(testoData, formatter);
                    
                    int numeroPosti = Integer.parseInt(dati[1].trim());
                    String titolo = dati[2].trim();
                    String genere = dati[3].trim();
                    String regista = dati[4].trim();
                    int anno = Integer.parseInt(dati[5].trim());
                    int durata = Integer.parseInt(dati[6].trim());
                    int eta = Integer.parseInt(dati[7].trim());
                    double prezzo = Double.parseDouble(dati[8].trim());

                    Film film = new Film(titolo, genere, regista, anno, durata, eta);
                    Proiezione p = new Proiezione(film, dataeora, prezzo, numeroPosti);
                    palinsesto.add(p);
                }
            }
        } catch (IOException e) {
            System.out.println("Nessun file proiezioni esistente o errore di lettura. Inizio con palinsesto vuoto.");
        }

        return palinsesto;
    }

    public static String[] splitCSV(String riga) {
        return riga.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
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

    public int getNumeroPosti() { return numeroPosti; }
    public void setNumeroPosti(int numeroPosti) { this.numeroPosti = numeroPosti; }
}
    
 
               

    
