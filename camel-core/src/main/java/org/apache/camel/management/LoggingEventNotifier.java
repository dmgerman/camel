begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
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
name|spi
operator|.
name|CamelEvent
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
name|EventNotifierSupport
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
comment|/**  * Logging event notifier that only notifies if<tt>INFO</tt> log level has  * been configured for its logger.  */
end_comment

begin_class
DECL|class|LoggingEventNotifier
specifier|public
class|class
name|LoggingEventNotifier
extends|extends
name|EventNotifierSupport
block|{
DECL|field|log
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|LoggingEventNotifier
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|logName
specifier|private
name|String
name|logName
decl_stmt|;
DECL|method|notify (CamelEvent event)
specifier|public
name|void
name|notify
parameter_list|(
name|CamelEvent
name|event
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Event: {}"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isDisabled ()
specifier|public
name|boolean
name|isDisabled
parameter_list|()
block|{
return|return
operator|!
name|log
operator|.
name|isInfoEnabled
argument_list|()
return|;
block|}
DECL|method|isEnabled (CamelEvent event)
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|CamelEvent
name|event
parameter_list|)
block|{
return|return
name|log
operator|.
name|isInfoEnabled
argument_list|()
return|;
block|}
DECL|method|getLogName ()
specifier|public
name|String
name|getLogName
parameter_list|()
block|{
return|return
name|logName
return|;
block|}
comment|/**      * Sets the log name to use.      *      * @param logName a custom log name to use      */
DECL|method|setLogName (String logName)
specifier|public
name|void
name|setLogName
parameter_list|(
name|String
name|logName
parameter_list|)
block|{
name|this
operator|.
name|logName
operator|=
name|logName
expr_stmt|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|logName
operator|!=
literal|null
condition|)
block|{
name|log
operator|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|logName
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{     }
block|}
end_class

end_unit

