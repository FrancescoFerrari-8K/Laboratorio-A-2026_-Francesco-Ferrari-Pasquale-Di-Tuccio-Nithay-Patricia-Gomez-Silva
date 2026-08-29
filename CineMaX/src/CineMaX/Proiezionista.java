package CineMaX;

import java.time.LocalDateTime;
import java.time.Year;
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

    // 1. AGGIUNGE UNA PROIEZIONE
    public boolean aggiungiProiezioneAlPalinsesto(Proiezione nuovaProiezione, ArrayList<Proiezione> palinsesto) {
        
        if (nuovaProiezione == null || palinsesto == null) {
            System.out.println("ERRORE: Proiezione o palinsesto non validi.");
            return false;
        }

        // CONTROLLO 1: L'anno di pubblicazione del film non può essere nel futuro
        int annoCorrente = Year.now().getValue();
        if (nuovaProiezione.getFilm().getAnno() > annoCorrente) {
            System.out.println("ERRORE: Impossibile aggiungere un film con anno di pubblicazione nel futuro (" 
                               + nuovaProiezione.getFilm().getAnno() + ")!");
            return false;
        }

        // CONTROLLO 2: La data e ora della proiezione deve essere nel futuro
        if (nuovaProiezione.getDataOra().isBefore(LocalDateTime.now())) {
            System.out.println("ERRORE: Impossibile inserire una proiezione nel passato! Data inserita: " + nuovaProiezione.getDataOra());
            return false;
        }

        // CONTROLLO 3: Sovrapposizione di orario con altre proiezioni (in base alla durata del film)
        for (Proiezione p : palinsesto) {
            LocalDateTime inizioEsistente = p.getDataOra();
            LocalDateTime fineEsistente = inizioEsistente.plusMinutes(p.getFilm().getDurata());
            
            LocalDateTime inizioNuova = nuovaProiezione.getDataOra();
            LocalDateTime fineNuova = inizioNuova.plusMinutes(nuovaProiezione.getFilm().getDurata());
            
            // Verifica se gli intervalli orari si sovrappongono
            if (inizioNuova.isBefore(fineEsistente) && fineNuova.isAfter(inizioEsistente)) {
                System.out.println("ERRORE: Il cinema è già occupato in quell'orario dal film: " + p.getFilm().getTitolo());
                return false; 
            }
        }

        // Se tutti i controlli passano: inserimento, ordinamento e salvataggio su CSV
        palinsesto.add(nuovaProiezione);
        Collections.sort(palinsesto);

        Proiezione.salvaProiezioni(palinsesto);

        System.out.println("Proiezione di \"" + nuovaProiezione.getFilm().getTitolo() + "\" inserita con successo.");
        return true;
    }

    // 2. RIMUOVE UNA SPECIFICA PROIEZIONE (Titolo + Data/Ora)
    public boolean rimuoviProiezioneDalPalinsesto(String titoloFilm, LocalDateTime dataOraProiezione, ArrayList<Proiezione> palinsesto, ArrayList<Prenotazione> listaPrenotazioni) {

        if (titoloFilm == null || dataOraProiezione == null || palinsesto == null || listaPrenotazioni == null) {
            System.out.println("ERRORE: Parametri non validi per la rimozione.");
            return false;
        }

        for (int i = 0; i < palinsesto.size(); i++) {
            Proiezione p = palinsesto.get(i);
            
            // Controllo doppio: Titolo del Film + Data/Ora esatta
            if (p.getFilm().getTitolo().equalsIgnoreCase(titoloFilm) && p.getDataOra().equals(dataOraProiezione)) {
                
                // Controlla se esistono prenotazioni per questa specifica proiezione
                for (Prenotazione pr : listaPrenotazioni) {
                    if (pr.getProiezione_Titolo().equalsIgnoreCase(p.getFilm().getTitolo()) &&
                        pr.getProiezione_Data().equals(p.getDataOra())) {
                        System.out.println("ERRORE: Impossibile rimuovere la proiezione di \"" + titoloFilm + "\" del " + dataOraProiezione + ". Ci sono prenotazioni attive!");
                        return false; 
                    }
                }
                
                // Rimuove la proiezione trovata e aggiorna il CSV
                palinsesto.remove(i);
                Proiezione.salvaProiezioni(palinsesto);

                System.out.println("La proiezione di \"" + titoloFilm + "\" del " + dataOraProiezione + " è stata rimossa con successo.");
                return true;
            }
        }

        System.out.println("Nessuna proiezione trovata per il film \"" + titoloFilm + "\" nella data/ora: " + dataOraProiezione);
        return false;
    }

    // 3. MODIFICA DATA E ORA DI UNA PROIEZIONE
    public boolean modificaDataOraProiezione(String titoloFilm, LocalDateTime dataOraAttuale, LocalDateTime nuovaDataOra, ArrayList<Proiezione> palinsesto) {

        if (titoloFilm == null || dataOraAttuale == null || nuovaDataOra == null || palinsesto == null) {
            System.out.println("ERRORE: Parametri non validi per la modifica.");
            return false;
        }

        // CONTROLLO: La nuova data/ora non può essere nel passato
        if (nuovaDataOra.isBefore(LocalDateTime.now())) {
            System.out.println("ERRORE: Impossibile spostare una proiezione nel passato! Nuova data inserita: " + nuovaDataOra);
            return false;
        }

        // Cerca la proiezione da modificare
        Proiezione proiezioneDaModificare = null;
        for (Proiezione p : palinsesto) {
            if (p.getFilm().getTitolo().equalsIgnoreCase(titoloFilm) && p.getDataOra().equals(dataOraAttuale)) {
                proiezioneDaModificare = p;
                break;
            }
        }

        if (proiezioneDaModificare == null) {
            System.out.println("ERRORE: Nessuna proiezione trovata per il film \"" + titoloFilm + "\" in data: " + dataOraAttuale);
            return false;
        }

        // Controllo sovrapposizioni con il nuovo orario
        int durataNuova = proiezioneDaModificare.getFilm().getDurata();
        LocalDateTime fineNuova = nuovaDataOra.plusMinutes(durataNuova);

        for (Proiezione p : palinsesto) {
            if (p == proiezioneDaModificare) {
                continue;
            }

            LocalDateTime inizioEsistente = p.getDataOra();
            LocalDateTime fineEsistente = inizioEsistente.plusMinutes(p.getFilm().getDurata());

            if (nuovaDataOra.isBefore(fineEsistente) && fineNuova.isAfter(inizioEsistente)) {
                System.out.println("ERRORE: Impossibile spostare la proiezione. Il cinema è già occupato dal film: " + p.getFilm().getTitolo());
                return false;
            }
        }

        // Applica la modifica, ri-ordina e salva
        proiezioneDaModificare.setDataOra(nuovaDataOra);
        Collections.sort(palinsesto);

        Proiezione.salvaProiezioni(palinsesto);

        System.out.println("Data e ora della proiezione di \"" + titoloFilm + "\" aggiornate con successo a: " + nuovaDataOra);
        return true;
    }

    // Metodi Getter e Setter
    public String getNome() { 
        return nome; 
    }
    
    public void setNome(String nome) { 
        this.nome = nome; 
    }

    public String getIdProiezionista() { 
        return idProiezionista; 
    }
    
    public void setIdProiezionista(String idProiezionista) { 
        this.idProiezionista = idProiezionista; 
    }
}