package CineMaX;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe che rappresenta il Proiezionista.
 * Estende Guest e serve per gestire il palinsesto del cinema
 * (aggiungere, rimuovere e modificare le proiezioni).
 * 
 * @author Francesco Ferrari, Pasquale Di Tuccio, Nithay Patricia Gomez Silva
 */
public class Proiezionista extends Guest {

    // Nome del proiezionista
    private String nome;

    // ID del proiezionista
    private String idProiezionista;

    /**
     * Costruttore vuoto.
     */
    public Proiezionista() {}

    /**
     * Costruttore con parametri per creare il proiezionista.
     * 
     * @param nome Il nome del proiezionista.
     * @param idProiezionista L'ID del proiezionista.
     */
    public Proiezionista(String nome, String idProiezionista) {
        this.nome = nome;
        this.idProiezionista = idProiezionista;
    }

    /**
     * Aggiunge una proiezione alla lista del palinsesto.
     * Controlla che l'anno del film non sia nel futuro, che la data della proiezione
     * non sia nel passato e che l'orario non si sovrapponga a un'altra proiezione.
     *
     * @param nuovaProiezione La nuova proiezione da inserire.
     * @param palinsesto La lista di tutte le proiezioni.
     * @return true se viene aggiunta con successo, false se c'e' un errore.
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
     * Rimuove una proiezione dal palinsesto usando titolo e data/ora.
     * Controlla prima che non ci siano prenotazioni gia' fatte dai clienti per quel film.
     *
     * @param titoloFilm Il titolo del film da cancellare.
     * @param dataOraProiezione La data e l'ora della proiezione da cancellare.
     * @param palinsesto La lista delle proiezioni.
     * @param listaPrenotazioni La lista delle prenotazioni attive.
     * @return true se la rimuove, false se non la trova o se ci sono prenotazioni.
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
     * Modifica l'orario di una proiezione esistente.
     * Controlla che il nuovo orario sia futuro e che la sala sia libera.
     *
     * @param titoloFilm Il titolo del film da spostare.
     * @param dataOraAttuale La data e l'ora attuali del film.
     * @param nuovaDataOra La nuova data e l'ora che si vuole impostare.
     * @param palinsesto La lista delle proiezioni.
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
     * @return L'ID.
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