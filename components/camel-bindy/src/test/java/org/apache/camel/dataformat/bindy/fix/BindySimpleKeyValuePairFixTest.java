begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.fix
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|fix
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|EndpointInject
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
name|apache
operator|.
name|camel
operator|.
name|Produce
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
name|ProducerTemplate
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
name|builder
operator|.
name|RouteBuilder
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|KeyValuePairField
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
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|Message
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
name|dataformat
operator|.
name|bindy
operator|.
name|kvp
operator|.
name|BindyKeyValuePairDataFormat
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
name|spi
operator|.
name|DataFormat
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|annotation
operator|.
name|DirtiesContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
DECL|class|BindySimpleKeyValuePairFixTest
specifier|public
class|class
name|BindySimpleKeyValuePairFixTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|FIX_REQUESTS
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|FIX_REQUESTS
init|=
operator|new
name|String
index|[]
block|{
literal|"8=FIX.4.1 37=1 38=1 40=butter"
block|,
literal|"8=FIX.4.1 37=2 38=2 40=milk"
block|,
literal|"8=FIX.4.1 37=3 38=3 40=bread"
block|}
decl_stmt|;
DECL|field|FIX_RESPONSES
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|FIX_RESPONSES
init|=
operator|new
name|String
index|[]
block|{
literal|"37=1 38=2 40=butter \r\n"
block|,
literal|"37=2 38=4 40=milk \r\n"
block|,
literal|"37=3 38=6 40=bread \r\n"
block|}
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:fix"
argument_list|)
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|mock
specifier|private
name|MockEndpoint
name|mock
decl_stmt|;
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testUnMarshallMessage ()
specifier|public
name|void
name|testUnMarshallMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
name|FIX_RESPONSES
operator|.
name|length
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|FIX_RESPONSES
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|request
range|:
name|FIX_REQUESTS
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:fix"
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|class|ContextConfig
specifier|public
specifier|static
class|class
name|ContextConfig
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|DataFormat
name|bindy
init|=
operator|new
name|BindyKeyValuePairDataFormat
argument_list|(
name|FixOrder
operator|.
name|class
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"direct:fix"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|bindy
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|FixOrder
name|order
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|FixOrder
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|order
operator|.
name|getProduct
argument_list|()
operator|.
name|equals
argument_list|(
literal|"butter"
argument_list|)
condition|)
block|{
name|order
operator|.
name|setQuantity
argument_list|(
literal|"2"
argument_list|)
expr_stmt|;
name|body
operator|=
name|order
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|order
operator|.
name|getProduct
argument_list|()
operator|.
name|equals
argument_list|(
literal|"milk"
argument_list|)
condition|)
block|{
name|order
operator|.
name|setQuantity
argument_list|(
literal|"4"
argument_list|)
expr_stmt|;
name|body
operator|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|order
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|order
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|order
operator|.
name|getProduct
argument_list|()
operator|.
name|equals
argument_list|(
literal|"bread"
argument_list|)
condition|)
block|{
name|order
operator|.
name|setQuantity
argument_list|(
literal|"6"
argument_list|)
expr_stmt|;
name|body
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
name|order
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|order
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|marshal
argument_list|(
name|bindy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Message
argument_list|(
name|keyValuePairSeparator
operator|=
literal|"="
argument_list|,
name|pairSeparator
operator|=
literal|" "
argument_list|,
name|type
operator|=
literal|"FIX"
argument_list|,
name|version
operator|=
literal|"4.1"
argument_list|)
DECL|class|FixOrder
specifier|public
specifier|static
class|class
name|FixOrder
block|{
annotation|@
name|KeyValuePairField
argument_list|(
name|tag
operator|=
literal|37
argument_list|)
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
annotation|@
name|KeyValuePairField
argument_list|(
name|tag
operator|=
literal|40
argument_list|)
DECL|field|product
specifier|private
name|String
name|product
decl_stmt|;
annotation|@
name|KeyValuePairField
argument_list|(
name|tag
operator|=
literal|38
argument_list|)
DECL|field|quantity
specifier|private
name|String
name|quantity
decl_stmt|;
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|getProduct ()
specifier|public
name|String
name|getProduct
parameter_list|()
block|{
return|return
name|product
return|;
block|}
DECL|method|getQuantity ()
specifier|public
name|String
name|getQuantity
parameter_list|()
block|{
return|return
name|quantity
return|;
block|}
DECL|method|setQuantity (String quantity)
specifier|public
name|void
name|setQuantity
parameter_list|(
name|String
name|quantity
parameter_list|)
block|{
name|this
operator|.
name|quantity
operator|=
name|quantity
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

