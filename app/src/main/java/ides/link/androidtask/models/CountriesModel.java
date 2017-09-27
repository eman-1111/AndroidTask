package ides.link.androidtask.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Eman on 9/27/2017.
 */

public class CountriesModel implements Parcelable {

    @Expose
    private String name;

    @Expose
    private String alpha2Code;

    @Expose
    private String alpha3Code;

    @Expose
    private String capital;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getAlpha2Code() {
        return alpha2Code;
    }

    public void setAlpha2Code(String alpha2Code) {
        this.alpha2Code = alpha2Code;
    }

    public String getAlpha3Code() {
        return alpha3Code;
    }

    public void setAlpha3Code(String alpha3Code) {
        this.alpha3Code = alpha3Code;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(alpha3Code);
        out.writeString(name);
        out.writeString(alpha2Code);
        out.writeString(capital);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<CountriesModel> CREATOR
            = new Parcelable.Creator<CountriesModel>() {


        @Override
        public CountriesModel createFromParcel(Parcel in) {
            return new CountriesModel(in);
        }

        @Override
        public CountriesModel[] newArray(int size) {
            return new CountriesModel[size];
        }
    };

    private CountriesModel(Parcel in) {
        alpha2Code = in.readString();
        alpha3Code = in.readString();
        name = in.readString();
        capital = in.readString();
    }
}
