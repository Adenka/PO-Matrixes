/*
    Implementacja wektora.
 */

package com.company.Full;

import static com.company.Matrix.Types.*;

import com.company.utils.*;

// Wszystkie funkcje w klasie Vector są analogiczne do funkcji w nadklasie (Full).
public class Vector extends Full {

    public Vector(double... vectorValues) {
        super(utils.oneDimToTwoDim(vectorValues));
        setShape(vectorValues.length, 1);
    }

    @Override
    public Types getClassName() {
        return VECTOR;
    }
}
