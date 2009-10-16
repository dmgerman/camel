begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
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
name|ContextTestSupport
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
name|Exchange
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
name|Producer
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
name|ProcessorEndpoint
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|BeanComponentCustomCreateEndpointTest
specifier|public
class|class
name|BeanComponentCustomCreateEndpointTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testCreateEndpoint ()
specifier|public
name|void
name|testCreateEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanComponent
name|bc
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"bean"
argument_list|,
name|BeanComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|ProcessorEndpoint
name|pe
init|=
name|bc
operator|.
name|createEndpoint
argument_list|(
operator|new
name|MyFooBean
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pe
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|pe
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bean:generated:MyFooBean"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|Producer
name|producer
init|=
name|pe
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"World"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateEndpointUri ()
specifier|public
name|void
name|testCreateEndpointUri
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanComponent
name|bc
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"bean"
argument_list|,
name|BeanComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|ProcessorEndpoint
name|pe
init|=
name|bc
operator|.
name|createEndpoint
argument_list|(
operator|new
name|MyFooBean
argument_list|()
argument_list|,
literal|"cheese"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pe
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|pe
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|Producer
name|producer
init|=
name|pe
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"World"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

