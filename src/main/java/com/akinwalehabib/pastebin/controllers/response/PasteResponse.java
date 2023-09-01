package com.akinwalehabib.pastebin.controllers.response;

import java.util.Optional;

import com.akinwalehabib.pastebin.data.Paste;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PasteResponse{
  
  private Paste data;

  public PasteResponse(Paste message) {
      super();
      this.data = message;
  }

  public PasteResponse(Optional<Paste> message) {
      super();
      this.data = message.get();
  }
}
