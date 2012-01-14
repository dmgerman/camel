begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.krati
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|krati
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|Map
import|;
end_import

begin_import
import|import
name|krati
operator|.
name|core
operator|.
name|segment
operator|.
name|ChannelSegmentFactory
import|;
end_import

begin_import
import|import
name|krati
operator|.
name|core
operator|.
name|segment
operator|.
name|SegmentFactory
import|;
end_import

begin_import
import|import
name|krati
operator|.
name|io
operator|.
name|Serializer
import|;
end_import

begin_import
import|import
name|krati
operator|.
name|store
operator|.
name|DataStore
import|;
end_import

begin_import
import|import
name|krati
operator|.
name|util
operator|.
name|FnvHashFunction
import|;
end_import

begin_import
import|import
name|krati
operator|.
name|util
operator|.
name|HashFunction
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
name|component
operator|.
name|krati
operator|.
name|serializer
operator|.
name|KratiDefaultSerializer
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

begin_comment
comment|/**  * Represents a Krati endpoint.  */
end_comment

begin_class
DECL|class|KratiEndpoint
specifier|public
class|class
name|KratiEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|dataStoreRegistry
specifier|protected
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|KratiDataStoreRegistration
argument_list|>
name|dataStoreRegistry
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|KratiDataStoreRegistration
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|key
specifier|protected
name|String
name|key
decl_stmt|;
DECL|field|value
specifier|protected
name|String
name|value
decl_stmt|;
DECL|field|operation
specifier|protected
name|String
name|operation
decl_stmt|;
DECL|field|initialCapacity
specifier|protected
name|int
name|initialCapacity
init|=
literal|100
decl_stmt|;
DECL|field|segmentFileSize
specifier|protected
name|int
name|segmentFileSize
init|=
literal|64
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
DECL|field|keySerializer
specifier|protected
name|Serializer
argument_list|<
name|Object
argument_list|>
name|keySerializer
init|=
operator|new
name|KratiDefaultSerializer
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
DECL|field|valueSerializer
specifier|protected
name|Serializer
argument_list|<
name|Object
argument_list|>
name|valueSerializer
init|=
operator|new
name|KratiDefaultSerializer
argument_list|()
decl_stmt|;
DECL|field|segmentFactory
specifier|protected
name|SegmentFactory
name|segmentFactory
init|=
operator|new
name|ChannelSegmentFactory
argument_list|()
decl_stmt|;
DECL|field|hashFunction
specifier|protected
name|HashFunction
argument_list|<
name|byte
index|[]
argument_list|>
name|hashFunction
init|=
operator|new
name|FnvHashFunction
argument_list|()
decl_stmt|;
DECL|field|path
specifier|protected
name|String
name|path
decl_stmt|;
DECL|method|KratiEndpoint (String uri, KratiComponent component)
specifier|public
name|KratiEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|KratiComponent
name|component
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|getPath
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|stop
argument_list|()
expr_stmt|;
name|KratiDataStoreRegistration
name|registration
init|=
name|dataStoreRegistry
operator|.
name|get
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|registration
operator|!=
literal|null
condition|)
block|{
name|registration
operator|.
name|unregister
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|DataStore
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|dataStore
init|=
literal|null
decl_stmt|;
name|KratiDataStoreRegistration
name|registration
init|=
name|dataStoreRegistry
operator|.
name|get
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|registration
operator|!=
literal|null
condition|)
block|{
name|dataStore
operator|=
name|registration
operator|.
name|getDataStore
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|dataStore
operator|==
literal|null
operator|||
operator|!
name|dataStore
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|dataStore
operator|=
name|KratiHelper
operator|.
expr|<
name|Object
operator|,
name|Object
operator|>
name|createDataStore
argument_list|(
name|path
argument_list|,
name|initialCapacity
argument_list|,
name|segmentFileSize
argument_list|,
name|segmentFactory
argument_list|,
name|hashFunction
argument_list|,
name|keySerializer
argument_list|,
name|valueSerializer
argument_list|)
expr_stmt|;
name|dataStoreRegistry
operator|.
name|put
argument_list|(
name|path
argument_list|,
operator|new
name|KratiDataStoreRegistration
argument_list|(
name|dataStore
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|KratiProducer
argument_list|(
name|this
argument_list|,
name|dataStore
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
name|DataStore
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|dataStore
init|=
literal|null
decl_stmt|;
name|KratiDataStoreRegistration
name|registration
init|=
name|dataStoreRegistry
operator|.
name|get
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|registration
operator|!=
literal|null
condition|)
block|{
name|dataStore
operator|=
name|registration
operator|.
name|getDataStore
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|dataStore
operator|==
literal|null
operator|||
operator|!
name|dataStore
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|dataStore
operator|=
name|KratiHelper
operator|.
name|createDataStore
argument_list|(
name|path
argument_list|,
name|initialCapacity
argument_list|,
name|segmentFileSize
argument_list|,
name|segmentFactory
argument_list|,
name|hashFunction
argument_list|,
name|keySerializer
argument_list|,
name|valueSerializer
argument_list|)
expr_stmt|;
name|dataStoreRegistry
operator|.
name|put
argument_list|(
name|path
argument_list|,
operator|new
name|KratiDataStoreRegistration
argument_list|(
name|dataStore
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|KratiConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|dataStore
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
comment|/**      * Returns the path from the URI.      *      * @param uri      * @return      */
DECL|method|getPath (String uri)
specifier|protected
name|String
name|getPath
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|StringBuilder
name|pathBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|u
operator|.
name|getHost
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|pathBuilder
operator|.
name|append
argument_list|(
name|u
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|u
operator|.
name|getPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|pathBuilder
operator|.
name|append
argument_list|(
name|u
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|pathBuilder
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getKey ()
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|setKey (String key)
specifier|public
name|void
name|setKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
DECL|method|getValue ()
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|setValue (String value)
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
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
DECL|method|getInitialCapacity ()
specifier|public
name|int
name|getInitialCapacity
parameter_list|()
block|{
return|return
name|initialCapacity
return|;
block|}
DECL|method|setInitialCapacity (int initialCapacity)
specifier|public
name|void
name|setInitialCapacity
parameter_list|(
name|int
name|initialCapacity
parameter_list|)
block|{
name|this
operator|.
name|initialCapacity
operator|=
name|initialCapacity
expr_stmt|;
block|}
DECL|method|getSegmentFileSize ()
specifier|public
name|int
name|getSegmentFileSize
parameter_list|()
block|{
return|return
name|segmentFileSize
return|;
block|}
DECL|method|setSegmentFileSize (int segmentFileSize)
specifier|public
name|void
name|setSegmentFileSize
parameter_list|(
name|int
name|segmentFileSize
parameter_list|)
block|{
name|this
operator|.
name|segmentFileSize
operator|=
name|segmentFileSize
expr_stmt|;
block|}
DECL|method|getSegmentFactory ()
specifier|public
name|SegmentFactory
name|getSegmentFactory
parameter_list|()
block|{
return|return
name|segmentFactory
return|;
block|}
DECL|method|setSegmentFactory (SegmentFactory segmentFactory)
specifier|public
name|void
name|setSegmentFactory
parameter_list|(
name|SegmentFactory
name|segmentFactory
parameter_list|)
block|{
name|this
operator|.
name|segmentFactory
operator|=
name|segmentFactory
expr_stmt|;
block|}
DECL|method|getHashFunction ()
specifier|public
name|HashFunction
argument_list|<
name|byte
index|[]
argument_list|>
name|getHashFunction
parameter_list|()
block|{
return|return
name|hashFunction
return|;
block|}
DECL|method|setHashFunction (HashFunction<byte[]> hashFunction)
specifier|public
name|void
name|setHashFunction
parameter_list|(
name|HashFunction
argument_list|<
name|byte
index|[]
argument_list|>
name|hashFunction
parameter_list|)
block|{
name|this
operator|.
name|hashFunction
operator|=
name|hashFunction
expr_stmt|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
block|}
end_class

end_unit

