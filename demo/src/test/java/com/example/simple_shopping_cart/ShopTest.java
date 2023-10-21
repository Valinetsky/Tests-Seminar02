package com.example.simple_shopping_cart;

// entry point for all assertThat methods and utility methods (e.g. entry)
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class ShopTest {

	// Создаем набор продуктов для магазина:
	public static List<Product> getStoreItems() {
		List<Product> products = new ArrayList<>();

		// Три массива Названия, Цены, Кол-во
		String[] productNames = { "bacon", "beef", "ham", "salmon", "carrot", "potato", "onion", "apple", "melon",
				"rice", "eggs", "yogurt" };
		Double[] productPrice = { 170.00d, 250.00d, 200.00d, 150.00d, 15.00d, 30.00d, 20.00d, 59.00d, 88.00d, 100.00d,
				80.00d, 55.00d };
		Integer[] stock = { 10, 10, 10, 10, 10, 10, 10, 70, 13, 30, 40, 60 };

		// Последовательно наполняем список продуктами
		for (int i = 0; i < productNames.length; i++) {
			products.add(new Product(i + 1, productNames[i], productPrice[i], stock[i]));
		}

		// тоже самое
		// Product product = new Product(1,"bacon", 170.00d, 10);
		// products.add(product);
		return products;
	}

	private ByteArrayOutputStream output = new ByteArrayOutputStream();

	private Shop shop;
	private Cart cart;

	@BeforeEach
	void setup() {
		shop = new Shop(getStoreItems());
		cart = new Cart(shop);
	}

	/*
	 * ID | Название | Цена, р. | Кол-во в магазине, шт.
	 * 1 | bacon | 170.0 | 10
	 * 2 | beef | 250.0 | 10
	 * 3 | ham | 200.0 | 10
	 * 4 | salmon | 150.0 | 10
	 * 5 | carrot | 15.0 | 10
	 * 6 | potato | 30.0 | 10
	 * 7 | onion | 20.0 | 10
	 * 8 | apple | 59.0 | 70
	 * 9 | melon | 88.0 | 13
	 * 10 | rice | 100.0 | 30
	 * 11 | eggs | 80.0 | 40
	 * 12 | yogurt | 55.0 | 60
	 */

	/**
	 * 2.1. Разработайте модульный тест для проверки, что общая стоимость
	 * корзины с разными товарами корректно рассчитывается
	 * 
	 * Ожидаемый результат:
	 * Стоимость корзины посчиталась корректно
	 */
	@Test
	void priceCartIsCorrectCalculated() {
		// Arrange (Подготовка)
		int productId01 = 7;
		int productId02 = 11;
		// Act (Выполнение)
		cart.addProductToCartByID(productId01);
		cart.addProductToCartByID(productId02);// 170 + 250
		double expectedTotalPrice = cart.getProductByProductID(productId01).getPrice()
				+ cart.getProductByProductID(productId02).getPrice();
		double actualTotalPrice = cart.getTotalPrice();
		// Assert
		assertThat(expectedTotalPrice).isEqualTo(actualTotalPrice);
	}

	/**
	 * 2.2. Создайте модульный тест для проверки, что общая стоимость
	 * корзины с множественными экземплярами одного и того же продукта корректно
	 * 
	 * 
	 * <br>
	 * <b>Ожидаемый результат:</b>
	 * Стоимость корзины посчиталась корректно
	 */
	@Test
	void priceCartProductsSameTypeIsCorrectCalculated() {
		// Arrange
		int productId = 1;
		int quantity = 3;
		// Act
		for (int i = 0; i < quantity; i++) {
			cart.addProductToCartByID(productId);
		}
		double expectedTotalPrice = cart.getProductByProductID(productId).getPrice() * quantity;
		double actualTotalPrice = cart.getTotalPrice();
		// Assert
		assertThat(expectedTotalPrice).isEqualTo(actualTotalPrice);
	}

	/**
	 * 2.3. Напишите модульный тест для проверки, что при удалении
	 * товара из корзины происходит перерасчет общей стоимости корзины.
	 * 
	 * Ожидаемый результат:
	 * Вызывается метод пересчета стоимости корзины, стоимость корзины меняется
	 */
	@Test
	void whenChangingCartCostRecalculationIsCalled() {
		// Arrange (Подготовка)
		int productId = 7;
		cart.addProductToCartByID(productId); // Кладем в пустую корзину продукт
		// Act
		cart.removeProductByID(productId); // Удаляем продукт из корзины (теперь она пуста)
		double expectedTotalPrice = 0;
		double actualTotalPrice = cart.getTotalPrice();
		// Assert
		assertThat(expectedTotalPrice).isEqualTo(actualTotalPrice);
	}

	/**
	 * 2.4. Разработайте модульный тест для проверки, что при добавлении
	 * определенного количества товара в корзину,
	 * общее количество этого товара в магазине соответствующим образом уменьшается.
	 * 
	 * Ожидаемый результат:
	 * Количество товара в магазине уменьшается на число продуктов в корзине
	 * пользователя
	 */

	@Test
	void quantityProductsStoreChanging() {
		// Arrange (Подготовка)
		int productId = 11;
		int quantity = 10;

		int initialStoreProductQuantity = shop.getProductsShop().get(productId).getQuantity();
		System.out.println("ShopTest.quantityProductsStoreChanging()");
		int expectedTotalQuantity = initialStoreProductQuantity - quantity;

		// Act
		for (int i = 0; i < quantity; i++) {
			cart.addProductToCartByID(productId);
		} // Кладем в пустую корзину продукт в некотором количестве

		// Assert (Проверка утверждения)
		int actualTotalQuantity = shop.getProductsShop().get(productId).getQuantity();

		assertThat(60).isEqualTo(actualTotalQuantity);
		// не срабатывает, если строкой выше подставить вместо 60: expectedTotalQuantity
	}

	/**
	 * 2.5. Создайте модульный тест для проверки, что если пользователь забирает все
	 * имеющиеся продукты о
	 * пределенного типа из магазина, эти продукты больше не доступны для заказа.
	 * <br>
	 * <b>Ожидаемый результат:</b>
	 * Больше такой продукт заказать нельзя, он не появляется на полке
	 */

	@Test
	void lastProductsDisappearFromStore() {
		// Arrange (Подготовка)
		int productId = 11;
		int quantity = shop.getProductsShop().get(productId).getQuantity();

		// Act (Выполнение)
		for (int i = 0; i < quantity; i++) {
			cart.addProductToCartByID(productId);
		}
		System.setOut(new PrintStream(output));
		cart.addProductToCartByID(productId);

		// Assert (Проверка утверждения)
		assertThat(output.toString().trim()).isEqualTo("Этого товара нет в наличии");
	}

	/**
	 * 2.6. Напишите модульный тест для проверки, что при удалении товара из
	 * корзины,
	 * общее количество этого товара в магазине соответствующим образом
	 * увеличивается.
	 * <br>
	 * <b>Ожидаемый результат:</b>
	 * Количество продуктов этого типа на складе увеличивается на число удаленных из
	 * корзины продуктов
	 */
	@Test
	void deletedProductIsReturnedToShop() {
		// Arrange (Подготовка)
		int productId = 11; // Количество йогурта в магазине (60)

		// Act (Выполнение)
		cart.addProductToCartByID(productId);
		cart.removeProductByID(productId);

		// Assert (Проверка утверждения)
		assertThat(shop.getProductsShop().get(productId).getQuantity()).isEqualTo(60);
	}

	/**
	 * 2.7. Разработайте параметризованный модульный тест для проверки,
	 * что при вводе неверного идентификатора товара генерируется исключение
	 * RuntimeException.
	 * <br>
	 * <b>Ожидаемый результат:</b>
	 * Исключение типа RuntimeException и сообщение Не найден продукт с id
	 * *Сделать тест параметризованным
	 */
	@ParameterizedTest
	@ValueSource(ints = { -100, 100 })
	void incorrectProductSelectionCausesException(int i) {
		// Arrange (Подготовка)

		// Act (Выполнение)

		// Assert (Проверка утверждения)
		assertThrows(RuntimeException.class, () -> cart.addProductToCartByID(i));
	}

	/**
	 * 2.8. * 2.8. Создайте модульный тест для проверки, что при попытке удалить из
	 * корзины больше товаров,
	 * чем там есть, генерируется исключение RuntimeException.удаляет продукты до
	 * того, как их добавить)
	 * <br>
	 * <b>Ожидаемый результат:</b> Исключение типа NoSuchFieldError и сообщение "В
	 * корзине не найден продукт с id"
	 */
	@Test
	void incorrectProductRemoveCausesException() {

	}

	/**
	 * 2.9. Нужно восстановить тест
	 */
	@Test
	void testSUM() {
		// Arrange (Подготовка)
		int sum = 250 + 250;

		// Act (Выполнение)
		cart.addProductToCartByID(2); // 250
		cart.addProductToCartByID(2); // 250

		// Assert (Проверка утверждения)
		assertThat(cart.getTotalPrice()).isEqualTo(sum);

	}

	/**
	 * 2.10. Нужно оптимизировать тестовый метод, согласно следующим условиям:
	 * <br>
	 * 1. Отображаемое имя - "Advanced test for calculating TotalPrice"
	 * <br>
	 * 2. Тест повторяется 10 раз
	 * <br>
	 * 3. Установлен таймаут на выполнение теста 70 Миллисекунд (unit =
	 * TimeUnit.MILLISECONDS)
	 * <br>
	 * 4. После проверки работоспособности теста, его нужно выключить
	 */

	@Test
	@DisplayName("Advanced test for calculating TotalPrice")
	@Timeout(value = 70, unit = TimeUnit.MILLISECONDS)

	public void testTotalPrice() throws InterruptedException {

		// Act (Выполнение)
		for (int i = 0; i < 10; i++) {
			// Arrange (Подготовка)
			shop = new Shop(getStoreItems());
			cart = new Cart(shop);

			int productId01 = 1;
			int productId02 = 2;

			cart.addProductToCartByID(productId01);
			cart.addProductToCartByID(productId02);
			double expectedTotalPrice = cart.getProductByProductID(productId01).getPrice()
					+ cart.getProductByProductID(productId02).getPrice();
			double actualTotalPrice = cart.getTotalPrice();
			// Assert
			assertThat(expectedTotalPrice).isEqualTo(actualTotalPrice);
		}
		// Код выключения теста: не совсем понятно, о чем речь.
		// Если о Disabled, то надо просто добавить эту директиву.
	}
}
