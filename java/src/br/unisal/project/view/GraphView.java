package br.unisal.project.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    private JFrame window;
    private Communicator comm;
    private XYSeries seriesM;
    private XYSeries seriesV;
    private XYSeriesCollection dataset;
    private double x = 0;
    private int m = 0;
    private int v = 0;
    private int direction = 1;

    public GraphView() {
        this.seriesM = new XYSeries("Motor");
        this.seriesV = new XYSeries("Volante");
        this.comm = new Communicator(this);
        this.configureWindow();
        this.configureChart();
        this.chartListener();
    }

    private void configureWindow() {
        window = new JFrame();
        window.setTitle("Gráficos do Sensor");
        window.setSize(600, 400);
        window.setLayout(new BorderLayout());
        window.setExtendedState(window.getExtendedState() | window.MAXIMIZED_BOTH);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            public void WindowClosing(WindowEvent e) {
                comm.close();
                window.dispose();
            }
        });

        JButton clearButton = new JButton("Limpar");
        JButton revertButton = new JButton("Mudar para Ré");
        JPanel topPanel = new JPanel();
        topPanel.add(clearButton);
        topPanel.add(revertButton);
        window.add(topPanel, BorderLayout.NORTH);

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataset.getSeries(0).clear();
                dataset.getSeries(1).clear();
            }
        });

        revertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (direction == 1) {
                    comm.send(0);
                    direction = 0;
                    revertButton.setText("Mudar para frente");

                } else {
                    comm.send(1);
                    direction = 1;
                    revertButton.setText("Mudar para Ré");
                }
            }
        });
    }

    private void configureChart() {
        dataset = new XYSeriesCollection();
        dataset.addSeries(this.seriesM);
        dataset.addSeries(this.seriesV);

        JFreeChart chart = ChartFactory.createXYLineChart("Dados do Controle", "Tempo (segundos)", "Valor", dataset);
        window.add(new ChartPanel(chart), BorderLayout.CENTER);
    }

    private void chartListener() {
        Thread t = new Thread() {
            public void run() {
                try {
                    while(true) {
                        seriesM.add(x, m);
                        seriesV.add(x, v);
                        System.out.println(x);
                        x += 0.5;
                        Thread.sleep(500);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public void start() {
        comm.initialize();
        window.setVisible(true);
    }

    public void drawMotor(int value) {
        this.m = value;
        //this.seriesM.add(this.x/2, value);
        //this.x++;
    }

    public void drawWheel(int value) {
        this.v = value;
        //this.seriesV.add(this.x/2, value);
        //this.x++;
    }
}