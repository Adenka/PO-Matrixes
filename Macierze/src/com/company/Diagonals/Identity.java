/*
    Implementacja macierzy identycznościowych.
 */

package com.company.Diagonals;

import com.company.IDoubleMatrix;

import static com.company.Matrix.Types.*;

import java.util.Arrays;

public class Identity extends Diagonals {
    public Identity(int size) {
        super(size, 1);
    }

    @Override
    public Types getClassName() {
        return IDENTITY;
    }

    @Override
    public double get(int row, int column) {
        return super.get(row, column);
    }

    // W wyniku mnożenia macierzy identycznościowej przez dowolną macierz B otrzymujemy macierz B.
    @Override
    public IDoubleMatrix multiply(IDoubleMatrix matrix, Types matrixClass) {
        return matrix;
    }

    @Override
    public double normOne() {
        return 1;
    }

    @Override
    public double normInfinity() {
        return 1;
    }

    @Override
    public double frobeniusNorm() {
        return Math.sqrt(shape().rows);
    }

    @Override
    public String toString() {
        double[] values = new double[this.shape().columns];
        Arrays.fill(values, 1);

        Diagonal identity = new Diagonal(values);

        return identity.toString();
    }
}
