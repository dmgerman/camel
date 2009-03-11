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
name|java
operator|.
name|util
operator|.
name|List
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
name|impl
operator|.
name|ServiceSupport
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
name|ExchangeHelper
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
comment|/**  * Implements try/catch/finally type processing  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|TryProcessor
specifier|public
class|class
name|TryProcessor
extends|extends
name|ServiceSupport
implements|implements
name|Processor
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|TryProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|tryProcessor
specifier|private
specifier|final
name|Processor
name|tryProcessor
decl_stmt|;
DECL|field|catchClauses
specifier|private
specifier|final
name|List
argument_list|<
name|CatchProcessor
argument_list|>
name|catchClauses
decl_stmt|;
DECL|field|finallyProcessor
specifier|private
specifier|final
name|Processor
name|finallyProcessor
decl_stmt|;
DECL|method|TryProcessor (Processor tryProcessor, List<CatchProcessor> catchClauses, Processor finallyProcessor)
specifier|public
name|TryProcessor
parameter_list|(
name|Processor
name|tryProcessor
parameter_list|,
name|List
argument_list|<
name|CatchProcessor
argument_list|>
name|catchClauses
parameter_list|,
name|Processor
name|finallyProcessor
parameter_list|)
block|{
name|this
operator|.
name|tryProcessor
operator|=
name|tryProcessor
expr_stmt|;
name|this
operator|.
name|catchClauses
operator|=
name|catchClauses
expr_stmt|;
name|this
operator|.
name|finallyProcessor
operator|=
name|finallyProcessor
expr_stmt|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|finallyText
init|=
operator|(
name|finallyProcessor
operator|==
literal|null
operator|)
condition|?
literal|""
else|:
literal|" Finally {"
operator|+
name|finallyProcessor
operator|+
literal|"}"
decl_stmt|;
return|return
literal|"Try {"
operator|+
name|tryProcessor
operator|+
literal|"} "
operator|+
name|catchClauses
operator|+
name|finallyText
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
name|Exception
name|e
decl_stmt|;
comment|// try processor first
try|try
block|{
name|tryProcessor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|e
operator|=
name|exchange
operator|.
name|getException
argument_list|()
expr_stmt|;
comment|// Ignore it if it was handled by the dead letter channel.
if|if
condition|(
name|e
operator|!=
literal|null
operator|&&
name|DeadLetterChannel
operator|.
name|isFailureHandled
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|e
operator|=
literal|null
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|e
operator|=
name|ex
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|// handle any exception occured during the try processor
try|try
block|{
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Caught exception while processing exchange."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|handleException
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// and run finally
comment|// notice its always executed since we always enter the try block
name|processFinally
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
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
name|tryProcessor
argument_list|,
name|catchClauses
argument_list|,
name|finallyProcessor
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
name|tryProcessor
argument_list|,
name|catchClauses
argument_list|,
name|finallyProcessor
argument_list|)
expr_stmt|;
block|}
DECL|method|handleException (Exchange exchange, Throwable e)
specifier|protected
name|void
name|handleException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|e
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|CatchProcessor
name|catchClause
range|:
name|catchClauses
control|)
block|{
if|if
condition|(
name|catchClause
operator|.
name|catches
argument_list|(
name|e
argument_list|)
condition|)
block|{
comment|// lets attach the exception to the exchange
name|Exchange
name|localExchange
init|=
name|exchange
operator|.
name|copy
argument_list|()
decl_stmt|;
name|localExchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// give the rest of the pipeline another chance
name|localExchange
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// do not catch any exception here, let it propagate up
name|catchClause
operator|.
name|process
argument_list|(
name|localExchange
argument_list|)
expr_stmt|;
name|localExchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|)
expr_stmt|;
name|ExchangeHelper
operator|.
name|copyResults
argument_list|(
name|exchange
argument_list|,
name|localExchange
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
DECL|method|processFinally (Exchange exchange)
specifier|protected
name|void
name|processFinally
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|finallyProcessor
operator|!=
literal|null
condition|)
block|{
name|Exception
name|lastException
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// do not catch any exception here, let it propagate up
name|finallyProcessor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|lastException
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

