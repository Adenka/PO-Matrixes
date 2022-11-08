package com.company.Regular;

import com.company.IDoubleMatrix;
import com.company.Matrix;

import static com.company.Matrix.Types.*;

public class Rowy extends Regular {
    public Rowy(double[] rowValues, int rows) {
        super(rowValues);
        assert (rows > 0);
        setShape(rows, rowValues.length);
    }

    @Override
    public Matrix.Types getClassName() {
        return ROWY;
    }

    @Override
    public double get(int row, int column) {
        shape().assertInShape(row, column);
        return super.getValue(column);
    }

    public IDoubleMatrix addRowy(IDoubleMatrix matrix) {
        return new Rowy(super.addRegular((Regular) matrix), matrix.shape().rows);
    }

    @Override
    public IDoubleMatrix times(double scalar) {
        return new Rowy(super.timesValues(scalar), this.shape().rows);
    }

    // Przyjmując, że w nadklasie (Regular) normy obliczane są dla macierzy typu Columny,
    // to wystarczy, że w macierzy typu Rowy wywołamy odwrotne normy
    // (w normOne normInfinity i vice versa).

    @Override
    public double normOne() {
        return super.normInfinity(this.shape().columns);
    }

    @Override
    public double normInfinity() {
        return super.normOne();
    }

    @Override
    public double frobeniusNorm() {
        return super.frobeniusNorm(shape().rows);
    }
}
