package br.unisal.project.controller;

public class Controller implements ControllerInterface {
    public void setMotor(int value) {}
    public void setWheel(int value) {}
    public int toggleDirection() { return 0; }
    public int getDirection() { return 0; }
    public void send(int value) {};
    public void close() {};
}
