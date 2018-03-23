export class Event
{
	public id:number;
	public name:string;
	public description:string;
	public picture:string;
	public date:Date;
	public latitude:string;
	public longitude:string;
	public location:string;
	public score:number;
	constructor()
	{
	}
	public getPicture()
	{
		return Event.pictureUrl + this.picture;
	}
	public getResizedPicture(w?:number, h?:number)
	{
		return Event.googleResizeServiceUrl
		+ 'url='+this.getPicture()
		+ '&w='+w
		+ '&h='+h
	}

	static googleResizeServiceUrl = 'https://images1-focus-opensocial.googleusercontent.com/gadgets/proxy?container=focus';
	static pictureUrl = 'https://api-mobileevents.rhcloud.com/rest/photos/';
}