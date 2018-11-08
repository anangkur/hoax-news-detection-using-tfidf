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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import weka.core.Debug.Random;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ArffLoader.ArffReader;
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
        
//        preProcessingDataTrain("Dataset/dataset fix/Datatrain");
//        preProcessingDataTest("Dataset/dataset fix/Datatest");   
        
//        hitungTfIdf();
        
//        modelJST = trainingProcessJST();
//        FileDatabase filedatabaseJST = new FileDatabase();
//        filedatabaseJST.saveFileModelJST(modelJST);
        
//        modelBayes = trainingProcessBayes();
//        FileDatabase filedatabaseBayes = new FileDatabase();
//        filedatabaseBayes.saveFileModelBayes(modelBayes);
        
//        modelSVM = trainingProcessSVM();
//        FileDatabase filedatabaseSVM = new FileDatabase();
//        filedatabaseSVM.saveFileModelSVM(modelSVM);
        
//        modelKNN = trainingProcessKNN();
//        FileDatabase filedatabaseKNN = new FileDatabase();
//        filedatabaseKNN.saveFileModelKNN(modelKNN);
        
//        predictionJST();
//        predictionBayes();
//        predictionSVM();
//        predictionKNN();
        
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
        ArrayList<String> resLemmatisasi = lemmatisasiSastrawi(resRemoveStopword, kamusKataDasar);
//        System.out.println("        hasil lemmatisasi: ");
//        System.out.println("        " + resLemmatisasi.toString());
//        System.out.println("        -------------------------");
        
        return resLemmatisasi;
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
    
    private static ArrayList<String> stemmerNaziefAdriani(String[] resTokenisasi){
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
        System.out.println("membuat file testing..");
        arrfFileBuilder.createArffTest(kolom, barisTest);
        System.out.println("membuat file training..");
        arrfFileBuilder.createArffTrain(kolom, barisTrain);
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
    
    private static NaiveBayes trainingProcessBayes() throws Exception{
        System.out.println("proses training Bayes..");
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
        NaiveBayes model = new NaiveBayes();
        model.buildClassifier(data);
        System.out.println("training Bayes selesai..");
        System.out.println("-------------------------");
        return model;
    }
    
    private static SMO trainingProcessSVM() throws Exception{
        System.out.println("proses training SVM..");
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
        SMO model = new SMO();
        model.buildClassifier(data);
        System.out.println("training SVM selesai..");
        System.out.println("-------------------------");
        return model;
    }
    
    private static IBk trainingProcessKNN() throws Exception{
        System.out.println("proses training KNN..");
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
        IBk model = new IBk();
        model.buildClassifier(data);
        System.out.println("training KNN selesai..");
        System.out.println("-------------------------");
        return model;
    }
    
    private static void predictionJST() throws FileNotFoundException, IOException, Exception{
        System.out.println("testing jst..");
        MultilayerPerceptron model;
        Utils.FileDatabase fileDatabase = new FileDatabase();
        model = fileDatabase.loadFileModelJST();
        String predString;
        
        ConverterUtils.DataSource source2 = new ConverterUtils.DataSource("Dataset/dataset fix/data arff/lemma/berita_test_500_lemma.arff");
        Instances testdata = source2.getDataSet();
        testdata.setClassIndex(testdata.numAttributes() - 1);

        List<String> kelas = new ArrayList<>();
        List<String> prediksi = new ArrayList<>();
        for (int i=1;i<=200;i++){
            if (i<=75){
                kelas.add("hoax");
            }else{
                kelas.add("no_hoax");
            }
        }
        for (int j = 0; j < testdata.numInstances(); j++) {
            Instance newInst = testdata.instance(j);
            double preNB = model.classifyInstance(newInst);
            if (preNB < 0.5){
                predString = "hoax";
            }else{
                predString = "no_hoax";
            }
            prediksi.add(predString);
        }
        
        double tp = 0;
        double tn = 0;
        double fp = 0;
        double fn = 0;
        for(int i = 0; i < 200; i++){
            if(kelas.get(i).equals("hoax") && prediksi.get(i).equals("hoax")){
                tp++;
            }else if(kelas.get(i).equals("hoax") && prediksi.get(i).equals("no_hoax")){
                fn++;
            }else if(kelas.get(i).equals("no_hoax") && prediksi.get(i).equals("hoax")){
                fp++;
            }else if(kelas.get(i).equals("no_hoax") && prediksi.get(i).equals("no_hoax")){
                tn++;
            }
        }
        
        System.out.println("");
        System.out.println("TP: "+String.valueOf(tp));
        System.out.println("TN: "+String.valueOf(tn));
        System.out.println("FP: "+String.valueOf(fp));
        System.out.println("FN: "+String.valueOf(fn));
        
        double akurasi = ((tp+tn)/(tp+tn+fp+fn));
        double presisi = tp/(tp+fp);
        double recall = tp/(tp+fn);
        double fmeasure = (2*(recall * presisi))/(recall + presisi);
        
        System.out.println("Akurasi: "+String.valueOf(akurasi));
        System.out.println("Presisi: "+String.valueOf(presisi));
        System.out.println("Recall: "+String.valueOf(recall));
        System.out.println("F-Measure: "+String.valueOf(fmeasure));
    }
    
    private static void predictionBayes() throws FileNotFoundException, IOException, Exception{
        System.out.println("testing Bayes..");
        NaiveBayes model;
        Utils.FileDatabase fileDatabase = new FileDatabase();
        model = fileDatabase.loadFileModelBayes();
        String predString;

        ConverterUtils.DataSource source2 = new ConverterUtils.DataSource("Dataset/dataset fix/data arff/lemma/berita_test_500_lemma.arff");
        Instances testdata = source2.getDataSet();
        testdata.setClassIndex(testdata.numAttributes() - 1);

        List<String> kelas = new ArrayList<>();
        List<String> prediksi = new ArrayList<>();
        for (int i=1;i<=200;i++){
                if (i<=75){
                    kelas.add("hoax");
                }else{
                    kelas.add("no_hoax");
                }
            }
        for (int j = 0; j < testdata.numInstances(); j++) {
            Instance newInst = testdata.instance(j);
            double preNB = model.classifyInstance(newInst);
            if (preNB < 0.5){
                predString = "hoax";
            }else{
                predString = "no_hoax";
            }
            prediksi.add(predString);
        }
        
        double tp = 0;
        double tn = 0;
        double fp = 0;
        double fn = 0;
        for(int i = 0; i < 200; i++){
            if(kelas.get(i).equals("hoax") && prediksi.get(i).equals("hoax")){
                tp++;
            }else if(kelas.get(i).equals("hoax") && prediksi.get(i).equals("no_hoax")){
                fn++;
            }else if(kelas.get(i).equals("no_hoax") && prediksi.get(i).equals("hoax")){
                fp++;
            }else if(kelas.get(i).equals("no_hoax") && prediksi.get(i).equals("no_hoax")){
                tn++;
            }
        }
        
        System.out.println("");
        System.out.println("TP: "+String.valueOf(tp));
        System.out.println("TN: "+String.valueOf(tn));
        System.out.println("FP: "+String.valueOf(fp));
        System.out.println("FN: "+String.valueOf(fn));
        
        double akurasi = ((tp+tn)/(tp+tn+fp+fn));
        double presisi = tp/(tp+fp);
        double recall = tp/(tp+fn);
        double fmeasure = (2*(recall * presisi))/(recall + presisi);
        
        System.out.println("Akurasi: "+String.valueOf(akurasi));
        System.out.println("Presisi: "+String.valueOf(presisi));
        System.out.println("Recall: "+String.valueOf(recall));
        System.out.println("F-Measure: "+String.valueOf(fmeasure));
    }
    
    private static void predictionSVM() throws FileNotFoundException, IOException, Exception{
        System.out.println("testing svm..");
        SMO model;
        Utils.FileDatabase fileDatabase = new FileDatabase();
        model = fileDatabase.loadFileModelSVM();
        String predString;
        
        ConverterUtils.DataSource source2 = new ConverterUtils.DataSource("Dataset/dataset fix/data arff/lemma/berita_test_500_lemma.arff");
        Instances testdata = source2.getDataSet();
        testdata.setClassIndex(testdata.numAttributes() - 1);

        List<String> kelas = new ArrayList<>();
        List<String> prediksi = new ArrayList<>();
        for (int i=1;i<=200;i++){
                if (i<=75){
                    kelas.add("hoax");
                }else{
                    kelas.add("no_hoax");
                }
            }
        for (int j = 0; j < testdata.numInstances(); j++) {
            Instance newInst = testdata.instance(j);
            double preNB = model.classifyInstance(newInst);
            if (preNB < 0.5){
                predString = "hoax";
            }else{
                predString = "no_hoax";
            }
            prediksi.add(predString);
        }
        
        double tp = 0;
        double tn = 0;
        double fp = 0;
        double fn = 0;
        for(int i = 0; i < 200; i++){
            if(kelas.get(i).equals("hoax") && prediksi.get(i).equals("hoax")){
                tp++;
            }else if(kelas.get(i).equals("hoax") && prediksi.get(i).equals("no_hoax")){
                fn++;
            }else if(kelas.get(i).equals("no_hoax") && prediksi.get(i).equals("hoax")){
                fp++;
            }else if(kelas.get(i).equals("no_hoax") && prediksi.get(i).equals("no_hoax")){
                tn++;
            }
        }
        
        System.out.println("");
        System.out.println("TP: "+String.valueOf(tp));
        System.out.println("TN: "+String.valueOf(tn));
        System.out.println("FP: "+String.valueOf(fp));
        System.out.println("FN: "+String.valueOf(fn));
        
        double akurasi = ((tp+tn)/(tp+tn+fp+fn));
        double presisi = tp/(tp+fp);
        double recall = tp/(tp+fn);
        double fmeasure = (2*(recall * presisi))/(recall + presisi);
        
        System.out.println("Akurasi: "+String.valueOf(akurasi));
        System.out.println("Presisi: "+String.valueOf(presisi));
        System.out.println("Recall: "+String.valueOf(recall));
        System.out.println("F-Measure: "+String.valueOf(fmeasure));
    }
    
    private static void predictionKNN() throws FileNotFoundException, IOException, Exception{
        System.out.println("testing knn..");
        IBk model;
        Utils.FileDatabase fileDatabase = new FileDatabase();
        model = fileDatabase.loadFileModelKNN();
        String predString;
        
        ConverterUtils.DataSource source2 = new ConverterUtils.DataSource("Dataset/dataset fix/data arff/lemma/berita_test_500_lemma.arff");
        Instances testdata = source2.getDataSet();
        testdata.setClassIndex(testdata.numAttributes() - 1);

        List<String> kelas = new ArrayList<>();
        List<String> prediksi = new ArrayList<>();
        for (int i=1;i<=200;i++){
                if (i<=75){
                    kelas.add("hoax");
                }else{
                    kelas.add("no_hoax");
                }
            }
        for (int j = 0; j < testdata.numInstances(); j++) {
            Instance newInst = testdata.instance(j);
            double preNB = model.classifyInstance(newInst);
            if (preNB < 0.5){
                predString = "hoax";
            }else{
                predString = "no_hoax";
            }
            prediksi.add(predString);
        }
        
        double tp = 0;
        double tn = 0;
        double fp = 0;
        double fn = 0;
        for(int i = 0; i < 200; i++){
            if(kelas.get(i).equals("hoax") && prediksi.get(i).equals("hoax")){
                tp++;
            }else if(kelas.get(i).equals("hoax") && prediksi.get(i).equals("no_hoax")){
                fn++;
            }else if(kelas.get(i).equals("no_hoax") && prediksi.get(i).equals("hoax")){
                fp++;
            }else if(kelas.get(i).equals("no_hoax") && prediksi.get(i).equals("no_hoax")){
                tn++;
            }
        }
        
        System.out.println("");
        System.out.println("TP: "+String.valueOf(tp));
        System.out.println("TN: "+String.valueOf(tn));
        System.out.println("FP: "+String.valueOf(fp));
        System.out.println("FN: "+String.valueOf(fn));
        
        double akurasi = ((tp+tn)/(tp+tn+fp+fn));
        double presisi = tp/(tp+fp);
        double recall = tp/(tp+fn);
        double fmeasure = (2*(recall * presisi))/(recall + presisi);
        
        System.out.println("Akurasi: "+String.valueOf(akurasi));
        System.out.println("Presisi: "+String.valueOf(presisi));
        System.out.println("Recall: "+String.valueOf(recall));
        System.out.println("F-Measure: "+String.valueOf(fmeasure));
    }
    
    private static void crossValidate(int fold) throws Exception{
        System.out.println("Cross Validate..");
        System.out.println("");
        
//        SMO modelSVM = new SMO();
//        PolyKernel polyKernel = new PolyKernel();

//        modelSVM.setKernel(polyKernel);
        
        MultilayerPerceptron modelJST = new MultilayerPerceptron();
        modelJST.setHiddenLayers("10");
        modelJST.setLearningRate(0.01);
        
//        NaiveBayes modelBayes = new NaiveBayes();
//        
//        IBk modelKNN = new IBk();
//        modelKNN.setKNN(9);
        
        ConverterUtils.DataSource sourceSVM = new ConverterUtils.DataSource("Dataset/dataset fix/data arff/lemma/berita_500_lemma.arff");
        Instances testData = sourceSVM.getDataSet();
        System.out.println("num atributes 1: "+String.valueOf(testData.numAttributes()));
        testData = informationGain(testData);
        System.out.println("num atributes 2: "+String.valueOf(testData.numAttributes()));
        testData.setClassIndex(testData.numAttributes() - 1);
        
        int folds = fold;
        
//        Evaluation evaluationSVM = new Evaluation(testData);
//        Evaluation evaluationBayes = new Evaluation(testData);
//        Evaluation evaluationKNN = new Evaluation(testData);
        Evaluation evaluationJST = new Evaluation(testData);
        
        FileDatabase fileDatabase = new FileDatabase();

//        System.out.println("hasil SVM: ");
//        evaluationSVM.crossValidateModel(modelSVM, testData, folds, new Random(1));
//        System.out.println(evaluationSVM.toSummaryString());
//        System.out.println("Presisi: "+String.valueOf(evaluationSVM.precision(0)*100)+"%");
//        System.out.println("Recall: "+String.valueOf(evaluationSVM.recall(0)*100)+"%");
//        System.out.println("F-Measure: "+String.valueOf(evaluationSVM.fMeasure(0)*100)+"%");
//        System.out.println("");
//        fileDatabase.saveFileModelSVM(modelSVM);
//        
//        System.out.println("hasil Bayes: ");
//        evaluationBayes.crossValidateModel(modelBayes, testData, folds, new Random(1));
//        System.out.println(evaluationBayes.toSummaryString());
//        System.out.println("Presisi: "+String.valueOf(evaluationBayes.precision(0)*100)+"%");
//        System.out.println("Recall: "+String.valueOf(evaluationBayes.recall(0)*100)+"%");
//        System.out.println("F-Measure: "+String.valueOf(evaluationBayes.fMeasure(0)*100)+"%");
//        System.out.println("");
//        fileDatabase.saveFileModelBayes(modelBayes);
//        
//        System.out.println("hasil KNN: ");
//        evaluationKNN.crossValidateModel(modelKNN, testData, folds, new Random(1));
//        System.out.println(evaluationKNN.toSummaryString());
//        System.out.println("Presisi: "+String.valueOf(evaluationKNN.precision(0)*100)+"%");
//        System.out.println("Recall: "+String.valueOf(evaluationKNN.recall(0)*100)+"%");
//        System.out.println("F-Measure: "+String.valueOf(evaluationKNN.fMeasure(0)*100)+"%");
//        System.out.println("");
//        fileDatabase.saveFileModelKNN(modelKNN);
        
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
        Ranker search = new Ranker();
        
        search.setOptions(new String[] { "-T", "0.029" });
        
	AttributeSelection attSelect = new AttributeSelection();
	attSelect.setEvaluator(eval);
	attSelect.setSearch(search);
        
        attSelect.SelectAttributes(data);
        
        data = attSelect.reduceDimensionality(data);
	
	return data;
    }
}