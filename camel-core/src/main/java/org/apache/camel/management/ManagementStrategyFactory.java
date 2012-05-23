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
name|spi
operator|.
name|ManagementStrategy
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
comment|/**  * Factory for creating {@link ManagementStrategy}  */
end_comment

begin_class
DECL|class|ManagementStrategyFactory
specifier|public
class|class
name|ManagementStrategyFactory
block|{
DECL|field|log
specifier|private
specifier|final
specifier|transient
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|create (CamelContext context, boolean disableJMX)
specifier|public
name|ManagementStrategy
name|create
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|boolean
name|disableJMX
parameter_list|)
block|{
name|ManagementStrategy
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|disableJMX
operator|||
name|Boolean
operator|.
name|getBoolean
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DISABLED
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"JMX is disabled."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|answer
operator|=
operator|new
name|ManagedManagementStrategy
argument_list|(
name|context
argument_list|,
operator|new
name|DefaultManagementAgent
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
comment|// must start it to ensure JMX works and can load needed Spring JARs
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|answer
argument_list|)
expr_stmt|;
comment|// prefer to have it at first strategy
name|context
operator|.
name|getLifecycleStrategies
argument_list|()
operator|.
name|add
argument_list|(
literal|0
argument_list|,
operator|new
name|DefaultManagementLifecycleStrategy
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"JMX enabled."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|answer
operator|=
literal|null
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot create JMX lifecycle strategy. Will fallback and disable JMX."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
operator|new
name|DefaultManagementStrategy
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

