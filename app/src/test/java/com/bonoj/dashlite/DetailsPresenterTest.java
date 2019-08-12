package com.bonoj.dashlite;

import com.bonoj.dashlite.data.model.Restaurant;
import com.bonoj.dashlite.data.model.RestaurantDetails;
import com.bonoj.dashlite.data.source.RestaurantsDataSource;
import com.bonoj.dashlite.details.DetailsContract;
import com.bonoj.dashlite.details.DetailsPresenter;
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

public class DetailsPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    RestaurantsDataSource restaurantsDataSource;

    @Mock
    DetailsContract.View view;

    private DetailsPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new DetailsPresenter(
                view, restaurantsDataSource, Schedulers.trampoline());
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
    }

    @Test
    public void shouldDeliverDetailsToView() {

        RestaurantDetails restaurantDetails = new RestaurantDetails(0.0, "","");

        Mockito.when(view.getId()).thenReturn(0L);
        Mockito.when(restaurantsDataSource.getRestaurantDetails(0)).thenReturn(Single.just(restaurantDetails));

        presenter.loadDetails();

        Mockito.verify(view).displayDetails(restaurantDetails);
    }

    @Test
    public void shouldHandleError() {

        Mockito.when(view.getId()).thenReturn(0L);
        Mockito.when(restaurantsDataSource.getRestaurantDetails(0)).thenReturn(Single.<RestaurantDetails>error(new Throwable("error")));

        presenter.loadDetails();

        Mockito.verify(view).displayError();
    }
}