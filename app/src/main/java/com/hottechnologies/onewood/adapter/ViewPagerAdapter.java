//package com.hottechnologies.onewoodvpn;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.viewpager.widget.PagerAdapter;
//
//public class ViewPagerAdapter extends PagerAdapter {
//
//    Context context;
//
//    int images [] = {
//
//            R.drawable.boardone,
//            R.drawable.boardtwo,
//            R.drawable.boardthree
//    };
//
//    int Heading [] ={
//            R.string.firstBoardFirstText,
////            R.string.firstBoardSecondText,
//            R.string.secondBoardFirstText,
////            R.string.secondBoardSecondText,
//            R.string.thirdBoardFirstText,
////            R.string.thirdBoardSecondText
//    };
//
//    int description[] = {
//            R.string.firstBoardSecondText,
//            R.string.secondBoardSecondText,
//            R.string.thirdBoardSecondText
//    };
//
//    public ViewPagerAdapter(Context context){
//        this.context = context;
//    }
//
//    @Override
//    public int getCount() {
//        return Heading.length;
//    }
//
//    @Override
//    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//        return view == (LinearLayout) object;
//    }
//
//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position){
//
//        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//        View view = layoutInflater.inflate(R.layout.slider_layout,container,false);
//
//        ImageView sliderImage = (ImageView) view.findViewById(R.id.board);
//        TextView FirstText = (TextView) view.findViewById(R.id.firstTitle);
//        TextView secondText  = (TextView) view.findViewById(R.id.secondTitle);
//
//        sliderImage.setImageResource(images[position]);
//        FirstText.setText(Heading[position]);
//        secondText.setText(description[position]);
//
//        container.addView(view);
//
//        return view;
//    }
//
//    @Override
//    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        container.removeView((LinearLayout)object);
//    }
//}

package com.hottechnologies.onewood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.hottechnologies.onewood.R;

//import com.lazycoder.cakevpn.R;


public class ViewPagerAdapter extends PagerAdapter {

    Context context;

    int images[] = {

            R.drawable.boardone,
            R.drawable.boardtwo,
            R.drawable.boardthree

    };

    int headings[] = {

            R.string.firstBoardFirstText,
////            R.string.firstBoardSecondText,
            R.string.secondBoardFirstText,
////            R.string.secondBoardSecondText,
            R.string.thirdBoardFirstText,
////            R.string.thirdBoardSecondText
    };

    int description[] = {
            R.string.firstBoardSecondText,
            R.string.secondBoardSecondText,
            R.string.thirdBoardSecondText
    };

    public ViewPagerAdapter(Context context){

        this.context = context;

    }

    @Override
    public int getCount() {
        return  headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout,container,false);

        ImageView slidetitleimage = (ImageView) view.findViewById(R.id.board);
        TextView slideHeading = (TextView) view.findViewById(R.id.firstTitle);
        TextView slideDesciption = (TextView) view.findViewById(R.id.secondTitle);

        slidetitleimage.setImageResource(images[position]);
        slideHeading.setText(headings[position]);
        slideDesciption.setText(description[position]);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((LinearLayout)object);

    }
}
