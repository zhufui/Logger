# Logger
我们在开发中经常会遇到，打包了一个release版本给测试去测试，测试过程中遇到bug，简单的bug当然一眼就能看出来，就怕遇到复杂的bug，而release又把日志关闭了，没法查看日志，如果想看日志，那么需要安装个debug版本的，让测试重新复现。<br>
鉴于上面开发中的痛点，我决定留个日志开关在App中，只有采用通关密语才能进入日志开关页面，将日志开启或关闭。<br>
日志打印是拿的https://github.com/orhanobut/logger中的代码。
<img src="https://github.com/zhufui/Logger/blob/master/screenshot/main.png" width="500" height="900"/>
<br>
<img src="https://github.com/zhufui/Logger/blob/master/screenshot/setting.png" width="500" height="900"/>
