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
name|CamelContext
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
name|processor
operator|.
name|exceptionpolicy
operator|.
name|ExceptionPolicyStrategy
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
name|CamelLogger
import|;
end_import

begin_comment
comment|/**  * An {@link ErrorHandler} which uses commons-logging to dump the error  *  * @version   */
end_comment

begin_class
DECL|class|LoggingErrorHandler
specifier|public
class|class
name|LoggingErrorHandler
extends|extends
name|DefaultErrorHandler
block|{
comment|/**      * Creates the logging error handler.      *      * @param camelContext            the camel context      * @param output                  outer processor that should use this logging error handler      * @param logger                  logger to use for logging failures      * @param redeliveryPolicy        redelivery policy      * @param exceptionPolicyStrategy strategy for onException handling      */
DECL|method|LoggingErrorHandler (CamelContext camelContext, Processor output, CamelLogger logger, RedeliveryPolicy redeliveryPolicy, ExceptionPolicyStrategy exceptionPolicyStrategy)
specifier|public
name|LoggingErrorHandler
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|output
parameter_list|,
name|CamelLogger
name|logger
parameter_list|,
name|RedeliveryPolicy
name|redeliveryPolicy
parameter_list|,
name|ExceptionPolicyStrategy
name|exceptionPolicyStrategy
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|,
name|output
argument_list|,
name|logger
argument_list|,
literal|null
argument_list|,
name|redeliveryPolicy
argument_list|,
name|exceptionPolicyStrategy
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
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
block|}
end_class

end_unit

