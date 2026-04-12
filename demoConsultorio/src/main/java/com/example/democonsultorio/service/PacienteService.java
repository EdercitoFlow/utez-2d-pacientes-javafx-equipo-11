package com.example.democonsultorio.service;

import com.example.democonsultorio.model.Paciente;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteService {
    private List<Paciente> pacientes = new ArrayList<>();
    private final String ARCHIVO = "pacientes.csv";

    public void cargarArchivo() {
        pacientes.clear();
        File file = new File(ARCHIVO);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(",");
                if (d.length == 6) {
                    pacientes.add(new Paciente(d[0], d[1], Integer.parseInt(d[2]), d[3], d[4], d[5]));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void guardarEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (Paciente p : pacientes) {
                pw.println(p.getCurp() + "," + p.getNombre() + "," + p.getEdad() + "," +
                        p.getTelefono() + "," + p.getAlergias() + "," + p.getEstatus());
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void agregar(Paciente p) {
        pacientes.add(p);
        guardarEnArchivo();
    }

    public List<Paciente> getLista() {
        return pacientes;
    }

    public void inactivar(int index) {
        if (index >= 0 && index < pacientes.size()) {
            pacientes.get(index).setEstatus("INACTIVO");
            guardarEnArchivo();
        }
    }
}