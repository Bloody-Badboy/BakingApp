package me.bloodybadboy.bakingapp;

import org.junit.Test;

import me.bloodybadboy.bakingapp.domain.Result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ResultTest {

  @Test
  public void Result_Success() {
    Result<String> result = Result.Success("Hello World!");
    assertTrue(result.succeeded());

    assertEquals("Hello World!", result.data);
  }

  @Test
  public void Result_Error() {
    Exception exception = new Exception();

    Result<String> result = Result.Error(exception);
    assertFalse(result.succeeded());

    assertEquals(exception, result.exception);
  }

  @Test
  public void Result_Loading() {
    Result<String> result = Result.Loading();
    assertTrue(result.loading());

    assertFalse(result.succeeded());
  }
}