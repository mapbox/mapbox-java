package com.mapbox.services.android.telemetry.location;

import android.content.Context;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocationSourceChainSupplierTest {

  @Test
  public void checksSupplyGoogleLocationSource() {
    Context mockedContext = mock(Context.class);
    ClasspathChecker classpathChecker = mock(ClasspathChecker.class);
    when(classpathChecker.hasDependencyOnClasspath(anyString())).thenReturn(true);
    List<LocationSourceChain> locationSources = new ArrayList<>();
    LocationSourceChain mockedGoogleLocationSourceChain = new MockLocationSourceProvider(classpathChecker, "Google");
    locationSources.add(mockedGoogleLocationSourceChain);
    LocationSourceChain mockedLostLocationSourceChain = new MockLocationSourceProvider(classpathChecker, "Lost");
    locationSources.add(mockedLostLocationSourceChain);
    LocationSourceChain mockedAndroidLocationSourceChain = new MockLocationSourceProvider(classpathChecker, "Android");
    locationSources.add(mockedAndroidLocationSourceChain);
    LocationSourceChainSupplier locationSourceChainSupplier = new LocationSourceChainSupplier(locationSources);

    LocationEngine actualLocationEngine = locationSourceChainSupplier.supply(mockedContext);

    assertTrue(actualLocationEngine instanceof MockGoogleLocationSource);
  }

  @Test
  public void checksSupplyLostLocationSource() {
    Context mockedContext = mock(Context.class);
    ClasspathChecker classpathChecker = mock(ClasspathChecker.class);
    when(classpathChecker.hasDependencyOnClasspath(anyString())).thenReturn(false, true);
    List<LocationSourceChain> locationSources = new ArrayList<>();
    LocationSourceChain mockedGoogleLocationSourceChain = new MockLocationSourceProvider(classpathChecker, "Google");
    locationSources.add(mockedGoogleLocationSourceChain);
    LocationSourceChain mockedLostLocationSourceChain = new MockLocationSourceProvider(classpathChecker, "Lost");
    locationSources.add(mockedLostLocationSourceChain);
    LocationSourceChain mockedAndroidLocationSourceChain = new MockLocationSourceProvider(classpathChecker, "Android");
    locationSources.add(mockedAndroidLocationSourceChain);
    LocationSourceChainSupplier locationSourceChainSupplier = new LocationSourceChainSupplier(locationSources);


    LocationEngine actualLocationEngine = locationSourceChainSupplier.supply(mockedContext);

    assertTrue(actualLocationEngine instanceof MockLostLocationSource);
  }

  @Test
  public void checksSupplyAndroidLocationSource() {
    Context mockedContext = mock(Context.class);
    ClasspathChecker classpathChecker = mock(ClasspathChecker.class);
    when(classpathChecker.hasDependencyOnClasspath(anyString())).thenReturn(false, false, true);
    List<LocationSourceChain> locationSources = new ArrayList<>();
    LocationSourceChain mockedGoogleLocationSourceChain = new MockLocationSourceProvider(classpathChecker, "Google");
    locationSources.add(mockedGoogleLocationSourceChain);
    LocationSourceChain mockedLostLocationSourceChain = new MockLocationSourceProvider(classpathChecker, "Lost");
    locationSources.add(mockedLostLocationSourceChain);
    LocationSourceChain mockedAndroidLocationSourceChain = new MockLocationSourceProvider(classpathChecker, "Android");
    locationSources.add(mockedAndroidLocationSourceChain);
    LocationSourceChainSupplier locationSourceChainSupplier = new LocationSourceChainSupplier(locationSources);

    LocationEngine actualLocationEngine = locationSourceChainSupplier.supply(mockedContext);

    assertTrue(actualLocationEngine instanceof MockAndroidLocationSource);
  }

  @Test
  public void checksOneElementLocationSources() {
    Context mockedContext = mock(Context.class);
    ClasspathChecker classpathChecker = mock(ClasspathChecker.class);
    when(classpathChecker.hasDependencyOnClasspath(anyString())).thenReturn(true);
    List<LocationSourceChain> locationSources = new ArrayList<>();
    LocationSourceChain mockedGoogleLocationSourceChain = new MockLocationSourceProvider(classpathChecker, "Google");
    locationSources.add(mockedGoogleLocationSourceChain);
    LocationSourceChainSupplier locationSourceChainSupplier = new LocationSourceChainSupplier(locationSources);

    LocationEngine actualLocationEngine = locationSourceChainSupplier.supply(mockedContext);

    assertTrue(actualLocationEngine instanceof MockGoogleLocationSource);
  }

  @Test
  public void checksEmptyLocationSources() {
    Context mockedContext = mock(Context.class);
    List locationSources = mock(List.class);
    MockAndroidLocationSource theMockedAndroidLocationSource = new MockAndroidLocationSource();
    LocationSourceChain mockedGoogleLocationSourceChain = mock(MockLocationSourceProvider.class);
    when(locationSources.size()).thenReturn(0);
    when(locationSources.get(0)).thenReturn(mockedGoogleLocationSourceChain);
    when(mockedGoogleLocationSourceChain.supply(mockedContext)).thenReturn(theMockedAndroidLocationSource);
    LocationSourceChainSupplier locationSourceChainSupplier = new LocationSourceChainSupplier(locationSources);

    LocationEngine actual = locationSourceChainSupplier.supply(mockedContext);

    assertEquals(theMockedAndroidLocationSource, actual);
  }
}