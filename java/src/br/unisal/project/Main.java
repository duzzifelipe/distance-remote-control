package br.unisal.project;

import br.unisal.project.arduino.Communicator;
import br.unisal.project.view.GraphView;

public class Main {

    public static void main(String[] args) {
        GraphView gf = new GraphView();
        gf.start();
    }
}
