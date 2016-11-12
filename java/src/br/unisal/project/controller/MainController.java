package br.unisal.project.controller;

import br.unisal.project.arduino.Communicator;
import br.unisal.project.view.GraphView;
import org.jfree.data.xy.XYSeries;

public class MainController implements ControllerInterface {
    private Communicator comm;
    private GraphView view;
    private ExportController export;
    private int motor;
    private int wheel;
    private int direction = 1;
    private double timer = 0;

    public MainController() {
        view = new GraphView(this);
        comm = new Communicator(this);
        export = new ExportController(this);
        comm.initialize();
        chartListener();
        view.setVisible(true);
    }

    private void chartListener() {
        Thread t = new Thread() {
            public void run() {
                try {
                    while(true) {
                        view.drawMotor(timer, motor);
                        view.drawWheel(timer, wheel);
                        timer += 0.25;
                        Thread.sleep(250);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public void openExport() {
        export.setVisible(true);
    }

    public int toggleDirection() {
        if (direction == 1) {
            direction = 0;

        } else {
            direction = 1;
        }

        send(direction);
        return direction;
    }

    public int getDirection() {
        return direction;
    }

    public XYSeries getMotor() {
        return view.getSeriesM();
    }

    public XYSeries getWheel() {
        return view.getSeriesW();
    }

    @Override
    public void send(int value) {
        this.comm.send(value);
    }

    @Override
    public void close() {
        this.comm.close();
    }

    @Override
    public void setMotor(int value) {
        this.motor = value;
    }

    @Override
    public void setWheel(int value) {
        this.wheel = value;
    }
}
