FROM java:8

COPY buck-out/gen/src/uk/gov/dwp/personal/details/server/lib__personal-details-server__output/personal-details-server.jar /srv/app/personal-details/lib

# CMD java -cp $lib_path $main_class
CMD java -cp /srv/app/personal-details/lib uk.gov.dwp.personal.details.