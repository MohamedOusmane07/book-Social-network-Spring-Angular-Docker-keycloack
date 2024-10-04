package com.lamine.book.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
  ACTIVATE_ACCOUNT("active_account");

  private final String name;

  EmailTemplateName(String name) {
    this.name = name;
  }
}
