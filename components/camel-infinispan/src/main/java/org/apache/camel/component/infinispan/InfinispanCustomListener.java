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

begin_comment
comment|/**  * This class is supposed to be extended by users and annotated with @Listener or @ClientListener  * and passed to the consumer endpoint through the 'customListener' parameter.  */
end_comment

begin_class
DECL|class|InfinispanCustomListener
specifier|public
specifier|abstract
class|class
name|InfinispanCustomListener
extends|extends
name|InfinispanEventListener
block|{
DECL|method|InfinispanCustomListener (InfinispanConsumer infinispanConsumer, Set<String> eventTypes)
specifier|public
name|InfinispanCustomListener
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
name|super
argument_list|(
name|infinispanConsumer
argument_list|,
name|eventTypes
argument_list|)
expr_stmt|;
block|}
DECL|method|getInfinispanConsumer ()
specifier|public
name|InfinispanConsumer
name|getInfinispanConsumer
parameter_list|()
block|{
return|return
name|infinispanConsumer
return|;
block|}
annotation|@
name|Override
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
block|}
end_class

end_unit

