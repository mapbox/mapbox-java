javadoc:
	# Output is ./libjava/lib/build/docs/javadoc
	cd libjava; ./gradlew clean javadoc

	# Output is ./libandroid/lib/build/docs/javadoc/release
	cd libandroid; ./gradlew clean javadocrelease