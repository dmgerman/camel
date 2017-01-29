begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeoutException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|Connection
import|;
end_import

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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_class
DECL|class|AbstractRabbitMQIntTest
specifier|public
specifier|abstract
class|class
name|AbstractRabbitMQIntTest
extends|extends
name|CamelTestSupport
block|{
comment|/**      * Helper method for creating a rabbitmq connection to the test instance of the      * rabbitmq server.      * @return      * @throws IOException      * @throws TimeoutException      */
DECL|method|connection ()
specifier|protected
name|Connection
name|connection
parameter_list|()
throws|throws
name|IOException
throws|,
name|TimeoutException
block|{
name|ConnectionFactory
name|factory
init|=
operator|new
name|ConnectionFactory
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setHost
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setPort
argument_list|(
literal|5672
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setUsername
argument_list|(
literal|"cameltest"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setPassword
argument_list|(
literal|"cameltest"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setVirtualHost
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
return|return
name|factory
operator|.
name|newConnection
argument_list|()
return|;
block|}
block|}
end_class

end_unit

