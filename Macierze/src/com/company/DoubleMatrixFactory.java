package com.company;

import com.company.Diagonals.Antidiagonal;
import com.company.Diagonals.Diagonal;
import com.company.Diagonals.Identity;
import com.company.Full.Full;
import com.company.Full.Vector;
import com.company.Regular.Columny;
import com.company.Regular.Consty;
import com.company.Regular.Rowy;
import com.company.Regular.Zero;
import com.company.Sparse.Sparse;

public class DoubleMatrixFactory {

    private DoubleMatrixFactory() {
    }

    public static IDoubleMatrix sparse(Shape shape, MatrixCellValue... values) {
        return new Sparse(shape, values);
    }

    public static IDoubleMatrix full(double[][] values) {
        return new Full(values);
    }

    public static IDoubleMatrix identity(int size) {
        return new Identity(size);
    }

    public static IDoubleMatrix diagonal(double... diagonalValues) {
        return new Diagonal(diagonalValues);
    }

    public static IDoubleMatrix antiDiagonal(double... antiDiagonalValues) {
        return new Antidiagonal(antiDiagonalValues);
    }

    public static IDoubleMatrix vector(double... values) {
        return new Vector(values);
    }

    public static IDoubleMatrix zero(Shape shape) {
        return new Zero(shape);
    }

    public static IDoubleMatrix consty(double value, Shape shape) {
        return new Consty(value, shape);
    }

    public static IDoubleMatrix rowy(double[] rowValues, int rows) {
        return new Rowy(rowValues, rows);
    }

    public static IDoubleMatrix columny(double[] columnValues, int columns) {
        return new Columny(columnValues, columns);
    }
}
