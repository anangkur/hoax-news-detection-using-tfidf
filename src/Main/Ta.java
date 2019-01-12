/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Utils.TFIDFCalculator1;
import Utils.ArrfFileBuilder;
import Utils.FileDatabase;
import IndonesianStemmer.IndonesianStemmer;
import Utils.SortFilesNumeric;
import Utils.TFIDFCalculator1;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
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
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.Debug.Random;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils;

/**
 *
 * @author Marwah
 */
public class Ta {

    /**
     * @param args the command line arguments
     */
    
    private static ArrayList<String> listStopWord = new ArrayList<>();
    private static Set<String> kamusKataDasar = new HashSet<>();
    private static final String relation = "kelas";
    
    private static MultilayerPerceptron modelJST;
    private static NaiveBayes modelBayes;
    private static SMO modelSVM;
    private static IBk modelKNN;
    
    private static ArrayList<String> kolom = new ArrayList<>();
    private static ArrayList<Double> feature = new ArrayList<>();
    private static ArrayList<ArrayList<Double>> baris = new ArrayList<>();
    private static ArrayList<ArrayList<String>> kamusKata = new ArrayList<>();
    
    public static void main(String[] args) throws IOException, Exception {
//        listStopWord = loadStopWord("Dataset/id.stopwords.02.01.2016.txt");
//        kamusKataDasar = loadKamusKataDasar();
//        
//        preProcessingDataTrain("Dataset/dataset fix/Datatrain");
//        preProcessingDataTest("Dataset/dataset fix/Datatest");   
//        
//        hitungTfIdf();
        
        crossValidate(10);
    }
    
    public static ArrayList<String> loadStopWord(String namaFile){
        ArrayList<String> tempListStopWord = new ArrayList<>();
        System.out.println("Load daftar stopword Bahasa Indonesia..");
        try{
            Scanner scanner = new Scanner(new File(namaFile));
            while (scanner.hasNext()){
                tempListStopWord.add(scanner.nextLine());
            }
            System.out.println("Daftar Stopword: ");
            System.out.println(tempListStopWord.toString());
            System.out.println("-------------------------");
        }catch(FileNotFoundException e){
            JOptionPane.showMessageDialog(null, "file tidak ditemukan", "error", JOptionPane.ERROR_MESSAGE);
        }
        return tempListStopWord;
    }
    
    public static void printStopWord(ArrayList<String> listStopWord){
        int size = listStopWord.size();
        System.out.println("stop word: ");
        System.out.println("-------------------------");
        for (int i = 1; i < size; i++){
            System.out.println(listStopWord.get(i));
        }
    }
    
    public static void preProcessingDataTrain(String namaFolder) throws Exception{
        File folderDataTrain = new File(namaFolder);
        
        System.out.println("pre processing data train..");
        System.out.println("-------------------------");
        
        SortFilesNumeric sortFilesNumeric = new SortFilesNumeric();
        File[] files = sortFilesNumeric.sortByNumber(folderDataTrain.listFiles());
        
        for (File file : files){
            try {
                String namafile = file.getName();
                System.out.println("    berita: " + namafile);
                System.out.println("    -------------------------");
                ArrayList<String> hasilPreProcessingTraining = preProcessing(file);
                
                kamusKata.add(hasilPreProcessingTraining);
                
                for (String s : hasilPreProcessingTraining){
                    if (!kolom.contains(s)){
                        kolom.add(s);
                    }
                }
                
                System.out.println("    hasil pre processing " + namafile);
                System.out.println("    " + hasilPreProcessingTraining.toString());
                System.out.println("    -------------------------");
                
            } catch (Exception e) {
                Logger.getLogger(Ta.class.getName()).log(Level.SEVERE,null,e);
            }
        }
    }
    
    public static void preProcessingDataTest(String namaFolder) throws Exception{
        File folderDataTest = new File(namaFolder);
        
        System.out.println("pre processing data test..");
        System.out.println("-------------------------");
        
        SortFilesNumeric sortFilesNumeric = new SortFilesNumeric();
        File[] files = sortFilesNumeric.sortByNumber(folderDataTest.listFiles());
        
        for (File file : files){
            try {
                String namafile = file.getName();
                System.out.println("    berita: " + namafile);
                System.out.println("    -------------------------");
                
                ArrayList<String> hasilPreProcessingTest = preProcessing(file);
                
                kamusKata.add(hasilPreProcessingTest);
                
                for (String s : hasilPreProcessingTest){
                    if (!kolom.contains(s)){
                        kolom.add(s);
                    }
                }
                
                System.out.println("    hasil pre processing " + namafile);
                System.out.println("    " + hasilPreProcessingTest.toString());
                System.out.println("    -------------------------");
                
            } catch (Exception e) {
                Logger.getLogger(Ta.class.getName()).log(Level.SEVERE,null,e);
            }
        }
        
    }
    
    private static ArrayList<String> preProcessing(File file) throws ParserConfigurationException, SAXException, IOException{
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
//        ArrayList<String> resLemmatisasi = lemmatisasiSastrawi(resRemoveStopword, kamusKataDasar);
//        System.out.println("        hasil lemmatisasi: ");
//        System.out.println("        " + resLemmatisasi.toString());
//        System.out.println("        -------------------------");
        
        return resRemoveStopword;
    }
    
    private static String[] tokenisasi(String s){
        String del = s.trim().replaceAll("\\s+", " ");
        return del.split(" ");
    }
    
    private static ArrayList<String> hapusStopWord(String[] s, ArrayList<String> listStopWord){
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
    
    private static String removePunctuation(String s){
        return s.trim().replaceAll("[^a-zA-Z]", " ");
    }
    
    private static ArrayList<String> lemmatisasiSastrawi(ArrayList<String> resTokenisasi, Set<String> kamusKataDasar){
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
    
    private static Set<String> loadKamusKataDasar() throws IOException{
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
    
    private static void hitungTfIdf() throws Exception{
        System.out.println("hitung tf-idf..");
        
        TFIDFCalculator1 tfidfc = new TFIDFCalculator1();
        
        int count = 0;
        for (ArrayList<String> hasilPreProcessingTest : kamusKata){
            for (String term : kolom){
                feature.add(tfidfc.tfIdf(hasilPreProcessingTest, kamusKata, term));
            }
            count++;
            System.out.println("hasil tf-idf "+String.valueOf(count)+" : "+feature.toString());
            baris.add((ArrayList<Double>)feature.clone());
            feature.clear();
        }
        
        System.out.println("kolom: "+ String.valueOf(kolom.size()));
        System.out.println("baris: "+ String.valueOf(baris.size()));
        
        ArrayList<ArrayList<Double>> barisTrain = new ArrayList<>();
        ArrayList<ArrayList<Double>> barisTest = new ArrayList<>();
        
        for (int i = 0; i < 200; i++){
            if (i < 300){
                barisTrain.add(baris.get(i));
            }else{
                barisTest.add(baris.get(i));
            }
//            barisTest.add(baris.get(i));
        }
        System.out.println("jumlah training: "+String.valueOf(barisTrain.size()));
        System.out.println("jumlah testing: "+String.valueOf(barisTest.size()));
        
        ArrfFileBuilder arrfFileBuilder = new ArrfFileBuilder();
//        System.out.println("membuat file testing..");
//        arrfFileBuilder.createArffTest(kolom, barisTest);
//        System.out.println("membuat file training..");
//        arrfFileBuilder.createArffTrain(kolom, barisTrain);
        System.out.println("membuat file gabungan..");
        arrfFileBuilder.createArff(kolom, baris);
        System.out.println("selesai..");
    }
    
    private static MultilayerPerceptron trainingProcessJST() throws Exception{
        System.out.println("proses training JST..");
        BufferedReader reader = new BufferedReader(new FileReader("Dataset/dataset fix/data arff/lemma/berita_train_500_lemma.arff"));
        ArffReader arff = new ArffReader(reader, 1000);
        Instances data = arff.getStructure();
        data.setClassIndex(data.numAttributes() - 1);
        Instance inst;
        int count = 0;
        while ((inst = arff.readInstance(data)) != null) {
          data.add(inst);
          count++;
        }
        System.out.println("jumlah instance training: "+String.valueOf(count));
        MultilayerPerceptron model = new MultilayerPerceptron();
        model.setLearningRate(0.2);
        model.setMomentum(0.5);
        model.setTrainingTime(10000);
        model.setHiddenLayers("1");
        model.buildClassifier(data);
        System.out.println("training JST selesai..");
        System.out.println("-------------------------");
        return model;
    }
    
    private static void crossValidate(int fold) throws Exception{
        System.out.println("Cross Validate..");
        System.out.println("");
        
        ConverterUtils.DataSource source = new ConverterUtils.DataSource("Dataset/dataset fix/data arff/lemma/berita_500_lemma.arff");
        Instances testData = source.getDataSet();
        testData.setClassIndex(testData.numAttributes() - 1);
        
        System.out.println("num atributes 1: "+String.valueOf(testData.numAttributes()-1));
        
        //testData = informationGain(testData);
        
        System.out.println("num atributes 2: "+String.valueOf(testData.numAttributes()-1));
        
//        saveDataToCsvFile("Dataset/dataset fix/data arff/lemma/information gain/ig_lemma_th_0.01.csv", testData);
        
        MultilayerPerceptron modelJST = new MultilayerPerceptron();
        modelJST.setHiddenLayers("1");
        modelJST.setLearningRate(0.1);
        modelJST.setTrainingTime(500);
        
        int folds = fold;
        
        Evaluation evaluationJST = new Evaluation(testData);
        
        FileDatabase fileDatabase = new FileDatabase();
        
        System.out.println("hasil JST: ");
        evaluationJST.crossValidateModel(modelJST, testData, folds, new Random(1));
        System.out.println(evaluationJST.toSummaryString());
        System.out.println("Presisi: "+String.valueOf(evaluationJST.precision(0)*100)+"%");
        System.out.println("Recall: "+String.valueOf(evaluationJST.recall(0)*100)+"%");
        System.out.println("F-Measure: "+String.valueOf(evaluationJST.fMeasure(0)*100)+"%");
        System.out.println("");
        fileDatabase.saveFileModelJST(modelJST);
    }
    
    private static Instances informationGain(Instances data) throws Exception{
        InfoGainAttributeEval eval = new InfoGainAttributeEval();
        
        eval.buildEvaluator(data);
        
        Map<Attribute, Double> infoGainScores = new HashMap<>();
        for (int i = 0; i<data.numAttributes(); i++){
            infoGainScores.put(data.attribute(i), eval.evaluateAttribute(i));
        }
        infoGainScores = sortByValue(infoGainScores);
//        saveHashmapToCsvFile(infoGainScores, "Dataset/dataset fix/data arff/lemma/information gain/ig_result.csv");
        
        Ranker search = new Ranker();
        
        search.setOptions(new String[] { "-T", "0.16" });
        
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
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());
        Collections.reverse(list);

        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
//            System.out.println("-----------------------------");
//            System.out.println("attribute: "+entry.getKey());
//            System.out.println("info gain: "+entry.getValue());
//            System.out.println("-----------------------------");
        }

        return result;
    }
}