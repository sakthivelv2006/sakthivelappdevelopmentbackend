package com.examly.springapp.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class claimdocumentdto {
@NotBlank(message = "Document name is required")
private String documentName;

@NotBlank(message = "File URL is required")
private String fileUrl;
}
