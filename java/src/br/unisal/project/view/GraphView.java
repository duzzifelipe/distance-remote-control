package br.unisal.project.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import br.unisal.project.controller.Controller;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class GraphView {
    private Controller controller;
    private JFrame window;
    private XYSeries seriesM;
    private XYSeries seriesW;
    private XYSeriesCollection dataset;
    private int direction = 1;

    public GraphView(Controller controller) {
        this.controller = controller;
        this.seriesM = new XYSeries("Motor");
        this.seriesW = new XYSeries("Volante");
        this.configureWindow();
        this.configureChart();
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
                controller.close();
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
                if (controller.toggleDirection() == 1) {
                    revertButton.setText("Mudar para Ré");

                } else {
                    revertButton.setText("Mudar para frente");
                }
            }
        });
    }

    private void configureChart() {
        dataset = new XYSeriesCollection();
        dataset.addSeries(this.seriesM);
        dataset.addSeries(this.seriesW);

        JFreeChart chart = ChartFactory.createXYLineChart("Dados do Controle", "Tempo (segundos)", "Valor", dataset);
        window.add(new ChartPanel(chart), BorderLayout.CENTER);
    }

    public void setVisible(boolean visible) {
        window.setVisible(visible);
    }

    public void drawMotor(double time, int value) {
        this.seriesM.add(time, value);
        this.clearOld(this.seriesM);
    }

    public void drawWheel(double time, int value) {
        this.seriesW.add(time, value);
        this.clearOld(this.seriesW);
    }

    private void clearOld(XYSeries series) {
        if (series.getItemCount() > 100) {
            series.remove(0);
        }
    }
}