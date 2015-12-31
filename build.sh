hg pull
hg update
mvn install
cp /home/project/weindex/weindex.web/target/weindex.web-1.0.0.war /root/jetty/webapps/ROOT.war
/root/jetty/bin/jetty.sh restart
/etc/init.d/apache2 restart
