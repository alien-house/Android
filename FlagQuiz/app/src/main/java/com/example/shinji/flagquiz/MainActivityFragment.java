package com.example.shinji.flagquiz;

import android.app.*;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
//import java.util.logging.Handler;

/**
 * Created by shinji on 2017/07/31.
 */

public class MainActivityFragment extends Fragment{
    private final int FLAGS_IN_QUIZ = 10;
    //TODO 11) Create a list for quiz
    private List<String> fileNameList;
    private List<String> quizCountriesList;
    private Set<String> regionsSet;

    //TODO 5) CREATE a reference variable for all GUI componnets
    private LinearLayout quizLinearLayout; // layout that contains the quiz
    private TextView questionNumberTextView; // shows current question #
    private ImageView flagImageView; // displays a flag
    private LinearLayout[] guessLinearLayouts; // rows of answer Buttons
    private TextView answerTextView; // displays correct answer
    private SecureRandom random; // used to randomize the quiz
    private String correctAnswer; // number of correct guesses
    private int guessRows; // number of rows displaying guess Buttons
    private int count = 1;
    public int countGuess = 0;
    private Handler handle;
    private Animation shakeAnimation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //TODO 6) creating the instance of securerandom class.
        random = new SecureRandom();
        fileNameList = new ArrayList<>();
        quizCountriesList = new ArrayList<>();

        // TODO 7) get references to GUI components
        quizLinearLayout = (LinearLayout)view.findViewById(R.id.quizLinearLayout);
        questionNumberTextView = (TextView)view.findViewById(R.id.questionNumberTextView);
        flagImageView = (ImageView)view.findViewById(R.id.flagImageView);
        guessLinearLayouts = new LinearLayout[4];
        guessLinearLayouts[0] = (LinearLayout)view.findViewById(R.id.row1LinearLayout);
        guessLinearLayouts[1] = (LinearLayout)view.findViewById(R.id.row2LinearLayout);
        guessLinearLayouts[2] = (LinearLayout)view.findViewById(R.id.row3LinearLayout);
        guessLinearLayouts[3] = (LinearLayout)view.findViewById(R.id.row4LinearLayout);
        answerTextView = (TextView) view.findViewById(R.id.answerTextView);

        // intialize the animation reference variable
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.incorrect_shake);
        shakeAnimation.setRepeatCount(2);

        handle = new Handler();
        return view;
    }


    // TODO 8) update guessRows based on value in SharedPreferences
    public void updateGuessRows(SharedPreferences sharedPreferences) {
        // get the number of guess buttons that should be displayed
        String choices = sharedPreferences.getString(MainActivity.CHOICES, null);

        guessRows = Integer.parseInt(choices) / 2;
        // hide all quess button LinearLayouts
        for (LinearLayout layout : guessLinearLayouts)
            layout.setVisibility(View.GONE);

        // display appropriate guess button LinearLayouts
        for (int row = 0; row < guessRows; row++)
            guessLinearLayouts[row].setVisibility(View.VISIBLE);
    }

    // TODO 9) update world regions for quiz based on values in SharedPreferences
    public void updateRegions(SharedPreferences sharedPreferences) {
        //set of countries
        regionsSet = sharedPreferences.getStringSet(MainActivity.REGIONS,null);
    }

    // TODO 12 ) クイズの設定と、次のクイズの準備
    public void startQuiz() {
        //for restart
        count = 1;
        countGuess = 0;
        int i = 0;
        AssetManager assets = getActivity().getAssets();

//        if(regionsSet.size() == 0) {
//            regionsSet.add("North_America");
//        }

        try {
            //　国籍のループ
            for (String region : regionsSet) {
                // 全てのフラグのループ
                Log.e("IN FLAG regionQUIZ : ", region);
                String[] paths = assets.list(region);
                for (String path : paths)
                    fileNameList.add(path.replace(".png", ""));
            }
        }catch (IOException exception) {
            Log.e("IN FLAG QUIZ : ", "Error loading image file names", exception);
        }
        int flagCounter = 1;
        int numberOfFlags = fileNameList.size();
        // 国のファイル名を入れて行く。最大の数まで（今回10コマで）
        while (flagCounter <= FLAGS_IN_QUIZ) {
            int randomIndex = random.nextInt(numberOfFlags);
            String filename = fileNameList.get(randomIndex);
            // その国が可能なら入れて行く
            if (!quizCountriesList.contains(filename)) {
                quizCountriesList.add(filename);
                ++flagCounter;
            }
        }
        loadtheflag();
    }

    // TODO 13) フラグ画像を読む
    private void loadtheflag() {

        // set questionNumberTextView's text
        // Question %1$d of %2$d
        questionNumberTextView.setText(getString(R.string.question, count, FLAGS_IN_QUIZ));

        // 次のフラグ名を取得、それをリストから削除
        String nextImage = quizCountriesList.remove(0);
        correctAnswer = nextImage; // 正解を更新

        answerTextView.setText(""); // テキストビューを綺麗に
        //国の名前をゲット、フラグから
        String region = nextImage.substring(0, nextImage.indexOf('-'));
        AssetManager assets = getActivity().getAssets();
        // InputStreamを使って次の国のフラグを取得
        try (InputStream stream = assets.open(region + "/" + nextImage + ".png")) {
            // Drawableとして表示する
            Drawable flag = Drawable.createFromStream(stream, nextImage);
            flagImageView.setImageDrawable(flag);
        }
        catch (IOException exception) {
            Log.e("in the flag quiz ", "Error loading " + nextImage, exception);
        }
        //名前を混ぜる
        Collections.shuffle(fileNameList);

        //正解をfileNameListの最後に入れる
        int correct = fileNameList.indexOf(correctAnswer);
        fileNameList.add(fileNameList.remove(correct));
        //ボタン数の表示有無の変化
        for (int row = 0; row < guessRows; row++) {
            // currentTableRowを置く
            for (int column = 0; column < guessLinearLayouts[row].getChildCount(); column++) {
                // 設定による数を可能不可能に
                Button newGuessButton = (Button) guessLinearLayouts[row].getChildAt(column);
                newGuessButton.setEnabled(true);
                newGuessButton.setOnClickListener(guessButtonListener);
                // 国名の取得、newGuessButtonのテキストをセット
                String filename = fileNameList.get((row * 2) + column);
                newGuessButton.setText(getCountryName(filename));
            }
        }

        // ボタンをランダムに置く、正しい答えと
        int row = random.nextInt(guessRows); // ランダムローをピック
        int column = random.nextInt(2); // ランダムコラムをピック
        LinearLayout randomRow = guessLinearLayouts[row]; // ローのゲット
        String countryName = getCountryName(correctAnswer);
        ((Button) randomRow.getChildAt(column)).setText(countryName);

        //PLACE THIS CODE IN ONCREATEVIEW()
        //TODO 15) Add the listeneer for all buttons
        // configure listeners for the guess Buttons
    }


    private void loadNextFlag(){
        Log.e("kore","next=================");
        loadtheflag();
    }

    private View.OnClickListener guessButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button guessButton = ((Button) v);
            String guess = ((Button) v).getText().toString();
            String answer = getCountryName(correctAnswer);

            //if the guess is correct
            // display correct answer in green text
            if(guess.equals(answer)){
                answerTextView.setText(answer + "!");
                //set the text in green Color
                answerTextView.setTextColor(getResources().getColor(R.color.correct_answer));
                disableAllButtons();

                //dialog end of continue?
                if(count < 2){

                    handle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadNextFlag(); // 次のフラグ
                        }
                    }, 1000);

                }else{
                    // finished!bye
                    ResultDialogAlert dialog = new ResultDialogAlert();
                    Bundle args = new Bundle();
                    args.putInt("countGuessInt",countGuess); //引数
                    args.putInt("count",count); //引数
                    dialog.setArguments(args);
                    dialog.show(getActivity().getFragmentManager(), "Alert");
                }

                count++;
            }else{
                countGuess++;
                Log.e("countGuess",String.valueOf(countGuess));
//                Animation hyperspaceJump = AnimationUtils.loadAnimation(getActivity(), R.anim.incorrect_shake);
                flagImageView.startAnimation(shakeAnimation);
                // display "Incorrect!" in red
                answerTextView.setText(R.string.incorrect_answer);
                answerTextView.setTextColor(getResources().getColor(R.color.incorrect_answer));
                //set the text in red Color
                guessButton.setEnabled(false); // disable incorrect answer
            }

        }
    };

    // utility method that disables all answer Buttons
    public void disableAllButtons(){
        for(int row = 0; row < guessRows; row++){
            LinearLayout guessRow = guessLinearLayouts[row];
            for(int i = 0; i < guessRow.getChildCount(); i++){
                guessRow.getChildAt(i).setEnabled(false);
            }
        }
    }

    // parses the country flag file name and returns the country name
    private String getCountryName(String name) {
        return name.substring(name.indexOf('-') + 1).replace('_', ' ');
    }

}
