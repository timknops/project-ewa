import { createApp } from "vue";
import App from "./App.vue";

/* import bootstrap */
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap";

/* import the fontawesome core */
import { library } from "@fortawesome/fontawesome-svg-core";

/* import font awesome icon component */
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

/* import specific icons
 * import more icons if needed
 * */
import { faXmark } from "@fortawesome/free-solid-svg-icons";
import { faCheck } from "@fortawesome/free-solid-svg-icons";
import { faChevronLeft } from "@fortawesome/free-solid-svg-icons";
import { faChevronRight } from "@fortawesome/free-solid-svg-icons";

/* add icons to the library */
library.add(faXmark, faCheck, faChevronLeft, faChevronRight);

createApp(App).component("font-awesome-icon", FontAwesomeIcon).mount("#app");
