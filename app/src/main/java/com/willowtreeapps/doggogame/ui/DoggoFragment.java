package com.willowtreeapps.doggogame.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.willowtreeapps.doggogame.core.DoggoGameApplication;
import com.willowtreeapps.doggogame.glide.GlideApp;
import com.willowtreeapps.doggogame.ui.presenter.QuestionPresenter;
import com.willowtreeapps.doggogame.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DoggoFragment extends Fragment implements Question.View {

    private static final Interpolator OVERSHOOT = new OvershootInterpolator();
    private static final Interpolator FAST_OUT_LINEAR_IN = new FastOutLinearInInterpolator();
    private static final TypeEvaluator ARGB_EVALUATOR = new ArgbEvaluator();
    private static final String KEY_DOGGO = "doggo";

    private Listener listener;

    @Inject
    QuestionPresenter presenter;

    private TextView title;
    private ViewGroup container;
    private List<ImageView> faces = new ArrayList<>(5);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DoggoGameApplication.get(getActivity()).component().inject(this);

        presenter.takeView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.doggo_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        title = view.findViewById(R.id.title);
        container = view.findViewById(R.id.face_container);

        for (int i = 0; i < container.getChildCount(); i++) {
            final int index = i;
            ImageView face = (ImageView) container.getChildAt(index);
            faces.add(face);
            face.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.checkAnswer(index);
                }
            });
        }

        presenter.pickDoggos();
        presenter.pickCorrectDoggo(savedInstanceState == null ? -1 : savedInstanceState.getInt(KEY_DOGGO));

        if (savedInstanceState == null) {
            title.setAlpha(0);
            title.animate().alpha(1).start();
            for (int i = 0; i < container.getChildCount(); i++) {
                ImageView face = faces.get(i);
                face.setScaleX(0);
                face.setScaleY(0);
                face.animate().scaleX(1).scaleY(1).setStartDelay(800 + 120 * i).setInterpolator(OVERSHOOT).start();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_DOGGO, presenter.getCorrectIndex());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (Listener) context;
    }

    @Override
    public void onDetach() {
        this.listener = null;
        super.onDetach();
    }

    @Override
    public void setTitle(String breed) {
        title.setText(getString(R.string.question, breed));
    }

    @Override
    public void setWrongAnswer(int index) {
        removeClickListener(index);

        ObjectAnimator anim = ObjectAnimator.ofInt((ImageView) container.getChildAt(index),
                "colorFilter",
                ContextCompat.getColor(getContext(), R.color.alphaRed));
        anim.setEvaluator(ARGB_EVALUATOR);
        anim.start();
    }

    @Override
    public void setCorrectAnswer(int index) {
        removeAllClickListeners();

        ObjectAnimator anim = ObjectAnimator.ofInt((ImageView) container.getChildAt(index),
                "colorFilter",
                ContextCompat.getColor(getContext(), R.color.alphaGreen));
        anim.setEvaluator(ARGB_EVALUATOR);
        anim.start();

        animateNextRound(index);
    }

    @Override
    public void setImage(int index, @NonNull String url) {
        GlideApp.with(this)
                .load(url)
                .placeholder(R.drawable.ic_face_white_48dp)
                .circleCrop()
                .into(faces.get(index));
    }

    @Override
    public void showFirstTryToast(String breed) {
        Toast.makeText(getContext(), getString(R.string.first_try, breed), Toast.LENGTH_SHORT).show();
    }

    private void animateNextRound(int correctIndex) {
        title.animate().alpha(0).setStartDelay(800).start();
        int n = container.getChildCount();
        for (int i = 0; i < n; i++) {
            if (i != correctIndex) {
                faces.get(i).animate().scaleX(0).scaleY(0).setStartDelay(800 + 120 * i).setInterpolator(FAST_OUT_LINEAR_IN).start();
            } else {
                faces.get(i).animate().scaleX(0).scaleY(0).setStartDelay(1800 + 200 * i).setInterpolator(FAST_OUT_LINEAR_IN).setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (listener != null) {
                            listener.onRoundComplete();
                        }
                    }
                }).start();
            }
        }
    }

    private void removeClickListener(int index) {
        faces.get(index).setOnClickListener(null);
    }

    private void removeAllClickListeners() {
        for (ImageView face : faces) {
            face.setOnClickListener(null);
        }
    }

    public interface Listener {
        void onRoundComplete();
    }
}
