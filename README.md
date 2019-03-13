spring cloud 的实例<b/>

先启动eureka注册服务器<b/>
然后启动config-server,注册到eureka上<b/>
<b/>
其他服务均通过服务发现的方式去获取config<b/>

eureka和config server都启用了简单的security验证,用户名密码都是test<b/>