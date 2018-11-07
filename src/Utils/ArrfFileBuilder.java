package Utils;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Generates a little ARFF file with different attribute types.
 *
 * @author FracPete
 */
public class ArrfFileBuilder {
    
    public void createArff(ArrayList<String> param, ArrayList<ArrayList<Double>> tfidf) throws Exception {

        FastVector atts;
        FastVector attVals;
        Instances data;
        Instances dataRel;
        double[] vals;
        double[] valsRel;
        int i;
        int j, l;
        int u;
        int kk;
        // 1. set up attributes
        atts = new FastVector();
        j = param.size();
        l = tfidf.size();
        for (int o = 0; o < j; o++) {
            // - numeric
            atts.addElement(new Attribute(param.get(o)));
        }
        // - nominal
        attVals = new FastVector();

        attVals.addElement("hoax");
        attVals.addElement("no_hoax");

        atts.addElement(new Attribute("khoax", attVals));

        // 2. create Instances object
        data = new Instances("berita", atts, 0);

        // 3. fill with data
        for (int k = 0; k < (tfidf.size()); k++) {
            // first instance
            vals = new double[data.numAttributes()];
            // - numeric
            kk = 0;
            for (Double d : tfidf.get(k)) {
                //u = param.indexOf(d);
                vals[kk] = d;
                kk++;
            }

            // - nominal
            if (k <= 125) {
                vals[j] = attVals.indexOf("hoax");
            } else if (k > 125 && k <= 300){
                vals[j] = attVals.indexOf("no_hoax");
            } else if (k > 300 && k <= 375){
                vals[j] = attVals.indexOf("hoax");
            } else {
                vals[j] = attVals.indexOf("no_hoax");
            }
//            vals[j] = attVals.indexOf("?");

            data.add(new Instance(1.0, vals));
        }

        // 4. output data
        //System.out.println(data.toString());
        PrintWriter writer = new PrintWriter("Dataset/dataset fix/data arff/lemma/berita_500_lemma.arff", "UTF-8");
        writer.print(data);
        writer.close();
    }

    public void createArffTrain(ArrayList<String> param, ArrayList<ArrayList<Double>> tfidf) throws Exception {

        FastVector atts;
        FastVector attVals;
        Instances data;
        Instances dataRel;
        double[] vals;
        double[] valsRel;
        int i;
        int j, l;
        int u;
        int kk;
        // 1. set up attributes
        atts = new FastVector();
        j = param.size();
        l = tfidf.size();
        for (int o = 0; o < j; o++) {
            // - numeric
            atts.addElement(new Attribute(param.get(o)));
        }
        // - nominal
        attVals = new FastVector();

        attVals.addElement("hoax");
        attVals.addElement("no_hoax");

        atts.addElement(new Attribute("khoax", attVals));

        // 2. create Instances object
        data = new Instances("berita", atts, 0);

        // 3. fill with data
        for (int k = 0; k < (tfidf.size()); k++) {
            // first instance
            vals = new double[data.numAttributes()];
            // - numeric
            kk = 0;
            for (Double d : tfidf.get(k)) {
                //u = param.indexOf(d);
                vals[kk] = d;
                kk++;
            }

            // - nominal
            if (k <= 125) {
                vals[j] = attVals.indexOf("hoax");
            } else {
                vals[j] = attVals.indexOf("no_hoax");
            }

            data.add(new Instance(1.0, vals));
        }

        // 4. output data
        //System.out.println(data.toString());
        PrintWriter writer = new PrintWriter("Dataset/dataset fix/data arff/lemma/berita_train_500_lemma.arff", "UTF-8");
        writer.print(data);
        writer.close();
    }
    
    public void createArffTest(ArrayList<String> param, ArrayList<ArrayList<Double>> tfidf) throws Exception {

        FastVector atts;
        FastVector attVals;
        Instances data;
        Instances dataRel;
        double[] vals;
        double[] valsRel;
        int i;
        int j, l;
        int u;
        int kk;
        // 1. set up attributes
        atts = new FastVector();
        j = param.size();
        l = tfidf.size();
        for (int o = 0; o < j; o++) {
            // - numeric
            atts.addElement(new Attribute(param.get(o)));
        }
        // - nominal
        attVals = new FastVector();

        attVals.addElement("?");
//        attVals.addElement("no_hoax");

        atts.addElement(new Attribute("khoax", attVals));

        // 2. create Instances object
        data = new Instances("berita", atts, 0);

        // 3. fill with data
        for (int k = 0; k < (tfidf.size()); k++) {
            // first instance
            vals = new double[data.numAttributes()];
            // - numeric
            kk = 0;
            for (Double d : tfidf.get(k)) {
                //u = param.indexOf(d);
                vals[kk] = d;
                kk++;
            }

            // - nominal
//            if (k <= 40){
//                vals[j] = attVals.indexOf("hoax");
//            } else {
//                vals[j] = attVals.indexOf("no_hoax");
//            }
            vals[j] = attVals.indexOf("?");

            data.add(new Instance(1.0, vals));
        }

        // 4. output data
        //System.out.println(data.toString());
        PrintWriter writer = new PrintWriter("Dataset/dataset fix/data arff/lemma/berita_test_500_lemma.arff", "UTF-8");
        writer.print(data);
        writer.close();
    }
    
    public void createArffTestCrossVal(ArrayList<String> param, ArrayList<ArrayList<Double>> tfidf) throws Exception {

        FastVector atts;
        FastVector attVals;
        Instances data;
        Instances dataRel;
        double[] vals;
        double[] valsRel;
        int i;
        int j, l;
        int u;
        int kk;
        // 1. set up attributes
        atts = new FastVector();
        j = param.size();
        l = tfidf.size();
        for (int o = 0; o < j; o++) {
            // - numeric
            atts.addElement(new Attribute(param.get(o)));
        }
        // - nominal
        attVals = new FastVector();

        attVals.addElement("hoax");
        attVals.addElement("no_hoax");

        atts.addElement(new Attribute("khoax", attVals));

        // 2. create Instances object
        data = new Instances("berita", atts, 0);

        // 3. fill with data
        for (int k = 0; k < (tfidf.size()); k++) {
            // first instance
            vals = new double[data.numAttributes()];
            // - numeric
            kk = 0;
            for (Double d : tfidf.get(k)) {
                //u = param.indexOf(d);
                vals[kk] = d;
                kk++;
            }

            // - nominal
            if (k <= 75) {
                vals[j] = attVals.indexOf("hoax");
            } else {
                vals[j] = attVals.indexOf("no_hoax");
            }

            data.add(new Instance(1.0, vals));
        }

        // 4. output data
        //System.out.println(data.toString());
        PrintWriter writer = new PrintWriter("Dataset/dataset fix/data arff/lemma/berita_test_crossval_500_lemma.arff", "UTF-8");
        writer.print(data);
        writer.close();
    }
    
    public void createArffPredictTrain(ArrayList<String> param, ArrayList<ArrayList<Double>> tfidf) throws Exception{
        FastVector atts;
        FastVector attVals;
        Instances data;
        Instances dataRel;
        double[] vals;
        double[] valsRel;
        int i;
        int j, l;
        int u;
        int kk;
        // 1. set up attributes
        atts = new FastVector();
        j = param.size();
        l = tfidf.size();
        for (int o = 0; o < j; o++) {
            // - numeric
            atts.addElement(new Attribute(param.get(o)));
        }
        // - nominal
        attVals = new FastVector();

        attVals.addElement("hoax");
        attVals.addElement("no_hoax");

        atts.addElement(new Attribute("khoax", attVals));

        // 2. create Instances object
        data = new Instances("berita", atts, 0);

        // 3. fill with data
        for (int k = 0; k < (tfidf.size()-1); k++) {
            // first instance
            vals = new double[data.numAttributes()];
            // - numeric
            kk = 0;
            for (Double d : tfidf.get(k)) {
                //u = param.indexOf(d);
                vals[kk] = d;
                kk++;
            }

            // - nominal
            if (k <= 125) {
                vals[j] = attVals.indexOf("hoax");
            } else if (k > 125 && k <= 300){
                vals[j] = attVals.indexOf("no_hoax");
            } else if (k > 300 && k <= 375){
                vals[j] = attVals.indexOf("hoax");
            } else {
                vals[j] = attVals.indexOf("no_hoax");
            }
//            vals[j] = attVals.indexOf("?");

            data.add(new Instance(1.0, vals));
        }

        // 4. output data
        //System.out.println(data.toString());
        
        PrintWriter writer = new PrintWriter("berita_prediksi_train.arff", "UTF-8");
        writer.print(data);
        writer.close();
    }
    
    public void createArffPredictTest(ArrayList<String> param,ArrayList<Double> tfidf) throws Exception {

//        System.out.println("kolom "+String.valueOf(param.size())+" : "+param.toString());
//        System.out.println("fitur "+String.valueOf(tfidf.size())+" : "+tfidf.toString());
        FastVector atts;
        FastVector attVals;
        Instances data;
        Instances dataRel;
        double[] vals;
        double[] valsRel;
        int i;
        int kk;

        // 1. set up attributes
        atts = new FastVector();
        // - numeric
        int j = param.size();
        for (int o = 0; o < j; o++) {
            // - numeric
            atts.addElement(new Attribute(param.get(o)));
        }
        // - nominal
        attVals = new FastVector();

        attVals.addElement("hoax");
        attVals.addElement("no_hoax");

        atts.addElement(new Attribute("khoax", attVals));

        // 2. create Instances object
        data = new Instances("berita", atts, 0);

        // 3. fill with data
        // first instance
        vals = new double[data.numAttributes()];
        // - numeric
        kk = 0;
        for (Double d : tfidf) {
            //u = param.indexOf(d);
            vals[kk] = d;
            kk++;
        }
        // - nominal

//        System.out.println("kk: "+String.valueOf(kk));
        vals[kk] = attVals.indexOf("hoax");

        data.add(new Instance(1.0, vals));

        // 4. output data
        char[] charArray = data.toString().toCharArray();
        int last = charArray.length-1;
//        System.out.println("size before: "+String.valueOf(charArray.length));
        char[] newCharArray = Arrays.copyOfRange(charArray,0,last-2);
//        System.out.println("size after: "+String.valueOf(newCharArray.length));
        last = newCharArray.length-1;
        newCharArray[last] = '?';
//        System.out.println(new String(charArray));

        PrintWriter writer = new PrintWriter("berita_prediksi_test.arff", "UTF-8");
        writer.print(new String(newCharArray));
        writer.close();
    }
}
