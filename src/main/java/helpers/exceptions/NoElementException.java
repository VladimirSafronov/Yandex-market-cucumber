package helpers.exceptions;

/**
 * Исключение возникающее при отсутствии элемента на странице
 */
public class NoElementException extends RuntimeException {

  public NoElementException(String message) {
    super(message);
  }
}
