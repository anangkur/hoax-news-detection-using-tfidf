/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author iqbal
 */
public class TFIDFCalculator1 {

    /**
     * @param doc list of strings
     * @param term String represents a term
     * @return term frequency of term in document
     */
    public double tf(ArrayList<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equals(word)) {
                result++;
            }
        }
        return result / doc.size();
    }

    /**
     * @param docs list of list of strings represents the dataset
     * @param term String represents a term
     * @return the inverse term frequency of term in documents
     */
    public double idf(ArrayList<ArrayList<String>> docs, String term) {
        double n = 0;
        for (List<String> doc : docs) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
        return Math.log((double)(docs.size() / n));
    }

    /**
     * @param doc a text document
     * @param docs all documents
     * @param term term
     * @return the TF-IDF of term
     */
    public double tfIdf(ArrayList<String> doc, ArrayList<ArrayList<String>> docs, String term) {
        return tf(doc, term) * idf(docs, term);

    }

    /*public static void main(String[] args) {

     List<String> doc1 = Arrays.asList("Lorem", "ipsum", "dolor", "ipsum", "sit", "ipsum");
     List<String> doc2 = Arrays.asList("Vituperata", "incorrupte", "at", "ipsum", "pro", "quo");
     List<String> doc3 = Arrays.asList("Has", "persius", "disputationi", "id", "simul");
     List<List<String>> documents = Arrays.asList(doc1, doc2, doc3);

     TFIDFCalculator calculator = new TFIDFCalculator();
     double tfidf = calculator.tfIdf(doc1, documents, "ipsum");
     System.out.println("TF-IDF (ipsum) = " + tfidf);


     }*/
}
