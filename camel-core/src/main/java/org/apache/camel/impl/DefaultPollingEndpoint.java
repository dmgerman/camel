begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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

begin_comment
comment|/**  * A base class for an endpoint which the default consumer mode is to use a {@link org.apache.camel.PollingConsumer}  *  * @version   */
end_comment

begin_class
DECL|class|DefaultPollingEndpoint
specifier|public
specifier|abstract
class|class
name|DefaultPollingEndpoint
extends|extends
name|ScheduledPollEndpoint
block|{
DECL|method|DefaultPollingEndpoint ()
specifier|protected
name|DefaultPollingEndpoint
parameter_list|()
block|{     }
DECL|method|DefaultPollingEndpoint (String endpointUri, Component component)
specifier|protected
name|DefaultPollingEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Deprecated
DECL|method|DefaultPollingEndpoint (String endpointUri)
specifier|protected
name|DefaultPollingEndpoint
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
annotation|@
name|Deprecated
DECL|method|DefaultPollingEndpoint (String endpointUri, CamelContext context)
specifier|protected
name|DefaultPollingEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|context
argument_list|)
expr_stmt|;
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
name|Consumer
name|result
init|=
operator|new
name|DefaultScheduledPollConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

