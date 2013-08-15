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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
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
name|builder
operator|.
name|RouteBuilder
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

begin_class
DECL|class|EndpointCompletionTest
specifier|public
class|class
name|EndpointCompletionTest
extends|extends
name|ManagementTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|EndpointCompletionTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|testEndpointCompletion ()
specifier|public
name|void
name|testEndpointCompletion
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"org.apache.camel:context=localhost/camel-1,type=context,name=\"camel-1\""
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|String
name|componentName
init|=
literal|"file"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|assertCompletion
argument_list|(
name|mbeanServer
argument_list|,
name|on
argument_list|,
name|componentName
argument_list|,
name|properties
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|assertCompletion
argument_list|(
name|mbeanServer
argument_list|,
name|on
argument_list|,
name|componentName
argument_list|,
name|properties
argument_list|,
literal|"po"
argument_list|)
expr_stmt|;
name|assertCompletion
argument_list|(
name|mbeanServer
argument_list|,
name|on
argument_list|,
name|componentName
argument_list|,
name|properties
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
name|assertCompletion
argument_list|(
name|mbeanServer
argument_list|,
name|on
argument_list|,
name|componentName
argument_list|,
name|properties
argument_list|,
literal|"/usr/local"
argument_list|)
expr_stmt|;
name|assertCompletion
argument_list|(
name|mbeanServer
argument_list|,
name|on
argument_list|,
name|componentName
argument_list|,
name|properties
argument_list|,
literal|"/usr/local/"
argument_list|)
expr_stmt|;
name|assertCompletion
argument_list|(
name|mbeanServer
argument_list|,
name|on
argument_list|,
name|componentName
argument_list|,
name|properties
argument_list|,
literal|"/usr/local/b"
argument_list|)
expr_stmt|;
block|}
DECL|method|testEndpointConfigurationJson ()
specifier|public
name|void
name|testEndpointConfigurationJson
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"org.apache.camel:context=localhost/camel-1,type=context,name=\"camel-1\""
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|assertParameterJsonSchema
argument_list|(
name|mbeanServer
argument_list|,
name|on
argument_list|,
literal|"bean"
argument_list|)
expr_stmt|;
name|assertParameterJsonSchema
argument_list|(
name|mbeanServer
argument_list|,
name|on
argument_list|,
literal|"timer"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|assertCompletion (MBeanServer mbeanServer, ObjectName on, String componentName, Map<String, Object> properties, String completionText)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|assertCompletion
parameter_list|(
name|MBeanServer
name|mbeanServer
parameter_list|,
name|ObjectName
name|on
parameter_list|,
name|String
name|componentName
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|String
name|completionText
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
index|[]
name|params
init|=
block|{
name|componentName
block|,
name|properties
block|,
name|completionText
block|}
decl_stmt|;
name|String
index|[]
name|signature
init|=
block|{
literal|"java.lang.String"
block|,
literal|"java.util.Map"
block|,
literal|"java.lang.String"
block|}
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|completions
init|=
name|assertIsInstanceOf
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"completeEndpointPath"
argument_list|,
name|params
argument_list|,
name|signature
argument_list|)
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Component "
operator|+
name|componentName
operator|+
literal|" with '"
operator|+
name|completionText
operator|+
literal|"' Returned: "
operator|+
name|completions
argument_list|)
expr_stmt|;
return|return
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|completions
return|;
block|}
DECL|method|assertParameterJsonSchema (MBeanServer mbeanServer, ObjectName on, String componentName)
specifier|private
name|String
name|assertParameterJsonSchema
parameter_list|(
name|MBeanServer
name|mbeanServer
parameter_list|,
name|ObjectName
name|on
parameter_list|,
name|String
name|componentName
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
index|[]
name|params
init|=
block|{
name|componentName
block|}
decl_stmt|;
name|String
index|[]
name|signature
init|=
block|{
literal|"java.lang.String"
block|}
decl_stmt|;
name|String
name|answer
init|=
name|assertIsInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"componentParameterJsonSchema"
argument_list|,
name|params
argument_list|,
name|signature
argument_list|)
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Component "
operator|+
name|componentName
operator|+
literal|" returned JSON: "
operator|+
name|answer
argument_list|)
expr_stmt|;
comment|// now lets validate that the generated JSON parses correctly
name|ObjectMapper
name|mapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
name|HashMap
name|data
init|=
name|mapper
operator|.
name|readValue
argument_list|(
name|answer
argument_list|,
name|HashMap
operator|.
name|class
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Read JSON: "
operator|+
name|data
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

