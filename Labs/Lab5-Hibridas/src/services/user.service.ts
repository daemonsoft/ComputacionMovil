import { Injectable }	from '@angular/core';
import { Http, Response, Headers, RequestOptions }	from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { User } from './user';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/first';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

@Injectable()
export class UserService
{
	private options: RequestOptions;
	private url: string = 'https://api-mobileevents.rhcloud.com/rest/users';

	constructor(private http:Http)
	{
		let headers = new Headers({'Content-Type': 'application/json'});
		this.options = new RequestOptions({headers: headers});
	}
	getUser(email: string): Observable<User>
	{
		let url = `${this.url}/${email}`;
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
