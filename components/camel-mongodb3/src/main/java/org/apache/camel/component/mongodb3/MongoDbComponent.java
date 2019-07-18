begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mongodb3
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|Endpoint
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
name|annotations
operator|.
name|Component
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
name|DefaultComponent
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link MongoDbEndpoint}.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"mongodb,mongodb3"
argument_list|)
DECL|class|MongoDbComponent
specifier|public
class|class
name|MongoDbComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|WRITE_OPERATIONS
specifier|public
specifier|static
specifier|final
name|Set
argument_list|<
name|MongoDbOperation
argument_list|>
name|WRITE_OPERATIONS
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|MongoDbOperation
operator|.
name|insert
argument_list|,
name|MongoDbOperation
operator|.
name|save
argument_list|,
name|MongoDbOperation
operator|.
name|update
argument_list|,
name|MongoDbOperation
operator|.
name|remove
argument_list|)
argument_list|)
decl_stmt|;
comment|/**      * A connection client provided externally      */
DECL|field|mongoConnection
specifier|private
name|MongoClient
name|mongoConnection
decl_stmt|;
DECL|method|MongoDbComponent ()
specifier|public
name|MongoDbComponent
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|MongoDbComponent (CamelContext context)
specifier|public
name|MongoDbComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|MongoDbEndpoint
name|endpoint
init|=
operator|new
name|MongoDbEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setConnectionBean
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setMongoConnection
argument_list|(
name|mongoConnection
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
comment|/**      * Get the connection bean client used for connection.      *      * @return the client used for connection to db      */
DECL|method|getMongoConnection ()
specifier|public
name|MongoClient
name|getMongoConnection
parameter_list|()
block|{
return|return
name|mongoConnection
return|;
block|}
comment|/**      * Set a client used for connection: all endpoints generated from the component      * will share this connection bean.      *      * @param mongoConnection the connection bean used as client      */
DECL|method|setMongoConnection (MongoClient mongoConnection)
specifier|public
name|void
name|setMongoConnection
parameter_list|(
name|MongoClient
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
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
block|}
DECL|method|wrapInCamelMongoDbException (Throwable t)
specifier|public
specifier|static
name|CamelMongoDbException
name|wrapInCamelMongoDbException
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|t
operator|instanceof
name|CamelMongoDbException
condition|)
block|{
return|return
operator|(
name|CamelMongoDbException
operator|)
name|t
return|;
block|}
else|else
block|{
return|return
operator|new
name|CamelMongoDbException
argument_list|(
name|t
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

