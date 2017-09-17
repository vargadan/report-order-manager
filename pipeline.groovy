node('maven') {
   // define commands
   def mvnCmd = "mvn -s configuration/maven-cicd-settings.xml"

   stage 'Build'
   git branch: 'master', url: 'https://github.com/vargadan/report-order-manager.git'
   def v = version()
   sh "${mvnCmd} clean install -DskipTests=true"

   stage 'Test and Analysis'
   parallel (
       'Test': {
           sh "${mvnCmd} test"
           step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
       },
       'Static Analysis': {
           sh "${mvnCmd} org.jacoco:jacoco-maven-plugin:report sonar:sonar -Dsonar.host.url=http://sonarqube:9000 -DskipTests=true"
       }
   )

   stage 'Push to Nexus'
   sh "${mvnCmd} deploy -DskipTests=true"

   stage 'Deploy DEV'
   sh "rm -rf oc-build && mkdir -p oc-build/deployments"
   sh "cp target/order-manager.war oc-build/deployments/ROOT.war"
   // clean up. keep the image stream
   sh "oc project ${DEV_PROJECT}"
   sh "oc delete bc,dc,svc,route -l app=order-manager -n ${DEV_PROJECT}"
   // create build. override the exit code since it complains about exising imagestream
   sh "oc new-build --name=order-manager --image-stream=jboss-eap70-openshift --binary=true --labels=app=order-manager -n ${DEV_PROJECT} || true"
   // build image
   sh "oc start-build order-manager --from-dir=oc-build --wait=true -n ${DEV_PROJECT}"
   // deploy image
   sh "oc new-app order-manager:latest -n ${DEV_PROJECT}"
   sh "oc expose svc/order-manager -n ${DEV_PROJECT}"

   stage 'Deploy STAGE'
   input message: "Promote to STAGE?", ok: "Promote"
   sh "oc project ${IT_PROJECT}"
   // tag for stage
   sh "oc tag ${DEV_PROJECT}/order-manager:latest ${IT_PROJECT}/order-manager:${v}"
   // clean up. keep the imagestream
   sh "oc delete bc,dc,svc,route -l app=order-manager -n ${IT_PROJECT}"
   // deploy stage image
   sh "oc new-app order-manager:${v} -n ${IT_PROJECT}"
   sh "oc expose svc/tasorder-managerks -n ${IT_PROJECT}"
}

def version() {
  def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
  matcher ? matcher[0][1] : null
}