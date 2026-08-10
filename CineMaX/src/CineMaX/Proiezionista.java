package CineMaX;

import java.time.LocalDateTime;
import java.util.ArrayList;

// Classe che rappresenta il Proiezionista e le sue azioni sul palinsesto
public class Proiezionista {

    // Costruttore del proiezionista
    public Proiezionista() {
      
    }

    // Aggiunge un film, controlla che l'orario sia libero e ordina il palinsesto
    public boolean aggiungiProiezioneAlPalinsesto(Proiezione nuovaProiezione, ArrayList<Proiezione> palinsesto) {
        
        // 1. CONTROLLO SOVRAPPOSIZIONI DI ORARIO
        // Controlliamo ogni film già presente in palinsesto
        for (Proiezione p : palinsesto) {
            // Calcoliamo quando inizia e quando finisce il film già presente
            LocalDateTime inizioEsistente = p.getDataOra();
            LocalDateTime fineEsistente = inizioEsistente.plusMinutes(p.getFilm().getDurata());
            
            // Calcoliamo quando inizia e quando finisce il nuovo film
            LocalDateTime inizioNuova = nuovaProiezione.getDataOra();
            LocalDateTime fineNuova = inizioNuova.plusMinutes(nuovaProiezione.getFilm().getDurata());
            
            // Se gli orari si incrociano, blocchiamo l'inserimento
            if (inizioNuova.isBefore(fineEsistente) && fineNuova.isAfter(inizioEsistente)) {
                System.out.println("ERRORE: Il cinema è già occupato in quell'orario dal film: " + p.getFilm().getTitolo());
                return false; 
            }
        }

        // Se l'orario è libero, aggiungiamo il film alla lista
        palinsesto.add(nuovaProiezione);
        System.out.println("Proiezione di \"" + nuovaProiezione.getFilm().getTitolo() + "\" inserita con successo.");

        // 2. ORDINAMENTO CRONOLOGICO AUTOMATICO (Bubble Sort)
        // Mette in ordine i film dal più vicino al più lontano usando due cicli for
        int n = palinsesto.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Se il film attuale viene dopo quello successivo, li scambia di posto
                if (palinsesto.get(j).getDataOra().isAfter(palinsesto.get(j + 1).getDataOra())) {
                    Proiezione temp = palinsesto.get(j);
                    palinsesto.set(j, palinsesto.get(j + 1));
                    palinsesto.set(j + 1, temp);
                }
            }
        }
        return true;
    }

    // Rimuove un film dal palinsesto cercando per titolo, ma solo se non ci sono prenotazioni
    public boolean rimuoviProiezioneDalPalinsesto(String titoloFilm, ArrayList<Proiezione> palinsesto, ArrayList<Prenotazione> listaPrenotazioni) {

        for (int i = 0; i < palinsesto.size(); i++) {
            Proiezione p = palinsesto.get(i);
            
            // Se troviamo il titolo corrispondente (ignorando maiuscole/minuscole)
            if (p.getFilm().getTitolo().equalsIgnoreCase(titoloFilm)) {
                
                // CONTROLLO DI SICUREZZA: Cerchiamo se ci sono già biglietti venduti
                for (Prenotazione pr : listaPrenotazioni) {
                    // Usiamo getProiezioni() per collegarci al codice dei compagni
                    if (pr.getProiezioni().equals(p)) {
                        System.out.println("ERRORE: Impossibile rimuovere \"" + titoloFilm + "\". Ci sono prenotazioni attive!");
                        return false; 
                    }
                }
                
                // Se non ci sono prenotazioni, cancelliamo il film dal palinsesto
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
