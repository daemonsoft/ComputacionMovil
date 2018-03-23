import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { LoadingController } from 'ionic-angular';
import { ToastController } from 'ionic-angular';
import { App } from 'ionic-angular';

import { EventService } from '../../services/event.service';
import { Event } from '../../services/event';
import { EventDetailPage } from './event-detail';

@Component({
    selector: 'page-event',
    templateUrl: 'event.html'
})

export class EventPage
{
    list: Event[];
	private loading;

    constructor(
        public navCtrl: NavController,
        private service: EventService,
        public loadingCtrl: LoadingController,
        public toast: ToastController,
        public app: App)
    {
        this.readEvent();
    }

    ionViewDidLoad()
    {
        this.app.getRootNav().remove(0);
    }
    onClick(event)
    {
        this.navCtrl.push(EventDetailPage, {event: event});
    }
    readEvent()
    {
		this.startLoading();
        this.service.getEvents().subscribe(
            rs => {
                this.list = rs;
                this.stopLoading();
            },
            er => {
                this.stopLoading();
                this.toast.create({
                    message: 'Hubo un error conectándose. Intente mas tarde.',
                    duration: 3000
                }).present();
                console.log(er);
            }
        )
    }
	startLoading()
	{
        this.loading = this.loadingCtrl.create({
            content: 'Cargando Eventos...'
        });
		this.loading.present();
	}
	stopLoading()
	{
		if (this.loading)
			this.loading.dismiss();
	}
    getPictureUrl(url: string)
    {
		return Event.pictureUrl + url;
    }
	getResizedPicture(url: string, w?:number, h?:number)
	{
		return Event.googleResizeServiceUrl
		+ '&url='+this.getPictureUrl(url)
		+ '&w='+w
		+ '&h='+h
	}
}