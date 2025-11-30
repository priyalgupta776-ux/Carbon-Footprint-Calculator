import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CarbonFootprintGUI extends JFrame implements ActionListener {

    private JTextField electricityField, transportField, wasteField;
    private JLabel resultLabel, categoryLabel;
    private JButton calcButton, tipsButton, resetButton;
    private double electricity, transport, waste, totalCO2;
    private PieChartPanel pieChartPanel;
    private String category;

    public CarbonFootprintGUI() {
        setTitle("Carbon Footprint Estimator with Chart");
        setSize(650, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Top panel for inputs ---
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Monthly Data"));

        inputPanel.add(new JLabel("Electricity (kWh):"));
        electricityField = new JTextField();
        inputPanel.add(electricityField);

        inputPanel.add(new JLabel("Transport (litres of fuel):"));
        transportField = new JTextField();
        inputPanel.add(transportField);

        inputPanel.add(new JLabel("Waste (kg):"));
        wasteField = new JTextField();
        inputPanel.add(wasteField);

        calcButton = new JButton("Calculate Footprint");
        calcButton.addActionListener(this);
        inputPanel.add(calcButton);

        tipsButton = new JButton("Show Eco Tips");
        tipsButton.addActionListener(e -> showEcoTips());
        inputPanel.add(tipsButton);

        add(inputPanel, BorderLayout.NORTH);

        // --- Center panel: pie chart ---
        pieChartPanel = new PieChartPanel();
        add(pieChartPanel, BorderLayout.CENTER);

        // --- Bottom panel: results and reset ---
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1));
        resultLabel = new JLabel("Your Carbon Footprint: ", SwingConstants.CENTER);
        categoryLabel = new JLabel("", SwingConstants.CENTER);

        resetButton = new JButton("🔄 Reset");
        resetButton.addActionListener(e -> resetFields());

        bottomPanel.add(resultLabel);
        bottomPanel.add(categoryLabel);
        bottomPanel.add(resetButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            electricity = Double.parseDouble(electricityField.getText());
            transport = Double.parseDouble(transportField.getText());
            waste = Double.parseDouble(wasteField.getText());

            // Calculate CO₂
            totalCO2 = (electricity * 0.5) + (transport * 2.3) + (waste * 0.1);

            // Determine category
            Color color;
            if (totalCO2 < 200) {
                category = "Low Emissions 🌿";
                color = new Color(0, 153, 0);
            } else if (totalCO2 < 500) {
                category = "Medium Emissions ⚡";
                color = new Color(255, 153, 0);
            } else {
                category = "High Emissions 🚗";
                color = Color.RED;
            }

            resultLabel.setText(String.format("Total CO₂: %.2f kg/month", totalCO2));
            categoryLabel.setText(category);
            categoryLabel.setForeground(color);

            // Update pie chart
            pieChartPanel.updateValues(electricity, transport, waste);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showEcoTips() {
        JOptionPane.showMessageDialog(this,
                "🌱 Eco-Friendly Tips:\n" +
                        "1. Switch to energy-efficient bulbs.\n" +
                        "2. Walk, bike, or use public transport.\n" +
                        "3. Recycle and compost regularly.\n" +
                        "4. Reduce air conditioner use.\n" +
                        "5. Plant more trees 🌳.",
                "Eco Tips",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // --- Reset all input fields and chart ---
    private void resetFields() {
        electricityField.setText("");
        transportField.setText("");
        wasteField.setText("");
        resultLabel.setText("Your Carbon Footprint: ");
        categoryLabel.setText("");
        categoryLabel.setForeground(Color.BLACK);
        pieChartPanel.updateValues(0, 0, 0); // clear chart
        totalCO2 = 0;
    }

    // --- Inner class for drawing pie chart ---
    class PieChartPanel extends JPanel {
        private double elecVal, transVal, wasteVal;

        public void updateValues(double e, double t, double w) {
            this.elecVal = e;
            this.transVal = t;
            this.wasteVal = w;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            double total = elecVal + transVal + wasteVal;
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
            g.drawString("Electricity", x + 20, y + diameter + 25);
            g.setColor(Color.CYAN);
            g.fillRect(x, y + diameter + 15, 10, 10);

            g.setColor(Color.BLACK);
            g.drawString("Transport", x + 120, y + diameter + 25);
            g.setColor(Color.ORANGE);
            g.fillRect(x + 100, y + diameter + 15, 10, 10);

            g.setColor(Color.BLACK);
            g.drawString("Waste", x + 220, y + diameter + 25);
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(x + 200, y + diameter + 15, 10, 10);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CarbonFootprintGUI().setVisible(true));
    }
}
