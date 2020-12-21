package com.tinFInale;

import java.util.Arrays;

public class JavPy {
    public static void main(String[] args) {

        var l1 = new String[]{"rr", "rp", "rs"};
        var l2 = new String[]{"pr", "pp", "ps"};
        var l3 = new String[]{"sr", "sp", "ss"};

        var res = new String[][]{l1,l2,l3};

        for(String [] s: res){
            Arrays.stream(s)
                    .reduce((a,b)->a+", " + b)
                    .ifPresent(System.out::println);
        }
        
    }
}
