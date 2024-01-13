package helpers.exceptions;

/**
 * Исключение возникающее при нажатии на элемент, который не был проинициализирован
 */
public class NoElementActivateException extends RuntimeException {

  public NoElementActivateException(String message) {
    super(message);
  }
}
