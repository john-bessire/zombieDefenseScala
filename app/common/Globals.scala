package common

object Globals {
  
	object livingStatus {
		def human():String = {"human"}
		def zombie():String = {"zombie"}
		def unknown(): String = {"unknown"} // Badly injured or missing
		def random(): String = {"Rondom"} // Used when generating random users
	}
	  

}