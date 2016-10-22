package Arduino;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Communicator {
    private OutputStream serialOut;
    private String comPort;
    private int tax;

    public Communicator(String comPort, int tax) {
        this.comPort = comPort;
        this.tax = tax;
        this.initialize();
    }

    private void initialize() {
        try {
            CommPortIdentifier portId = null;
            try {
                portId = CommPortIdentifier.getPortIdentifier(this.comPort);
            } catch (NoSuchPortException npe) {
                // Port not found
                npe.printStackTrace();
            }

            SerialPort port = (SerialPort) portId.open("Serial communication", this.tax);
            serialOut = port.getOutputStream();
            port.setSerialPortParams(this.tax, //taxa de transferência da porta serial
                    SerialPort.DATABITS_8, //taxa de 10 bits 8 (envio)
                    SerialPort.STOPBITS_1, //taxa de 10 bits 1 (recebimento)
                    SerialPort.PARITY_NONE); //receber e enviar dados
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
