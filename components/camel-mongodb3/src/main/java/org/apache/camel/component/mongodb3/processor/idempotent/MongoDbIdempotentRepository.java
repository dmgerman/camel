begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mongodb3.processor.idempotent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mongodb3
operator|.
name|processor
operator|.
name|idempotent
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|model
operator|.
name|Filters
operator|.
name|eq
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mongodb3
operator|.
name|MongoDbConstants
operator|.
name|MONGO_ID
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|IdempotentRepository
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
name|support
operator|.
name|ServiceSupport
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
name|bson
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|conversions
operator|.
name|Bson
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|ErrorCategory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|MongoClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|MongoCollection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|result
operator|.
name|DeleteResult
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Mongo db based message id repository"
argument_list|)
DECL|class|MongoDbIdempotentRepository
specifier|public
class|class
name|MongoDbIdempotentRepository
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ServiceSupport
implements|implements
name|IdempotentRepository
argument_list|<
name|E
argument_list|>
block|{
DECL|field|mongoClient
specifier|private
name|MongoClient
name|mongoClient
decl_stmt|;
DECL|field|collectionName
specifier|private
name|String
name|collectionName
decl_stmt|;
DECL|field|dbName
specifier|private
name|String
name|dbName
decl_stmt|;
DECL|field|collection
specifier|private
name|MongoCollection
argument_list|<
name|Document
argument_list|>
name|collection
decl_stmt|;
DECL|method|MongoDbIdempotentRepository ()
specifier|public
name|MongoDbIdempotentRepository
parameter_list|()
block|{     }
DECL|method|MongoDbIdempotentRepository (MongoClient mongoClient, String collectionName, String dbName)
specifier|public
name|MongoDbIdempotentRepository
parameter_list|(
name|MongoClient
name|mongoClient
parameter_list|,
name|String
name|collectionName
parameter_list|,
name|String
name|dbName
parameter_list|)
block|{
name|this
operator|.
name|mongoClient
operator|=
name|mongoClient
expr_stmt|;
name|this
operator|.
name|collectionName
operator|=
name|collectionName
expr_stmt|;
name|this
operator|.
name|dbName
operator|=
name|dbName
expr_stmt|;
name|this
operator|.
name|collection
operator|=
name|mongoClient
operator|.
name|getDatabase
argument_list|(
name|dbName
argument_list|)
operator|.
name|getCollection
argument_list|(
name|collectionName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Adds the key to the store"
argument_list|)
annotation|@
name|Override
DECL|method|add (E key)
specifier|public
name|boolean
name|add
parameter_list|(
name|E
name|key
parameter_list|)
block|{
name|Document
name|document
init|=
operator|new
name|Document
argument_list|(
name|MONGO_ID
argument_list|,
name|key
argument_list|)
decl_stmt|;
try|try
block|{
name|collection
operator|.
name|insertOne
argument_list|(
name|document
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|com
operator|.
name|mongodb
operator|.
name|MongoWriteException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|.
name|getError
argument_list|()
operator|.
name|getCategory
argument_list|()
operator|==
name|ErrorCategory
operator|.
name|DUPLICATE_KEY
condition|)
block|{
return|return
literal|false
return|;
block|}
throw|throw
name|ex
throw|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Does the store contain the given key"
argument_list|)
annotation|@
name|Override
DECL|method|contains (E key)
specifier|public
name|boolean
name|contains
parameter_list|(
name|E
name|key
parameter_list|)
block|{
name|Bson
name|document
init|=
name|eq
argument_list|(
name|MONGO_ID
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|long
name|count
init|=
name|collection
operator|.
name|count
argument_list|(
name|document
argument_list|)
decl_stmt|;
return|return
name|count
operator|>
literal|0
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Remove the key from the store"
argument_list|)
annotation|@
name|Override
DECL|method|remove (E key)
specifier|public
name|boolean
name|remove
parameter_list|(
name|E
name|key
parameter_list|)
block|{
name|Bson
name|document
init|=
name|eq
argument_list|(
name|MONGO_ID
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|DeleteResult
name|res
init|=
name|collection
operator|.
name|deleteOne
argument_list|(
name|document
argument_list|)
decl_stmt|;
return|return
name|res
operator|.
name|getDeletedCount
argument_list|()
operator|>
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|confirm (E key)
specifier|public
name|boolean
name|confirm
parameter_list|(
name|E
name|key
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Clear the store"
argument_list|)
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|collection
operator|.
name|deleteMany
argument_list|(
operator|new
name|Document
argument_list|()
argument_list|)
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|mongoClient
argument_list|,
literal|"cli"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|dbName
argument_list|,
literal|"dbName"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|collectionName
argument_list|,
literal|"collectionName"
argument_list|)
expr_stmt|;
if|if
condition|(
name|collection
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|collection
operator|=
name|mongoClient
operator|.
name|getDatabase
argument_list|(
name|dbName
argument_list|)
operator|.
name|getCollection
argument_list|(
name|collectionName
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
return|return;
block|}
DECL|method|getMongoClient ()
specifier|public
name|MongoClient
name|getMongoClient
parameter_list|()
block|{
return|return
name|mongoClient
return|;
block|}
DECL|method|setMongoClient (MongoClient mongoClient)
specifier|public
name|void
name|setMongoClient
parameter_list|(
name|MongoClient
name|mongoClient
parameter_list|)
block|{
name|this
operator|.
name|mongoClient
operator|=
name|mongoClient
expr_stmt|;
block|}
DECL|method|getCollectionName ()
specifier|public
name|String
name|getCollectionName
parameter_list|()
block|{
return|return
name|collectionName
return|;
block|}
DECL|method|setCollectionName (String collectionName)
specifier|public
name|void
name|setCollectionName
parameter_list|(
name|String
name|collectionName
parameter_list|)
block|{
name|this
operator|.
name|collectionName
operator|=
name|collectionName
expr_stmt|;
block|}
DECL|method|getDbName ()
specifier|public
name|String
name|getDbName
parameter_list|()
block|{
return|return
name|dbName
return|;
block|}
DECL|method|setDbName (String dbName)
specifier|public
name|void
name|setDbName
parameter_list|(
name|String
name|dbName
parameter_list|)
block|{
name|this
operator|.
name|dbName
operator|=
name|dbName
expr_stmt|;
block|}
block|}
end_class

end_unit

