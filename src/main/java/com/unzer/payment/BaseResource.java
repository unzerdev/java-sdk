package com.unzer.payment;

public abstract class BaseResource implements Resource {
  private final static String RESOURCE_ID_TOKEN = "<resourceId>";

  @Override
  public String getUrl() {
//    return getId() == null
//        ? getResourceUrl()
//        : getResourceUrl().replaceAll(RESOURCE_ID_TOKEN, getId());
    return getResourceUrl().replaceAll(RESOURCE_ID_TOKEN, getId() == null ? "" : getId());
  }

  protected abstract String getResourceUrl();
}
