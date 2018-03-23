import { Injectable }	from '@angular/core';
import { Http, Response, Headers, RequestOptions }	from '@angular/http';
import { Event } from './event';
import { Observable } from 'rxjs/Observable';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/first';
import 'rxjs/add/operator/catch';

@Injectable()
export class EventService
{
	private options: RequestOptions;
	private url: string = 'https://api-mobileevents.rhcloud.com/rest/events';

	constructor(private http:Http)
	{
		let headers = new Headers({ 'Content-Type': 'application/json' });
		this.options = new RequestOptions({headers: headers});
	}

	getEvents(): Observable<Event[]>
	{
		let url = `${this.url}`;
		return this.http.get(url, this.options)
			.map(r => r.json())
			.catch(this.handleError);
	}
	getEvent(id: number): Observable<Event[]>
	{
		let url = `${this.url}/${id}`;
		return this.http.get(url, this.options)
			.first()
			.map(res => res.json())
			.catch(this.handleError);
	}

	private handleError(error: Response | any)
	{
		let errMsg: string;
		if (error instanceof Response)
		{
			let body = error.json() || '';
			let err = body.error || JSON.stringify(body);
			errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
		}
		else
			errMsg = error.message ? error.message : error.toString();
		return Observable.throw(errMsg);
	}
}
