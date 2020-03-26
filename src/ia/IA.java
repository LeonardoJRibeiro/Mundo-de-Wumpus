/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leona
 */
public class IA {
    private final int yMax;
    private final int xMax;
    private int vidas = 5;
    private int delay = 300;
    private boolean voltando;
    private boolean naoEhPossivelAvancarComSeguranca = false;
    private Guerreiro guerreiro;
    private Labirinto labirinto;
    private Tela tela;
    private Sala salaAtual;
    private Movimento ultimoMovimento;
    private final Stack<Movimento> historicoMovimentosAdiante = new Stack<>();
    private boolean [][] salasConhecidas;
    
    private ArrayList<Integer []> salasComMonstro = new ArrayList<>();
    private ArrayList<Integer []> salasComBuraco = new ArrayList<>();

    public IA() {
        this.yMax = 5;
        this.xMax = 6;
        this.salasConhecidas = new boolean [xMax][xMax];
        inicializaSalasConhecidas();
    }
    
    public void inicializaSalasConhecidas(){
        for(int i = 0; i < yMax; i++){
            for(int j = 0; j < xMax; j++){
                salasConhecidas[j][i] = false;
            }
        }
    }
    
    /**
     *
     */
    public void inicio(){
        labirinto = new Labirinto(xMax, yMax);
        guerreiro = new Guerreiro();
        tela = new Tela(labirinto);
        guerreiro.setVidas(vidas);
        guerreiro.setPontuacao(100);
        salaAtual = labirinto.getPrimeiraSala();
        while(guerreiro.estaVivo()  && !encontrouOuro()){
            tela.atualizaGuerreiro(salaAtual.getCoordenadasX(), salaAtual.getCoordenadasY());
            tela.atualizaVidas(guerreiro.getVidas(), vidas);
            //if(tela.pausa()){ 
                conheceSala();
                if(encontrouBuraco()){
                    salasComBuraco.add(new Integer[]{salaAtual.getCoordenadasX(), salaAtual.getCoordenadasY()});
                    guerreiro.perdeVidas(1);
                    historicoMovimentosAdiante.clear();//limpa a pilha de movimentos
                    salaAtual = labirinto.getPrimeiraSala();//coloca o guerreiro na primeira sala
                    delay(delay);
                }
                else if(encontrouMonstro()){
                    salasComMonstro.add(new Integer[]{salaAtual.getCoordenadasX(), salaAtual.getCoordenadasY()});
                    guerreiro.perdeVidas(1);
                    historicoMovimentosAdiante.clear();//limpa a pilha de movimentos
                    salaAtual = labirinto.getPrimeiraSala();//coloca o guerreiro na primeira sala
                    delay(delay);
                }
                else{
                    Movimento movimento = escolherDirecao();
                    salaAtual = labirinto.getProximaSala(movimento, salaAtual);
                    salvarMovimentos(movimento);
                    delay(delay);
                } 
            //}
        }
        tela.atualizaGuerreiro(salaAtual.getCoordenadasX(), salaAtual.getCoordenadasY());
        tela.atualizaVidas(guerreiro.getVidas(), vidas);
        tela.fimDeJogo();
        System.out.println("Fim de Jogo!");
    }
    
    public boolean encontrouBuraco(){
        return salaAtual.getAgente() == AgenteSala.BURACO;
    }
    
    public boolean encontrouMonstro(){
        return salaAtual.getAgente() == AgenteSala.MONSTRO;
    }
    
    public boolean encontrouOuro(){
        return salaAtual.getAgente() == AgenteSala.OURO;
    }
    
    public boolean salaSegura(){
        return (!salaAtual.getCaracteristicaSala().contains(CaracteristicaSala.BRISA) && !salaAtual.getCaracteristicaSala().contains(CaracteristicaSala.RUIDO));
    }
    
    public void conheceSala(){
        salasConhecidas[salaAtual.getCoordenadasX()][salaAtual.getCoordenadasY()]=true;
    }
    
    
    public void salvarMovimentos(Movimento movimento){
        ultimoMovimento = movimento;
        if(!voltando){
            salvaMovimentosAdiante();
        }
    }
    
    /**
     * Esse método cria um delay em milissegundos.
     * @param tempoMillissegundos tempo do delay em milissegundos.
     */
    public void delay(int tempoMillissegundos){
        try {
            Thread.sleep(tempoMillissegundos);
        } catch (InterruptedException ex) {
            Logger.getLogger(Guerreiro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Esse método salva o ultimo movimento quando ele é feito adiante, na pilha de movimentos realizados.
     */
    public void salvaMovimentosAdiante(){
        if(!voltando){
            historicoMovimentosAdiante.push(ultimoMovimento);
        }
    }
    
    /**
     * Esse método retorna um movimento contrário ao último feito.
     * Por exemplo, se o ultimo movimento foi CIMA ele retornará ABAIXO.
     * 
     * @return um movimento contrário ao último feito.
     */
    public Movimento voltar(){
        if(historicoMovimentosAdiante.size() >= 1){//se a pilha conter mais de um elemento, ainda será possível voltar para trás
            return inverteMovimento(historicoMovimentosAdiante.pop());
        }
        return null;
    }
    
    /**
     * Esse método é o responsável por fazer a escolha do movimento do guerreiro.
     * @return um movimento escolhido.
     */
    public Movimento escolherDirecao(){
        Movimento movimentoVoltar;
        ArrayList<Movimento> movimento = new ArrayList<>();
        
        movimentosPossiveis().forEach(movimentos->{
            movimento.add(movimentos); //adiciona os movimentos possíveis na lista
        });

        tela.movimentosPossiveis(movimento);//retorna os movimentos possiveis para a tela
        
        if(ultimoMovimento != null){//se existe ultimo movimento
            movimento.remove(inverteMovimento(ultimoMovimento));//não deixa o guerreiro voltar pra ultima sala que ele esteve
        }
        
        movimentosQueLevaraoAUmaSalaConhecida().forEach(sala->{//não deixa o guerreiro ir pra uma sala conhecida
            movimento.remove(sala);
        });
        
        movimentosQueLevaraoAUmaSalaComBuraco().forEach(movimentos->{ //não deixa o guerreiro ir para uma sala com buraco
            movimento.remove(movimentos);
        });
        
        movimentosQueLevaraoAUmaSalaComMonstro().forEach(movimentos->{// não deixa o guerreiro ir para uma sala com monstro
            movimento.remove(movimentos);
        });
        
        voltando = false;
        if(salaAtual.getCaracteristicaSala().contains(CaracteristicaSala.RUIDO)){//se a na sala tem ruido
            if(!naoEhPossivelAvancarComSeguranca){//se é possivel avançar com segurança
                movimento.clear();
            }
        }
        if(salaAtual.getCaracteristicaSala().contains(CaracteristicaSala.BRISA)){// se na sala tem brisa
            if(!naoEhPossivelAvancarComSeguranca){// se é possível avançar com segurança
                movimento.clear();
            }
        }
        tela.movimentosRecomendados(movimento);//retorna os movimentos recomendados para a tela
        if(movimento.isEmpty()){
            movimentoVoltar = voltar();
            if(movimentoVoltar != null){ // se a pilha de movimentos não está vazia
                voltando = true;
                movimento.add(movimentoVoltar);
            }
            else{
               voltando = false;
               naoEhPossivelAvancarComSeguranca = true;
               inicializaSalasConhecidas();//zera as salas conhecidas
               movimento.add(escolherDirecao());
            }
        }
        return movimento.get(new Random().nextInt(movimento.size()));//retorna um movimento aleatório entre os movimentos definidos
    }
    
    public Movimento inverteMovimento(Movimento movimento){
        switch(movimento){
            case ABAIXO:{
                return Movimento.CIMA;
            }
            case CIMA:{
                return Movimento.ABAIXO;
            }
            case DIREITA:{
                return Movimento.ESQUERDA;
            }
            case ESQUERDA:{
                return Movimento.DIREITA;
            }
            default:{
                return null;
            }
        }
    }
    
    public ArrayList<Movimento> movimentosQueLevaraoAUmaSalaConhecida(){
        ArrayList<Movimento> movimentos = new ArrayList<>();
        if(salaAtual.getCoordenadasX() < xMax-1){
            if(salasConhecidas[salaAtual.getCoordenadasX()+1][salaAtual.getCoordenadasY()]){
                movimentos.add(Movimento.DIREITA);
            }
        }
        if(salaAtual.getCoordenadasX() > 0){
            if(salasConhecidas[salaAtual.getCoordenadasX()-1][salaAtual.getCoordenadasY()]){
                movimentos.add(Movimento.ESQUERDA);
            }
        }
        if(salaAtual.getCoordenadasY() < yMax-1){
            if(salasConhecidas[salaAtual.getCoordenadasX()][salaAtual.getCoordenadasY()+1]){
                movimentos.add(Movimento.CIMA);
            }
        }
        if(salaAtual.getCoordenadasY() > 0){
            if(salasConhecidas[salaAtual.getCoordenadasX()][salaAtual.getCoordenadasY()-1]){
                movimentos.add(Movimento.ABAIXO);
            }
        }
        return movimentos;
    }
    
    public ArrayList<Movimento> movimentosQueLevaraoAUmaSalaComBuraco(){
        ArrayList<Movimento> movimentos = new ArrayList<>();
        salasComBuraco.forEach(coordenadas ->{
            if(coordenadas[0] == salaAtual.getCoordenadasX()+1 && coordenadas[1] == salaAtual.getCoordenadasY()){//se é igual as coordenadas da sala direita
                movimentos.add(Movimento.DIREITA);
            }
            if(coordenadas[0] == salaAtual.getCoordenadasX()-1 && coordenadas[1] == salaAtual.getCoordenadasY()){//se é igual as coordenadas da sala esquerda
                movimentos.add(Movimento.ESQUERDA);
            }
            if(coordenadas[0] == salaAtual.getCoordenadasX() && coordenadas[1] == salaAtual.getCoordenadasY()+1){//se é igual as coordenadas da sala acima
                movimentos.add(Movimento.CIMA);
            }
            if(coordenadas[0] == salaAtual.getCoordenadasX() && coordenadas[1] == salaAtual.getCoordenadasY()-1){//se é igual as coordenadas da sala abaixo
                movimentos.add(Movimento.ABAIXO);
            }
        });
        return movimentos;
    }
    
    public ArrayList<Movimento> movimentosQueLevaraoAUmaSalaComMonstro(){
        ArrayList<Movimento> movimentos = new ArrayList<>();
        salasComMonstro.forEach(coordenadas ->{
            if(coordenadas[0] == salaAtual.getCoordenadasX()+1 && coordenadas[1] == salaAtual.getCoordenadasY()){//se é igual as coordenadas da sala direita
                movimentos.add(Movimento.DIREITA);
            }
            if(coordenadas[0] == salaAtual.getCoordenadasX()-1 && coordenadas[1] == salaAtual.getCoordenadasY()){//se é igual as coordenadas da sala esquerda
                movimentos.add(Movimento.ESQUERDA);
            }
            if(coordenadas[0] == salaAtual.getCoordenadasX() && coordenadas[1] == salaAtual.getCoordenadasY()+1){//se é igual as coordenadas da sala acima
                movimentos.add(Movimento.CIMA);
            }
            if(coordenadas[0] == salaAtual.getCoordenadasX() && coordenadas[1] == salaAtual.getCoordenadasY()-1){//se é igual as coordenadas da sala abaixo
                movimentos.add(Movimento.ABAIXO);
            }
        });
        return movimentos;
    }
    /**
     *
     * @return um ArrayList contendo todos os movimentos possíveis para a sala atual
     */
    public ArrayList<Movimento> movimentosPossiveis(){
        ArrayList<Movimento> movimentosPossiveis = new ArrayList<>();
        if(possivelIrPraAcima()){
            movimentosPossiveis.add(Movimento.CIMA);
        }
        if(possivelIrPraAbaixo()){
            movimentosPossiveis.add(Movimento.ABAIXO);
        }
        if(possivelIrPraDireita()){
            movimentosPossiveis.add(Movimento.DIREITA);
        }
        if(possivelIrPraEsquerda()){
            movimentosPossiveis.add(Movimento.ESQUERDA);
        }
        return movimentosPossiveis;
    }
    
    /**
     * Esse método é responsável por verificar se é possível ir para cima.
     * @return true se é possivel ir para cima.
     */
    public boolean possivelIrPraAcima(){
        return salaAtual.getPortas().contains(Porta.ACIMA);
    }
    
    /**
     * Esse método é responsável por verificar se é possível ir para baixo.
     * @return true se é possivel ir para baixo.
     */
    public boolean possivelIrPraAbaixo(){
        return salaAtual.getPortas().contains(Porta.ABAIXO);
    }
    
    /**
     * Esse método é responsável por verificar se é possível ir para a direita.
     * @return true se é possivel ir para a direita.
     */
    public boolean possivelIrPraDireita(){
        return salaAtual.getPortas().contains(Porta.DIREITA);
    }
    
    /**
     * Esse método é responsável por verificar se é possível ir para a esquerda.
     * @return true se é possivel ir para a esquerda.
     */
    public boolean possivelIrPraEsquerda(){
        return salaAtual.getPortas().contains(Porta.ESQUERDA);
    }
    
    public static void main(String[] args){
        new IA().inicio();
    }
}
