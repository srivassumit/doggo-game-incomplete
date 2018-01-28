package com.willowtreeapps.doggogame;

import com.willowtreeapps.doggogame.core.ListRandomizer;
import com.willowtreeapps.doggogame.model.Doggo;
import com.willowtreeapps.doggogame.network.api.DoggoRepository;
import com.willowtreeapps.doggogame.ui.Question;
import com.willowtreeapps.doggogame.ui.presenter.QuestionPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuestionPresenterTest {
    private List<Doggo> testDoggos;
    private int correctIndex = 0;
    private int wrongIndex = 1;

    @Mock private ListRandomizer randomizer;
    @Mock private DoggoRepository repository;

    @Mock private Question.View view;

    @InjectMocks @Spy private QuestionPresenter presenter;

    @Before
    public void setup() {
        presenter.takeView(view);

        testDoggos = new ArrayList<>();
        setupTestDoggos();
        setupCorrectDoggo();
    }

    @Test
    public void test_check_answer_sets_correct_answer() {
        presenter.pickCorrectDoggo(correctIndex);

        presenter.checkAnswer(correctIndex);

        verify(view).setCorrectAnswer(correctIndex);
        verify(view, never()).setWrongAnswer(anyInt());
    }

    @Test
    public void test_check_answer_sets_wrong_answer() {
        presenter.pickCorrectDoggo(correctIndex);

        presenter.checkAnswer(wrongIndex);

        verify(view).setWrongAnswer(wrongIndex);
        verify(view, never()).setCorrectAnswer(anyInt());
    }

    @Test
    public void test_pick_correct_doggo_sets_correct_breed() {
        int testIndex = 4;
        presenter.pickCorrectDoggo(testIndex);

        verify(view).setTitle(testDoggos.get(testIndex).getBreedName());
    }

    private void setupTestDoggos() {
        testDoggos.add(new Doggo("Pupper", "http://pupper"));
        testDoggos.add(new Doggo("Doggo", "http://doggo"));
        testDoggos.add(new Doggo("Woofer", "http://woofer"));
        testDoggos.add(new Doggo("Aqua Doggo", "http://aquaDoggo"));
        testDoggos.add(new Doggo("Puggerino", "http://puggerino"));

        presenter.setDoggos(testDoggos);
    }

    private void setupCorrectDoggo() {
        when(randomizer.pickN(anyListOf(Doggo.class), anyInt())).thenReturn(testDoggos);

        presenter.pickDoggos();
        presenter.pickCorrectDoggo(correctIndex);
    }
}
