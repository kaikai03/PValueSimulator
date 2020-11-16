# PValueSimulator
针对某些时候，标准参考线数据间隔太大，导致难以正确判断输入数值所处区间的问题。通过对参考线拟合，解决该问题。

<font color=#FF0000> ！</font>需要import第三方[矩阵运算库colt](https://dst.lbl.gov/ACSSoftware/colt/index.html)


##文件说明##

	-
	|-MainTest.java  运行例子，作为sample演示
	|-PValueSimulator.java 实际功能类

## 主要函数 ##

	PValueSimulation.predictTruePs(List<double[][]> standardDataList, double ageTarget )
	

	

- 功能描述：以WHO-BMI曲线为例，输入多条参考线，输入目标年龄，返回目标年龄对应的 每条参考线的预测p值。
- 参数2:   ageTarget，要进行估计的年龄，（禁止输入<=0的值）
- 参数1:   predictTruePs，参考标准数据，（禁止输入<=0的值）三层结构，<br>
&nbsp;&nbsp;&nbsp;&nbsp;为[[[结果，目标]，[结果，目标]]，[[结果，目标]，[结果，目标]]]],<br>
&nbsp;&nbsp;&nbsp;&nbsp;WHO-BMI标准为例子的话：[[[p值，年龄]，[p值，年龄]]，[[p值，年龄]，[p值，年龄]]]]<br>
&nbsp;&nbsp;&nbsp;&nbsp;最内层数组，为p值和年龄构成一组键值对，对应坐标空间中的1个点。<br>
&nbsp;&nbsp;&nbsp;&nbsp;因为要构造参考线，所以要求至少输入两个点，<br>
&nbsp;&nbsp;&nbsp;&nbsp;为了确保预测结果准确性，最好输入五个点至八个点，被预测点接近中间位置。<br>
&nbsp;&nbsp;&nbsp;&nbsp;中间层可理解为，由内层点构成的线，其实就是我们p值的曲线<br>
&nbsp;&nbsp;&nbsp;&nbsp;最外层是一个list，相当于我们p50、p75...多条曲线,曲线数量可为非0的任意条。<br>
- 返回:  数组，返回的数组中的值为ageTarget对应的p值，数组的下标等同于入参时参考线的顺序。
- 提示： 这个东西的主要耗时在初始化，由于全是静态方法，所以可以作为全局对象运行时只初始化一次。