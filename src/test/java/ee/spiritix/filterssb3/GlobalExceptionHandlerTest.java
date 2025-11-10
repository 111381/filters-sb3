package ee.spiritix.filterssb3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldHandleValidationException_WhenInvalidJsonProvided() throws Exception {
    String invalidJson = "{\"name\": \"\", \"selectionType\": null}";

    mockMvc.perform(post("/api/filters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldHandleValidationException_WhenMalformedJsonProvided() throws Exception {
    String malformedJson = "{}";

    mockMvc.perform(post("/api/filters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(malformedJson))
        .andExpect(status().isBadRequest());
  }
}