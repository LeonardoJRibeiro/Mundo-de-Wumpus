 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia;

import java.util.ArrayList;

/**
 *
 * @author leona
 */
public class Sala {
    private AgenteSala agente;
    private int coordenadasX;
    private int coordenadasY;
    private int idSala;
    private ArrayList<Porta> portas;
    private ArrayList<CaracteristicaSala> caracteristicaSala;
    
    public Sala(){
        caracteristicaSala = new ArrayList<>();
        portas = new ArrayList<>();
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public int getCoordenadasX() {
        return coordenadasX;
    }

    public void setCoordenadasX(int coordenadasX) {
        this.coordenadasX = coordenadasX;
    }
    
    public int getCoordenadasY() {
        return coordenadasY;
    }

    public void setCoordenadasY(int coordenadasY) {
        this.coordenadasY = coordenadasY;
    }
    
    public AgenteSala getAgente() {
        return agente;
    }

    public void setAgente(AgenteSala agente) {
        this.agente = agente;
    }

    public ArrayList<CaracteristicaSala> getCaracteristicaSala() {
        return caracteristicaSala;
    }

    public void setCaracteristicaSala(ArrayList<CaracteristicaSala> caracteristicaSala) {
        this.caracteristicaSala = caracteristicaSala;
    }

    public ArrayList<Porta> getPortas() {
        return portas;
    }

    public void setPortas(ArrayList<Porta> portas) {
        this.portas = portas;
    }

   
    
}
