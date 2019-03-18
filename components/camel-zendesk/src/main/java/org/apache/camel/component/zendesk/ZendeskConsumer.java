begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zendesk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zendesk
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
name|component
operator|.
name|zendesk
operator|.
name|internal
operator|.
name|ZendeskApiName
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
name|component
operator|.
name|AbstractApiConsumer
import|;
end_import

begin_comment
comment|/**  * The Zendesk consumer.  */
end_comment

begin_class
DECL|class|ZendeskConsumer
specifier|public
class|class
name|ZendeskConsumer
extends|extends
name|AbstractApiConsumer
argument_list|<
name|ZendeskApiName
argument_list|,
name|ZendeskConfiguration
argument_list|>
block|{
DECL|method|ZendeskConsumer (ZendeskEndpoint endpoint, Processor processor)
specifier|public
name|ZendeskConsumer
parameter_list|(
name|ZendeskEndpoint
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
block|}
block|}
end_class

end_unit

