package com.example.democonsultorio.service;

import com.example.democonsultorio.model.Paciente;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteService {
    private List<Paciente> pacientes = new ArrayList<>();
    private final String ARCHIVO = "pacientes.csv";

    //Trae los cambios más recientes
    public void cargarArchivo() {
        pacientes.clear();
        File file = new File(ARCHIVO);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) { //Lector de archivo
            String linea;
            while ((linea = br.readLine()) != null) { //Lee las lineas mientras haya que leer
                String[] d = linea.split(","); //Corta
                if (d.length == 6) {
                    pacientes.add(new Paciente(d[0], d[1], Integer.parseInt(d[2]), d[3], d[4], d[5]));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    //De&Wr linea por linea
    public void guardarEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (Paciente p : pacientes) {
                pw.println(p.getCurp() + "," + p.getNombre() + "," + p.getEdad() + "," +
                        p.getTelefono() + "," + p.getAlergias() + "," + p.getEstatus());
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    //Se agrega al ArrayList
    public void agregar(Paciente p) {
        pacientes.add(p);
        guardarEnArchivo();
    }
    //Llena la tabla principal
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