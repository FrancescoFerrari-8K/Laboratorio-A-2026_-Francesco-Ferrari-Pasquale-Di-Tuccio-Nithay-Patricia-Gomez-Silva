package CineMaX;

public class Cliente extends Guest {

    //parametri //

    //costruttore//

    public Cliente (String nome, String cognome, String password, int id ){
        super(nome, cognome, "C",password, id);
    }


      //metodi//

      //- cerca proiezione (implementato da guest)//
      //- visulazza proiezione (implementato da guest)//




      //-visualizzare le propie prenotazioni//
    public void visualizzarePrenotazione(){
        //leggere file prenotazione 
        //strarre le prenotazioni a nome di questo cliente
        //mostrarle

    }

      //crea prenotazione//
      public Prenotazione creaPrenotazione( String prenotazione_Data, String prenotazione_Titolo, int fila, int posto){
        //TODO: controllare il numero di posti disponibili della proiezione e creare la prenotazione solo si ci sono posti
        
        Prenotazione prenotazione = new Prenotazione(this.getNome(), this.getCognome(), prenotazione_Data, prenotazione_Titolo, fila, posto);
        // Prenotazione prenotazione = new Prenotazione(this.getNome(), this.getCognome(), film, fila,posto);
        return prenotazione;

      }

      //modifica prenotazione//
      public void modificaPrenotazione(int idPrenotazioneDaModificare, Prenotazione nuovaPrenotazione){
        //leggere file prenotazione 
        //trovare prenotazione con idPrenotazioneDaModificare
        //sostituirla con nuovaPrenotazione
        //salvare file prenotazione
      }

      //elimina prenotazione//
      public void eliminaPrenotazione(int idPrenotazioneDaEliminare){

         //leggere file prenotazione 
          //trovare prenotazione con idPrenotazioneDaEliminare
          //eliminarla
          //salvare file prenotazione
      }
     
      //-logout//
      public void logout(){
        
      }

}
