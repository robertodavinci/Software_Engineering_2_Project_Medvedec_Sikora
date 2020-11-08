abstract sig Location{}
sig Country{}
sig City{}
sig Street{}
sig decNum{
	intPart: one Int,
	decPart: one Int
	}{ decPart>0
	}

sig img{}


// mozda samo ici s lokacijom tj. s GPS koordinatima
sig Address extends Location {
	country: one Country,
	city: one City,
	street: one Street,
	number: one Int
	}

sig GPSCoordinates extends Location{
	latitude: one decNum,
	longitude: one decNum
	}	
	{
		latitude.intPart < 90 and latitude.decPart > -90
		longitude.intPart < 180 and longitude.decPart > -180
	}

sig WorkingTime{
	openHours: one Int,
	openMins: one Int,
	closeHours: one Int,
	closeMins: one Int,
}
{
	openHours > 0 and openHours < 24
	closeHours > 0 and closeHours < 24
	openMins >= 0 and openMins < 60
	closeMins >= 0 and closeMins < 60
}


sig Date{
	day: one Int,
	month: one Int,
	year: one Int
}{
	day > 0 and day < 31
	month > 0 and month < 13
	year >0 
}

sig ShoppingWindow{
	beginHours: one Int,
	beginMins: one Int,
	expectedTime: one Int,
	dayOfYear: one Date
}
{
	beginHours >= 0 and beginHours < 24
	beginMins >= 0 and beginMins < 60
	expectedTime > 0 and expectedTime < 120
}


sig Store{
	loc: one Address, // maybe without that
	locGPS: one GPSCoordinates,
	time: one WorkingTime,
	maxBuyers: one Int,
	currentBuyers: one Int,
	lastTicketNumber: one Int	
}
{
	maxBuyers > 0
	currentBuyers >= 0	
}

sig Buyer{
	loc: one Address, // maybe without that
	locGPS: one GPSCoordinates,
	shop: one ShoppingWindow,
	ticketNumber: one Int
}

abstract sig TixState{}
one sig WAITING extends TixState{}
one sig INSTORE extends TixState{}
one sig FINISHED extends TixState{}


sig Ticket{
	ticketNumber: one Int,
	ticketState: 
	store: one Store,
	buyer: one Buyer,
	qrcode: one img
}

sig requestATicket{}


pred show(){}


run show for 3 but exactly 2 Buyer
