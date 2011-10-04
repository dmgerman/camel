begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|config
package|;
end_package

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
name|TestSupport
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
name|model
operator|.
name|FromDefinition
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
name|model
operator|.
name|ModelCamelContext
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
name|model
operator|.
name|ProcessorDefinition
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
name|model
operator|.
name|RouteDefinition
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|XmlConfigTestSupport
specifier|public
class|class
name|XmlConfigTestSupport
extends|extends
name|TestSupport
block|{
DECL|field|LOG
specifier|protected
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelContextFactoryBeanTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|assertValidContext (CamelContext context)
specifier|protected
name|void
name|assertValidContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"No context found!"
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routes
init|=
operator|(
operator|(
name|ModelCamelContext
operator|)
name|context
operator|)
operator|.
name|getRouteDefinitions
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found routes: "
operator|+
name|routes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"One Route should be found"
argument_list|,
literal|1
argument_list|,
name|routes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|RouteDefinition
name|route
range|:
name|routes
control|)
block|{
name|List
argument_list|<
name|FromDefinition
argument_list|>
name|inputs
init|=
name|route
operator|.
name|getInputs
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number of inputs"
argument_list|,
literal|1
argument_list|,
name|inputs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|FromDefinition
name|fromType
init|=
name|inputs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"from URI"
argument_list|,
literal|"seda:test.a"
argument_list|,
name|fromType
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputs
init|=
name|route
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number of outputs"
argument_list|,
literal|1
argument_list|,
name|outputs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

