package com.ralfhenze.railplan.userinterface.web.drafts.stations;

import java.util.List;

public class StationTableRow {

    public boolean showInputField = false;
    public boolean disabled = false;
    public String stationId = "";
    public String stationName = "";
    public List<String> stationNameErrors = List.of();
    public List<String> locationErrors = List.of();
    public String latitude = "0.0";
    public List<String> latitudeErrors = List.of();
    public String longitude = "0.0";
    public List<String> longitudeErrors = List.of();

    public boolean hasErrors() {
        return !(stationNameErrors.isEmpty()
            && locationErrors.isEmpty()
            && latitudeErrors.isEmpty()
            && longitudeErrors.isEmpty()
        );
    }

    public boolean stationNameIsInvalid() {
        return !stationNameErrors.isEmpty();
    }

    public boolean latitudeIsInvalid() {
        return !latitudeErrors.isEmpty() || !locationErrors.isEmpty();
    }

    public boolean longitudeIsInvalid() {
        return !longitudeErrors.isEmpty() || !locationErrors.isEmpty();
    }

    public boolean isShowInputField() {
        return showInputField;
    }

    public void setShowInputField(boolean showInputField) {
        this.showInputField = showInputField;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public List<String> getStationNameErrors() {
        return stationNameErrors;
    }

    public void setStationNameErrors(List<String> stationNameErrors) {
        this.stationNameErrors = stationNameErrors;
    }

    public List<String> getLocationErrors() {
        return locationErrors;
    }

    public void setLocationErrors(List<String> locationErrors) {
        this.locationErrors = locationErrors;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public List<String> getLatitudeErrors() {
        return latitudeErrors;
    }

    public void setLatitudeErrors(List<String> latitudeErrors) {
        this.latitudeErrors = latitudeErrors;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public List<String> getLongitudeErrors() {
        return longitudeErrors;
    }

    public void setLongitudeErrors(List<String> longitudeErrors) {
        this.longitudeErrors = longitudeErrors;
    }
}
