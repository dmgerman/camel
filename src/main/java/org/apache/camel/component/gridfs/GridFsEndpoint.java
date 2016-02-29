begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gridfs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gridfs
package|;
end_package

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
name|gridfs
operator|.
name|GridFS
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
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

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"gridfs"
argument_list|,
name|title
operator|=
literal|"MongoDBGridFS"
argument_list|,
name|syntax
operator|=
literal|"gridfs:connectionBean"
argument_list|,
name|label
operator|=
literal|"database,nosql"
argument_list|)
DECL|class|GridFsEndpoint
specifier|public
class|class
name|GridFsEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|GRIDFS_OPERATION
specifier|public
specifier|static
specifier|final
name|String
name|GRIDFS_OPERATION
init|=
literal|"gridfs.operation"
decl_stmt|;
DECL|field|GRIDFS_METADATA
specifier|public
specifier|static
specifier|final
name|String
name|GRIDFS_METADATA
init|=
literal|"gridfs.metadata"
decl_stmt|;
DECL|field|GRIDFS_CHUNKSIZE
specifier|public
specifier|static
specifier|final
name|String
name|GRIDFS_CHUNKSIZE
init|=
literal|"gridfs.chunksize"
decl_stmt|;
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
name|GridFsEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|connectionBean
specifier|private
name|String
name|connectionBean
decl_stmt|;
annotation|@
name|UriParam
DECL|field|database
specifier|private
name|String
name|database
decl_stmt|;
annotation|@
name|UriParam
DECL|field|bucket
specifier|private
name|String
name|bucket
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|enums
operator|=
literal|"ACKNOWLEDGED,W1,W2,W3,UNACKNOWLEDGED,JOURNALED,MAJORITY,SAFE"
argument_list|)
DECL|field|writeConcern
specifier|private
name|WriteConcern
name|writeConcern
decl_stmt|;
annotation|@
name|UriParam
DECL|field|writeConcernRef
specifier|private
name|WriteConcern
name|writeConcernRef
decl_stmt|;
annotation|@
name|UriParam
DECL|field|readPreference
specifier|private
name|ReadPreference
name|readPreference
decl_stmt|;
annotation|@
name|UriParam
DECL|field|operation
specifier|private
name|String
name|operation
decl_stmt|;
annotation|@
name|UriParam
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
annotation|@
name|UriParam
DECL|field|initialDelay
specifier|private
name|long
name|initialDelay
init|=
literal|1000
decl_stmt|;
annotation|@
name|UriParam
DECL|field|delay
specifier|private
name|long
name|delay
init|=
literal|500
decl_stmt|;
DECL|field|mongoConnection
specifier|private
name|Mongo
name|mongoConnection
decl_stmt|;
DECL|field|db
specifier|private
name|DB
name|db
decl_stmt|;
DECL|field|gridFs
specifier|private
name|GridFS
name|gridFs
decl_stmt|;
DECL|field|filesCollection
specifier|private
name|DBCollection
name|filesCollection
decl_stmt|;
DECL|method|GridFsEndpoint (String uri, GridFsComponent component)
specifier|public
name|GridFsEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|GridFsComponent
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
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|initializeConnection
argument_list|()
expr_stmt|;
return|return
operator|new
name|GridFsProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|initializeConnection
argument_list|()
expr_stmt|;
return|return
operator|new
name|GridFsConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|initializeConnection ()
specifier|public
name|void
name|initializeConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Initialize GridFS endpoint: {}"
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
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Missing required endpoint configuration: database"
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
name|IllegalStateException
argument_list|(
literal|"Could not initialize GridFsComponent. Database "
operator|+
name|database
operator|+
literal|" does not exist."
argument_list|)
throw|;
block|}
name|gridFs
operator|=
operator|new
name|GridFS
argument_list|(
name|db
argument_list|,
name|bucket
operator|==
literal|null
condition|?
name|GridFS
operator|.
name|DEFAULT_BUCKET
else|:
name|bucket
argument_list|)
block|{
block|{
name|filesCollection
operator|=
name|getFilesCollection
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
block|}
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
DECL|method|getConnectionBean ()
specifier|public
name|String
name|getConnectionBean
parameter_list|()
block|{
return|return
name|connectionBean
return|;
block|}
comment|/**      * Name of {@link com.mongodb.Mongo} to use.      */
DECL|method|setConnectionBean (String connectionBean)
specifier|public
name|void
name|setConnectionBean
parameter_list|(
name|String
name|connectionBean
parameter_list|)
block|{
name|this
operator|.
name|connectionBean
operator|=
name|connectionBean
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
DECL|method|getBucket ()
specifier|public
name|String
name|getBucket
parameter_list|()
block|{
return|return
name|bucket
return|;
block|}
DECL|method|setBucket (String bucket)
specifier|public
name|void
name|setBucket
parameter_list|(
name|String
name|bucket
parameter_list|)
block|{
name|this
operator|.
name|bucket
operator|=
name|bucket
expr_stmt|;
block|}
DECL|method|getQuery ()
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
DECL|method|setQuery (String query)
specifier|public
name|void
name|setQuery
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
DECL|method|getDelay ()
specifier|public
name|long
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
DECL|method|setDelay (long delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
block|}
DECL|method|getInitialDelay ()
specifier|public
name|long
name|getInitialDelay
parameter_list|()
block|{
return|return
name|initialDelay
return|;
block|}
DECL|method|setInitialDelay (long initialDelay)
specifier|public
name|void
name|setInitialDelay
parameter_list|(
name|long
name|initialDelay
parameter_list|)
block|{
name|this
operator|.
name|initialDelay
operator|=
name|delay
expr_stmt|;
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
comment|/**      * Sets the operation this endpoint will execute against GridRS.      */
DECL|method|setOperation (String operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
DECL|method|getGridFs ()
specifier|public
name|GridFS
name|getGridFs
parameter_list|()
block|{
return|return
name|gridFs
return|;
block|}
DECL|method|setGridFs (GridFS gridFs)
specifier|public
name|void
name|setGridFs
parameter_list|(
name|GridFS
name|gridFs
parameter_list|)
block|{
name|this
operator|.
name|gridFs
operator|=
name|gridFs
expr_stmt|;
block|}
DECL|method|getFilesCollection ()
specifier|public
name|DBCollection
name|getFilesCollection
parameter_list|()
block|{
return|return
name|filesCollection
return|;
block|}
block|}
end_class

end_unit

