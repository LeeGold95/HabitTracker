package listapp.habittracker.mainscreen;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

/*
This class manages the animation appearance and update of the main activity progress bar.
 */

public class ProgressBarAnimation extends Animation {

        private ProgressBar progressBar;
        private int start;
        private int end;

        public ProgressBarAnimation(ProgressBar progressBar, int start, int end) {
            super();
            this.progressBar = progressBar;
            this.start = start;
            this.end = end;
            this.setDuration(1000);
        }


        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = (float) (start) + (float)(end - start) * interpolatedTime;
            progressBar.setProgress((int) value);
        }


        public void changeProgress(int end){
            this.start = progressBar.getProgress();
            this.end = end;

            if(start==0&&end==0){
                this.start = 1; //can't animate 0 to 0 --> animate small unseen animation 1 to 0.
            }
            progressBar.startAnimation(this);
        }

}
