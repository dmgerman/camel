begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ignite
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ignite
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|Ignite
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|configuration
operator|.
name|IgniteConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|events
operator|.
name|EventType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|spi
operator|.
name|discovery
operator|.
name|tcp
operator|.
name|TcpDiscoverySpi
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|spi
operator|.
name|discovery
operator|.
name|tcp
operator|.
name|ipfinder
operator|.
name|TcpDiscoveryIpFinder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|spi
operator|.
name|discovery
operator|.
name|tcp
operator|.
name|ipfinder
operator|.
name|vm
operator|.
name|TcpDiscoveryVmIpFinder
import|;
end_import

begin_class
DECL|class|AbstractIgniteTest
specifier|public
specifier|abstract
class|class
name|AbstractIgniteTest
extends|extends
name|CamelTestSupport
block|{
comment|/** Ip finder for TCP discovery. */
DECL|field|LOCAL_IP_FINDER
specifier|private
specifier|static
specifier|final
name|TcpDiscoveryIpFinder
name|LOCAL_IP_FINDER
init|=
operator|new
name|TcpDiscoveryVmIpFinder
argument_list|(
literal|false
argument_list|)
block|{
block|{
name|setAddresses
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
literal|"127.0.0.1:47500..47509"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
DECL|field|ignite
specifier|private
name|Ignite
name|ignite
decl_stmt|;
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
name|getScheme
argument_list|()
argument_list|,
name|createComponent
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|createConfiguration ()
specifier|protected
name|IgniteConfiguration
name|createConfiguration
parameter_list|()
block|{
name|IgniteConfiguration
name|config
init|=
operator|new
name|IgniteConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|setGridName
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setIncludeEventTypes
argument_list|(
name|EventType
operator|.
name|EVT_JOB_FINISHED
argument_list|,
name|EventType
operator|.
name|EVT_JOB_RESULTED
argument_list|)
expr_stmt|;
name|config
operator|.
name|setDiscoverySpi
argument_list|(
operator|new
name|TcpDiscoverySpi
argument_list|()
operator|.
name|setIpFinder
argument_list|(
name|LOCAL_IP_FINDER
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
DECL|method|getScheme ()
specifier|protected
specifier|abstract
name|String
name|getScheme
parameter_list|()
function_decl|;
DECL|method|createComponent ()
specifier|protected
specifier|abstract
name|AbstractIgniteComponent
name|createComponent
parameter_list|()
function_decl|;
DECL|method|ignite ()
specifier|protected
name|Ignite
name|ignite
parameter_list|()
block|{
if|if
condition|(
name|ignite
operator|==
literal|null
condition|)
block|{
name|ignite
operator|=
name|context
operator|.
name|getComponent
argument_list|(
name|getScheme
argument_list|()
argument_list|,
name|AbstractIgniteComponent
operator|.
name|class
argument_list|)
operator|.
name|getIgnite
argument_list|()
expr_stmt|;
block|}
return|return
name|ignite
return|;
block|}
block|}
end_class

end_unit

