package be.fluid_it.µs.bundle.camel.guice;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.lang.annotation.Annotation;

@SuppressWarnings("all")
class CamelBindImpl implements CamelBind, Serializable {
  private static final long serialVersionUID = 0;

  private final String value;


  public CamelBindImpl(String value) {
    this.value = checkNotNull(value, "name");
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    return CamelBind.class;
  }

  @Override
  public String value() {
    return value;
  }

  public int hashCode() {
    return (127 * "value".hashCode()) ^ value.hashCode();
  }

  public boolean equals(Object o) {
    if (!(o instanceof CamelBind)) {
      return false;
    }
    CamelBind other = (CamelBind) o;
    return value.equals(other.value());
  }

  public String toString() {
    return "@" + CamelBind.class.getName() + "(value=" + value + ")";
  }
}