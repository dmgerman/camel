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
name|ReactiveStreamsComponent
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
name|ReactiveStreamsConstants
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * This is the main entry-point for getting Camel streams associate to reactive-streams endpoints.  *  * It allows to retrieve the {@link CamelReactiveStreamsService} to access Camel streams.  */
end_comment

begin_class
DECL|class|CamelReactiveStreams
specifier|public
specifier|final
class|class
name|CamelReactiveStreams
block|{
DECL|method|CamelReactiveStreams ()
specifier|private
name|CamelReactiveStreams
parameter_list|()
block|{     }
DECL|method|get (CamelContext context)
specifier|public
specifier|static
name|CamelReactiveStreamsService
name|get
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|ReactiveStreamsComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
name|ReactiveStreamsConstants
operator|.
name|SCHEME
argument_list|,
name|ReactiveStreamsComponent
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|component
operator|.
name|getReactiveStreamsService
argument_list|()
argument_list|,
literal|"ReactiveStreamsService"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

