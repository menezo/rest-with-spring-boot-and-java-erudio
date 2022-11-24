package br.com.erudio.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.erudio.configs.TestConfigs;
import br.com.erudio.data.vo.security.TokenVO;
import br.com.erudio.integrationtests.controller.withyaml.mapper.YMLMapper;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.integrationtests.vo.AccountCredentialsVO;
import br.com.erudio.integrationtests.vo.BookVO;
import br.com.erudio.integrationtests.vo.pagedmodels.PagedModelBook;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerYamlTest extends AbstractIntegrationTest{

	private static RequestSpecification specification;
	private static YMLMapper objectMapper;
	
	private static BookVO book;

	@BeforeAll
	public static void setup() {
		objectMapper = new YMLMapper();
		book = new BookVO();
	}
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		
		AccountCredentialsVO user = new AccountCredentialsVO("menezes", "admin123");
		
		var accessToken = given()
				.config(
					RestAssuredConfig
						.config()
						.encoderConfig(EncoderConfig.encoderConfig()
							.encodeContentTypeAs(
								TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_YML)
					.accept(TestConfigs.CONTENT_TYPE_YML)
			.body(user, objectMapper)
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
							.body()
								.as(TokenVO.class, objectMapper)
							.getAccessToken();
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockBook();
		
		var persistedBook = given().spec(specification)
			.config(
				RestAssuredConfig
					.config()
					.encoderConfig(EncoderConfig.encoderConfig()
						.encodeContentTypeAs(
							TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
			.contentType(TestConfigs.CONTENT_TYPE_YML)
			.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(book, objectMapper)
				.when()
				.post()
			.then()
				.statusCode(200)
			.extract()
				.body()
					.as(BookVO.class, objectMapper);
		
		book = persistedBook;
		
		assertNotNull(persistedBook);
		
		assertNotNull(persistedBook.getId());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getTitle());
		
		assertTrue(persistedBook.getId() > 0);
		
		assertEquals("Joao", persistedBook.getAuthor());
		assertEquals(50.00, persistedBook.getPrice());
		assertEquals("Historia da minha vida", persistedBook.getTitle());
	}
	
	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		book.setAuthor("Piquet Souto Maior");
		
		var persistedBook = given().spec(specification)
				.config(
					RestAssuredConfig
						.config()
						.encoderConfig(EncoderConfig.encoderConfig()
							.encodeContentTypeAs(
								TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.body(book, objectMapper)
					.when()
					.put()
				.then()
					.statusCode(200)
						.extract()
							.body()
								.as(BookVO.class, objectMapper);
		
		book = persistedBook;
		
		assertNotNull(persistedBook);
		
		assertNotNull(persistedBook.getId());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getTitle());
		
		assertEquals(book.getId(), persistedBook.getId());
		
		assertEquals("Piquet Souto Maior", persistedBook.getAuthor());
		assertEquals(50.00, persistedBook.getPrice());
		assertEquals("Historia da minha vida", persistedBook.getTitle());
	}
	
	@Test
	@Order(3)
	public void testfindById() throws JsonMappingException, JsonProcessingException {
		mockBook();
		
		var persistedBook = given().spec(specification)
			.config(
				RestAssuredConfig
					.config()
					.encoderConfig(EncoderConfig.encoderConfig()
						.encodeContentTypeAs(
							TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
			.contentType(TestConfigs.CONTENT_TYPE_YML)
			.accept(TestConfigs.CONTENT_TYPE_YML)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.pathParam("id", book.getId())
				.when()
				.get("{id}")
			.then()
				.statusCode(200)
					.extract()
						.body()
							.as(BookVO.class, objectMapper);
		
		book = persistedBook;
		
		assertNotNull(persistedBook);
		
		assertNotNull(persistedBook.getId());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getTitle());
		
		assertEquals(book.getId(), persistedBook.getId());
		
		assertEquals("Piquet Souto Maior", persistedBook.getAuthor());
		assertEquals(50.00, persistedBook.getPrice());
		assertEquals("Historia da minha vida", persistedBook.getTitle());
	}
	
	@Test
	@Order(4)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		
		given().spec(specification)
			.config(
				RestAssuredConfig
					.config()
					.encoderConfig(EncoderConfig.encoderConfig()
						.encodeContentTypeAs(
							TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
			.contentType(TestConfigs.CONTENT_TYPE_YML)
			.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", book.getId())
				.when()
				.delete("{id}")
			.then()
				.statusCode(204);
	}
	
	@Test
	@Order(5)
	public void testFindBookByTitle() throws JsonMappingException, JsonProcessingException {
		
		var wrapper = given().spec(specification)
			.config(
				RestAssuredConfig
					.config()
					.encoderConfig(EncoderConfig.encoderConfig()
						.encodeContentTypeAs(
							TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
			.contentType(TestConfigs.CONTENT_TYPE_YML)
			.accept(TestConfigs.CONTENT_TYPE_YML)
			.pathParam("title", "java")
			.queryParams("page", 0, "size", 10, "direction", "asc")
				.when()
				.get("findBookByTitle/{title}")
			.then()
				.statusCode(200)
			.extract()
				.body()
					.as(PagedModelBook.class, objectMapper);
		
		List<BookVO> books = wrapper.getContent();
		
		BookVO foundBookOne = books.get(0);
		
		assertNotNull(foundBookOne.getId());
		assertNotNull(foundBookOne.getAuthor());
		assertNotNull(foundBookOne.getLaunchDate());
		assertNotNull(foundBookOne.getPrice());
		assertNotNull(foundBookOne.getTitle());
		
		assertEquals(9, foundBookOne.getId());
		
		assertEquals("Brian Goetz e Tim Peierls", foundBookOne.getAuthor());
		assertEquals(80.0, foundBookOne.getPrice());
		assertEquals("Java Concurrency in Practice", foundBookOne.getTitle());
		
		BookVO foundBookTwo = books.get(1);
		
		assertNotNull(foundBookTwo.getId());
		assertNotNull(foundBookTwo.getAuthor());
		assertNotNull(foundBookTwo.getPrice());
		assertNotNull(foundBookTwo.getTitle());
		
		assertEquals(4, foundBookTwo.getId());
		
		assertEquals("Crockford", foundBookTwo.getAuthor());
		assertEquals(67.0, foundBookTwo.getPrice());
		assertEquals("JavaScript", foundBookTwo.getTitle());
	}
	
	@Test
	@Order(6)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
		
		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
			.setBasePath("/api/book/v1")
			.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
		
		given().spec(specificationWithoutToken)
			.contentType(TestConfigs.CONTENT_TYPE_YML)
			.accept(TestConfigs.CONTENT_TYPE_YML)
				.when()
				.get()
			.then()
				.statusCode(403);
	}
	
	@Test
	@Order(7)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {
		
		var wrapper = given().spec(specification)
			.config(
				RestAssuredConfig
					.config()
					.encoderConfig(EncoderConfig.encoderConfig()
						.encodeContentTypeAs(
							TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
			.contentType(TestConfigs.CONTENT_TYPE_YML)
			.accept(TestConfigs.CONTENT_TYPE_YML)
			.queryParams("page", 0, "size", 10, "direction", "asc")
				.when()
				.get()
			.then()
				.statusCode(200)
			.extract()
				.body()
					.as(PagedModelBook.class, objectMapper);
		
		List<BookVO> books = wrapper.getContent();
		
		BookVO foundBookOne = books.get(0);
		
		assertNotNull(foundBookOne.getId());
		assertNotNull(foundBookOne.getAuthor());
		assertNotNull(foundBookOne.getLaunchDate());
		assertNotNull(foundBookOne.getPrice());
		assertNotNull(foundBookOne.getTitle());
		
		assertEquals(12, foundBookOne.getId());
		
		assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", foundBookOne.getAuthor());
		assertEquals(54.0, foundBookOne.getPrice());
		assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", foundBookOne.getTitle());
		
		BookVO foundBookSix = books.get(5);
		
		assertNotNull(foundBookSix.getId());
		assertNotNull(foundBookSix.getAuthor());
		assertNotNull(foundBookSix.getPrice());
		assertNotNull(foundBookSix.getTitle());
		
		assertEquals(11, foundBookSix.getId());
		
		assertEquals("Roger S. Pressman", foundBookSix.getAuthor());
		assertEquals(56.0, foundBookSix.getPrice());
		assertEquals("Engenharia de Software: uma abordagem profissional", foundBookSix.getTitle());
	}
	
	@Test
	@Order(8)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.config(
					RestAssuredConfig
						.config()
						.encoderConfig(EncoderConfig.encoderConfig()
							.encodeContentTypeAs(
								TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.queryParams("page", 0, "size", 10, "direction", "asc")
					.when()
					.get()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
		
		assertTrue(content.contains("  links:\n  - rel: \"self\"\n    href: \"http://localhost:8888/api/book/v1/12\"\n  links: []"));
		assertTrue(content.contains("  links:\n  - rel: \"self\"\n    href: \"http://localhost:8888/api/book/v1/3\"\n  links: []"));
		assertTrue(content.contains("  links:\n  - rel: \"self\"\n    href: \"http://localhost:8888/api/book/v1/5\"\n  links: []"));
		assertTrue(content.contains("  links:\n  - rel: \"self\"\n    href: \"http://localhost:8888/api/book/v1/2\"\n  links: []"));

		assertTrue(content.contains("page:\n  size: 10\n  totalElements: 15\n  totalPages: 2\n  number: 0"));

		assertTrue(content.contains("- rel: \"first\"\n  href: \"http://localhost:8888/api/book/v1?direction=asc&page=0&size=10&sort=title,asc\""));
		assertTrue(content.contains("- rel: \"self\"\n  href: \"http://localhost:8888/api/book/v1?page=0&size=10&direction=asc\""));
		assertTrue(content.contains("- rel: \"next\"\n  href: \"http://localhost:8888/api/book/v1?direction=asc&page=1&size=10&sort=title,asc\""));
		assertTrue(content.contains("- rel: \"last\"\n  href: \"http://localhost:8888/api/book/v1?direction=asc&page=1&size=10&sort=title,asc\""));
	}
	
	private void mockBook() {
		book.setAuthor("Joao");
		book.setLaunchDate(new Date());
		book.setPrice(50.00);
		book.setTitle("Historia da minha vida");;
	}
}
