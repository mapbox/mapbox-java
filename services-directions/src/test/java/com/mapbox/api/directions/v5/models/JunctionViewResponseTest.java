package com.mapbox.api.directions.v5.models;

import com.mapbox.core.TestUtils;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class JunctionViewResponseTest extends TestUtils {

  private static final String BANNER_INSTRUCTION_V5_JUNCTION_VIEW_FIXTURE = "banner_instruction_v5_junction_view.json";

  @Test
  public void shouldReadBannerInstruction() throws Exception {
    String json = loadJsonFixture(BANNER_INSTRUCTION_V5_JUNCTION_VIEW_FIXTURE);
    BannerInstructions response = BannerInstructions.fromJson(json);
    assertNotNull(response);
  }

  @Test
  public void fromtestToFromJson() throws Exception {
    String json = loadJsonFixture(BANNER_INSTRUCTION_V5_JUNCTION_VIEW_FIXTURE);
    BannerInstructions responseFromJson1 = BannerInstructions.fromJson(json);

    String jsonString = responseFromJson1.toJson();
    BannerInstructions responseFromJson2 = BannerInstructions.fromJson(jsonString);

    Assert.assertEquals(responseFromJson1, responseFromJson2);
    Assert.assertEquals(responseFromJson2, responseFromJson1);
  }

  @Test
  public void testValuesFromJson() throws Exception {
    String json = loadJsonFixture(BANNER_INSTRUCTION_V5_JUNCTION_VIEW_FIXTURE);
    BannerInstructions responseFromJson = BannerInstructions.fromJson(json);

    BannerView bannerView = responseFromJson.view();
    Assert.assertEquals(bannerView.text(), "CA01610_1_E");
    Assert.assertEquals(bannerView.type(), "fork");
    Assert.assertEquals(bannerView.modifier(), "right");

    List<BannerComponents> bannerComponents = responseFromJson.view().components();
    Assert.assertEquals(bannerComponents.size(), 1);
    Assert.assertEquals(bannerComponents.get(0).text(), "CA01610_1_E");
    Assert.assertEquals(bannerComponents.get(0).type(), "guidance-view");
    // TODO add the url check
  }
}
