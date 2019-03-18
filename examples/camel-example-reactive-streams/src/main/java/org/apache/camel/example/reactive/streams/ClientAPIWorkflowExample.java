begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.reactive.streams
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|reactive
operator|.
name|streams
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
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
name|builder
operator|.
name|RouteBuilder
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|condition
operator|.
name|ConditionalOnProperty
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|reactor
operator|.
name|core
operator|.
name|publisher
operator|.
name|Flux
import|;
end_import

begin_comment
comment|/**  * This example shows how to define a complex workflow using Camel direct client API.  *  * Note: the code is not spring-boot related and could have been placed in a standalone main().  */
end_comment

begin_class
annotation|@
name|Configuration
annotation|@
name|ConditionalOnProperty
argument_list|(
literal|"examples.client-api.workflow"
argument_list|)
DECL|class|ClientAPIWorkflowExample
specifier|public
class|class
name|ClientAPIWorkflowExample
block|{
comment|/**      * The reactor streams.      */
annotation|@
name|Component
DECL|class|ClientAPIWorkflowExampleStreams
specifier|public
specifier|static
class|class
name|ClientAPIWorkflowExampleStreams
block|{
annotation|@
name|Autowired
DECL|field|camel
specifier|private
name|CamelReactiveStreamsService
name|camel
decl_stmt|;
annotation|@
name|PostConstruct
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
block|{
comment|/**              * This workflow reads all files from the directory named "input",              * marshals them using the Camel marshalling features (simulation)              * and sends them to an external system (simulation)              * only if they contain the word "camel".              */
name|Flux
operator|.
name|from
argument_list|(
name|camel
operator|.
name|from
argument_list|(
literal|"file:input"
argument_list|,
name|InputStream
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|flatMap
argument_list|(
name|camel
operator|.
name|to
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|text
lambda|->
name|text
operator|.
name|contains
argument_list|(
literal|"camel"
argument_list|)
argument_list|)
operator|.
name|flatMap
argument_list|(
name|camel
operator|.
name|to
argument_list|(
literal|"direct:send"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|subscribe
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * The Camel Configuration.      */
annotation|@
name|Component
DECL|class|BasicReactorToCamelExampleRoutes
specifier|public
specifier|static
class|class
name|BasicReactorToCamelExampleRoutes
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:unmarshal"
argument_list|)
comment|// This can be far more complex, using marshal()
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|log
argument_list|(
literal|"Content marshalled to string: ${body}"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:send"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Sending the file to an external system (simulation)"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

