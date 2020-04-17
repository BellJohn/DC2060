#!/bin/bash
source ~/.bash_profile

DIR=/opt/build
LOG=${DIR}/buildLog.log
cd $DIR
mkdir -p $DIR/download 
date > ${LOG}
#find $DIR/download/ -delete
mkdir -p $DIR/download

git clone https://github.com/BellJohn/DC2060.git $DIR/download >> ${LOG} 2>&1

# Have we managed the download?
if [[ -e ${DIR}/download/reachout ]]; then
       
        cd ${DIR}/download/reachout
        # Run the database build 
        cat ../database/databaseCreate.sql | sudo  mysql --defaults-extra-file=~/.my.cnf.extra -v >> ${LOG} 2>&1
        if [[ $? -ne 0 ]]; then
            echo "ERROR RUNNING DATABASE BUILD" >> ${LOG}
            exit 3
        fi
        mvn clean install > $DIR/mavenLog.log 2>&1
        cat ${DIR}/mavenLog.log >> ${LOG}
else
        echo "ERROR DOWNLOAD FAILED"
        exit 1
fi

SUCCESS=$( grep -c "BUILD SUCCESS" ${DIR}/mavenLog.log)

if [[ $SUCCESS -eq 1 ]]; then
        #We built successfully so stop tomcat, replace existing war and redeploy
        #Backup the existing war first incase deployment fails
        mkdir -p ${DIR}/backup
        cp -p /opt/tomcat/webapps/ReachOut.war ${DIR}/backup/
  
        sudo systemctl stop tomcat 
        echo "STOPPED TOMCAT" >> ${LOG}
        sudo rm -rf /opt/tomcat/webapps/ReachOut
        sudo rm -f /opt/tomcat/webapps/ReachOut.war
        sudo cp ${DIR}/download/reachout/target/ReachOut.war /opt/tomcat/webapps/
        sudo chmod 650 /opt/tomcat/webapps/ReachOut.war
        sudo chown tomcat:tomcat /opt/tomcat/webapps/ReachOut.war
        echo "REPLACED WAR" >> ${LOG}
        
        #remove existing server output
        DATE=$(date +'%m-%d-%Y')
        cp /opt/tomcat/logs/catalina.out ${DIR}/backup/catalina.out.${DATE}
        sudo rm -f /opt/tomcat/logs/catalina.out
        sudo systemctl start tomcat
        echo "RESTARTED TOMCAT" >> ${LOG}
        sleep 20
        sudo systemctl status tomcat >> ${LOG}
else
        echo "BUILD FAILED" >> ${LOG}
        exit 2
fi

SUCCESS=0;
# WAIT FOR DEPLOYMENT
sleep 60;
SUCCESS=$(grep -c "Deployment of web application archive \[/opt/tomcat/webapps/ReachOut.war\] has finished" /opt/tomcat/logs/catalina.out)

if [[ $SUCCESS -eq 1 ]]; then
        echo "DEPLOYMENT SUCCESSFULL" >> ${LOG}
else
        echo "DEPLOYMENT FAILED, rolling back" >> ${LOG}

        sudo systemctl stop tomcat
        echo "STOPPED TOMCAT FOR REDEPLOYMENT" >> ${LOG}
        sudo rm -rf /opt/tomcat/webapps/ReachOut
        sudo rm -f /opt/tomcat/webapps/ReachOut.war
        sudo cp ${DIR}/backup/ReachOut.war /opt/tomcat/webapps/
        sudo chmod 650 /opt/tomcat/webapps/ReachOut.war
        sudo chown tomcat:tomcat /opt/tomcat/webapps/ReachOut.war
        echo "REPLACED WAR FOR REDEPLOYMENT" >> ${LOG}

        sudo systemctl start tomcat
        echo "RESTARTED TOMCAT FOR REDEPLOYMENT" >> ${LOG}
        sleep 20
        sudo systemctl status tomcat >> ${LOG}
fi