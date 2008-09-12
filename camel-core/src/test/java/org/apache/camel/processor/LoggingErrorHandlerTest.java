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
name|ContextTestSupport
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_comment
comment|/**  * Exception throw inside Pipeline was not reported or handled when error  * handler is LoggingErrorHandler. (CAMEL-792)  */
end_comment

begin_class
DECL|class|LoggingErrorHandlerTest
specifier|public
class|class
name|LoggingErrorHandlerTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|log
specifier|private
name|MyLog
name|log
init|=
operator|new
name|MyLog
argument_list|()
decl_stmt|;
DECL|method|testLogException ()
specifier|public
name|void
name|testLogException
parameter_list|()
block|{
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:in"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|assertTrue
argument_list|(
literal|"Should have logged it"
argument_list|,
name|log
operator|.
name|logged
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// set to use our logger
name|errorHandler
argument_list|(
name|loggingErrorHandler
argument_list|(
name|log
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
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
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Hello World"
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**      * Just implement the Log interface, dont wanna mess with easymock or the like at current time      * for this simple test.      */
DECL|class|MyLog
specifier|private
class|class
name|MyLog
implements|implements
name|Log
block|{
DECL|field|logged
name|boolean
name|logged
decl_stmt|;
DECL|method|isDebugEnabled ()
specifier|public
name|boolean
name|isDebugEnabled
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|isErrorEnabled ()
specifier|public
name|boolean
name|isErrorEnabled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|isFatalEnabled ()
specifier|public
name|boolean
name|isFatalEnabled
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|isInfoEnabled ()
specifier|public
name|boolean
name|isInfoEnabled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|isTraceEnabled ()
specifier|public
name|boolean
name|isTraceEnabled
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|isWarnEnabled ()
specifier|public
name|boolean
name|isWarnEnabled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|trace (Object message)
specifier|public
name|void
name|trace
parameter_list|(
name|Object
name|message
parameter_list|)
block|{         }
DECL|method|trace (Object message, Throwable t)
specifier|public
name|void
name|trace
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{         }
DECL|method|debug (Object message)
specifier|public
name|void
name|debug
parameter_list|(
name|Object
name|message
parameter_list|)
block|{         }
DECL|method|debug (Object message, Throwable t)
specifier|public
name|void
name|debug
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{         }
DECL|method|info (Object message)
specifier|public
name|void
name|info
parameter_list|(
name|Object
name|message
parameter_list|)
block|{         }
DECL|method|info (Object message, Throwable t)
specifier|public
name|void
name|info
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|logged
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|warn (Object message)
specifier|public
name|void
name|warn
parameter_list|(
name|Object
name|message
parameter_list|)
block|{         }
DECL|method|warn (Object message, Throwable t)
specifier|public
name|void
name|warn
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{         }
DECL|method|error (Object message)
specifier|public
name|void
name|error
parameter_list|(
name|Object
name|message
parameter_list|)
block|{         }
DECL|method|error (Object message, Throwable t)
specifier|public
name|void
name|error
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{         }
DECL|method|fatal (Object message)
specifier|public
name|void
name|fatal
parameter_list|(
name|Object
name|message
parameter_list|)
block|{         }
DECL|method|fatal (Object message, Throwable t)
specifier|public
name|void
name|fatal
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{         }
block|}
block|}
end_class

end_unit

