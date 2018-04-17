begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.box
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|box
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxAPIConnection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxEvent
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|EventListener
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
name|box
operator|.
name|api
operator|.
name|BoxEventsManager
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
name|box
operator|.
name|internal
operator|.
name|BoxApiName
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
name|component
operator|.
name|AbstractApiConsumer
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
name|component
operator|.
name|ApiConsumerHelper
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
name|component
operator|.
name|ApiMethod
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
name|component
operator|.
name|ApiMethodHelper
import|;
end_import

begin_comment
comment|/**  * The Box consumer.  */
end_comment

begin_class
DECL|class|BoxConsumer
specifier|public
class|class
name|BoxConsumer
extends|extends
name|AbstractApiConsumer
argument_list|<
name|BoxApiName
argument_list|,
name|BoxConfiguration
argument_list|>
implements|implements
name|EventListener
block|{
DECL|field|LISTENER_PROPERTY
specifier|private
specifier|static
specifier|final
name|String
name|LISTENER_PROPERTY
init|=
literal|"listener"
decl_stmt|;
DECL|field|boxConnection
specifier|private
name|BoxAPIConnection
name|boxConnection
decl_stmt|;
DECL|field|apiProxy
specifier|private
name|BoxEventsManager
name|apiProxy
decl_stmt|;
DECL|field|apiMethod
specifier|private
specifier|final
name|ApiMethod
name|apiMethod
decl_stmt|;
DECL|field|properties
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
decl_stmt|;
DECL|method|BoxConsumer (BoxEndpoint endpoint, Processor processor)
specifier|public
name|BoxConsumer
parameter_list|(
name|BoxEndpoint
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
name|apiMethod
operator|=
name|ApiConsumerHelper
operator|.
name|findMethod
argument_list|(
name|endpoint
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// Add listener property to register this consumer as listener for
comment|// events.
name|properties
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|properties
operator|.
name|putAll
argument_list|(
name|endpoint
operator|.
name|getEndpointProperties
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|LISTENER_PROPERTY
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|boxConnection
operator|=
name|endpoint
operator|.
name|getBoxConnection
argument_list|()
expr_stmt|;
name|apiProxy
operator|=
operator|new
name|BoxEventsManager
argument_list|(
name|boxConnection
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|interceptPropertyNames (Set<String> propertyNames)
specifier|public
name|void
name|interceptPropertyNames
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|propertyNames
parameter_list|)
block|{
name|propertyNames
operator|.
name|add
argument_list|(
name|LISTENER_PROPERTY
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onEvent (BoxEvent event)
specifier|public
name|void
name|onEvent
parameter_list|(
name|BoxEvent
name|event
parameter_list|)
block|{
try|try
block|{
comment|// Convert Events to exchange and process
name|log
operator|.
name|debug
argument_list|(
literal|"Processed {} event for {}"
argument_list|,
name|ApiConsumerHelper
operator|.
name|getResultsProcessed
argument_list|(
name|this
argument_list|,
name|event
argument_list|,
literal|false
argument_list|)
argument_list|,
name|boxConnection
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Received exception consuming event: "
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onNextPosition (long position)
specifier|public
name|void
name|onNextPosition
parameter_list|(
name|long
name|position
parameter_list|)
block|{     }
annotation|@
name|Override
DECL|method|onException (Throwable e)
specifier|public
name|boolean
name|onException
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
comment|// invoke the API method to start listening
name|ApiMethodHelper
operator|.
name|invokeMethod
argument_list|(
name|apiProxy
argument_list|,
name|apiMethod
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|apiProxy
operator|.
name|stopListening
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

