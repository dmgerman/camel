begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
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
name|junit
operator|.
name|Ignore
import|;
end_import

begin_comment
comment|//import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
end_comment

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
annotation|@
name|Ignore
argument_list|(
literal|"For manual testing CAMEL-6922"
argument_list|)
DECL|class|NodeListToDocumentTest
specifier|public
class|class
name|NodeListToDocumentTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testXPathNodeResultToDocument ()
specifier|public
name|void
name|testXPathNodeResultToDocument
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO: uses an internal nexus class which can only be tested on some platforms
comment|/*         Object result = xpath("/foo").nodeResult().evaluate(createExchange("<foo><bar>1</bar><bar>2</bar></foo>"));         ElementNSImpl el = assertIsInstanceOf(ElementNSImpl.class, result);         assertNotNull(el);         NodeList nodeList = (NodeList) el;         assertEquals(0, nodeList.getLength());         Document doc = context.getTypeConverter().convertTo(Document.class, nodeList);         assertNotNull(doc);         assertEquals("foo", doc.getFirstChild().getLocalName());         */
block|}
DECL|method|createExchange (Object xml)
specifier|protected
name|Exchange
name|createExchange
parameter_list|(
name|Object
name|xml
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
name|context
argument_list|,
name|xml
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"name"
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

