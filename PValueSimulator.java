package com.test;


import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.colt.matrix.DoubleFactory2D;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PValueSimulation {

    public static List<Double> predictTruePs ( List<double[][]> standardDataList, double ageTarget ) {
        return standardDataList.stream()
                .map(standardData -> predict(standardData, ageTarget))
                .collect(Collectors.toList());
    }

    private static DoubleFactory2D appender = DoubleFactory2D.dense;
    private static Algebra operator = Algebra.DEFAULT;

    private static double customLog ( double num, double base ) {
        return Math.log(num) / Math.log(base);
    }

    ///core code!!!!!!!!!
    private static double predict ( double[][] pVData, double ageTarget ) {
        //change the array into 2dim matrix,which get from  parameter
        DoubleMatrix2D pValue = new DenseDoubleMatrix2D(pVData);

        //make constant variant to absorb the model error
        double[] constant_ = new double[pValue.rows()];
        Arrays.fill(constant_, 1);
        DoubleMatrix2D constant = new DenseDoubleMatrix2D(new double[][]{constant_});

        //get label from the parameter,then reduce the rank(order)
        double[][] labels_ = {pValue.viewColumn(0).toArray()};
        DoubleMatrix2D Y = new DenseDoubleMatrix2D(labels_);
        Y.assign(x -> Math.log(x));

        //get original age from the parameter
        double[][] ages_ = {pValue.viewColumn(1).toArray()};
        DoubleMatrix2D agesOriginal = new DenseDoubleMatrix2D(ages_);

        //init a independent variable matrix and fill(append) data into it
        DoubleMatrix2D xStep1 = new DenseDoubleMatrix2D(pValue.rows(), 1);
        xStep1.assign(operator.transpose(constant));
        DoubleMatrix2D xStep2 = appender.appendColumns(xStep1,
                operator.transpose(agesOriginal)
        );

        DoubleMatrix2D agesPower = agesOriginal.copy();
        agesPower.assign(x -> Math.pow(x, 1.5));
        DoubleMatrix2D xStep3 = appender.appendColumns(xStep2,
                operator.transpose(agesPower)
        );

        DoubleMatrix2D agesReduce = agesOriginal.copy();
        agesReduce.assign(x -> customLog(x, Math.exp(0.5)));
        DoubleMatrix2D X = appender.appendColumns(xStep3,
                operator.transpose(agesReduce)
        );
        ///construct the independent variable matrix completely
        /////////////////////////////////////////////////////////////

        ///start solve the equation:f(age|c,θ) = pValue;
        // C,θ = (X.T * X)^-1 * X.T * Y

        DoubleMatrix2D X_T = operator.transpose(X);
        DoubleMatrix2D squareMat = operator.mult(X_T, X);
        DoubleMatrix2D invMat = operator.inverse(squareMat);
        DoubleMatrix2D C_θ = operator.mult(operator.mult(invMat, X_T), operator.transpose(Y));

        //predict the pValue by target age
        double[][] target_ = {{1, ageTarget, Math.pow(ageTarget, 1.5), customLog(ageTarget, Math.exp(0.5))}};
        DenseDoubleMatrix2D XTarget = new DenseDoubleMatrix2D(target_);
        DoubleMatrix2D predictResult = operator.mult(XTarget, C_θ);

        //finally return, because reduced the rank(order),so restore it.
        return Math.exp(predictResult.toArray()[0][0]);
    }
}