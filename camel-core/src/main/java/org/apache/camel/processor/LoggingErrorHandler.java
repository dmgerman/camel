begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|LoggingLevel
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
name|util
operator|.
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * An {@link ErrorHandler} which uses commons-logging to dump the error  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|LoggingErrorHandler
specifier|public
class|class
name|LoggingErrorHandler
extends|extends
name|ErrorHandlerSupport
block|{
DECL|field|output
specifier|private
name|Processor
name|output
decl_stmt|;
DECL|field|log
specifier|private
name|Log
name|log
decl_stmt|;
DECL|field|level
specifier|private
name|LoggingLevel
name|level
decl_stmt|;
DECL|method|LoggingErrorHandler (Processor output)
specifier|public
name|LoggingErrorHandler
parameter_list|(
name|Processor
name|output
parameter_list|)
block|{
name|this
argument_list|(
name|output
argument_list|,
name|LogFactory
operator|.
name|getLog
argument_list|(
name|LoggingErrorHandler
operator|.
name|class
argument_list|)
argument_list|,
name|LoggingLevel
operator|.
name|INFO
argument_list|)
expr_stmt|;
block|}
DECL|method|LoggingErrorHandler (Processor output, Log log, LoggingLevel level)
specifier|public
name|LoggingErrorHandler
parameter_list|(
name|Processor
name|output
parameter_list|,
name|Log
name|log
parameter_list|,
name|LoggingLevel
name|level
parameter_list|)
block|{
name|this
operator|.
name|output
operator|=
name|output
expr_stmt|;
name|this
operator|.
name|log
operator|=
name|log
expr_stmt|;
name|this
operator|.
name|level
operator|=
name|level
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"LoggingErrorHandler["
operator|+
name|output
operator|+
literal|"]"
return|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Throwable
name|error
init|=
literal|null
decl_stmt|;
try|try
block|{
name|output
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// could also fail and set exception on the exchange itself
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|error
operator|=
name|exchange
operator|.
name|getException
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|error
operator|=
name|e
expr_stmt|;
block|}
if|if
condition|(
name|error
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|customProcessorForException
argument_list|(
name|exchange
argument_list|,
name|error
argument_list|)
condition|)
block|{
name|logError
argument_list|(
name|exchange
argument_list|,
name|error
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
comment|/**      * Returns the output processor      */
DECL|method|getOutput ()
specifier|public
name|Processor
name|getOutput
parameter_list|()
block|{
return|return
name|output
return|;
block|}
DECL|method|getLevel ()
specifier|public
name|LoggingLevel
name|getLevel
parameter_list|()
block|{
return|return
name|level
return|;
block|}
DECL|method|setLevel (LoggingLevel level)
specifier|public
name|void
name|setLevel
parameter_list|(
name|LoggingLevel
name|level
parameter_list|)
block|{
name|this
operator|.
name|level
operator|=
name|level
expr_stmt|;
block|}
DECL|method|getLog ()
specifier|public
name|Log
name|getLog
parameter_list|()
block|{
return|return
name|log
return|;
block|}
DECL|method|setLog (Log log)
specifier|public
name|void
name|setLog
parameter_list|(
name|Log
name|log
parameter_list|)
block|{
name|this
operator|.
name|log
operator|=
name|log
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|logError (Exchange exchange, Throwable e)
specifier|protected
name|void
name|logError
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|e
parameter_list|)
block|{
switch|switch
condition|(
name|level
condition|)
block|{
case|case
name|DEBUG
case|:
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|logMessage
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|ERROR
case|:
if|if
condition|(
name|log
operator|.
name|isErrorEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
name|logMessage
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|FATAL
case|:
if|if
condition|(
name|log
operator|.
name|isFatalEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|fatal
argument_list|(
name|logMessage
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|INFO
case|:
if|if
condition|(
name|log
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
name|logMessage
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|TRACE
case|:
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
name|logMessage
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|WARN
case|:
if|if
condition|(
name|log
operator|.
name|isWarnEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|logMessage
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
name|log
operator|.
name|error
argument_list|(
literal|"Unknown level: "
operator|+
name|level
operator|+
literal|" when trying to log exchange: "
operator|+
name|logMessage
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|logMessage (Exchange exchange, Throwable e)
specifier|protected
name|Object
name|logMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|e
parameter_list|)
block|{
return|return
name|e
operator|+
literal|" while processing exchange: "
operator|+
name|exchange
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

