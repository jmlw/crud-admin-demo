//tasks.register("copyCLientResources") {
//    dependsOn(":client:build")
//    group = "build"
//    description = "Copy client resources into server"
//    doFirst {
//        copy {
//            from(project(":client").buildDir.absolutePath)
//            into("${project(":server").buildDir}/resources/main/public")
//        }
//    }
//}
//
//task assembleServerAndClient(dependsOn: ['copyClientResources', ':server:shadowJar']) { //<3>
//    group = 'build'
//    description = 'Build combined server & client JAR'
//}
