begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.multimap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
operator|.
name|multimap
package|;
end_package

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
name|hazelcast
operator|.
name|core
operator|.
name|HazelcastInstance
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|MultiMap
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
name|component
operator|.
name|hazelcast
operator|.
name|HazelcastComponentHelper
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
name|hazelcast
operator|.
name|HazelcastConstants
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
name|hazelcast
operator|.
name|HazelcastDefaultEndpoint
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
name|hazelcast
operator|.
name|HazelcastDefaultProducer
import|;
end_import

begin_class
DECL|class|HazelcastMultimapProducer
specifier|public
class|class
name|HazelcastMultimapProducer
extends|extends
name|HazelcastDefaultProducer
block|{
DECL|field|cache
specifier|private
specifier|final
name|MultiMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
decl_stmt|;
DECL|method|HazelcastMultimapProducer (HazelcastInstance hazelcastInstance, HazelcastDefaultEndpoint endpoint, String cacheName)
specifier|public
name|HazelcastMultimapProducer
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|,
name|HazelcastDefaultEndpoint
name|endpoint
parameter_list|,
name|String
name|cacheName
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|cache
operator|=
name|hazelcastInstance
operator|.
name|getMultiMap
argument_list|(
name|cacheName
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
comment|// get header parameters
name|Object
name|oid
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|)
condition|)
block|{
name|oid
operator|=
name|headers
operator|.
name|get
argument_list|(
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|)
expr_stmt|;
block|}
specifier|final
name|int
name|operation
init|=
name|lookupOperationNumber
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
name|HazelcastConstants
operator|.
name|PUT_OPERATION
case|:
name|this
operator|.
name|put
argument_list|(
name|oid
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|HazelcastConstants
operator|.
name|GET_OPERATION
case|:
name|this
operator|.
name|get
argument_list|(
name|oid
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|HazelcastConstants
operator|.
name|DELETE_OPERATION
case|:
name|this
operator|.
name|delete
argument_list|(
name|oid
argument_list|)
expr_stmt|;
break|break;
case|case
name|HazelcastConstants
operator|.
name|REMOVEVALUE_OPERATION
case|:
name|this
operator|.
name|removevalue
argument_list|(
name|oid
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|HazelcastConstants
operator|.
name|CLEAR_OPERATION
case|:
name|this
operator|.
name|clear
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"The value '%s' is not allowed for parameter '%s' on the MULTIMAP cache."
argument_list|,
name|operation
argument_list|,
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|)
argument_list|)
throw|;
block|}
comment|// finally copy headers
name|HazelcastComponentHelper
operator|.
name|copyHeaders
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|put (Object oid, Exchange exchange)
specifier|private
name|void
name|put
parameter_list|(
name|Object
name|oid
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|this
operator|.
name|cache
operator|.
name|put
argument_list|(
name|oid
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|get (Object oid, Exchange exchange)
specifier|private
name|void
name|get
parameter_list|(
name|Object
name|oid
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|this
operator|.
name|cache
operator|.
name|get
argument_list|(
name|oid
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|delete (Object oid)
specifier|private
name|void
name|delete
parameter_list|(
name|Object
name|oid
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|.
name|remove
argument_list|(
name|oid
argument_list|)
expr_stmt|;
block|}
DECL|method|removevalue (Object oid, Exchange exchange)
specifier|private
name|void
name|removevalue
parameter_list|(
name|Object
name|oid
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|.
name|remove
argument_list|(
name|oid
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|clear (Exchange exchange)
specifier|private
name|void
name|clear
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

