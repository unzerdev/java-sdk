package com.unzer.payment;

public class Processing {
  private String uniqueId;
  private String shortId;
  private String descriptor;
  private String bic;
  private String iban;
  private String holder;
  private String pdfLink;
  private String externalOrderId;
  private String zgReferenceId;
  private String creatorId;
  private String identification;
  private String traceId;
  private String participantId;

  @Override
  public int hashCode() {
    int result = getUniqueId() != null ? getUniqueId().hashCode() : 0;
    result = 31 * result + (getShortId() != null ? getShortId().hashCode() : 0);
    result = 31 * result + (getDescriptor() != null ? getDescriptor().hashCode() : 0);
    result = 31 * result + (getBic() != null ? getBic().hashCode() : 0);
    result = 31 * result + (getIban() != null ? getIban().hashCode() : 0);
    result = 31 * result + (getHolder() != null ? getHolder().hashCode() : 0);
    result = 31 * result + (getPdfLink() != null ? getPdfLink().hashCode() : 0);
    result = 31 * result + (getExternalOrderId() != null ? getExternalOrderId().hashCode() : 0);
    result = 31 * result + (getZgReferenceId() != null ? getZgReferenceId().hashCode() : 0);
    result = 31 * result + (getCreatorId() != null ? getCreatorId().hashCode() : 0);
    result = 31 * result + (getIdentification() != null ? getIdentification().hashCode() : 0);
    result = 31 * result + (getTraceId() != null ? getTraceId().hashCode() : 0);
    result = 31 * result + (getParticipantId() != null ? getParticipantId().hashCode() : 0);
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Processing that = (Processing) o;

    if (getUniqueId() != null ? !getUniqueId().equals(that.getUniqueId()) :
        that.getUniqueId() != null) {
      return false;
    }
    if (getShortId() != null ? !getShortId().equals(that.getShortId()) :
        that.getShortId() != null) {
      return false;
    }
    if (getDescriptor() != null ? !getDescriptor().equals(that.getDescriptor()) :
        that.getDescriptor() != null) {
      return false;
    }
    if (getBic() != null ? !getBic().equals(that.getBic()) : that.getBic() != null) {
      return false;
    }
    if (getIban() != null ? !getIban().equals(that.getIban()) : that.getIban() != null) {
      return false;
    }
    if (getHolder() != null ? !getHolder().equals(that.getHolder()) : that.getHolder() != null) {
      return false;
    }
    if (getPdfLink() != null ? !getPdfLink().equals(that.getPdfLink()) :
        that.getPdfLink() != null) {
      return false;
    }
    if (getExternalOrderId() != null ? !getExternalOrderId().equals(that.getExternalOrderId()) :
        that.getExternalOrderId() != null) {
      return false;
    }
    if (getZgReferenceId() != null ? !getZgReferenceId().equals(that.getZgReferenceId()) :
        that.getZgReferenceId() != null) {
      return false;
    }
    if (getCreatorId() != null ? !getCreatorId().equals(that.getCreatorId()) :
        that.getCreatorId() != null) {
      return false;
    }
    if (getIdentification() != null ? !getIdentification().equals(that.getIdentification()) :
        that.getIdentification() != null) {
      return false;
    }
    if (getTraceId() != null ? !getTraceId().equals(that.getTraceId()) :
        that.getTraceId() != null) {
      return false;
    }
    return getParticipantId() != null ? getParticipantId().equals(that.getParticipantId()) :
        that.getParticipantId() == null;
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public Processing setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
    return this;
  }

  public String getShortId() {
    return shortId;
  }

  public Processing setShortId(String shortId) {
    this.shortId = shortId;
    return this;
  }

  public String getDescriptor() {
    return descriptor;
  }

  public Processing setDescriptor(String descriptor) {
    this.descriptor = descriptor;
    return this;
  }

  public String getBic() {
    return bic;
  }

  public Processing setBic(String bic) {
    this.bic = bic;
    return this;
  }

  public String getIban() {
    return iban;
  }

  public Processing setIban(String iban) {
    this.iban = iban;
    return this;
  }

  public String getHolder() {
    return holder;
  }

  public Processing setHolder(String holder) {
    this.holder = holder;
    return this;
  }

  public String getPdfLink() {
    return pdfLink;
  }

  public Processing setPdfLink(String pdfLink) {
    this.pdfLink = pdfLink;
    return this;
  }

  public String getExternalOrderId() {
    return externalOrderId;
  }

  public Processing setExternalOrderId(String externalOrderId) {
    this.externalOrderId = externalOrderId;
    return this;
  }

  public String getZgReferenceId() {
    return zgReferenceId;
  }

  public Processing setZgReferenceId(String zgReferenceId) {
    this.zgReferenceId = zgReferenceId;
    return this;
  }

  public String getCreatorId() {
    return creatorId;
  }

  public Processing setCreatorId(String creatorId) {
    this.creatorId = creatorId;
    return this;
  }

  public String getIdentification() {
    return identification;
  }

  public Processing setIdentification(String identification) {
    this.identification = identification;
    return this;
  }

  public String getTraceId() {
    return traceId;
  }

  public Processing setTraceId(String traceId) {
    this.traceId = traceId;
    return this;
  }

  public String getParticipantId() {
    return participantId;
  }

  public Processing setParticipantId(String participantId) {
    this.participantId = participantId;
    return this;
  }
}
