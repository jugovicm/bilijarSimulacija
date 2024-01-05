package bilijarIgraSimulacija;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BilijarIgra {
    public static void main(String[] args) {
        double sirinaStola = 50.0;
        double visinaStola = 20.0;
        double deltaVreme = 0.01;

        Sto sto = new Sto(sirinaStola, visinaStola);

        postaviPocetnoStanje(sto);

        // Kreiraj JFrame za prikaz igre.
        JFrame frame = new JFrame("Bilijar Igra");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize((int) (sirinaStola * 20), (int) (visinaStola * 20));

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int skaliranje = 20;

                g.setColor(Color.GREEN);
                g.fillRect(0, 0, (int) (sirinaStola * skaliranje), (int) (visinaStola * skaliranje));

                g.setColor(Color.BLACK);

                // Nacrtaj rupe
                int rupaPrecnik = 1;
                g.fillOval(0, 0, (int) (rupaPrecnik * skaliranje), (int) (rupaPrecnik * skaliranje));
                g.fillOval((int) (sirinaStola * skaliranje - rupaPrecnik * skaliranje), 0, (int) (rupaPrecnik * skaliranje), (int) (rupaPrecnik * skaliranje));
                g.fillOval((int) (sirinaStola * skaliranje / 2 - rupaPrecnik * skaliranje / 2), 0, (int) (rupaPrecnik * skaliranje), (int) (rupaPrecnik * skaliranje));
                g.fillOval(0, (int) (visinaStola * skaliranje - rupaPrecnik * skaliranje), (int) (rupaPrecnik * skaliranje), (int) (rupaPrecnik * skaliranje));
                g.fillOval((int) (sirinaStola * skaliranje - rupaPrecnik * skaliranje), (int) (visinaStola * skaliranje - rupaPrecnik * skaliranje), (int) (rupaPrecnik * skaliranje), (int) (rupaPrecnik * skaliranje));
                g.fillOval((int) (sirinaStola * skaliranje / 2 - rupaPrecnik * skaliranje / 2), (int) (visinaStola * skaliranje - rupaPrecnik * skaliranje), (int) (rupaPrecnik * skaliranje), (int) (rupaPrecnik * skaliranje));

                List<Lopta> lopte = sto.getLopte();
                for (Lopta lopta : lopte) {
                    if (!lopta.isuRupi()) {
                        g.setColor(lopta.getBoja());
                        g.fillOval((int) (lopta.getX() * skaliranje - lopta.getPrecnik() * skaliranje),
                                (int) (lopta.getY() * skaliranje - lopta.getPrecnik() * skaliranje),
                                (int) (2 * lopta.getPrecnik() * skaliranje),
                                (int) (2 * lopta.getPrecnik() * skaliranje));
                    }
                }
                repaint();
            }
        };

        panel.setPreferredSize(new Dimension((int) (sirinaStola * 20), (int) (visinaStola * 20)));
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
                sto.detekcijaSudara();
                panel.repaint();
                Path outputPath = Paths.get("src/bilijarIgraSimulacija/output.txt");
                cuvajTrenutnoStanje(sto.getLopte(), outputPath.toString());
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

    private static void cuvajTrenutnoStanje(List<Lopta> lopte, String putanjaDoFajla) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(putanjaDoFajla))) {
            for (Lopta lopta : lopte) {
                writer.println(lopta.getX() + " " + lopta.getY() + " " + lopta.getBrzinaX() + " " + lopta.getBrzinaY());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void postaviPocetnoStanje(Sto sto) {
        // Postavi početno stanje stola i kugli
        Path inputPath = Paths.get("src/bilijarIgraSimulacija/input.txt");
        sto.ucitajPocetnoStanje(inputPath.toString());
        /* TESTIRANJE
        sto.dodajRupu(1, 1, 1);  // Leva gornja rupa
        sto.dodajRupu(49, 1, 1);  // Desna gornja rupa
        sto.dodajRupu(25, 1, 1);  // Srednja gornja rupa
        sto.dodajRupu(1, 19, 1);  // Leva donja rupa
        sto.dodajRupu(49, 19, 1);  // Desna donja rupa
        sto.dodajRupu(25, 19, 1);  // Srednja donja rupa

        sto.dodajLoptu(new Lopta(5, 5, 0.4, -0.02, Color.RED));
        sto.dodajLoptu(new Lopta(10, 10, -0.3, 0.1, Color.BLUE));
        sto.dodajLoptu(new Lopta(25, 15, 0, 0, Color.GREEN));
        sto.dodajLoptu(new Lopta(20, 5, 0, 0, Color.YELLOW));
        sto.dodajLoptu(new Lopta(35, 10, 0.2, 0.1, Color.ORANGE));
        sto.dodajLoptu(new Lopta(30, 15, -0.2, -0.4, Color.LIGHT_GRAY));
        sto.dodajLoptu(new Lopta(15, 10, 0.2, 0.3, Color.WHITE));
        sto.dodajLoptu(new Lopta(40, 5, -0.3, 0.5, Color.PINK));*/
    }
}