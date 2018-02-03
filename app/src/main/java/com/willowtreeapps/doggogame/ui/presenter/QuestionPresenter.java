package com.willowtreeapps.doggogame.ui.presenter;

import com.willowtreeapps.doggogame.core.ListRandomizer;
import com.willowtreeapps.doggogame.model.Doggo;
import com.willowtreeapps.doggogame.network.api.DoggoRepository;
import com.willowtreeapps.doggogame.ui.Question;

import java.util.List;

import javax.inject.Inject;

public class QuestionPresenter implements Question.Presenter {
    private static int choiceNumber = 5;
    private int correctIndex;
    private int tries = 0;

    private Question.View view;

    private List<Doggo> doggos;
    private List<Doggo> pickedDoggos;

    private ListRandomizer randomizer;

    @Inject
    public QuestionPresenter(ListRandomizer randomizer, DoggoRepository repository) {
        this.randomizer = randomizer;
        setDoggos(repository.getDoggos());
    }

    @Override
    public void takeView(Question.View view) {
        this.view = view;
    }

    @Override
    public void checkAnswer(int index) {
        tries++;
        //#CHECK-ANSWER
        if (index == correctIndex) {
            view.setCorrectAnswer(index);
        } else {
            view.setWrongAnswer(index);
        }
    }

    @Override
    public int getCorrectIndex() {
        return correctIndex;
    }

    @Override
    public void setDoggos(List<Doggo> doggos) {
        this.doggos = doggos;
    }

    @Override
    public void pickDoggos() {
        //#PICK-DOGS
        pickedDoggos = randomizer.pickN(doggos, choiceNumber);
        loadImages();
    }

    @Override
    public void pickCorrectDoggo(int savedIndex) {
        //#PICK-CORRECT-DOG
        if (savedIndex >= 0) {
            correctIndex = savedIndex;
        } else {
            correctIndex = randomizer.pickOneIndex(pickedDoggos);
        }
        view.setTitle(pickedDoggos.get(correctIndex).getBreedName());
    }

    private void loadImages() {
        //#PICK-DOGS
        for (int i=0; i<pickedDoggos.size(); i++) {
            view.setImage(i, pickedDoggos.get(i).getImageUrl());
        }
    }
}
