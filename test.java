import java.text.DecimalFormat;

/**
 * 实现矩阵的运算
 * 
 * @author Regino
 * class Matrix：
 * - height(): return no. of rows(行数)
 * - width(): return no. of columns(列数)
 * - add(Matrix target), multiply(double target): Linear Operations(线性运算)
 * - multiply(Matrix target): Multiplication(乘法运算)
 * - transpose(Matrix target)(): Transposed(转置)
 * - Solve equatino: solve(Matrix target), GaussElimination(Matrix target), setNum(int num): Equation System(解方程组)
 * - print()
 * test:
 * - output: origin & transpose & add & multiply & solve equation
 */
class Matrix {
    private int row;
    private int column;
    private double value[][];
    private int num;

    Matrix() {
    }

    Matrix(int row, int column, double[][] value) {
        this.row = row;
        this.column = column;
        this.value = value;
    }

    int height() {
        return column;
    }

    int width() {
        return row;
    }

    Matrix add(Matrix target) throws Exception {
        if (this.height() != target.height() || this.width() != target.width()) {
            throw new Exception("The two matrices must be identical in addition and subtraction! " +
                    "(加减法运算时两个矩阵必须是同型矩阵！)");
        } else {
            double result[][] = new double[this.row][this.column];
            for (int i = 0; i < this.row; i++) {
                for (int j = 0; j < this.column; j++) {
                    result[i][j] = this.value[i][j] + target.value[i][j];
                }
            }
            Matrix addition = new Matrix(this.row, this.column, result);
            return addition;
        }
    }

    Matrix multiply(double d) {
        double result[][] = new double[this.row][this.column];
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.column; j++) {
                result[i][j] = d * this.value[i][j];
            }
        }
        Matrix multiplication2 = new Matrix(this.row, this.column, result);
        return multiplication2;
    }

    Matrix multiply(Matrix target) throws Exception {
        if (this.column != target.row) {
            throw new Exception("The number of columns in the left matrix must equal to the number of rows in the right matrix! " +
                    "(乘法运算时左边矩阵的列数必须等于右边矩阵的行数!)");
        } else {
            double result[][] = new double[this.row][this.column];
            double c = 0;
            for (int i = 0; i < this.row; i++) {
                for (int j = 0; j < target.column; j++) {
                    //求C的元素值
                    for (int k = 0; k < this.column; k++) {
                        c += this.value[i][k] * target.value[k][j];
                    }
                    result[i][j] = c;
                    c = 0;
                }
            }
            Matrix multiplication1 = new Matrix(this.row, target.column, result);
            return multiplication1;
        }
    }

    void setNum(int num) {
        this.num = num;
    }

    Matrix transpose() {
        double result[][] = new double[this.column][this.row];
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.column; j++) {
                result[j][i] = this.value[i][j];
            }
        }
        Matrix transposed = new Matrix(this.column, this.row, result);
        return transposed;
    }

    String solve(Matrix target) {
        //Augmented Matrix
        double aug[][] = new double[this.row][this.column + 1];
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.column + 1; j++) {
                if (j == this.column) {
                    aug[i][j] = target.value[i][0];
                } else {
                    aug[i][j] = this.value[i][j];
                }
            }
        }
        Matrix augmented = new Matrix(this.row, this.column + 1, aug);
        Matrix solution = GaussElimination(augmented);
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n");
        switch (solution.num) {
            case 0:
                sb.append("AX = B has no solution(方程无解).");
                break;
            case 1:
                sb.append("AX = B has one unique solution(方程有解),\r\n");
                sb.append("X =");
                sb.append(solution.print());
                break;
            default:
                sb.append("AX = B has infinite many solutions(方程有无穷多个解).");
                break;
        }
        return sb.toString();
    }

    Matrix GaussElimination(Matrix augmented) {
        double[][] sol = new double[augmented.row][1];
        //TODO: 1. calculate arbitrary Equation System; 2. catch exception
        //r2-r3,r3*(1/2),r1-r3
        for (int j = 0; j < augmented.column; j++) {
            augmented.value[1][j] -= augmented.value[2][j];
            augmented.value[2][j] *= 0.5;
            augmented.value[0][j] -= augmented.value[2][j];
        }
        //r2*(1/3),r1-r2
        for (int j = 0; j < augmented.column; j++) {
            augmented.value[1][j] /= 3;
            augmented.value[0][j] -= augmented.value[1][j];
        }
        //r3-r1
        for (int j = 0; j < augmented.column; j++) {
            augmented.value[2][j] -= augmented.value[0][j];
        }
        //switch lines: r3->r1, r2->r3, r1->r2
        for (int j = 0; j < augmented.column; j++) {
            double temp0 = augmented.value[0][j];
            double temp1 = augmented.value[1][j];
            double temp2 = augmented.value[2][j];
            augmented.value[0][j] = temp2;
            augmented.value[1][j] = temp0;
            augmented.value[2][j] = temp1;
        }
        //output X
        for (int i = 0; i < augmented.row; i++) {
            sol[i][0] = augmented.value[i][3];
        }
        Matrix solution = new Matrix(augmented.row, 1, sol);
        //set no. of solutions
        solution.setNum(1);
        return solution;
    }

    String print() {
        DecimalFormat df = new DecimalFormat("0.##");
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n");
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.column; j++) {
                if (j == this.column - 1) {
                    sb.append(df.format(this.value[i][j]) + "\r\n");
                } else {
                    sb.append(df.format(this.value[i][j]) + " ");
                }
            }
        }
        sb.append("------");
        return sb.toString();
    }
}

public class test {
    public static void main(String[] args) {
        double a[][] = {{1, 2, 1}, {2, 2, 3}, {2, 2, 0}};
        double b[][] = {{2}, {0}, {0}};
        Matrix A = new Matrix(a.length, a[0].length, a);
        Matrix B = new Matrix(b.length, b[0].length, b);
        try {
            System.out.println("Matrix A(矩阵 A):" + A.print());
            System.out.println("Matrix B(矩阵 B):" + B.print());
            System.out.println("A x 5.5 =" + A.multiply(5.5).print());
            System.out.println("A x B =" + A.multiply(B).print());
            System.out.println("Transposed Matrix B(矩阵 B的置换):" + B.transpose().print());
            System.out.println("Solve equation AX = B(解方程 AX = B):" + A.solve(B));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
