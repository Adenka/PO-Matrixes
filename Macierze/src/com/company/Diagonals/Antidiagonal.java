/*
    Implementacja macierzy antydiagonalnych.
 */

package com.company.Diagonals;

import com.company.*;
import com.company.Full.Full;
import com.company.Full.Vector;
import com.company.Regular.Columny;
import com.company.Regular.Zero;
import com.company.Sparse.Sparse;

public class Antidiagonal extends Diagonals {
    public Antidiagonal(double... antiDiagonalValues) {
        super(antiDiagonalValues);
    }

    @Override
    public Matrix.Types getClassName() {
        return Matrix.Types.ANTIDIAGONAL;
    }

    @Override
    public double get(int row, int column) {
        return super.get(row, this.getValueLength() - 1 - column);
    }


    private IDoubleMatrix multiplyFull(IDoubleMatrix matrix) {
        double[][] output = new double[this.shape().rows][matrix.shape().columns];
        for (int i = 0; i < this.shape().rows; ++i) {
            for (int j = 0; j < matrix.shape().columns; ++j) {
                output[i][j] = this.get(i, this.shape().rows - 1 - i) * matrix.get(this.shape().rows - 1 - i, j);
            }
        }

        return new Full(output);
    }

    private IDoubleMatrix multiplyDiagonal(IDoubleMatrix matrix) {
        double[] output = new double[this.shape().rows];
        for (int i = 0; i < this.shape().rows; ++i) {
            output[i] = this.get(i, this.shape().rows - 1 - i)
                    * matrix.get(this.shape().rows - 1 - i, this.shape().rows - 1 - i);
        }

        return new Antidiagonal(output);
    }

    private IDoubleMatrix multiplyAntidiagonal(IDoubleMatrix matrix) {
        double[] output = new double[this.shape().rows];
        for (int i = 0; i < this.shape().rows; ++i) {
            output[i] = this.get(i, this.shape().rows - 1 - i)
                    * matrix.get(this.shape().rows - 1 - i, i);
        }

        return new Diagonal(output);
    }

    private IDoubleMatrix multiplyVector(IDoubleMatrix matrix) {
        double[] output = new double[this.shape().rows];
        for (int i = 0; i < this.shape().rows; ++i) {
            output[i] = this.get(i, this.shape().rows - 1 - i)
                    * matrix.get(this.shape().rows - 1 - i, 0);
        }

        return new Vector(output);
    }

    private IDoubleMatrix multiplyColumny(IDoubleMatrix matrix) {
        double[] output = new double[this.shape().rows];
        for (int i = 0; i < this.shape().rows; ++i) {
            output[i] = this.get(i, this.shape().rows - 1 - i)
                    * matrix.get(this.shape().rows - 1 - i, 0);
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
                return ((Sparse) matrix).multiplyReverseAntidiagonal(this);
            default:
                return super.multiply(matrix, matrixClass);
        }
    }

    @Override
    public IDoubleMatrix times(double scalar) {
        return new Antidiagonal(super.timesValues(scalar));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int n = shape().rows;

        for (int i = 0; i < shape().rows; ++i) {
            if (n - i - 1 > 2) {
                sb.append("0.0 ... 0.0 ");
            } else if (n - i - 1 == 2) {
                sb.append("0.0 0.0 ");
            } else if (n - i - 1 == 1) {
                sb.append("0.0 ");
            }

            sb.append(this.get(i, n - 1 - i));

            if (i > 2) {
                sb.append(" 0.0 ... 0.0");
            } else if (i == 2) {
                sb.append(" 0.0 0.0");
            } else if (i == 1) {
                sb.append(" 0.0");
            }

            sb.append('\n');
        }

        return sb.toString();
    }
}
