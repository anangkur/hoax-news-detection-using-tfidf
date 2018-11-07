/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Utils.Model;
import View.ViewPreProcessing;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 *
 * @author Marwah
 */
public class ControllerPreProcessing implements ActionListener{

    private ViewPreProcessing preprocessing;
    private Model model;
    
    private String listBeritaAsli; 
    private String listPunctuation; 
    private String[] listTokenisasi; 
    private ArrayList<String> listStopword; 
    private ArrayList<String> listLemma; 
    private ArrayList<String> listHasil; 

    public ControllerPreProcessing(Model model, String listBeritaAsli, String listPunctuation, String[] listTokenisasi, ArrayList<String> listStopword, ArrayList<String> listLemma, ArrayList<String> listHasil) {
        this.model = model;
        
        this.listBeritaAsli = listBeritaAsli;
        this.listHasil = listHasil;
        this.listLemma = listLemma;
        this.listPunctuation = listPunctuation;
        this.listStopword = listStopword;
        this.listTokenisasi = listTokenisasi;
        
        preprocessing = new ViewPreProcessing();
        
        preprocessing.setVisible(true);
        preprocessing.addListener(this);
        
        try{
            String[] arrayHasil = new String[1000000];
            int iHasil = 0;
            for (String hasil:listHasil){
                arrayHasil[iHasil] = hasil;
                iHasil++;
            }
            
            String[] arrayLemma = new String[1000000];
            int iLemma = 0;
            for (String lemma:listLemma){
                arrayLemma[iLemma] = lemma;
                iLemma++;
            }
            
            String[] arrayStopWord = new String[1000000];
            int iStopword = 0;
            for (String stopword:listStopword){
                arrayStopWord[iStopword] = stopword;
                iStopword++;
            }
            
            String[] arrayTokenisasi = new String[1000000];
            int iTokenisasi = 0;
            for (String tokenisasi:listTokenisasi){
                arrayTokenisasi[iTokenisasi] = tokenisasi;
                iTokenisasi++;
            }
            preprocessing.getTxt_berita_asli().setText(listBeritaAsli);
            preprocessing.getList_hasil().setListData(arrayHasil);
            preprocessing.getList_lemma().setListData(arrayLemma);
            preprocessing.getTxt_punctuation().setText(listPunctuation);
            preprocessing.getList_stopword().setListData(arrayStopWord);
            preprocessing.getList_tokenisasi().setListData(arrayTokenisasi);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        
        Object source = ae.getSource();
        if (source.equals(preprocessing.getBtn_cek_berita_lagi())){
            preprocessing.dispose();
            new ControllerInputBeritaGUI(model);
        }
        
        if (source.equals(preprocessing.getBtn_keluar())){
            preprocessing.dispose();
        } 
    }
    
}
