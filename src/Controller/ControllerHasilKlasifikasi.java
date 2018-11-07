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

    public ControllerHasilKlasifikasi(Model model, String listBeritaAsli, String listPunctuation, String[] listTokenisasi, ArrayList<String> listStopword, ArrayList<String> listLemma, ArrayList<String> listHasil, String hasilJST, String hasilSVM, String hasilBayes, String hasilKNN, ArrayList<String> hasilTfIdf) {
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
            String[] arrayTfIdf = new String[1000000];
            int i = 0;
            for (String tfIdf:hasilTfIdf){
                arrayTfIdf[i] = tfIdf;
                i++;
            }
            hasilklasifikasi.getList_hasil_tfidf().setListData(arrayTfIdf);
            hasilklasifikasi.getTxt_hasil_bayes().setText(hasilBayes);
            hasilklasifikasi.getTxt_hasil_jst().setText(hasilJST);
            hasilklasifikasi.getTxt_hasil_knn().setText(hasilKNN);
            hasilklasifikasi.getTxt_hasil_svm().setText(hasilSVM);
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
