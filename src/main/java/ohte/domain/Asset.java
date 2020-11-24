package ohte.domain;

import java.util.List;
import javafx.beans.property.Property;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

public class Asset {
    /**
     * IP address of the machine.
     */
    ListProperty<String> ipAddresses = new SimpleListProperty<>(FXCollections.emptyObservableList());

    ObjectProperty<String> hostname = new SimpleObjectProperty<>("");
    ObjectProperty<String> manufacturer = new SimpleObjectProperty<>("");
    ObjectProperty<String> model = new SimpleObjectProperty<>("");
    ObjectProperty<String> serialNumber = new SimpleObjectProperty<>("");

    public List<String> getIpAddresses() {
        return ipAddresses.get();
    }

    public ListProperty<String> getIpAddressesProperty() {
        return ipAddresses;
    }

    public String getHostname() {
        return hostname.get();
    }

    public void setHostname(String hostname) {
        this.hostname.set(hostname);
    }

    public Property<String> getHostnameProperty() {
        return hostname;
    }

    public String getManufacturer() {
        return manufacturer.get();
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer.set(manufacturer);
    }

    public Property<String> getManufacturerProperty() {
        return manufacturer;
    }

    public String getModel() {
        return model.get();
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public Property<String> getModelProperty() {
        return model;
    }

    public String getSerialNumber() {
        return serialNumber.get();
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber.set(serialNumber);
    }

    public Property<String> getSerialNumberProperty() {
        return serialNumber;
    }
}
