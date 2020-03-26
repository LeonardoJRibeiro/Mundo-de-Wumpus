/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import sun.awt.image.ToolkitImage;

/**
 *
 * @author leona
 */
public class Tela extends javax.swing.JFrame {
    private boolean pausa = false;
    private BufferedImage buffer;
    private BufferedImage iconeGuerreiro;
    private Graphics graficoGuerreiro;
    private Graphics grafico;
    private Labirinto mapa;
    private int salasHorizontais;
    private int salasVerticais;
    private int larguraPorta;
    private int comprimentoPorta;
    private int larguraSala;
    private int comprimentoSala;
    private int distanciaEntreSalas;
    private int metadeLarguraPorta;
    private int metadeComprimentoPorta;
    private int metadeLarguraSala;
    private int metadeComprimentoSala;
    private int metadeDistanciaEntreSalas;

    private int coordenadasGuerreiroX;
    private int coordenadasGuerreiroY;
    
    public void desenharGuerreiro(){
        iconeGuerreiro = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
        graficoGuerreiro = iconeGuerreiro.createGraphics();
        graficoGuerreiro.setColor(Color.white);
        graficoGuerreiro.fillRect(0, 0, 64, 64);
        graficoGuerreiro.setColor(Color.BLACK);
        graficoGuerreiro.drawOval(24, 8, 16, 16);//desenha a cabeça
        graficoGuerreiro.drawLine(32, 24, 32, 36);//desenha a coluna
        graficoGuerreiro.drawLine(33, 24, 24, 32);//desenha o braco esquerdo
        graficoGuerreiro.drawLine(33, 24, 40, 32);//desenha o braco direito
        graficoGuerreiro.drawLine(32, 36, 24, 48);//desenha a perna esquerda
        graficoGuerreiro.drawLine(32, 36, 40, 48);//desenha a perna esquerda
    }
    
    public void fimDeJogo(){
        lblFimDeJogo.setText("Fim de Jogo!!!");
    }
    
    public ImageIcon desenhar(){
        
        iniciarDesenho();
        //desenha as salas
        for(int y = 0; y < salasVerticais; y++){
            for(int x = 0; x < salasHorizontais; x++){
                grafico.drawRect(x*(larguraSala+distanciaEntreSalas),y*(comprimentoSala+distanciaEntreSalas), larguraSala , comprimentoSala);
                desenharAgentesSala(x, y);
            }
        }
        desenharPortasHorizontais();
        desenharPortasVerticais();
        
        return new ImageIcon(buffer);
    }
    
    public void iniciarDesenho(){
        buffer = new BufferedImage(((comprimentoSala+distanciaEntreSalas)*salasHorizontais), (larguraSala+distanciaEntreSalas)*salasVerticais, BufferedImage.TYPE_INT_RGB);
        grafico = buffer.createGraphics();
        grafico.setColor(Color.WHITE);
        grafico.fillRect(0, 0, (comprimentoSala+distanciaEntreSalas)*salasHorizontais, (larguraSala+distanciaEntreSalas)*salasVerticais);
        grafico.setColor(Color.BLACK);
    }
    
    public void desenharAgentesSala(int x, int y){
        switch (mapa.getSalas()[x][y].getAgente()){
            case MONSTRO:{
                grafico.drawString("Monstro", (x*(larguraSala+distanciaEntreSalas))+metadeLarguraSala/2,((salasVerticais-y-1)*(comprimentoSala+distanciaEntreSalas))+metadeComprimentoSala);
                break;
            }
            case OURO:{
                grafico.drawString("Ouro", (x*(larguraSala+distanciaEntreSalas))+metadeLarguraSala/2,((salasVerticais-y-1)*(comprimentoSala+distanciaEntreSalas))+metadeComprimentoSala);
                break;
            }
            case BURACO:{
                grafico.drawString("Buraco", (x*(larguraSala+distanciaEntreSalas))+metadeLarguraSala/2,((salasVerticais-y-1)*(comprimentoSala+distanciaEntreSalas))+metadeComprimentoSala);
                break;
            }
        }
        if(coordenadasGuerreiroX == x && coordenadasGuerreiroY == y){
            //grafico.drawString("Guerreiro", (x*(larguraSala+distanciaEntreSalas))+metadeLarguraSala/2,((salasVerticais-y-1)*(comprimentoSala+distanciaEntreSalas))+metadeComprimentoSala);
            grafico.drawImage(iconeGuerreiro, (x*(larguraSala+distanciaEntreSalas))+5,((salasVerticais-y-1)*(comprimentoSala+distanciaEntreSalas))+5,64 ,64 , null);
            //grafico.setColor(Color.green);
            //grafico.fillRect((x*(larguraSala+distanciaEntreSalas))+2,((salasVerticais-y-1)*(comprimentoSala+distanciaEntreSalas))+2, 10, 10);
            //grafico.setColor(Color.black);
        }
    }
    
    public void desenharPortasVerticais(){
        for(int y=0; y<salasVerticais-1; y++){
            for(int x=0; x<salasHorizontais; x++){
                grafico.setColor(Color.gray);
                grafico.fillRoundRect(metadeLarguraSala-metadeLarguraPorta+(x*(larguraSala+distanciaEntreSalas)), comprimentoSala+metadeDistanciaEntreSalas-metadeComprimentoPorta+(y*(comprimentoSala+distanciaEntreSalas)), larguraPorta, comprimentoPorta, 10 , 10);
                grafico.setColor(Color.BLACK);
                grafico.drawRoundRect(metadeLarguraSala-metadeLarguraPorta+(x*(larguraSala+distanciaEntreSalas)), comprimentoSala+metadeDistanciaEntreSalas-metadeComprimentoPorta+(y*(comprimentoSala+distanciaEntreSalas)), larguraPorta, comprimentoPorta, 10 , 10);
            }
        }
    }
    
    public void desenharPortasHorizontais(){
        for(int y=0; y<salasVerticais; y++){
            for(int x=0; x<salasHorizontais-1; x++){
                grafico.setColor(Color.gray);
                grafico.fillRoundRect(larguraSala+metadeDistanciaEntreSalas-metadeComprimentoPorta+(x*(larguraSala+distanciaEntreSalas)),metadeComprimentoSala-metadeLarguraPorta+(y*(comprimentoSala+distanciaEntreSalas)) , comprimentoPorta, larguraPorta, 10 , 10);
                grafico.setColor(Color.black);
                grafico.drawRoundRect(larguraSala+metadeDistanciaEntreSalas-metadeComprimentoPorta+(x*(larguraSala+distanciaEntreSalas)),metadeComprimentoSala-metadeLarguraPorta+(y*(comprimentoSala+distanciaEntreSalas)) , comprimentoPorta, larguraPorta, 10 , 10);
            }
        }
    }
    
    public void atualizaGuerreiro(int coordenadasX, int coordenadasY){
        coordenadasGuerreiroX = coordenadasX;
        coordenadasGuerreiroY = coordenadasY;
        labirinto.setIcon(desenhar());
    }
    
    public void atualizaVidas(int vidas, int totalVidas){
        lblVidas.setText("Vidas: "+String.valueOf(vidas));
        this.vidas.setValue((vidas*100)/totalVidas);
    }
    
    public void movimentosPossiveis(ArrayList<Movimento> movimentos){
        txtMovimentosPossiveis.setText("Movimentos Possiveis\n");
        movimentos.forEach((t) -> {
            txtMovimentosPossiveis.setText(txtMovimentosPossiveis.getText()+t+"\n");
        });
    }
    public void movimentosRecomendados(ArrayList<Movimento> movimentos){
        txtMovimentosRecomendados.setText("Movimentos recomendados\n");
        movimentos.forEach((t) -> {
            txtMovimentosRecomendados.setText(txtMovimentosRecomendados.getText()+t+"\n");
        });
    }
    
    public boolean pausa(){
        return pausa;
    }
    
    public void pausar(){
        pausa = !pausa;
    }

    
    /**
     * Creates new form Gu
     */
    public Tela(Labirinto mapa) {
        larguraPorta = 16;
        comprimentoPorta = 24;
        larguraSala = 80;
        comprimentoSala = 80;
        distanciaEntreSalas = 8;
        metadeLarguraPorta = larguraPorta/2;
        metadeComprimentoPorta = comprimentoPorta/2;
        metadeLarguraSala = larguraSala/2;
        metadeComprimentoSala = comprimentoSala/2;
        metadeDistanciaEntreSalas = distanciaEntreSalas/2;
        this.mapa = mapa;
        salasHorizontais = mapa.tamanhoX;
        salasVerticais = mapa.tamanhoY;
        initComponents();
        setVisible(true);
        setLocationRelativeTo(null);
        labirinto.setIcon(desenhar());
        desenharGuerreiro();
    }

    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        vidas = new javax.swing.JProgressBar();
        labirinto = new javax.swing.JLabel();
        lblVidas = new javax.swing.JLabel();
        txtMovimentosPossiveis = new javax.swing.JTextArea();
        txtMovimentosRecomendados = new javax.swing.JTextArea();
        lblFimDeJogo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 600));

        labirinto.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        labirinto.setFocusable(false);
        labirinto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        txtMovimentosPossiveis.setEditable(false);
        txtMovimentosPossiveis.setColumns(20);
        txtMovimentosPossiveis.setRows(5);

        txtMovimentosRecomendados.setColumns(20);
        txtMovimentosRecomendados.setRows(5);

        lblFimDeJogo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(vidas, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblVidas, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 462, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labirinto, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lblFimDeJogo, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMovimentosRecomendados, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                            .addComponent(txtMovimentosPossiveis, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtMovimentosRecomendados, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtMovimentosPossiveis, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(labirinto, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblFimDeJogo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(lblVidas, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(vidas, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labirinto;
    private javax.swing.JLabel lblFimDeJogo;
    private javax.swing.JLabel lblVidas;
    private javax.swing.JTextArea txtMovimentosPossiveis;
    private javax.swing.JTextArea txtMovimentosRecomendados;
    private javax.swing.JProgressBar vidas;
    // End of variables declaration//GEN-END:variables
}
