package com.tinFInale;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

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

            System.out.println(param(5));
        }

    IntStream.rangeClosed(0,100)
             .mapToObj(x-> x%3==0 ? (x% 5 == 0 ? "FizzBuzz" : "Fizz"): (x%5==0)?"Buzz": "")
             .filter(x-> !x.equals(""))
             .forEach(System.out::println);
        function(5);

    }


    public static boolean param(Integer param){
        Predicate<Integer> pred = x-> x< 5;
        return pred.test(param);
    }
    public static void function(Integer a){
        Function<Integer,Double> fun = b->b/2.0;
        System.out.println(fun.apply(a).doubleValue());

    }
}
