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
name|impl
operator|.
name|ProcessorEndpoint
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
comment|/**  * @version $Revision: $  */
end_comment

begin_class
DECL|class|LogComponent
specifier|public
class|class
name|LogComponent
extends|extends
name|DefaultComponent
argument_list|<
name|Exchange
argument_list|>
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|LogComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
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
name|Logger
name|logger
init|=
operator|new
name|Logger
argument_list|(
name|remaining
argument_list|,
name|level
argument_list|)
decl_stmt|;
return|return
operator|new
name|ProcessorEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|logger
argument_list|)
return|;
block|}
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
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"level"
argument_list|)
decl_stmt|;
name|LoggingLevel
name|level
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|levelText
operator|!=
literal|null
condition|)
block|{
name|level
operator|=
name|LoggingLevel
operator|.
name|valueOf
argument_list|(
name|levelText
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|level
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not convert level text: "
operator|+
name|levelText
operator|+
literal|" to a valid logging level so defaulting to WARN"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|level
operator|==
literal|null
condition|)
block|{
name|level
operator|=
name|LoggingLevel
operator|.
name|INFO
expr_stmt|;
block|}
return|return
name|level
return|;
block|}
block|}
end_class

end_unit

