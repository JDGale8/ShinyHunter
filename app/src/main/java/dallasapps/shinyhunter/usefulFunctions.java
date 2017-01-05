package dallasapps.shinyhunter;

/**
 * Created by Jake on 1/5/2017
 * this houses a few helpful functions that arent included in any standard distrubitons
 */
public class usefulFunctions {

    int comb(int n, int k){
        return factorial(n)/ (factorial(k)*(n - factorial(k)));
    };

    public int factorial(int n) {
        int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
}

