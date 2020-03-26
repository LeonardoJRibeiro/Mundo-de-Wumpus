/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia;

/**
 *
 * @author leona
 */
public class Guerreiro {
    private int vidas;
    private int pontuacao;
    
    public void perdeVidas(int vidas){
        this.vidas-=vidas;
    }
    
    public void  ganhaVidas(int vidas){
        this.vidas+=vidas;
    }
    
    public void perdePontuacao(int pontuacao){
        this.pontuacao-=pontuacao;
    }
    
    public void  ganhaPontuacao(int pontuacao){
        this.pontuacao+=pontuacao;
    }
    
    public boolean estaVivo(){
        return this.vidas > 0;
    }
    
    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }
    
}
