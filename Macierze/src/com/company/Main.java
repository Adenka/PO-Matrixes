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

import java.util.Random;

public class Main {
    private static final int SIZE = 10;

    public static double[] oneDimFill(Random rd) {
        double[] valuesOneDim = new double[SIZE];

        for (int i = 0; i < SIZE; ++i) {
            valuesOneDim[i] = rd.nextInt(5);
        }

        return valuesOneDim;
    }

    public static double[][] twoDimFill(Random rd) {
        double[][] valuesTwoDim = new double[SIZE][SIZE];

        for (int i = 0; i < SIZE; ++i) {
            valuesTwoDim[i] = oneDimFill(rd);
        }

        return valuesTwoDim;
    }

    public static MatrixCellValue[] sparseFill(Random rd) {
        int s = rd.nextInt(100);
        MatrixCellValue[] valuesSparse = new MatrixCellValue[s];
        for (int i = 0; i < s; ++i) {
            valuesSparse[i] = new MatrixCellValue(rd.nextInt(SIZE), rd.nextInt(SIZE), rd.nextInt(5));
        }

        return valuesSparse;
    }

    public static void main(String[] args) {
        Random rd = new Random();

        double[] valuesOneDim;
        double[][] valuesTwoDim;
        MatrixCellValue[] valuesSparse;

        //  Inicjalizacja przykładowych macierzy wszystkich zaimplementowanych typów.

        valuesOneDim = oneDimFill(rd);
        Antidiagonal antidiagonal = new Antidiagonal(valuesOneDim);
        System.out.println("Antidiagonal:");
        System.out.println(antidiagonal);

        valuesOneDim = oneDimFill(rd);
        Columny columny = new Columny(valuesOneDim, SIZE);
        System.out.println("Columny:");
        System.out.println(columny);

        Consty consty = new Consty(rd.nextInt(5), Shape.matrix(SIZE, SIZE));
        System.out.println("Consty:");
        System.out.println(consty);

        valuesOneDim = oneDimFill(rd);
        Diagonal diagonal = new Diagonal(valuesOneDim);
        System.out.println("Diagonal:");
        System.out.println(diagonal);

        valuesTwoDim = twoDimFill(rd);
        Full full = new Full(valuesTwoDim);
        System.out.println("Full:");
        System.out.println(full);

        Identity identity = new Identity(SIZE);
        System.out.println("Identity:");
        System.out.println(identity);

        valuesOneDim = oneDimFill(rd);
        Rowy rowy = new Rowy(valuesOneDim, SIZE);
        System.out.println("Rowy:");
        System.out.println(rowy);

        valuesSparse = sparseFill(rd);
        Sparse sparse = new Sparse(Shape.matrix(SIZE, SIZE), valuesSparse);
        System.out.println("Sparse:");
        System.out.println(sparse);

        valuesOneDim = oneDimFill(rd);
        System.out.println("Vector:");
        Vector vector = new Vector(valuesOneDim);
        System.out.println(vector);

        Zero zero = new Zero(Shape.matrix(SIZE, SIZE));
        System.out.println("Zero:");
        System.out.println(zero);

        double scalar = rd.nextInt(5);
        System.out.println("Scalar: " + scalar);

        // Wykonanie przykładowych operacji na zainicjalizowanych macierzach.

        // Operacje arytmetyczne.

        System.out.println("Antidiagonal + Columny:");
        System.out.println(antidiagonal.plus(columny));

        System.out.println("Consty + scalar:");
        System.out.println(antidiagonal.plus(consty.plus(scalar)));

        System.out.println("Full - Identity:");
        System.out.println(full.minus(identity));

        System.out.println("Zero - scalar:");
        System.out.println(zero.minus(scalar));

        System.out.println("Rowy * Sparse");
        System.out.println(rowy.times(sparse));

        System.out.println("Vector * Scalar");
        System.out.println(vector.times(scalar));

        // Obliczenie norm.

        System.out.println("Norm One (Full):");
        System.out.println(full.normOne());

        System.out.println("Norm Infinity (Sparse):");
        System.out.println(sparse.normInfinity());

        System.out.println("Frobenius Norm (Antidiagonal):");
        System.out.println(antidiagonal.frobeniusNorm());
    }
}
