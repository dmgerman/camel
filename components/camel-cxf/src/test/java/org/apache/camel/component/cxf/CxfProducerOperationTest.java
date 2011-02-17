begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
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
name|Processor
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|CxfProducerOperationTest
specifier|public
class|class
name|CxfProducerOperationTest
extends|extends
name|CxfProducerTest
block|{
DECL|field|NAMESPACE
specifier|private
specifier|static
specifier|final
name|String
name|NAMESPACE
init|=
literal|"http://apache.org/hello_world_soap_http"
decl_stmt|;
DECL|method|getSimpleEndpointUri ()
specifier|protected
name|String
name|getSimpleEndpointUri
parameter_list|()
block|{
return|return
literal|"cxf://"
operator|+
name|SIMPLE_SERVER_ADDRESS
operator|+
literal|"?serviceClass=org.apache.camel.component.cxf.HelloService"
operator|+
literal|"&defaultOperationName="
operator|+
name|ECHO_OPERATION
return|;
block|}
DECL|method|getJaxwsEndpointUri ()
specifier|protected
name|String
name|getJaxwsEndpointUri
parameter_list|()
block|{
return|return
literal|"cxf://"
operator|+
name|JAXWS_SERVER_ADDRESS
operator|+
literal|"?serviceClass=org.apache.hello_world_soap_http.Greeter"
operator|+
literal|"&defaultOperationName="
operator|+
name|GREET_ME_OPERATION
operator|+
literal|"&defaultOperationNamespace="
operator|+
name|NAMESPACE
return|;
block|}
DECL|method|sendSimpleMessage ()
specifier|protected
name|Exchange
name|sendSimpleMessage
parameter_list|()
block|{
return|return
name|sendSimpleMessage
argument_list|(
name|getSimpleEndpointUri
argument_list|()
argument_list|)
return|;
block|}
DECL|method|sendSimpleMessage (String endpointUri)
specifier|private
name|Exchange
name|sendSimpleMessage
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
name|endpointUri
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|params
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|params
operator|.
name|add
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"testFile"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|sendJaxWsMessage ()
specifier|protected
name|Exchange
name|sendJaxWsMessage
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
name|getJaxwsEndpointUri
argument_list|()
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|params
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|params
operator|.
name|add
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"testFile"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|exchange
return|;
block|}
annotation|@
name|Test
DECL|method|testSendingComplexParameter ()
specifier|public
name|void
name|testSendingComplexParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
name|getSimpleEndpointUri
argument_list|()
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// we need to override the operation name first
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|para1
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|para1
operator|.
name|add
argument_list|(
literal|"para1"
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|para2
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|para2
operator|.
name|add
argument_list|(
literal|"para2"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|List
argument_list|>
name|parameters
init|=
operator|new
name|ArrayList
argument_list|<
name|List
argument_list|>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|add
argument_list|(
name|para1
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|add
argument_list|(
name|para2
argument_list|)
expr_stmt|;
comment|// The object array version is working too
comment|// Object[] parameters = new Object[] {para1, para2};
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
literal|"complexParameters"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|exchange
operator|.
name|getException
argument_list|()
throw|;
block|}
name|assertEquals
argument_list|(
literal|"Get a wrong response."
argument_list|,
literal|"param:para1para2"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

