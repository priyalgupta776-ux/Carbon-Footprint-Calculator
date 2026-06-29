import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class PieChartPanel extends JPanel {
    // --- Inner class for drawing pie chart ---//
        private double elecVal, transVal, wasteVal;

        public void updateValues(double e, double t, double w) {
            this.elecVal = e;
            this.transVal = t;
            this.wasteVal = w;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            DecimalFormat df = new DecimalFormat("#.#");
            super.paintComponent(g);
            double total = elecVal + transVal + wasteVal;
            double elecP=(elecVal/total)*100;
            double transP=(transVal/total)*100;
            double wasteP = (wasteVal /total)*100;
            if (total == 0) return;

            int diameter = 220;
            int x = (getWidth() - diameter) / 2;
            int y = 30;

            double elecAngle = (elecVal / total) * 360;
            double transAngle = (transVal / total) * 360;
            double wasteAngle = (wasteVal / total) * 360;

            g.setColor(Color.CYAN);
            g.fillArc(x, y, diameter, diameter, 0, (int) elecAngle);

            g.setColor(Color.ORANGE);
            g.fillArc(x, y, diameter, diameter, (int) elecAngle, (int) transAngle);

            g.setColor(Color.LIGHT_GRAY);
            g.fillArc(x, y, diameter, diameter, (int) (elecAngle + transAngle), (int) wasteAngle);

            // Legend
            g.setColor(Color.BLACK);
            g.drawString("Electricity" +" " + df.format(elecP) +"%" , x + 20, y + diameter + 25);
            g.setColor(Color.CYAN);
            g.fillRect(x, y + diameter + 15, 10, 10);

            g.setColor(Color.BLACK);
            g.drawString("Transport" + " "+df.format(transP) + "%" ,x + 170, y + diameter + 25);
            g.setColor(Color.ORANGE);
            g.fillRect(x + 150, y + diameter + 15, 10, 10);

            g.setColor(Color.BLACK);
            g.drawString("Waste" +" "+df.format(wasteP) +"%", x + 320, y + diameter + 25);
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(x + 300, y + diameter + 15, 10, 10);
        }
    }


