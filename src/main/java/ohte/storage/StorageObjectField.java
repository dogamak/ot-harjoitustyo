package ohte.storage;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Marks a property to be managed by {@link Storage} implementations and provides
 * programmatical access to the field via {@link StorageObjectFieldVisitor}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface StorageObjectField {
  /**
   * Non user-facing name of the field.
   *
   * Defaults to the property name.
   */
  String name() default "";

  /**
   * Adds an unique-constraint to the field.
   *
   * Helps the {@link Storage} implementation to optimize queries.
   */
  boolean unique() default false;

  /**
   * Whether the field should not be visible to the user.
   *
   * Useful for fields containing non human-readable data (e.g. hashes).
   */
  boolean hidden() default false;

  Class<?> convertTo() default String.class;
}
