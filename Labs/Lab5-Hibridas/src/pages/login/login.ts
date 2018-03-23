import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { IonicPage, NavController } from 'ionic-angular';

import { LoadingController } from 'ionic-angular';
import { ToastController } from 'ionic-angular';

import { TabsPage } from '../tabs/tabs';

import { UserService } from '../../services/user.service';
import { User } from '../../services/user';

@IonicPage()
@Component({
  selector: 'page-login',
  templateUrl: 'login.html',
})

export class LoginPage
{
  private loginForm;
  private user: User;
	private loading;

  constructor(
    public navCtrl: NavController,
    private service: UserService,
    public formBuilder:FormBuilder,
    public toast: ToastController,
    public loadingCtrl: LoadingController)
  {
    this.loginForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
    this.loginForm.setValue({
      email: 'ad@min.com',
      password: 'admin'
    });
  }

  ionViewWillEnter()
  {
    if (localStorage.getItem('token'))
      this.navCtrl.push(TabsPage);
  }

  showError()
  {
    let toast = this.toast.create({
      message: 'E-mail o Contraseña inválidos',
      duration: 3000
    });
    toast.present();
  }

  submit()
  {
		this.startLoading();
    this.service.getUser(this.loginForm.value.email).subscribe(
        rs => {
          this.stopLoading();
          if (!rs)
          {
            this.showError();
            return;
          }
          this.user = rs;
          this.navCtrl.push(TabsPage);
          localStorage.setItem('token', this.user.email);
        },
        er => {
          this.stopLoading();
          this.toast.create({
            message: 'Hubo un error conectándose. Intente mas tarde.',
            duration: 3000
          }).present();
          console.log(er);
        }
        //,() => console.log(this.user)
    );
  }
	startLoading()
	{
    this.loading = this.loadingCtrl.create({
      content: 'Porfavor espere...'
    });
		this.loading.present();
	}
	stopLoading()
	{
		if (this.loading)
			this.loading.dismiss();
	}
}
