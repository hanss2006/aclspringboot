package com.ocrv.skimrv.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocrv.skimrv.backend.domain.FormSkimIt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AclbackApplicationTests {
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MockMvc mockMvc;
	@Test
	@WithMockUser(username = "admin")
	public void user1ShouldGetDocument1() throws Exception {
		FormSkimIt d1=new FormSkimIt();
		d1.setId(1);
		mockMvc.perform(get("/api/form-skim-it"))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(d1))));
	}
	@Test
	@WithMockUser(username = "admin")
	public void user2ShouldGetDocument2() throws Exception {
		FormSkimIt d2=new FormSkimIt();
		d2.setId(2);
		mockMvc.perform(
						get("/api/form-skim-it"))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(d2))));
	}
	@Test
	@WithMockUser(username = "admin")
	public void adminShouldGetDocuments123() throws Exception {
		FormSkimIt d1=new FormSkimIt();
		d1.setId(1);
		FormSkimIt d2=new FormSkimIt();
		d2.setId(2);
		FormSkimIt d3=new FormSkimIt();
		d3.setId(3);
		mockMvc.perform(
						get("/api/form-skim-it"))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(d1,d2,d3))));
	}
	@Test
	@WithMockUser(username = "admin")
	public void user1ShouldGetDocument1WithPathVariable() throws Exception {
		FormSkimIt d1=new FormSkimIt();
		d1.setId(1);
		mockMvc.perform(
						get("/api/form-skim-it/1"))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(d1)));
	}
	@Test
	@WithMockUser(username = "admin")
	public void user1ShouldNotGetDocument2() throws Exception {
		mockMvc.perform(
						get("/api/form-skim-it/2"))
				.andExpect(status().isForbidden());
	}
	@Test
	@WithMockUser(username = "admin")
	public void adminShouldEditDocument() throws Exception {
		FormSkimIt d1=new FormSkimIt();
		d1.setId(1);
		mockMvc.perform(
						put("/api/form-skim-it/1")
								.content(objectMapper.writeValueAsString(d1))
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(d1)));
	}
	@Test
	@WithMockUser(username = "user1")
	public void user1ShouldNotEditDocument() throws Exception {
		FormSkimIt d1=new FormSkimIt();
		d1.setId(1);
		mockMvc.perform(
						put("/api/form-skim-it/1")
								.content(objectMapper.writeValueAsString(d1))
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}
	@Test
	@WithMockUser(authorities = { "ROLE_ADMIN" })
	public void adminShouldSetPermissionAndPostDocument() throws Exception {
		FormSkimIt d4=new FormSkimIt();
		d4.setId(4);
		mockMvc.perform(
						post("/api/form-skim-it")
								.content(objectMapper.writeValueAsString(d4))
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(d4)));
	}
}
