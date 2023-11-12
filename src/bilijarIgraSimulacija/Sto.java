package bilijarIgraSimulacija;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Sto {
    private static final double MAX_BRZINA = 1.0; // Definišite maksimalnu brzinu

    private double sirinaStola;
    private double visinaStola;
    private ArrayList<Lopta> lopte;
    private ArrayList<Rectangle> rupe;

    public ArrayList<Lopta> getLopte() {
        return lopte;
    }

    public void setLopte(ArrayList<Lopta> lopte) {
        this.lopte = lopte;
    }

    public Sto(double sirinaStola, double visinaStola) {
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

    public void ucitajPocetnoStanje(String putanjaDoDatoteke) {
        try (BufferedReader br = new BufferedReader(new FileReader(putanjaDoDatoteke))) {
            String linija;
            Color[] dostupneBoje = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.PINK};

            int indeksBoje = 0;

            while ((linija = br.readLine()) != null) {
                String[] delovi = linija.split(" ");
                if (delovi.length == 4) {
                    double x = Double.parseDouble(delovi[0]);
                    double y = Double.parseDouble(delovi[1]);
                    double brzinaX = Double.parseDouble(delovi[2]);
                    double brzinaY = Double.parseDouble(delovi[3]);

                    // Proveri da li je lopta unutar stola pre dodavanja
                    if (x >= 0 && x <= sirinaStola && y >= 0 && y <= visinaStola) {
                        // Provera brzine
                        if (Math.abs(brzinaX) <= MAX_BRZINA && Math.abs(brzinaY) <= MAX_BRZINA) {
                            // Odabir boje i ažuriranje indeksa
                            Color boja = dostupneBoje[indeksBoje];
                            indeksBoje = (indeksBoje + 1) % dostupneBoje.length;

                            Lopta novaLopta = new Lopta(x, y, brzinaX, brzinaY, boja);
                            dodajLoptu(novaLopta);
                        } else {
                            System.out.println("Neispravne brzine u datoteci: " + brzinaX + ", " + brzinaY);
                        }
                    } else {
                        System.out.println("Neispravna pozicija u datoteci: " + x + ", " + y);
                    }
                } else {
                    System.out.println("Neispravan red u datoteci: " + linija);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
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