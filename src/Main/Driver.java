/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Utils.Model;
import java.io.Serializable;

/**
 *
 * @author Marwah
 */
public class Driver implements Serializable{

    public static void main(String[] args) {
        Model model = new Model();
        new Controller.ControllerInputBeritaGUI(model);
    }
}

