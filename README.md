<p align="center"> 
  <img height="325px" src="/images/logo.png">
</p>

# Segmento
Simple android view component that can be used to showing a round segmented progress bar

<p float="left">
  <img src="/images/size.gif" width="310" />
  <img src="/images/remove.gif" width="310" /> 
  <img src="/images/shadow.gif" width="310" />
</p>

## Usage

### XML

```xml
 <com.z4.segmento.SegmentedProgressBar
        android:layout_width="@dimen/segmented_progress_bar_size"
        android:layout_height="@dimen/segmented_progress_bar_size"
        app:scaleFactor="@integer/scale_anim_factor"
        app:scaleAnimDuration="@integer/scale_anim_duration"
        app:inner_progress_color="@color/innerProgressColor"
        app:inner_progress_width="@dimen/segmented_progress_bar_outer_circle_width"
        app:outer_progress_color="@color/outerProgressBlueColor"
        app:outer_progress_width="@dimen/segmented_progress_bar_outer_circle_width"
        app:max_progress="@integer/segmented_progress_bar_max_progress"
        app:progress="@integer/segmented_progress_bar_progress"
        app:clock_wise="true"
        app:segment_color="@color/segmentColor"
        app:segment_size="@dimen/segment_size"
        app:shadow_width="@dimen/segmented_progress_bar_shadow_width" />
```

### Programmatically
##### Style:

```java
 mSegmentedProgressBar.setOuterProgressColor(Color.WHITE);
 mSegmentedProgressBar.setInnerProgressColor(Color.RED);
 mSegmentedProgressBar.setSegmentColor(Color.YELLOW);
  
 mSegmentedProgressBar.setOuterProgressColor(getProgressColors());
 mSegmentedProgressBar.setOuterProgressColor(getProgressColors(), positions);
 
 mSegmentedProgressBar.setShadowWidth(10);
 mSegmentedProgressBar.setOuterProgressWidth(15);
 mSegmentedProgressBar.setInnerProgressWidth(15);
 mSegmentedProgressBar.setSegmentSize(3);
```

##### Progress:

```java
 mSegmentedProgressBar.setMaxProgress(100);
 mSegmentedProgressBar.setProgress(50);
 mSegmentedProgressBar.setProgressWithAnimation(50, 1000);
 mSegmentedProgressBar.setClockwise(true);
 
 //scale in animation value
 mSegmentedProgressBar.setScaleFactor(1.5f);
```

##### Segments:

```java
 mSegmentedProgressBar.getSegments();
 mSegmentedProgressBar.getSegmentsCount();
 mSegmentedProgressBar.removeAllSegments();
 mSegmentedProgressBar.removeLastSegment();
```

##### OnProgressbarChangeListener:

```java
 mSegmentedProgressBar.setOnProgressbarChangeListener(new SegmentedProgressBar.OnProgressbarChangeListener() {
            
          @Override
          public void onProgressChanged(SegmentedProgressBar progressBar, float progress) {
              // Progress changed
          }

          @Override
          public void onStartTracking(SegmentedProgressBar progressBar) {
              // Start tracking
          }

          @Override
          public void onStopTracking(SegmentedProgressBar progressBar) {
              // Stop tracking
          }
     });
```
##### OnSegmentCountChangeListener:

```java
 mSegmentedProgressBar.setOnSegmentCountChangeListener(new OnSegmentCountChangedListener() {
            
          @Override
          public void onSegmentCountChanged(SegmentedProgressBar progressBar, int segmentCount) {
               // Get segment count
          }
     });
```
Installation
--------

```groovy
dependencies {
  compile 'com.github.z-four:segmento:1.0.3'
}
```

```groovy
allprojects {
    repositories {
        ...
        maven {
            url "https://jitpack.io"
        }
    }
}
```

License
-------

Copyright 2017 Dmitriy Zhyzhko

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
