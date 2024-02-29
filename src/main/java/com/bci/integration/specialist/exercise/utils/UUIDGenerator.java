package com.bci.integration.specialist.exercise.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public final class UUIDGenerator {

  public static UUID generateType5UUID(String name) {

    try {

      final byte[] bytes = name.getBytes(StandardCharsets.UTF_8);
      final MessageDigest md = MessageDigest.getInstance("SHA-1");

      final byte[] hash = md.digest(bytes);

      long msb = getLeastAndMostSignificantBitsVersion5(hash, 0);
      long lsb = getLeastAndMostSignificantBitsVersion5(hash, 8);
      // Set the version field
      msb &= ~(0xfL << 12);
      msb |= 5L << 12;
      // Set the variant field to 2
      lsb &= ~(0x3L << 62);
      lsb |= 2L << 62;
      return new UUID(msb, lsb);

    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }

  private static long getLeastAndMostSignificantBitsVersion5(final byte[] src, final int offset) {
    long ans = 0;
    for (int i = offset + 7; i >= offset; i -= 1) {
      ans <<= 8;
      ans |= src[i] & 0xffL;
    }
    return ans;
  }
}
