begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|TokenXMLPairMultiNamespaceSplitTest
specifier|public
class|class
name|TokenXMLPairMultiNamespaceSplitTest
extends|extends
name|TokenXMLPairNamespaceSplitTest
block|{
DECL|method|testTokenXMLPair ()
specifier|public
name|void
name|testTokenXMLPair
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"<order id=\"1\" xmlns=\"http:acme.com\" xmlns:foo=\"http:foo.com\">Camel in Action</order>"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|1
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"<order id=\"2\" xmlns=\"http:acme.com\" xmlns:foo=\"http:foo.com\">ActiveMQ in Action</order>"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|2
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"<order id=\"3\" xmlns=\"http:acme.com\" xmlns:foo=\"http:foo.com\">DSL in Action</order>"
argument_list|)
expr_stmt|;
name|String
name|body
init|=
name|createBody
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/pair"
argument_list|,
name|body
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"orders.xml"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testTokenXMLPair2 ()
specifier|public
name|void
name|testTokenXMLPair2
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|createBody ()
specifier|protected
name|String
name|createBody
parameter_list|()
block|{
comment|// multiple namespaces on parent on the same line
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"<?xml version=\"1.0\"?>\n"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<orders xmlns=\"http:acme.com\" xmlns:foo=\"http:foo.com\">\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<order id=\"1\">Camel in Action</order>\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<order id=\"2\">ActiveMQ in Action</order>\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<order id=\"3\">DSL in Action</order>\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"</orders>"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

