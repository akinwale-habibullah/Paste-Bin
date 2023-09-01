package com.akinwalehabib.pastebin.controllers.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

import com.akinwalehabib.pastebin.data.Category;
import com.akinwalehabib.pastebin.data.Exposure;
import com.akinwalehabib.pastebin.data.Paste;

public record PasteRequest(
  @NotNull(message = "Paste name must not be null")
  @NotBlank(message = "Paste name not be blank")
  String name,

  @NotNull(message = "Content must not be null")
  @NotBlank(message = "Content must not be blank")

  String content,
  String password,
  List<String> tags,
  Category category,
  String syntax,
  String expiresOn,
  Exposure exposure,
  Boolean burnAfterRead
){
  public static PasteRequest of(
    String name,
    String content,
    String password,
    List<String> tags,
    Category category,
    String syntax,
    String expiresOn,
    Exposure exposure,
    Boolean burnAfterRead
  ) {
    return new PasteRequest(name, content, password, tags, category, syntax, expiresOn, exposure, burnAfterRead);
  }

  public Paste toPaste() {
    Paste paste = new Paste();
    paste.setContent(this.content());
    paste.setName(this.name());
    paste.setPassword(this.password());
    paste.setTags(this.tags());
    paste.setCategory(this.category());
    paste.setSyntax(this.syntax());
    paste.setExpiresOn(this.expiresOn());
    paste.setExposure(this.exposure());
    paste.setBurnAfterRead(this.burnAfterRead());

    return paste;
  }
}
