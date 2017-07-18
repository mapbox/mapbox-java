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

public class LocationEngineChainSupplierTest {

  @Test
  public void checksSupplyGoogleLocationSource() {
    Context mockedContext = mock(Context.class);
    ClasspathChecker classpathChecker = mock(ClasspathChecker.class);
    when(classpathChecker.hasDependencyOnClasspath(anyString())).thenReturn(true);
    List<LocationEngineChain> locationSources = new ArrayList<>();
    LocationEngineChain mockedGoogleLocationEngineChain = new MockLocationEngineProvider(classpathChecker, "Google");
    locationSources.add(mockedGoogleLocationEngineChain);
    LocationEngineChain mockedLostLocationEngineChain = new MockLocationEngineProvider(classpathChecker, "Lost");
    locationSources.add(mockedLostLocationEngineChain);
    LocationEngineChain mockedAndroidLocationEngineChain = new MockLocationEngineProvider(classpathChecker, "Android");
    locationSources.add(mockedAndroidLocationEngineChain);
    LocationEngineChainSupplier locationEngineChainSupplier = new LocationEngineChainSupplier(locationSources);

    LocationEngine actualLocationEngine = locationEngineChainSupplier.supply(mockedContext);

    assertTrue(actualLocationEngine instanceof MockGoogleLocationSource);
  }

  @Test
  public void checksSupplyLostLocationSource() {
    Context mockedContext = mock(Context.class);
    ClasspathChecker classpathChecker = mock(ClasspathChecker.class);
    when(classpathChecker.hasDependencyOnClasspath(anyString())).thenReturn(false, true);
    List<LocationEngineChain> locationSources = new ArrayList<>();
    LocationEngineChain mockedGoogleLocationEngineChain = new MockLocationEngineProvider(classpathChecker, "Google");
    locationSources.add(mockedGoogleLocationEngineChain);
    LocationEngineChain mockedLostLocationEngineChain = new MockLocationEngineProvider(classpathChecker, "Lost");
    locationSources.add(mockedLostLocationEngineChain);
    LocationEngineChain mockedAndroidLocationEngineChain = new MockLocationEngineProvider(classpathChecker, "Android");
    locationSources.add(mockedAndroidLocationEngineChain);
    LocationEngineChainSupplier locationEngineChainSupplier = new LocationEngineChainSupplier(locationSources);


    LocationEngine actualLocationEngine = locationEngineChainSupplier.supply(mockedContext);

    assertTrue(actualLocationEngine instanceof MockLostLocationSource);
  }

  @Test
  public void checksSupplyAndroidLocationSource() {
    Context mockedContext = mock(Context.class);
    ClasspathChecker classpathChecker = mock(ClasspathChecker.class);
    when(classpathChecker.hasDependencyOnClasspath(anyString())).thenReturn(false, false, true);
    List<LocationEngineChain> locationSources = new ArrayList<>();
    LocationEngineChain mockedGoogleLocationEngineChain = new MockLocationEngineProvider(classpathChecker, "Google");
    locationSources.add(mockedGoogleLocationEngineChain);
    LocationEngineChain mockedLostLocationEngineChain = new MockLocationEngineProvider(classpathChecker, "Lost");
    locationSources.add(mockedLostLocationEngineChain);
    LocationEngineChain mockedAndroidLocationEngineChain = new MockLocationEngineProvider(classpathChecker, "Android");
    locationSources.add(mockedAndroidLocationEngineChain);
    LocationEngineChainSupplier locationEngineChainSupplier = new LocationEngineChainSupplier(locationSources);

    LocationEngine actualLocationEngine = locationEngineChainSupplier.supply(mockedContext);

    assertTrue(actualLocationEngine instanceof MockAndroidLocationSource);
  }

  @Test
  public void checksOneElementLocationSources() {
    Context mockedContext = mock(Context.class);
    ClasspathChecker classpathChecker = mock(ClasspathChecker.class);
    when(classpathChecker.hasDependencyOnClasspath(anyString())).thenReturn(true);
    List<LocationEngineChain> locationSources = new ArrayList<>();
    LocationEngineChain mockedGoogleLocationEngineChain = new MockLocationEngineProvider(classpathChecker, "Google");
    locationSources.add(mockedGoogleLocationEngineChain);
    LocationEngineChainSupplier locationEngineChainSupplier = new LocationEngineChainSupplier(locationSources);

    LocationEngine actualLocationEngine = locationEngineChainSupplier.supply(mockedContext);

    assertTrue(actualLocationEngine instanceof MockGoogleLocationSource);
  }

  @Test
  public void checksEmptyLocationSources() {
    Context mockedContext = mock(Context.class);
    List locationSources = mock(List.class);
    MockAndroidLocationSource theMockedAndroidLocationSource = new MockAndroidLocationSource();
    LocationEngineChain mockedGoogleLocationEngineChain = mock(MockLocationEngineProvider.class);
    when(locationSources.size()).thenReturn(0);
    when(locationSources.get(0)).thenReturn(mockedGoogleLocationEngineChain);
    when(mockedGoogleLocationEngineChain.supply(mockedContext)).thenReturn(theMockedAndroidLocationSource);
    LocationEngineChainSupplier locationEngineChainSupplier = new LocationEngineChainSupplier(locationSources);

    LocationEngine actual = locationEngineChainSupplier.supply(mockedContext);

    assertEquals(theMockedAndroidLocationSource, actual);
  }
}