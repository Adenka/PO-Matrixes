/*
    Implementacja macierzy regularnych (o identycznych wierszach, kolumnach lub obu na raz).
 */

package com.company.Regular;

import com.company.IDoubleMatrix;
import com.company.Matrix;
import com.company.Shape;

public abstract class Regular extends Matrix {
    private final double[] values;

    public Regular(double[] values) {
        assert (values != null);
        this.values = new double[values.length];
        System.arraycopy(values, 0, this.values, 0, values.length);
    }

    // Dodanie do siebie macierzy typu Regular tego samego rodzaju
    // lub macierzy typu Rowy/Columny i Consty.
    public double[] addRegular(Regular matrix) {
        int n = this.values.length;
        double[] values = new double[n];

        for (int i = 0; i < n; ++i) {
            values[i] = this.getValue(i) + matrix.getValue(i);
        }

        return values;
    }

    protected double getValue(int i) {
        return values[i];
    }

    @Override
    public IDoubleMatrix add(IDoubleMatrix matrix, Types matrixClass) {
        if (this.getClassName() == matrixClass || matrixClass == Types.CONSTY) {
            if (this.getClassName() == Types.ROWY) {
                return new Rowy(addRegular((Regular) matrix), this.shape().rows);
            } else if (this.getClassName() == Types.COLUMNY) {
                return new Columny(addRegular((Regular) matrix), this.shape().columns);
            } else if (this.getClassName() == Types.CONSTY) {
                return new Consty(this.getValue(0) + matrix.get(0, 0), matrix.shape());
            }
        }

        switch (matrixClass) {
            // W przypadku poniższych klas korzystamy z przemienności dodawania macierzy (A + B = B + A)
            // i zwracamy wynik operacji B + A zamiast ponownie implementować dodawanie dwóch danych klas macierzy.
            case SPARSE:
            case FULL:
            case IDENTITY:
            case DIAGONAL:
            case ANTIDIAGONAL:
            case VECTOR:
            case ZERO:
            case CONSTY:
                return matrix.add(this, this.getClassName());
            default:
                return super.add(matrix, matrixClass);
        }
    }

    @Override
    public IDoubleMatrix multiply(IDoubleMatrix matrix, Types matrixClass) {
        switch (matrixClass) {
            // Rozważamy poszczególne przypadki mnożenia dwóch danych klas macierzy.
            // Jeżeli optymalizacja nie jest znacząca, wywołujemy domyślne mnożenie.
            case IDENTITY:
                return this;
            case ZERO:
                return new Zero(Shape.matrix(this.shape().rows, matrix.shape().columns));
            default:
                return super.multiply(matrix, matrixClass);
        }
    }

    // Przemnożenie wszystkich wartości w macierzy typu Regular przez skalar.
    public double[] timesValues(double scalar) {
        int n = this.values.length;
        double[] values = new double[n];

        for (int i = 0; i < n; ++i) {
            values[i] = scalar * this.getValue(i);
        }

        return values;
    }

    // Normy.

    @Override
    public double normOne() {
        double output = 0;
        for (double d : values) {
            output += Math.abs(d);
        }

        return output;
    }

    // Aby norma była uniwersalna dla macierzy typu Rowy oraz Columny
    // podajemy parametr secondDim, przez który należy przemnożyć końcowy wynik
    // (liczba wierszy/kolumn, których sumę zliczyliśmy).
    public double normInfinity(int secondDim) {
        double output = 0;
        for (double d : values) {
            output = Math.max(output, Math.abs(d));
        }

        return output * secondDim;
    }

    public double frobeniusNorm(int secondDim) {
        double output = 0;
        for (double d : values) {
            output += d * d;
        }

        return Math.sqrt(output * secondDim);
    }
}
