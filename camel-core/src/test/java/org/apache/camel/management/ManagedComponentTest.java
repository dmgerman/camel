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
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|ComponentVerifier
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
name|VerifiableComponent
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
name|component
operator|.
name|direct
operator|.
name|DirectComponent
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
name|component
operator|.
name|extension
operator|.
name|verifier
operator|.
name|DefaultComponentVerifierExtension
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
name|component
operator|.
name|extension
operator|.
name|verifier
operator|.
name|ResultBuilder
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

begin_class
DECL|class|ManagedComponentTest
specifier|public
class|class
name|ManagedComponentTest
extends|extends
name|ManagementTestSupport
block|{
DECL|field|VERIFY_SIGNATURE
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|VERIFY_SIGNATURE
init|=
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"java.util.Map"
block|}
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
literal|"my-verifiable-component"
argument_list|,
operator|new
name|MyVerifiableComponent
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"direct"
argument_list|,
operator|new
name|DirectComponent
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|testVerifySupported ()
specifier|public
name|void
name|testVerifySupported
parameter_list|()
throws|throws
name|Exception
block|{
comment|// JMX tests don't work well on AIX CI servers (hangs them)
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
return|return;
block|}
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|on
decl_stmt|;
name|on
operator|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=components,name=\"my-verifiable-component\""
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|invoke
argument_list|(
name|mbeanServer
argument_list|,
name|on
argument_list|,
literal|"isVerifySupported"
argument_list|)
argument_list|)
expr_stmt|;
name|on
operator|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=components,name=\"direct\""
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|invoke
argument_list|(
name|mbeanServer
argument_list|,
name|on
argument_list|,
literal|"isVerifySupported"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testVerify ()
specifier|public
name|void
name|testVerify
parameter_list|()
throws|throws
name|Exception
block|{
comment|// JMX tests don't work well on AIX CI servers (hangs them)
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
return|return;
block|}
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|on
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=components,name=\"my-verifiable-component\""
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|invoke
argument_list|(
name|mbeanServer
argument_list|,
name|on
argument_list|,
literal|"isVerifySupported"
argument_list|)
argument_list|)
expr_stmt|;
name|ComponentVerifier
operator|.
name|Result
name|res
decl_stmt|;
comment|// check lowercase
name|res
operator|=
name|invoke
argument_list|(
name|mbeanServer
argument_list|,
name|on
argument_list|,
literal|"verify"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"connectivity"
block|,
name|Collections
operator|.
name|emptyMap
argument_list|()
block|}
argument_list|,
name|VERIFY_SIGNATURE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ComponentVerifier
operator|.
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|res
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ComponentVerifier
operator|.
name|Scope
operator|.
name|CONNECTIVITY
argument_list|,
name|res
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
comment|// check mixed case
name|res
operator|=
name|invoke
argument_list|(
name|mbeanServer
argument_list|,
name|on
argument_list|,
literal|"verify"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"ConnEctivIty"
block|,
name|Collections
operator|.
name|emptyMap
argument_list|()
block|}
argument_list|,
name|VERIFY_SIGNATURE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ComponentVerifier
operator|.
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|res
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ComponentVerifier
operator|.
name|Scope
operator|.
name|CONNECTIVITY
argument_list|,
name|res
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
comment|// check uppercase
name|res
operator|=
name|invoke
argument_list|(
name|mbeanServer
argument_list|,
name|on
argument_list|,
literal|"verify"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"PARAMETERS"
block|,
name|Collections
operator|.
name|emptyMap
argument_list|()
block|}
argument_list|,
name|VERIFY_SIGNATURE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ComponentVerifier
operator|.
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|res
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ComponentVerifier
operator|.
name|Scope
operator|.
name|PARAMETERS
argument_list|,
name|res
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// ***********************************
comment|//
comment|// ***********************************
DECL|class|MyVerifiableComponent
specifier|private
specifier|static
class|class
name|MyVerifiableComponent
extends|extends
name|DefaultComponent
implements|implements
name|VerifiableComponent
block|{
annotation|@
name|Override
DECL|method|getVerifier ()
specifier|public
name|ComponentVerifier
name|getVerifier
parameter_list|()
block|{
return|return
operator|new
name|DefaultComponentVerifierExtension
argument_list|(
literal|"my-verifiable-component"
argument_list|,
name|getCamelContext
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Result
name|verifyConnectivity
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
return|return
name|ResultBuilder
operator|.
name|withStatusAndScope
argument_list|(
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|Scope
operator|.
name|CONNECTIVITY
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Result
name|verifyParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
return|return
name|ResultBuilder
operator|.
name|withStatusAndScope
argument_list|(
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|Scope
operator|.
name|PARAMETERS
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

