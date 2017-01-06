package dallasapps.shinyhunter;

import java.math.BigInteger;

/**
 * Created by Jake on 1/5/2017
 * this houses a few helpful functions that arent included in any standard distrubitons
 */
public class usefulFunctions {


    public int comb(int n, int k)
    {
        BigInteger N = new BigInteger(n+"");
        BigInteger K = new BigInteger(k+"");
        BigInteger combResult = factorial(N).divide(factorial(K).multiply(factorial(N.min(K))));
        return combResult.intValue();
    }

    public BigInteger factorial(BigInteger i)
    {

        if (i.equals(1))
        {

            return  BigInteger.valueOf(1);
        }
        return i.multiply(factorial(i.min(BigInteger.valueOf(1))));
    }
}

