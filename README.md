# 第八次红岩作业

## HTTP网络请求和数据分析

将HTTP网络分析和数据分析作为一个类，将HTTP请求成功的结果传给数据分析类进行解析，其中将数据更新到数据管理类，即DataManager类。

## DataManager

DataManager使用单例模式，并进行数据的管理，向外暴露提供数据的类。

## RecycleView的不同子项

对于不同类型的子项，建立相应的xml文件，同时为每个类型的子项建立ViewHolder，并通过getItemViewType(int position)方法为每个类型的子项设定属于自己的flag，在create和bind通过flag进行创建和加载。

## 下拉刷新和上拉加载

下拉刷新使用控件SwipeRefreshLayout，为其设置setOnRefreshListener，并在onRefresh()方法中调用DataManager的refresh方法。
上拉加载个人实现，首先写出上拉加载时最下面显示的加载xml和对应的ViewHolder，并在其设置flag，在recycleview最末端加载。由isLoading变量判断是否需要加载，需要加载则调用DataManager的loadMore方法加载下一页的数据。

## 效果展示(dbq，UI我尽力了。。。虽然还是丑哭了)

![show](img/show.gif)
