begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|component
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|DefaultCamelContext
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
name|DefaultExchange
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
DECL|class|ApiMethodPropertiesHelperTest
specifier|public
class|class
name|ApiMethodPropertiesHelperTest
block|{
DECL|field|TEST_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|TEST_PREFIX
init|=
literal|"CamelTest."
decl_stmt|;
DECL|field|PROPERTY_1
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_1
init|=
name|TEST_PREFIX
operator|+
literal|"property1"
decl_stmt|;
DECL|field|PROPERTY_2
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_2
init|=
name|TEST_PREFIX
operator|+
literal|"property2"
decl_stmt|;
DECL|field|PROPERTY_3
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_3
init|=
name|TEST_PREFIX
operator|+
literal|"property3"
decl_stmt|;
DECL|field|PROPERTY_4
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_4
init|=
name|TEST_PREFIX
operator|+
literal|"property4"
decl_stmt|;
comment|// test camel case property names
DECL|field|PROPERTY_5
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_5
init|=
name|TEST_PREFIX
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|TEST_PREFIX
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|+
literal|"Property5"
decl_stmt|;
DECL|field|VALUE_1
specifier|private
specifier|static
specifier|final
name|String
name|VALUE_1
init|=
literal|"value1"
decl_stmt|;
DECL|field|VALUE_2
specifier|private
specifier|static
specifier|final
name|long
name|VALUE_2
init|=
literal|2
decl_stmt|;
DECL|field|VALUE_3
specifier|private
specifier|static
specifier|final
name|String
name|VALUE_3
init|=
literal|"value3"
decl_stmt|;
DECL|field|VALUE_4
specifier|private
specifier|static
specifier|final
name|String
name|VALUE_4
init|=
literal|"true"
decl_stmt|;
DECL|field|VALUE_5
specifier|private
specifier|static
specifier|final
name|String
name|VALUE_5
init|=
literal|"CamelCaseValue"
decl_stmt|;
DECL|field|propertiesHelper
specifier|private
specifier|static
name|ApiMethodPropertiesHelper
argument_list|<
name|TestComponentConfiguration
argument_list|>
name|propertiesHelper
init|=
operator|new
name|ApiMethodPropertiesHelper
argument_list|<
name|TestComponentConfiguration
argument_list|>
argument_list|(
name|TestComponentConfiguration
operator|.
name|class
argument_list|,
name|TEST_PREFIX
argument_list|)
block|{ }
decl_stmt|;
annotation|@
name|Test
DECL|method|testGetExchangeProperties ()
specifier|public
name|void
name|testGetExchangeProperties
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
operator|new
name|MockEndpoint
argument_list|()
decl_stmt|;
name|mock
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
specifier|final
name|HashMap
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
specifier|final
name|DefaultExchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|mock
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|PROPERTY_1
argument_list|,
name|VALUE_1
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|PROPERTY_2
argument_list|,
name|VALUE_2
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|PROPERTY_3
argument_list|,
name|VALUE_3
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|PROPERTY_4
argument_list|,
name|VALUE_4
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|PROPERTY_5
argument_list|,
name|VALUE_5
argument_list|)
expr_stmt|;
name|propertiesHelper
operator|.
name|getExchangeProperties
argument_list|(
name|exchange
argument_list|,
name|properties
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|properties
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetEndpointProperties ()
specifier|public
name|void
name|testGetEndpointProperties
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|HashMap
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
specifier|final
name|TestEndpointConfiguration
name|endpointConfiguration
init|=
operator|new
name|TestEndpointConfiguration
argument_list|()
decl_stmt|;
name|endpointConfiguration
operator|.
name|setProperty1
argument_list|(
name|VALUE_1
argument_list|)
expr_stmt|;
name|endpointConfiguration
operator|.
name|setProperty2
argument_list|(
name|VALUE_2
argument_list|)
expr_stmt|;
name|endpointConfiguration
operator|.
name|setProperty3
argument_list|(
name|VALUE_3
argument_list|)
expr_stmt|;
name|endpointConfiguration
operator|.
name|setProperty4
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|VALUE_4
argument_list|)
argument_list|)
expr_stmt|;
name|propertiesHelper
operator|.
name|getEndpointProperties
argument_list|(
name|endpointConfiguration
argument_list|,
name|properties
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|properties
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetEndpointPropertyNames ()
specifier|public
name|void
name|testGetEndpointPropertyNames
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|TestEndpointConfiguration
name|endpointConfiguration
init|=
operator|new
name|TestEndpointConfiguration
argument_list|()
decl_stmt|;
name|endpointConfiguration
operator|.
name|setProperty1
argument_list|(
name|VALUE_1
argument_list|)
expr_stmt|;
name|endpointConfiguration
operator|.
name|setProperty4
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|VALUE_4
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|propertiesHelper
operator|.
name|getEndpointPropertyNames
argument_list|(
name|endpointConfiguration
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetValidEndpointProperties ()
specifier|public
name|void
name|testGetValidEndpointProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|propertiesHelper
operator|.
name|getValidEndpointProperties
argument_list|(
operator|new
name|TestEndpointConfiguration
argument_list|()
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|class|TestComponentConfiguration
specifier|private
specifier|static
class|class
name|TestComponentConfiguration
block|{
DECL|field|property1
specifier|private
name|String
name|property1
decl_stmt|;
DECL|field|property2
specifier|private
name|Long
name|property2
decl_stmt|;
DECL|method|getProperty1 ()
specifier|public
name|String
name|getProperty1
parameter_list|()
block|{
return|return
name|property1
return|;
block|}
DECL|method|setProperty1 (String property1)
specifier|public
name|void
name|setProperty1
parameter_list|(
name|String
name|property1
parameter_list|)
block|{
name|this
operator|.
name|property1
operator|=
name|property1
expr_stmt|;
block|}
DECL|method|getProperty2 ()
specifier|public
name|long
name|getProperty2
parameter_list|()
block|{
return|return
name|property2
return|;
block|}
DECL|method|setProperty2 (Long property2)
specifier|public
name|void
name|setProperty2
parameter_list|(
name|Long
name|property2
parameter_list|)
block|{
name|this
operator|.
name|property2
operator|=
name|property2
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|class|TestEndpointConfiguration
specifier|private
specifier|static
class|class
name|TestEndpointConfiguration
extends|extends
name|TestComponentConfiguration
block|{
DECL|field|property3
specifier|private
name|String
name|property3
decl_stmt|;
DECL|field|property4
specifier|private
name|Boolean
name|property4
decl_stmt|;
DECL|method|getProperty3 ()
specifier|public
name|String
name|getProperty3
parameter_list|()
block|{
return|return
name|property3
return|;
block|}
DECL|method|setProperty3 (String property3)
specifier|public
name|void
name|setProperty3
parameter_list|(
name|String
name|property3
parameter_list|)
block|{
name|this
operator|.
name|property3
operator|=
name|property3
expr_stmt|;
block|}
DECL|method|getProperty4 ()
specifier|public
name|Boolean
name|getProperty4
parameter_list|()
block|{
return|return
name|property4
return|;
block|}
DECL|method|setProperty4 (Boolean property4)
specifier|public
name|void
name|setProperty4
parameter_list|(
name|Boolean
name|property4
parameter_list|)
block|{
name|this
operator|.
name|property4
operator|=
name|property4
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

