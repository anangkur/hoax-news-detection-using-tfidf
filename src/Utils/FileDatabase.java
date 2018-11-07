/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;


/**
 *
 * @author Marwah
 */
public class FileDatabase implements Serializable{
    
    public void saveFileModelJST(MultilayerPerceptron modelJST){
        try{
            FileOutputStream fos = new FileOutputStream("Dataset/dataset fix/model/jst/modeljst_hidden_10_lemma.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(modelJST);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public MultilayerPerceptron loadFileModelJST(){
        MultilayerPerceptron temp=null;
        try{
            FileInputStream fis = new FileInputStream("Dataset/dataset fix/model/jst/modeljst_hidden_10_lemma.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            temp = (MultilayerPerceptron) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
    
    public void saveFileModelBayes(NaiveBayes modelBayes){
        try{
            FileOutputStream fos = new FileOutputStream("Dataset/dataset fix/model/bayes/modelbayes_1_lemma.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(modelBayes);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public NaiveBayes loadFileModelBayes(){
        NaiveBayes temp=null;
        try{
            FileInputStream fis = new FileInputStream("Dataset/dataset fix/model/bayes/modelbayes_1_lemma.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            temp = (NaiveBayes) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
    
    public void saveFileModelSVM(SMO modelSVM){
        try{
            FileOutputStream fos = new FileOutputStream("Dataset/dataset fix/model/svm/modelsvm_poly_lemma.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(modelSVM);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public SMO loadFileModelSVM(){
        SMO temp=null;
        try{
            FileInputStream fis = new FileInputStream("Dataset/dataset fix/model/svm/modelsvm_poly_lemma.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            temp = (SMO) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
    
    public void saveFileModelKNN(IBk modelKNN){
        try{
            FileOutputStream fos = new FileOutputStream("Dataset/dataset fix/model/knn/modelknn_7_lemma.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(modelKNN);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public IBk loadFileModelKNN(){
        IBk temp=null;
        try{
            FileInputStream fis = new FileInputStream("Dataset/dataset fix/model/knn/modelknn_7_lemma.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            temp = (IBk) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
    
    public void saveTfIdf (String tfIdf, String PATH){

        try{
            FileOutputStream fos = new FileOutputStream(PATH+".txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(tfIdf);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String loadTfIdf(String PATH){
        String temp = null;
        try{
            FileInputStream fis = new FileInputStream(PATH+".txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            temp = (String) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
    
    public void writeHashMapToCsv(HashMap<String, Double> tfIdf) throws Exception {
        Map<String, Double> map = tfIdf;
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("tfidf.csv"))){
            for(Map.Entry<String, Double> entry : map.entrySet()){
                writer.write(String.valueOf(entry));
                writer.newLine();
            }
        }
    }
}
