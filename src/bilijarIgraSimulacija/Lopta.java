package bilijarIgraSimulacija;
public class Lopta {
    double x;
    double y;
    double brzinaX;
    double brzinaY;
    double precnik;
    boolean uRupi;

    public boolean isuRupi() {
        return uRupi;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getBrzinaX() {
        return brzinaX;
    }

    public void setBrzinaX(double brzinaX) {
        this.brzinaX = brzinaX;
    }

    public double getBrzinaY() {
        return brzinaY;
    }

    public void setBrzinaY(double brzinaY) {
        this.brzinaY = brzinaY;
    }

    public double getPrecnik() {
        return precnik;
    }

    public void setPrecnik(double precnik) {
        this.precnik = precnik;
    }

    public void setuRupi(boolean uRupi) {
        this.uRupi = uRupi;
    }

    public Lopta(double x, double y, double brzinaX, double brzinaY) {
        this.x = x;
        this.y = y;
        this.brzinaX = brzinaX;
        this.brzinaY = brzinaY;
        this.precnik = 0.5; // Precnik kugle
        this.uRupi = false;
    }

    public void azurirajPoziciju(double deltaVreme) {
        if (!uRupi) {
            // Ažuriraj poziciju kugle ako nije u rupi
            x += brzinaX * deltaVreme;
            y += brzinaY * deltaVreme;
        }
    }

    public void sudarSaZidom(double sirinaStola, double visinaStola) {
        if (x - precnik < 0 || x + precnik > sirinaStola) {
            brzinaX = -brzinaX; // Odbijanje od leve ili desne ivice stola
        }

        if (y - precnik < 0 || y + precnik > visinaStola) {
            brzinaY = -brzinaY; // Odbijanje od gornje ili donje ivice stola
        }
    }

    public boolean sudarSaLoptom(Lopta drugaLopta) {
        if (this == drugaLopta) {
            return false; // Lopta se ne može sudariti sa samom sobom
        }

        if (Math.sqrt(Math.pow(x - drugaLopta.x, 2) + Math.pow(y - drugaLopta.y, 2)) <= 2 * precnik) {
            // Sudar između loptica
            double temp = brzinaX;
            brzinaX = drugaLopta.brzinaX;
            drugaLopta.brzinaX = temp;

            temp = brzinaY;
            brzinaY = drugaLopta.brzinaY;
            drugaLopta.brzinaY = temp;

            return true;
        }

        return false;
    }

    public void upadniURupu() {
        x = -1;
        y = -1;
        brzinaX = 0;
        brzinaY = 0;
        uRupi = true;
    }
}