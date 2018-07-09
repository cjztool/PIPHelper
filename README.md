# PIPHelper 画中画辅助工具

A convenient tool to realize picture in picture mode of playing video. check permission automticly.

一个实现画中画模式下播放视频的辅助工具， 运行时自动申请悬浮窗权限。

# Usage 用法

1. Declear permission 声明悬浮窗权限

```xml
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```
2. as follow: 调用如下
```java
        PIPHelper.get() // get instance of PIPHelper. 单例
                //.reMeetDismissFloatOne() //dismiss pip when reEnter 再次进入播放页面时消失掉画中画
                .reMeetReplaceNewByFloatOne() // replace by float 再次进入时用画中画替换新的
                //.reMeetDoNothing() // do nothing 啥也不干
                .floatViewDisableDrag() // set pip can not be dragged 悬浮播放后禁止拖拽，默认可以
                .onBackKeyUpAutoFloat() // back key 返回键按下后自动进入pip
                .onEnterPIPListener(new PIPHelper.EnterPIPListener() { //进入画中画模式下的回调
                    @Override
                    public void onEnter(View floatRootView) {//返回了画中画的根节点view 可以在这个view上添加相应的菜单
                        
                    }
                })
                .initTarget(playerView) // which view will be floated , enter pip mode.指定要进入画中画模式的view
                .floatView();//enter pip 悬浮指定视图
```

# More improvements coming... 更多改进欢迎pr