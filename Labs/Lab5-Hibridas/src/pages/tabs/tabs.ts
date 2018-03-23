import { Component } from '@angular/core';

import { AboutPage } from '../about/about';
import { EventPage } from '../event/event';

@Component({
  templateUrl: 'tabs.html'
})
export class TabsPage {

  tab2Root = AboutPage;
  tab4Root = EventPage;

  constructor() {}
}
