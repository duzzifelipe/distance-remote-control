package br.unisal.project.controller;

import br.unisal.project.model.PointModel;
import br.unisal.project.view.ExportView;

import java.sql.SQLException;

public class ExportController {
    private ExportView exportView;

    public ExportController() {
        this.exportView = new ExportView();

        PointModel point = new PointModel();
        point.setMotor(150);
        point.setWheel(30);
        point.setCicle(3.0);

        try {
            point.save();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setVisible(boolean cond) {
        exportView.setVisible(cond);
    }
}
