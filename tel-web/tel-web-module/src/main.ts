import '@/assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { createI18n } from "vue-i18n";

import App from '@/app/App.vue'
import router from '@/app/shared/router'
import PrimeVue from 'primevue/config'

// Import PrimeVue components
import Card from 'primevue/card'
import Toolbar from "primevue/toolbar";

// PrimeVue Theme
import '/node_modules/primeflex/primeflex.css'
import 'primevue/resources/themes/lara-light-indigo/theme.css'

// Create i18n instance with supported languages
import enUS from "@/app/locales/en-US.json";

type MessageSchema = typeof enUS;

const i18n = createI18n<[MessageSchema], "en-US">({
  locale: "en-US", // set locale
  fallbackLocale: "en-US", // set fallback locale
  messages: {
    "en-US": enUS,
  }, // set locale messages
  legacy: false,
});

// Create App
const app = createApp(App)

app.use(createPinia())
app.use(i18n);
app.use(router)
app.use(PrimeVue)

// Add PrimeVue components
app.component('p-card', Card);
app.component('p-toolbar', Toolbar);

app.mount('#app')
