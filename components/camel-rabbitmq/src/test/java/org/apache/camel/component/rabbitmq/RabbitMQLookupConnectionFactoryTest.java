begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rabbitmq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rabbitmq
package|;
end_package

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|ConnectionFactory
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
name|JndiRegistry
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|RabbitMQLookupConnectionFactoryTest
specifier|public
class|class
name|RabbitMQLookupConnectionFactoryTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|myConnectionFactory
specifier|private
name|ConnectionFactory
name|myConnectionFactory
decl_stmt|;
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|myConnectionFactory
operator|=
operator|new
name|ConnectionFactory
argument_list|()
expr_stmt|;
name|myConnectionFactory
operator|.
name|setHost
argument_list|(
literal|"myhost"
argument_list|)
expr_stmt|;
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"myConnectionFactory"
argument_list|,
name|myConnectionFactory
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Test
DECL|method|testLookupConnectionFactory ()
specifier|public
name|void
name|testLookupConnectionFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|RabbitMQEndpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"rabbitmq:myexchange"
argument_list|,
name|RabbitMQEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|endpoint
operator|.
name|getConnectionFactory
argument_list|()
argument_list|,
name|myConnectionFactory
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

