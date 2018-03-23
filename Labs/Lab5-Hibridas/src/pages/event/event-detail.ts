import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { MapPage } from '../map/map';

import { EventService } from '../../services/event.service';
import { Event } from '../../services/event';

@Component({
    selector: 'page-event-detail',
    templateUrl: 'event-detail.html'
})

export class EventDetailPage
{
    private event: Event;

    constructor(public navCtrl: NavController, public navParams: NavParams, private service: EventService)
    {
         this.event = this.navParams.get('event');
    }
    openMap()
    {
        this.navCtrl.push(MapPage, {location: {
            lat: this.event.latitude,
            lng: this.event.longitude
        }});
    }
    cleanText(str: string)
    {
        return str.replace(/\+/g , ' ');
    }
    formatDate(date: string)
    {
        let splitDate:string[];
        if (date)
            splitDate = date.split('/');
        return new Date(+splitDate[1], +splitDate[0], +splitDate[2]);
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
