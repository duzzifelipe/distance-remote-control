package br.unisal.project.connector;

import br.unisal.project.model.PointModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCInsert {
    private String insert = "INSERT INTO point (motor, wheel, cicle) VALUES (?, ?, ?)";

    public JDBCInsert(PointModel point) throws SQLException {
        Connection con = (new JDBCConnector()).getConnection();

        PreparedStatement stmt = con.prepareStatement(insert);
        stmt.setInt(1, point.getMotor());
        stmt.setInt(2, point.getWheel());
        stmt.setDouble(3, point.getCicle());

        stmt.execute();
        stmt.close();
        con.close();
    }
}
