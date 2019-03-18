begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams.api
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
name|api
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
name|engine
operator|.
name|ReactiveStreamsEngineConfiguration
import|;
end_import

begin_interface
DECL|interface|CamelReactiveStreamsServiceFactory
specifier|public
interface|interface
name|CamelReactiveStreamsServiceFactory
block|{
comment|/**      * Creates a new instance of the {@link ReactiveStreamsEngineConfiguration}      *      * @param context the Camel context      * @param configuration the ReactiveStreams engine configuration      * @return the ReactiveStreams service      */
DECL|method|newInstance (CamelContext context, ReactiveStreamsEngineConfiguration configuration)
name|CamelReactiveStreamsService
name|newInstance
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ReactiveStreamsEngineConfiguration
name|configuration
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

