package com.company.testH;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import com.company.IDoubleMatrix;

import java.util.function.Function;
import java.util.stream.IntStream;

import static java.lang.Math.sqrt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.company.DoubleMatrixFactory.*;
import static com.company.MatrixCellValue.cell;
import static com.company.Shape.matrix;

public class MatrixNormTest {

  private static final double FROBENIUS_NORM = sqrt(IntStream.range(1, 7).mapToDouble(it -> it * it).sum());
  private static final double FROBENIUS_NORM_SQUARED = IntStream.range(1, 7).mapToDouble(it -> it * it).sum();

  @Nested
  public class FrobeniusNormTest {

    @Test
    void testFullMatrices() {
      testMatrixNorm(
        full(new double[][]{
          new double[]{1, -2},
          new double[]{3, -4},
          new double[]{5, -6},
        }),
        IDoubleMatrix::frobeniusNorm,
        FROBENIUS_NORM
      );
    }

      @Test
      void testRow() {
        testMatrixNorm(
          rowy(new double[]{1, 2, 3, 4, 5, 6}, 6),
          IDoubleMatrix::frobeniusNorm,
          sqrt(FROBENIUS_NORM_SQUARED * 6)
        );
      }

      @Test
      void testColumn() {
       testMatrixNorm(
        columny(new double[]{1, 2, 3, 4, 5, 6}, 6),
        IDoubleMatrix::frobeniusNorm,
        sqrt(FROBENIUS_NORM_SQUARED * 6)
       );
      }

    @Test
    void testSparseMatrices() {
      testMatrixNorm(
        sparse(
          matrix(1_000_000, 1_000_000),
          cell(0, 999_999, 1),
          cell(23, 1, -2),
          cell(424_242, 242_242, 3),
          cell(123, 789, -4),
          cell(456, 101, 5),
          cell(999_999, 111, -6)
        ),
        IDoubleMatrix::frobeniusNorm,
        FROBENIUS_NORM
      );
    }

    @Test
    void testVector() {
      testMatrixNorm(
        vector(1, -2, 3, -4, 5, -6),
        IDoubleMatrix::frobeniusNorm,
        FROBENIUS_NORM
      );
    }

    @Test
    void testDiagonal() {
      testMatrixNorm(
        diagonal(1, -2, 3, -4, 5, -6),
        IDoubleMatrix::frobeniusNorm,
        FROBENIUS_NORM
      );
    }

    @Test
    void testAntiDiagonal() {
      testMatrixNorm(
        antiDiagonal(1, -2, 3, -4, 5, -6),
        IDoubleMatrix::frobeniusNorm,
        FROBENIUS_NORM
      );
    }

    @Test
    void testConstant() {
      testMatrixNorm(
        consty(12, matrix(5,5)),
        IDoubleMatrix::frobeniusNorm,
        60
      );
    }
  }

  @Nested
  public class NormOneTest {

    @Test
    void testFullMatrices() {
      testMatrixNorm(
        full(new double[][]{
          new double[]{1, -2},
          new double[]{3, -4},
          new double[]{5, -6},
        }),
        IDoubleMatrix::normOne,
        2 + 4 + 6
      );
    }

      @Test
      void testRow() {
        testMatrixNorm(
          rowy(new double[]{1, 2, 3, 4, 5, 6}, 6),
          IDoubleMatrix::normOne,
          6*6
        );
      }

      @Test
      void testColumn() {
        testMatrixNorm(
        columny(new double[]{1, 2, 3, 4, 5, 6}, 6),
        IDoubleMatrix::normOne,
        1+2+3+4+5+6
        );
      }

      @Test
      void testConstantMatrices() {
      testMatrixNorm(
        consty(12, matrix(5,5)),
        IDoubleMatrix::normOne,
        12*5
        );
      }

    @Test
    void testSparseMatrices() {
      testMatrixNorm(
        sparse(
          matrix(1_000_000, 1_000_000),
          cell(0, 999_999, 1),
          cell(23, 1, -2),
          cell(424_242, 242_242, 3),
          cell(123, 789, -4),
          cell(456, 101, 5),
          cell(999_999, 111, -6)
        ),
        IDoubleMatrix::normOne,
        6
      );
    }

    @Test
    void testVector() {
      testMatrixNorm(
        vector(1, -2, 3, -4, 5, -6),
        IDoubleMatrix::normOne,
        1 + 2 + 3 + 4 + 5 + 6
      );
    }

    @Test
    void testDiagonal() {
      testMatrixNorm(
        diagonal(1, -2, 3, -4, 5, -6),
        IDoubleMatrix::normOne,
        6
      );
    }

    @Test
    void testAntiDiagonal() {
      testMatrixNorm(
        antiDiagonal(1, -2, 3, -4, 5, -6),
        IDoubleMatrix::normOne,
        6
      );
    }
  }

  @Nested
  public class NormInfinityTest {

    @Test
    void testFullMatrices() {
      testMatrixNorm(
        full(new double[][]{
          new double[]{1, -2},
          new double[]{3, -4},
          new double[]{5, -6},
        }),
        IDoubleMatrix::normInfinity,
        5 + 6
      );
    }

    @Test
    void testConstantMatrices() {
      testMatrixNorm(
        consty(12, matrix(5,5)),
        IDoubleMatrix::normInfinity,
        12*5
      );
    }

      @Test
      void testRow() {
        testMatrixNorm(
          rowy(new double[]{1, 2, 3, 4, 5, 6}, 6),
          IDoubleMatrix::normInfinity,
         1+2+3+4+5+6
        );
      }

      @Test
      void testColumn() {
        testMatrixNorm(
          columny(new double[]{1, 2, 3, 4, 5, 6}, 6),
          IDoubleMatrix::normInfinity,
         6*6
        );
      }

    @Test
    void testSparseMatrices() {
      testMatrixNorm(
        sparse(
          matrix(1_000_000, 1_000_000),
          cell(0, 999_999, 1),
          cell(23, 1, -2),
          cell(424_242, 242_242, 3),
          cell(123, 789, -4),
          cell(456, 101, 5),
          cell(999_999, 111, -6)
        ),
        IDoubleMatrix::normInfinity,
        6
      );
    }

    @Test
    void testVector() {
      testMatrixNorm(
        vector(1, -2, 3, -4, 5, -6),
        IDoubleMatrix::normInfinity,
        6
      );
    }

    @Test
    void testDiagonal() {
      testMatrixNorm(
        diagonal(1, -2, 3, -4, 5, -6),
        IDoubleMatrix::normInfinity,
        6
      );
    }

    @Test
    void testAntiDiagonal() {
      testMatrixNorm(
        antiDiagonal(1, -2, 3, -4, 5, -6),
        IDoubleMatrix::normInfinity,
        6
      );
    }
  }

  private static void testMatrixNorm(IDoubleMatrix m, Function<IDoubleMatrix, Double> matrixNorm, double expectedNorm) {
    final var norm = matrixNorm.apply(m);
    assertEquals(expectedNorm, norm);
  }
}
