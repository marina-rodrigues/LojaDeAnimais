package loja;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class Users {

    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    @Test // POST - Incluir
    public void incluirUsuario() throws IOException {
        String jsonBody = lerJson("data/user.json");

        given() // essa ordem não altera, posso colocar qualquer ordem com exceção do given, when e then
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
                .when()
                .post("https://petstore.swagger.io/v2/user")
                .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", is("5305"))

        ;
        System.out.print("Executou o serviço");

    }

    @Test // GET - Consultar
    public void consultarUsuario() {
        String username = "mrodrigues";

        given()
                .contentType("application/json")
                .log().all()
                .when()
                .get("https://petstore.swagger.io/v2/user/" + username)
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(5305))
                .body("firstName", is("marina"))
                .body("lastName", is("rodrigues"))
                .body("email", is("mari.keeggo@gmail.com"))

        ;

    }

    @Test // PUT
    public void alterarUsuario() throws IOException {
        String jsonBody = lerJson("data/userput.json");
        String username = "mrodrigues";

        given()
                .log().all()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .put("https://petstore.swagger.io/v2/user/" + username)
                .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", is("5305"))
        ;
    }

    @Test // DELETE
    public void excluirUsuario() {
        String username = "mrodrigues";

        given()
                .contentType("application/json")
                .log().all()
                .when()
                .delete("https://petstore.swagger.io/v2/user/" + username)
                .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("message", is(username))
        ;


    }
}
