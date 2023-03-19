package org.example;

public class ColumnThread implements Runnable{
    private final Integer[][] result;
    private final Integer a;
    private final Integer b;

    ColumnThread(Integer[][] result, int a, int b) {
        this.result = result;
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        int n = Main.rowFirst;
        int i = a % n;
        int j = a / n;
        int k = b - a;
        for (int index = 0; index < k; ++index) {
            this.result[i][j] = Matrix.getProductForPosition(Main.matrix1, Main.matrix2, i, j);
            ++i;
            if (i == n) {
                i = 0;
                ++j;
            }
        }
    }
}
