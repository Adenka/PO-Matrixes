/*
    Klasa abstrakcyjna, po której dziedziczą wszystkie macierze,
    których charakterystyka obejmuje wartości niezerowe na którejś z przekątnych
    (DIAGONAL, ANTIDIAGONAL, IDENTITY).
 */

package com.company.Diagonals;

import com.company.*;
import com.company.Sparse.Sparse;
import com.company.utils.utils;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Diagonals extends Matrix {
    // Macierz typu Diagonals reprezentowana jest
    // przez zbiór swoich wartości na wybranej przekątnej.
    private final double[] values;

    // Konstruktory.

    // Konstruktor dla macierzy, których wartości na przekątnej są różne.
    protected Diagonals(double[] values) {
        assert (values != null);
        this.values = new double[values.length];
        System.arraycopy(values, 0, this.values, 0, values.length);

        setShape(values.length, values.length);
    }

    // Konstruktor dla macierzy, których wartosci na przekątnej są równe.
    protected Diagonals(int size, double value) {
        assert (size > 0);
        double[] values = new double[size];
        Arrays.fill(values, value);

        this.values = values;
        setShape(size, size);
    }

    protected int getValueLength() {
        return values.length;
    }

    @Override
    public double get(int row, int column) {
        shape().assertInShape(row, column);
        if (row != column) {
            return 0;
        }

        return values[row];
    }

    // Dodanie do siebie dwóch macierzy typu Diagonal, Antidiagonal, Identity
    // lub wykonanie operacji typu Diagonal + Identity.
    public double[] addSameDiagonal(Diagonals matrix) {
        double[] values = new double[getValueLength()];
        for (int i = 0; i < getValueLength(); ++i) {
            values[i] = this.values[i] + matrix.values[i];
        }

        return values;
    }

    // Dodanie do siebie macierzy, które mają wartości na różnych przekątnych;
    // wynikiem jest wówczas macierz typu Sparse.
    public IDoubleMatrix addDifferentDiagonal(IDoubleMatrix matrix) {
        int size = matrix.shape().rows;
        ArrayList<MatrixCellValue> values = new ArrayList<>();

        // Na wynikowej tablicy znajdą się wszystkie komórki obu tablic z wyjątkiem "środkowej"
        // (komórka środkowa to komórka leżąca na miejscu o współrzędnych (floor(n/2), floor(n/2)))
        // (takowa istnieje wyłącznie dla macierzy o nieparzystych wymiarach).
        for (int i = 0; i < size; ++i) {
            if (2 * i + 1 != size) {
                values.add(new MatrixCellValue(i, size - 1 - i, matrix.get(i, size - 1 - i)));
            }
        }

        for (int i = 0; i < matrix.shape().rows; ++i) {
            if (2 * i + 1 != size) {
                values.add(new MatrixCellValue(i, i, this.get(i, i)));
            }
        }

        // Wartość w komórce "środkowej" obliczamy osobno dla macierzy o współrzędnych nieparzystych
        // (dodanie do siebie komórek środkowych z obu macierzy).
        if (size % 2 == 1) {
            int half = size / 2;
            values.add(new MatrixCellValue(half, half,
                    this.get(half, half) + matrix.get(half, size - 1 - half)));
        }

        return new Sparse(Shape.matrix(matrix.shape().rows, matrix.shape().columns), utils.toArray(values));
    }

    @Override
    public IDoubleMatrix add(IDoubleMatrix matrix, Types matrixClass) {
        // Dodanie do siebie macierzy typu Diagonals tych samych typów.
        if (this.getClassName() == matrixClass) {
            if (this.getClassName() == Types.IDENTITY) {
                return new Diagonal(this.getValueLength(), 2);
            }
            if (this.getClassName() == Types.DIAGONAL) {
                return new Diagonal(addSameDiagonal((Diagonals) matrix));
            } else if (this.getClassName() == Types.ANTIDIAGONAL) {
                return new Antidiagonal(addSameDiagonal((Diagonals) matrix));
            }
        }

        // Dodanie do siebie macierzy typu Diagonal i Identity.
        if (this.getClassName() == Types.DIAGONAL
                && matrixClass == Types.IDENTITY) {
            return new Diagonal(addSameDiagonal((Diagonals) matrix));
        }

        // Dodanie do siebie macierzy typu Diagonals o wartościach na różnych przekątnych.
        if ((this.getClassName() == Types.DIAGONAL && matrixClass == Types.ANTIDIAGONAL)
                || (this.getClassName() == Types.IDENTITY && matrixClass == Types.ANTIDIAGONAL)) {
            return addDifferentDiagonal(matrix);
        }

        // Dodanie macierzy pozostałych typów do macierzy typu Diagonals.
        switch (matrixClass) {
            // W przypadku poniższych klas korzystamy z przemienności dodawania macierzy (A + B = B + A)
            // i zwracamy wynik operacji B + A zamiast ponownie implementować dodawanie dwóch danych klas macierzy.
            case SPARSE:
            case FULL:
            case IDENTITY:
            case ZERO:
            case DIAGONAL:
            case ANTIDIAGONAL:
                return matrix.add(this, this.getClassName());
            default:
                return super.add(matrix, matrixClass);
        }
    }

    // Pomnożenie macierzy typu Diagonals przez skalar.
    protected double[] timesValues(double scalar) {
        int n = this.shape().rows;
        double[] values = new double[n];

        for (int i = 0; i < n; ++i) {
            values[i] = scalar * this.values[i];
        }

        return values;
    }

    // Obliczenie norm.

    @Override
    public double normOne() {
        double output = 0;
        for (double d : values) {
            output = Math.max(output, Math.abs(d));
        }

        return output;
    }

    @Override
    public double normInfinity() {
        double output = 0;
        for (double d : values) {
            output = Math.max(output, Math.abs(d));
        }

        return output;
    }

    @Override
    public double frobeniusNorm() {
        double output = 0;
        for (double d : values) {
            output += d * d;
        }

        return Math.sqrt(output);
    }
}
