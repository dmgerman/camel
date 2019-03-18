begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|engine
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
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreamsService
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
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreamsServiceFactory
import|;
end_import

begin_class
DECL|class|DefaultCamelReactiveStreamsServiceFactory
specifier|public
class|class
name|DefaultCamelReactiveStreamsServiceFactory
implements|implements
name|CamelReactiveStreamsServiceFactory
block|{
annotation|@
name|Override
DECL|method|newInstance (CamelContext context, ReactiveStreamsEngineConfiguration configuration)
specifier|public
name|CamelReactiveStreamsService
name|newInstance
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ReactiveStreamsEngineConfiguration
name|configuration
parameter_list|)
block|{
return|return
operator|new
name|DefaultCamelReactiveStreamsService
argument_list|(
name|context
argument_list|,
name|configuration
argument_list|)
return|;
block|}
block|}
end_class

end_unit

