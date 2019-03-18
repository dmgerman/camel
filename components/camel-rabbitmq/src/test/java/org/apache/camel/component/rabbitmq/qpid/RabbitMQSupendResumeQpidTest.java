begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rabbitmq.qpid
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
operator|.
name|qpid
package|;
end_package

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
name|rabbitmq
operator|.
name|RabbitMQSupendResumeIntTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_class
DECL|class|RabbitMQSupendResumeQpidTest
specifier|public
class|class
name|RabbitMQSupendResumeQpidTest
extends|extends
name|RabbitMQSupendResumeIntTest
block|{
annotation|@
name|BeforeClass
DECL|method|startBroker ()
specifier|public
specifier|static
name|void
name|startBroker
parameter_list|()
throws|throws
name|Exception
block|{
name|systemLauncher
operator|.
name|startup
argument_list|(
name|createQpidSystemConfig
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|stopBroker ()
specifier|public
specifier|static
name|void
name|stopBroker
parameter_list|()
block|{
name|systemLauncher
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

