package com.exercise.seiyoung.exercise_etreatmd;

import java.io.Serializable;

/**
 * Created by Seiyoung on 8/12/2017.
 */

public class Patient implements Serializable {
    public String name;
    public String id;

    public Patient (String name, String id){
        this.name = name;
        this.id = id;
    }
    @Override
    public String toString(){
        return name;
    }
}
