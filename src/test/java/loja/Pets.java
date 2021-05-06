package loja;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.*;

public class Pets {
    public String tokenGeral; // variável para receber o token

    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    @Test
    public void ordemDaExecucao() throws IOException { // Coloco em ordem, faço isso no final quando td der certo
        incluirPet();
        consultarPet();
        alterarPet();
        excluirPet();
    }


    // Create / Incluir / POST
    @Test
    public void incluirPet() throws IOException {
        // Ler o conteúdo do arquivo pet.json
        String jsonBody = lerJson("data/pet.json");

        given()
                .contentType("application/json")    // Tipo de conteúdo da requisição
                // "text/xml" para web services comuns
                // "application/json" para APIs REST
                .log().all()                        // Gerar um log completo da requisição
                .body(jsonBody)                     // Conteúdo do corpo da requisição
                .when()
                .post("https://petstore.swagger.io/v2/pet") // Operação e endpoint
                .then()
                .log().all()                        // Gerar um log completo da resposta
                .statusCode(200)                    // Validou o código de status da requisição como 200
                // .body("code", is(200))  // Valida o code como 200
                .body("id", is(4204))    // Validou a tag id com o conteúdo esperado
                .body("name", is("Snoopy")) // Validou a tag nome como Garfield
                .body("tags.name", contains("adoption")) // Validou a tag Name filha da tag Tags
        ;
        System.out.print("Executou o serviço");
    }

    // Reach or Research / Consultar / GET
    //@Test
    public void consultarPet() {
        String petId = "4204";

        given()
                .contentType("application/json")                        // Tipo de conteúdo da requisição
                .log().all()                                            // Mostrar tudo que foi enviado
                .when()
                .get("https://petstore.swagger.io/v2/pet/" + petId) // Consulta pelo petId
                .then()
                .log().all()                                            // Mostrar tudo que foi recebido
                .statusCode(200)                                        // Validou que a operação foi realizada
                .body("name", is("Snoopy"))                  // Validou o nome do pet
                .body("category.name", is("dog"))            // Validou a espécie
        ;
    }

    // Update/ Alterar / PUT
    @Test
    public void alterarPet() throws IOException {
        // Ler o conteúdo do arquivo pet.json
        String jsonBody = lerJson("data/petput.json");

        given()
                .contentType("application/json")                        // Tipo de conteúdo da requisição
                // "text/xml" para web services comuns
                // "application/json" para APIs REST
                .log().all()                                            // Gerar um log completo da requisição
                .body(jsonBody)                                         // Conteúdo do corpo da requisição
                .when()
                .put("https://petstore.swagger.io/v2/pet")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Snoopy"))
                .body("status", is("adopted"))
        ;

    }

    // Delete/ Excluir/ DELETE
    @Test
    public void excluirPet() {
        String petId = "4204";

        given()
                .contentType("application/json")                        // Tipo de conteúdo da requisição
                .log().all()                                            // Mostrar tudo que foi enviado
                .when()
                .delete("https://petstore.swagger.io/v2/pet/" + petId) // Consulta pelo petId
                .then()
                .log().all()
                .statusCode(200)
        ;
    }

    // Login GET
    @Test
    public void incluirUser() {
        String token =
                given()                                                         // Dado que
                        .contentType("application/json")                        // Tipo de conteúdo da requisição
                        .log().all()                                            // Mostrar tudo que foi enviado
                        .when()
                        .get("https://petstore.swagger.io/v2/user/login?username=charlie&password=brown%22")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .body("message", containsString("logged in user session:"))
                        .extract()
                        .path("message");
        tokenGeral = token.substring(24);
        System.out.println("O token valido eh" + tokenGeral);

    }


}
