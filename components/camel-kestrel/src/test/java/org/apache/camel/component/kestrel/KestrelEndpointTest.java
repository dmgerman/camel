begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kestrel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kestrel
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|CamelTestSupport
import|;
end_import

begin_class
DECL|class|KestrelEndpointTest
specifier|public
class|class
name|KestrelEndpointTest
extends|extends
name|CamelTestSupport
block|{
DECL|class|TestCase
specifier|private
specifier|static
specifier|final
class|class
name|TestCase
block|{
DECL|field|uri
name|String
name|uri
decl_stmt|;
DECL|field|addresses
name|String
index|[]
name|addresses
decl_stmt|;
DECL|field|queue
name|String
name|queue
decl_stmt|;
DECL|field|waitTimeMs
name|Integer
name|waitTimeMs
decl_stmt|;
DECL|field|concurrentConsumers
name|Integer
name|concurrentConsumers
decl_stmt|;
DECL|method|TestCase (String uri, String[] addresses, String queue, Integer waitTimeMs, Integer concurrentConsumers)
name|TestCase
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
index|[]
name|addresses
parameter_list|,
name|String
name|queue
parameter_list|,
name|Integer
name|waitTimeMs
parameter_list|,
name|Integer
name|concurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
name|this
operator|.
name|addresses
operator|=
name|addresses
expr_stmt|;
name|this
operator|.
name|queue
operator|=
name|queue
expr_stmt|;
name|this
operator|.
name|waitTimeMs
operator|=
name|waitTimeMs
expr_stmt|;
name|this
operator|.
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
block|}
block|}
DECL|field|TEST_CASES
specifier|private
specifier|static
specifier|final
name|TestCase
index|[]
name|TEST_CASES
init|=
operator|new
name|TestCase
index|[]
block|{
operator|new
name|TestCase
argument_list|(
literal|"kestrel:///queuename"
argument_list|,
literal|null
argument_list|,
literal|"queuename"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
block|,
operator|new
name|TestCase
argument_list|(
literal|"kestrel://queuename?concurrentConsumers=44"
argument_list|,
literal|null
argument_list|,
literal|"queuename"
argument_list|,
literal|null
argument_list|,
literal|44
argument_list|)
block|,
operator|new
name|TestCase
argument_list|(
literal|"kestrel://queuename?waitTimeMs=4567"
argument_list|,
literal|null
argument_list|,
literal|"queuename"
argument_list|,
literal|4567
argument_list|,
literal|null
argument_list|)
block|,
operator|new
name|TestCase
argument_list|(
literal|"kestrel://localhost/queuename"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"localhost"
block|}
argument_list|,
literal|"queuename"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
block|,
operator|new
name|TestCase
argument_list|(
literal|"kestrel://127.0.0.1:22133,localhost:22134/queuename?waitTimeMs=4567&concurrentConsumers=99"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"127.0.0.1:22133"
block|,
literal|"localhost:22134"
block|}
argument_list|,
literal|"queuename"
argument_list|,
literal|4567
argument_list|,
literal|99
argument_list|)
block|,
operator|new
name|TestCase
argument_list|(
literal|"kestrel://127.0.0.1:22133/queuename?concurrentConsumers=10&waitTimeMs=4567"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"127.0.0.1:22133"
block|}
argument_list|,
literal|"queuename"
argument_list|,
literal|4567
argument_list|,
literal|10
argument_list|)
block|,
operator|new
name|TestCase
argument_list|(
literal|"kestrel://localhost/queuename?concurrentConsumers=20"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"localhost"
block|}
argument_list|,
literal|"queuename"
argument_list|,
literal|null
argument_list|,
literal|20
argument_list|)
block|,
operator|new
name|TestCase
argument_list|(
literal|"kestrel://localhost,otherhost/queuename?waitTimeMs=4567"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"localhost"
block|,
literal|"otherhost"
block|}
argument_list|,
literal|"queuename"
argument_list|,
literal|4567
argument_list|,
literal|null
argument_list|)
block|,
operator|new
name|TestCase
argument_list|(
literal|"kestrel://localhost:22133,otherhost/queuename?waitTimeMs=4567&concurrentConsumers=5"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"localhost:22133"
block|,
literal|"otherhost"
block|}
argument_list|,
literal|"queuename"
argument_list|,
literal|4567
argument_list|,
literal|5
argument_list|)
block|,
operator|new
name|TestCase
argument_list|(
literal|"kestrel://localhost,otherhost:22133/queuename?waitTimeMs=4567"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"localhost"
block|,
literal|"otherhost:22133"
block|}
argument_list|,
literal|"queuename"
argument_list|,
literal|4567
argument_list|,
literal|null
argument_list|)
block|,     }
decl_stmt|;
DECL|field|baseConfiguration
specifier|private
name|KestrelConfiguration
name|baseConfiguration
decl_stmt|;
DECL|field|kestrelComponent
specifier|private
name|KestrelComponent
name|kestrelComponent
decl_stmt|;
DECL|method|testEndpoints ()
specifier|public
name|void
name|testEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|TestCase
name|testCase
range|:
name|TEST_CASES
control|)
block|{
name|KestrelEndpoint
name|endpoint
init|=
operator|(
name|KestrelEndpoint
operator|)
name|resolveMandatoryEndpoint
argument_list|(
name|testCase
operator|.
name|uri
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getQueue("
operator|+
name|testCase
operator|.
name|uri
operator|+
literal|")"
argument_list|,
name|testCase
operator|.
name|queue
argument_list|,
name|endpoint
operator|.
name|getQueue
argument_list|()
argument_list|)
expr_stmt|;
name|KestrelConfiguration
name|configuration
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|testCase
operator|.
name|addresses
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
literal|"getAddresses("
operator|+
name|testCase
operator|.
name|uri
operator|+
literal|")"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|testCase
operator|.
name|addresses
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|configuration
operator|.
name|getAddresses
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"getAddresses("
operator|+
name|testCase
operator|.
name|uri
operator|+
literal|")"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|baseConfiguration
operator|.
name|getAddresses
argument_list|()
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|configuration
operator|.
name|getAddresses
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|testCase
operator|.
name|waitTimeMs
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
literal|"getWaitTimeMs("
operator|+
name|testCase
operator|.
name|uri
operator|+
literal|")"
argument_list|,
operator|(
name|Object
operator|)
name|testCase
operator|.
name|waitTimeMs
argument_list|,
name|configuration
operator|.
name|getWaitTimeMs
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"getWaitTimeMs("
operator|+
name|testCase
operator|.
name|uri
operator|+
literal|")"
argument_list|,
name|baseConfiguration
operator|.
name|getWaitTimeMs
argument_list|()
argument_list|,
name|configuration
operator|.
name|getWaitTimeMs
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|testCase
operator|.
name|concurrentConsumers
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
literal|"getConcurrentConsumers("
operator|+
name|testCase
operator|.
name|uri
operator|+
literal|")"
argument_list|,
operator|(
name|Object
operator|)
name|testCase
operator|.
name|concurrentConsumers
argument_list|,
name|configuration
operator|.
name|getConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"getConcurrentConsumers("
operator|+
name|testCase
operator|.
name|uri
operator|+
literal|")"
argument_list|,
name|baseConfiguration
operator|.
name|getConcurrentConsumers
argument_list|()
argument_list|,
name|configuration
operator|.
name|getConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|baseConfiguration
operator|=
operator|new
name|KestrelConfiguration
argument_list|()
expr_stmt|;
name|baseConfiguration
operator|.
name|setAddresses
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"base:12345"
block|}
argument_list|)
expr_stmt|;
name|baseConfiguration
operator|.
name|setWaitTimeMs
argument_list|(
literal|9999
argument_list|)
expr_stmt|;
name|baseConfiguration
operator|.
name|setConcurrentConsumers
argument_list|(
literal|11
argument_list|)
expr_stmt|;
name|kestrelComponent
operator|=
operator|new
name|KestrelComponent
argument_list|()
expr_stmt|;
name|kestrelComponent
operator|.
name|setConfiguration
argument_list|(
name|baseConfiguration
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"kestrel"
argument_list|,
name|kestrelComponent
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
block|}
end_class

end_unit

