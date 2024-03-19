import { describe, it, expect, vitest } from "vitest";
import { mount } from "@vue/test-utils";

import PrimeVue from "primevue/config";

import LayoutHeader from "../LayoutHeader.vue";
import Toolbar from "primevue/toolbar";

import { createTestingPinia } from "@pinia/testing";
import { I18NMocks } from "@/tests/i18n";

describe("LayoutHeader.vue Tests", () => {
  const primeOptions = {
    global: {
      mocks: {
        $t: I18NMocks.t([
          ['app.title', 'App Title']
        ]),
      },
      plugins: [
        PrimeVue,
        createTestingPinia({
          createSpy: vitest.fn(),
        }),
      ],
      components: {
        "p-toolbar": Toolbar
      },
    },
  };

  it("Render application title", () => {
    const wrapper = mount(LayoutHeader, primeOptions);

    const element = wrapper.find("span");
    expect(element).toBeDefined();
    expect(element.attributes("class")).toStrictEqual("app-title");
    expect(element.text()).toStrictEqual("App Title");
  });
});
