import java.io.FileReader;
import java.math.BigInteger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class PolynomialSecretSharing {
    public static void main(String[] args) {
        try {
            // Read and parse the first test case
            JSONObject testCase1 = (JSONObject) new JSONParser().parse(new FileReader("testcase1.json"));
            BigInteger secret1 = findSecret(testCase1);
            System.out.println("Secret for Test Case 1: " + secret1);

            // Read and parse the second test case
            JSONObject testCase2 = (JSONObject) new JSONParser().parse(new FileReader("testcase2.json"));
            BigInteger secret2 = findSecret(testCase2);
            System.out.println("Secret for Test Case 2: " + secret2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BigInteger findSecret(JSONObject testCase) {
        JSONObject keys = (JSONObject) testCase.get("keys");
        int k = ((Long) keys.get("k")).intValue();
    
        BigInteger[] x = new BigInteger[k];
        BigInteger[] y = new BigInteger[k];
    
        int index = 0;
        for (Object key : testCase.keySet()) {
            if (!key.equals("keys")) {
                JSONObject point = (JSONObject) testCase.get(key);
                if (point != null) {
                    x[index] = new BigInteger((String) key);  // Use the key as x value
                    y[index] = decodeValue((String) point.get("value"), Integer.parseInt((String) point.get("base")));
                    index++;
                }
                if (index == k) break;
            }
        }
        return lagrangeInterpolation(x, y, BigInteger.ZERO);
    }
    
    
    private static BigInteger decodeValue(String value, int base) {
        return new BigInteger(value, base);
    }
    
    private static BigInteger lagrangeInterpolation(BigInteger[] x, BigInteger[] y, BigInteger xValue) {
        BigInteger result = BigInteger.ZERO;
        int k = x.length;
        
        for (int i = 0; i < k; i++) {
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;
            for (int j = 0; j < k; j++) {
                if (j != i) {
                    numerator = numerator.multiply(xValue.subtract(x[j]));
                    denominator = denominator.multiply(x[i].subtract(x[j]));
                }
            }
            result = result.add(y[i].multiply(numerator).divide(denominator));
        }
        
        return result;
    }
}