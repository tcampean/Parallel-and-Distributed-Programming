package org.example;

public class RowThread implements Runnable{
    private final Integer[][] result;
    private final Integer a;
    private final Integer b;

    RowThread(Integer[][] result, int a, int b) {
        this.result = result;
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        int m = Main.columnSecond;
        int i = a / m;
        int j = a % m;
        int k = b - a;
        for (int index = 0; index < k; ++index) {
            this.result[i][j] = Matrix.getProductForPosition(Main.matrix1, Main.matrix2, i, j);
            ++j;
            if (j == m) {
                j = 0;
                ++i;
            }
        }
    }
}
