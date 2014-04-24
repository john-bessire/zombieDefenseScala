package common

object Globals {
 
	val statusAll     = "all"      // Used to search for all living and undead users
	val statusHuman   = "human"    // If you can read this then you are still human
	val statusZombie  = "zombie"   // Smarter than the average cell phone user
	val statusUnknown = "unknown"  // Badly injured and could change into a zombie
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