package ohte.ui;

import java.util.ArrayList;
import java.util.stream.Collectors;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ModifiableObservableListBase;

/**
 * Utility class for adapting an {@link ObservableSet} into an {@link ObservableList}.
 *
 * Changes propagate in both directions, from the adapter to the
 * source set and the other way around.
 */
public class ObservableSetToListAdapter<E> extends ModifiableObservableListBase<E> implements SetChangeListener<E> {
    /**
     * The {@link ObservableSet} we are wrapping.
     */
    private ObservableSet<E> set;

    /**
     * Internal list for maintaining a consistent order of elements.
     */
    private ArrayList<E> list;

    /**
     * Wrap an {@link ObservableSet}.
     *
     * @param set The {@link ObservableSet} to wrap and adapt into an {@link ObservableList}.
     */
    public ObservableSetToListAdapter(ObservableSet<E> set) {
        this.set = set;
        this.list = new ArrayList<>(set);
        set.addListener(this);
    }

    /**
     * Callback, which is triggered every time a change happens in the source set.
     */
    @Override
    public void onChanged(Change<? extends E> change) {
        if (change.wasAdded() && !list.contains(change.getElementAdded())) {
            beginChange();
            list.add(change.getElementAdded());
            nextAdd(list.size() - 1, list.size());
            endChange();
        } else if (change.wasRemoved()) {
            for (int i = 0; i < list.size(); i++) {
                E element = list.get(i);

                if (element.equals(change.getElementRemoved())) {
                    beginChange();
                    list.remove(i);
                    nextRemove(i, element);
                    endChange();
                    break;
                }
            }
        }
    }

    /**
     * Required method by the abstract class {@link ModifiableObservableListBase}.
     * Returns the value at a specified offset.
     */
    @Override
    public E get(int index) {
        return list.get(index);
    }

    /**
     * Required method by the abstract class {@link ModifiableObservableListBase}.
     * Returns the number of items contained in the list.
     */
    @Override
    public int size() {
        return list.size();
    }

    /**
     * Required method by the abstract class {@link ModifiableObservableListBase}.
     * Performs an add operation on the list.
     */
    @Override
    public void doAdd(int index, E element) {
        list.add(index, element);
        set.add(element);
    }

    /**
     * Required method by the abstract class {@link ModifiableObservableListBase}.
     * Performs a remove operation on the list.
     */
    @Override
    public E doRemove(int index) {
        E element = list.remove(index);
        set.remove(element);
        return element;
    }

    /**
     * Required method by the abstract class {@link ModifiableObservableListBase}.
     * Performs an entry assignment operation on the list.
     */
    @Override
    public E doSet(int index, E element) {
        E oldValue = list.get(index);
        list.set(index, element);
        set.remove(oldValue);
        set.add(element);
        return oldValue;
    }
}
