# PIPHelper

A convenient tool to realize picture in picture mode of playing video.

# Usage

```java
        PIPHelper.get() // get instance of PIPHelper.
                //.reMeetDismissFloatOne() //dismiss pip when reEnter 
                .reMeetReplaceNewByFloatOne() // replace by float
                //.reMeetDoNothing() // do nothing
                .floatViewDisableDrag() // set pip can not be dragged
                .onBackKeyUpAutoFloat() // back key
                .onEnterPIPListener(new PIPHelper.EnterPIPListener() {
                    @Override
                    public void onEnter(View floatRootView) {
                        
                    }
                })
                .initTarget(playerView); // which view will be floated , enter pip mode.
```

# More improvements coming...