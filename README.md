# Two Side Chooser

Two side chooser for accept and ignore situation

[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)
[![](https://jitpack.io/v/radeshf/TwoSideChooser.svg)](https://jitpack.io/#radeshf/TwoSideChooser)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/radeshf/TwoSideChooser/blob/master/LICENSE)

<img src="/arts/ex2.jpg?raw=true" width="300px">              <img src="/arts/ex1.jpg?raw=true" width="300px">


### Dependency

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
   repositories {
	...
	maven { url "https://jitpack.io" }
   }
}
```

Then, add the library to your project `build.gradle`
```gradle
dependencies {
    implementation 'com.github.radeshf:TwoSideChooser:1.0.0'
}
```
##### Example 1

<img src="/arts/ex1.gif?raw=true" width="300px">

* XML :

``` XML
    <radesh.twosidechooser.Chooser
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/chooser"
            app:acceptDrawable="@drawable/ic_accept_ex1"
            app:acceptDrawableSize="50dp"
            app:acceptBackgroundColor="#ffca05"
            app:ignoreDrawable="@drawable/ic_ignore_ex1"
            app:ignoreDrawableSize="50dp"
            app:ignoreBackgroundColor="#ffca05"
            app:arrowsDrawable="@drawable/ic_arrows_ex1"
            app:arrowsDrawableRotation="180"
            app:chooserBackgroundColor="#124743"
            app:drawablesPadding="5dp"
            app:thumbDrawable="@drawable/ic_candidate"
    />
```

##### Example 2

<img src="/arts/ex2.gif?raw=true" width="300px">

* XML :

``` XML
    <radesh.twosidechooser.Chooser
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/chooser"
            app:enableSelectWithClick="false"
            app:enableNearbyAnimation="true"
            app:enableReturnToCenter="true"
            app:acceptDrawableSize="55dp"
            app:ignoreDrawableSize="55dp"
            app:ignoreDrawableRotation="135"
            app:chooserBackgroundColor="#00ffffff"
            app:drawablesPadding="10dp"
            app:acceptValue="90"
            app:ignoreValue="10"
            app:thumbDrawable="@drawable/ic_fingerprint_ex2"
    />

```

* Kotlin :

For listening user swipe to accept or ignore 

``` kotlin
chooser.setOnSwipeEndListener(object : onSwipeEndListener{
            override fun onAccept() {
                Log.i(tag,"Accepted")
		//return to center with animation
		chooser.smoothReturnToCenter()
            }
            
            override fun onIgnore() {
                Log.i(tag,"Ignored")
		//return to center promptly
		chooser.returnToCenter()
            }
        })
```

Use this if you want to be approved promptly 

``` kotlin
//approved promptly when you swipe to end or start
chooser.enableDoWithoutStopTracking(true)
//sets acceptValue to 100 and calculate ignore value (ignoreValue = 0)
chooser.setAcceptFinalValue(100,true)
```

Some Other functions 

``` kotlin
//sets acceptValue to 80 and calculate ignore value (ignoreValue = 20)
chooser.setAcceptValue(80,true)
// when enter this value animation will be start (when arrive above 60 and below 40)
chooser.setAcceptAnimationValue(60,true)
// change animation when arrive animation value(set above)
chooser.setMovementAnimation(customAnimation)
//if you want hide arrows use 0f
chooser.setArrowsImagesAlpha(0.8f)
//set drawable padding to look better
chooser.setDrawablePadding(resources.getDimensionPixelOffset(R.dimen.padding))
//set chooser background 
chooser.setChooserBackgroundColor(resources.getColor(R.color.colorAccent))
```
* Configure using xml attributes or setters in code:

    <table>
    <th>Attribute Name</th>
    <th>Default Value</th>
    <th>Description</th>
    <tr>
        <td>app:acceptValue="10"</td>
        <td>85</td>
        <td>when arrives to this value fire onAccept()\n must be in 55 to 100</td>
    </tr>
    <tr>
        <td>app:ignoreValue="90"</td>
        <td>15</td>
        <td>when arrives to this value fire onIgnore() \n must be in 0 to 45</td>
    </tr>
    <tr>
        <td>app:acceptAnimationValue="70"</td>
        <td>60</td>
        <td>after this value will played animation for accept button \n must be in 55 to 100</td>
    </tr>
    <tr>
        <td>app:ignoreAnimationValue="20"</td>
        <td>30</td>
        <td>after this value will played animation for ignore button \n must be in 0 to 45</td>
    </tr>
    <tr>
        <td>app:centerProgress="50"</td>
        <td>50</td>
        <td>thumb return to this progress</td>
    </tr>
    <tr>
        <td>app:enableNearbyAnimation="false"</td>
        <td>true</td>
        <td>enable/disable animation when thumb close to buttons</td>
    </tr>
    <tr>
        <td>app:enableReturnToCenter="false"</td>
        <td>true</td>
        <td>return to center when thumb release and not in accept/ignore value if `true`</td>
    </tr>
    <tr>
        <td>app:enableSelectWithClick="true"</td>
        <td>false</td>
        <td>prevent to accept with single tap on buttons (swipe not necessary if `true`)</td>
    </tr>
    <tr>
        <td>app:acceptDrawable="@drawable/ic_accept"</td>
        <td>ic_accept</td>
        <td>sets accept drawable</td>
    </tr>
    <tr>
        <td>app:ignoreDrawable="@drawable/ic_ignore"</td>
        <td>ic_accept</td>
        <td>sets ignore drawable</td>
    </tr>
    <tr>
        <td>app:acceptBackgroundDrawable="@drawable/accept_bg"</td>
        <td>border_accept</td>
        <td>sets accept background drawable</td>
    </tr>
    <tr>
        <td>app:ignoreBackgroundDrawable="@drawable/ignore_bg"</td>
        <td>chooser_border_ignore</td>
        <td>sets ignore background drawable</td>
    </tr>
    <tr>
        <td>app:acceptBackgroundColor="@color/accept_color"</td>
        <td>#15AE15</td>
        <td>sets accept background drawable color</td>
    </tr>
    <tr>
        <td>app:ignoreBackgroundColor="@color/ignore_color"</td>
        <td>#D91A1A</td>
        <td>sets ignore background drawable color</td>
    </tr>
    <tr>
        <td>app:arrowsDrawable="@drawable/ic_arrows"</td>
        <td>ic_arrows</td>
        <td>sets arrows drawable together</td>
    </tr>
    <tr>
        <td>app:arrowsDrawableRotation="180"</td>
        <td>0</td>
        <td>rotate both arrows</td>
    </tr>
    <tr>
        <td>app:acceptDrawableRotation="90"</td>
        <td>0</td>
        <td>rotate accept drawable</td>
    </tr>
    <tr>
        <td>app:ignoreDrawableRotation="135"</td>
        <td>0</td>
        <td>rotate ignore drawable</td>
    </tr>
    <tr>
        <td>app:thumbDrawable="@drawable/ic_thumb"</td>
        <td>ic_thumb</td>
        <td>sets thumb drawable</td>
    </tr>
    <tr>
        <td>app:drawablesPadding="0dp"</td>
        <td>10dp</td>
        <td>sets accept/ignore drawables padding(recommended)</td>
    </tr>
    <tr>
        <td>app:acceptDrawableSize="50dp"</td>
        <td>40dp</td>
        <td>sets accept drawable size</td>
    </tr>
    <tr>
        <td>app:ignoreDrawableSize="50dp"</td>
        <td>40dp</td>
        <td>sets ignore drawable size</td>
    </tr>
    <tr>
        <td>app:chooserBackgroundColor="@color/chooser_color"</td>
        <td>#008577</td>
        <td>sets chooser background color</td>
    </tr>

    </table>

License
-------

   Copyright 2019 Radesh Farokh Manesh

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
limitations under the License.
