begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
operator|.
name|support
package|;
end_package

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

begin_import
import|import
name|org
operator|.
name|testcontainers
operator|.
name|containers
operator|.
name|output
operator|.
name|Slf4jLogConsumer
import|;
end_import

begin_class
DECL|class|ConsulContainerLogger
specifier|public
specifier|final
class|class
name|ConsulContainerLogger
extends|extends
name|Slf4jLogConsumer
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ConsulContainerLogger
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|ConsulContainerLogger ()
specifier|public
name|ConsulContainerLogger
parameter_list|()
block|{
name|super
argument_list|(
name|LOGGER
argument_list|)
expr_stmt|;
name|withPrefix
argument_list|(
literal|"consul"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

