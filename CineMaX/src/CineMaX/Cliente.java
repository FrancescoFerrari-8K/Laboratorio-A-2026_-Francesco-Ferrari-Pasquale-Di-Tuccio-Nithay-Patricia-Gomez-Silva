package CineMaX;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Cliente extends Guest {

  // parametri //
  private String Nome;

  private String Cognome;

  private String IDutente;

  // costruttore//

  public Cliente(String nome, String cognome, String idutente) {

    this.Nome = nome;
    this.Cognome = cognome;
    this.IDutente = idutente;
  }

  public String getNome() {
    return this.Nome;
  }

  public void setNome(String nome) {
    this.Nome = nome;
  }

  public String getCognome() {
    return this.Cognome;
  }

  public void setCognome(String cognome) {
    this.Cognome = cognome;
  }

  public String getIDUtente() {
    return this.IDutente;
  }

  public void setIDUtente(String IDutente) {
    this.IDutente = IDutente;
  }
  // metodi//
/*/
 *  cerca proiezione e un metodo implementato da guest.
  visualizza proiezione (implementato da guest)
 */
  public static Cliente getClienteDaFile(int IdCliente) {
	  /*/
	   * metodo che messo in input l'ID del cliente posso restituire un objetto del
   tipo cliente
	   */
    return null;
  }

  /*/
   *  questo metodo serve per visualizzare le propie prenotazioni, siano quelle vechie che quelle nuove. ogni prenotazione ti mostra sia il nome, cognome,
   *  
   */
  public void visualizzaLeMiePrenotazioni() {
    // legge il file prenotazione
    // strae le prenotazioni a nome di questo cliente
    // mostrarle
    ArrayList<Prenotazione> l = Prenotazione.caricaPrenotazioni(); // carica la lista delle prenotazione presente nel
                                                                   // sistema
    ArrayList<Prenotazione> prenotazioniCliente = new ArrayList<Prenotazione>(); // se crea una lista vuota che servirà
                                                                                 // da contenitore per salvare solo le
                                                                                 // prenotazioni trovate per quel
                                                                                 // determinato cliente
    System.out.println("");
    System.out.println("Ricerca per: " + this.getNome() + " " + this.getCognome());
    prenotazioniCliente = Prenotazione.TrovaPrenotazioniConNomeECognome(this.getNome(), this.getCognome(),
        l, true); // se salva la prenotazione trovata con nome e cognome da quel cliente
    System.out.println("");
    System.out.println("Prenotazioni Trovate: " + prenotazioniCliente.size());
    System.out.println("");
    for (Prenotazione elemento : prenotazioniCliente) {
      System.out.println(elemento.toString(false));// Scorre la lista delle prenotazioni trovate e stampa i
      // dettagli di ciascuna
    }

    System.out.println("");

  }
/*/
 *   questo metodo serve per creare le prenotazione dal cliente mostrando tutta la informazione compreso il prezzo totale 
 *   delle prenotazioni dal utente 
 */
  public boolean creaPrenotazione(Proiezione proiezioneSelezionata, int posti) {
    Scanner sc = new Scanner(System.in);

    boolean haPostiDisponibili = proiezioneSelezionata.haPostiDisponibili(posti); // se crea un boolean per sapere se ci
                                                                                  // sono posti disponibili

    if (haPostiDisponibili == true) { // se la variabile e true se procede a fare la prenotazione
      String sceltaPrenota;
      System.out
          .println("il prezzo totale delle tue prenotazione è: " + proiezioneSelezionata.getPrezzoBiglietto() * posti);
      System.out.println("");
      System.out.println("sei sicuro/a che vuoi prenotare? (S/N): ");
      sceltaPrenota = sc.nextLine();

      if (sceltaPrenota.toUpperCase().charAt(0) == 'S') { // se verifica che il carattere sia S, in caso in cui siano
                                                          // uguale se procede a fare la nuova prenotazione

        Prenotazione prenotazione = new Prenotazione(this.getIDUtente(), this.getNome(), this.getCognome(),
            proiezioneSelezionata.getDataOra(),
            proiezioneSelezionata.getFilm().getTitolo(), posti, proiezioneSelezionata.getPrezzoBiglietto());

        return Prenotazione.aggiungiPrenotazioneAlCSV(prenotazione);
      } else {
        System.out.println("prenotazione annulata");
        return false;
      }

    } else {
      System.out.println("non si puo efettuare la prenotazione perche non ci sono posti disponibili");
      return false;
    }

  }
/*/
 * questo metodo serve per modificare le prenotazioni dal CSV 
 */
  public boolean modificaPrenotazione(int idPrenotazione, Prenotazione nuovaPrenotazione) {
    boolean prenotazioneModificata = Prenotazione.modificaPrenotazioneNelCSV(idPrenotazione, nuovaPrenotazione);
    // se verifica se la prenotazione e quella che se vuo modificare con il boolean
    // di prenotazioneModificata
    if (prenotazioneModificata == true) {
      System.out.println("tua prenotazione è stata modificata con successo");
    } else {

      System.out.println("Non è stato possibile modificare la tua prenotazione.");
    }

    return prenotazioneModificata;
  }

  // questo metodo serve per modificare le prenotazioni del CSV//
  public boolean eliminaPrenotazione(int idPrenotazioneDaEliminare) {

    return Prenotazione.eliminaPrenotazioneDalCSV(idPrenotazioneDaEliminare);

    // se legge il file prenotazione, trova le prenotazione con
    // idPrenotazioneDaEliminare, elimina la prenotazione e salva il file
    // prenotazione
  }

  // -logout//
  public void logout() {

  }

  // mettere test true mostra l'interfaccia del menu cliente//
  public void mostraMenuCliente() throws FileNotFoundException, IOException {


      int sceltaMenucliente = 0;
      Scanner sc = new Scanner(System.in);

      do {

        System.out.println("---MENU CLIENTE---");
        System.out.println("");
        System.out.println("Benvenuto/a: " + this.getNome() + " " + this.getCognome());
        System.out.println("");
        System.out.println("-Scegli un numero (1,2,3) per continuare-");
        System.out.println("");
        System.out.println("1- Cerca proiezione e prenota");
        System.out.println("2- Visualizzare le mie prenotazione");
        System.out.println("3- Logout");
        System.out.println("");
        System.out.print("Scelta: ");
        sceltaMenucliente = this.leggiInt("numero non valido"); // se sceltaMenuCliente e diverso da 1,2,3 allora appare
                                                                // questo messagio

        switch (sceltaMenucliente) {
          case 1:

            Proiezione[] risultatoRicerca = this.cercaProiezione(); // cerca le proiezioni disponibi e salva il
                                                                    // risultato in un array
            Proiezione proiezioneSelezionata = Guest.selezionaProiezDaRicerca(risultatoRicerca); // Prende l'elenco
                                                                                                 // appena trovato, lo
                                                                                                 // mostra all'utente e
                                                                                                 // gli fa scegliere una
                                                                                                 // specifica proiezione
            if (proiezioneSelezionata == null) { // in caso in cui la proiezione sia null, esce
              System.out.println("Nessuna proiezione selezionata, tornando al menu iniziale");
              break;
            }

            int numeroPostiRichiesti = this.chiediNumeroDiPosti(proiezioneSelezionata);
            if (numeroPostiRichiesti <= 0) {
              System.out.println("posti non disponibili, tornando al menu iniziale");
              System.out.println("");

            } else {

              this.creaPrenotazione(proiezioneSelezionata, numeroPostiRichiesti); // in caso in cui si ci siano posti
                                                                                  // disponibili se procede afare la
                                                                                  // prenotazione
            }

            break;

          case 2:
            int sceltaPrenotazioni;
            do {
            	/*/
            	 *  interfaccia di prenotazione
            	 */
              System.out.println("---LE MIE PRENOTAZIONI---");
              System.out.println("");
              System.out.println("-Scegli un numero per continuare-");
              System.out.println("");
              System.out.println("1- Visualizzare prenotazioni");
              System.out.println("2- Modificare prenotazione");
              System.out.println("3- Eliminare prenotazione");
              System.out.println("0- INDIETRO");
              System.out.println("");
              System.out.println("Scelta");
              sceltaPrenotazioni = this.leggiInt("numero non valido");

              switch (sceltaPrenotazioni) {
                case 1:
                  int tornareIndietro;
                  System.out.println("---VISUALIZZA PRENOTAZIONI---");
                  this.visualizzaLeMiePrenotazioni();

                  do {
                    System.out.println("inserisci 0 per tornare indietro");
                    System.out.println("");

                    tornareIndietro = this.leggiInt("scelta non valida, \ninserisci 0 per tornare indietro");

                  } while (tornareIndietro != 0); // ogni volta che l'utente scrive un numero diverso da 0 torna
                                                  // indietro
                  break;
                case 2:
                  int scletaModificaPrenotazione;
                  do {
                    System.out.println("---MODIFICA PRENOTAZIONE---");
                    ArrayList<Prenotazione> prenotazioneC = Prenotazione.caricaPrenotazioni(); // Legge il file CSV e
                                                                                               // carica la lista
                                                                                               // completa di tutte le
                                                                                               // prenotazioni di tutti
                                                                                               // gli utenti
                    ArrayList<Prenotazione> prenotazioneCliente = new ArrayList<Prenotazione>();
                    prenotazioneCliente = Prenotazione.TrovaPrenotazioniConNomeECognome(this.getNome(),
                        this.getCognome(), prenotazioneC, false); // Filtra la lista generale cercando soltanto le
                                                                  // prenotazioni
                    // che corrispondono al nome e cognome
                    System.out.println("");
                    System.out.println(
                        "Le tue prenotazioni: ");
                    System.out.println("");

                    int indice = 1;
                    for (Prenotazione elemento : prenotazioneCliente) {
                      System.out.println(indice + " - " + elemento.toString(false)); // mostra i detagli di ogni
                                                                                     // prenotazione del cliente
                      indice++;
                    }
                    System.out.println("");
                    System.out.print(
                        "Inserisci il numero della prenotazione che vuoi modificare oppure scrivi 0 per tornare indietro: ");
                    scletaModificaPrenotazione = this.leggiInt("numero non valido");
                    if (scletaModificaPrenotazione == 0) {
                      break;
                    }
                    Prenotazione prenotazioneSelezionata;
                    try {
                      prenotazioneSelezionata = prenotazioneCliente.get(scletaModificaPrenotazione - 1);

                    } catch (IndexOutOfBoundsException ex) {
                      System.out.println("Indice non valido");
                      break;
                    }
                    // se verifica se l'ID del
                    // utente corrisponde a quello
                    // della prenotazione corrente
                    int sceltaDiModifica;

                    System.out.println("la prenotazione che vuoi modificare è: " + prenotazioneSelezionata.toString());
                    System.out.println("");
                    System.out.println("cosa vuoi modificare?: ");
                    System.out.println("");
                    System.out.println("1- Numero di posti");
                    System.out.println("2- La data della proiezione");
                    System.out.println("");
                    System.out.println("Scrive 1 oppure 2 per scegliere: ");
                    sceltaDiModifica = this.leggiInt("numero non valido");

                    switch (sceltaDiModifica) {
                      case 1:

                        // ATENZIONE: per ora la proiezioneSelezionata e null (se deve testare)
                        this.chiediQuantiPostiPrenotareEmodifica(null, prenotazioneSelezionata);
                        break;

                      case 2:
                        LinkedList<Proiezione> llProiezioni = this
                            .cercaProiezPerCambioPrenotaz(prenotazioneSelezionata.getProiezione_Titolo());

                        System.out.println("lista delle proiezioni da selezionare: ");
                        Proiezione[] listaProiezioni = new Proiezione[llProiezioni.size()];

                        int i = 0;
                        for (Proiezione el : llProiezioni) {
                          System.out.println();
                          System.out.println(i + 1);
                          el.visualizzaProiezione();

                          listaProiezioni[i] = el;
                          i++;
                        }

                        // TODO: fare i casi in cui le proiezioni che cerca l'utente non ci siano
                        // disponibilie

                        proiezioneSelezionata = Guest.selezionaProiezDaRicerca(listaProiezioni);
                        // l'utente sceglie la proiezione

                        this.chiediQuantiPostiPrenotareEmodifica(proiezioneSelezionata, prenotazioneSelezionata);
                        break;
                      default:
                        System.out.println("opzione non riconosciuta, riprova");
                        break;
                    }
                  } while (scletaModificaPrenotazione != 0);
                  // 3)chiamare il modifica
                  // se viene modifiacta la proiezione l'utente deve poter vedere la lista di
                  // proiezioni e orari. sulla base di quello io modifico il file di prenotazioni
                  // se invece l'utente puo modificare solo il numero di posti prenotati, ho
                  // bisogno di un metodo che mi modifichi il totale di posti per la proiezione
                  // (NUMPOSTIDISPONIBILI)

                  break;

                case 3:
                  int sceltaElimina;
                  System.out.println("---ELIMINA PRENOTAZIONE---");
                  System.out.println("");
                  ArrayList<Prenotazione> prenotazioniC = Prenotazione.caricaPrenotazioni(); // Legge il file CSV e
                  // carica la lista
                  // completa di tutte le
                  // prenotazioni di tutti
                  // gli utenti
                  ArrayList<Prenotazione> prenotazioniClienti = new ArrayList<Prenotazione>();
                  prenotazioniClienti = Prenotazione.TrovaPrenotazioniConNomeECognome(this.getNome(), this.getCognome(),
                      prenotazioniC, false); // Filtra la lista generale cercando soltanto le prenotazioni
                  // che corrispondono al nome e cognome
                  System.out.println("");
                  System.out.println("Que prenotazione vuoi eliminare? ");
                  System.out.println("");
                  // TODO: in caso in cui non ci siano prenotazioni, lasciare un messagio

                  int indice = 1;

                  if (prenotazioniClienti.size() == 0) {
                    System.out
                        .println("non ci sono prenotazioni disponibili di questa data in poi (tornando indietro)");
                    break;
                  }
                  for (Prenotazione elemento : prenotazioniClienti) {
                    System.out.println(indice + " - " + elemento.toString(false)); //// mostra i detagli di ogni
                                                                                   /// prenotazione del cliente
                    indice++;

                  }
                  System.out.println("");
                  System.out.print(
                      "Inserisci il numero della prenotazione che vuoi cancellare oppure scrivi 0 per tornare indietro: : ");
                  sceltaElimina = this.leggiInt("numero non valido");
                  if (sceltaElimina == 0) {
                    break;
                  }
                  boolean esiste = false;
                  String sceltaEliminaPrenotazione;
                  Prenotazione prenotazioneScelta;
                  try {
                    prenotazioneScelta = prenotazioniClienti.get(sceltaElimina - 1);
                  } catch (IndexOutOfBoundsException ex) {
                    System.out.println("Numero non valido");
                    break;
                  }

                  System.out.println("sei sicuro/a che vuoi eliminare questa prenotazione? (S/N): ");
                  sceltaEliminaPrenotazione = sc.nextLine();

                  if (sceltaEliminaPrenotazione.toUpperCase().charAt(0) == 'S') { // se verifica che il carattere
                                                                                  // scritto dal utente sia quello
                                                                                  // giusto
                    this.eliminaPrenotazione(prenotazioneScelta.getIDPrenotazione());
                  } else {
                    System.out.println("eliminazione annulata");
                  }

                  // in caso in cui sia 'S' la prenotazione se elimina, in caso in cui sia 'N',
                  // torna indietro
                  break;

                default:
                  System.out.println("opzione non valida");
                  break;
              }
            } while (sceltaPrenotazioni != 0);

          case 3:
            System.out.println("stai per efettuare il logout...");
            break;
          default:
            System.out.println("opzione non riconosciuta, riprova");
            break;

        }

      } while (sceltaMenucliente != 3);

    

  }
/*/
 *  metodo privato che serve a chiedere quanti posti prenotare per modificare
 */
  private void chiediQuantiPostiPrenotareEmodifica(Proiezione proiezioneSelezionata,
      Prenotazione prenotazioneSelezionata) {

    int postiSceltoDalUtente;

    int numeroDiPostiDisponibili = 0;
    if (proiezioneSelezionata == null) {
      ArrayList<Proiezione> palinsesto = Proiezione.caricaProiezioni();

      numeroDiPostiDisponibili = Proiezione.getPostiLiberiPerFilmEData(prenotazioneSelezionata.getProiezione_Titolo(),
          prenotazioneSelezionata.getProiezione_Data(), palinsesto);

    } else {
      numeroDiPostiDisponibili = proiezioneSelezionata.calcolaPostiLiberi();
    }

    System.out.println("scrivi il numero di posti che vuoi modificare");
    postiSceltoDalUtente = leggiInt("numero di posti non disponibili");

    if (numeroDiPostiDisponibili != -1 && postiSceltoDalUtente <= numeroDiPostiDisponibili) {

      Prenotazione nuovaPrenotazione = new Prenotazione(prenotazioneSelezionata.getIDPrenotazione(),
          prenotazioneSelezionata.getIDUtente(), prenotazioneSelezionata.getNome(),
          prenotazioneSelezionata.getCognome(),
          prenotazioneSelezionata.getProiezione_Data(), prenotazioneSelezionata.getProiezione_Titolo(),
          postiSceltoDalUtente,
          prenotazioneSelezionata.getPrezzoBiglietto());
      this.modificaPrenotazione(prenotazioneSelezionata.getIDPrenotazione(), nuovaPrenotazione);

    } else {
      System.out.println("non ci sono posti disponibili");
    }

  }
/*/
 * metodo che serve a chiedere quanti numeri di posti sono disponibili 
 */
  private int chiediNumeroDiPosti(Proiezione proiezioneSelezionata) {

    Scanner sc = new Scanner(System.in);
    System.out.println("scrivi quanti posti vuoi");
    int numeroDiPosti = this.leggiInt("numero di posti non valido");
    System.out.println("posti scritti:" + numeroDiPosti);

    boolean hapostidisponibili = proiezioneSelezionata.haPostiDisponibili(numeroDiPosti);
    if (hapostidisponibili == false) {
      numeroDiPosti = -1; //metto a -1 per controllarlo quando esco dal metodo
    }

    return numeroDiPosti;
  }

  private int leggiInt(String errorMessage) {
    Scanner sc = new Scanner(System.in);
    boolean inputStringintOk;
    String inputStringint;
    int variabileint = 0;

    do {
      inputStringintOk = true;
      try {
        inputStringint = sc.nextLine();
        variabileint = Integer.parseInt(inputStringint);

      } catch (NumberFormatException e) {
        inputStringintOk = false;
        System.out.println(errorMessage);
      }
    } while (inputStringintOk == false);
    return variabileint;
  }
}
