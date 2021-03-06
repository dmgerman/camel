begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jgroups
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jgroups
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
name|AsyncCallback
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
name|AsyncProcessor
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
name|Exchange
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
name|support
operator|.
name|AsyncProcessorConverterHelper
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
name|jgroups
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|ReceiverAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|View
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

begin_comment
comment|/**  * Implementation of JGroups message receiver ({@code org.jgroups.Receiver}) wrapping incoming messages into Camel  * exchanges. Used by {@link JGroupsConsumer}.  */
end_comment

begin_class
DECL|class|CamelJGroupsReceiver
specifier|public
class|class
name|CamelJGroupsReceiver
extends|extends
name|ReceiverAdapter
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelJGroupsReceiver
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|JGroupsEndpoint
name|endpoint
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|AsyncProcessor
name|processor
decl_stmt|;
DECL|method|CamelJGroupsReceiver (JGroupsEndpoint endpoint, Processor processor)
specifier|public
name|CamelJGroupsReceiver
parameter_list|(
name|JGroupsEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|endpoint
argument_list|,
literal|"endpoint"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|processor
argument_list|,
literal|"processor"
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|viewAccepted (View view)
specifier|public
name|void
name|viewAccepted
parameter_list|(
name|View
name|view
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|isEnableViewMessages
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|view
argument_list|)
decl_stmt|;
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Processing view: {}"
argument_list|,
name|view
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// noop
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|JGroupsException
argument_list|(
literal|"Error in consumer while dispatching exchange containing view "
operator|+
name|view
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Option enableViewMessages is set to false. Skipping processing of the view: {}"
argument_list|,
name|view
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|receive (Message message)
specifier|public
name|void
name|receive
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|message
argument_list|)
decl_stmt|;
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Processing message: {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// noop
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|JGroupsException
argument_list|(
literal|"Error in consumer while dispatching exchange containing message "
operator|+
name|message
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

