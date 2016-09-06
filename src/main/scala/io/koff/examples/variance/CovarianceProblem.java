package io.koff.examples.variance;

/**
 * Example of a problem with covariant types in Java
 */
public class CovarianceProblem {
    public static void main(String[] args) {
        String[] strArray = { "str#1", "str#2" };
        Object[] objArray = strArray;
        objArray[0] = 1; //throws ArrayStoreException: java.lang.Integer
    }
}
