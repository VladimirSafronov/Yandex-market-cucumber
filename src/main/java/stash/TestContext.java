package stash;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс управляет хранилищем данных, которые передаются между шагами теста
 */
public class TestContext {

  /**
   * Коллекция с данными, формата ключ-объект
   */
  private final Map<String, Object> stash;
  private static TestContext INSTANCE;

  private TestContext() {
    stash = new HashMap<>();
  }

  /**
   * Реализация паттерна Singleton для обращения к единой коллекции с данными
   */
  public static TestContext getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new TestContext();
    }
    return INSTANCE;
  }

  /**
   * Метод кладет объект в хранилище
   */
  public void put(String key, Object value) {
    stash.put(key, value);
  }

  /**
   * Метод возвращает объект из хранилища
   */
  public Object get(String key) {
    return stash.get(key);
  }

  /**
   * Метод проверяет наличие объекта по ключу
   */
  public Boolean contains(String key) {
    return stash.containsKey(key);
  }
}
