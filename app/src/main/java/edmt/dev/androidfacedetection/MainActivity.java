package edmt.dev.androidfacedetection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button btnProgress;

    Bitmap eyePatchBitmap;
    Bitmap flowerLine;
    Canvas canvas;

    Paint rectPaint = new Paint();

    private void drawEyePatchBitmap(int landmarkType, float cx, float cy) {

//        if (landmarkType == Landmark.LEFT_EYE) {
//            // TODO: Optimize so that this calculation is not done for every face
//            int scaledWidth = eyePatchBitmap.getScaledWidth(canvas);
//            int scaledHeight = eyePatchBitmap.getScaledHeight(canvas);
//            canvas.drawBitmap(eyePatchBitmap, cx - (scaledWidth / 2)+20, cy - (scaledHeight / 2), null);
//        }

        if(landmarkType == Landmark.NOSE_BASE)
        {
            int scaledWidth = flowerLine.getScaledWidth(canvas);
           int scaledHeight = flowerLine.getScaledHeight(canvas);
           canvas.drawBitmap(flowerLine, cx - (scaledWidth/2), cy-(scaledHeight*2), null);
            canvas.drawBitmap(eyePatchBitmap,cx-500,cy-(scaledHeight)+120,null);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.imageView);
        btnProgress = (Button)findViewById(R.id.btnProgress);

        final Bitmap myBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.tom);
        imageView.setImageBitmap(myBitmap);

        eyePatchBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.eye_patch);
        flowerLine = BitmapFactory.decodeResource(getResources(),R.drawable.flower);

        rectPaint.setStrokeWidth(5);
        rectPaint.setColor(Color.WHITE);
        rectPaint.setStyle(Paint.Style.STROKE);

        final Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(),myBitmap.getHeight(), Bitmap.Config.RGB_565);
        canvas  = new Canvas(tempBitmap);
        canvas.drawBitmap(myBitmap,0,0,null);

        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .setMode(FaceDetector.FAST_MODE)
                        .build();
                if(!faceDetector.isOperational())
                {
                    Toast.makeText(MainActivity.this, "Face Detector could not be set up on your device", Toast.LENGTH_SHORT).show();
                    return;
                }
                Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                SparseArray<Face> sparseArray = faceDetector.detect(frame);

                for(int i=0;i<sparseArray.size();i++)
                {
                    Face face = sparseArray.valueAt(i);
//                    float x1=face.getPosition().x;
//                    float y1 =face.getPosition().y;
//                    float x2 = x1+face.getWidth();
//                    float y2=y1+face.getHeight();
//                    //RectF rectF = new RectF(x1,y1,x2,y2);
//                    canvas.drawRoundRect(rectF,2,2,rectPaint);

                    detectLandmarks(face);

                }

                imageView.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));

            }
        });

    }

    private void detectLandmarks(Face face) {
        for (Landmark landmark : face.getLandmarks()) {

            int cx = (int) (landmark.getPosition().x);
            int cy = (int) (landmark.getPosition().y);

          


            drawEyePatchBitmap(landmark.getType(), cx, cy);
        }
    }
}
