/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author leona
 */
public class Labirinto {
    int tamanhoX;
    int tamanhoY;
    Sala [][] salas;
    ArrayList<Coordenada> listaSalas;
    ArrayList<Coordenada> listaSalaVazia;
    ArrayList<Coordenada> listaSalaComMonstro;
    ArrayList<Coordenada> listaSalaComBuraco;
    ArrayList<Coordenada> listaSalaComOuro;
    ArrayList<Coordenada> listaPrimeiraSala;
    
    
    /**
     *
     * @param tamanhoX cumprimento do labirinto
     * @param tamanhoY largura do labirinto
     */
    public Labirinto(int tamanhoX, int tamanhoY){
        this.listaSalaComBuraco = new ArrayList<>();
        this.listaSalaComOuro = new ArrayList<>();
        this.listaSalaComMonstro = new ArrayList<>();
        this.listaSalas = new ArrayList<>();
        this.listaSalaVazia = new ArrayList<>();
        this.listaPrimeiraSala = new ArrayList<>();
        salas = new Sala[tamanhoX][tamanhoY];
        this.tamanhoX = tamanhoX;
        this.tamanhoY = tamanhoY;
        inicializarSalas();
    }
    
    public boolean salaVazia(int coordenadaX, int coordenadaY){
        return salas[coordenadaX][coordenadaY].getAgente() == AgenteSala.NADA;
    }
    
    public Coordenada coordenadaSoltarGuerreiro(){
        listaPrimeiraSala.forEach(id->{
            listaSalas.remove(id);
        });
        Collections.shuffle(listaSalas);
        return listaSalas.get(0);
    }
    
    public Coordenada coordenadaAleatoria(){
        listaSalaComBuraco.forEach(id->{//remove os ids com buraco
            listaSalaVazia.remove(id);
        });
        
        listaSalaComMonstro.forEach(id->{//remove os ids com monstro
            listaSalaVazia.remove(id);
        });
        
        listaSalaComOuro.forEach(id->{//remove os ids com ouro
            listaSalaVazia.remove(id);
        });
        
        Collections.shuffle(listaSalaVazia);//sorteia 
        return listaSalaVazia.get(0);
    }
    
    public void soltarMonstro(){
        Coordenada coordenadaSoltarMonstro = coordenadaAleatoria();
        listaSalaComMonstro.add(coordenadaSoltarMonstro);
        salas[coordenadaSoltarMonstro.x][coordenadaSoltarMonstro.y].setAgente(AgenteSala.MONSTRO);
    }
    
    public void colocarBuraco(){
        Coordenada coordenadaColocarBuraco = coordenadaAleatoria();
        listaSalaComBuraco.add(coordenadaColocarBuraco);
        salas[coordenadaColocarBuraco.x][coordenadaColocarBuraco.y].setAgente(AgenteSala.BURACO);
    }
    
    public void colocarOuro(){
        Coordenada coordenadaColocarOuro = coordenadaAleatoria();
        listaSalaComOuro.add(coordenadaColocarOuro);
        salas[coordenadaColocarOuro.x][coordenadaColocarOuro.y].setAgente(AgenteSala.OURO);
    }
    
    public Sala getProximaSala(Movimento movimento, Sala salaAtual){
        switch (movimento){
            case CIMA:{
                return salas[salaAtual.getCoordenadasX()][salaAtual.getCoordenadasY()+1];
            }
            case DIREITA:{
                return salas[salaAtual.getCoordenadasX()+1][salaAtual.getCoordenadasY()];
            }
            case ABAIXO:{
                return salas[salaAtual.getCoordenadasX()][salaAtual.getCoordenadasY()-1];
            }
            case ESQUERDA:{
                return salas[salaAtual.getCoordenadasX()-1][salaAtual.getCoordenadasY()];
            }
            default:{
                return null;
            }
        }
    }
    
    public Sala getPrimeiraSala(){
        Coordenada coordenadaGuerreiro = coordenadaSoltarGuerreiro();
        listaPrimeiraSala.add(coordenadaGuerreiro);
        return salas[0][0];//aqui pode ser feito um sorteio de uma sala segura
    }
    
    /**
     *
     */
    public void inicializarSalas(){
        for(int y=0; y<tamanhoY; y++){
            for(int x=0; x<tamanhoX; x++){
                salas[x][y] = new Sala();
                salas[x][y].setCoordenadasX(x);
                salas[x][y].setCoordenadasY(y);
                listaSalaVazia.add(new Coordenada(x, y));
                listaSalas.add(new Coordenada(x, y));
                salas[x][y].setAgente(AgenteSala.NADA);
                setarPortasSala(x, y);
            }
        }
        //soltarMonstro();
        //colocarBuraco();
        //4soltarMonstro();
        colocarBuraco();
        soltarMonstro();
        colocarBuraco();
        colocarOuro();
        
        for(int y=0; y<tamanhoY; y++){
            for(int x=0; x<tamanhoX; x++){
                setarCaracteristicasSalasAdjacentes(new int [] {x,y}, salas[x][y].getAgente());
            }
        }
    }
    
    public void setarPortasSala(int x, int y){
        ArrayList<Porta> portas = new ArrayList<>();
        if(x > 0){
            portas.add(Porta.ESQUERDA);
        }
        if(x < tamanhoX - 1){
            portas.add(Porta.DIREITA);
        }
        if(y > 0){
            portas.add(Porta.ABAIXO);
        }
        if(y < tamanhoY -1){
            portas.add(Porta.ACIMA);
        }
        salas[x][y].setPortas(portas);
    }
    /**
     *
     * @param coordenadas cordenadas da sala atual
     * @param oQueHaNaSala agente existente na sala 0-monstro 1-buraco 2-ouro 3-parede
     */
    public void setarCaracteristicasSalasAdjacentes(int [] coordenadas, AgenteSala oQueHaNaSala){
        CaracteristicaSala caracteristicaSala = CaracteristicaSala.NADA;
        switch (oQueHaNaSala){
            case BURACO:{
                caracteristicaSala = CaracteristicaSala.BRISA;
                break;
            }
            case MONSTRO:{
                caracteristicaSala = CaracteristicaSala.RUIDO;
                break;
            }
        }
        if(coordenadas[0]>0){/* se as cordenadasx da sala atual for maior do que 0, 
                                a sala a esquerda será atiginda pela ação do agente que está nessa sala*/
            
            defineCaracteristicas(new int [] {coordenadas[0]-1,coordenadas[1]}, caracteristicaSala);
        }
        if(coordenadas[0]<tamanhoX-1){/*se as cordenadasx da sala atual for menor do que o tamanho do mapa, 
                                        a sala a direita será atiginda pela ação do agente que está nessa sala*/
            defineCaracteristicas(new int [] {coordenadas[0]+1,coordenadas[1]}, caracteristicaSala);
        }
        if(coordenadas[1]>0){/* se as cordenadasy da sala atual for maior do que 0, 
                                a sala abaixo será atiginda pela ação do agente que está nessa sala*/
            defineCaracteristicas(new int [] {coordenadas[0],coordenadas[1]-1}, caracteristicaSala);
        }
        if(coordenadas[1]<tamanhoY-1){/*se as cordenadasy da sala atual for menor do que otamanho do mapa, 
                                        a sala acima será atiginda pela ação do agente que está nessa sala*/
            defineCaracteristicas(new int [] {coordenadas[0],coordenadas[1]+1}, caracteristicaSala);
        }
    }
    
    /**
     *
     * @param coordenadas cordenadas da sala afetada
     * @param caracteristica
     */
    public void defineCaracteristicas(int[] coordenadas, CaracteristicaSala caracteristica){
        //copia as caracteristicas existentes em cada sala
        ArrayList<CaracteristicaSala> caracteristicasSala = salas[coordenadas[0]][coordenadas[1]].getCaracteristicaSala();
        if(!salas[coordenadas[0]][coordenadas[1]].getCaracteristicaSala().contains(caracteristica)){
            caracteristicasSala.add(caracteristica);
        }
        salas[coordenadas[0]][coordenadas[1]].setCaracteristicaSala(caracteristicasSala);
    }

    public int getTamanhoX() {
        return tamanhoX;
    }

    public void setTamanhoX(int tamanhoX) {
        this.tamanhoX = tamanhoX;
    }

    public int getTamanhoY() {
        return tamanhoY;
    }

    public void setTamanhoY(int tamanhoY) {
        this.tamanhoY = tamanhoY;
    }

    public Sala[][] getSalas() {
        return salas;
    }

    public void setSalas(Sala[][] salas) {
        this.salas = salas;
    }
    
}
