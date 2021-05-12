/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package io.confluent.demo.bankaccount.avro.pojo;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class Withdrawal extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 8651518530595273040L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Withdrawal\",\"namespace\":\"io.confluent.demo.bankaccount.avro.pojo\",\"fields\":[{\"name\":\"account_id\",\"type\":\"long\"},{\"name\":\"amount\",\"type\":\"double\"},{\"name\":\"withdrawal_date\",\"type\":\"long\",\"logicalType\":\"date\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<Withdrawal> ENCODER =
      new BinaryMessageEncoder<Withdrawal>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<Withdrawal> DECODER =
      new BinaryMessageDecoder<Withdrawal>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<Withdrawal> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<Withdrawal> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<Withdrawal> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<Withdrawal>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this Withdrawal to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a Withdrawal from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a Withdrawal instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static Withdrawal fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public long account_id;
  @Deprecated public double amount;
  @Deprecated public long withdrawal_date;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public Withdrawal() {}

  /**
   * All-args constructor.
   * @param account_id The new value for account_id
   * @param amount The new value for amount
   * @param withdrawal_date The new value for withdrawal_date
   */
  public Withdrawal(java.lang.Long account_id, java.lang.Double amount, java.lang.Long withdrawal_date) {
    this.account_id = account_id;
    this.amount = amount;
    this.withdrawal_date = withdrawal_date;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return account_id;
    case 1: return amount;
    case 2: return withdrawal_date;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: account_id = (java.lang.Long)value$; break;
    case 1: amount = (java.lang.Double)value$; break;
    case 2: withdrawal_date = (java.lang.Long)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'account_id' field.
   * @return The value of the 'account_id' field.
   */
  public long getAccountId() {
    return account_id;
  }


  /**
   * Sets the value of the 'account_id' field.
   * @param value the value to set.
   */
  public void setAccountId(long value) {
    this.account_id = value;
  }

  /**
   * Gets the value of the 'amount' field.
   * @return The value of the 'amount' field.
   */
  public double getAmount() {
    return amount;
  }


  /**
   * Sets the value of the 'amount' field.
   * @param value the value to set.
   */
  public void setAmount(double value) {
    this.amount = value;
  }

  /**
   * Gets the value of the 'withdrawal_date' field.
   * @return The value of the 'withdrawal_date' field.
   */
  public long getWithdrawalDate() {
    return withdrawal_date;
  }


  /**
   * Sets the value of the 'withdrawal_date' field.
   * @param value the value to set.
   */
  public void setWithdrawalDate(long value) {
    this.withdrawal_date = value;
  }

  /**
   * Creates a new Withdrawal RecordBuilder.
   * @return A new Withdrawal RecordBuilder
   */
  public static io.confluent.demo.bankaccount.avro.pojo.Withdrawal.Builder newBuilder() {
    return new io.confluent.demo.bankaccount.avro.pojo.Withdrawal.Builder();
  }

  /**
   * Creates a new Withdrawal RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new Withdrawal RecordBuilder
   */
  public static io.confluent.demo.bankaccount.avro.pojo.Withdrawal.Builder newBuilder(io.confluent.demo.bankaccount.avro.pojo.Withdrawal.Builder other) {
    if (other == null) {
      return new io.confluent.demo.bankaccount.avro.pojo.Withdrawal.Builder();
    } else {
      return new io.confluent.demo.bankaccount.avro.pojo.Withdrawal.Builder(other);
    }
  }

  /**
   * Creates a new Withdrawal RecordBuilder by copying an existing Withdrawal instance.
   * @param other The existing instance to copy.
   * @return A new Withdrawal RecordBuilder
   */
  public static io.confluent.demo.bankaccount.avro.pojo.Withdrawal.Builder newBuilder(io.confluent.demo.bankaccount.avro.pojo.Withdrawal other) {
    if (other == null) {
      return new io.confluent.demo.bankaccount.avro.pojo.Withdrawal.Builder();
    } else {
      return new io.confluent.demo.bankaccount.avro.pojo.Withdrawal.Builder(other);
    }
  }

  /**
   * RecordBuilder for Withdrawal instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Withdrawal>
    implements org.apache.avro.data.RecordBuilder<Withdrawal> {

    private long account_id;
    private double amount;
    private long withdrawal_date;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(io.confluent.demo.bankaccount.avro.pojo.Withdrawal.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.account_id)) {
        this.account_id = data().deepCopy(fields()[0].schema(), other.account_id);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.amount)) {
        this.amount = data().deepCopy(fields()[1].schema(), other.amount);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.withdrawal_date)) {
        this.withdrawal_date = data().deepCopy(fields()[2].schema(), other.withdrawal_date);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
    }

    /**
     * Creates a Builder by copying an existing Withdrawal instance
     * @param other The existing instance to copy.
     */
    private Builder(io.confluent.demo.bankaccount.avro.pojo.Withdrawal other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.account_id)) {
        this.account_id = data().deepCopy(fields()[0].schema(), other.account_id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.amount)) {
        this.amount = data().deepCopy(fields()[1].schema(), other.amount);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.withdrawal_date)) {
        this.withdrawal_date = data().deepCopy(fields()[2].schema(), other.withdrawal_date);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'account_id' field.
      * @return The value.
      */
    public long getAccountId() {
      return account_id;
    }


    /**
      * Sets the value of the 'account_id' field.
      * @param value The value of 'account_id'.
      * @return This builder.
      */
    public io.confluent.demo.bankaccount.avro.pojo.Withdrawal.Builder setAccountId(long value) {
      validate(fields()[0], value);
      this.account_id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'account_id' field has been set.
      * @return True if the 'account_id' field has been set, false otherwise.
      */
    public boolean hasAccountId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'account_id' field.
      * @return This builder.
      */
    public io.confluent.demo.bankaccount.avro.pojo.Withdrawal.Builder clearAccountId() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'amount' field.
      * @return The value.
      */
    public double getAmount() {
      return amount;
    }


    /**
      * Sets the value of the 'amount' field.
      * @param value The value of 'amount'.
      * @return This builder.
      */
    public io.confluent.demo.bankaccount.avro.pojo.Withdrawal.Builder setAmount(double value) {
      validate(fields()[1], value);
      this.amount = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'amount' field has been set.
      * @return True if the 'amount' field has been set, false otherwise.
      */
    public boolean hasAmount() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'amount' field.
      * @return This builder.
      */
    public io.confluent.demo.bankaccount.avro.pojo.Withdrawal.Builder clearAmount() {
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'withdrawal_date' field.
      * @return The value.
      */
    public long getWithdrawalDate() {
      return withdrawal_date;
    }


    /**
      * Sets the value of the 'withdrawal_date' field.
      * @param value The value of 'withdrawal_date'.
      * @return This builder.
      */
    public io.confluent.demo.bankaccount.avro.pojo.Withdrawal.Builder setWithdrawalDate(long value) {
      validate(fields()[2], value);
      this.withdrawal_date = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'withdrawal_date' field has been set.
      * @return True if the 'withdrawal_date' field has been set, false otherwise.
      */
    public boolean hasWithdrawalDate() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'withdrawal_date' field.
      * @return This builder.
      */
    public io.confluent.demo.bankaccount.avro.pojo.Withdrawal.Builder clearWithdrawalDate() {
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Withdrawal build() {
      try {
        Withdrawal record = new Withdrawal();
        record.account_id = fieldSetFlags()[0] ? this.account_id : (java.lang.Long) defaultValue(fields()[0]);
        record.amount = fieldSetFlags()[1] ? this.amount : (java.lang.Double) defaultValue(fields()[1]);
        record.withdrawal_date = fieldSetFlags()[2] ? this.withdrawal_date : (java.lang.Long) defaultValue(fields()[2]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<Withdrawal>
    WRITER$ = (org.apache.avro.io.DatumWriter<Withdrawal>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<Withdrawal>
    READER$ = (org.apache.avro.io.DatumReader<Withdrawal>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeLong(this.account_id);

    out.writeDouble(this.amount);

    out.writeLong(this.withdrawal_date);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.account_id = in.readLong();

      this.amount = in.readDouble();

      this.withdrawal_date = in.readLong();

    } else {
      for (int i = 0; i < 3; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.account_id = in.readLong();
          break;

        case 1:
          this.amount = in.readDouble();
          break;

        case 2:
          this.withdrawal_date = in.readLong();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










