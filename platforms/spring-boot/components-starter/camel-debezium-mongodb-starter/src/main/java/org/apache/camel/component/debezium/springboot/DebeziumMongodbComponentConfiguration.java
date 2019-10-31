begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.debezium.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|debezium
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Represents a Debezium MongoDB endpoint which is used to capture changes in  * MongoDB database so that that applications can see those changes and respond  * to them.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.debezium-mongodb"
argument_list|)
DECL|class|DebeziumMongodbComponentConfiguration
specifier|public
class|class
name|DebeziumMongodbComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the debezium-mongodb component.      * This is enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * Allow pre-configured Configurations to be set.      */
DECL|field|configuration
specifier|private
name|MongoDbConnectorEmbeddedDebeziumConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|MongoDbConnectorEmbeddedDebeziumConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( MongoDbConnectorEmbeddedDebeziumConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|MongoDbConnectorEmbeddedDebeziumConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|class|MongoDbConnectorEmbeddedDebeziumConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|MongoDbConnectorEmbeddedDebeziumConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|debezium
operator|.
name|configuration
operator|.
name|MongoDbConnectorEmbeddedDebeziumConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * Maximum size of the queue for change events read from the database          * log but not yet recorded or forwarded. Defaults to 8192, and should          * always be larger than the maximum batch size.          */
DECL|field|maxQueueSize
specifier|private
name|Integer
name|maxQueueSize
init|=
literal|8192
decl_stmt|;
comment|/**          * Password to be used when connecting to MongoDB, if necessary.          */
DECL|field|mongodbPassword
specifier|private
name|String
name|mongodbPassword
decl_stmt|;
comment|/**          * Maximum number of threads used to perform an intial sync of the          * collections in a replica set. Defaults to 1.          */
DECL|field|initialSyncMaxThreads
specifier|private
name|Integer
name|initialSyncMaxThreads
init|=
literal|1
decl_stmt|;
comment|/**          * The number of milliseconds to delay before a snapshot will begin.          */
DECL|field|snapshotDelayMs
specifier|private
name|Long
name|snapshotDelayMs
init|=
literal|0L
decl_stmt|;
comment|/**          * Description is not available here, please check Debezium website for          * corresponding key 'collection.blacklist' description.          */
DECL|field|collectionBlacklist
specifier|private
name|String
name|collectionBlacklist
decl_stmt|;
comment|/**          * The collections for which changes are to be captured          */
DECL|field|collectionWhitelist
specifier|private
name|String
name|collectionWhitelist
decl_stmt|;
comment|/**          * Should connector use SSL to connect to MongoDB instances          */
DECL|field|mongodbSslEnabled
specifier|private
name|Boolean
name|mongodbSslEnabled
init|=
literal|false
decl_stmt|;
comment|/**          * Whether delete operations should be represented by a delete event and          * a subsquenttombstone event (true) or only by a delete event (false).          * Emitting the tombstone event (the default behavior) allows Kafka to          * completely delete all events pertaining to the given key once the          * source record got deleted.          */
DECL|field|tombstonesOnDelete
specifier|private
name|Boolean
name|tombstonesOnDelete
init|=
literal|false
decl_stmt|;
comment|/**          * Specifies whether the addresses in 'hosts' are seeds that should be          * used to discover all members of the cluster or replica set ('true'),          * or whether the address(es) in 'hosts' should be used as is ('false').          * The default is 'true'.          */
DECL|field|mongodbMembersAutoDiscover
specifier|private
name|Boolean
name|mongodbMembersAutoDiscover
init|=
literal|true
decl_stmt|;
comment|/**          * Description is not available here, please check Debezium website for          * corresponding key 'field.renames' description.          */
DECL|field|fieldRenames
specifier|private
name|String
name|fieldRenames
decl_stmt|;
comment|/**          * Frequency in milliseconds to wait for new change events to appear          * after receiving no events. Defaults to 500ms.          */
DECL|field|pollIntervalMs
specifier|private
name|Long
name|pollIntervalMs
init|=
literal|500L
decl_stmt|;
comment|/**          * The databases for which changes are to be captured          */
DECL|field|databaseWhitelist
specifier|private
name|String
name|databaseWhitelist
decl_stmt|;
comment|/**          * The hostname and port pairs (in the form 'host' or 'host:port') of          * the MongoDB server(s) in the replica set.          */
DECL|field|mongodbHosts
specifier|private
name|String
name|mongodbHosts
decl_stmt|;
comment|/**          * The initial delay when trying to reconnect to a primary after a          * connection cannot be made or when no primary is available. Defaults          * to 1 second (1000 ms).          */
DECL|field|connectBackoffInitialDelayMs
specifier|private
name|Long
name|connectBackoffInitialDelayMs
init|=
literal|1000L
decl_stmt|;
comment|/**          * The maximum number of records that should be loaded into memory while          * performing a snapshot          */
DECL|field|snapshotFetchSize
specifier|private
name|Integer
name|snapshotFetchSize
decl_stmt|;
comment|/**          * The path to the file that will be used to record the database history          */
DECL|field|databaseHistoryFileFilename
specifier|private
name|String
name|databaseHistoryFileFilename
decl_stmt|;
comment|/**          * Maximum number of failed connection attempts to a replica set primary          * before an exception occurs and task is aborted. Defaults to 16, which          * with the defaults for 'connect.backoff.initial.delay.ms' and          * 'connect.backoff.max.delay.ms' results in just over 20 minutes of          * attempts before failing.          */
DECL|field|connectMaxAttempts
specifier|private
name|Integer
name|connectMaxAttempts
init|=
literal|16
decl_stmt|;
comment|/**          * Database user for connecting to MongoDB, if necessary.          */
DECL|field|mongodbUser
specifier|private
name|String
name|mongodbUser
decl_stmt|;
comment|/**          * Description is not available here, please check Debezium website for          * corresponding key 'field.blacklist' description.          */
DECL|field|fieldBlacklist
specifier|private
name|String
name|fieldBlacklist
decl_stmt|;
comment|/**          * Unique name that identifies the MongoDB replica set or cluster and          * all recorded offsets, andthat is used as a prefix for all schemas and          * topics. Each distinct MongoDB installation should have a separate          * namespace and monitored by at most one Debezium connector.          */
DECL|field|mongodbName
specifier|private
name|String
name|mongodbName
decl_stmt|;
comment|/**          * The maximum delay when trying to reconnect to a primary after a          * connection cannot be made or when no primary is available. Defaults          * to 120 second (120,000 ms).          */
DECL|field|connectBackoffMaxDelayMs
specifier|private
name|Long
name|connectBackoffMaxDelayMs
init|=
literal|120000L
decl_stmt|;
comment|/**          * Whether invalid host names are allowed when using SSL. If true the          * connection will not prevent man-in-the-middle attacks          */
DECL|field|mongodbSslInvalidHostnameAllowed
specifier|private
name|Boolean
name|mongodbSslInvalidHostnameAllowed
init|=
literal|false
decl_stmt|;
comment|/**          * The databases for which changes are to be excluded          */
DECL|field|databaseBlacklist
specifier|private
name|String
name|databaseBlacklist
decl_stmt|;
comment|/**          * Maximum size of each batch of source records. Defaults to 2048.          */
DECL|field|maxBatchSize
specifier|private
name|Integer
name|maxBatchSize
init|=
literal|2048
decl_stmt|;
comment|/**          * The criteria for running a snapshot upon startup of the connector.          * Options include: 'initial' (the default) to specify the connector          * should always perform an initial sync when required; 'never' to          * specify the connector should never perform an initial sync          */
DECL|field|snapshotMode
specifier|private
name|String
name|snapshotMode
init|=
literal|"initial"
decl_stmt|;
comment|/**          * The name of the Java class for the connector          */
DECL|field|connectorClass
specifier|private
name|Class
name|connectorClass
decl_stmt|;
comment|/**          * Unique name for the connector. Attempting to register again with the          * same name will fail.          */
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
comment|/**          * The name of the Java class that is responsible for persistence of          * connector offsets.          */
DECL|field|offsetStorage
specifier|private
name|String
name|offsetStorage
init|=
literal|"org.apache.kafka.connect.storage.FileOffsetBackingStore"
decl_stmt|;
comment|/**          * Path to file where offsets are to be stored. Required when          * offset.storage is set to the FileOffsetBackingStore          */
DECL|field|offsetStorageFileName
specifier|private
name|String
name|offsetStorageFileName
decl_stmt|;
comment|/**          * The name of the Kafka topic where offsets are to be stored. Required          * when offset.storage is set to the KafkaOffsetBackingStore.          */
DECL|field|offsetStorageTopic
specifier|private
name|String
name|offsetStorageTopic
decl_stmt|;
comment|/**          * Replication factor used when creating the offset storage topic.          * Required when offset.storage is set to the KafkaOffsetBackingStore          */
DECL|field|offsetStorageReplicationFactor
specifier|private
name|Integer
name|offsetStorageReplicationFactor
decl_stmt|;
comment|/**          * The name of the Java class of the commit policy. It defines when          * offsets commit has to be triggered based on the number of events          * processed and the time elapsed since the last commit. This class must          * implement the interface 'OffsetCommitPolicy'. The default is a          * periodic commit policy based upon time intervals.          */
DECL|field|offsetCommitPolicy
specifier|private
name|String
name|offsetCommitPolicy
init|=
literal|"io.debezium.embedded.spi.OffsetCommitPolicy.PeriodicCommitOffsetPolicy"
decl_stmt|;
comment|/**          * Interval at which to try committing offsets. The default is 1 minute.          */
DECL|field|offsetFlushIntervalMs
specifier|private
name|Long
name|offsetFlushIntervalMs
init|=
literal|60000L
decl_stmt|;
comment|/**          * Maximum number of milliseconds to wait for records to flush and          * partition offset data to be committed to offset storage before          * cancelling the process and restoring the offset data to be committed          * in a future attempt. The default is 5 seconds.          */
DECL|field|offsetCommitTimeoutMs
specifier|private
name|Long
name|offsetCommitTimeoutMs
init|=
literal|5000L
decl_stmt|;
comment|/**          * The number of partitions used when creating the offset storage topic.          * Required when offset.storage is set to the 'KafkaOffsetBackingStore'.          */
DECL|field|offsetStoragePartitions
specifier|private
name|Integer
name|offsetStoragePartitions
decl_stmt|;
comment|/**          * The Converter class that should be used to serialize and deserialize          * key data for offsets. The default is JSON converter.          */
DECL|field|internalKeyConverter
specifier|private
name|String
name|internalKeyConverter
init|=
literal|"org.apache.kafka.connect.json.JsonConverter"
decl_stmt|;
comment|/**          * The Converter class that should be used to serialize and deserialize          * value data for offsets. The default is JSON converter.          */
DECL|field|internalValueConverter
specifier|private
name|String
name|internalValueConverter
init|=
literal|"org.apache.kafka.connect.json.JsonConverter"
decl_stmt|;
DECL|method|getMaxQueueSize ()
specifier|public
name|Integer
name|getMaxQueueSize
parameter_list|()
block|{
return|return
name|maxQueueSize
return|;
block|}
DECL|method|setMaxQueueSize (Integer maxQueueSize)
specifier|public
name|void
name|setMaxQueueSize
parameter_list|(
name|Integer
name|maxQueueSize
parameter_list|)
block|{
name|this
operator|.
name|maxQueueSize
operator|=
name|maxQueueSize
expr_stmt|;
block|}
DECL|method|getMongodbPassword ()
specifier|public
name|String
name|getMongodbPassword
parameter_list|()
block|{
return|return
name|mongodbPassword
return|;
block|}
DECL|method|setMongodbPassword (String mongodbPassword)
specifier|public
name|void
name|setMongodbPassword
parameter_list|(
name|String
name|mongodbPassword
parameter_list|)
block|{
name|this
operator|.
name|mongodbPassword
operator|=
name|mongodbPassword
expr_stmt|;
block|}
DECL|method|getInitialSyncMaxThreads ()
specifier|public
name|Integer
name|getInitialSyncMaxThreads
parameter_list|()
block|{
return|return
name|initialSyncMaxThreads
return|;
block|}
DECL|method|setInitialSyncMaxThreads (Integer initialSyncMaxThreads)
specifier|public
name|void
name|setInitialSyncMaxThreads
parameter_list|(
name|Integer
name|initialSyncMaxThreads
parameter_list|)
block|{
name|this
operator|.
name|initialSyncMaxThreads
operator|=
name|initialSyncMaxThreads
expr_stmt|;
block|}
DECL|method|getSnapshotDelayMs ()
specifier|public
name|Long
name|getSnapshotDelayMs
parameter_list|()
block|{
return|return
name|snapshotDelayMs
return|;
block|}
DECL|method|setSnapshotDelayMs (Long snapshotDelayMs)
specifier|public
name|void
name|setSnapshotDelayMs
parameter_list|(
name|Long
name|snapshotDelayMs
parameter_list|)
block|{
name|this
operator|.
name|snapshotDelayMs
operator|=
name|snapshotDelayMs
expr_stmt|;
block|}
DECL|method|getCollectionBlacklist ()
specifier|public
name|String
name|getCollectionBlacklist
parameter_list|()
block|{
return|return
name|collectionBlacklist
return|;
block|}
DECL|method|setCollectionBlacklist (String collectionBlacklist)
specifier|public
name|void
name|setCollectionBlacklist
parameter_list|(
name|String
name|collectionBlacklist
parameter_list|)
block|{
name|this
operator|.
name|collectionBlacklist
operator|=
name|collectionBlacklist
expr_stmt|;
block|}
DECL|method|getCollectionWhitelist ()
specifier|public
name|String
name|getCollectionWhitelist
parameter_list|()
block|{
return|return
name|collectionWhitelist
return|;
block|}
DECL|method|setCollectionWhitelist (String collectionWhitelist)
specifier|public
name|void
name|setCollectionWhitelist
parameter_list|(
name|String
name|collectionWhitelist
parameter_list|)
block|{
name|this
operator|.
name|collectionWhitelist
operator|=
name|collectionWhitelist
expr_stmt|;
block|}
DECL|method|getMongodbSslEnabled ()
specifier|public
name|Boolean
name|getMongodbSslEnabled
parameter_list|()
block|{
return|return
name|mongodbSslEnabled
return|;
block|}
DECL|method|setMongodbSslEnabled (Boolean mongodbSslEnabled)
specifier|public
name|void
name|setMongodbSslEnabled
parameter_list|(
name|Boolean
name|mongodbSslEnabled
parameter_list|)
block|{
name|this
operator|.
name|mongodbSslEnabled
operator|=
name|mongodbSslEnabled
expr_stmt|;
block|}
DECL|method|getTombstonesOnDelete ()
specifier|public
name|Boolean
name|getTombstonesOnDelete
parameter_list|()
block|{
return|return
name|tombstonesOnDelete
return|;
block|}
DECL|method|setTombstonesOnDelete (Boolean tombstonesOnDelete)
specifier|public
name|void
name|setTombstonesOnDelete
parameter_list|(
name|Boolean
name|tombstonesOnDelete
parameter_list|)
block|{
name|this
operator|.
name|tombstonesOnDelete
operator|=
name|tombstonesOnDelete
expr_stmt|;
block|}
DECL|method|getMongodbMembersAutoDiscover ()
specifier|public
name|Boolean
name|getMongodbMembersAutoDiscover
parameter_list|()
block|{
return|return
name|mongodbMembersAutoDiscover
return|;
block|}
DECL|method|setMongodbMembersAutoDiscover ( Boolean mongodbMembersAutoDiscover)
specifier|public
name|void
name|setMongodbMembersAutoDiscover
parameter_list|(
name|Boolean
name|mongodbMembersAutoDiscover
parameter_list|)
block|{
name|this
operator|.
name|mongodbMembersAutoDiscover
operator|=
name|mongodbMembersAutoDiscover
expr_stmt|;
block|}
DECL|method|getFieldRenames ()
specifier|public
name|String
name|getFieldRenames
parameter_list|()
block|{
return|return
name|fieldRenames
return|;
block|}
DECL|method|setFieldRenames (String fieldRenames)
specifier|public
name|void
name|setFieldRenames
parameter_list|(
name|String
name|fieldRenames
parameter_list|)
block|{
name|this
operator|.
name|fieldRenames
operator|=
name|fieldRenames
expr_stmt|;
block|}
DECL|method|getPollIntervalMs ()
specifier|public
name|Long
name|getPollIntervalMs
parameter_list|()
block|{
return|return
name|pollIntervalMs
return|;
block|}
DECL|method|setPollIntervalMs (Long pollIntervalMs)
specifier|public
name|void
name|setPollIntervalMs
parameter_list|(
name|Long
name|pollIntervalMs
parameter_list|)
block|{
name|this
operator|.
name|pollIntervalMs
operator|=
name|pollIntervalMs
expr_stmt|;
block|}
DECL|method|getDatabaseWhitelist ()
specifier|public
name|String
name|getDatabaseWhitelist
parameter_list|()
block|{
return|return
name|databaseWhitelist
return|;
block|}
DECL|method|setDatabaseWhitelist (String databaseWhitelist)
specifier|public
name|void
name|setDatabaseWhitelist
parameter_list|(
name|String
name|databaseWhitelist
parameter_list|)
block|{
name|this
operator|.
name|databaseWhitelist
operator|=
name|databaseWhitelist
expr_stmt|;
block|}
DECL|method|getMongodbHosts ()
specifier|public
name|String
name|getMongodbHosts
parameter_list|()
block|{
return|return
name|mongodbHosts
return|;
block|}
DECL|method|setMongodbHosts (String mongodbHosts)
specifier|public
name|void
name|setMongodbHosts
parameter_list|(
name|String
name|mongodbHosts
parameter_list|)
block|{
name|this
operator|.
name|mongodbHosts
operator|=
name|mongodbHosts
expr_stmt|;
block|}
DECL|method|getConnectBackoffInitialDelayMs ()
specifier|public
name|Long
name|getConnectBackoffInitialDelayMs
parameter_list|()
block|{
return|return
name|connectBackoffInitialDelayMs
return|;
block|}
DECL|method|setConnectBackoffInitialDelayMs ( Long connectBackoffInitialDelayMs)
specifier|public
name|void
name|setConnectBackoffInitialDelayMs
parameter_list|(
name|Long
name|connectBackoffInitialDelayMs
parameter_list|)
block|{
name|this
operator|.
name|connectBackoffInitialDelayMs
operator|=
name|connectBackoffInitialDelayMs
expr_stmt|;
block|}
DECL|method|getSnapshotFetchSize ()
specifier|public
name|Integer
name|getSnapshotFetchSize
parameter_list|()
block|{
return|return
name|snapshotFetchSize
return|;
block|}
DECL|method|setSnapshotFetchSize (Integer snapshotFetchSize)
specifier|public
name|void
name|setSnapshotFetchSize
parameter_list|(
name|Integer
name|snapshotFetchSize
parameter_list|)
block|{
name|this
operator|.
name|snapshotFetchSize
operator|=
name|snapshotFetchSize
expr_stmt|;
block|}
DECL|method|getDatabaseHistoryFileFilename ()
specifier|public
name|String
name|getDatabaseHistoryFileFilename
parameter_list|()
block|{
return|return
name|databaseHistoryFileFilename
return|;
block|}
DECL|method|setDatabaseHistoryFileFilename ( String databaseHistoryFileFilename)
specifier|public
name|void
name|setDatabaseHistoryFileFilename
parameter_list|(
name|String
name|databaseHistoryFileFilename
parameter_list|)
block|{
name|this
operator|.
name|databaseHistoryFileFilename
operator|=
name|databaseHistoryFileFilename
expr_stmt|;
block|}
DECL|method|getConnectMaxAttempts ()
specifier|public
name|Integer
name|getConnectMaxAttempts
parameter_list|()
block|{
return|return
name|connectMaxAttempts
return|;
block|}
DECL|method|setConnectMaxAttempts (Integer connectMaxAttempts)
specifier|public
name|void
name|setConnectMaxAttempts
parameter_list|(
name|Integer
name|connectMaxAttempts
parameter_list|)
block|{
name|this
operator|.
name|connectMaxAttempts
operator|=
name|connectMaxAttempts
expr_stmt|;
block|}
DECL|method|getMongodbUser ()
specifier|public
name|String
name|getMongodbUser
parameter_list|()
block|{
return|return
name|mongodbUser
return|;
block|}
DECL|method|setMongodbUser (String mongodbUser)
specifier|public
name|void
name|setMongodbUser
parameter_list|(
name|String
name|mongodbUser
parameter_list|)
block|{
name|this
operator|.
name|mongodbUser
operator|=
name|mongodbUser
expr_stmt|;
block|}
DECL|method|getFieldBlacklist ()
specifier|public
name|String
name|getFieldBlacklist
parameter_list|()
block|{
return|return
name|fieldBlacklist
return|;
block|}
DECL|method|setFieldBlacklist (String fieldBlacklist)
specifier|public
name|void
name|setFieldBlacklist
parameter_list|(
name|String
name|fieldBlacklist
parameter_list|)
block|{
name|this
operator|.
name|fieldBlacklist
operator|=
name|fieldBlacklist
expr_stmt|;
block|}
DECL|method|getMongodbName ()
specifier|public
name|String
name|getMongodbName
parameter_list|()
block|{
return|return
name|mongodbName
return|;
block|}
DECL|method|setMongodbName (String mongodbName)
specifier|public
name|void
name|setMongodbName
parameter_list|(
name|String
name|mongodbName
parameter_list|)
block|{
name|this
operator|.
name|mongodbName
operator|=
name|mongodbName
expr_stmt|;
block|}
DECL|method|getConnectBackoffMaxDelayMs ()
specifier|public
name|Long
name|getConnectBackoffMaxDelayMs
parameter_list|()
block|{
return|return
name|connectBackoffMaxDelayMs
return|;
block|}
DECL|method|setConnectBackoffMaxDelayMs (Long connectBackoffMaxDelayMs)
specifier|public
name|void
name|setConnectBackoffMaxDelayMs
parameter_list|(
name|Long
name|connectBackoffMaxDelayMs
parameter_list|)
block|{
name|this
operator|.
name|connectBackoffMaxDelayMs
operator|=
name|connectBackoffMaxDelayMs
expr_stmt|;
block|}
DECL|method|getMongodbSslInvalidHostnameAllowed ()
specifier|public
name|Boolean
name|getMongodbSslInvalidHostnameAllowed
parameter_list|()
block|{
return|return
name|mongodbSslInvalidHostnameAllowed
return|;
block|}
DECL|method|setMongodbSslInvalidHostnameAllowed ( Boolean mongodbSslInvalidHostnameAllowed)
specifier|public
name|void
name|setMongodbSslInvalidHostnameAllowed
parameter_list|(
name|Boolean
name|mongodbSslInvalidHostnameAllowed
parameter_list|)
block|{
name|this
operator|.
name|mongodbSslInvalidHostnameAllowed
operator|=
name|mongodbSslInvalidHostnameAllowed
expr_stmt|;
block|}
DECL|method|getDatabaseBlacklist ()
specifier|public
name|String
name|getDatabaseBlacklist
parameter_list|()
block|{
return|return
name|databaseBlacklist
return|;
block|}
DECL|method|setDatabaseBlacklist (String databaseBlacklist)
specifier|public
name|void
name|setDatabaseBlacklist
parameter_list|(
name|String
name|databaseBlacklist
parameter_list|)
block|{
name|this
operator|.
name|databaseBlacklist
operator|=
name|databaseBlacklist
expr_stmt|;
block|}
DECL|method|getMaxBatchSize ()
specifier|public
name|Integer
name|getMaxBatchSize
parameter_list|()
block|{
return|return
name|maxBatchSize
return|;
block|}
DECL|method|setMaxBatchSize (Integer maxBatchSize)
specifier|public
name|void
name|setMaxBatchSize
parameter_list|(
name|Integer
name|maxBatchSize
parameter_list|)
block|{
name|this
operator|.
name|maxBatchSize
operator|=
name|maxBatchSize
expr_stmt|;
block|}
DECL|method|getSnapshotMode ()
specifier|public
name|String
name|getSnapshotMode
parameter_list|()
block|{
return|return
name|snapshotMode
return|;
block|}
DECL|method|setSnapshotMode (String snapshotMode)
specifier|public
name|void
name|setSnapshotMode
parameter_list|(
name|String
name|snapshotMode
parameter_list|)
block|{
name|this
operator|.
name|snapshotMode
operator|=
name|snapshotMode
expr_stmt|;
block|}
DECL|method|getConnectorClass ()
specifier|public
name|Class
name|getConnectorClass
parameter_list|()
block|{
return|return
name|connectorClass
return|;
block|}
DECL|method|setConnectorClass (Class connectorClass)
specifier|public
name|void
name|setConnectorClass
parameter_list|(
name|Class
name|connectorClass
parameter_list|)
block|{
name|this
operator|.
name|connectorClass
operator|=
name|connectorClass
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getOffsetStorage ()
specifier|public
name|String
name|getOffsetStorage
parameter_list|()
block|{
return|return
name|offsetStorage
return|;
block|}
DECL|method|setOffsetStorage (String offsetStorage)
specifier|public
name|void
name|setOffsetStorage
parameter_list|(
name|String
name|offsetStorage
parameter_list|)
block|{
name|this
operator|.
name|offsetStorage
operator|=
name|offsetStorage
expr_stmt|;
block|}
DECL|method|getOffsetStorageFileName ()
specifier|public
name|String
name|getOffsetStorageFileName
parameter_list|()
block|{
return|return
name|offsetStorageFileName
return|;
block|}
DECL|method|setOffsetStorageFileName (String offsetStorageFileName)
specifier|public
name|void
name|setOffsetStorageFileName
parameter_list|(
name|String
name|offsetStorageFileName
parameter_list|)
block|{
name|this
operator|.
name|offsetStorageFileName
operator|=
name|offsetStorageFileName
expr_stmt|;
block|}
DECL|method|getOffsetStorageTopic ()
specifier|public
name|String
name|getOffsetStorageTopic
parameter_list|()
block|{
return|return
name|offsetStorageTopic
return|;
block|}
DECL|method|setOffsetStorageTopic (String offsetStorageTopic)
specifier|public
name|void
name|setOffsetStorageTopic
parameter_list|(
name|String
name|offsetStorageTopic
parameter_list|)
block|{
name|this
operator|.
name|offsetStorageTopic
operator|=
name|offsetStorageTopic
expr_stmt|;
block|}
DECL|method|getOffsetStorageReplicationFactor ()
specifier|public
name|Integer
name|getOffsetStorageReplicationFactor
parameter_list|()
block|{
return|return
name|offsetStorageReplicationFactor
return|;
block|}
DECL|method|setOffsetStorageReplicationFactor ( Integer offsetStorageReplicationFactor)
specifier|public
name|void
name|setOffsetStorageReplicationFactor
parameter_list|(
name|Integer
name|offsetStorageReplicationFactor
parameter_list|)
block|{
name|this
operator|.
name|offsetStorageReplicationFactor
operator|=
name|offsetStorageReplicationFactor
expr_stmt|;
block|}
DECL|method|getOffsetCommitPolicy ()
specifier|public
name|String
name|getOffsetCommitPolicy
parameter_list|()
block|{
return|return
name|offsetCommitPolicy
return|;
block|}
DECL|method|setOffsetCommitPolicy (String offsetCommitPolicy)
specifier|public
name|void
name|setOffsetCommitPolicy
parameter_list|(
name|String
name|offsetCommitPolicy
parameter_list|)
block|{
name|this
operator|.
name|offsetCommitPolicy
operator|=
name|offsetCommitPolicy
expr_stmt|;
block|}
DECL|method|getOffsetFlushIntervalMs ()
specifier|public
name|Long
name|getOffsetFlushIntervalMs
parameter_list|()
block|{
return|return
name|offsetFlushIntervalMs
return|;
block|}
DECL|method|setOffsetFlushIntervalMs (Long offsetFlushIntervalMs)
specifier|public
name|void
name|setOffsetFlushIntervalMs
parameter_list|(
name|Long
name|offsetFlushIntervalMs
parameter_list|)
block|{
name|this
operator|.
name|offsetFlushIntervalMs
operator|=
name|offsetFlushIntervalMs
expr_stmt|;
block|}
DECL|method|getOffsetCommitTimeoutMs ()
specifier|public
name|Long
name|getOffsetCommitTimeoutMs
parameter_list|()
block|{
return|return
name|offsetCommitTimeoutMs
return|;
block|}
DECL|method|setOffsetCommitTimeoutMs (Long offsetCommitTimeoutMs)
specifier|public
name|void
name|setOffsetCommitTimeoutMs
parameter_list|(
name|Long
name|offsetCommitTimeoutMs
parameter_list|)
block|{
name|this
operator|.
name|offsetCommitTimeoutMs
operator|=
name|offsetCommitTimeoutMs
expr_stmt|;
block|}
DECL|method|getOffsetStoragePartitions ()
specifier|public
name|Integer
name|getOffsetStoragePartitions
parameter_list|()
block|{
return|return
name|offsetStoragePartitions
return|;
block|}
DECL|method|setOffsetStoragePartitions (Integer offsetStoragePartitions)
specifier|public
name|void
name|setOffsetStoragePartitions
parameter_list|(
name|Integer
name|offsetStoragePartitions
parameter_list|)
block|{
name|this
operator|.
name|offsetStoragePartitions
operator|=
name|offsetStoragePartitions
expr_stmt|;
block|}
DECL|method|getInternalKeyConverter ()
specifier|public
name|String
name|getInternalKeyConverter
parameter_list|()
block|{
return|return
name|internalKeyConverter
return|;
block|}
DECL|method|setInternalKeyConverter (String internalKeyConverter)
specifier|public
name|void
name|setInternalKeyConverter
parameter_list|(
name|String
name|internalKeyConverter
parameter_list|)
block|{
name|this
operator|.
name|internalKeyConverter
operator|=
name|internalKeyConverter
expr_stmt|;
block|}
DECL|method|getInternalValueConverter ()
specifier|public
name|String
name|getInternalValueConverter
parameter_list|()
block|{
return|return
name|internalValueConverter
return|;
block|}
DECL|method|setInternalValueConverter (String internalValueConverter)
specifier|public
name|void
name|setInternalValueConverter
parameter_list|(
name|String
name|internalValueConverter
parameter_list|)
block|{
name|this
operator|.
name|internalValueConverter
operator|=
name|internalValueConverter
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

