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
comment|/**  * This module contains test cases that verifies jmx system property uses.  *  * @version   */
end_comment

begin_class
DECL|class|JmxInstrumentationUsingPropertiesTest
specifier|public
class|class
name|JmxInstrumentationUsingPropertiesTest
extends|extends
name|JmxInstrumentationUsingDefaultsTest
block|{
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
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
name|domainName
operator|=
literal|"org.apache.camel-properties"
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DOMAIN
argument_list|,
name|domainName
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|MBEAN_DOMAIN
argument_list|,
name|domainName
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|testMBeanServerType ()
specifier|public
name|void
name|testMBeanServerType
parameter_list|()
throws|throws
name|Exception
block|{
comment|// JMX tests dont work well on AIX CI servers (hangs them)
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
comment|// default is platform mbs
name|assertNotNull
argument_list|(
name|mbsc
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

