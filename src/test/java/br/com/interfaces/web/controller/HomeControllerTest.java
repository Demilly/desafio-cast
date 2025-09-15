package br.com.interfaces.web.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeControllerTest {

    private final HomeController controller = new HomeController();

    @Test
    void indexShouldReturnIndexView() {
        String view = controller.index();
        assertEquals("index", view);
    }
}
