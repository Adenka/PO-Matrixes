/*
    Implementacja macierzy o wszystkich kolumnach identycznych.
 */

package com.company.Regular;

import com.company.IDoubleMatrix;
import com.company.Matrix;

import static com.company.Matrix.Types.*;

public class Columny extends Regular {
    public Columny(double[] columnValues, int columns) {
        super(columnValues);
        assert (columns > 0);
        setShape(columnValues.length, columns);
    }

    @Override
    public Matrix.Types getClassName() {
        return COLUMNY;
    }

    @Override
    public double get(int row, int column) {
        shape().assertInShape(row, column);
        return super.getValue(row);
    }

    public IDoubleMatrix addColumny(IDoubleMatrix matrix) {
        return new Columny(super.addRegular((Regular) matrix), matrix.shape().columns);
    }

    @Override
    public IDoubleMatrix times(double scalar) {
        return new Columny(super.timesValues(scalar), this.shape().columns);
    }

    @Override
    public double normInfinity() {
        return super.normInfinity(this.shape().rows);
    }

    @Override
    public double frobeniusNorm() {
        return super.frobeniusNorm(shape().columns);
    }

    @Override
    public String toString() {
        int n = this.shape().rows;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < n; ++i) {
            double value = this.get(i, 0);
            if (n > 2) {
                sb.append(value);
                sb.append(" ... ");
                sb.append(value);
            } else if (n == 2) {
                sb.append(value);
                sb.append(" ");
                sb.append(value);
            } else {
                sb.append(value);
            }

            sb.append('\n');
        }

        return sb.toString();
    }
}