package com.iiitd.dsavisualizer.algorithms.sorting.insertion;

import android.content.Context;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iiitd.dsavisualizer.R;
import com.iiitd.dsavisualizer.algorithms.sorting.AnimationState;
import com.iiitd.dsavisualizer.algorithms.sorting.ElementAnimationData;
import com.iiitd.dsavisualizer.constants.AppSettings;
import com.iiitd.dsavisualizer.runapp.others.AnimationDirection;
import com.iiitd.dsavisualizer.utility.Util;
import com.iiitd.dsavisualizer.utility.UtilUI;

import java.util.ArrayList;
import java.util.Random;

public class InsertionSort {

    final Context context;
    final int arraySize;
    int[] data;
    InsertionSortData[] insertionSortData;
    View[] views;
    int[] positions;
    final LinearLayout linearLayout;
    InsertionSortSequence sequence;
    final Random random;
    float width;
    float height;
    int textSize;
    final boolean isRandomize;
    final int[] rawInput;
    int comparisons;
    final ArrayList<Pair<Integer, Integer>> sortedIndexes;

    public InsertionSort(Context context, LinearLayout linearLayout, int arraySize) {
        this.context = context;
        this.random = new Random();
        this.arraySize = arraySize;
        this.isRandomize = true;
        this.linearLayout = linearLayout;
        this.comparisons = 0;
        this.sortedIndexes = new ArrayList<>();
        rawInput = null;

        init();
    }

    public InsertionSort(Context context, LinearLayout linearLayout, int[] rawInput) {
        this.context = context;
        this.random = new Random();
        this.arraySize = rawInput.length;
        this.isRandomize = false;
        this.linearLayout = linearLayout;
        this.comparisons = 0;
        this.sortedIndexes = new ArrayList<>();
        this.rawInput = rawInput;

        init();
    }

    private void init() {
        if(arraySize > 8){
            textSize = AppSettings.TEXT_SMALL;
        }
        else{
            textSize = AppSettings.TEXT_MEDIUM;
        }

        int totalWidth = linearLayout.getWidth();
        int totalHeight = linearLayout.getHeight();
        this.width = (float) totalWidth / arraySize;
        this.height =  (float) totalHeight;

        int MAX = 0;

        this.data = new int[arraySize];
        this.insertionSortData = new InsertionSortData[arraySize];
        this.views = new View[arraySize];
        this.positions = new int[arraySize];
        if(isRandomize){
            for (int i = 0; i < data.length; i++) {
                data[i] = random.nextInt(20) + 1;
                MAX = Math.max(data[i], MAX);
            }
        }
        else{
            for (int i = 0; i < data.length; i++) {
                data[i] = rawInput[i];
                MAX = Math.max(data[i], MAX);
            }
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, (int) height, 1);
        LayoutInflater vi = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i=0;i<data.length;i++){
            float x = (float)data[i] / (float)MAX;
            float h = (x * .75f) + .20f;

            View myView = vi.inflate(R.layout.element_insertion_sort, null);
            myView.setLayoutParams(layoutParams);
            myView.setPadding(5,5,5,5);
            TextView tv = myView.findViewById(R.id.tv_elementvalue);
            tv.setText(String.valueOf(data[i]));
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(textSize);
            tv.getLayoutParams().height = (int) (height * h);
            tv.setBackground(UtilUI.getDrawable(context, AppSettings.ROUNDED_RECT_ELEMENT));
            linearLayout.addView(myView);

            InsertionSortData mergeSortData1 = new InsertionSortData();
            mergeSortData1.data = data[i];
            mergeSortData1.index = i;
            views[i] = myView;
            positions[i] = i;
            insertionSortData[i] = mergeSortData1;
        }

        this.sequence = new InsertionSortSequence(0);
        this.sequence.setViews(views);
        this.sequence.setPositions(positions);
        this.sequence.setAnimateViews(height, width, context);

        this.insertionsort();
    }

    public void forward(){
        sequence.forward();
    }

    public void backward(){
        sequence.backward();
    }

    private void insertionsort() {
        AnimationState animationState = new AnimationState(InsertionSortInfo.BS, InsertionSortInfo.getBubbleSortString());
        for(int i = 0; i< insertionSortData.length; i++){
            animationState.addElementAnimationData(new ElementAnimationData(insertionSortData[i].index, new Pair<>(AnimationDirection.NULL, 1)));
            animationState.addHighlightIndexes(insertionSortData[i].index);
        }
        sequence.addAnimSeq(animationState);
        insertion(insertionSortData);
    }

    private void insertion(InsertionSortData[] arr){
        int length = arr.length;
        boolean flag = false;

        for (int i = 0; i < length; i++) {
            flag = false;

            for (int j = 0; j < length - i - 1; j++) {
                comparisons++;
                if (arr[j].data > arr[j + 1].data) {
                    AnimationState animationState = new AnimationState(InsertionSortInfo.L_GREATER_R, InsertionSortInfo.getComparedString(arr[j].data, arr[j+1].data, j, j+1));
                    animationState.addHighlightIndexes(insertionSortData[j].index, insertionSortData[j+1].index);
                    animationState.addElementAnimationData(new ElementAnimationData(insertionSortData[j].index, new Pair<>(AnimationDirection.RIGHT, 1)));
                    animationState.addElementAnimationData(new ElementAnimationData(insertionSortData[j+1].index, new Pair<>(AnimationDirection.LEFT, 1)));
                    sequence.addAnimSeq(animationState);

                    Util.swap(arr[j], arr[j + 1]);
                    flag = true;
                }
                else{
                    AnimationState animationState = new AnimationState(InsertionSortInfo.L_LESSEQUAL_R, InsertionSortInfo.getComparedString(arr[j].data, arr[j+1].data, j, j+1));
                    animationState.addHighlightIndexes(insertionSortData[j].index, insertionSortData[j+1].index);
                    animationState.addElementAnimationData(new ElementAnimationData(insertionSortData[j].index, new Pair<>(AnimationDirection.NULL, 1)));
                    animationState.addElementAnimationData(new ElementAnimationData(insertionSortData[j+1].index, new Pair<>(AnimationDirection.NULL, 1)));
                    sequence.addAnimSeq(animationState);
                }

            }

            if(!flag){
                AnimationState animationState = new AnimationState(InsertionSortInfo.FLAG, InsertionSortInfo.getFlagString());
                sequence.addAnimSeq(animationState);
                for(int k=0;k<length-i-1;k++){
                    sortedIndexes.add(new Pair<>(sequence.animationStates.size(), arr[k].index));
                }
                return;
            }

            sortedIndexes.add(new Pair<>(sequence.animationStates.size(), arr[length-i-1].index));
        }
    }

}
