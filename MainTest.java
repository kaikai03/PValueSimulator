package com.test;

import java.util.ArrayList;
import java.util.List;

public class MainTest {

	public static void main(String[] args) {
		/********************************************************************
		//功能描述：输入多条参考线，输入目标年龄， 返回目标年龄对应的 每条参考线的预测p值
		//参数2    ageTarget，要进行估计的年龄
		//参数1    predictTruePs，参考标准数据
		//        结构为[[[p值，年龄]，[p值，年龄]]，[[p值，年龄]，[p值，年龄]]]]
		//        最内层数组，为p值和年龄构成一组键值对，对应坐标空间中的1个点。
		//           因为要构造参考线，所以要求至少输入两个点，
		//           为了确保预测结果准确性，最好输入五个点以上。
		//	      中间层可理解为，由内层点构成的线，其实就是我们p值的曲线
		//        最外层是一个list，相当于我们p50、p75...多条曲线,曲线数量可为非0的任意条。
		//返回  数组，数组中的值为ageTarget对应的p值，数组的下标等同于入参时参考线的顺序。
		********************************************************************/
	    double[][] p15Data = {{69.604,9},{70.898,10},{72.093,11},{73.277,12},{74.416,13},{74.416,13}};
		double[][] p50Data = {{71.927,9},{73.265,10},{74.506,11},{75.739,12},{76.93,13}};
		double[][] p75Data = {{74.251,9},{75.633,10},{76.919,11},{78.202,12},{79.445,13},{79.445,13},{79.445,13}};

		List<double[][]> pList = new ArrayList <double[][]>();
		pList.add(p15Data);
		pList.add(p50Data);
		pList.add(p75Data);

		pList.add(p50Data);
		pList.add(p75Data);

		List<Double> result = PValueSimulator.predictTruePs(pList, 11.8);

        System.out.println(result);
	}

}
