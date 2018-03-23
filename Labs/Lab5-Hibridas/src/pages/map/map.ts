import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { ToastController } from 'ionic-angular';
import { Geolocation } from '@ionic-native/geolocation';

declare var google
declare var navigator

@IonicPage()
@Component({
  selector: 'page-map',
  templateUrl: 'map.html',
})
export class MapPage {

  private map: any = null;
  // private marker: any = null;
  // private icon: string = 'assets/map.png';
  private mapOptions: any;
  private directionsDisplay;
  private directionsService;

  constructor(
    public navCtrl: NavController,
    public navParams: NavParams,
    public toast: ToastController,
    private geolocation: Geolocation)
  {
    let location = this.navParams.get('location') || { lat: 6.2456231, lng: -75.5633184 };
    this.mapOptions = {
        zoom: 15,
        center: location
    };
  }

  ionViewDidLoad()
  {
    if ( this.map == null )
      this.initMap();
  }
  
  initMap()
  {
    this.directionsDisplay = new google.maps.DirectionsRenderer;
    this.directionsService = new google.maps.DirectionsService;
    this.map = new google.maps.Map(document.getElementById('map'), this.mapOptions);

    this.directionsDisplay.setMap(this.map);
    this.getLocation();
    // this.calculateAndDisplayRoute(directionsService, directionsDisplay);

    /*
    this.marker = new google.maps.Marker({
        position: this.mapOptions.center,
        map: this.map,
        icon: this.icon
    });
    */
  }

  calculateAndDisplayRoute(location)
  {
    // DRIVING, WALKING, BICYCLING, TRANSIT
    let selectedMode = 'DRIVING';
    this.directionsService.route(
      {
        origin: location,
        destination: this.mapOptions.center,
        travelMode: google.maps.TravelMode[selectedMode]
      },
      (response, status) =>
      {
        if (status == 'OK')
          this.directionsDisplay.setDirections(response);
        else
          this.toast.create({
            message: 'No se pudo establecer una ruta.',
            duration: 3000
          }).present();
      }
    );
  }

  getLocation()
  {
    if (navigator.geolocation)
      navigator.geolocation.getCurrentPosition((position) => {
        let location = {
          lat: position.coords.latitude,
          lng: position.coords.longitude
        };
        this.calculateAndDisplayRoute(location);
      });
    else
      this.toast.create({
        message: 'No se pudo establecer una ruta.',
        duration: 3000
      }).present();
  }

  getNativeLocation()
  {
    this.geolocation.getCurrentPosition().then((resp) => {
        let location = {
          lat: resp.coords.latitude,
          lng: resp.coords.longitude
        };
        this.calculateAndDisplayRoute(location);
    }).catch((error) => {
      this.toast.create({
        message: 'No se pudo establecer la posición de origen.',
        duration: 3000
      }).present();
    });
  }
}