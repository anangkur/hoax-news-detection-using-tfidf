/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Utils.Model;
import View.ViewInputBeritaGUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import static Main.Ta.loadStopWord;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Marwah
 */
public class ControllerInputBeritaGUI implements ActionListener{

    private ViewInputBeritaGUI inputberitagui;
    private Model model;
    
    private ArrayList<String> kolom = new ArrayList<>();
    private ArrayList<Double> feature = new ArrayList<>();
    private ArrayList<ArrayList<Double>> baris = new ArrayList<>();
    private ArrayList<ArrayList<String>> kamusKata = new ArrayList<>();
    
    private ArrayList<String> listStopWord = new ArrayList<>();
    private Set<String> kamusKataDasar = new HashSet<>();
    private String hasilJST, hasilSVM, hasilKNN, hasilBayes;
    
    private String listBeritaAsli; 
    private String listPunctuation; 
    private String[] listTokenisasi; 
    private ArrayList<String> listStopword; 
    private ArrayList<String> listLemma; 
    private ArrayList<String> listHasil; 
    

    public ControllerInputBeritaGUI(Model model) {
        this.model = model;
        inputberitagui = new ViewInputBeritaGUI();
        
        inputberitagui.setVisible(true);
        inputberitagui.addListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
       
        Object source = ae.getSource();
        
        if (source.equals(inputberitagui.getBtn_input_berita())){
            String namaFile = model.PickAFile();
            inputberitagui.getTxt_nama_file_berita().setText(namaFile);
        }
        
        if (source.equals(inputberitagui.getBtn_cek_berita())){
            
            try{
                listStopWord = model.loadStopWord("Dataset/id.stopwords.02.01.2016.txt");
                kamusKataDasar = model.loadKamusKataDasar();
                
                File file = new File(inputberitagui.getTxt_nama_file_berita().getText());            
                
                model.preProcessingDataTrain("Dataset/Datatrain", listStopWord, kamusKataDasar);
                model.preProcessingDataTest("Dataset/Datatest", listStopWord, kamusKataDasar);
                kamusKata = model.kamusKata;
                kolom = model.kolom;
                
//                ArrayList<String> hasilPreProcessingTest = model.preProcessing(file, listStopWord, kamusKataDasar);

                //get berita asli
                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);        
                Element node = document.getDocumentElement();
                node.normalize();
                listBeritaAsli = node.getTextContent();
                
                //get hasil remove puntuation
                listPunctuation = model.removePunctuation(listBeritaAsli);
                
                //get hasil tokenisasi
                listTokenisasi = model.tokenisasi(listPunctuation);
                
                //get hasil remove stopword
                listStopword = model.hapusStopWord(listTokenisasi, listStopWord);
                
                //get hasil lemmatisasi
                listLemma = model.lemmatisasiSastrawi(listStopword, kamusKataDasar);
                
                //get list hasil
                listHasil = listLemma;
                
                kamusKata.add(listHasil);
                
                for (String s : listHasil){
                    if (!kolom.contains(s)){
                        kolom.add(s);
                    }
                }
                
                feature = model.hitungTfIdf(kolom, kamusKata);
                model.createFileTesting(kolom, feature);
                
                ArrayList<String> hasilTfIdf = new ArrayList<>();
                for (Double d : feature ){
                    // Apply formatting to the string if necessary
                    hasilTfIdf.add(d.toString());
                }
                
                hasilBayes = model.predictionBayes();
                System.out.println("hasil bayes: "+hasilBayes);
                hasilJST = model.predictionJST();
                System.out.println("hasil jst: "+hasilJST);
                hasilKNN = model.predictionKNN();
                System.out.println("hasil knn: "+hasilKNN);
                hasilSVM = model.predictionSVM();
                System.out.println("hasil svm: "+hasilSVM);
                
                inputberitagui.dispose();
                new ControllerHasilKlasifikasi(model, listBeritaAsli, listPunctuation, listTokenisasi, listStopword, listLemma, listHasil, hasilJST, hasilSVM, hasilBayes, hasilKNN, hasilTfIdf);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
