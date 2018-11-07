/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author anang
 */
public class SortFilesNumeric {

    public File[] sortByNumber(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                int n1 = extractNumber(o1.getName());
                int n2 = extractNumber(o2.getName());
                return n1 - n2;
            }

            private int extractNumber(String name) {
                int i = 0;
                try {
                    int s = name.indexOf('a')+1;
                    int e = name.lastIndexOf('.');
                    String number = name.substring(s, e);
                    i = Integer.parseInt(number);
                } catch(Exception e) {
                    i = 0; // if filename does not match the format
                           // then default to 0
                }
                return i;
            }
        });
        return files;
//        for(File f : files) {
//            System.out.println(f.getName());
//        }
    }
}

