package com.mapbox.api.directions.models;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class JunctionViewResponseTest {

  private static final String BANNER_INSTRUCTION_JSON = "{\"distanceAlongGeometry\":139.2,\"primary\":{\"text\":\"E23\",\"components\":[{\"text\":\"E23\",\"type\":\"icon\"}],\"type\":\"fork\",\"modifier\":\"right\"},\"secondary\":{\"text\":\"東名阪自動車道 / 亀山 / 四日市 / 東名阪自動車道\",\"components\":[{\"text\":\"東名阪自動車道\",\"type\":\"text\"},{\"text\":\"/\",\"type\":\"text\"},{\"text\":\"亀山\",\"type\":\"text\"},{\"text\":\"/\",\"type\":\"text\"},{\"text\":\"四日市\",\"type\":\"text\"},{\"text\":\"/\",\"type\":\"text\"},{\"text\":\"東名阪自動車道\",\"type\":\"text\"}],\"type\":\"fork\",\"modifier\":\"right\"},\"view\":{\"text\":\"CA01610_1_E\",\"components\":[{\"text\":\"CA01610_1_E\",\"type\":\"guidance-view\",\"imageURL\":\"https://api-turn-here-staging-451578336.us-east-1.elb.amazonaws.com/guidance-views/v1/z/jct/CA01610_1_E\"}],\"type\":\"fork\",\"modifier\":\"right\"}}";

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
    Assert.assertEquals(bannerView.type(), StepManeuver.FORK);
    Assert.assertEquals(bannerView.modifier(), "right");

    Assert.assertEquals(responseFromJson.view().components().size(), 1);
    BannerComponents bannerComponent = responseFromJson.view().components().get(0);
    Assert.assertEquals(bannerComponent.text(), "CA01610_1_E");
    Assert.assertEquals(bannerComponent.type(), BannerComponents.GUIDANCE_VIEW);
    Assert.assertEquals(bannerComponent.imageUrl(), "https://api-turn-here-staging-451578336.us-east-1.elb.amazonaws.com/guidance-views/v1/z/jct/CA01610_1_E");
  }
}
