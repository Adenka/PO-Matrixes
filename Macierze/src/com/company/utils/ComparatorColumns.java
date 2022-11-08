package com.company.utils;

import com.company.MatrixCellValue;

import java.util.Comparator;

public class ComparatorColumns implements Comparator<MatrixCellValue> {
    public int compare(MatrixCellValue mCV1, MatrixCellValue mCV2) {
        if (mCV1.column < mCV2.column) return -1;
        if (mCV1.column > mCV2.column) return 1;

        return Integer.compare(mCV1.row, mCV2.row);

    }
}
