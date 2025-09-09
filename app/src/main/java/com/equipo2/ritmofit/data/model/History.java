package com.equipo2.ritmofit.data.model;

public class History {
    public int history_id;
    public String asistencia_fecha; // "2025-09-10T18:05:00.000Z" (ISO del back)

    // Datos de la clase
    public int id;             // class_id
    public String name;
    public String discipline;
    public String sede;
    public String fecha;       // yyyy-MM-dd
    public String hora;        // HH:mm:ss
    public int cupo;
    public String profesor;
    public int duracion;
}
