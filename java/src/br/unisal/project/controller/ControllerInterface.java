package br.unisal.project.controller;

public interface ControllerInterface {
    void setMotor(int value);
    void setWheel(int value);
    int toggleDirection();
    int getDirection();
    void send(int value);
    void close();
}
