package com.mapbox.api.directions.v5.models;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class JunctionViewResponseTest {

  private static final String BANNER_INSTRUCTION_JSON = "{\"distanceAlongGeometry\":139.2,\"primary\":{\"text\":\"E23\",\"components\":[{\"text\":\"E23\",\"type\":\"icon\"}],\"type\":\"fork\",\"modifier\":\"right\"},\"secondary\":{\"text\":\"東名阪自動車道 / 亀山 / 四日市 / 東名阪自動車道\",\"components\":[{\"text\":\"東名阪自動車道\",\"type\":\"text\"},{\"text\":\"/\",\"type\":\"text\"},{\"text\":\"亀山\",\"type\":\"text\"},{\"text\":\"/\",\"type\":\"text\"},{\"text\":\"四日市\",\"type\":\"text\"},{\"text\":\"/\",\"type\":\"text\"},{\"text\":\"東名阪自動車道\",\"type\":\"text\"}],\"type\":\"fork\",\"modifier\":\"right\"},\"view\":{\"text\":\"CA01610_1_E\",\"components\":[{\"text\":\"CA01610_1_E\",\"type\":\"guidance-view\",\"url\":\"https://api-turn-here-staging-451578336.us-east-1.elb.amazonaws.com/guidance-views/v1/z/jct/CA01610_1_E\"}],\"type\":\"fork\",\"modifier\":\"right\"}}";

  @Test
  public void shouldReadBannerInstruction() {
    BannerInstructions response = BannerInstructions.fromJson(BANNER_INSTRUCTION_JSON);
    assertNotNull(response);
  }

  @Test
  public void fromtestToFromJson() {
    BannerInstructions responseFromJson1 = BannerInstructions.fromJson(BANNER_INSTRUCTION_JSON);

    String jsonString = responseFromJson1.toJson();
    BannerInstructions responseFromJson2 = BannerInstructions.fromJson(jsonString);

    Assert.assertEquals(responseFromJson1, responseFromJson2);
    Assert.assertEquals(responseFromJson2, responseFromJson1);
  }

  @Test
  public void testValuesFromJson() {
    BannerInstructions responseFromJson = BannerInstructions.fromJson(BANNER_INSTRUCTION_JSON);

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
