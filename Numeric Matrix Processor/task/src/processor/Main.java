package processor;

import java.util.Scanner;

public class Main {
    private static boolean run = true;
    private static int counter;
    //private static  State state = State.WAIT;
    //private static Operation operation = Operation.ADD;

    private static int state = 1;
                   // 1 – WAIT
                   // 2 – INPUT_SIZE_A
                   // 3 – INPUT_MATRIX_A
                   // 4 – INPUT_CONST
                   // 5 – INPUT_SIZE_B
                   // 6 – INPUT_MATRIX_B
                   // 7 – INPUT_TRANSPOSE_OPTION
    private static int operation = 1;
                   // 1 – ADD
                   // 2 – CONST_MULTIPLY
                   // 3 – MULTIPLY
                   // 4 – TRANSPOSITION
                   // 5 – DETERMINANT
                   // 5 – INVERSE
    private static int transposeOption = 1;
                   // 1 – Main diagonal
                   // 2 – Side diagonal
                   // 3 – Vertical line
                   // 4 – Horizontal line

    private static int rowsA = 1;
    private static int colsA = 1;
    private static double[][] A = new double[rowsA][colsA];

    private static int rowsB = 1;
    private static int colsB = 1;
    private static double[][] B = new double[rowsB][colsB];

    private static double c = 1;

    private static int rowsR = 1;
    private static int colsR = 1;
    private static double[][] R = new double[rowsR][colsR];

    private static boolean isRun() {
        return run;
    }

    private static void constMultiply() {
        rowsR = rowsA;
        colsR = colsA;
        R = constMultiply(A, c);
        printResult();
    }

    private static double[][] constMultiply(double[][] A, double c) {
        double[][] result = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[0].length; j++)
                result[i][j] = A[i][j] * c;
        return result;
    }

    private static void add() {
        if (rowsA != rowsB || colsA != colsB) {
            System.out.println("ERROR");
            return;
        }
        rowsR = rowsA;
        colsR = colsA;
        R = new double[rowsR][colsR];
        for (int i = 0; i < R.length; i++)
            for (int j = 0; j < R[0].length; j++)
                R[i][j] = A[i][j] + B[i][j];
        printResult();
    }

    private static void multiply() {
        if (colsA != rowsB) {
            System.out.println("ERROR");
            return;
        }
        rowsR = rowsA;
        colsR = colsB;
        R = new double[rowsR][colsR];
        double result;
        for (int i = 0; i < rowsA; i++)
            for (int j = 0; j < colsB; j++) {
                result = 0;
                for (int k = 0; k < colsA; k++)
                    result += A[i][k] * B[k][j];
                R[i][j] = result;
            }
        printResult();
    }

    private static void mainDiagonalTranspose() {
        if (rowsA != colsA) {
            System.out.println("ERROR");
            return;
        }
        rowsR = rowsA;
        colsR = colsA;
        R = mainDiagonalTranspose(A);
        printResult();
    }

    private static double[][] mainDiagonalTranspose(double[][] A) {
        double[][] transpMatrix = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[0].length; j++)
                transpMatrix[j][i] = A[i][j];
        return transpMatrix;
    }

    private static void sideDiagonalTranspose() {
        if (rowsA != colsA) {
            System.out.println("ERROR");
            return;
        }
        rowsR = rowsA;
        colsR = colsA;
        R = new double[rowsR][colsR];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[0].length; j++)
                R[R[0].length - 1 - j][R.length - 1 - i] = A[i][j];
        printResult();
    }

    private static void verticalLineTranspose() {
        rowsR = rowsA;
        colsR = colsA;
        R = new double[rowsR][colsR];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[0].length; j++)
                R[i][R[0].length - 1 - j] = A[i][j];
        printResult();
    }

    private static void horizontalLineTranspose() {
        rowsR = rowsA;
        colsR = colsA;
        R = new double[rowsR][colsR];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[0].length; j++)
                R[R.length - 1 - i][j] = A[i][j];
        printResult();
    }

    private static void determinant() {
        if (rowsA != colsA) {
            System.out.println("ERROR");
            return;
        }
        rowsR = 1;
        colsR = 1;
        R = new double[rowsR][colsR];
        R[0][0] = determinant(A);
        printResult();
    }

    private static double determinant(double[][] A) {
        if (A.length == 1) {
            return A[0][0];
        }
        if (A.length == 2) {
            return A[0][0] * A[1][1] - A[0][1] * A[1][0];
        }
        double s = 0;
        for (int k = 0; k < A[0].length; k++) {
            double[][] minor = minor(A, 0, k);
            s += Math.pow(-1, k) * A[0][k] * determinant(minor);
        }
        return s;
    }

    private static double[][] minor(double[][] A, int row, int column) {
        double[][] minor = new double[A.length - 1][A[0].length - 1];
        int c = 0;
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[0].length; j++)
                if (i != row && j != column)
                    minor[(c / minor[0].length) % minor.length][c++ % minor[0].length] = A[i][j];
        return minor;

    }

    private static void inverseMatrix() {
        if (rowsA != colsA) {
            System.out.println("ERROR");
            return;
        }
        if (determinant(A) == 0) {
            System.out.println("Determinant equals zero");
            return;
        }
        rowsR = rowsA;
        colsR = colsA;
        R = inverseMatrix(A);
        printResult();
    }

    private static double[][] inverseMatrix(double[][] A) {
        double invDet = 1.0 / determinant(A);
        double[][] cofactorMatrix = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[0].length; j++)
                cofactorMatrix[i][j] = Math.pow(-1, i + j) * determinant(minor(A, i, j));
        cofactorMatrix = mainDiagonalTranspose(cofactorMatrix);
        return constMultiply(cofactorMatrix, invDet);
    }

    private static void printResult() {
        System.out.println("The result is:");
        for (double[] row : R) {
            for (double elem : row)
                System.out.printf("%20.3f", elem);
            System.out.println();
        }
        System.out.println();
    }

    private static void error() {
        System.out.println("ERROR");
        goToMenu();
    }

    private static void process(String input) {
        //System.out.println(input);
        String[] temp;
        input = input.trim();
        switch (state) {
            case 1:
                switch (input) {
                    case "1":
                        operation = 1;
                        state = 2;
                        System.out.print("Enter size of first matrix: ");
                        break;
                    case "2":
                        operation = 2;
                        state = 2;
                        System.out.print("Enter size of matrix: ");
                        break;
                    case "3":
                        operation = 3;
                        state = 2;
                        System.out.print("Enter size of first matrix: ");
                        break;
                    case "0":
                        run = false;
                        break;
                    case "4":
                        operation = 4;
                        state = 7;
                        transposeOptionMenu();
                        break;
                    case "5":
                        operation = 5;
                        state = 2;
                        System.out.print("Enter size of matrix: ");
                        break;
                    case "6":
                        operation = 6;
                        state = 2;
                        System.out.print("Enter size of matrix: ");
                        break;
                    default:
                        error();
                        break;
                }
                break;
            case 2:
                temp = input.split(" ");
                rowsA = Integer.parseInt(temp[0]);
                colsA = Integer.parseInt(temp[1]);
                A = new double[rowsA][colsA];
                counter = 0;
                state = 3;
                switch (operation) {
                    case 2:
                    case 4:
                    case 5:
                    case 6:
                        System.out.println("Enter matrix");
                        break;
                    case 1:
                    case 3:
                        System.out.println("Enter first matrix:");
                        break;
                    default:
                        error();
                        break;
                }
                break;
            case 3:
                temp = input.split(" ");
                for (int j = 0; j < A[0].length; j++)
                    A[counter][j] = Double.parseDouble(temp[j]);
                counter++;
                if (counter == rowsA) {
                    counter = 0;
                    switch (operation) {
                        case 2:
                            state = 4;
                            System.out.print("Enter constant: ");
                            break;
                        case 4:
                            switch (transposeOption) {
                                case 1:
                                    mainDiagonalTranspose();
                                    break;
                                case 2:
                                    sideDiagonalTranspose();
                                    break;
                                case 3:
                                    verticalLineTranspose();
                                    break;
                                case 4:
                                    horizontalLineTranspose();
                                    break;
                                default:
                                    error();
                                    break;
                            }
                            goToMenu();
                            break;
                        case 5:
                            determinant();
                            goToMenu();
                            break;
                        case 6:
                            inverseMatrix();
                            goToMenu();
                            break;
                        case 1:
                        case 3:
                            state = 5;
                            System.out.print("Enter size of second matrix: ");
                            break;
                        default:
                            error();
                            break;
                    }
                }
                break;
            case 4:
                c = Double.parseDouble(input);
                constMultiply();
                goToMenu();
                break;
            case 5:
                temp = input.split(" ");
                rowsB = Integer.parseInt(temp[0]);
                colsB = Integer.parseInt(temp[1]);
                B = new double[rowsB][colsB];
                state = 6;
                System.out.println("Enter second matrix:");
                break;
            case 6:
                temp = input.split(" ");
                for (int j = 0; j < B[0].length; j++)
                    B[counter][j] = Double.parseDouble(temp[j]);
                counter++;
                if (counter == rowsB) {
                    counter = 0;
                    switch (operation) {
                        case 1:
                            add();
                            break;
                        case 3:
                            multiply();
                            break;
                        default:
                            System.out.println("ERROR");
                            break;
                    }
                    goToMenu();
                }
                break;
            case 7:
                transposeOption = Integer.parseInt(input);
                state = 2;
                System.out.print("Enter matrix size: ");
                break;
            default:
                error();
                break;

        }
    }

    private static void goToMenu() {
        state = 1;
        menu();
    }

    private static void menu() {
        System.out.println("1. Add matrices");
        System.out.println("2. Multiply matrix to a constant");
        System.out.println("3. Multiply matrices");
        System.out.println("4. Transpose matrix");
        System.out.println("5. Calculate a determinant");
        System.out.println("6. Inverse matrix");
        System.out.println("0. Exit");
        System.out.print("Your choice: ");
    }

    private static void transposeOptionMenu() {
        System.out.println("1. Main diagonal");
        System.out.println("2. Side diagonal");
        System.out.println("3. Vertical line");
        System.out.println("4. Horizontal line");
        System.out.print("Your choice: ");
    }

     public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        goToMenu();
        while (isRun()) {
            process(sc.nextLine());
        }
    }
}
