/*
    Implementacja macierzy diagonalnych.
 */

package com.company.Diagonals;

import com.company.*;
import com.company.Full.Full;
import com.company.Full.Vector;
import com.company.Regular.Columny;
import com.company.Regular.Zero;
import com.company.Sparse.Sparse;

import static com.company.Matrix.Types.*;

public class Diagonal extends Diagonals {
    public Diagonal(double[] diagonalValues) {
        super(diagonalValues);
    }

    public Diagonal(int size, double value) {
        super(size, value);
    }

    @Override
    public Matrix.Types getClassName() {
        return DIAGONAL;
    }

    @Override
    public double get(int row, int column) {
        return super.get(row, column);
    }

    private IDoubleMatrix multiplyFull(IDoubleMatrix matrix) {
        double[][] output = new double[this.shape().rows][matrix.shape().columns];
        for (int i = 0; i < this.shape().rows; ++i) {
            for (int j = 0; j < matrix.shape().columns; ++j) {
                output[i][j] = this.get(i, i) * matrix.get(i, j);
            }
        }

        return new Full(output);
    }

    private IDoubleMatrix multiplyDiagonal(IDoubleMatrix matrix) {
        double[] output = new double[this.shape().rows];
        for (int i = 0; i < this.shape().rows; ++i) {
            output[i] = this.get(i, i) * matrix.get(i, i);
        }

        return new Diagonal(output);
    }

    private IDoubleMatrix multiplyAntidiagonal(IDoubleMatrix matrix) {
        double[] output = new double[this.shape().rows];
        for (int i = 0; i < this.shape().rows; ++i) {
            output[i] = this.get(i, i) * matrix.get(i, this.shape().rows - 1 - i);
        }

        return new Antidiagonal(output);
    }

    private IDoubleMatrix multiplyVector(IDoubleMatrix matrix) {
        double[] output = new double[this.shape().rows];
        for (int i = 0; i < this.shape().rows; ++i) {
            output[i] = this.get(i, i) * matrix.get(i, 0);
        }

        return new Vector(output);
    }

    private IDoubleMatrix multiplyColumny(IDoubleMatrix matrix) {
        double[] output = new double[this.shape().rows];
        for (int i = 0; i < this.shape().rows; ++i) {
            output[i] = this.get(i, i) * matrix.get(i, 0);
        }

        return new Columny(output, matrix.shape().columns);
    }

    @Override
    public IDoubleMatrix multiply(IDoubleMatrix matrix, Matrix.Types matrixClass) {
        // Rozważamy poszczególne przypadki mnożenia dwóch danych klas macierzy.
        // Jeżeli optymalizacja nie jest znacząca, wywołujemy domyślne mnożenie.
        switch (matrixClass) {
            case FULL:
                return multiplyFull(matrix);
            case IDENTITY:
                return this;
            case DIAGONAL:
                return multiplyDiagonal(matrix);
            case ANTIDIAGONAL:
                return multiplyAntidiagonal(matrix);
            case VECTOR:
                return multiplyVector(matrix);
            case ZERO:
                return new Zero(Shape.matrix(this.shape().rows, matrix.shape().columns));
            case COLUMNY:
            case CONSTY:
                return multiplyColumny(matrix);
            case SPARSE:
                return ((Sparse) matrix).multiplyReverseDiagonal(this);
            default:
                return super.multiply(matrix, matrixClass);
        }
    }

    @Override
    public IDoubleMatrix times(double scalar) {
        return new Diagonal(super.timesValues(scalar));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int n = shape().rows;

        for (int i = 0; i < shape().rows; ++i) {
            if (i > 2) {
                sb.append("0.0 ... 0.0 ");
            } else if (i == 2) {
                sb.append("0.0 0.0 ");
            } else if (i == 1) {
                sb.append("0.0 ");
            }

            sb.append(this.get(i, i));

            if (n - 1 - i > 2) {
                sb.append(" 0.0 ... 0.0");
            } else if (n - 1 - i == 2) {
                sb.append(" 0.0 0.0");
            } else if (n - 1 - i == 1) {
                sb.append(" 0.0");
            }

            sb.append('\n');
        }

        return sb.toString();
    }
}
