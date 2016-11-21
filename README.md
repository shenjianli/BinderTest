# BinderTest
Android中使用Binder进行进程间通信

## 实例效果：
## 实例结构：
![实例结构图](img/structure.png)
## 关键代码：
![aidl生成的java文件](img/aidl_build_java_file.png)

![Service在另一进程中](img/put_service_to_other_thread.png)
## 知识介绍：
![aild生成文件UML](img/Aidl_binder_uml.png)

![实例进程UML](img/binder_two_thead.png)
在Android中，Binder用于完成进程间通信（IPC）,即把多个进程“别”在一起，比如，普通应用程序可以调用音乐播放服务提供的播放，暂停，停止等功能！

Binder驱动
重载了transact()方法，主要包括以下内容
1.以线程间消息通信的模式，向服务端发送客户端传递过来的参数
2.挂起当前线程，当前线程正是客户端线程，并等待服务器线程执行完指定服务函数通知（notify）
3.接收到服务端线程通知，然后继续执行客户端线程，并返回到客户端代码区

### 设计Servicer端
1. 继承Binder类新建一个Service类
2. 重载onTransact()方法，从data变量中读出客户端传递的参数（data中的变量的位置需要客户端和服务端进行约定）
3. onTransact中的参数code用于标识客户端期望调用服务端那个函数，需要双方进行约定
4. enforceInterface()是为了某种校验，它与客户端的writeInterfaceToken相对应

### Binder客户端
### 使用Service类
以上Binder服务端和客户端存在两个问题：
**1.客户端如何获得服务端的Binder对象引用**
**2.客户端和服务端必须事先约定好两件事，服务端函数的参数在包裹中的顺序，服务端不同函数的int标识**

public boolean bindService(Intent service,ServiceConnection conn,int flags)

这个函数用于绑定一个服务，其中第二个参数interface类定义如下
```
public interface ServiceConnection{
	public void onServiceConnected(ComponentName name ,IBinder service);
    public void onServiceDisconnected(ComponentName name);	
}
```
当客户端请求Ams启动某个Service后，该Service如果正常启动，那么Ams就会远程调用ActivityThread类中的ApplicationThread对象，调用参数中会包含Service的Binder引用，然后在ApplicationThread中会回调bindService中的conn接口。因此，在客户端中，可以在onServiceConnected()方法中将其参数Service保存为一个全局变量，从而在客户端的任何地方都可以调用该远程服务！

### 保证包裹内参数顺序aidl工具的使用
aidl 可以把aidl文件转化为一个Java类文件，这个文件同时重载了transact和onTransact()方法，统一了存入包裹和读取包裹参数


参考文献：
[http://blog.csdn.net/huachao1001/article/details/51504469](http://blog.csdn.net/huachao1001/article/details/51504469)
