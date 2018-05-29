package me.nithin.james.freqchart;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;

public class BundleSavedState extends android.support.v4.view.AbsSavedState {
    public static final Creator<BundleSavedState> CREATOR =
            ParcelableCompat.newCreator(
                    new ParcelableCompatCreatorCallbacks<BundleSavedState>() {
                        @Override
                        public BundleSavedState createFromParcel(Parcel source,
                                                                 ClassLoader loader) {
                            return new BundleSavedState(source, loader);
                        }

                        @Override
                        public BundleSavedState[] newArray(int size) {
                            return new BundleSavedState[size];
                        }
                    });

    public final Bundle bundle;

    BundleSavedState(Parcelable superState, Bundle bundle) {
        super(superState);
        this.bundle = bundle;
    }

    private BundleSavedState(Parcel source, ClassLoader loader) {
        super(source, loader);
        this.bundle = source.readBundle(loader);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeBundle(bundle);
    }
}