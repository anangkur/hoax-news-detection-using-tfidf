/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Utils.Model;
import View.ViewHasilKlasifikasi;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.lang.Object;

/**
 *
 * @author Marwah
 */
public class ControllerHasilKlasifikasi implements ActionListener{
    
    private ViewHasilKlasifikasi hasilklasifikasi;
    private Model model;
    
    private String listBeritaAsli; 
    private String listPunctuation; 
    private String[] listTokenisasi; 
    private ArrayList<String> listStopword; 
    private ArrayList<String> listLemma; 
    private ArrayList<String> listHasil;
    
    private String hasilJST; 
    private String hasilSVM; 
    private String hasilBayes; 
    private String hasilKNN;
    
    private ArrayList<String> hasilTfIdf;
    private ArrayList<String> kolom;

    public ControllerHasilKlasifikasi(Model model, String listBeritaAsli, String listPunctuation, String[] listTokenisasi, ArrayList<String> listStopword, ArrayList<String> listLemma, ArrayList<String> listHasil, String hasilJST, String hasilSVM, String hasilBayes, String hasilKNN, ArrayList<String> hasilTfIdf, ArrayList<String> kolom) {
        this.model = model;
        hasilklasifikasi = new ViewHasilKlasifikasi();
        
        this.listBeritaAsli = listBeritaAsli;
        this.listHasil = listHasil;
        this.listLemma = listLemma;
        this.listPunctuation = listPunctuation;
        this.listStopword = listStopword;
        this.listTokenisasi = listTokenisasi;
        
        this.hasilKNN = hasilKNN;
        this.hasilJST = hasilJST;
        this.hasilSVM = hasilSVM;
        this.hasilBayes = hasilBayes;
        
        this.hasilTfIdf = hasilTfIdf;
        
        hasilklasifikasi.setVisible(true);
        hasilklasifikasi.addListener(this);
        
        try{
            hasilklasifikasi.getTxt_hasil_jst().setText(hasilJST);
            
            Object[][] isiTabel = new Object[this.hasilTfIdf.size()][3];
            
            for (int i = 0; i<this.hasilTfIdf.size(); i++){
                System.out.println("i: "+String.valueOf(i));
                isiTabel[i][0] = i+1;
                isiTabel[i][1] = kolom.get(i);
                isiTabel[i][2] = this.hasilTfIdf.get(i);
            }
            
            String[] titletable = new String[3];
            titletable[0] = "No";
            titletable[1] = "Kata";
            titletable[2] = "Tf-Idf";
            
            hasilklasifikasi.getTable_tfidf().setModel(new javax.swing.table.DefaultTableModel(
                    isiTabel, titletable
            ));
            
            hasilklasifikasi.getScroll_table().setViewportView(hasilklasifikasi.getTable_tfidf());
            hasilklasifikasi.getContentPane().add(hasilklasifikasi.getScroll_table());
            hasilklasifikasi.pack();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
    
        Object source = ae.getSource();
        if (source.equals(hasilklasifikasi.getBtn_Tampilkan_textPreProcessing())){
            
            hasilklasifikasi.dispose();
            new ControllerPreProcessing(model, listBeritaAsli, listPunctuation, listTokenisasi, listStopword, listLemma, listHasil);
        
        }
    }    
}
