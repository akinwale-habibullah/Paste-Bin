package com.akinwalehabib.pastebin.controllers.response;

import java.util.List;

import com.akinwalehabib.pastebin.data.Paste;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasteResponseListResponse{
  
  private List<Paste> data;

  public PasteResponseListResponse(List<Paste> message) {
      super();
      this.data = message;
  }
}
