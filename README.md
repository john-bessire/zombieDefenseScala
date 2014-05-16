Last year a created a repository called ZombieDefence to practice Ruby on Rails. This repository is to help me learn Play Framework with scala. PostgreSQL with geocoding will be used to map the zombie outbreaks. Then I will have a chance to test if direct REST connections from a mobile device are faster than sockets.
 
 Since this project was created for learning there is no goal and the site will evolve over time.
 
###The following REST API calls are currently working.###
 
  /users        - No parameter will list all users
  /users        - Create user by sending correct Json
  /users/:id	- Get a user by ID number
  /usersRadius	- Get all users within 
 
 
###These functions are not ready for use (Some locations are hard coded and will be finished later)###
  /createMap    - Creates a map
  /location     - Get longitude and latitude from an address
  /bearing		- Returns longitude and latitude at distance and bearing from location
  /distance     - Distance between two longitudes and latitudes

### Temporary to try things out###
  /test1         - Whatever is here will keep changing
  /test2
  /test3

###API calls###

####Get all users####

  curl \
	--header "Content-type: application/json" \
	--request GET \
	--data '{}' \
	localhost:9000/users \
	| python -mjson.tool
	
####Get user by ID####

  curl \
	--header "Content-type: application/json" \
	--request GET \
	--data '{}' \
	localhost:9000/users/1 \
	| python -mjson.tool
	
####Create user####

  curl \
	--header "Content-type: application/json" \
	--request POST \
	--data '{"userName" : "Jimmy28991", "email" : "Jimmy28991.com", "password" : "secr2Es3Sts", "livingStatus" : "human", "latitude" : 37.123, "longitude": -127.456}' \
	localhost:9000/users \
	| python -mjson.tool

####Get all users within x meters from latitude and longitude#### 
Living Status is (human, zombie, unknown, missing).  Unknown status is when a person is badly injured and could turn into a zombie or get better.

  curl \
	--header "Content-type: application/json" \
	--request POST \
	--data '{"livingStatus" : "human", "latitude" : 48.432044, "longitude": -71.060316, "radiusMeters": 2000}' \
	localhost:9000/usersRadius \
	| python -mjson.tool
 