package com.bonoj.dashlite;

import com.bonoj.dashlite.data.model.Business;
import com.bonoj.dashlite.data.model.Restaurant;
import com.bonoj.dashlite.data.source.RestaurantsDataSource;
import com.bonoj.dashlite.restaurants.RestaurantsContract;
import com.bonoj.dashlite.restaurants.RestaurantsPresenter;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RestaurantsPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    RestaurantsDataSource restaurantsDataSource;

    @Mock
    RestaurantsContract.View view;

    private RestaurantsPresenter presenter;

    private Restaurant getMockRestaurant() {
        return new Restaurant(new Business(0, ""), "", "", 0, "", new ArrayList<String>());
    }

    @Before
    public void setUp() throws Exception {
        presenter = new RestaurantsPresenter(
                view, restaurantsDataSource, Schedulers.trampoline());
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
    }

    @Test
    public void shouldDeliverRestaurantsToView() {

        List<Restaurant> restaurants = Arrays.asList(
                getMockRestaurant(),
                getMockRestaurant(),
                getMockRestaurant());
        Mockito.when(restaurantsDataSource.getRestaurants()).thenReturn(Single.just(restaurants));

        presenter.loadRestaurants();

        Mockito.verify(view).displayRestaurants(restaurants);
    }

    @Test
    public void shouldHandleNoRestaurantsFound() {

        List<Restaurant> restaurants = Collections.emptyList();
        Mockito.when(restaurantsDataSource.getRestaurants()).thenReturn(Single.just(restaurants));

        presenter.loadRestaurants();

        Mockito.verify(view).displayNoRestaurants();
    }

    @Test
    public void shouldHandleError() {

        Mockito.when(restaurantsDataSource.getRestaurants()).thenReturn(Single.<List<Restaurant>>error(new Throwable("error")));

        presenter.loadRestaurants();

        Mockito.verify(view).displayError();
    }
}
