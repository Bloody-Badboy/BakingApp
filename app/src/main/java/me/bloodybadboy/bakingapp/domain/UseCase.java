package me.bloodybadboy.bakingapp.domain;

public interface UseCase<T> {
  T execute();
}
