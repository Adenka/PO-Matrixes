/*
    Implementacja macierzy zerowej.
 */

package com.company.Regular;

import com.company.IDoubleMatrix;
import com.company.Shape;

import static com.company.Matrix.Types.*;

public class Zero extends Consty {
    public Zero(Shape shape) {
        super(0, shape);
    }

    @Override
    public Types getClassName() {
        return ZERO;
    }

    @Override
    public IDoubleMatrix add(IDoubleMatrix matrix, Types matrixClass) {
        return matrix;
    }

    // W wyniku przemnożenia macierzy przez macierz zerową zawsze otrzymamy macierz zerową
    // o rozmiarach zadanym zgodnie z algorytmem mnożenia macierzy.
    @Override
    public IDoubleMatrix multiply(IDoubleMatrix matrix, Types matrixClass) {
        return new Zero(Shape.matrix(this.shape().rows, matrix.shape().columns));
    }

    @Override
    public IDoubleMatrix times(double scalar) {
        return new Zero(this.shape());
    }

    @Override
    public String toString() {
        int n = this.shape().rows;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < n; ++i) {
            if (n > 2) {
                sb.append("0.0 ... 0.0");
            } else if (n == 2) {
                sb.append("0.0 0.0");
            } else {
                sb.append("0.0");
            }

            sb.append('\n');
        }

        return sb.toString();
    }
}
