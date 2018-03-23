import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { AlertController } from 'ionic-angular';
import { App } from 'ionic-angular';

import { LoginPage } from '../login/login';

@Component({
  selector: 'page-about',
  templateUrl: 'about.html'
})
export class AboutPage {

  constructor(public navCtrl: NavController, public alertCtrl: AlertController, public app: App)
  {}

  onExitClick()
  {
    let confirm = this.alertCtrl.create({
      title: 'Cerrar Sesión',
      message: "Desea salir de la aplicación?",
      buttons: [
        {
          text: 'Si',
          handler: data => {
            localStorage.removeItem('token');
            this.app.getRootNav().setRoot( LoginPage )
          }
        },
        { text: 'No' }
      ]
    });
    confirm.present();
  }

}
