package ohte.storage;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Class for iterating through fields of storage objects.
 *
 * Designed to be extended.
 */
public class StorageObjectFieldVisitor {
  /**
   * Number of fields in the object we are currently visiting.
   *
   * Set at the beginning of a visit. Value is undefined in between visits.
   */
  private int fieldCount;

  /**
   * Get the number of fields in the visited object.
   */
  protected int getFieldCount() {
    return fieldCount;
  }

  /** Called for each field of the type String. */
  public void visitString(FieldValue<String> field) {}

  /** Called for each field of the type Boolean. */
  public void visitBoolean(FieldValue<Boolean> field) {}

  /** Called for each field of the type Integer. */
  public void visitInteger(FieldValue<Integer> field) {}

  /**
   * Called for each field in the object.
   *
   * Subclasses need to make sure to call the original implementation of this method
   * if they want to also use the {@link #visitString}, {@link #visitBoolean} or
   * {@link #visitInteger} methods.
   */
  public void visitField(FieldValue<Object> field) {
    if (field.type.equals(String.class)) {
      visitString(field.<String>cast());
    } else if (field.type.equals(Integer.class)) {
      visitInteger(field.<Integer>cast());
    } else if (field.type.equals(Boolean.class)) {
      visitBoolean(field.<Boolean>cast());
    } else {
      try {
        visitField(field.convert());
      } catch (ClassCastException cce) {
        throw new ClassCastException("unsupported type for field " + field.name);
      }
    }
  }

  /**
   * Field value and information.
   */
  public static class FieldValue<V> {
    /**
     * Name of the field.
     *
     * Either the actual name of the Java property or a name explicitly
     * defined with the {@link #ObjectStorageField} annotation.
     */
    public String name;

    /**
     * Whether or not the field should be exposed to the user.
     */
    public boolean hidden;

    /**
     * Value of the field.
     */
    public V value;

    /**
     * Object, part of which this field is.
     */
    private Object object;

    /**
     * The reflection of the field.
     */
    private Field field;

    private Class<?> type;

    /**
     * Cast the contained value to the given type.
     *
     * @throws ClassCastException
     */
    <T> FieldValue<T> cast() {
      FieldValue<T> res = new FieldValue<>(); 

      res.name = name;
      res.hidden = hidden;
      res.value = (T) value;
      res.field = field;
      res.object = object;
      res.type = type;

      return res;
    }

    /**
     * Sets the value of this field.
     */
    public void set(V value) {
      try {
        field.set(object, value);
      } catch (IllegalAccessException iae) {
        iae.printStackTrace();
      } catch (IllegalArgumentException iae2) {
        try {
          Method conversionMethod = field.getType()
            .getDeclaredMethod("convertFrom", value.getClass());

          if (value != null) {
            field.set(object, conversionMethod.invoke(null, value));
          }
        } catch(NoSuchMethodException nsme) {
          throw new ClassCastException("cannot convert value for field " + name);
        } catch(IllegalAccessException iae) {
          throw new ClassCastException("cannot convert value for field " + name);
        } catch (InvocationTargetException ite) {
          throw new ClassCastException("cannot convert value for field " + name);
        }
      }
    }

    public <T> FieldValue<T> convert() throws ClassCastException {
      FieldValue<T> res = new FieldValue<>();

      try {
        Method conversionMethod = type.getDeclaredMethod("convertTo");
        res.type = conversionMethod.getReturnType();

        if (value != null) {
          res.value = (T) conversionMethod.invoke(value);
        }
      } catch(NoSuchMethodException nsme) {
        throw new ClassCastException("cannot convert value for field " + name);
      } catch(IllegalAccessException iae) {
        throw new ClassCastException("cannot convert value for field " + name);
      } catch (InvocationTargetException ite) {
        throw new ClassCastException("cannot convert value for field " + name);
      }

      res.name = name;
      res.hidden = hidden;
      res.field = field;
      res.object = object;

      return res;
    }
  }

  /**
   * Visits the object and iterates through each of the
   * {@link StorageObjectField StorageObjectFields} defined in the class.
   *
   * @param object
   *  Any value. Providing a value with no fields defined with the
   *  {@link StorageObjectField} annotation is simply no-op.
   */
  public void visit(Object object) {
    Field[] fields = object.getClass().getDeclaredFields();

    for (Field field : fields) {
      if (!field.isAnnotationPresent(StorageObjectField.class))
        continue;

      StorageObjectField annotation =
        (StorageObjectField) field.getAnnotation(StorageObjectField.class);

      Class<?> fieldType = field.getType();

      try {
        String name = field.getName();
        Object value = field.get(object);

        if (!annotation.name().equals("")) {
          name = annotation.name();
        }

        FieldValue<Object> fieldValue = new FieldValue<>();
        fieldValue.name = name;
        fieldValue.value = value;
        fieldValue.hidden = annotation.hidden();
        fieldValue.object = object;
        fieldValue.field = field;
        fieldValue.type = field.getType();

        visitField(fieldValue);
      } catch (IllegalAccessException iae) {
        continue;
      } catch (ClassCastException cce) {
        System.out.println(cce);
        continue;
      }
    }
  }
}

