/*
    Implementacja macierzy rzadkiej.
 */

package com.company.Sparse;

import com.company.Full.Full;
import com.company.Full.Vector;
import com.company.IDoubleMatrix;
import com.company.Matrix;
import com.company.MatrixCellValue;
import com.company.Regular.Zero;
import com.company.Shape;
import com.company.utils.*;

import java.util.ArrayList;
import java.util.Arrays;

import static com.company.Matrix.Types.*;

public class Sparse extends Matrix {
    // Dwie kopie wartości macierzy rzadkiej trzymamy w dwóch tablicach -
    // jedna została posortowana rosnąca według numerów wierszy, a druga według numerów kolumn.
    private final MatrixCellValue[] valuesByRows;
    private final MatrixCellValue[] valuesByColumns;

    public Sparse(Shape shape, MatrixCellValue... values) {
        assert (values != null);
        setShape(shape.rows, shape.columns);

        for (MatrixCellValue mCV : values) {
            shape.assertInShape(mCV.row, mCV.column);
        }

        valuesByRows = new MatrixCellValue[values.length];
        valuesByColumns = new MatrixCellValue[values.length];

        System.arraycopy(values, 0, valuesByRows, 0, values.length);
        System.arraycopy(values, 0, valuesByColumns, 0, values.length);

        // Odpowiednie posortowanie obu tablic.
        Arrays.sort(valuesByRows, new ComparatorRows());
        Arrays.sort(valuesByColumns, new ComparatorColumns());

        // Sprawdzenie, czy w podanym wejściu nie występują wartości
        // zadeklarowane na tych samych miejscach w macierzy.
        for (int i = 1; i < valuesByRows.length; ++i) {
            assert (valuesByRows[i - 1].row != valuesByRows[i].row
                    || valuesByRows[i - 1].column != valuesByRows[i].column);
        }
    }

    // Konstruktor, który zamiast zwracać błąd przy pojawieniu się wartości
    // zadeklarowanych na tych samych miejscach w macierzy,
    // dodaje je do siebie.
    public Sparse SparseMerge(Shape shape, MatrixCellValue... values) {
        Arrays.sort(values, new ComparatorRows());
        ArrayList<MatrixCellValue> merged = new ArrayList<>();

        double sum = (values.length > 0 ? values[0].value : 0);

        for (int i = 1; i < values.length; ++i) {
            if (values[i].row == values[i - 1].row && values[i].column == values[i - 1].column) {
                sum += values[i].value;
            } else {
                merged.add(new MatrixCellValue(values[i - 1].row, values[i - 1].column, sum));
                sum = values[i].value;
            }
        }
        merged.add(new MatrixCellValue(values[values.length - 1].row, values[values.length - 1].column, sum));

        return new Sparse(shape, utils.toArray(merged));
    }

    @Override
    public Types getClassName() {
        return SPARSE;
    }

    @Override
    public double get(int row, int column) {
        shape().assertInShape(row, column);

        // Miejsce, którego szukamy.
        MatrixCellValue wanted = new MatrixCellValue(row, column, 0);
        // Wyszukujemy binarnie w posortowanej tablicy wartości.
        int i = Arrays.binarySearch(valuesByRows, wanted, new ComparatorRows());

        // Jeśli dane miejsce nie zostało odnalezione, oznacza to,
        // że na tym miejscu w macierzy jest 0.
        if (i < 0) {
            return 0;
        }

        return valuesByRows[i].value;
    }

    // Dodawanie dwóch macierzy typu Sparse.
    public IDoubleMatrix addSparse(Sparse matrix) {
        ArrayList<MatrixCellValue> values = new ArrayList<>();

        int i = 0;
        int j = 0;

        // Iterujemy się po wartościach obu macierzy
        // (i to indeks pierwszej macierzy, j to indeks drugiej macierzy).
        while (i < this.valuesByRows.length && j < matrix.valuesByRows.length) {
            MatrixCellValue mCVthis = this.valuesByRows[i];
            MatrixCellValue mCVmatrix = matrix.valuesByRows[j];

            // Porównanie współrzędnych dwóch aktualnie rozważanych komórek.
            int cmpOutput = (new ComparatorRows()).compare(mCVthis, mCVmatrix);

            // Jeżeli współrzędne są równe,
            // dodajemy do wyniku komórkę o wartości równej sumie dwóch aktualnych komórek.
            if (cmpOutput == 0) {
                values.add(
                        new MatrixCellValue(mCVthis.row, mCVthis.column, mCVthis.value + mCVmatrix.value)
                );
                // Zwiększamy indeksy, by iterować się dalej.
                ++i;
                ++j;
            // Jeżeli współrzędne nie są równe, to dodajemy do wyniku komórkę o "mniejszych" współrzędnych.
            } else if (cmpOutput < 0) {
                values.add(mCVthis);
                ++i;
            } else {
                values.add(mCVmatrix);
                ++j;
            }
        }

        // Dodajemy do wyniku pozostałe komórki.
        while (i < this.valuesByRows.length) {
            values.add(this.valuesByRows[i]);
            ++i;
        }

        while (j < matrix.valuesByRows.length) {
            values.add(this.valuesByRows[j]);
            ++j;
        }

        return new Sparse(shape(), utils.toArray(values));
    }

    public IDoubleMatrix addDiagonal(IDoubleMatrix matrix) {
        ArrayList<MatrixCellValue> values = new ArrayList<>();

        // Dodanie do wyniku wszystkich komórek występujących w obu tablicach
        // i obliczenie końcowego wyniku przy użyciu funkcji SparseMerge.

        for (int i = 0; i < matrix.shape().rows; ++i) {
            values.add(new MatrixCellValue(i, i, matrix.get(i, i)));
        }

        values.addAll(Arrays.asList(valuesByRows));

        return new Sparse(shape(), utils.toArray(values));
    }

    public IDoubleMatrix addAntidiagonal(IDoubleMatrix matrix) {
        ArrayList<MatrixCellValue> values = new ArrayList<>();

        // Dodanie do wyniku wszystkich komórek występujących w obu tablicach
        // i obliczenie końcowego wyniku przy użyciu funkcji SparseMerge.

        int n = matrix.shape().rows - 1;
        for (int i = 0; i < n; ++i) {
            values.add(new MatrixCellValue(n - 1 - i, i, matrix.get(n - 1 - i, i)));
        }

        values.addAll(Arrays.asList(valuesByRows));

        return new Sparse(shape(), utils.toArray(values));
    }

    @Override
    public IDoubleMatrix add(IDoubleMatrix matrix, Types matrixClass) {
        if (matrixClass == SPARSE) {
            return addSparse((Sparse) matrix);
        }

        switch (matrixClass) {
            // Rozważamy poszczególne przypadki dodawania dwóch danych klas macierzy.
            // Jeżeli optymalizacja nie jest znacząca (w tym wypadku szczególnie optymalizacja pamięci),
            // wywołujemy domyślne dodawanie.
            case IDENTITY:
            case DIAGONAL:
                return addDiagonal(matrix);
            case ANTIDIAGONAL:
                return addAntidiagonal(matrix);
            case ZERO:
                return this;
            default:
                return super.add(matrix, matrixClass);
        }
    }

    // Full.

    private IDoubleMatrix multiplyFull(IDoubleMatrix matrix) {
        // Ustalenie wyniku o dobrych wymiarach.
        double[][] results = new double[this.shape().rows][matrix.shape().columns];

        // Dla ustalonej niezerowej wartości w macierzy A typu Sparse (na pozycji (i, j))
        // mnożymy ją od razu przez wszystkie wartości w macierzy B, które mają indeks wiersza równy j
        // i dodajemy wyniki częściowe do odpowiednich komórek wynikowej macierzy.

        for (MatrixCellValue mCV : valuesByRows) {
            for (int i = 0; i < matrix.shape().columns; ++i) {
                results[mCV.row][i] += mCV.value * matrix.get(mCV.column, i);
            }
        }

        return new Full(results);
    }

    public IDoubleMatrix multiplyReverseFull(IDoubleMatrix matrix) {
        // Ustalenie wyniku o dobrych wymiarach.
        double[][] results = new double[matrix.shape().rows][this.shape().columns];

        // Proces jest analogiczny do mnożenia macierzy typu Sparse przez macierz typu Full
        // (funkcja powyżej).

        for (MatrixCellValue mCV : valuesByRows) {
            for (int i = 0; i < matrix.shape().rows; ++i) {
                results[i][mCV.column] += matrix.get(i, mCV.row) * mCV.value;
            }
        }

        return new Full(results);
    }

    // Diagonal.

    // Wykonanie mnożenia macierzy typu Sparse * Diagonal.
    private IDoubleMatrix multiplyDiagonal(IDoubleMatrix matrix) {
        ArrayList<MatrixCellValue> values = new ArrayList<>();

        // Każda komórka w macierzy typu Sparse zostanie przemnożona przez
        // dokładnie jedną liczbę z macierzy typu Diagonal
        // (wartość na miejscu (i, j) zostanie przemnożona przez wartość w rzędzie j, która jest tylko jedna).

        for (MatrixCellValue mCV : valuesByRows) {
            values.add(
                    new MatrixCellValue(mCV.row, mCV.column, mCV.value * matrix.get(mCV.column, mCV.column))
            );
        }
        return new Sparse(
                Shape.matrix(this.shape().rows, matrix.shape().columns),
                utils.toArray(values)
        );
    }

    // Wykonanie mnożenia macierzy odwrotnego do Sparse * Diagonal,
    // czyli Diagonal * Sparse.
    public IDoubleMatrix multiplyReverseDiagonal(IDoubleMatrix matrix) {
        ArrayList<MatrixCellValue> values = new ArrayList<>();

        for (MatrixCellValue mCV : valuesByRows) {
            values.add(
                    new MatrixCellValue(mCV.row, mCV.column, mCV.value * matrix.get(mCV.row, mCV.row))
            );
        }
        return new Sparse(
                Shape.matrix(matrix.shape().rows, this.shape().columns),
                utils.toArray(values));
    }

    // Antidiagonal.

    private IDoubleMatrix multiplyAntidiagonal(IDoubleMatrix matrix) {
        ArrayList<MatrixCellValue> values = new ArrayList<>();

        for (MatrixCellValue mCV : valuesByRows) {
            values.add(new MatrixCellValue(mCV.row, matrix.shape().columns - 1 - mCV.column,
                    mCV.value * matrix.get(mCV.column, matrix.shape().columns - 1 - mCV.column)));
        }

        return new Sparse(
                Shape.matrix(this.shape().rows, matrix.shape().columns),
                utils.toArray(values)
        );
    }

    public IDoubleMatrix multiplyReverseAntidiagonal(IDoubleMatrix matrix) {
        ArrayList<MatrixCellValue> values = new ArrayList<>();

        for (MatrixCellValue mCV : valuesByRows) {
            values.add(new MatrixCellValue(matrix.shape().rows - 1 - mCV.row, mCV.column,
                    matrix.get(matrix.shape().rows - 1 - mCV.row, mCV.row) * mCV.value));
        }

        return new Sparse(
                Shape.matrix(matrix.shape().rows, this.shape().columns),
                utils.toArray(values)
        );
    }

    // Vector.

    private IDoubleMatrix multiplyVector(IDoubleMatrix matrix) {
        double[] values = new double[matrix.shape().rows];

        for (MatrixCellValue mCV : valuesByRows) {
            values[mCV.row] += mCV.value * matrix.get(mCV.column, 0);
        }

        return new Vector(values);
    }

    // Funkcja odwrotna do multiplyVector (multiplyReverseVector),
    // ponieważ jest ona analogiczna do multiplyReverseFull.

    // Sparse.

    // Przemnożenie wszystkich elementów znajdujących się na jednej tablicy
    // ze wszystkimi elementami znajdującymi się na drugiej tablicy
    // i dodanie ich do wyniku razem ze współrzędnymi, na której znajdą się w wynikowej macierzy.
    private void multiplyEverything(ArrayList<MatrixCellValue> columnVector,
                                    ArrayList<MatrixCellValue> rowVector,
                                    ArrayList<MatrixCellValue> result) {
        for (MatrixCellValue mCV1 : columnVector) {
            for (MatrixCellValue mCV2 : rowVector) {
                result.add(
                        new MatrixCellValue(
                                mCV1.row,
                                mCV2.column,
                                mCV1.value * mCV2.value
                        )
                );
            }
        }
    }

    // Pomnożenia dwóch macierzy typu Sparse.
    private IDoubleMatrix multiplySparse(Sparse matrix) {
        ArrayList<MatrixCellValue> sameColumnIndex;
        ArrayList<MatrixCellValue> sameRowIndex;
        ArrayList<MatrixCellValue> result = new ArrayList<>();

        int i = 0;
        int j = 0;
        int column, row;

        while (i < valuesByColumns.length && j < matrix.valuesByRows.length) {
            // Jeżeli w i-tej kolumnie pierwszej macierzy oraz w j-tym wierszu drugiej macierzy
            // występują niezerowe wartości,
            // to zostaną one kiedyś przez siebie przemnożone.
            if (valuesByColumns[i].column == matrix.valuesByRows[j].row) {
                column = valuesByColumns[i].column;
                row = matrix.valuesByRows[j].row;

                sameColumnIndex = new ArrayList<>();
                sameRowIndex = new ArrayList<>();

                // Zbieramy wszystkie komórki z pierwszej macierzy o tym samym indeksie kolumny.
                while (i < valuesByColumns.length && valuesByColumns[i].column == column) {
                    sameColumnIndex.add(valuesByColumns[i]);
                    ++i;
                }

                // Zbieramy wszystkie komórki z drugiej macierzy o tym samym indeksie wiersza.
                while (j < matrix.valuesByRows.length && matrix.valuesByRows[j].row == row) {
                    sameRowIndex.add(matrix.valuesByRows[j]);
                    ++j;
                }

                // Wymnażamy te komórki, które i tak kiedyś przez siebie przemnożymy.
                multiplyEverything(sameColumnIndex, sameRowIndex, result);
            // Jeżeli elementy nie mają odpowiednich współrzędnych równych,
            // to inkrementujemy mniejszy z nich.
            } else if (valuesByColumns[i].column < matrix.valuesByRows[j].row) {
                ++i;
            } else {
                ++j;
            }
        }

        // Przez oryginalną konstrukcję funkcję multiplySparse mamy teraz na wynikowej strukturze
        // wiele komórek o identycznych współrzędnych,
        // (mnożenia zostały wykonane w różnych momentach).
        // Pozbywamy się powtórek dzięki konstruktorowi SparseMerge.
        return SparseMerge(
                Shape.matrix(this.shape().rows, matrix.shape().columns),
                utils.toArray(result)
        );
    }

    public IDoubleMatrix multiply(IDoubleMatrix matrix, Types matrixClass) {
        switch (matrixClass) {
            // Rozważamy poszczególne przypadki mnożenia dwóch danych klas macierzy.
            // Jeżeli optymalizacja nie jest znacząca, wywołujemy domyślne mnożenie.
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
            case SPARSE:
                return multiplySparse((Sparse) matrix);
            default:
                return super.multiply(matrix, matrixClass);

        }
    }

    // Przemnożenie macierzy typu Sparse przez skalar.
    @Override
    public IDoubleMatrix times(double scalar) {
        MatrixCellValue[] values = new MatrixCellValue[valuesByRows.length];

        for (int i = 0; i < valuesByRows.length; ++i) {
            values[i] = new MatrixCellValue(
                    valuesByRows[i].row,
                    valuesByRows[i].column,
                    valuesByRows[i].value * scalar);
        }

        return new Sparse(shape(), values);
    }

    // Normy.

    @Override
    public double normOne() {
        double output = 0;
        double partOutput = 0;
        int lastColumn = -1;

        for (MatrixCellValue mCV : valuesByColumns) {
            if (lastColumn == mCV.column) {
                partOutput += Math.abs(mCV.value);
            } else {
                output = Math.max(output, partOutput);
                partOutput = Math.abs(mCV.value);
                lastColumn = mCV.column;
            }
        }
        output = Math.max(output, partOutput);

        return output;
    }

    @Override
    public double normInfinity() {
        double output = 0;
        double partOutput = 0;
        int lastRow = -1;

        for (MatrixCellValue mCV : valuesByRows) {
            if (lastRow == mCV.row) {
                partOutput += Math.abs(mCV.value);
            } else {
                output = Math.max(output, partOutput);
                partOutput = Math.abs(mCV.value);
                lastRow = mCV.row;
            }
        }
        output = Math.max(output, partOutput);

        return output;
    }

    @Override
    public double frobeniusNorm() {
        double output = 0;

        for (MatrixCellValue mCV : valuesByRows) {
            output += mCV.value * mCV.value;
        }

        return Math.sqrt(output);
    }

    // Potencjalnie szybsza implementacja data.
    @Override
    public double[][] data() {
        double[][] output = new double[shape().rows][shape().columns];

        for (MatrixCellValue mCV : valuesByRows) {
            output[mCV.row][mCV.column] = mCV.value;
        }

        return output;
    }
}