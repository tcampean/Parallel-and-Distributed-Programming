package org.example;

public class KthThread implements Runnable{
    private final Integer[][] result;
    private final Integer k;
    private final Integer stepSize;

    KthThread(Integer[][] result, int k, int stepSize) {
        this.result = result;
        this.k = k;
        this.stepSize = stepSize;
    }

    @Override
    public void run() {
        int n = Main.rowFirst;
        int m = Main.columnSecond;
        int i = 0;
        int j = k;
        while (true) {
            int offset = j/m;
            i += offset;
            j -= offset * m;
            if (i >= n)
                break;
            result[i][j] = Matrix.getProductForPosition(Main.matrix1, Main.matrix2, i, j);
            j += stepSize;
        }
    }
}
