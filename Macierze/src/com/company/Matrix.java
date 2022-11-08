/*
    Klasa abstrakcyjna, po której dziedziczą wszystkie macierze.
 */

package com.company;

import com.company.Diagonals.Diagonal;
import com.company.Full.Full;
import com.company.Regular.Consty;

public abstract class Matrix implements IDoubleMatrix {
    private Shape shape;

    // W celu optymalizacji wykonywanych operacji na macierzach będziemy określać ich typ
    // (jeden z elementów poniższego typu wyliczeniowego).
    public enum Types {
        SPARSE,
        FULL,
        IDENTITY,
        DIAGONAL,
        ANTIDIAGONAL,
        VECTOR,
        ZERO,
        ROWY,
        COLUMNY,
        CONSTY
    }

    // Zwrócenie typu danej macierzy.
    public abstract Types getClassName();

    protected void setShape(int rows, int columns) {
        shape = Shape.matrix(rows, columns);
    }

    public Shape shape() {
        return shape;
    }

    // Podstawowe dodanie dwóch macierzy.
    public IDoubleMatrix add(IDoubleMatrix matrix, Types matrixClass) {
        double[][] values = new double[matrix.shape().rows][matrix.shape().columns];

        for (int i = 0; i < matrix.shape().rows; ++i) {
            for (int j = 0; j < matrix.shape().columns; ++j) {
                values[i][j] = this.get(i, j) + matrix.get(i, j);
            }
        }

        return new Full(values);
    }

    public IDoubleMatrix plus(IDoubleMatrix matrix) {
        // Sprawdzenie poprawności danych wejściowych.
        assert (shape().equals(matrix.shape()));

        // Wywołanie właściwego dodawania.
        return add(matrix, matrix.getClassName());
    }

    // Dodanie skalara do macierzy A jest równoważne wykonaniu dodawania macierzy A + B,
    // gdzie B to macierz stała o wartości scalar.
    public IDoubleMatrix plus(double scalar) {
        Consty consty = new Consty(scalar, shape());

        return this.plus(consty);
    }

    // Odjęcie macierzy B od macierzy A jest równoważne przemnożeniu macierzy B przez -1
    // i wykonaniu operacji A + B.
    public IDoubleMatrix minus(IDoubleMatrix matrix) {
        return plus(matrix.times(-1));
    }

    // Odjęcie skalara od macierzy A jest równoważne wykonaniu operacji A + B,
    // gdzie B to macierz stała o wartości (-1) * skalar.
    public IDoubleMatrix minus(double scalar) {
        return plus((-1) * scalar);
    }

    // Podstawowe pomnożenie macierzy.
    public IDoubleMatrix multiply(IDoubleMatrix matrix, Types matrixClass) {
        double[][] values = new double[this.shape().rows][matrix.shape().columns];

        for (int i = 0; i < this.shape().rows; ++i) {
            for (int j = 0; j < matrix.shape().columns; ++j) {
                for (int k = 0; k < matrix.shape().rows; ++k) {
                    values[i][j] += (this.get(i, k) * matrix.get(k, j));
                }
            }
        }

        return new Full(values);
    }

    public IDoubleMatrix times(IDoubleMatrix matrix) {
        // Sprawdzenie poprawności danych wejściowych.
        assert (this.shape().columns == matrix.shape().rows);

        // Właściwe pomnożenie macierzy.
        return multiply(matrix, matrix.getClassName());
    }

    // Pomnożenie macierzy A przez skalar jest równoważne wykonaniu działania A * B,
    // gdzie B to macierz diagonalna o wartościach równych skalar na przekątnej.
    public IDoubleMatrix times(double scalar) {
        return times(new Diagonal(this.shape.columns, scalar));
    }

    // Utworzenie tablicy dwuwymiarowej o wartościach takich jak w macierzy.
    public double[][] data() {
        double[][] output = new double[shape().rows][shape().columns];

        for (int i = 0; i < shape().rows; ++i) {
            for (int j = 0; j < shape().columns; ++j) {
                output[i][j] = get(i, j);
            }
        }

        return output;
    }

    // Podstawowe wypisanie macierzy.
    public String toString() {
        double[][] data = data();
        StringBuilder sb = new StringBuilder();

        // Utworzenie napisu oddającego data.
        for (double[] D : data) {
            for (double d : D) {
                sb.append(d);
                sb.append(" ");
            }

            sb.append('\n');
        }

        return sb.toString();
    }
}
