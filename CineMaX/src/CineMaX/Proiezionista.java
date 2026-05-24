package CineMaX;
import java.util.ArrayList; // Importiamo l'ArrayList che serve per fare la lista dinamica dei film

public class Proiezionista {
    // Caratteristiche del proiezionista
    private String nome;
    private String idProiezionista;
    // Creiamo una lista dinamica (ArrayList) che conterrà tutte le proiezioni create
    private ArrayList<Proiezione> listaProiezioni; 

    // Costruttore per creare il proiezionista
    public Proiezionista(String nome, String idProiezionista) {
        this.nome = nome;
        this.idProiezionista = idProiezionista;
        this.listaProiezioni = new ArrayList<>(); 
    }

    // Metodo per aggiungere un nuovo film/proiezione alla lista
    public void aggiungiProiezione(Proiezione p) {
        listaProiezioni.add(p); // Aggiunge l'oggetto proiezione dentro l'ArrayList
        System.out.println("Proiezione di \"" + p.getTitoloFilm() + "\" inserita nel sistema.");
    }

    // Metodo per cancellare un film cercando il suo titolo
    public boolean rimuoviProiezione(String titoloFilm) 
        for (int i = 0; i < listaProiezioni.size(); i++) {
            if (listaProiezioni.get(i).getTitoloFilm().equalsIgnoreCase(titoloFilm)) {
                listaProiezioni.remove(i); // Lo cancelliamo dalla lista tramite la sua posizione (i)
                System.out.println("Il film \"" + titoloFilm + "\" è stato rimosso dal palinsesto.");
                return true; 
            }
        }
        // Se il ciclo finisce e non ha trovato nulla, avvisa l'utente
        System.out.println("Nessun film trovato con il nome: " + titoloFilm);
        return false; 
    }

    // Metodo che stampa l'elenco di tutti i film attualmente caricati
    public void visualizzaPalinsesto() {
        System.out.println("\n--- PROGRAMMAZIONE CINEMAX ---");
        if (listaProiezioni.isEmpty()) {
            System.out.println("Al momento non ci sono proiezioni programmate.");
        } else {
            for (Proiezione p : listaProiezioni) {
                p.stampaDettagli(); // Sfrutta il metodo di stampa che abbiamo scritto nella classe Proiezione
            }
        }
        System.out.println("----------------------------------");
    }

    // Questo serve a dare la lista dei film al Main (così anche i Clienti possono vederla per prenotare)
    public ArrayList<Proiezione> getListaProiezioni() {
        return listaProiezioni;
    }

    // Classici metodi get per il nome e l'ID del dipendente
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getIdProiezionista() { return idProiezionista; }
    public void setIdProiezionista(String idProiezionista) { this.idProiezionista = idProiezionista; }
}
