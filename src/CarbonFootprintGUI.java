import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Desktop;
import java.io.File;

public class CarbonFootprintGUI extends JFrame implements ActionListener {

    private JTextField electricityField, transportField, wasteField;
    private JLabel resultLabel, categoryLabel;
    private JButton calcButton, tipsButton, resetButton, downloadButton;
    private double electricity, transport, waste, totalCO2;
    private PieChartPanel pieChartPanel;
    private String category;
    private Color color;
    private static final Color low = new Color(0, 153, 0);
    private static final Color medium = new Color(255, 153, 0);
    private static final Color high = Color.RED;

    public CarbonFootprintGUI() {
        setTitle("Carbon Footprint Estimator with Chart");
        setSize(750, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Top panel for inputs ---
        JPanel inputPanel = new JPanel(new GridLayout(5, 3, 10, 10));
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
        tipsButton.addActionListener(this);
        inputPanel.add(tipsButton);

        downloadButton = new JButton("Download Report");
        downloadButton.addActionListener(this);
        inputPanel.add(downloadButton);

        add(inputPanel, BorderLayout.NORTH);

        // --- Center panel: pie chart ---
        pieChartPanel = new PieChartPanel();
        add(pieChartPanel, BorderLayout.CENTER);

        // --- Bottom panel: results and reset ---
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1));
        resultLabel = new JLabel("Your Carbon Footprint: ", SwingConstants.CENTER);
        categoryLabel = new JLabel("", SwingConstants.CENTER);

        resetButton = new JButton("🔄 Reset");
        resetButton.addActionListener(this);

        bottomPanel.add(resultLabel);
        bottomPanel.add(categoryLabel);
        bottomPanel.add(resetButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void downloadReport() {
        try {
            FileWriter report = new FileWriter("CarbonFootprintReport.txt");
            report.write("Carbon Footprint Report\n\n");

            report.write("Electricity : " + electricity + " kWh\n");
            report.write("Transport   : " + transport + " litres\n");
            report.write("Waste       : " + waste + " kg\n\n");

            report.write("Total CO2   : " + totalCO2 + " kg/month\n");
            report.write("Category    : " + category + "\n");

            report.close();
            Desktop.getDesktop().open(new File("CarbonFootprintReport.txt"));


            JOptionPane.showMessageDialog(this,
                    "Report downloaded successfully!");

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(this,
                    "Error saving file.");
        }
    }


    public void calculateCO2() {
        readInput();
        calculateAnswer();
        showOutput();
    }

    public void readInput() {
        try {
            electricity = Double.parseDouble(electricityField.getText());
            transport = Double.parseDouble(transportField.getText());
            waste = Double.parseDouble(wasteField.getText());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void calculateAnswer() {
        totalCO2 = (electricity * 0.5) + (transport * 2.3) + (waste * 0.1);
        if (totalCO2 < 200) {
            category = "Low Emissions 🌿";
            color =low;
        } else if (totalCO2 < 500) {
            category = "Medium Emissions ⚡";
            color = medium;
        } else {
            category = "High Emissions 🚗";
            color = high;
        }
    }


    public void showOutput() {
        resultLabel.setText(String.format("Total CO₂: %.2f kg/month", totalCO2));
        categoryLabel.setText(category);
        categoryLabel.setForeground(color);
        pieChartPanel.updateValues(electricity, transport, waste);


    }

    public void showTips() {
        JOptionPane.showMessageDialog(this,
                "🌱 Eco-Friendly Tips:\n" +
                        "1. Switch to energy-efficient bulbs.\n" +
                        "2. Walk, bike, or use public transport.\n" +
                        "3. Recycle and compost regularly.\n" +
                        "4. Reduce air conditioner use.\n" +
                        "5. Plant more trees.",
                "Eco Tips",
                JOptionPane.INFORMATION_MESSAGE);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == calcButton) {
            calculateCO2();
        } else if (e.getSource() == tipsButton) {
            showTips();
        } else if (e.getSource() == downloadButton) {
            downloadReport();
        } else {
            resetAll();
        }
    }


    // --- Reset all input fields and chart ---
    public void resetAll() {
        electricityField.setText("");
        transportField.setText("");
        wasteField.setText("");
        resultLabel.setText("Your Carbon Footprint: ");
        categoryLabel.setText("");
        categoryLabel.setForeground(Color.BLACK);
        pieChartPanel.updateValues(0, 0, 0); // clear chart
        totalCO2 = 0;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CarbonFootprintGUI().setVisible(true));
    }
}

