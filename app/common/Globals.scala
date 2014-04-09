package common

object Globals {
 
	val statusHuman   = "human"
	val statusZombie  = "zombie"
	val statusUnknown = "unknown"  // Badly injured and could change
	val statusMissing = "missing"  // Missing or turned into a zombie
	val statusRandom  = "random"   // Generate random person, zombie, missing, unknow
  
  /*
	object livingStatus {
		def human():String = {"human"}
		def zombie():String = {"zombie"}
		def unknown(): String = {"unknown"} // Badly injured or missing
		def random(): String = {"Rondom"} // Used when generating random users
	}
	*/

}