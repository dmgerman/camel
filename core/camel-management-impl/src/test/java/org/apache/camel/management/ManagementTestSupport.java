begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|InstanceNotFoundException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanException
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
name|MBeanServerConnection
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
name|javax
operator|.
name|management
operator|.
name|ReflectionException
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
name|ContextTestSupport
import|;
end_import

begin_comment
comment|/**  * Base class for JMX tests.  */
end_comment

begin_class
DECL|class|ManagementTestSupport
specifier|public
specifier|abstract
class|class
name|ManagementTestSupport
extends|extends
name|ContextTestSupport
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
DECL|method|getMBeanServer ()
specifier|protected
name|MBeanServer
name|getMBeanServer
parameter_list|()
block|{
return|return
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getMBeanServer
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|invoke (MBeanServerConnection server, ObjectName name, String operationName)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|invoke
parameter_list|(
name|MBeanServerConnection
name|server
parameter_list|,
name|ObjectName
name|name
parameter_list|,
name|String
name|operationName
parameter_list|)
throws|throws
name|InstanceNotFoundException
throws|,
name|MBeanException
throws|,
name|ReflectionException
throws|,
name|IOException
block|{
return|return
operator|(
name|T
operator|)
name|server
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
name|operationName
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|invoke (MBeanServerConnection server, ObjectName name, String operationName, Object params[], String signature[])
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|invoke
parameter_list|(
name|MBeanServerConnection
name|server
parameter_list|,
name|ObjectName
name|name
parameter_list|,
name|String
name|operationName
parameter_list|,
name|Object
name|params
index|[]
parameter_list|,
name|String
name|signature
index|[]
parameter_list|)
throws|throws
name|InstanceNotFoundException
throws|,
name|MBeanException
throws|,
name|ReflectionException
throws|,
name|IOException
block|{
return|return
operator|(
name|T
operator|)
name|server
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
name|operationName
argument_list|,
name|params
argument_list|,
name|signature
argument_list|)
return|;
block|}
block|}
end_class

end_unit

