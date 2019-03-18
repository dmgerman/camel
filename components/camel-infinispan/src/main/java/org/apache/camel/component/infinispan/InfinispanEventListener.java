begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|infinispan
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|InfinispanEventListener
specifier|public
specifier|abstract
class|class
name|InfinispanEventListener
block|{
DECL|field|infinispanConsumer
specifier|protected
name|InfinispanConsumer
name|infinispanConsumer
decl_stmt|;
DECL|field|eventTypes
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|eventTypes
decl_stmt|;
DECL|field|cacheName
specifier|protected
name|String
name|cacheName
decl_stmt|;
DECL|method|InfinispanEventListener (InfinispanConsumer infinispanConsumer, Set<String> eventTypes)
specifier|public
name|InfinispanEventListener
parameter_list|(
name|InfinispanConsumer
name|infinispanConsumer
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|eventTypes
parameter_list|)
block|{
name|this
operator|.
name|infinispanConsumer
operator|=
name|infinispanConsumer
expr_stmt|;
name|this
operator|.
name|eventTypes
operator|=
name|eventTypes
expr_stmt|;
block|}
DECL|method|setInfinispanConsumer (InfinispanConsumer infinispanConsumer)
specifier|public
name|void
name|setInfinispanConsumer
parameter_list|(
name|InfinispanConsumer
name|infinispanConsumer
parameter_list|)
block|{
name|this
operator|.
name|infinispanConsumer
operator|=
name|infinispanConsumer
expr_stmt|;
block|}
DECL|method|setEventTypes (Set<String> eventTypes)
specifier|public
name|void
name|setEventTypes
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|eventTypes
parameter_list|)
block|{
name|this
operator|.
name|eventTypes
operator|=
name|eventTypes
expr_stmt|;
block|}
DECL|method|setCacheName (String cacheName)
specifier|public
name|void
name|setCacheName
parameter_list|(
name|String
name|cacheName
parameter_list|)
block|{
name|this
operator|.
name|cacheName
operator|=
name|cacheName
expr_stmt|;
block|}
DECL|method|isAccepted (String eventType)
specifier|protected
name|boolean
name|isAccepted
parameter_list|(
name|String
name|eventType
parameter_list|)
block|{
return|return
name|eventTypes
operator|==
literal|null
operator|||
name|eventTypes
operator|.
name|isEmpty
argument_list|()
operator|||
name|eventTypes
operator|.
name|contains
argument_list|(
name|eventType
argument_list|)
return|;
block|}
block|}
end_class

end_unit

