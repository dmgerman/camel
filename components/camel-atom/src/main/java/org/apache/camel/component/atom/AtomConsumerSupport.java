begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atom
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atom
package|;
end_package

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
name|impl
operator|.
name|ScheduledPollConsumer
import|;
end_import

begin_comment
comment|/**  * Base class for consuming Atom feeds.  */
end_comment

begin_class
DECL|class|AtomConsumerSupport
specifier|public
specifier|abstract
class|class
name|AtomConsumerSupport
extends|extends
name|ScheduledPollConsumer
block|{
DECL|field|DEFAULT_CONSUMER_DELAY
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_CONSUMER_DELAY
init|=
literal|60
operator|*
literal|1000L
decl_stmt|;
DECL|field|endpoint
specifier|protected
specifier|final
name|AtomEndpoint
name|endpoint
decl_stmt|;
DECL|method|AtomConsumerSupport (AtomEndpoint endpoint, Processor processor)
specifier|public
name|AtomConsumerSupport
parameter_list|(
name|AtomEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
block|}
end_class

end_unit

