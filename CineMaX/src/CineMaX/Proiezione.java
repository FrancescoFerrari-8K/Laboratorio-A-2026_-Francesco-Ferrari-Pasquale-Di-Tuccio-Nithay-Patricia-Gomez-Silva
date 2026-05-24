package CineMax;
public class Proiezione {
    // Qui elenchiamo le caratteristiche che deve avere ogni singola proiezione
    private String titoloFilm;     
    private String orario;       
    private int numeroSala;         
    private double prezzoBiglietto; 
    private int postiDisponibili;   

    // Questo è il costruttore che ci serve per creare concretamente una proiezione inserendo i dati iniziali
    public Proiezione(String titoloFilm, String orario, int numeroSala, double prezzoBiglietto, int postiDisponibili) {
        this.titoloFilm = titoloFilm;
        this.orario = orario;
        this.numeroSala = numeroSala;
        this.prezzoBiglietto = prezzoBiglietto;
        this.postiDisponibili = postiDisponibili;
    }

    // Metodo per "ricalcolare" i posti quando qualcuno compra i biglietti
    public boolean prenotaPosti(int numeroPosti) {
        // Controllo di sicurezza: non puoi prenotare 0 o posti negativi
        if (numeroPosti <= 0) {
            System.out.println("Errore: Devi inserire un numero di posti maggiore di zero!");
            return false; 
        }
        
        // Se i posti richiesti ci sono, li sottraggo da quelli totali disponibili
        if (this.postiDisponibili >= numeroPosti) {
            this.postiDisponibili = this.postiDisponibles - numeroPosti;
            return true; // Ritorna true per dire che la prenotazione è andata a buon fine
        } else {
            // Se non ci sono abbastanza sedili liberi, blocco la prenotazione
            System.out.println("Non ci sono abbastanza posti! Ne rimangono solo: " + this.postiDisponibili);
            return false; // Ritorna false perché non è stato possibile fare la prenotazione
        }
    }

    // Questo metodo serve solamente per stampare a schermo i dati del film
    public void stampaDettagli() {
        System.out.println("Film: " + titoloFilm + " | Ora: " + orario + 
                           " | Sala: " + numeroSala + " | Prezzo: €" + prezzoBiglietto + 
                           " | Posti Liberi: " + postiDisponibili);
    }

    // Da qui in poi ci sono i classici metodi get standard per permettere alle altre classi di leggere o modificare le variabili
    public String getTitleFilm() { return titoloFilm; }
    public void setTitoloFilm(String titoloFilm) { this.titoloFilm = titoloFilm; }

    public String getOrario() { return orario; }
    public void setOrario(String orario) { this.orario = orario; }

    public int getNumeroSala() { return numeroSala; }
    public void setNumeroSala(int numeroSala) { this.numeroSala = numeroSala; }

    public double getPrezzoBiglietto() { return prezzoBiglietto; }
    public void setPrezzoBiglietto(double prezzoBiglietto) { this.prezzoBiglietto = prezzoBiglietto; }

    public int getPostiDisponibili() { return postiDisponibili; }
    public void setPostiDisponibili(int postiDisponibili) { this.postiDisponibili = postiDisponibili; }
}

