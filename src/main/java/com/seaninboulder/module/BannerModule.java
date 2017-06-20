package com.seaninboulder.module;

import java.io.IOException;
import java.net.URL;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.inject.Binder;
import com.google.inject.Module;

public final class BannerModule implements Module {
  @Override
  public void configure(Binder binder) {
    try {
      System.out.println(banner());
    } catch (IOException e) {

    }
  }

  private String banner() throws IOException {
    URL url = Resources.getResource("banner.txt");
    return Resources.toString(url, Charsets.UTF_8);
  }
}
