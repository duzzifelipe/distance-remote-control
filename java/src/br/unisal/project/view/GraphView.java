package br.unisal.project.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import br.unisal.project.arduino.Communicator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class GraphView {
    private XYSeries seriesM;
    private XYSeries seriesV;
    private int x = 0;

    public GraphView() {
        this.seriesM = new XYSeries("Motor");
        this.seriesV = new XYSeries("Volante");
    }

    public void main() {
        JFrame window = new JFrame();
        window.setTitle("Gráficos do Sensor");
        window.setSize(600, 400);
        window.setLayout(new BorderLayout());
        window.setExtendedState(window.getExtendedState() | window.MAXIMIZED_BOTH);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton connectButton = new JButton("Ligar");
        JPanel topPanel = new JPanel();
        topPanel.add(connectButton);
        window.add(topPanel, BorderLayout.NORTH);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(this.seriesM);
        dataset.addSeries(this.seriesV);

        JFreeChart chart = ChartFactory.createXYLineChart("Dados do Controle", "Tempo (segundos)", "Valor", dataset);
        window.add(new ChartPanel(chart), BorderLayout.CENTER);

        Communicator comm = new Communicator(this);
        comm.initialize();
        Thread t = new Thread() {
            public void run() {
                try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
            }
        };
        t.start();

        // show the window
        window.setVisible(true);
    }

    public void drawMotor(int value) {
        this.seriesM.add(this.x/2, value);
        this.x++;
    }

    public void drawWheel(int value) {
        this.seriesV.add(this.x/2, value);
        this.x++;
    }
}