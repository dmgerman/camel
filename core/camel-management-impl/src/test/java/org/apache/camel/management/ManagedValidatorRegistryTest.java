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
name|util
operator|.
name|ArrayList
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
name|Set
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
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
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
name|Message
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
name|ValidationException
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
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|DataType
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
name|Validator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
DECL|class|ManagedValidatorRegistryTest
specifier|public
class|class
name|ManagedValidatorRegistryTest
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
name|ManagedValidatorRegistryTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testManageValidatorRegistry ()
specifier|public
name|void
name|testManageValidatorRegistry
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
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// get the stats for the route
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|set
init|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"*:type=services,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ObjectName
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|set
argument_list|)
decl_stmt|;
name|ObjectName
name|on
init|=
literal|null
decl_stmt|;
for|for
control|(
name|ObjectName
name|name
range|:
name|list
control|)
block|{
if|if
condition|(
name|name
operator|.
name|getCanonicalName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"DefaultValidatorRegistry"
argument_list|)
condition|)
block|{
name|on
operator|=
name|name
expr_stmt|;
break|break;
block|}
block|}
name|assertNotNull
argument_list|(
literal|"Should have found ValidatorRegistry"
argument_list|,
name|on
argument_list|)
expr_stmt|;
name|Integer
name|max
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"MaximumCacheSize"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1000
argument_list|,
name|max
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Integer
name|current
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"Size"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|current
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|current
operator|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"StaticSize"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|current
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|current
operator|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"DynamicSize"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|current
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|source
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"Source"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|source
operator|.
name|startsWith
argument_list|(
literal|"ValidatorRegistry"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|source
operator|.
name|endsWith
argument_list|(
literal|"capacity: 1000"
argument_list|)
argument_list|)
expr_stmt|;
name|TabularData
name|data
init|=
operator|(
name|TabularData
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"listValidators"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|row
range|:
name|data
operator|.
name|values
argument_list|()
control|)
block|{
name|CompositeData
name|composite
init|=
operator|(
name|CompositeData
operator|)
name|row
decl_stmt|;
name|String
name|type
init|=
operator|(
name|String
operator|)
name|composite
operator|.
name|get
argument_list|(
literal|"type"
argument_list|)
decl_stmt|;
name|String
name|description
init|=
operator|(
name|String
operator|)
name|composite
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
decl_stmt|;
name|boolean
name|isStatic
init|=
operator|(
name|boolean
operator|)
name|composite
operator|.
name|get
argument_list|(
literal|"static"
argument_list|)
decl_stmt|;
name|boolean
name|isDynamic
init|=
operator|(
name|boolean
operator|)
name|composite
operator|.
name|get
argument_list|(
literal|"dynamic"
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"[{}][{}][{}][{}]"
argument_list|,
name|type
argument_list|,
name|isStatic
argument_list|,
name|isDynamic
argument_list|,
name|description
argument_list|)
expr_stmt|;
if|if
condition|(
name|description
operator|.
name|startsWith
argument_list|(
literal|"ProcessorValidator"
argument_list|)
condition|)
block|{
if|if
condition|(
name|description
operator|.
name|contains
argument_list|(
literal|"direct://transformer"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"xml:foo"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|description
operator|.
name|contains
argument_list|(
literal|"validate(simple{${body}} is not null"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"json:test"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
literal|"Unexpected validator:"
operator|+
name|description
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|description
operator|.
name|startsWith
argument_list|(
literal|"MyValidator"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"custom"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
literal|"Unexpected validator:"
operator|+
name|description
argument_list|)
expr_stmt|;
block|}
block|}
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
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
name|validator
argument_list|()
operator|.
name|type
argument_list|(
literal|"xml:foo"
argument_list|)
operator|.
name|withUri
argument_list|(
literal|"direct:transformer"
argument_list|)
expr_stmt|;
name|validator
argument_list|()
operator|.
name|type
argument_list|(
literal|"json:test"
argument_list|)
operator|.
name|withExpression
argument_list|(
name|body
argument_list|()
operator|.
name|isNotNull
argument_list|()
argument_list|)
expr_stmt|;
name|validator
argument_list|()
operator|.
name|type
argument_list|(
literal|"custom"
argument_list|)
operator|.
name|withJava
argument_list|(
name|MyValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyValidator
specifier|public
specifier|static
class|class
name|MyValidator
extends|extends
name|Validator
block|{
annotation|@
name|Override
DECL|method|validate (Message message, DataType type)
specifier|public
name|void
name|validate
parameter_list|(
name|Message
name|message
parameter_list|,
name|DataType
name|type
parameter_list|)
throws|throws
name|ValidationException
block|{
comment|// empty
block|}
block|}
block|}
end_class

end_unit

