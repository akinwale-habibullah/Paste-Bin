package com.akinwalehabib.pastebin.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.akinwalehabib.pastebin.controllers.exceptions.BadRequestException;
import com.akinwalehabib.pastebin.controllers.exceptions.ResourceNotFoundException;
import com.akinwalehabib.pastebin.controllers.exceptions.UnauthorizedException;
import com.akinwalehabib.pastebin.controllers.request.PasteRequest;
import com.akinwalehabib.pastebin.controllers.response.PasteResponse;
import com.akinwalehabib.pastebin.controllers.response.PasteResponseListResponse;
import com.akinwalehabib.pastebin.data.Paste;
import com.akinwalehabib.pastebin.data.User;
import com.akinwalehabib.pastebin.services.PasteService;
import com.akinwalehabib.pastebin.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/pastes",
                produces = MediaType.APPLICATION_JSON_VALUE)
public class PasteController {

  @Autowired
  private PasteService pasteService;

  @Autowired
  private UserService userService;

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public PasteResponse addPaste(
    @Valid @RequestBody PasteRequest pasteRequest,
    Errors errors,
    Principal principal) {
      if (errors.hasErrors()) {
        throw new BadRequestException("Invalid request body");
      }

      Paste paste = pasteRequest.toPaste();
      if (principal != null) {
        Optional<User> user = userService.findByEmail(principal.getName());
        paste.setUser(user.get());  
      }

      Paste newPaste = pasteService.createPaste(paste);
      return new PasteResponse(newPaste);
  }

  @GetMapping("/recentPastes")
  public PasteResponseListResponse getRecentPastes() {
    return new PasteResponseListResponse(pasteService.getTopPastes());
  }

  @GetMapping
  public PasteResponseListResponse getPastesByPage (
    @RequestParam(name = "page", required = false, defaultValue = "0") int page,
    @RequestParam(name = "size", required = false, defaultValue = "5") int size) {
      Page<Paste> pastes = pasteService.getAllPastesByPage(page, size);
      return new PasteResponseListResponse(pastes.getContent());
  }

  @GetMapping(path = "/{id}")
  public PasteResponse getPaste (@PathVariable long id) throws Exception {
    Optional<Paste> paste = pasteService.getPaste(id);

    if(!paste.isPresent()) {
      throw new ResourceNotFoundException("Paste does not exist.");
    }

    if (paste.get().getPassword() != null) {
      throw new Exception("Undefined handler for Pastes with password.");
    }

    if (paste.get().isBurnAfterRead()) {
      pasteService.deletePaste(id);
    }

    return new PasteResponse(paste);
  }

  @PatchMapping(path = "/{id}",
                consumes = MediaType.APPLICATION_JSON_VALUE)
  public PasteResponse updatePaste(
    @PathVariable long id,
    @Valid @RequestBody Paste paste,
    Errors errors,
    Principal principal) {
    if (errors.hasErrors()) {
      throw new BadRequestException("Invalid request body");
    }

    Optional<Paste> pasteToUpdate = pasteService.getPaste(id);
    if (!pasteToUpdate.isPresent()) {
      throw new ResourceNotFoundException("Paste does not exist");
    }
    String userId = pasteToUpdate.get().getUser().getId().toString();

    Optional<User> user = userService.findByEmail(principal.getName());
    if (!userId.equals(user.get().getId().toString())) {
      throw new UnauthorizedException("You are not authorized to edit this paste");
    }

    Paste updatedPaste = pasteService.updatePaste(id, paste, pasteToUpdate.get());
    return new PasteResponse(updatedPaste);
  }

  @DeleteMapping(path = "/{id}")
  public void deletePaste(@PathVariable long id,
    Principal principal) {
      Optional<Paste> pasteToDelete = pasteService.getPaste(id);
      if (!pasteToDelete.isPresent()) {
        throw new ResourceNotFoundException("Paste not found.");
      }

      Optional<User> user = userService.findByEmail(principal.getName());
      if (!pasteToDelete.get().getUser().getId().toString().equals(user.get().getId().toString())) {
        throw new UnauthorizedException("You are unauthorised to delete this resource");
      }

      pasteService.deletePaste(id);
  }
}
