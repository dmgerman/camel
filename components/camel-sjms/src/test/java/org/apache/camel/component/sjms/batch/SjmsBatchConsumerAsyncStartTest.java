begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.batch
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|batch
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
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
name|sjms
operator|.
name|SjmsComponent
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
name|sjms
operator|.
name|support
operator|.
name|MockConnectionFactory
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
name|support
operator|.
name|SimpleRegistry
import|;
end_import

begin_class
DECL|class|SjmsBatchConsumerAsyncStartTest
specifier|public
class|class
name|SjmsBatchConsumerAsyncStartTest
extends|extends
name|SjmsBatchConsumerTest
block|{
comment|// lets just test that any of the existing tests works
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|public
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"testStrategy"
argument_list|,
operator|new
name|ListAggregationStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|ConnectionFactory
name|connectionFactory
init|=
operator|new
name|MockConnectionFactory
argument_list|(
name|broker
operator|.
name|getTcpConnectorUri
argument_list|()
argument_list|)
decl_stmt|;
name|SjmsComponent
name|sjmsComponent
init|=
operator|new
name|SjmsComponent
argument_list|()
decl_stmt|;
name|sjmsComponent
operator|.
name|setConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
name|SjmsBatchComponent
name|sjmsBatchComponent
init|=
operator|new
name|SjmsBatchComponent
argument_list|()
decl_stmt|;
name|sjmsBatchComponent
operator|.
name|setConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
comment|// turn on async start listener
name|sjmsBatchComponent
operator|.
name|setAsyncStartListener
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"sjms"
argument_list|,
name|sjmsComponent
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"sjms-batch"
argument_list|,
name|sjmsBatchComponent
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

