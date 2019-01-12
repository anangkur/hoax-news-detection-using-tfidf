/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Main.Ta;
import Utils.ArrfFileBuilder;
import Utils.FileDatabase;
import IndonesianStemmer.IndonesianStemmer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import jsastrawi.morphology.DefaultLemmatizer;
import jsastrawi.morphology.Lemmatizer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.Debug;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils;
//import ta.InstancesBuilder;

/**
 *
 * @author Marwah
 */
public class Model implements Serializable{
    
    private static ArrayList<String> listStopWord = new ArrayList<>();
    private static Set<String> kamusKataDasar = new HashSet<>();
    
    private static MultilayerPerceptron modelJST;
    private static NaiveBayes modelBayes;
    private static SMO modelSVM;
    private static IBk modelKNN;
    
    public static ArrayList<String> kolom = new ArrayList<>();
    private static ArrayList<Double> feature = new ArrayList<>();
    private static ArrayList<ArrayList<Double>> baris = new ArrayList<>();
    public static ArrayList<ArrayList<String>> kamusKata = new ArrayList<>();
    
    public Model() {
        
    }
    
    public String PickAFile() {
    
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Input File","xml");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " +chooser.getSelectedFile().getAbsolutePath());
        }
        
        return chooser.getSelectedFile().getAbsolutePath();
    }
  
    public ArrayList<String> loadStopWord(String namaFile){
        ArrayList<String> tempListStopWord = new ArrayList<>();
        System.out.println("-------------------------");
        System.out.println("Load daftar stopword Bahasa Indonesia..");
        try{
            Scanner scanner = new Scanner(new File(namaFile));
            while (scanner.hasNext()){
                tempListStopWord.add(scanner.nextLine());
            }
//            System.out.println("Daftar Stopword: ");
//            System.out.println(tempListStopWord.toString());
//            System.out.println("-------------------------");
        }catch(FileNotFoundException e){
            JOptionPane.showMessageDialog(null, "file tidak ditemukan", "error", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("-------------------------");
        return tempListStopWord;
    }
    
    public void printStopWord(ArrayList<String> listStopWord){
        int size = listStopWord.size();
        System.out.println("stop word: ");
        System.out.println("-------------------------");
        for (int i = 1; i < size; i++){
            System.out.println(listStopWord.get(i));
        }
    }
    
    public void preProcessingDataTrain(String namaFolder, ArrayList<String> listStopWord, Set<String> kamusKataDasar) throws Exception{
        File folderDataTrain = new File(namaFolder);
        
        System.out.println("pre processing data train..");
        System.out.println("-------------------------");
        
        for (File file : folderDataTrain.listFiles()){
            try {
                String namafile = file.getName();
//                System.out.println("    berita: " + namafile);
//                System.out.println("    -------------------------");
                ArrayList<String> hasilPreProcessingTraining = preProcessing(file, listStopWord, kamusKataDasar);
                
                kamusKata.add(hasilPreProcessingTraining);
                
                for (String s : hasilPreProcessingTraining){
                    if (!kolom.contains(s)){
                        kolom.add(s);
                    }
                }
                
//                System.out.println("    hasil pre processing " + namafile);
//                System.out.println("    " + hasilPreProcessingTraining.toString());
//                System.out.println("    -------------------------");
                
            } catch (Exception e) {
                Logger.getLogger(Ta.class.getName()).log(Level.SEVERE,null,e);
            }
        }
    }
    
    public void preProcessingDataTest(String namaFolder, ArrayList<String> listStopWord, Set<String> kamusKataDasar) throws Exception{
        File folderDataTest = new File(namaFolder);
        
        System.out.println("pre processing data test..");
        System.out.println("-------------------------");
        
        for (File file : folderDataTest.listFiles()){
            try {
                String namafile = file.getName();
//                System.out.println("    berita: " + namafile);
//                System.out.println("    -------------------------");
                
                ArrayList<String> hasilPreProcessingTest = preProcessing(file, listStopWord, kamusKataDasar);
                
                kamusKata.add(hasilPreProcessingTest);
                
                for (String s : hasilPreProcessingTest){
                    if (!kolom.contains(s)){
                        kolom.add(s);
                    }
                }
                
//                System.out.println("    hasil pre processing " + namafile);
//                System.out.println("    " + hasilPreProcessingTest.toString());
//                System.out.println("    -------------------------");
                
            } catch (Exception e) {
                Logger.getLogger(Ta.class.getName()).log(Level.SEVERE,null,e);
            }
        }
        
    }
    
    public ArrayList<String> preProcessing(File file, ArrayList<String> listStopWord, Set<String> kamusKataDasar) throws ParserConfigurationException, SAXException, IOException{
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        
        Element node = document.getDocumentElement();
        node.normalize();
        
//        System.out.println("    dokumen awal..");
//        System.out.println("        " + node.getTextContent());
//        System.out.println("        -------------------------");
//        
//        System.out.println("        remove punctuation..");
        String resRemovePunctuation = removePunctuation(node.getTextContent());
//        System.out.println("        hasil remove punctuation: ");
//        System.out.println("        " + resRemovePunctuation);
//        System.out.println("        -------------------------");
//        
//        System.out.println("        Tokenisasi..");
        String[] resTokenize = tokenisasi(resRemovePunctuation);
//        System.out.println("        hasil tokenisasi: ");
//        System.out.println("        " + Arrays.toString(resTokenize));
//        System.out.println("        -------------------------");
//        
//        System.out.println("        Menghapus StopWord..");
        ArrayList<String> resRemoveStopword = hapusStopWord(resTokenize, listStopWord);
//        System.out.println("        hasil stopword removal: ");
//        System.out.println("        " + Arrays.toString(resRemoveStopword));
//        System.out.println("        -------------------------");
//        
//        System.out.println("        Lemmatisasi..");
        ArrayList<String> resLemmatisasi = lemmatisasiSastrawi(resRemoveStopword, kamusKataDasar);
//        System.out.println("        hasil lemmatisasi: ");
//        System.out.println("        " + resLemmatisasi.toString());
//        System.out.println("        -------------------------");
        
        return resLemmatisasi;
    }
    
    public String[] tokenisasi(String s){
        String del = s.trim().replaceAll("\\s+", " ");
        return del.split(" ");
    }
    
    public ArrayList<String> hapusStopWord(String[] s, ArrayList<String> listStopWord){
        ArrayList<String> temp = new ArrayList<>(Arrays.asList(s));
        int size = temp.size();
        for (int i = 1; i < size; i++){
            if (listStopWord.contains(temp.get(i).toLowerCase())){
                temp.remove(i);
                size = size-1;
            }
        }
        return temp;
    }
    
    public String removePunctuation(String s){
        return s.trim().replaceAll("[^a-zA-Z]", " ");
    }
    
    public ArrayList<String> stemmerNaziefAdriani(String[] resTokenisasi){
        IndonesianStemmer stemmer = new IndonesianStemmer();
        ArrayList<String> listStemming = new ArrayList<>();
        int size = resTokenisasi.length;
        for (int i = 1; i < size; i++){
            String stem;
            if (stemmer.findRootWord(resTokenisasi[i]) == null){
                stem = resTokenisasi[i];
            } else{
                stem = stemmer.findRootWord(resTokenisasi[i]);
            }
            listStemming.add(stem);
        }
        return listStemming;
    }
    
    public ArrayList<String> lemmatisasiSastrawi(ArrayList<String> resTokenisasi, Set<String> kamusKataDasar){
        ArrayList<String> listLemma = new ArrayList<>();
        Lemmatizer lemma = new DefaultLemmatizer(kamusKataDasar);
        
        int size = resTokenisasi.size();
        String temp;
        for (int i = 0; i < size; i++){
            temp = lemma.lemmatize(resTokenisasi.get(i));
            if (temp == null){
                listLemma.add(resTokenisasi.get(i));
            }else{
                listLemma.add(temp);
            }
        }
        return listLemma;
    }
    
    public Set<String> loadKamusKataDasar() throws IOException{
        Set<String> tempKamusKataDasar = new HashSet<>();
        InputStream input = Lemmatizer.class.getResourceAsStream("/root-words.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
        String line ;
        while ((line = bufferedReader.readLine()) != null){
            tempKamusKataDasar.add(line);
        }
//        System.out.println(kamusKataDasar.toString());
        return tempKamusKataDasar;
    }
    
    public ArrayList<Double> hitungTfIdf(ArrayList<String> kolom, ArrayList<ArrayList<String>> kamusKata) throws Exception{
        System.out.println("hitung tf-idf..");
        ArrayList<Double> feature = new ArrayList<>();
        
        TFIDFCalculator1 tfidfc = new TFIDFCalculator1();

        for (String term : kolom){
            feature.add(tfidfc.tfIdf(kamusKata.get(kamusKata.size()-1), kamusKata, term));
        }

//        System.out.println("hasil tf-idf : "+feature.toString());
        System.out.println("-------------------------");
        return feature;
    }
    
    public String predictionJST() throws FileNotFoundException, IOException, Exception{
        System.out.println("testing jst..");
        MultilayerPerceptron model;
        Utils.FileDatabase fileDatabase = new FileDatabase();
        model = fileDatabase.loadFileModelJST();
        String predString;
        
        ConverterUtils.DataSource source2 = new ConverterUtils.DataSource("berita_prediksi_test.arff");
        Instances testdata = source2.getDataSet();
        testdata.setClassIndex(testdata.numAttributes() - 1);
        
//        testdata = informationGain(testdata);
        
        double preNB = model.classifyInstance(testdata.firstInstance());
//        System.out.println(String.valueOf(preNB));
        if (preNB < 0.5){
            predString = "tidak hoax";
        }else{
            predString = "hoax";
        }
        
        return predString;
    }
    
    public void createFileTesting(ArrayList<String> kolom, ArrayList<Double> feature) throws Exception{
        System.out.println("Membuat file testing..");
        ArrfFileBuilder arrfFileBuilder = new ArrfFileBuilder();
        arrfFileBuilder.createArffPredictTest(kolom, feature);
//        System.out.println("selesai..");
        System.out.println("-------------------------");
    }
    
    private static Instances informationGain(Instances data) throws Exception{
        InfoGainAttributeEval eval = new InfoGainAttributeEval();
        
        eval.buildEvaluator(data);
        
        Map<Attribute, Double> infoGainScores = new HashMap<>();
        for (int i = 0; i<data.numAttributes(); i++){
            infoGainScores.put(data.attribute(i), eval.evaluateAttribute(i));
        }
        infoGainScores = sortByValue(infoGainScores);
        saveHashmapToCsvFile(infoGainScores, "Dataset/dataset fix/data arff/lemma/information gain/ig_result.csv");
        
        Ranker search = new Ranker();
        
        search.setOptions(new String[] { "-T", "0.01" });
        
	AttributeSelection attSelect = new AttributeSelection();
	attSelect.setEvaluator(eval);
	attSelect.setSearch(search);
        
        attSelect.SelectAttributes(data);
        
        data = attSelect.reduceDimensionality(data);
	
	return data;
    }
    
    public static void saveDataToCsvFile(String path, Instances data) throws IOException{
        System.out.println("Saving to file " + path + "...");
        CSVSaver saver = new CSVSaver();
        saver.setInstances(data);
        saver.setFile(new File(path));
        saver.writeBatch();
    }
    
    public static void saveHashmapToCsvFile(Map<Attribute, Double> map, String pathFile) throws IOException{
        String eol = System.getProperty("line.separator");
        try (Writer writer = new FileWriter(pathFile)) {
            for (Map.Entry<Attribute, Double> entry : map.entrySet()) {
                writer.append(String.valueOf(entry.getKey()))
                        .append(',')
                        .append(String.valueOf(entry.getValue()))
                        .append(eol);
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }
    
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
//            System.out.println("-----------------------------");
//            System.out.println("attribute: "+entry.getKey());
//            System.out.println("info gain: "+entry.getValue());
//            System.out.println("-----------------------------");
        }

        return result;
    }
}
