package br.unisal.project.model;

import br.unisal.project.connector.JDBCInsert;

import java.sql.SQLException;

public class PointModel {
    private Long id;
    private int motor;
    private int wheel;
    private double cicle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMotor() {
        return motor;
    }

    public void setMotor(int motor) {
        this.motor = motor;
    }

    public int getWheel() {
        return wheel;
    }

    public void setWheel(int wheel) {
        this.wheel = wheel;
    }

    public double getCicle() {
        return cicle;
    }

    public void setCicle(double cicle) {
        this.cicle = cicle;
    }

    public void save() throws SQLException {
        JDBCInsert insert = new JDBCInsert(this);
    }
}
