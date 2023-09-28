import { createApp } from 'vue'
import { router } from './router'
import App from './App.vue'

/* import bootstrap */
import "bootstrap/dist/css/bootstrap.min.css";
import 'bootstrap';

/* import the fontawesome core */
import { library } from '@fortawesome/fontawesome-svg-core'

/* import font awesome icon component */
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

/* import specific icons
* import more icons if needed
* */
import { faXmark } from '@fortawesome/free-solid-svg-icons'
import { faCheck } from '@fortawesome/free-solid-svg-icons'
import { faHouse } from '@fortawesome/free-solid-svg-icons'
import { faBoxesStacked } from '@fortawesome/free-solid-svg-icons'
import { faDiagramProject } from '@fortawesome/free-solid-svg-icons'
import { faWarehouse } from '@fortawesome/free-solid-svg-icons'
import { faUsers } from '@fortawesome/free-solid-svg-icons'
import { faUser } from '@fortawesome/free-solid-svg-icons'

/* add icons to the library */
library.add(faXmark, faCheck, faHouse, faBoxesStacked, faDiagramProject, faWarehouse, faUsers, faUser)

createApp(App).use(router).component('font-awesome-icon',FontAwesomeIcon).mount('#app')
