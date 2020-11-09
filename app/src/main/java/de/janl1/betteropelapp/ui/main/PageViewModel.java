package de.janl1.betteropelapp.ui.main;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import de.janl1.betteropelapp.retrofit.objects.Vehicle;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Vehicle> mVehicleIndex = new MutableLiveData<>();
    private LiveData<Vehicle> mVehicle = Transformations.map(mVehicleIndex, new Function<Vehicle, Vehicle>() {
        @Override
        public Vehicle apply(Vehicle input) {
            return input;
        }
    });

    public void setVehicleIndex(Vehicle index) {
        mVehicleIndex.setValue(index);
    }

    public LiveData<Vehicle> getmVehicle() {
        return mVehicle;
    }


}