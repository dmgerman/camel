begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|time
operator|.
name|Duration
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
comment|/**  * This example shows how a reactive stream framework can asynchronously request data to a Camel stream  * and continue processing.  *  * The exchange pattern is in-out from Reactor to Camel.  *  * Note: the Camel and reactor components are placed in the same configuration class for the sake of clarity,  * but they can be moved in their own files.  */
end_comment

begin_class
annotation|@
name|Configuration
annotation|@
name|ConditionalOnProperty
argument_list|(
literal|"examples.basic.reactor-to-camel-in-out"
argument_list|)
DECL|class|BasicReactorToCamelInOutExample
specifier|public
class|class
name|BasicReactorToCamelInOutExample
block|{
comment|/**      * The reactor streams.      */
annotation|@
name|Component
DECL|class|BasicReactorToCamelInOutExampleStreams
specifier|public
specifier|static
class|class
name|BasicReactorToCamelInOutExampleStreams
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BasicReactorToCamelInOutExample
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Autowired
DECL|field|camel
specifier|private
name|CamelReactiveStreamsService
name|camel
decl_stmt|;
annotation|@
name|PostConstruct
DECL|method|setupStreams ()
specifier|public
name|void
name|setupStreams
parameter_list|()
block|{
name|Flux
operator|.
name|interval
argument_list|(
name|Duration
operator|.
name|ofSeconds
argument_list|(
literal|8
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|i
lambda|->
name|i
operator|+
literal|1
argument_list|)
comment|// to start from 1
operator|.
name|flatMap
argument_list|(
name|camel
operator|.
name|toStream
argument_list|(
literal|"sqrt"
argument_list|,
name|Double
operator|.
name|class
argument_list|)
argument_list|)
comment|// call Camel and continue
operator|.
name|map
argument_list|(
name|d
lambda|->
literal|"BasicReactorToCamelInOut - sqrt="
operator|+
name|d
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|LOG
operator|::
name|info
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
DECL|class|BasicReactorToCamelInOutExampleRoutes
specifier|public
specifier|static
class|class
name|BasicReactorToCamelInOutExampleRoutes
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
comment|// Transform the body of every exchange into its square root
name|from
argument_list|(
literal|"reactive-streams:sqrt"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|body
argument_list|(
name|Double
operator|.
name|class
argument_list|,
name|Math
operator|::
name|sqrt
argument_list|)
expr_stmt|;
comment|// This route can be much more complex: it can use any Camel component to compute the output data
block|}
block|}
block|}
end_class

end_unit

