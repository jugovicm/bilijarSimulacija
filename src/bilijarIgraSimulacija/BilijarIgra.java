import bilijarIgraSimulacija.Lopta;
import bilijarIgraSimulacija.Sto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BilijarIgra {
    public static void main(String[] args) {
        int sirinaStola = 50;
        int visinaStola = 20;
        double deltaVreme = 0.01;

        Sto sto = new Sto(sirinaStola, visinaStola);

        // Postavite početno stanje stola i kugli
        postaviPocetnoStanje(sto);

        // Kreirajte JFrame za prikaz igre.
        JFrame frame = new JFrame("Bilijar Igra");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(sirinaStola * 20, visinaStola * 20);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int skaliranje = 20;

                g.setColor(Color.GREEN);
                g.fillRect(0, 0, sirinaStola * skaliranje, visinaStola * skaliranje);

                g.setColor(Color.BLACK);

                // Nacrtajte rupe na odgovarajućim pozicijama
                int rupaPrecnik = 1;
                g.fillOval(0, 0, rupaPrecnik * skaliranje, rupaPrecnik * skaliranje);
                g.fillOval(sirinaStola * skaliranje - rupaPrecnik * skaliranje, 0, rupaPrecnik * skaliranje, rupaPrecnik * skaliranje);
                g.fillOval(sirinaStola * skaliranje / 2 - rupaPrecnik * skaliranje / 2, 0, rupaPrecnik * skaliranje, rupaPrecnik * skaliranje);
                g.fillOval(0, visinaStola * skaliranje - rupaPrecnik * skaliranje, rupaPrecnik * skaliranje, rupaPrecnik * skaliranje);
                g.fillOval(sirinaStola * skaliranje - rupaPrecnik * skaliranje, visinaStola * skaliranje - rupaPrecnik * skaliranje, rupaPrecnik * skaliranje, rupaPrecnik * skaliranje);
                g.fillOval(sirinaStola * skaliranje / 2 - rupaPrecnik * skaliranje / 2, visinaStola * skaliranje - rupaPrecnik * skaliranje, rupaPrecnik * skaliranje, rupaPrecnik * skaliranje);

                g.setColor(Color.RED);
                List<Lopta> lopte = sto.getLopte();
                for (Lopta lopta : lopte) {
                    if (!lopta.isuRupi ()) {
                        g.fillOval((int) (lopta.getX() * skaliranje - lopta.getPrecnik() * skaliranje),
                                (int) (lopta.getY() * skaliranje - lopta.getPrecnik() * skaliranje),
                                (int) (2 * lopta.getPrecnik() * skaliranje),
                                (int) (2 * lopta.getPrecnik() * skaliranje));
                    }
                }
                repaint();
            }
        };

        panel.setPreferredSize(new Dimension(sirinaStola * 20, visinaStola * 20));
        frame.add(panel);
        frame.pack();

        Timer timer = new Timer((int) (deltaVreme * 1000), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sto.detekcijaSudara();
                panel.repaint();
            }
        });
        timer.start();

        Thread simulacijaThread = new Thread(() -> {
            while (true) {
                sto.detekcijaSudara();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        simulacijaThread.start();

        Thread cuvanjeStanjaThread = new Thread(() -> {
            while (true) {
                // Čuvaj trenutno stanje sistema u izlazne fajlove, koristeći isti format kao ulazni fajlovi.
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        cuvanjeStanjaThread.start();

        frame.setVisible(true);
    }

    private static void postaviPocetnoStanje(Sto sto) {
        // Postavite početno stanje stola i kugli.
    }
}