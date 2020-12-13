package ohte.domain;

import java.util.List;
import java.util.ArrayList;
import javafx.beans.property.Property;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

/**
 * Class for storing the data of an asset. 
 *
 * The setters and getters are self explanatory and follow the JavaFX bean naming convention.
 * Getters ending with {@code Property} return a modifiable and observable {@link Property}
 * reference to the value.
 */
public class Asset {
    /** IP address of the machine. */
    ListProperty<String> ipAddresses = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));

    /** Hostname of the machine. Either local or a proper domain name. */
    ObjectProperty<String> hostname = new SimpleObjectProperty<>("");

    /** Manufacturer of the machine. */
    ObjectProperty<String> manufacturer = new SimpleObjectProperty<>("");

    /** Model of the machine. */
    ObjectProperty<String> model = new SimpleObjectProperty<>("");

    /** Serial number of the machine */
    ObjectProperty<String> serialNumber = new SimpleObjectProperty<>("");

    /** Internal identifier of the asset. */
    int id;

    public void setId(int id) {
      this.id = id;
    }

    public int getId() {
      return this.id;
    }

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

    public boolean equals(Asset other) {
      return getId() == other.getId();
    }
}
