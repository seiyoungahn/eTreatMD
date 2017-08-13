package com.exercise.seiyoung.exercise_etreatmd;

import java.util.ArrayList;

/**
 * Created by Seiyoung on 8/12/2017.
 */

public class DataHolder {
    private static ArrayList<Patient> patients;
    public static ArrayList<Patient> getPatients() {return patients;};
    public static void setPatients(ArrayList<Patient> patients) {DataHolder.patients = patients;};
}
