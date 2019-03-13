spring cloud 的实例<br/>

先启动eureka注册服务器<br/>
然后启动config-server,注册到eureka上<br/>
<br/>
其他服务均通过服务发现的方式去获取config<br/>

eureka和config server都启用了简单的security验证,用户名密码都是test<br/>
