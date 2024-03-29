begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.iec60870.server
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|iec60870
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CompletionStage
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|iec60870
operator|.
name|DiscardAckModule
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
name|iec60870
operator|.
name|ObjectAddress
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|asdu
operator|.
name|types
operator|.
name|ASDUAddress
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|asdu
operator|.
name|types
operator|.
name|InformationObjectAddress
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|asdu
operator|.
name|types
operator|.
name|Value
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|server
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|server
operator|.
name|data
operator|.
name|DataModule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|server
operator|.
name|data
operator|.
name|model
operator|.
name|BackgroundModel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|server
operator|.
name|data
operator|.
name|model
operator|.
name|ChangeDataModel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|server
operator|.
name|data
operator|.
name|model
operator|.
name|ChangeModel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|server
operator|.
name|data
operator|.
name|model
operator|.
name|WriteModel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|server
operator|.
name|data
operator|.
name|model
operator|.
name|WriteModel
operator|.
name|Action
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|server
operator|.
name|data
operator|.
name|model
operator|.
name|WriteModel
operator|.
name|Request
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
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
import|;
end_import

begin_class
DECL|class|ServerInstance
specifier|public
class|class
name|ServerInstance
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
name|ServerInstance
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|options
specifier|private
specifier|final
name|ServerOptions
name|options
decl_stmt|;
DECL|class|DataModelImpl
specifier|private
specifier|final
class|class
name|DataModelImpl
extends|extends
name|ChangeDataModel
block|{
DECL|method|DataModelImpl ()
specifier|private
name|DataModelImpl
parameter_list|()
block|{
name|super
argument_list|(
literal|"Camel/IEC60870/DataModel"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createChangeModel ()
specifier|protected
name|ChangeModel
name|createChangeModel
parameter_list|()
block|{
if|if
condition|(
name|ServerInstance
operator|.
name|this
operator|.
name|options
operator|.
name|getBufferingPeriod
argument_list|()
operator|!=
literal|null
operator|&&
name|ServerInstance
operator|.
name|this
operator|.
name|options
operator|.
name|getBufferingPeriod
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating buffering change model: {} ms"
argument_list|,
name|ServerInstance
operator|.
name|this
operator|.
name|options
operator|.
name|getBufferingPeriod
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|makeBufferingChangeModel
argument_list|(
name|ServerInstance
operator|.
name|this
operator|.
name|options
operator|.
name|getBufferingPeriod
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating instant change model"
argument_list|)
expr_stmt|;
return|return
name|makeInstantChangeModel
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|createWriteModel ()
specifier|protected
name|WriteModel
name|createWriteModel
parameter_list|()
block|{
return|return
operator|new
name|WriteModel
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Action
name|prepareCommand
parameter_list|(
specifier|final
name|Request
argument_list|<
name|Boolean
argument_list|>
name|request
parameter_list|)
block|{
return|return
name|prepareAction
argument_list|(
name|request
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Action
name|prepareSetpointFloat
parameter_list|(
specifier|final
name|Request
argument_list|<
name|Float
argument_list|>
name|request
parameter_list|)
block|{
return|return
name|prepareAction
argument_list|(
name|request
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Action
name|prepareSetpointScaled
parameter_list|(
specifier|final
name|Request
argument_list|<
name|Short
argument_list|>
name|request
parameter_list|)
block|{
return|return
name|prepareAction
argument_list|(
name|request
argument_list|)
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|createBackgroundModel ()
specifier|protected
name|BackgroundModel
name|createBackgroundModel
parameter_list|()
block|{
if|if
condition|(
name|ServerInstance
operator|.
name|this
operator|.
name|options
operator|.
name|getBackgroundScanPeriod
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating background scan model: {} ms"
argument_list|,
name|ServerInstance
operator|.
name|this
operator|.
name|options
operator|.
name|getBackgroundScanPeriod
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|makeDefaultBackgroundModel
argument_list|()
return|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Not creating background scan model"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|notifyDataChange (final ASDUAddress asduAddress, final InformationObjectAddress informationObjectAddress, final Value<?> value, final boolean notify)
specifier|public
name|void
name|notifyDataChange
parameter_list|(
specifier|final
name|ASDUAddress
name|asduAddress
parameter_list|,
specifier|final
name|InformationObjectAddress
name|informationObjectAddress
parameter_list|,
specifier|final
name|Value
argument_list|<
name|?
argument_list|>
name|value
parameter_list|,
specifier|final
name|boolean
name|notify
parameter_list|)
block|{
name|super
operator|.
name|notifyDataChange
argument_list|(
name|asduAddress
argument_list|,
name|informationObjectAddress
argument_list|,
name|value
argument_list|,
name|notify
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|FunctionalInterface
DECL|interface|ServerObjectListener
specifier|public
interface|interface
name|ServerObjectListener
block|{
DECL|method|execute (Request<?> request)
name|CompletionStage
argument_list|<
name|Void
argument_list|>
name|execute
parameter_list|(
name|Request
argument_list|<
name|?
argument_list|>
name|request
parameter_list|)
function_decl|;
block|}
DECL|field|dataModel
specifier|private
specifier|final
name|DataModelImpl
name|dataModel
init|=
operator|new
name|DataModelImpl
argument_list|()
decl_stmt|;
DECL|field|server
specifier|private
name|Server
name|server
decl_stmt|;
DECL|field|dataModule
specifier|private
name|DataModule
name|dataModule
decl_stmt|;
DECL|field|address
specifier|private
specifier|final
name|InetSocketAddress
name|address
decl_stmt|;
DECL|field|listeners
specifier|private
specifier|final
name|Map
argument_list|<
name|ObjectAddress
argument_list|,
name|ServerObjectListener
argument_list|>
name|listeners
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|ServerInstance (final String host, final int port, final ServerOptions options)
specifier|public
name|ServerInstance
parameter_list|(
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|int
name|port
parameter_list|,
specifier|final
name|ServerOptions
name|options
parameter_list|)
throws|throws
name|UnknownHostException
block|{
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
name|this
operator|.
name|address
operator|=
operator|new
name|InetSocketAddress
argument_list|(
name|InetAddress
operator|.
name|getByName
argument_list|(
name|host
argument_list|)
argument_list|,
name|port
argument_list|)
expr_stmt|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
name|this
operator|.
name|dataModel
operator|.
name|start
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataModule
operator|=
operator|new
name|DataModule
argument_list|(
name|this
operator|.
name|options
operator|.
name|getDataModuleOptions
argument_list|()
argument_list|,
name|this
operator|.
name|dataModel
argument_list|)
expr_stmt|;
name|this
operator|.
name|server
operator|=
operator|new
name|Server
argument_list|(
name|this
operator|.
name|address
argument_list|,
name|this
operator|.
name|options
operator|.
name|getProtocolOptions
argument_list|()
argument_list|,
name|asList
argument_list|(
name|this
operator|.
name|dataModule
argument_list|,
operator|new
name|DiscardAckModule
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
specifier|final
name|LinkedList
argument_list|<
name|Exception
argument_list|>
name|ex
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|server
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|this
operator|.
name|server
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|e
parameter_list|)
block|{
name|ex
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|server
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|dataModule
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|this
operator|.
name|dataModule
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|e
parameter_list|)
block|{
name|ex
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|dataModule
operator|=
literal|null
expr_stmt|;
block|}
comment|// handle all exceptions
specifier|final
name|Exception
name|e
init|=
name|ex
operator|.
name|pollFirst
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|RuntimeException
name|re
decl_stmt|;
if|if
condition|(
name|e
operator|instanceof
name|RuntimeException
condition|)
block|{
name|re
operator|=
operator|(
name|RuntimeException
operator|)
name|e
expr_stmt|;
block|}
else|else
block|{
name|re
operator|=
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|ex
operator|.
name|forEach
argument_list|(
name|re
operator|::
name|addSuppressed
argument_list|)
expr_stmt|;
throw|throw
name|re
throw|;
block|}
block|}
DECL|method|prepareAction (final Request<?> request)
specifier|private
name|Action
name|prepareAction
parameter_list|(
specifier|final
name|Request
argument_list|<
name|?
argument_list|>
name|request
parameter_list|)
block|{
specifier|final
name|ObjectAddress
name|address
init|=
name|ObjectAddress
operator|.
name|valueOf
argument_list|(
name|request
operator|.
name|getHeader
argument_list|()
operator|.
name|getAsduAddress
argument_list|()
argument_list|,
name|request
operator|.
name|getAddress
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|ServerObjectListener
name|listener
init|=
name|this
operator|.
name|listeners
operator|.
name|get
argument_list|(
name|address
argument_list|)
decl_stmt|;
if|if
condition|(
name|listener
operator|==
literal|null
condition|)
block|{
comment|// no one is listening
return|return
literal|null
return|;
block|}
return|return
parameter_list|()
lambda|->
name|listener
operator|.
name|execute
argument_list|(
name|request
argument_list|)
return|;
block|}
DECL|method|setListener (final ObjectAddress address, final ServerObjectListener listener)
specifier|public
name|void
name|setListener
parameter_list|(
specifier|final
name|ObjectAddress
name|address
parameter_list|,
specifier|final
name|ServerObjectListener
name|listener
parameter_list|)
block|{
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|address
argument_list|)
expr_stmt|;
if|if
condition|(
name|listener
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|listeners
operator|.
name|put
argument_list|(
name|address
argument_list|,
name|listener
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|listeners
operator|.
name|remove
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|notifyValue (final ObjectAddress address, final Value<?> value)
specifier|public
name|void
name|notifyValue
parameter_list|(
specifier|final
name|ObjectAddress
name|address
parameter_list|,
specifier|final
name|Value
argument_list|<
name|?
argument_list|>
name|value
parameter_list|)
block|{
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataModel
operator|.
name|notifyDataChange
argument_list|(
name|address
operator|.
name|getASDUAddress
argument_list|()
argument_list|,
name|address
operator|.
name|getInformationObjectAddress
argument_list|()
argument_list|,
name|value
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

