package com.company.utils;

import com.company.MatrixCellValue;

import java.util.ArrayList;

public class utils {
    public static double[][] oneDimToTwoDim(double ... values)  {
        assert(values != null);
        double[][] result = new double[values.length][1];

        for (int i = 0; i < values.length; ++i) {
            result[i][0] = values[i];
        }

        return result;
    }

    public static MatrixCellValue[] toArray(ArrayList<MatrixCellValue> array) {
        MatrixCellValue[] output = new MatrixCellValue[array.size()];
        for (int i = 0; i < output.length; ++i) {
            output[i] = array.get(i);
        }

        return output;
    }
}
