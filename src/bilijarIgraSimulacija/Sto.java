package bilijarIgraSimulacija;
import java.awt.*;
import java.util.ArrayList;

public class Sto {
    private int sirinaStola;
    private int visinaStola;
    private ArrayList<Lopta> lopte;
    private ArrayList<Rectangle> rupe;

    public ArrayList<Lopta> getLopte() {
        return lopte;
    }

    public void setLopte(ArrayList<Lopta> lopte) {
        this.lopte = lopte;
    }

    public Sto(int sirinaStola, int visinaStola) {
        this.sirinaStola = sirinaStola;
        this.visinaStola = visinaStola;
        this.lopte = new ArrayList<>();
        this.rupe = new ArrayList<>();
    }

    public void dodajLoptu(Lopta lopta) {
        lopte.add(lopta);
    }

    public void dodajRupu(double x, double y, double precnik) {
        Rectangle rupa = new Rectangle((int) (x - precnik), (int) (y - precnik), (int) (2 * precnik), (int) (2 * precnik));
        rupe.add(rupa);
    }

    public void detekcijaSudara() {
        for (Lopta lopta : lopte) {
            lopta.azurirajPoziciju(0.01); // Ažuriraj poziciju kugle
            lopta.sudarSaZidom(sirinaStola, visinaStola); // Sudara sa ivicama stola

            for (Rectangle rupa : rupe) {
                if (rupa.contains(lopta.x, lopta.y)) {
                    lopta.upadniURupu(); // Lopta je upala u rupu
                }
            }

            for (Lopta drugaLopta : lopte) {
                lopta.sudarSaLoptom(drugaLopta); // Sudara između loptica
            }
        }
    }

}
