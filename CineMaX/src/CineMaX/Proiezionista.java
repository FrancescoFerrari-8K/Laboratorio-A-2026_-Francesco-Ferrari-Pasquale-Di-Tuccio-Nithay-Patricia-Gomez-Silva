package CineMaX;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe che rappresenta la figura del Proiezionista.
 * Estende Guest e consente la gestione del palinsesto cinematografico,
 * inclusi inserimento, rimozione e modifica delle proiezioni.
 * 
 * @author Pasquale Di Tuccio
 */
public class Proiezionista extends Guest {

    private String nome;
    private String idProiezionista;

    /**
     * Costruttore vuoto della classe Proiezionista.
     */
    public Proiezionista() {}

    /**
     * Costruttore con parametri per la classe Proiezionista.
     * 
     * @param nome Il nome del proiezionista.
     * @param idProiezionista L'identificatore univoco del proiezionista.
     */
    public Proiezionista(String nome, String idProiezionista) {
        this.nome = nome;
        this.idProiezionista = idProiezionista;
    }

    /**
     * Aggiunge una nuova proiezione al palinsesto.
     * Verifica che l'anno del film sia valido, che la data sia futura
     * e che l'orario non sovrapponga altre proiezioni esistenti.
     * 
     * @param nuovaProiezione La proiezione da aggiungere.
     * @param palinsesto La lista attuale delle proiezioni.
     * @return true se l'inserimento ha successo, false altrimenti.
     */
    public boolean aggiungiProiezioneAlPalinsesto(Proiezione nuovaProiezione, ArrayList<Proiezione> palinsesto) {
        
        if (nuovaProiezione == null || palinsesto == null) {
            System.out.println("ERRORE: Proiezione o palinsesto non validi.");
            return false;
        }

        // Controllo 1: L'anno del film non deve essere nel futuro
        int annoCorrente = Year.now().getValue();
        if (nuovaProiezione.getFilm().getAnno() > annoCorrente) {
            System.out.println("ERRORE: Impossibile aggiungere un film con anno di pubblicazione nel futuro (" 
                               + nuovaProiezione.getFilm().getAnno() + ")!");
            return false;
        }

        // Controllo 2: La data della proiezione deve essere futura
        if (nuovaProiezione.getDataOra().isBefore(LocalDateTime.now())) {
            System.out.println("ERRORE: Impossibile inserire una proiezione nel passato! Data inserita: " + nuovaProiezione.getDataOra());
            return false;
        }

        // Controllo 3: Verifica se l'orario si sovrappone a qualche altro film in sala
        for (Proiezione p : palinsesto) {
            LocalDateTime inizioEsistente = p.getDataOra();
            LocalDateTime fineEsistente = inizioEsistente.plusMinutes(p.getFilm().getDurata());
            
            LocalDateTime inizioNuova = nuovaProiezione.getDataOra();
            LocalDateTime fineNuova = inizioNuova.plusMinutes(nuovaProiezione.getFilm().getDurata());
            
            // Controlla se le due proiezioni si incrociano
            if (inizioNuova.isBefore(fineEsistente) && fineNuova.isAfter(inizioEsistente)) {
                System.out.println("ERRORE: Il cinema e' gia' occupato in quell'orario dal film: " + p.getFilm().getTitolo());
                return false; 
            }
        }

        // Aggiunge la proiezione, riordina la lista e salva sul file CSV
        palinsesto.add(nuovaProiezione);
        Collections.sort(palinsesto);

        Proiezione.salvaProiezioni(palinsesto);

        System.out.println("Proiezione di \"" + nuovaProiezione.getFilm().getTitolo() + "\" inserita con successo.");
        return true;
    }

    /**
     * Rimuove una proiezione dal palinsesto in base a titolo e data/ora.
     * Controlla preventivamente che non vi siano prenotazioni attive.
     * 
     * @param titoloFilm Titolo del film da rimuovere.
     * @param dataOraProiezione Data e ora della proiezione da cancellare.
     * @param palinsesto Lista delle proiezioni in memoria.
     * @param listaPrenotazioni Lista delle prenotazioni attive nel sistema.
     * @return true se la proiezione viene rimossa, false altrimenti.
     */
    public boolean rimuoviProiezioneDalPalinsesto(String titoloFilm, LocalDateTime dataOraProiezione, ArrayList<Proiezione> palinsesto, ArrayList<Prenotazione> listaPrenotazioni) {

        if (titoloFilm == null || dataOraProiezione == null || palinsesto == null || listaPrenotazioni == null) {
            System.out.println("ERRORE: Parametri non validi per la rimozione.");
            return false;
        }

        for (int i = 0; i < palinsesto.size(); i++) {
            Proiezione p = palinsesto.get(i);
            
            // Confronta sia il titolo che la data/ora esatta
            if (p.getFilm().getTitolo().equalsIgnoreCase(titoloFilm) && p.getDataOra().equals(dataOraProiezione)) {
                
                // Cerca se ci sono prenotazioni per questa proiezione
                for (Prenotazione pr : listaPrenotazioni) {
                    if (pr.getProiezione_Titolo().equalsIgnoreCase(p.getFilm().getTitolo()) &&
                        pr.getProiezione_Data().equals(p.getDataOra())) {
                        System.out.println("ERRORE: Impossibile rimuovere la proiezione di \"" + titoloFilm + "\" del " + dataOraProiezione + ". Ci sono prenotazioni attive!");
                        return false; 
                    }
                }
                
                // Rimuove la proiezione e aggiorna il file
                palinsesto.remove(i);
                Proiezione.salvaProiezioni(palinsesto);

                System.out.println("La proiezione di \"" + titoloFilm + "\" del " + dataOraProiezione + " e' stata rimossa con successo.");
                return true;
            }
        }

        System.out.println("Nessuna proiezione trovata per il film \"" + titoloFilm + "\" nella data/ora: " + dataOraProiezione);
        return false;
    }

    /**
     * Modifica la data e l'orario di una proiezione esistente.
     * Verifica che la nuova data sia futura e non generi sovrapposizioni.
     * 
     * @param titoloFilm Titolo del film da spostare.
     * @param dataOraAttuale Data e ora attuali della proiezione.
     * @param nuovaDataOra Nuova data e ora da impostare.
     * @param palinsesto Lista delle proiezioni in memoria.
     * @return true se la modifica va a buon fine, false altrimenti.
     */
    public boolean modificaDataOraProiezione(String titoloFilm, LocalDateTime dataOraAttuale, LocalDateTime nuovaDataOra, ArrayList<Proiezione> palinsesto) {

        if (titoloFilm == null || dataOraAttuale == null || nuovaDataOra == null || palinsesto == null) {
            System.out.println("ERRORE: Parametri non validi per la modifica.");
            return false;
        }

        // Controllo che la nuova data non sia nel passato
        if (nuovaDataOra.isBefore(LocalDateTime.now())) {
            System.out.println("ERRORE: Impossibile spostare una proiezione nel passato! Nuova data inserita: " + nuovaDataOra);
            return false;
        }

        // Cerca la proiezione giusta nella lista
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

        // Controllo se il nuovo orario si sovrappone con un altro film
        int durataNuova = proiezioneDaModificare.getFilm().getDurata();
        LocalDateTime fineNuova = nuovaDataOra.plusMinutes(durataNuova);

        for (Proiezione p : palinsesto) {
            // Salta il controllo con se stessa
            if (p == proiezioneDaModificare) {
                continue;
            }

            LocalDateTime inizioEsistente = p.getDataOra();
            LocalDateTime fineEsistente = inizioEsistente.plusMinutes(p.getFilm().getDurata());

            if (nuovaDataOra.isBefore(fineEsistente) && fineNuova.isAfter(inizioEsistente)) {
                System.out.println("ERRORE: Impossibile spostare la proiezione. Il cinema e' gia' occupato dal film: " + p.getFilm().getTitolo());
                return false;
            }
        }

        // Aggiorna l'orario, riordina e salva sul CSV
        proiezioneDaModificare.setDataOra(nuovaDataOra);
        Collections.sort(palinsesto);

        Proiezione.salvaProiezioni(palinsesto);

        System.out.println("Data e ora della proiezione di \"" + titoloFilm + "\" aggiornate con successo a: " + nuovaDataOra);
        return true;
    }

    /**
     * Restituisce il nome del proiezionista.
     * @return Il nome.
     */
    public String getNome() { 
        return nome; 
    }

    /**
     * Imposta il nome del proiezionista.
     * @param nome Il nome da impostare.
     */
    public void setNome(String nome) { 
        this.nome = nome; 
    }

    /**
     * Restituisce l'ID del proiezionista.
     * @return L'ID univoco.
     */
    public String getIdProiezionista() { 
        return idProiezionista; 
    }

    /**
     * Imposta l'ID del proiezionista.
     * @param idProiezionista L'ID da impostare.
     */
    public void setIdProiezionista(String idProiezionista) { 
        this.idProiezionista = idProiezionista; 
    }
}