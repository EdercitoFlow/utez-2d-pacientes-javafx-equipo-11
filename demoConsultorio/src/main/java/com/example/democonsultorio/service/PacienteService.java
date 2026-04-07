package com.example.democonsultorio.service;

import com.example.democonsultorio.model.Paciente;

import java.util.ArrayList;
import java.util.List;

public class PacienteService {

    private List<Paciente> pacientes = new ArrayList<>();
    private int contadorId = 1;


    public Paciente agregar(Paciente paciente) {
        paciente.setId(contadorId++);
        pacientes.add(paciente);
        return paciente;
    }


    public List<Paciente> obtenerTodos() {
        return pacientes;
    }


    public Paciente buscarPorId(int id) {
        for (Paciente p : pacientes) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }


    public boolean actualizar(Paciente paciente) {
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getId() == paciente.getId()) {
                pacientes.set(i, paciente);
                return true;
            }
        }
        return false;
    }


    public boolean eliminar(int id) {
        for (Paciente p : pacientes) {
            if (p.getId() == id) {
                pacientes.remove(p);
                return true;
            }
        }
        return false;
    }
}