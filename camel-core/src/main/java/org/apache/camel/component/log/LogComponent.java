begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.log
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|log
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
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
name|DefaultComponent
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
name|model
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
name|ThroughputLogger
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
name|IntrospectionSupport
import|;
end_import

begin_comment
comment|/**  * The<a href="http://activemq.apache.org/camel/log.html">Log Component</a>  * to log message exchanges to the underlying logging mechanism.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|LogComponent
specifier|public
class|class
name|LogComponent
extends|extends
name|DefaultComponent
block|{
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|LoggingLevel
name|level
init|=
name|getLoggingLevel
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
name|Integer
name|groupSize
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"groupSize"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|Logger
name|logger
decl_stmt|;
if|if
condition|(
name|groupSize
operator|!=
literal|null
condition|)
block|{
name|logger
operator|=
operator|new
name|ThroughputLogger
argument_list|(
name|remaining
argument_list|,
name|level
argument_list|,
name|groupSize
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LogFormatter
name|formatter
init|=
operator|new
name|LogFormatter
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|formatter
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|logger
operator|=
operator|new
name|Logger
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|logger
operator|.
name|setLevel
argument_list|(
name|level
argument_list|)
expr_stmt|;
name|logger
operator|.
name|setFormatter
argument_list|(
name|formatter
argument_list|)
expr_stmt|;
block|}
name|LogEndpoint
name|endpoint
init|=
operator|new
name|LogEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
operator|new
name|LogEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|logger
argument_list|)
return|;
block|}
comment|/**      * Gets the logging level, will default to use INFO if no level parameter provided.      */
DECL|method|getLoggingLevel (Map parameters)
specifier|protected
name|LoggingLevel
name|getLoggingLevel
parameter_list|(
name|Map
name|parameters
parameter_list|)
block|{
name|String
name|levelText
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"level"
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|"INFO"
argument_list|)
decl_stmt|;
return|return
name|LoggingLevel
operator|.
name|valueOf
argument_list|(
name|levelText
operator|.
name|toUpperCase
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

