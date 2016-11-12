package br.unisal.project.controller;

import br.unisal.project.model.PointModel;
import br.unisal.project.view.ExportView;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;

import java.sql.SQLException;

public class ExportController {
    private ExportView exportView;
    private MainController mainController;

    public ExportController(MainController mainController) {
        this.mainController = mainController;
        this.exportView = new ExportView(this);
    }

    public boolean export(int value) {
        XYSeries wheel = mainController.getWheel();
        XYSeries motor = mainController.getMotor();

        Thread t = new Thread() {
            @Override
            public void run() {
                if (value == 0) {
                    int maxLoop = (wheel.getItemCount() > motor.getItemCount()) ? motor.getItemCount() : wheel.getItemCount();

                    for (int i = 0; i < maxLoop; i++) {
                        XYDataItem itemW = wheel.getDataItem(i);
                        XYDataItem itemM = motor.getDataItem(i);

                        if (itemW.getX().equals(itemM.getX())) {
                            PointModel point = new PointModel();
                            createItem((int) itemW.getYValue(), (int) itemM.getYValue(), itemW.getXValue());
                        }
                    }

                } else {
                    if (value == 1) {
                        for (int i = 0; i < wheel.getItemCount(); i++) {
                            XYDataItem itemW = motor.getDataItem(i);
                            createItem((int) itemW.getYValue(), 0, itemW.getXValue());
                        }

                    } else if (value ==2) {
                        for (int i = 0; i < motor.getItemCount(); i++) {
                            XYDataItem itemM = motor.getDataItem(i);
                            createItem(0, (int) itemM.getYValue(), itemM.getXValue());
                        }

                    }
                }
            }
        };
        t.start();

        return true;
    }

    private boolean createItem(int wheel, int motor, double cicle) {
        try {
            PointModel point = new PointModel();
            point.setWheel(wheel);
            point.setMotor(motor);
            point.setCicle(cicle);
            point.save();

            return true;

        } catch (SQLException ex) {
            return false;
        }
    }

    public void setVisible(boolean cond) {
        exportView.setVisible(cond);
    }
}
