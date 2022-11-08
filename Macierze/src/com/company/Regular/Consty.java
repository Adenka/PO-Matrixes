/*
    Implementacja macierzy stałej.
 */

package com.company.Regular;

import com.company.IDoubleMatrix;
import com.company.Matrix;
import com.company.Shape;

import static com.company.Matrix.Types.*;

public class Consty extends Regular {
    public Consty(double value, Shape shape) {
        super(new double[]{value});
        assert (shape != null);
        setShape(shape.rows, shape.columns);
    }

    @Override
    public Matrix.Types getClassName() {
        return CONSTY;
    }

    @Override
    public double get(int row, int column) {
        shape().assertInShape(row, column);
        return super.getValue(0);
    }

    private IDoubleMatrix multiplyFull(IDoubleMatrix matrix) {
        double[] values = new double[matrix.shape().columns];
        for (int i = 0; i < matrix.shape().columns; ++i) {
            double columnSum = 0;
            for (int j = 0; j < this.shape().rows; ++j) {
                columnSum += matrix.get(j, i);
            }
            values[i] = columnSum * this.getValue(0);
        }

        return new Rowy(values, this.shape().rows);
    }

    private IDoubleMatrix multiplyDiagonal(IDoubleMatrix matrix) {
        double[] values = new double[matrix.shape().columns];
        for (int i = 0; i < matrix.shape().columns; ++i) {
            values[i] = matrix.get(i, i) * this.getValue(0);
        }

        return new Rowy(values, this.shape().rows);
    }

    private IDoubleMatrix multiplyAntidiagonal(IDoubleMatrix matrix) {
        double[] values = new double[matrix.shape().columns];
        for (int i = 0; i < matrix.shape().columns; ++i) {
            values[i] = matrix.get(matrix.shape().columns - 1 - i, i) * this.getValue(0);
        }

        return new Rowy(values, this.shape().rows);
    }

    private IDoubleMatrix multiplyVector(IDoubleMatrix matrix) {
        double sum = 0;
        for (int i = 0; i < matrix.shape().rows; ++i) {
            sum += matrix.get(i, 0);
        }

        sum *= this.getValue(0);

        return new Consty(sum, Shape.matrix(this.shape().rows, 1));
    }

    private IDoubleMatrix multiplyRowy(IDoubleMatrix matrix) {
        double[] values = new double[matrix.shape().columns];
        for (int i = 0; i < matrix.shape().columns; ++i) {
            values[i] = matrix.get(0, i) * this.shape().columns * this.getValue(0);
        }

        return new Rowy(values, this.shape().rows);
    }

    private IDoubleMatrix multiplyColumny(IDoubleMatrix matrix) {
        double sum = 0;
        for (int i = 0; i < matrix.shape().rows; ++i) {
            sum += matrix.get(i, 0);
        }

        sum *= this.get(0, 0);

        return new Consty(sum, Shape.matrix(this.shape().rows, matrix.shape().columns));
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
            case ROWY:
                return multiplyRowy(matrix);
            case COLUMNY:
                return multiplyColumny(matrix);
            case CONSTY:
                return new Consty(this.getValue(0) * matrix.get(0, 0) * this.shape().columns,
                        Shape.matrix(this.shape().rows, matrix.shape().columns));
            default:
                return super.multiply(matrix, matrixClass);
        }
    }

    @Override
    public IDoubleMatrix times(double scalar) {
        return new Consty(this.getValue(0) * scalar, this.shape());
    }

    @Override
    public double normOne() {
        return Math.abs(getValue(0)) * shape().rows;
    }

    @Override
    public double normInfinity() {
        return Math.abs(getValue(0)) * shape().columns;
    }

    @Override
    public double frobeniusNorm() {
        return Math.sqrt(getValue(0) * getValue(0) * shape().rows * shape().columns);
    }

    @Override
    public String toString() {
        int n = this.shape().rows;
        StringBuilder sb = new StringBuilder();

        double value = this.get(0, 0);
        for (int i = 0; i < n; ++i) {
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
