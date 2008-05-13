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
name|javax
operator|.
name|management
operator|.
name|ObjectName
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmxInstrumentationUsingPlatformMBSTest
specifier|public
class|class
name|JmxInstrumentationUsingPlatformMBSTest
extends|extends
name|JmxInstrumentationUsingPropertiesTest
block|{
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|InstrumentationAgentImpl
operator|.
name|SYSTEM_PROPERTY_JMX_USE_PLATFORM_MBS
argument_list|,
literal|"True"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
comment|// restore environment to original state
name|System
operator|.
name|setProperty
argument_list|(
name|InstrumentationAgentImpl
operator|.
name|SYSTEM_PROPERTY_JMX_USE_PLATFORM_MBS
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|testMBeanServerType ()
specifier|public
name|void
name|testMBeanServerType
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|iAgent
operator|.
name|getMBeanServer
argument_list|()
operator|.
name|getMBeanInfo
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"java.lang:type=OperatingSystem"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

