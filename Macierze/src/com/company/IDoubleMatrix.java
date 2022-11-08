package com.company;

public interface IDoubleMatrix {
    Matrix.Types getClassName();

    IDoubleMatrix times(IDoubleMatrix other);

    IDoubleMatrix times(double scalar);

    IDoubleMatrix plus(IDoubleMatrix other);

    IDoubleMatrix plus(double scalar);

    IDoubleMatrix add(IDoubleMatrix matrix, Matrix.Types matrixClass);

    IDoubleMatrix minus(IDoubleMatrix other);

    IDoubleMatrix minus(double scalar);

    double get(int row, int column);

    double[][] data();

    double normOne();

    double normInfinity();

    double frobeniusNorm();

    String toString();

    Shape shape();
}
