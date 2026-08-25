package CineMaX;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

// Classe che rappresenta il Proiezionista e le sue azioni sul palinsesto
public class Proiezionista extends Guest {

    private String nome;
    private String idProiezionista;

    // Costruttori
    public Proiezionista() {}

    public Proiezionista(String nome, String idProiezionista) {
        this.nome = nome;
        this.idProiezionista = idProiezionista;
    }

    // Aggiunge un film, controlla sovrapposizioni orarie e ordina il palinsesto
    public boolean aggiungiProiezioneAlPalinsesto(Proiezione nuovaProiezione, ArrayList<Proiezione> palinsesto) {
        
        if (nuovaProiezione == null || palinsesto == null) {
            System.out.println("ERRORE: Proiezione o palinsesto non validi.");
            return false;
        }

        // 1. CONTROLLO SOVRAPPOSIZIONI DI ORARIO
        for (Proiezione p : palinsesto) {
            LocalDateTime inizioEsistente = p.getDataOra();
            LocalDateTime fineEsistente = inizioEsistente.plusMinutes(p.getFilm().getDurata());
            
            LocalDateTime inizioNuova = nuovaProiezione.getDataOra();
            LocalDateTime fineNuova = inizioNuova.plusMinutes(nuovaProiezione.getFilm().getDurata());
            
            // Verifica sovrapposizione temporale
            if (inizioNuova.isBefore(fineEsistente) && fineNuova.isAfter(inizioEsistente)) {
                System.out.println("ERRORE: Il cinema è già occupato in quell'orario dal film: " + p.getFilm().getTitolo());
                return false; 
            }
        }

        // 2. INSERIMENTO E ORDINAMENTO AUTOMATICO
        palinsesto.add(nuovaProiezione);
        Collections.sort(palinsesto); // Utilizza il compareTo di Proiezione
        System.out.println("Proiezione di \"" + nuovaProiezione.getFilm().getTitolo() + "\" inserita con successo.");

        return true;
    }

    // Rimuove una proiezione dal palinsesto cercando per titolo (solo se priva di prenotazioni)
    public boolean rimuoviProiezioneDalPalinsesto(String titoloFilm, ArrayList<Proiezione> palinsesto, ArrayList<Prenotazione> listaPrenotazioni) {

        if (titoloFilm == null || palinsesto == null || listaPrenotazioni == null) {
            System.out.println("ERRORE: Parametri non validi per la rimozione.");
            return false;
        }

        for (int i = 0; i < palinsesto.size(); i++) {
            Proiezione p = palinsesto.get(i);
            
            if (p.getFilm().getTitolo().equalsIgnoreCase(titoloFilm)) {
                
                // Controlla se esistono prenotazioni per questa proiezione
                for (Prenotazione pr : listaPrenotazioni) {
                    if (pr.getProiezione_Titolo().equalsIgnoreCase(p.getFilm().getTitolo()) &&
                        pr.getProiezione_Data().equals(p.getDataOra())) {
                        System.out.println("ERRORE: Impossibile rimuovere \"" + titoloFilm + "\". Ci sono prenotazioni attive!");
                        return false; 
                    }
                }
                
                palinsesto.remove(i);
                System.out.println("Il film \"" + titoloFilm + "\" è stato rimosso dal palinsesto.");
                return true;
            }
        }
        System.out.println("Nessun film trovato in palinsesto con il nome: " + titoloFilm);
        return false;
    }

    // Metodi Getter e Setter
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getIdProiezionista() { return idProiezionista; }
    public void setIdProiezionista(String idProiezionista) { this.idProiezionista = idProiezionista; }
}
