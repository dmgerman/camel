begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|processor
operator|.
name|Logger
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
name|processor
operator|.
name|LoggingErrorHandler
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
name|processor
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
comment|/**  * Uses the {@link Logger} as an error handler  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|LoggingErrorHandlerBuilder
specifier|public
class|class
name|LoggingErrorHandlerBuilder
extends|extends
name|ErrorHandlerBuilderSupport
block|{
DECL|field|log
specifier|private
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|Logger
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|level
specifier|private
name|LoggingLevel
name|level
init|=
name|LoggingLevel
operator|.
name|INFO
decl_stmt|;
DECL|method|LoggingErrorHandlerBuilder ()
specifier|public
name|LoggingErrorHandlerBuilder
parameter_list|()
block|{     }
DECL|method|LoggingErrorHandlerBuilder (Log log)
specifier|public
name|LoggingErrorHandlerBuilder
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
DECL|method|LoggingErrorHandlerBuilder (Log log, LoggingLevel level)
specifier|public
name|LoggingErrorHandlerBuilder
parameter_list|(
name|Log
name|log
parameter_list|,
name|LoggingLevel
name|level
parameter_list|)
block|{
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
DECL|method|copy ()
specifier|public
name|ErrorHandlerBuilder
name|copy
parameter_list|()
block|{
name|LoggingErrorHandlerBuilder
name|answer
init|=
operator|new
name|LoggingErrorHandlerBuilder
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setLog
argument_list|(
name|getLog
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setLevel
argument_list|(
name|getLevel
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|createErrorHandler (Processor processor)
specifier|public
name|Processor
name|createErrorHandler
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|LoggingErrorHandler
name|handler
init|=
operator|new
name|LoggingErrorHandler
argument_list|(
name|processor
argument_list|,
name|log
argument_list|,
name|level
argument_list|)
decl_stmt|;
name|configure
argument_list|(
name|handler
argument_list|)
expr_stmt|;
return|return
name|handler
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
block|}
end_class

end_unit

