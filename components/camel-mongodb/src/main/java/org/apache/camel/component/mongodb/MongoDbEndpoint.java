begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mongodb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mongodb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|BasicDBObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|DB
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|DBCollection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|DBObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|Mongo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|ReadPreference
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|WriteConcern
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|WriteResult
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
name|Consumer
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
name|Exchange
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
name|Message
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
name|Processor
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
name|Producer
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
name|impl
operator|.
name|DefaultEndpoint
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
name|impl
operator|.
name|DefaultExchange
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
name|impl
operator|.
name|DefaultMessage
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Represents a MongoDb endpoint.   * It is responsible for creating {@link MongoDbProducer} and {@link MongoDbTailableCursorConsumer} instances.  * It accepts a number of options to customise the behaviour of consumers and producers.  */
end_comment

begin_class
DECL|class|MongoDbEndpoint
specifier|public
class|class
name|MongoDbEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MongoDbEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|mongoConnection
specifier|private
name|Mongo
name|mongoConnection
decl_stmt|;
DECL|field|database
specifier|private
name|String
name|database
decl_stmt|;
DECL|field|collection
specifier|private
name|String
name|collection
decl_stmt|;
DECL|field|collectionIndex
specifier|private
name|String
name|collectionIndex
decl_stmt|;
DECL|field|operation
specifier|private
name|MongoDbOperation
name|operation
decl_stmt|;
DECL|field|createCollection
specifier|private
name|boolean
name|createCollection
init|=
literal|true
decl_stmt|;
DECL|field|invokeGetLastError
specifier|private
name|boolean
name|invokeGetLastError
decl_stmt|;
comment|// = false
DECL|field|writeConcern
specifier|private
name|WriteConcern
name|writeConcern
decl_stmt|;
DECL|field|writeConcernRef
specifier|private
name|WriteConcern
name|writeConcernRef
decl_stmt|;
DECL|field|readPreference
specifier|private
name|ReadPreference
name|readPreference
decl_stmt|;
DECL|field|dynamicity
specifier|private
name|boolean
name|dynamicity
decl_stmt|;
comment|// = false
DECL|field|writeResultAsHeader
specifier|private
name|boolean
name|writeResultAsHeader
decl_stmt|;
comment|// = false
comment|// tailable cursor consumer by default
DECL|field|consumerType
specifier|private
name|MongoDbConsumerType
name|consumerType
decl_stmt|;
DECL|field|cursorRegenerationDelay
specifier|private
name|long
name|cursorRegenerationDelay
init|=
literal|1000L
decl_stmt|;
DECL|field|tailTrackIncreasingField
specifier|private
name|String
name|tailTrackIncreasingField
decl_stmt|;
comment|// persitent tail tracking
DECL|field|persistentTailTracking
specifier|private
name|boolean
name|persistentTailTracking
decl_stmt|;
comment|// = false;
DECL|field|persistentId
specifier|private
name|String
name|persistentId
decl_stmt|;
DECL|field|tailTrackDb
specifier|private
name|String
name|tailTrackDb
decl_stmt|;
DECL|field|tailTrackCollection
specifier|private
name|String
name|tailTrackCollection
decl_stmt|;
DECL|field|tailTrackField
specifier|private
name|String
name|tailTrackField
decl_stmt|;
DECL|field|tailTrackingConfig
specifier|private
name|MongoDbTailTrackingConfig
name|tailTrackingConfig
decl_stmt|;
DECL|field|dbCollection
specifier|private
name|DBCollection
name|dbCollection
decl_stmt|;
DECL|field|db
specifier|private
name|DB
name|db
decl_stmt|;
comment|// ======= Constructors ===============================================
DECL|method|MongoDbEndpoint ()
specifier|public
name|MongoDbEndpoint
parameter_list|()
block|{     }
DECL|method|MongoDbEndpoint (String uri, MongoDbComponent component)
specifier|public
name|MongoDbEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|MongoDbComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|MongoDbEndpoint (String endpointUri)
specifier|public
name|MongoDbEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
comment|// ======= Implementation methods =====================================
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|validateOptions
argument_list|(
literal|'P'
argument_list|)
expr_stmt|;
name|initializeConnection
argument_list|()
expr_stmt|;
return|return
operator|new
name|MongoDbProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|validateOptions
argument_list|(
literal|'C'
argument_list|)
expr_stmt|;
comment|// we never create the collection
name|createCollection
operator|=
literal|false
expr_stmt|;
name|initializeConnection
argument_list|()
expr_stmt|;
comment|// select right consumer type
if|if
condition|(
name|consumerType
operator|==
literal|null
condition|)
block|{
name|consumerType
operator|=
name|MongoDbConsumerType
operator|.
name|tailable
expr_stmt|;
block|}
name|Consumer
name|consumer
decl_stmt|;
if|if
condition|(
name|consumerType
operator|==
name|MongoDbConsumerType
operator|.
name|tailable
condition|)
block|{
name|consumer
operator|=
operator|new
name|MongoDbTailableCursorConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CamelMongoDbException
argument_list|(
literal|"Consumer type not supported: "
operator|+
name|consumerType
argument_list|)
throw|;
block|}
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|validateOptions (char role)
specifier|private
name|void
name|validateOptions
parameter_list|(
name|char
name|role
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
comment|// make our best effort to validate, options with defaults are checked against their defaults, which is not always a guarantee that
comment|// they haven't been explicitly set, but it is enough
if|if
condition|(
name|role
operator|==
literal|'P'
condition|)
block|{
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|consumerType
argument_list|)
operator|||
name|persistentTailTracking
operator|||
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|tailTrackDb
argument_list|)
operator|||
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|tailTrackCollection
argument_list|)
operator|||
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|tailTrackField
argument_list|)
operator|||
name|cursorRegenerationDelay
operator|!=
literal|1000L
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"consumerType, tailTracking, cursorRegenerationDelay options cannot appear on a producer endpoint"
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|role
operator|==
literal|'C'
condition|)
block|{
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|operation
argument_list|)
operator|||
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|writeConcern
argument_list|)
operator|||
name|writeConcernRef
operator|!=
literal|null
operator|||
name|readPreference
operator|!=
literal|null
operator|||
name|dynamicity
operator|||
name|invokeGetLastError
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"operation, writeConcern, writeConcernRef, readPreference, dynamicity, invokeGetLastError "
operator|+
literal|"options cannot appear on a consumer endpoint"
argument_list|)
throw|;
block|}
if|if
condition|(
name|consumerType
operator|==
name|MongoDbConsumerType
operator|.
name|tailable
condition|)
block|{
if|if
condition|(
name|tailTrackIncreasingField
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"tailTrackIncreasingField option must be set for tailable cursor MongoDB consumer endpoint"
argument_list|)
throw|;
block|}
if|if
condition|(
name|persistentTailTracking
operator|&&
operator|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|persistentId
argument_list|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"persistentId is compulsory for persistent tail tracking"
argument_list|)
throw|;
block|}
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown endpoint role"
argument_list|)
throw|;
block|}
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Initialises the MongoDB connection using the Mongo object provided to the endpoint      *       * @throws CamelMongoDbException      */
DECL|method|initializeConnection ()
specifier|public
name|void
name|initializeConnection
parameter_list|()
throws|throws
name|CamelMongoDbException
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Initialising MongoDb endpoint: {}"
argument_list|,
name|this
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|database
operator|==
literal|null
operator|||
operator|(
name|collection
operator|==
literal|null
operator|&&
operator|!
operator|(
name|MongoDbOperation
operator|.
name|getDbStats
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
operator|||
name|MongoDbOperation
operator|.
name|command
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
operator|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|CamelMongoDbException
argument_list|(
literal|"Missing required endpoint configuration: database and/or collection"
argument_list|)
throw|;
block|}
name|db
operator|=
name|mongoConnection
operator|.
name|getDB
argument_list|(
name|database
argument_list|)
expr_stmt|;
if|if
condition|(
name|db
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelMongoDbException
argument_list|(
literal|"Could not initialise MongoDbComponent. Database "
operator|+
name|database
operator|+
literal|" does not exist."
argument_list|)
throw|;
block|}
if|if
condition|(
name|collection
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|createCollection
operator|&&
operator|!
name|db
operator|.
name|collectionExists
argument_list|(
name|collection
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CamelMongoDbException
argument_list|(
literal|"Could not initialise MongoDbComponent. Collection "
operator|+
name|collection
operator|+
literal|" and createCollection is false."
argument_list|)
throw|;
block|}
name|dbCollection
operator|=
name|db
operator|.
name|getCollection
argument_list|(
name|collection
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"MongoDb component initialised and endpoint bound to MongoDB collection with the following parameters. Address list: {}, Db: {}, Collection: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|mongoConnection
operator|.
name|getAllAddress
argument_list|()
operator|.
name|toString
argument_list|()
block|,
name|db
operator|.
name|getName
argument_list|()
block|,
name|dbCollection
operator|.
name|getName
argument_list|()
block|}
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|collectionIndex
argument_list|)
condition|)
block|{
name|ensureIndex
argument_list|(
name|dbCollection
argument_list|,
name|createIndex
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelMongoDbException
argument_list|(
literal|"Error creating index"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Add Index      *      * @param collection      */
DECL|method|ensureIndex (DBCollection collection, List<DBObject> dynamicIndex)
specifier|public
name|void
name|ensureIndex
parameter_list|(
name|DBCollection
name|collection
parameter_list|,
name|List
argument_list|<
name|DBObject
argument_list|>
name|dynamicIndex
parameter_list|)
block|{
name|collection
operator|.
name|dropIndexes
argument_list|()
expr_stmt|;
if|if
condition|(
name|dynamicIndex
operator|!=
literal|null
operator|&&
operator|!
name|dynamicIndex
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|DBObject
name|index
range|:
name|dynamicIndex
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"create BDObject Index {}"
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|collection
operator|.
name|createIndex
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Create technical list index      *      * @return technical list index      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createIndex ()
specifier|public
name|List
argument_list|<
name|DBObject
argument_list|>
name|createIndex
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|DBObject
argument_list|>
name|indexList
init|=
operator|new
name|ArrayList
argument_list|<
name|DBObject
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|collectionIndex
argument_list|)
condition|)
block|{
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|indexMap
init|=
operator|new
name|ObjectMapper
argument_list|()
operator|.
name|readValue
argument_list|(
name|collectionIndex
argument_list|,
name|HashMap
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|set
range|:
name|indexMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|DBObject
name|index
init|=
operator|new
name|BasicDBObject
argument_list|()
decl_stmt|;
comment|// MongoDB 2.4 upwards is restrictive about the type of the 'single field index' being
comment|// in use below (set.getValue())) as only an integer value type is accepted, otherwise
comment|// server will throw an exception, see more details:
comment|// http://docs.mongodb.org/manual/release-notes/2.4/#improved-validation-of-index-types
name|index
operator|.
name|put
argument_list|(
name|set
operator|.
name|getKey
argument_list|()
argument_list|,
name|set
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|indexList
operator|.
name|add
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|indexList
return|;
block|}
comment|/**      * Applies validation logic specific to this endpoint type. If everything succeeds, continues initialization      */
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|writeConcern
operator|!=
literal|null
operator|&&
name|writeConcernRef
operator|!=
literal|null
condition|)
block|{
name|String
name|msg
init|=
literal|"Cannot set both writeConcern and writeConcernRef at the same time. Respective values: "
operator|+
name|writeConcern
operator|+
literal|", "
operator|+
name|writeConcernRef
operator|+
literal|". Aborting initialization."
decl_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
name|setWriteReadOptionsOnConnection
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
DECL|method|createMongoDbExchange (DBObject dbObj)
specifier|public
name|Exchange
name|createMongoDbExchange
parameter_list|(
name|DBObject
name|dbObj
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|this
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|getExchangePattern
argument_list|()
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|DefaultMessage
argument_list|()
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|MongoDbConstants
operator|.
name|DATABASE
argument_list|,
name|database
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|MongoDbConstants
operator|.
name|COLLECTION
argument_list|,
name|collection
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|MongoDbConstants
operator|.
name|FROM_TAILABLE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|dbObj
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|setWriteReadOptionsOnConnection ()
specifier|private
name|void
name|setWriteReadOptionsOnConnection
parameter_list|()
block|{
comment|// Set the WriteConcern
if|if
condition|(
name|writeConcern
operator|!=
literal|null
condition|)
block|{
name|mongoConnection
operator|.
name|setWriteConcern
argument_list|(
name|writeConcern
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|writeConcernRef
operator|!=
literal|null
condition|)
block|{
name|mongoConnection
operator|.
name|setWriteConcern
argument_list|(
name|writeConcernRef
argument_list|)
expr_stmt|;
block|}
comment|// Set the ReadPreference
if|if
condition|(
name|readPreference
operator|!=
literal|null
condition|)
block|{
name|mongoConnection
operator|.
name|setReadPreference
argument_list|(
name|readPreference
argument_list|)
expr_stmt|;
block|}
block|}
comment|// ======= Getters and setters ===============================================
comment|/**      * Sets the name of the MongoDB collection to bind to this endpoint      *       * @param collection collection name      */
DECL|method|setCollection (String collection)
specifier|public
name|void
name|setCollection
parameter_list|(
name|String
name|collection
parameter_list|)
block|{
name|this
operator|.
name|collection
operator|=
name|collection
expr_stmt|;
block|}
DECL|method|getCollection ()
specifier|public
name|String
name|getCollection
parameter_list|()
block|{
return|return
name|collection
return|;
block|}
comment|/**      * Sets the collection index (JSON FORMAT : { "field1" : order1, "field2" : order2})      */
DECL|method|setCollectionIndex (String collectionIndex)
specifier|public
name|void
name|setCollectionIndex
parameter_list|(
name|String
name|collectionIndex
parameter_list|)
block|{
name|this
operator|.
name|collectionIndex
operator|=
name|collectionIndex
expr_stmt|;
block|}
DECL|method|getCollectionIndex ()
specifier|public
name|String
name|getCollectionIndex
parameter_list|()
block|{
return|return
name|collectionIndex
return|;
block|}
comment|/**      * Sets the operation this endpoint will execute against MongoDB. For possible values, see {@link MongoDbOperation}.      * @param operation name of the operation as per catalogued values      *       * @throws CamelMongoDbException      */
DECL|method|setOperation (String operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
throws|throws
name|CamelMongoDbException
block|{
try|try
block|{
name|this
operator|.
name|operation
operator|=
name|MongoDbOperation
operator|.
name|valueOf
argument_list|(
name|operation
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelMongoDbException
argument_list|(
literal|"Operation not supported"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getOperation ()
specifier|public
name|MongoDbOperation
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * Sets the name of the MongoDB database to target      *       * @param database name of the MongoDB database      */
DECL|method|setDatabase (String database)
specifier|public
name|void
name|setDatabase
parameter_list|(
name|String
name|database
parameter_list|)
block|{
name|this
operator|.
name|database
operator|=
name|database
expr_stmt|;
block|}
DECL|method|getDatabase ()
specifier|public
name|String
name|getDatabase
parameter_list|()
block|{
return|return
name|database
return|;
block|}
comment|/**      * Create collection during initialisation if it doesn't exist. Default is true.      *       * @param createCollection true or false      */
DECL|method|setCreateCollection (boolean createCollection)
specifier|public
name|void
name|setCreateCollection
parameter_list|(
name|boolean
name|createCollection
parameter_list|)
block|{
name|this
operator|.
name|createCollection
operator|=
name|createCollection
expr_stmt|;
block|}
DECL|method|isCreateCollection ()
specifier|public
name|boolean
name|isCreateCollection
parameter_list|()
block|{
return|return
name|createCollection
return|;
block|}
DECL|method|getDb ()
specifier|public
name|DB
name|getDb
parameter_list|()
block|{
return|return
name|db
return|;
block|}
DECL|method|getDbCollection ()
specifier|public
name|DBCollection
name|getDbCollection
parameter_list|()
block|{
return|return
name|dbCollection
return|;
block|}
comment|/**      * Sets the Mongo instance that represents the backing connection      *       * @param mongoConnection the connection to the database      */
DECL|method|setMongoConnection (Mongo mongoConnection)
specifier|public
name|void
name|setMongoConnection
parameter_list|(
name|Mongo
name|mongoConnection
parameter_list|)
block|{
name|this
operator|.
name|mongoConnection
operator|=
name|mongoConnection
expr_stmt|;
block|}
DECL|method|getMongoConnection ()
specifier|public
name|Mongo
name|getMongoConnection
parameter_list|()
block|{
return|return
name|mongoConnection
return|;
block|}
comment|/**      * Set the {@link WriteConcern} for write operations on MongoDB using the standard ones.      * Resolved from the fields of the WriteConcern class by calling the {@link WriteConcern#valueOf(String)} method.      *       * @param writeConcern the standard name of the WriteConcern      * @see<a href="http://api.mongodb.org/java/current/com/mongodb/WriteConcern.html#valueOf(java.lang.String)">possible options</a>      */
DECL|method|setWriteConcern (String writeConcern)
specifier|public
name|void
name|setWriteConcern
parameter_list|(
name|String
name|writeConcern
parameter_list|)
block|{
name|this
operator|.
name|writeConcern
operator|=
name|WriteConcern
operator|.
name|valueOf
argument_list|(
name|writeConcern
argument_list|)
expr_stmt|;
block|}
DECL|method|getWriteConcern ()
specifier|public
name|WriteConcern
name|getWriteConcern
parameter_list|()
block|{
return|return
name|writeConcern
return|;
block|}
comment|/**      * Instructs this endpoint to invoke {@link WriteResult#getLastError()} with every operation. By default, MongoDB does not wait      * for the write operation to occur before returning. If set to true, each exchange will only return after the write operation       * has actually occurred in MongoDB.      *       * @param invokeGetLastError true or false      */
DECL|method|setInvokeGetLastError (boolean invokeGetLastError)
specifier|public
name|void
name|setInvokeGetLastError
parameter_list|(
name|boolean
name|invokeGetLastError
parameter_list|)
block|{
name|this
operator|.
name|invokeGetLastError
operator|=
name|invokeGetLastError
expr_stmt|;
block|}
DECL|method|isInvokeGetLastError ()
specifier|public
name|boolean
name|isInvokeGetLastError
parameter_list|()
block|{
return|return
name|invokeGetLastError
return|;
block|}
comment|/**      * Set the {@link WriteConcern} for write operations on MongoDB, passing in the bean ref to a custom WriteConcern which exists in the Registry.      * You can also use standard WriteConcerns by passing in their key. See the {@link #setWriteConcern(String) setWriteConcern} method.      *       * @param writeConcernRef the name of the bean in the registry that represents the WriteConcern to use      */
DECL|method|setWriteConcernRef (String writeConcernRef)
specifier|public
name|void
name|setWriteConcernRef
parameter_list|(
name|String
name|writeConcernRef
parameter_list|)
block|{
name|WriteConcern
name|wc
init|=
name|this
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|writeConcernRef
argument_list|,
name|WriteConcern
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|wc
operator|==
literal|null
condition|)
block|{
name|String
name|msg
init|=
literal|"Camel MongoDB component could not find the WriteConcern in the Registry. Verify that the "
operator|+
literal|"provided bean name ("
operator|+
name|writeConcernRef
operator|+
literal|")  is correct. Aborting initialization."
decl_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
name|this
operator|.
name|writeConcernRef
operator|=
name|wc
expr_stmt|;
block|}
DECL|method|getWriteConcernRef ()
specifier|public
name|WriteConcern
name|getWriteConcernRef
parameter_list|()
block|{
return|return
name|writeConcernRef
return|;
block|}
comment|/**       * Sets a MongoDB {@link ReadPreference} on the Mongo connection. Read preferences set directly on the connection will be      * overridden by this setting.      *<p/>      * The {@link com.mongodb.ReadPreference#valueOf(String)} utility method is used to resolve the passed {@code readPreference}      * value. Some examples for the possible values are {@code nearest}, {@code primary} or {@code secondary} etc.      *       * @param readPreference the name of the read preference to set      */
DECL|method|setReadPreference (String readPreference)
specifier|public
name|void
name|setReadPreference
parameter_list|(
name|String
name|readPreference
parameter_list|)
block|{
name|this
operator|.
name|readPreference
operator|=
name|ReadPreference
operator|.
name|valueOf
argument_list|(
name|readPreference
argument_list|)
expr_stmt|;
block|}
DECL|method|getReadPreference ()
specifier|public
name|ReadPreference
name|getReadPreference
parameter_list|()
block|{
return|return
name|readPreference
return|;
block|}
comment|/**      * Sets whether this endpoint will attempt to dynamically resolve the target database and collection from the incoming Exchange properties.      * Can be used to override at runtime the database and collection specified on the otherwise static endpoint URI.      * It is disabled by default to boost performance. Enabling it will take a minimal performance hit.      *       * @see MongoDbConstants#DATABASE      * @see MongoDbConstants#COLLECTION      * @param dynamicity true or false indicated whether target database and collection should be calculated dynamically based on Exchange properties.      */
DECL|method|setDynamicity (boolean dynamicity)
specifier|public
name|void
name|setDynamicity
parameter_list|(
name|boolean
name|dynamicity
parameter_list|)
block|{
name|this
operator|.
name|dynamicity
operator|=
name|dynamicity
expr_stmt|;
block|}
DECL|method|isDynamicity ()
specifier|public
name|boolean
name|isDynamicity
parameter_list|()
block|{
return|return
name|dynamicity
return|;
block|}
comment|/**      * Reserved for future use, when more consumer types are supported.      *      * @param consumerType key of the consumer type      * @throws CamelMongoDbException      */
DECL|method|setConsumerType (String consumerType)
specifier|public
name|void
name|setConsumerType
parameter_list|(
name|String
name|consumerType
parameter_list|)
throws|throws
name|CamelMongoDbException
block|{
try|try
block|{
name|this
operator|.
name|consumerType
operator|=
name|MongoDbConsumerType
operator|.
name|valueOf
argument_list|(
name|consumerType
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelMongoDbException
argument_list|(
literal|"Consumer type not supported"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getConsumerType ()
specifier|public
name|MongoDbConsumerType
name|getConsumerType
parameter_list|()
block|{
return|return
name|consumerType
return|;
block|}
DECL|method|getTailTrackDb ()
specifier|public
name|String
name|getTailTrackDb
parameter_list|()
block|{
return|return
name|tailTrackDb
return|;
block|}
comment|/**      * Indicates what database the tail tracking mechanism will persist to. If not specified, the current database will       * be picked by default. Dynamicity will not be taken into account even if enabled, i.e. the tail tracking database       * will not vary past endpoint initialisation.      *       * @param tailTrackDb database name      */
DECL|method|setTailTrackDb (String tailTrackDb)
specifier|public
name|void
name|setTailTrackDb
parameter_list|(
name|String
name|tailTrackDb
parameter_list|)
block|{
name|this
operator|.
name|tailTrackDb
operator|=
name|tailTrackDb
expr_stmt|;
block|}
DECL|method|getTailTrackCollection ()
specifier|public
name|String
name|getTailTrackCollection
parameter_list|()
block|{
return|return
name|tailTrackCollection
return|;
block|}
comment|/**      * Collection where tail tracking information will be persisted. If not specified, {@link MongoDbTailTrackingConfig#DEFAULT_COLLECTION}       * will be used by default.      *       * @param tailTrackCollection collection name      */
DECL|method|setTailTrackCollection (String tailTrackCollection)
specifier|public
name|void
name|setTailTrackCollection
parameter_list|(
name|String
name|tailTrackCollection
parameter_list|)
block|{
name|this
operator|.
name|tailTrackCollection
operator|=
name|tailTrackCollection
expr_stmt|;
block|}
DECL|method|getTailTrackField ()
specifier|public
name|String
name|getTailTrackField
parameter_list|()
block|{
return|return
name|tailTrackField
return|;
block|}
comment|/**      * Field where the last tracked value will be placed. If not specified,  {@link MongoDbTailTrackingConfig#DEFAULT_FIELD}       * will be used by default.      *       * @param tailTrackField field name      */
DECL|method|setTailTrackField (String tailTrackField)
specifier|public
name|void
name|setTailTrackField
parameter_list|(
name|String
name|tailTrackField
parameter_list|)
block|{
name|this
operator|.
name|tailTrackField
operator|=
name|tailTrackField
expr_stmt|;
block|}
comment|/**      * Enable persistent tail tracking, which is a mechanism to keep track of the last consumed message across system restarts.      * The next time the system is up, the endpoint will recover the cursor from the point where it last stopped slurping records.      *       * @param persistentTailTracking true or false      */
DECL|method|setPersistentTailTracking (boolean persistentTailTracking)
specifier|public
name|void
name|setPersistentTailTracking
parameter_list|(
name|boolean
name|persistentTailTracking
parameter_list|)
block|{
name|this
operator|.
name|persistentTailTracking
operator|=
name|persistentTailTracking
expr_stmt|;
block|}
DECL|method|isPersistentTailTracking ()
specifier|public
name|boolean
name|isPersistentTailTracking
parameter_list|()
block|{
return|return
name|persistentTailTracking
return|;
block|}
comment|/**      * Correlation field in the incoming record which is of increasing nature and will be used to position the tailing cursor every       * time it is generated.      * The cursor will be (re)created with a query of type: tailTrackIncreasingField> lastValue (possibly recovered from persistent      * tail tracking).      * Can be of type Integer, Date, String, etc.      * NOTE: No support for dot notation at the current time, so the field should be at the top level of the document.      *       * @param tailTrackIncreasingField      */
DECL|method|setTailTrackIncreasingField (String tailTrackIncreasingField)
specifier|public
name|void
name|setTailTrackIncreasingField
parameter_list|(
name|String
name|tailTrackIncreasingField
parameter_list|)
block|{
name|this
operator|.
name|tailTrackIncreasingField
operator|=
name|tailTrackIncreasingField
expr_stmt|;
block|}
DECL|method|getTailTrackIncreasingField ()
specifier|public
name|String
name|getTailTrackIncreasingField
parameter_list|()
block|{
return|return
name|tailTrackIncreasingField
return|;
block|}
DECL|method|getTailTrackingConfig ()
specifier|public
name|MongoDbTailTrackingConfig
name|getTailTrackingConfig
parameter_list|()
block|{
if|if
condition|(
name|tailTrackingConfig
operator|==
literal|null
condition|)
block|{
name|tailTrackingConfig
operator|=
operator|new
name|MongoDbTailTrackingConfig
argument_list|(
name|persistentTailTracking
argument_list|,
name|tailTrackIncreasingField
argument_list|,
name|tailTrackDb
operator|==
literal|null
condition|?
name|database
else|:
name|tailTrackDb
argument_list|,
name|tailTrackCollection
argument_list|,
name|tailTrackField
argument_list|,
name|getPersistentId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|tailTrackingConfig
return|;
block|}
comment|/**      * MongoDB tailable cursors will block until new data arrives. If no new data is inserted, after some time the cursor will be automatically      * freed and closed by the MongoDB server. The client is expected to regenerate the cursor if needed. This value specifies the time to wait      * before attempting to fetch a new cursor, and if the attempt fails, how long before the next attempt is made. Default value is 1000ms.      *       * @param cursorRegenerationDelay delay specified in milliseconds      */
DECL|method|setCursorRegenerationDelay (long cursorRegenerationDelay)
specifier|public
name|void
name|setCursorRegenerationDelay
parameter_list|(
name|long
name|cursorRegenerationDelay
parameter_list|)
block|{
name|this
operator|.
name|cursorRegenerationDelay
operator|=
name|cursorRegenerationDelay
expr_stmt|;
block|}
DECL|method|getCursorRegenerationDelay ()
specifier|public
name|long
name|getCursorRegenerationDelay
parameter_list|()
block|{
return|return
name|cursorRegenerationDelay
return|;
block|}
comment|/**      * One tail tracking collection can host many trackers for several tailable consumers.       * To keep them separate, each tracker should have its own unique persistentId.      *       * @param persistentId the value of the persistent ID to use for this tailable consumer      */
DECL|method|setPersistentId (String persistentId)
specifier|public
name|void
name|setPersistentId
parameter_list|(
name|String
name|persistentId
parameter_list|)
block|{
name|this
operator|.
name|persistentId
operator|=
name|persistentId
expr_stmt|;
block|}
DECL|method|getPersistentId ()
specifier|public
name|String
name|getPersistentId
parameter_list|()
block|{
return|return
name|persistentId
return|;
block|}
DECL|method|isWriteResultAsHeader ()
specifier|public
name|boolean
name|isWriteResultAsHeader
parameter_list|()
block|{
return|return
name|writeResultAsHeader
return|;
block|}
comment|/**      * In write operations, it determines whether instead of returning {@link WriteResult} as the body of the OUT      * message, we transfer the IN message to the OUT and attach the WriteResult as a header.      *       * @param writeResultAsHeader flag to indicate if this option is enabled      */
DECL|method|setWriteResultAsHeader (boolean writeResultAsHeader)
specifier|public
name|void
name|setWriteResultAsHeader
parameter_list|(
name|boolean
name|writeResultAsHeader
parameter_list|)
block|{
name|this
operator|.
name|writeResultAsHeader
operator|=
name|writeResultAsHeader
expr_stmt|;
block|}
block|}
end_class

end_unit

