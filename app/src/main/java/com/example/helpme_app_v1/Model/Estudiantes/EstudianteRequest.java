

package com.example.helpme_app_v1.Model.Estudiantes;

import com.example.helpme_app_v1.Model.Estudiante;
import com.example.helpme_app_v1.Model.Persona;
import com.example.helpme_app_v1.Model.Usuario;

public class EstudianteRequest  {
    private Usuario usuario;
    private Persona persona;
    private Estudiante estudiante;
    private String[] intereses;

    public String[] getIntereses() {
        return intereses;
    }

    public void setIntereses(String[] intereses) {
        this.intereses = intereses;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


}
