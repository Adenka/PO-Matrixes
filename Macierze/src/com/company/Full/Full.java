/*
    Implementacja macierzy pełnych.
 */

package com.company.Full;

import com.company.IDoubleMatrix;
import com.company.Matrix;
import com.company.Regular.Columny;
import com.company.Regular.Zero;
import com.company.Shape;
import com.company.Sparse.Sparse;

import static com.company.Matrix.Types.*;

public class Full extends Matrix {
    private final double[][] values;

    public Full(double[][] values) {
        assert (values != null);
        assert (values.length > 0 && values[0].length > 0);
        this.values = new double[values.length][values[0].length];

        for (int i = 0; i < values.length; ++i) {
            assert (values[i].length == values[0].length);
            System.arraycopy(values[i], 0, this.values[i], 0, values[i].length);
        }

        setShape(values.length, values[0].length);
    }

    @Override
    public Types getClassName() {
        return FULL;
    }

    @Override
    public double get(int row, int column) {
        shape().assertInShape(row, column);
        return values[row][column];
    }

    // Jedyna optymalizacja dodawania, którą możemy wykonać to dodanie macierzy zerowej.
    @Override
    public IDoubleMatrix add(IDoubleMatrix matrix, Types matrixClass) {
        if (matrixClass == ZERO) {
            return this;
        }
        return super.add(matrix, matrixClass);
    }

    private IDoubleMatrix multiplyDiagonal(IDoubleMatrix matrix) {
        double[][] output = new double[this.shape().rows][matrix.shape().columns];
        for (int i = 0; i < this.shape().rows; ++i) {
            for (int j = 0; j < matrix.shape().columns; ++j) {
                output[i][j] = this.get(i, j) * matrix.get(j, j);
            }
        }

        return new Full(output);
    }

    private IDoubleMatrix multiplyAntidiagonal(IDoubleMatrix matrix) {
        double[][] values = new double[this.shape().rows][matrix.shape().columns];
        for (int i = 0; i < this.shape().rows; ++i) {
            for (int j = 0; j < matrix.shape().columns; ++j) {
                values[i][j] = this.get(i,
                        matrix.shape().columns - 1 - j) * matrix.get(matrix.shape().columns - 1 - j, j);
            }
        }

        return new Full(values);
    }

    // Obliczenie jednej kolumny w przypadku mnożenia, gdzie wszystkie wychodzą one analogiczne
    // (przydatne w przypadku mnożenia macierzy typu Full przez macierz Typu Vector lub Columny).
    private double[] valuesVector(IDoubleMatrix matrix) {
        double[] values = new double[this.shape().rows];
        for (int i = 0; i < this.shape().rows; ++i) {
            for (int k = 0; k < this.shape().columns; ++k) {
                values[i] += this.get(i, k) * matrix.get(k, 0);
            }
        }
        return values;
    }

    private IDoubleMatrix multiplyVector(IDoubleMatrix matrix) {
        return new Vector(valuesVector(matrix));
    }

    private IDoubleMatrix multiplyColumny(IDoubleMatrix matrix) {
        return new Columny(valuesVector(matrix), matrix.shape().columns);
    }

    @Override
    public IDoubleMatrix multiply(IDoubleMatrix matrix, Types matrixClass) {
        // Rozważamy poszczególne przypadki mnożenia dwóch danych klas macierzy.
        // Jeżeli optymalizacja nie jest znacząca, wywołujemy domyślne mnożenie.
        switch (matrixClass) {
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
                return ((Sparse) matrix).multiplyReverseFull(this);
            default:
                return super.multiply(matrix, matrixClass);

        }
    }

    // Normy.

    @Override
    public double normOne() {
        double output = 0;
        for (int i = 0; i < shape().columns; ++i) {
            double partOutput = 0;
            for (int j = 0; j < shape().rows; ++j) {
                partOutput += Math.abs(get(j, i));
            }
            output = Math.max(output, partOutput);
        }

        return output;
    }

    public double normInfinity() {
        double output = 0;
        for (int i = 0; i < shape().rows; ++i) {
            double partOutput = 0;
            for (int j = 0; j < shape().columns; ++j) {
                partOutput += Math.abs(get(i, j));
            }
            output = Math.max(output, partOutput);
        }

        return output;
    }

    @Override
    public double frobeniusNorm() {
        double output = 0;
        for (int i = 0; i < shape().rows; ++i) {
            for (int j = 0; j < shape().columns; ++j) {
                output += get(i, j) * get(i, j);
            }
        }

        return Math.sqrt(output);
    }
}
