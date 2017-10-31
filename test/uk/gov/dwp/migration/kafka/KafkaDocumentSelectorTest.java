package uk.gov.dwp.migration.kafka;

public class KafkaDocumentSelectorTest {

    // Receive a message on the topic
    // Insert - Insert, if the record does not exist
    // Update - Update, if the lastModifiedDateTime of the kafka record is >  lastModifiedDateTime of the mongo record
    // Delete - Update/Insert

}
