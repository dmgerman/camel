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
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
operator|.
name|XPathBuilder
operator|.
name|xpath
import|;
end_import

begin_class
DECL|class|XPathFeatureTest
specifier|public
class|class
name|XPathFeatureTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|DOM_BUILER_FACTORY_FEATRUE
specifier|public
specifier|static
specifier|final
name|String
name|DOM_BUILER_FACTORY_FEATRUE
init|=
name|XmlConverter
operator|.
name|DOCUMENT_BUILDER_FACTORY_FEATURE
decl_stmt|;
DECL|field|XML_DATA
specifier|public
specifier|static
specifier|final
name|String
name|XML_DATA
init|=
literal|"<!DOCTYPE foo [ "
operator|+
literal|"<!ELEMENT foo ANY><!ENTITY xxe SYSTEM \"file:///bin/test.sh\">]><test>&xxe;</test>"
decl_stmt|;
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
DECL|method|testXPathResult ()
specifier|public
name|void
name|testXPathResult
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Set this feature will disable the external general entities
name|System
operator|.
name|setProperty
argument_list|(
name|DOM_BUILER_FACTORY_FEATRUE
operator|+
literal|":"
operator|+
literal|"http://xml.org/sax/features/external-general-entities"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|String
name|result
init|=
operator|(
name|String
operator|)
name|xpath
argument_list|(
literal|"/"
argument_list|)
operator|.
name|stringResult
argument_list|()
operator|.
name|evaluate
argument_list|(
name|createExchange
argument_list|(
name|XML_DATA
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong result"
argument_list|,
literal|"  "
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
name|DOM_BUILER_FACTORY_FEATRUE
operator|+
literal|":"
operator|+
literal|"http://xml.org/sax/features/external-general-entities"
argument_list|)
expr_stmt|;
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
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

